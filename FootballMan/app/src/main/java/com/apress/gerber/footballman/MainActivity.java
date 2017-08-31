package com.apress.gerber.footballman;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.apress.gerber.footballman.Fragments.BaseFragment;
import com.apress.gerber.footballman.Fragments.HomeFragment;
import com.apress.gerber.footballman.Fragments.TeamsFragment;
import com.apress.gerber.footballman.Models.League;


public class MainActivity extends AppCompatActivity implements BaseFragment.OnFragmentInteractionListener {
    FragmentManager manager ;
    public DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpNavigationDrawer();
        setUpToolBar("FootballMan");
        manager = getSupportFragmentManager();
        Fragment homeFragment = new HomeFragment();
        commitFragment(homeFragment,true);
    }
    public void commitFragment(Fragment fragment,boolean addToBackStack){
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.activity_main,fragment);
        if(addToBackStack){
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
    public void setUpNavigationDrawer(){
        drawer = (DrawerLayout) findViewById(R.id.activity_main);
        ListView list = (ListView) findViewById(R.id.left_drawer);
    }
    public void visibleHome(){
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void invisibleHome(){
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
    public void setUpToolBar(String title){
        Toolbar bar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(bar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void openLeague(League league) {

    }
}
