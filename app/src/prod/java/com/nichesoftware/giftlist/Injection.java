package com.nichesoftware.giftlist;

import android.content.Context;

import com.nichesoftware.giftlist.dataproviders.DataProvider;

/**
 * Created by n_che on 25/04/2016.
 */
public final class Injection {
    public static DataProvider getDataProvider(final Context context) {
        return new DataProvider(context, new RestService());
    }
}
