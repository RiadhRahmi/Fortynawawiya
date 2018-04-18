package com.wridmob.fortynawawiya.activites;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import com.wridmob.fortynawawiya.R;
import com.wridmob.fortynawawiya.adapters.ImageAdapter;

public class ImageActivity extends AppCompatActivity {
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        ImageAdapter adapter;


        Bundle bundle = this.getIntent().getExtras();
        int position = bundle.getInt("position");

        viewPager = (ViewPager) findViewById(R.id.list_pager);
        ArrayList<String> data = new ArrayList<>();
        for (int i = 42; i > 0; i--) {
            data.add("img" + i + ".jpg");
        }
        adapter = new ImageAdapter(this, data);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(42 - position);
    }


    public void onBackPressed() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("atPage", 42 - viewPager.getCurrentItem());
        editor.apply();
        this.finish();

    }
}
