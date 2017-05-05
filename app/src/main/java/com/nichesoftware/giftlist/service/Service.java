package com.nichesoftware.giftlist.service;

import com.nichesoftware.giftlist.model.Contacts;
import com.nichesoftware.giftlist.model.Gift;
import com.nichesoftware.giftlist.model.Invitation;
import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.model.User;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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

    // TODO: 24/03/2017 From GCMRegistrationDTO to String...
    @POST("gcm")
    Observable<Boolean> registerDevice(@Body final String registrationId);
    // endregion

    // region Invitation
//    @POST("invite")
//    Observable<Boolean> inviteUserToRoom(@Body final Invitation invitation);
//
//    @POST("contacts")
//    Observable<List<User>> retreiveAvailableContacts(@Header("X-Auth-Token") final String token,
//                                               @Body final ContactDto contactDto);
//
//    @GET("invite")
//    Observable<List<Room>> getPendingInvitation(@Header("X-Auth-Token") String token);
    @GET("invite")
    Observable<List<Invitation>> getPendingInvitation();

    @POST("/rooms/{id}/invite")
    Observable<Invitation> inviteUserToRoom(@Path("id") final String roomId, @Body final Invitation invitation);

    @POST("/rooms/{id}/invite")
    Observable<List<Invitation>> inviteUsersToRoom(@Path("id") final String roomId, @Body final List<Invitation> invitations);

    @PUT("/rooms/{id}/invite")
    Observable<Invitation> acceptInvitationToRoom(@Path("id") final String roomId, @Body final Invitation invitation);

    @DELETE("/rooms/{id}/invite")
    Observable<List<Invitation>> declineInvitation(@Path("id") final String roomId, @Body final Invitation invitation);

    @POST("/rooms/{id}/contacts")
    Observable<List<User>> retreiveAvailableContacts(@Path("id") final String roomId, @Body final Contacts contacts);
    // endregion

    // region Room
    @GET("rooms")
    Observable<List<Room>> getAllRooms();

    @GET("rooms/{id}")
    Observable<Room> getRoom(@Path("id") final String roomId);

    @POST("rooms")
    Observable<Room> createRoom(@Body final Room room);

    @PUT("rooms/{id}")
    Observable<Room> updateRoom(@Path("id") final String roomId,
                                @Body Room room);

    @DELETE("rooms/{id}")
    Observable<List<Room>> leaveRoom(@Path("id") final String roomId);
    // endregion

    // region Gift
    @GET("/rooms/{roomId}/gifts")
    Observable<List<Gift>> getGifts(@Path("roomId") final String roomId);

    @GET("/rooms/{roomId}/gifts/{giftId}")
    Observable<Gift> getGift(@Path("roomId") final String roomId, @Path("giftId") final String giftId);

    @Multipart
    @POST("/rooms/{roomId}/gifts")
    Observable<Gift> addGift(@Path("roomId") final String roomId,
                             @Part MultipartBody.Part file,
                             @Part("gift") final RequestBody gift);
//    Observable<Gift> addGift(@Part MultipartBody.Part file,
//                             @PartMap() final Map<String, RequestBody> map);

    @POST("/rooms/{roomId}/gifts")
    Observable<Gift> addGift(@Path("roomId") final String roomId,
                             @Body Gift gift);

    @PUT("/rooms/{roomId}/gifts/{id}")
    Observable<Gift> updateGift(@Path("roomId") final String roomId,
                                @Path("id") final String giftId,
                                @Body final Gift gift);

    @DELETE("/rooms/{roomId}/gifts/{id}")
    Observable<List<Gift>> deleteGift(@Path("roomId") final String roomId,
                                      @Path("giftId") final String giftId);
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
