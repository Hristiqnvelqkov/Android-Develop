package com.apress.gerber.footballman;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.apress.gerber.footballman.Fragments.BaseFragment;
import com.apress.gerber.footballman.Fragments.HomeFragment;
import com.apress.gerber.footballman.Fragments.StartMatchFragment;
import com.apress.gerber.footballman.Models.Game;
import com.apress.gerber.footballman.Models.League;

import io.realm.Realm;


public class MainActivity extends AppCompatActivity implements BaseFragment.OnFragmentInteractionListener {
    FragmentManager manager = null;
    public DrawerLayout drawer;
    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);
        manager = getSupportFragmentManager();
        setUpNavigationDrawer();
        setUpToolBar("FootballMan");
        Fragment homeFragment = new HomeFragment();
        commitFragment(homeFragment, false);

    }

    public void commitFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void setUpNavigationDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.activity_main);
        ListView list = (ListView) findViewById(R.id.left_drawer);
    }

    public void visibleHome() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void invisibleHome() {
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public void setUpToolBar(String title) {
        Toolbar bar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(bar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void startGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void openLeague(League league) {

    }

    @Override
    public void onBackPressed() {
        Fragment fragment = manager.findFragmentById(R.id.fragment);
        if (fragment instanceof StartMatchFragment) {
            GoToHomeDialog dialog = new GoToHomeDialog(this,(StartMatchFragment) fragment);

        } else{
                if (fragment instanceof HomeFragment) {
                    if (((HomeFragment) fragment).onBackPressed()) {
                        ((HomeFragment) fragment).setRecyclerView(false);
                    } else {
                        super.onBackPressed();
                    }
                } else {
                    super.onBackPressed();
                }
            }
        }
    }
