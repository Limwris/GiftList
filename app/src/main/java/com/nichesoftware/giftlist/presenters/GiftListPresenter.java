package com.nichesoftware.giftlist.presenters;

import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.Log;

import com.nichesoftware.giftlist.R;
import com.nichesoftware.giftlist.contracts.GiftListContract;
import com.nichesoftware.giftlist.model.Gift;
import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.repository.cache.Cache;
import com.nichesoftware.giftlist.repository.datasource.AuthDataSource;
import com.nichesoftware.giftlist.repository.datasource.CloudDataSource;
import com.nichesoftware.giftlist.repository.datasource.DataSource;
import com.nichesoftware.giftlist.repository.provider.DataSourceProvider;
import com.nichesoftware.giftlist.session.SessionManager;
import com.nichesoftware.giftlist.utils.ResourcesUtils;
import com.nichesoftware.giftlist.views.giftlist.GiftListDetailVO;
import com.nichesoftware.giftlist.views.giftlist.GiftVO;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Gifts list presenter
 */
public class GiftListPresenter extends BasePresenter<GiftListContract.View, Gift>
        implements GiftListContract.Presenter {
    /// Constants   ---------------------------------------------------------------------------------
    private static final String TAG = GiftListPresenter.class.getSimpleName();
    private static final String PRICE_DECIMAL_PATTERN = "#0.00";
    private static final String AMOUNT_REGEX_PATTERN = "[.|,\\s]\\s*\\d+€$";

    /// Fields
    // Data provider for {@link Room}
    private final DataSource<Room> mRoomDataProvider;
    // Cache for {@link Room}
    private final Cache<Room> mRoomCache;
    // Subscription RX
    private Disposable mLoadGiftsSubscription, mLoadGiftDetailSubscription, mLeaveRoomSubscription;
    // Decimal format (price)
    private final DecimalFormat mDecimalFormat;
    // Pattern
    private final Pattern mAmountPattern;

    /**
     * Constructor
     *
     * @param view                      View to attach
     * @param giftCache                 Cache for {@link Gift}
     * @param giftConnectedDataSource   The cloud data provider for {@link Gift}
     * @param roomCache                 Cache for {@link Room}
     * @param roomConnectedDataSource   The cloud data provider for {@link Room}
     * @param authDataSource            Authentication data source
     */
    public GiftListPresenter(@NonNull GiftListContract.View view, @NonNull Cache<Gift> giftCache,
                             @NonNull CloudDataSource<Gift> giftConnectedDataSource,
                             @NonNull Cache<Room> roomCache,
                             @NonNull CloudDataSource<Room> roomConnectedDataSource,
                             @NonNull AuthDataSource authDataSource) {
        super(view, giftCache, giftConnectedDataSource, authDataSource);
        mRoomCache = roomCache;
        mRoomDataProvider = new DataSourceProvider<>(mRoomCache, roomConnectedDataSource);
        mDecimalFormat = new DecimalFormat(PRICE_DECIMAL_PATTERN);
        mAmountPattern = Pattern.compile(AMOUNT_REGEX_PATTERN);
    }

    // region Lifecycle methods
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLoadGiftsSubscription != null && !mLoadGiftsSubscription.isDisposed()) {
            mLoadGiftsSubscription.dispose();
        }
        if (mLoadGiftDetailSubscription != null && !mLoadGiftDetailSubscription.isDisposed()) {
            mLoadGiftDetailSubscription.dispose();
        }
        if (mLeaveRoomSubscription != null && !mLeaveRoomSubscription.isDisposed()) {
            mLeaveRoomSubscription.dispose();
        }
    }
    // endregion

    // region Presenter contract implementation
    @Override
    public void loadGifts(final String roomId, boolean forceUpdate) {
        if (mLoadGiftsSubscription != null && !mLoadGiftsSubscription.isDisposed()) {
            mLoadGiftsSubscription.dispose();
        }
        mLoadGiftsSubscription = Observable.defer(() -> {
            if (forceUpdate) {
                mCache.evictAll();
            }
            return mDataProvider.getAll();
        }).observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mAttachedView.showLoader())
                .doFinally(() -> mAttachedView.hideLoader())
                .map(this::map)
                .subscribe(gifts -> mAttachedView.showGifts(gifts),
                        throwable -> {
                            Log.e(TAG, "loadGifts: onError", throwable);
                            mAttachedView.showError(throwable.getMessage());
                        });
    }

    @Override
    public void openGiftDetail(String giftId) {
        Log.d(TAG, String.format("Le cadeau [id=%s] a été cliqué...", giftId));
        if (mLoadGiftDetailSubscription != null && !mLoadGiftDetailSubscription.isDisposed()) {
            mLoadGiftDetailSubscription.dispose();
        }
        mLoadGiftDetailSubscription = mDataProvider.get(giftId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mAttachedView.showLoader())
                .doFinally(() -> mAttachedView.hideLoader())
                .subscribe(gift -> mAttachedView.showGiftDetail(gift),
                        throwable -> mAttachedView.showError(throwable.getMessage()));
    }

    @Override
    public void leaveCurrentRoom(String roomId) {
        Room leavingRoom = new Room(roomId);
        if (mLeaveRoomSubscription != null && !mLeaveRoomSubscription.isDisposed()) {
            mLeaveRoomSubscription.dispose();
        }
        mLeaveRoomSubscription = mRoomDataProvider.delete(leavingRoom)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mAttachedView.showLoader())
                .doFinally(() -> mAttachedView.hideLoader())
                .subscribe(rooms -> {
                            Log.d(TAG, "leaveCurrentRoom: onNext");
                            mAttachedView.onLeaveRoomSuccess();
                        },
                        throwable -> {
                            Log.e(TAG, "leaveCurrentRoom: onError", throwable);
                            mAttachedView.onLeaveRoomError();
                            mAttachedView.showError(throwable.getMessage());
                        },
                        () -> Log.d(TAG, "leaveCurrentRoom: onComplete"));
    }

    @Override
    public boolean isInvitationAvailable() {
        return SessionManager.getInstance().isConnected();
    }
    // endregion

    // region Private methods
    /**
     * Mapping {@link Gift} to {@link GiftVO}
     * @param gifts     The list of {@link Gift} to map
     * @return          The corresponding list of {@link GiftVO}
     */
    private List<GiftVO> map(List<Gift> gifts) {
        List<GiftVO> giftVOs = new ArrayList<>();
        for (Gift gift : gifts) {
            SpannableString spannablePrice = getSpannableStringFromDouble(gift.getPrice());
            SpannableString spannableAmount = getSpannableStringFromDouble(gift.getAmount());
            SpannableString spannableRemainder = getSpannableStringFromDouble(gift.getRemainder());

            GiftVO giftVO = new GiftVO(gift.getId(), spannablePrice, spannableAmount,
                    gift.getName(), gift.getImageUrl(), spannableRemainder);
            List<GiftListDetailVO> detailVOs = new ArrayList<>();
            for (Map.Entry<String, Double> entry : gift.getAmountByUser().entrySet()) {
                GiftListDetailVO vo = new GiftListDetailVO();
                vo.setUsername(entry.getKey());
                double participation = entry.getValue();
                SpannableString spannableAmountPerUser = getSpannableStringFromDouble(participation);
                vo.setParticipation(spannableAmountPerUser);
                detailVOs.add(vo);
            }
            giftVO.setDetailVO(detailVOs);
            giftVOs.add(giftVO);
        }
        return giftVOs;
    }

    /**
     * Utiliy methods which formats amount fields (price, amout, amountPerUser...)
     *
     * @param aDouble       The amount to format
     * @return              The amount well-formatted in a {@link SpannableString}
     */
    @NonNull
    private SpannableString getSpannableStringFromDouble(final Double aDouble) {
        String formattedString = String.format(ResourcesUtils.getString(R.string.euros_amount),
                mDecimalFormat.format(aDouble));
        SpannableString spannableString = new SpannableString(formattedString);
        Matcher matcher = mAmountPattern.matcher(formattedString);
        if (matcher.find()) {
            spannableString.setSpan(new RelativeSizeSpan(0.5f),
                    matcher.start(), matcher.end(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        return spannableString;
    }
    // endregion
}
