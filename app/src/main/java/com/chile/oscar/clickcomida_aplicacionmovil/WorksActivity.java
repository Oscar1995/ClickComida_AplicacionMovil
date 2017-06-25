package com.chile.oscar.clickcomida_aplicacionmovil;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

public class WorksActivity extends AppCompatActivity {


    private SectionsPageAdapter sectionPageAdapter;
    private ViewPager viewPager;

    public WorksActivity()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_works);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sectionPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        viewPager = (ViewPager)findViewById(R.id.container);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
    private void setupViewPager(ViewPager viewPager)
    {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new tab_list_work(), "Lista");
        adapter.addFragment(new tab_map_work(), "Mapa");
        viewPager.setAdapter(adapter);
    }

}
