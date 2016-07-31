package com.nichesoftware.giftlist;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.nichesoftware.giftlist.dto.ContactDto;
import com.nichesoftware.giftlist.dto.GCMRegistrationDto;
import com.nichesoftware.giftlist.dto.GiftDto;
import com.nichesoftware.giftlist.dto.InvitationDto;
import com.nichesoftware.giftlist.dto.RoomDto;
import com.nichesoftware.giftlist.dto.UserDto;
import com.nichesoftware.giftlist.model.Gift;
import com.nichesoftware.giftlist.model.Room;
import com.nichesoftware.giftlist.model.User;
import com.nichesoftware.giftlist.service.ServiceAPI;
import com.nichesoftware.giftlist.utils.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by n_che on 25/04/2016.
 */
public class RestService implements ServiceAPI {
    private final static String TAG = RestService.class.getSimpleName();

    @Override
    public void authenticate(final String username, final String password,
                             final String phoneNumber, final ServiceCallback<String> callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("authenticate [username = %s, password = %s]", username, password));
        }

        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword(password);
        userDto.setPhoneNumber(phoneNumber);
        RestServiceEndpoint restServiceEndpoint = ServiceGenerator.createService(RestServiceEndpoint.class);
        Call<String> call = restServiceEndpoint.authenticate(userDto);
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

        RestServiceEndpoint restServiceEndpoint = ServiceGenerator.createService(RestServiceEndpoint.class);
        Call<String> call = restServiceEndpoint.register(userDto);
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

        RestServiceEndpoint restServiceEndpoint = ServiceGenerator.createService(RestServiceEndpoint.class);
        Call<List<Room>> call = restServiceEndpoint.getAllRooms(token);
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

        RestServiceEndpoint restServiceEndpoint = ServiceGenerator.createService(RestServiceEndpoint.class);
        Call<List<Gift>> call = restServiceEndpoint.getGifts(token, roomDto);
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

        RestServiceEndpoint restServiceEndpoint = ServiceGenerator.createService(RestServiceEndpoint.class);
        Call<Room> call = restServiceEndpoint.createRoom(token, roomDto);
        call.enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, String.format("createRoom - response %s", response.raw()));
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
    public void leaveRoom(final String token, int roomId, final ServiceCallback<List<Room>> callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("leaveRoom [token = %s, roomId = %d]", token, roomId));
        }

        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(roomId);

        RestServiceEndpoint restServiceEndpoint = ServiceGenerator.createService(RestServiceEndpoint.class);
        Call<List<Room>> call = restServiceEndpoint.leaveRoom(token, roomDto);
        call.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, String.format("leaveRoom - response %s", response.raw()));
                }
                callback.onLoaded(response.body());
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "leaveRoom - onError");
                }
                callback.onError();
            }
        });
    }

    @Override
    public void addGift(final String token, int roomId, String name, double price, double amount,
                        final String description, final String filePath,
                        final ServiceCallback<Gift> callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("addGift [token = %s, name = %s, price = %s]", token, name, price));
        }

        GiftDto giftDto = new GiftDto();
        giftDto.setRoomId(roomId);
        giftDto.setName(name);
        giftDto.setDescription(description);
        giftDto.setPrice(price);
        giftDto.setAmount(amount);
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), gson.toJson(giftDto));

        MultipartBody.Part fileBody = null;
        if (!StringUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.exists()) {
                // create RequestBody instance from file
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                // MultipartBody.Part is used to send also the actual file name
                fileBody = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            }
        }

        RestServiceEndpoint restServiceEndpoint = ServiceGenerator.createService(RestServiceEndpoint.class);
        Call<Gift> call = restServiceEndpoint.addGift(token, fileBody, requestBody);
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
                           final String description, final String filePath,
                           @NonNull final ServiceCallback<Gift> callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("updateGift [token = %s, giftId = %s, amount = %s]", token, giftId, amount));
        }

        GiftDto giftDto = new GiftDto();
        giftDto.setId(giftId);
        giftDto.setRoomId(roomId);
        giftDto.setDescription(description);
        giftDto.setAmount(amount);

        RestServiceEndpoint restServiceEndpoint = ServiceGenerator.createService(RestServiceEndpoint.class);
        Call<Gift> call = restServiceEndpoint.updateGift(token, giftDto);
        call.enqueue(new Callback<Gift>() {
            @Override
            public void onResponse(Call<Gift> call, Response<Gift> response) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, String.format("updateGift - response %s", response.raw()));
                }
                if (response.isSuccessful()) {
                    callback.onLoaded(response.body());
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<Gift> call, Throwable t) {
                callback.onError();
            }
        });

        // Upload file now...
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), gson.toJson(giftDto));

        if (!StringUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.exists()) {
                // create RequestBody instance from file
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                // MultipartBody.Part is used to send also the actual file name
                MultipartBody.Part fileBody = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

                Call<Void> uploadFileCall = restServiceEndpoint.updateGifFile(token, fileBody, requestBody);
                uploadFileCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, String.format("updateGiftFile - response %s", response.raw()));
                        }
                        // Do nothing
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "updateGiftFile - onFailure");
                        }
                        // Do nothing
                    }
                });
            }
        }
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

        RestServiceEndpoint restServiceEndpoint = ServiceGenerator.createService(RestServiceEndpoint.class);
        Call<List<User>> call = restServiceEndpoint.retreiveAvailableContacts(token, contactDto);
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

        RestServiceEndpoint restServiceEndpoint = ServiceGenerator.createService(RestServiceEndpoint.class);
        Call<Boolean> call = restServiceEndpoint.inviteUserToRoom(token, invitationDto);
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

    @Override
    public void acceptInvitationToRoom(String token, int roomId,
                                       final ServiceCallback<Boolean> callback) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("acceptInvitationToRoom [token = %s]", token));
        }

        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(roomId);

        RestServiceEndpoint restServiceEndpoint = ServiceGenerator.createService(RestServiceEndpoint.class);
        Call<Boolean> call = restServiceEndpoint.acceptInvitationToRoom(token, roomDto);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, String.format("acceptionInvitatToRoom - response %s", response.raw()));
                }
                callback.onLoaded(response.body());
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                callback.onError();
            }
        });
    }

    @Override
    public void sendRegistrationToServer(final String token, final String gcmToken, final OnRegistrationCompleted callback) {
        GCMRegistrationDto dto = new GCMRegistrationDto();
        dto.setRegisterId(gcmToken);

        RestServiceEndpoint restServiceEndpoint = ServiceGenerator.createService(RestServiceEndpoint.class);
        Call<Boolean> call = restServiceEndpoint.registerDevice(token, dto);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    final Boolean result = response.body();
                    if (result) {
                        callback.onSuccess();
                    } else {
                        callback.onError();
                    }
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                callback.onError();
            }
        });
    }

    @Override
    public String getGiftImageUrl(final int giftId) {
        return String.format(Locale.getDefault(), ServiceGenerator.GIFT_IMAGE_URL, giftId);
    }
}
