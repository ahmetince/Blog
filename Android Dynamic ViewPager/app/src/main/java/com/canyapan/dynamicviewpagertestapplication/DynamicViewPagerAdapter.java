package com.canyapan.dynamicviewpagertestapplication;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class DynamicViewPagerAdapter extends PagerAdapter {
    private List<CustomViewPagerContentView> mViews;

    public DynamicViewPagerAdapter(Context context, int start) {
        mViews = new ArrayList<>(3);
        mViews.add(makeView(context, start - 1));
        mViews.add(makeView(context, start));
        mViews.add(makeView(context, start + 1));
    }

    private CustomViewPagerContentView makeView(Context context, int id) {
        LayoutInflater inflater = LayoutInflater.from(context);
        CustomViewPagerContentView v = (CustomViewPagerContentView) inflater.inflate(R.layout.custom_view_pager_content_view, null);

        v.setContent(id);

        return v;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = mViews.get(position);
        container.addView(v);

        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        if (mViews.contains(object)) {
            return mViews.indexOf(object);
        }

        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    public void move(int position) {
        CustomViewPagerContentView v;
        int newID;
        switch (position) {
            case 0:
                newID = mViews.get(0).getItemID() - 1;
                v = mViews.remove(2);
                v.setContent(newID);

                mViews.add(0, v);
                break;
            case 2:
                newID = mViews.get(2).getItemID() + 1;
                v = mViews.remove(0);
                v.setContent(newID);

                mViews.add(2, v);
                break;
            default:
                return;
        }
        notifyDataSetChanged();
    }

    public int getId(int position) {
        return mViews.get(position).getItemID();
    }
}
