package com.nichesoftware.giftlist.service;

import com.nichesoftware.giftlist.model.Gift;
import com.nichesoftware.giftlist.model.Invitation;
import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.model.User;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Rest API for retrieving data from the network.
 */
public interface Service {
    // region User
    @POST("authentication")
    Observable<User> authenticate(@Body final User user);

    @POST("register")
    Observable<User> register(@Body final User user);

    @POST("invite")
    Observable<Boolean> inviteUserToRoom(@Body final Invitation invitation);

//    @POST("contacts")
//    Observable<List<User>> retreiveAvailableContacts(@Header("X-Auth-Token") final String token,
//                                               @Body final ContactDto contactDto);
//

    // TODO: 24/03/2017 From GCMRegistrationDTO to String...
    @POST("gcm")
    Observable<Boolean> registerDevice(@Body final String registrationId);
    // endregion

    // region Room
    @GET("rooms")
    Observable<List<Room>> getAllRooms();

    @POST("room")
    Observable<Room> createRoom(@Body final Room room);

    @HTTP(method = "DELETE", path = "room", hasBody = true)
    Observable<List<Room>> leaveRoom(@Body final Room room);
    // endregion

    // region Gift
    @GET("gifts")
    Observable<List<Gift>> getGifts(@Body final Room room);

    @GET("gift/{id}")
    Observable<Gift> getGift(@Path("id") final String giftId);

    @Multipart
    @POST("gift")
    Observable<Gift> addGift(@Part MultipartBody.Part file,
                             @Part("body") final RequestBody gift);

    @PUT("gift")
    Observable<Gift> updateGift(@Body final Gift gift);
    // endregion

//    @Multipart
//    @POST("giftfile")
//    Observable<Void> updateGifFile(@Header("X-Auth-Token") final String token,
//                             @Part MultipartBody.Part file,
//                             @Part("body") final RequestBody giftDto);
//
//    @POST("contacts")
//    Observable<List<User>> retreiveAvailableContacts(@Header("X-Auth-Token") final String token,
//                                               @Body final ContactDto contactDto);
//
//    @GET("invite")
//    Observable<List<Room>> getPendingInvitation(@Header("X-Auth-Token") String token);
//
//    @POST("accept")
//    Observable<Boolean> acceptInvitationToRoom(@Header("X-Auth-Token") String token,
//                                         @Body final RoomDto roomDto);
//
//    @POST("gcm")
//    Observable<Boolean> registerDevice(@Header("X-Auth-Token") String token,
//                                 @Body final GCMRegistrationDto registrationDto);
//
//    @Multipart
//    @POST("upload")
//    Observable<ResponseBody> upload(@Part("description") RequestBody description,
//                              @Part MultipartBody.Part file);
//
//    @GET("file")
//    Observable<ResponseBody> getImageFile(@Header("X-Auth-Token") String token,
//                                    @Query("giftId") int giftId);
}
