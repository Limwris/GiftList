package com.nichesoftware.giftlist;

import com.nichesoftware.giftlist.dto.AcceptInvitationDto;
import com.nichesoftware.giftlist.dto.ContactDto;
import com.nichesoftware.giftlist.dto.GiftDto;
import com.nichesoftware.giftlist.dto.InvitationDto;
import com.nichesoftware.giftlist.dto.RoomDto;
import com.nichesoftware.giftlist.dto.UserDto;
import com.nichesoftware.giftlist.model.Gift;
import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by n_che on 06/06/2016.
 */
public interface RestServiceEndpoint {
    @GET("rooms")
    Call<List<Room>> getAllRooms(@Header("X-Auth-Token") final String token);

    @POST("gifts")
    Call<List<Gift>> getGifts(@Header("X-Auth-Token") final String token, @Body final RoomDto roomDto);

    @POST("authentication")
    Call<String> authenticate(@Body final UserDto userDto);

    @POST("register")
    Call<String> register(@Body final UserDto userDto);

    @POST("room")
    Call<Room> createRoom(@Header("X-Auth-Token") final String token, @Body final RoomDto roomDto);

    @GET("room/{roomId}")
    Call<Room> getRoomInformation(@Header("X-Auth-Token") final String token,
                                  @Path("roomId") final int roomId);

    @POST("gift")
    Call<Gift> addGift(@Header("X-Auth-Token") final String token, @Body final GiftDto giftDto);

    @PUT("gift")
    Call<Gift> updateGift(@Header("X-Auth-Token") final String token, @Body final GiftDto giftDto);

    @POST("contacts")
    Call<List<User>> retreiveAvailableContacts(@Header("X-Auth-Token") final String token,
                                               @Body final ContactDto contactDto);

    @POST("invite")
    Call<Boolean> inviteUserToRoom(@Header("X-Auth-Token") String token,
                                   @Body final InvitationDto invitationDto);

    @POST("accept")
    Call<Boolean> acceptInvitationToRoom(@Header("X-Auth-Token") String token,
                                   @Body final AcceptInvitationDto acceptInvitationDto);

    @POST("gcm/{registerId}")
    Call<Boolean> registerDevice(@Header("X-Auth-Token") String token,
                                 @Path("registerId") final String registerId);
}
