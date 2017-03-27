package com.nichesoftware.giftlist;

import com.nichesoftware.giftlist.service.Service;
import com.nichesoftware.giftlist.service.ServiceGenerator;

import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Injection module (Mock flavor)
 */
public final class Injection {
    public static Service getService() {
        NetworkBehavior behavior = NetworkBehavior.create();
        behavior.setDelay(2000, MILLISECONDS);
        behavior.setFailurePercent(20);
        behavior.setVariancePercent(20);
        return new MockRestService(new MockRetrofit.Builder(ServiceGenerator.getRetrofit())
                .networkBehavior(behavior).build());
    }
}
