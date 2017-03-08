package com.nichesoftware.giftlist.utils;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by admin on 08/03/2017.
 */

public final class BusProvider {

    /**
     * Constructeur privé
     */
    private BusProvider() {
        // Nothing
    }

    /**
     * Holder (@see http://thecodersbreakfast.net/index.php?post/2008/02/25/26-de-la-bonne-implementation-du-singleton-en-java)
     */
    private static class BusHolder {
        private BusHolder() {
        }

        /**
         * Instance unique non préinitialisée
         */
        private final static MainThreadBus instance = new MainThreadBus();
    }

    /**
     * Point d'accès pour l'instance unique du singleton
     */
    public static Bus getInstance() {
        return BusHolder.instance;
    }

    /**
     * Register an object as an otto event listener.
     *
     * @param object the listener object to register
     */
    public static void register(Object object) {
        getInstance().register(object);
    }

    /**
     * Unregister an otto listener object from otto event bus.
     *
     * @param object the listener object to unregister
     */
    public static void unregister(Object object) {
        getInstance().unregister(object);
    }


    /**
     * Post an event into the bus Otto, notifying subscribed object of the event
     *
     * @param event the event to post
     */
    public static void post(Object event) {
        getInstance().post(event);
    }

    /**
     * An implementation of the Otto bus class that makes the event goes back to the UI thread
     */
    private static class MainThreadBus extends Bus {
        private final Handler mHandler = new Handler(Looper.getMainLooper());

        public MainThreadBus() {
            super(ThreadEnforcer.ANY);
        }

        @Override
        public void post(final Object event) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                super.post(event);
            } else {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        MainThreadBus.super.post(event);

                    }
                });
            }
        }
    }
}
