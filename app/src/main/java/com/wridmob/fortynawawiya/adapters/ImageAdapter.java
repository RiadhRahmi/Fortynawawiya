package com.wridmob.fortynawawiya.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.wridmob.fortynawawiya.R;


public class ImageAdapter extends PagerAdapter {

    private Activity activity;
    private ArrayList<String> data;



    public ImageAdapter(Activity activity, ArrayList<String> data) {
        this.activity = activity;
        this.data = data;
    }

    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imgDisplay;
        LayoutInflater inflater;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View viewLayout = inflater.inflate(R.layout.image_pager, container, false);
        imgDisplay = (ImageView) viewLayout.findViewById(R.id.img_pager);

        InputStream ims = null;
        try {
            ims = activity.getAssets().open(data.get(position));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Drawable d = Drawable.createFromStream(ims, null);
        imgDisplay.setImageDrawable(d);
        ((ViewPager) container).addView(viewLayout);
        return viewLayout;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }

}


