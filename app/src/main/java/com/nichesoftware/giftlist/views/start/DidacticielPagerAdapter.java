package com.nichesoftware.giftlist.views.start;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by n_che on 08/06/2016.
 */
public class DidacticielPagerAdapter extends PagerAdapter {
    /**
     * Contexte
     */
    private Context context;

    /**
     * Constructeur
     * @param context
     */
    public DidacticielPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        PagerEnum customPagerEnum = PagerEnum.values()[position];
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(customPagerEnum.getLayoutResId(), collection, false);
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return PagerEnum.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        PagerEnum pagerEnum = PagerEnum.values()[position];
        return context.getString(pagerEnum.getTitleResId());
    }
}
