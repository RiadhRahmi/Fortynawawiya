package com.wridmob.fortynawawiya.activites;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.wridmob.fortynawawiya.R;
import com.wridmob.fortynawawiya.adapters.FahresAdapter;
import com.wridmob.fortynawawiya.entities.Fahres;

public class FahresActivity extends AppCompatActivity {
    ListView listViewFahres;
    FahresAdapter fahresAdapter;
    List<Fahres> data;
    private MaterialSearchView searchView;
    Toolbar toolbar;
    String[] atPage,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fahres);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        centerToolbarTitle(toolbar);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.cursor);
        searchView.setEllipsize(true);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getInt("atPage", 0) != 0) {
            Intent intent = new Intent(this, ImageActivity.class);
            Bundle b = new Bundle();
            b.putInt("position", preferences.getInt("atPage", 0));
            intent.putExtras(b);
            startActivity(intent);
        }


        data = new ArrayList<>();
        atPage = getResources().getStringArray(R.array.atPage);
        name = getResources().getStringArray(R.array.name);

        for (int i = 0; i < atPage.length; i++) {
            Fahres s = new Fahres();
            s.atPage = atPage[i];
            s.name = name[i];
            data.add(s);
        }

        listViewFahres = (ListView) findViewById(R.id.listView_fahres);
        fahresAdapter = new FahresAdapter(this, data);
        listViewFahres.setAdapter(fahresAdapter);
        listViewFahres.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FahresActivity.this, ImageActivity.class);
                Bundle b = new Bundle();
                b.putInt("position", Integer.parseInt(fahresAdapter.getItem(position).atPage));
                intent.putExtras(b);
                startActivity(intent);
            }
        });


        searchView.setSuggestions(getResources().getStringArray(R.array.name));

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fahresAdapter.getFilter().filter(query);
                toolbar.setTitle(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                fahresAdapter.getFilter().filter("");
                toolbar.setTitle(getResources().getString(R.string.name_summary));

            }

            @Override
            public void onSearchViewClosed() {
            }
        });

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FahresActivity.this, ImageActivity.class);
                Bundle b = new Bundle();
                b.putInt("position", Integer.parseInt(atPage[getPosition(fahresAdapter.getItem(position).name)]));
                intent.putExtras(b);
                startActivity(intent);
                searchView.closeSearch();
            }
        });

    }

    static void centerToolbarTitle(Toolbar toolbar) {
        final CharSequence title = toolbar.getTitle();
        final ArrayList<View> outViews = new ArrayList<>(1);
        toolbar.findViewsWithText(outViews, title, View.FIND_VIEWS_WITH_TEXT);
        if (!outViews.isEmpty()) {
            final TextView titleView = (TextView) outViews.get(0);
            titleView.setGravity(Gravity.RIGHT);
            final Toolbar.LayoutParams layoutParams = (Toolbar.LayoutParams) titleView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            toolbar.requestLayout();
        }
    }

    private int getPosition(String string) {
        int i = 0;
        while (!name[i].equals(string) && i < atPage.length) {
            i++;
        }
        return i;
    }


    public void share(View view) {
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpg");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
        shareIntent.putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps/details?id=" + getPackageName());
        startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.share)));
    }


    public void rating(View view) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    public void myApplications(View view) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://developer?id=WridMob")));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=WridMob")));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fahres, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }



    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }
            return;
        } else if (requestCode == 9999) {
            fahresAdapter.getFilter().filter("");
            toolbar.setTitle(getResources().getString(R.string.name_summary));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
