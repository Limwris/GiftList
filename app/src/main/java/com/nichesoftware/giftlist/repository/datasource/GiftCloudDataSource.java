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
public class GiftCloudDataSource extends CloudDataSource<Gift> {
    // Constants
    private static final String MEDIATYPE_MULTIPART_FORM_DATA = "multipart/form-data";
    private static final String MEDIATYPE_APPLICATION_JSON = "application/json";
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
        Gson gson = new Gson();
        // TODO: 29/03/2017 Which MediaType should I choose ?
//        RequestBody requestBody = RequestBody.create(MediaType.parse(MEDIATYPE_MULTIPART_FORM_DATA), gson.toJson(element));
        RequestBody requestBody = RequestBody.create(MediaType.parse(MEDIATYPE_APPLICATION_JSON), gson.toJson(element));

        MultipartBody.Part fileBody = null;
        if (!StringUtils.isEmpty(element.getImageUrl())) {
            File file = new File(element.getImageUrl());
            if (file.exists()) {
                // create RequestBody instance from file
                RequestBody requestFile = RequestBody.create(MediaType.parse(MEDIATYPE_MULTIPART_FORM_DATA), file);

                // MultipartBody.Part is used to send also the actual file name
                fileBody = MultipartBody.Part.createFormData(MEDIATYPE_FILE, file.getName(), requestFile);
            }
        }
        return mService.addGift(fileBody, requestBody);
    }

    @Override
    public Observable<List<Gift>> getAll() {
        Room room = new Room(mRoomId);
        return mService.getGifts(room);
    }

    @Override
    public Observable<Gift> get(String id) {
        return mService.getGift(id);
    }

    @Override
    public Observable<Gift> update(Gift element) {
        return mService.updateGift(element);
    }

    @Override
    public Observable<List<Gift>> delete(Gift element) {
        throw new UnsupportedOperationException("This operation is not supported right now...");
    }
}
