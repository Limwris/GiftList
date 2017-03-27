package com.nichesoftware.giftlist;

import com.nichesoftware.giftlist.service.Service;
import com.nichesoftware.giftlist.service.ServiceGenerator;

/**
 * Injection module (Prod flavor)
 */
public final class Injection {
    public static Service getService() {
        return ServiceGenerator.createService(Service.class);
    }
}
