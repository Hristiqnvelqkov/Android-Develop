package com.example.hristiyan.menu;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.hristiyan.menu.database.MenuDatabase;
import com.example.hristiyan.menu.fragments.BaseFragment;
import com.example.hristiyan.menu.fragments.ChoosePersonOrAdministratorFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showFragmentAndAddToBackstack(ChoosePersonOrAdministratorFragment.newInstance());
    }

    public void showFragmentAndAddToBackstack(BaseFragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment, fragment.getClass().getName())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(fragment.getClass().toString())
                .commitAllowingStateLoss();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }



    @Override
    public void onBackPressed() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment);
        if(fragment instanceof ChoosePersonOrAdministratorFragment){
            finish();
        }else {
            super.onBackPressed();
        }
    }
}
