package com.nichesoftware.giftlist.repository.datasource;

import com.google.gson.Gson;
import com.nichesoftware.giftlist.model.Gift;
import com.nichesoftware.giftlist.model.Room;
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
public class GiftCloudDataSource extends CloudDataSource<Gift> {
    // Constants
    private static final String MEDIATYPE_MULTIPART_FORM_DATA = "multipart/form-data";
    private static final String MEDIATYPE_APPLICATION_JSON = "application/json";
    private static final String MEDIATYPE_IMAGE = "image/*";
    private static final String MEDIATYPE_FILE = "file";

    // Fields
    private String mRoomId;

    /**
     * Public constructor
     *
     * @param service       The bound {@link Service}
     * @param roomId        Id of the parent {@link Room}
     */
    public GiftCloudDataSource(final Service service, String roomId) {
        super(service);
        mRoomId = roomId;
    }

    @Override
    public Observable<Gift> add(Gift element) {
        if (element.isImageFileLocal() && !StringUtils.isEmpty(element.getImageUrl())) {
            Gson gson = new Gson();
//            RequestBody giftRequestBody = RequestBody.create(MediaType.parse(MEDIATYPE_APPLICATION_JSON), gson.toJson(element));
            RequestBody giftRequestBody = RequestBody.create(MultipartBody.FORM, gson.toJson(element));

            MultipartBody.Part fileBody = null;
            File file = new File(element.getImageUrl());
            if (file.exists()) {
                // create RequestBody instance from file
//                RequestBody requestFile = RequestBody.create(MultipartBody.FORM, file);
                RequestBody requestFile = RequestBody.create(MediaType.parse(MEDIATYPE_IMAGE), file);

                // MultipartBody.Part is used to send also the actual file name
                fileBody = MultipartBody.Part.createFormData(MEDIATYPE_FILE, file.getName(), requestFile);
            }
            return mService.addGift(mRoomId, fileBody, giftRequestBody);
        } else {
            return mService.addGift(mRoomId, element);
        }
    }

    @Override
    public Observable<List<Gift>> getAll() {
        return mService.getGifts(mRoomId);
    }

    @Override
    public Observable<Gift> get(String id) {
        return mService.getGift(mRoomId, id);
    }

    @Override
    public Observable<Gift> update(Gift element) {
        return mService.updateGift(mRoomId, element.getId(), element);
    }

    @Override
    public Observable<List<Gift>> delete(Gift element) {
        return mService.deleteGift(mRoomId, element.getId());
    }
}
