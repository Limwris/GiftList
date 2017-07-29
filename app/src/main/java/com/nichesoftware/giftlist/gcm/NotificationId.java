package com.nichesoftware.giftlist.gcm;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility class which generates a new ID for notification
 */
public class NotificationId {
    private final static AtomicInteger c = new AtomicInteger(0);
    public static int getID() {
        return c.incrementAndGet();
    }
}
