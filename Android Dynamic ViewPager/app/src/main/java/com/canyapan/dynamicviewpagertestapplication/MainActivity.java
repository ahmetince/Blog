package com.canyapan.dynamicviewpagertestapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private final String KEY_CURRENT_ITEM_ID_INT = "ITEM_ID";
    private ViewPager mDynamicViewPager;
    private DynamicViewPagerAdapter mDynamicViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int itemID;
        if (savedInstanceState != null) {
            itemID = savedInstanceState.getInt(KEY_CURRENT_ITEM_ID_INT);
        } else {
            itemID = 0;
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mDynamicViewPager = (ViewPager) findViewById(R.id.DynamicViewPager);
        mDynamicViewPagerAdapter = new DynamicViewPagerAdapter(MainActivity.this, itemID);
        mDynamicViewPager.setAdapter(mDynamicViewPagerAdapter);
        mDynamicViewPager.addOnPageChangeListener(this);
        mDynamicViewPager.setCurrentItem(1, false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(KEY_CURRENT_ITEM_ID_INT,
                mDynamicViewPagerAdapter.getId(mDynamicViewPager.getCurrentItem()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // NOT USED...
    }

    @Override
    public void onPageSelected(final int position) {
        mDynamicViewPagerAdapter.move(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // NOT USED...
    }
}
