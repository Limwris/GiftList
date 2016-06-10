package com.nichesoftware.giftlist;

import com.nichesoftware.giftlist.dto.GiftDto;
import com.nichesoftware.giftlist.dto.RoomDto;
import com.nichesoftware.giftlist.dto.UserDto;
import com.nichesoftware.giftlist.model.Gift;
import com.nichesoftware.giftlist.model.Room;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by n_che on 06/06/2016.
 */
public interface RestAPI {
    String BASE_URL = "http://192.168.42.176:8080/giftlist/";

    @GET("rooms")
    Call<List<Room>> getAllRooms(@Header("X-Auth-Token") final String token);

    @POST("gifts")
    Call<List<Gift>> getGifts(@Header("X-Auth-Token") final String token, @Body final RoomDto roomDto);

    @POST("authentication")
    Call<String> authenticate(@Body final UserDto userDto);

    @POST("register")
    Call<String> register(@Body final UserDto userDto);

    @POST("room")
    Call<Boolean> createRoom(@Header("X-Auth-Token") final String token, @Body final RoomDto roomDto);

    @POST("gift")
    Call<Boolean> addGift(@Header("X-Auth-Token") final String token, @Body final GiftDto giftDto);

    @PUT("gift")
    Call<Boolean> updateGift(@Header("X-Auth-Token") final String token, @Body final GiftDto giftDto);
}
