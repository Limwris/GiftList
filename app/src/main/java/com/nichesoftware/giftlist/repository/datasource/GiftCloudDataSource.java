package com.nichesoftware.giftlist.repository.datasource;

import com.google.gson.Gson;
import com.nichesoftware.giftlist.model.Gift;
import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.model.User;
import com.nichesoftware.giftlist.service.Service;
import com.nichesoftware.giftlist.utils.StringUtils;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * {@link CloudDataSource} implementation for {@link Gift}.
 */
public class GiftCloudDataSource extends ConnectedDataSource<Gift> {

    // Fields
    private String mRoomId;

    /**
     * Public constructor
     *
     * @param token         {@link User} token
     * @param service       The bound {@link Service}
     * @param roomId        Id of the parent {@link Room}
     */
    public GiftCloudDataSource(final String token, final Service service, String roomId) {
        super(token, service);
        mRoomId = roomId;
    }

    @Override
    public Observable<Gift> add(Gift element) {
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), gson.toJson(element));

        MultipartBody.Part fileBody = null;
        if (!StringUtils.isEmpty(element.getImageUrl())) {
            File file = new File(element.getImageUrl());
            if (file.exists()) {
                // create RequestBody instance from file
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                // MultipartBody.Part is used to send also the actual file name
                fileBody = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            }
        }
        return mService.addGift(mToken, fileBody, requestBody);
    }

    @Override
    public Observable<List<Gift>> getAll() {
        Room room = new Room(mRoomId);
        return mService.getGifts(mToken, room);
    }

    @Override
    public Observable<Gift> get(String id) {
        return mService.getGift(mToken, id);
    }

    @Override
    public Observable<Gift> update(Gift element) {
        return mService.updateGift(mToken, element);
    }

    @Override
    public Observable<List<Gift>> delete(Gift element) {
        throw new UnsupportedOperationException("This operation is not supported right now...");
    }
}
