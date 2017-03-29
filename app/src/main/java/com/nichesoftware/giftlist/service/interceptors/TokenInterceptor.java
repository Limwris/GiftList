package com.nichesoftware.giftlist.service.interceptors;

import com.nichesoftware.giftlist.session.TokenManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Interceptor to handle the token in HTTP requests
 */
public class TokenInterceptor implements Interceptor {

    /**
     * {@link TokenManager} to provide and handle the token
     */
    private final TokenManager mTokenManager;

    /**
     * Default constructor
     *
     * @param tokenManager      {@link TokenManager} to provide and handle the token
     */
    public TokenInterceptor(TokenManager tokenManager) {
        mTokenManager = tokenManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request initialRequest = chain.request();
        Request modifiedRequest = initialRequest;

        if (mTokenManager.hasToken()) {
            //noinspection ConstantConditions
            modifiedRequest = initialRequest.newBuilder()
                    .addHeader("X-Auth-Token", mTokenManager.getToken())
                    .build();
        }

        return chain.proceed(modifiedRequest);
    }
}
