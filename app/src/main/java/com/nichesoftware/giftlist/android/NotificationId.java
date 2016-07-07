package com.nichesoftware.giftlist.android;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Kattleya on 07/07/2016.
 */
public class NotificationId {
    private final static AtomicInteger c = new AtomicInteger(0);
    public static int getID() {
        return c.incrementAndGet();
    }
}
