package com.nichesoftware.giftlist;

import android.util.Log;

import com.nichesoftware.giftlist.dto.ContactDto;
import com.nichesoftware.giftlist.dto.GiftDto;
import com.nichesoftware.giftlist.dto.InvitationDto;
import com.nichesoftware.giftlist.dto.RoomDto;
import com.nichesoftware.giftlist.dto.UserDto;
import com.nichesoftware.giftlist.model.Gift;
import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.model.User;
import com.nichesoftware.giftlist.service.ServiceAPI;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by n_che on 25/04/2016.
 */
public class RestService implements ServiceAPI {
    private final static String TAG = RestService.class.getSimpleName();

    @Override
    public void authenticate(final String username, final String password, final ServiceCallback<String> callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("authenticate [username = %s, password = %s]", username, password));
        }

        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword(password);
        RestAPI restAPI = getRetrofit().create(RestAPI.class);
        Call<String> call = restAPI.authenticate(userDto);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, String.format("authenticate - response %s", response.raw()));
                }
                callback.onLoaded(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onError();
            }
        });
    }

    @Override
    public void register(final String username, final String password, final String phoneNumber,
                         final ServiceCallback<String> callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("register [username = %s, password = %s]", username, password));
        }

        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword(password);
        userDto.setPhoneNumber(phoneNumber);

        RestAPI restAPI = getRetrofit().create(RestAPI.class);
        Call<String> call = restAPI.register(userDto);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, String.format("register - response %s", response.raw()));
                }
                callback.onLoaded(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onError();
            }
        });
    }

    @Override
    public void getAllRooms(final String token, final ServiceCallback<List<Room>> callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("getAllRooms [token = %s]", token));
        }

        RestAPI restAPI = getRetrofit().create(RestAPI.class);
        Call<List<Room>> call = restAPI.getAllRooms(token);
        call.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, String.format("getAllRooms - response %s", response.raw()));
                }
                callback.onLoaded(response.body());
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                callback.onError();
            }
        });
    }

    @Override
    public void getGifts(final String token, int roomId, final ServiceCallback<List<Gift>> callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("getGifts [token = %s, roomId = %d]", token, roomId));
        }

        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(roomId);

        RestAPI restAPI = getRetrofit().create(RestAPI.class);
        Call<List<Gift>> call = restAPI.getGifts(token, roomDto);
        call.enqueue(new Callback<List<Gift>>() {
            @Override
            public void onResponse(Call<List<Gift>> call, Response<List<Gift>> response) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, String.format("getGifts - response %s", response.raw()));
                }
                callback.onLoaded(response.body());
            }

            @Override
            public void onFailure(Call<List<Gift>> call, Throwable t) {
                callback.onError();
            }
        });
    }

    @Override
    public void createRoom(final String token, final String roomName, final String occasion, final ServiceCallback<Room> callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("createRoom [token = %s, roomName = %s, occasion = %s]", token, roomName, occasion));
        }

        RoomDto roomDto = new RoomDto();
        roomDto.setRoomName(roomName);
        roomDto.setOccasion(occasion);

        RestAPI restAPI = getRetrofit().create(RestAPI.class);
        Call<Room> call = restAPI.createRoom(token, roomDto);
        call.enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, String.format("getGifts - response %s", response.raw()));
                }
                callback.onLoaded(response.body());
            }

            @Override
            public void onFailure(Call<Room> call, Throwable t) {
                callback.onError();
            }
        });
    }

    @Override
    public void addGift(String token, int roomId, String name, double price, double amount,
                        final ServiceCallback<Gift> callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("addGift [token = %s, name = %s, price = %s]", token, name, price));
        }

        GiftDto giftDto = new GiftDto();
        giftDto.setRoomId(roomId);
        giftDto.setName(name);
        giftDto.setPrice(price);
        giftDto.setAmount(amount);

        RestAPI restAPI = getRetrofit().create(RestAPI.class);
        Call<Gift> call = restAPI.addGift(token, giftDto);
        call.enqueue(new Callback<Gift>() {
            @Override
            public void onResponse(Call<Gift> call, Response<Gift> response) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, String.format("addGift - response %s", response.raw()));
                }
                callback.onLoaded(response.body());
            }

            @Override
            public void onFailure(Call<Gift> call, Throwable t) {
                callback.onError();
            }
        });
    }

    @Override
    public void updateGift(String token, int roomId, int giftId, double amount,
                           final ServiceCallback<Gift> callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("updateGift [token = %s, giftId = %s, amount = %s]", token, giftId, amount));
        }

        GiftDto giftDto = new GiftDto();
        giftDto.setId(giftId);
        giftDto.setRoomId(roomId);
        giftDto.setAmount(amount);

        RestAPI restAPI = getRetrofit().create(RestAPI.class);
        Call<Gift> call = restAPI.updateGift(token, giftDto);
        call.enqueue(new Callback<Gift>() {
            @Override
            public void onResponse(Call<Gift> call, Response<Gift> response) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, String.format("updateGift - response %s", response.raw()));
                }
                callback.onLoaded(response.body());
            }

            @Override
            public void onFailure(Call<Gift> call, Throwable t) {
                callback.onError();
            }
        });
    }

    @Override
    public void retreiveAvailableUsers(final String token, final int roomId,
                                       final List<String> phoneNumbers,
                                       final ServiceCallback<List<User>> callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("retreiveAvailableUsers [token = %s]", token));
        }

        ContactDto contactDto = new ContactDto();
        contactDto.setRoomId(roomId);
        contactDto.setPhoneNumbers(phoneNumbers);

        RestAPI restAPI = getRetrofit().create(RestAPI.class);
        Call<List<User>> call = restAPI.retreiveAvailableContacts(token, contactDto);
        if (BuildConfig.DEBUG) {
            for (String phoneNumber : phoneNumbers) {
                Log.d(TAG, String.format("retreiveAvailableUsers [tel = %s]", phoneNumber));
            }
        }
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, String.format("retreiveAvailableUsers - response %s", response.raw()));
                }
                callback.onLoaded(response.body());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                callback.onError();
            }
        });
    }

    @Override
    public void inviteUserToRoom(final String token, int roomId, final String username,
                                 final ServiceCallback<Boolean> callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("inviteUserToRoom [token = %s]", token));
        }

        InvitationDto invitationDto = new InvitationDto();
        invitationDto.setRoomId(roomId);
        invitationDto.setUsername(username);

        RestAPI restAPI = getRetrofit().create(RestAPI.class);
        Call<Boolean> call = restAPI.inviteUserToRoom(token, invitationDto);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, String.format("inviteUserToRoom - response %s", response.raw()));
                }
                callback.onLoaded(response.body());
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                callback.onError();
            }
        });
    }

    private Retrofit getRetrofit() {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(RestAPI.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            // add your other interceptors …
            // add logging as last interceptor
            httpClient.addInterceptor(logging);  // <-- this is the important line!
            retrofitBuilder.client(httpClient.build());
        }

        return retrofitBuilder.build();
    }
}
