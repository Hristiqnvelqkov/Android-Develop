package com.apress.gerber.footballman;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.apress.gerber.footballman.Fragments.StatisticsFragment;
import com.apress.gerber.footballman.Models.Game;
import com.apress.gerber.footballman.Models.League;
import com.google.firebase.database.FirebaseDatabase;

import utils.StoreGameInPrefenreces;


public class MainActivity extends AppCompatActivity implements BaseFragment.OnFragmentInteractionListener {
    FragmentManager manager = null;
    public DrawerLayout drawer;
    Game game;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //  FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = getSupportFragmentManager();
        setUpNavigationDrawer();
        setUpToolBar("");
        preferences = getSharedPreferences(
                getString(R.string.preferences_file), Context.MODE_PRIVATE);
        if(preferences.getString("GAME",null)!=null){
           AlertDialog mAlert = new AlertDialog.Builder(this).create();
            mAlert.setTitle(R.string.do_you_want_to_continue_game);
            mAlert.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    commitFragment(StartMatchFragment.newInstance(StoreGameInPrefenreces.getGameFromPreferences(MainActivity.this)),false);
                }
            });
            mAlert.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Fragment homeFragment = new HomeFragment();
                    StoreGameInPrefenreces.erasePreferences(MainActivity.this);
                    commitFragment(homeFragment, false);
                }
            });
            mAlert.show();
        }else {
            Fragment homeFragment = new HomeFragment();
            commitFragment(homeFragment, false);
        }
    }
    public SharedPreferences getPreferences(){
        return preferences;
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
//        drawer = (DrawerLayout) findViewById(R.id.activity_main);
//        ListView list = (ListView) findViewById(R.id.left_drawer);
    }

    public void visibleHome() {
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void invisibleHome() {
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        ExportData.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
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
        if (fragment instanceof StatisticsFragment) {
            ((StatisticsFragment) fragment).backPressed();
            return;
        }
        if (fragment instanceof StartMatchFragment) {
            GoToHomeDialog dialog = new GoToHomeDialog(this, (StartMatchFragment) fragment);

        } else {
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
    public void restartActivity() { //simulates clear backstack
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
