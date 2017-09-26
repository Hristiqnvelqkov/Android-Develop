package example.streetview.stamsoft.app.streetviewexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.streetview.stamsoft.app.streetviewexample.fragments.BaseFragment;
import example.streetview.stamsoft.app.streetviewexample.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        showFragment(HomeFragment.newInstance());


        if (Utils.getSavedDevice(this).isEmpty()) {
            List<String> lumi = new ArrayList();
            lumi.add("lumi walk");
            Utils.saveDevices(lumi, this);
        }
    }

    public void requestFullScreen() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void showFragment(BaseFragment fragment) {
        removeAllFragments();
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .commitAllowingStateLoss();
    }

    public boolean isSameFragmentShowing(BaseFragment fragment) {
        return getFragmentManager().findFragmentById(R.id.fragment) != null &&
                getFragmentManager().findFragmentById(R.id.fragment).getClass().equals(fragment.getClass());
    }

    public void showFragmentAndAddToBackstack(BaseFragment fragment) {
        if (isSameFragmentShowing(fragment)) {
            return;
        }
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commitAllowingStateLoss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LumiManager.getInstance(this).onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LumiManager.getInstance(this).onPause();
    }

    public void removeAllFragments() {
        for (int i = 0; i < getFragmentManager()
                .getBackStackEntryCount(); ++i) {
            getFragmentManager().popBackStack();
        }
    }
    @Override public void onBackPressed(){
        if(getFragmentManager().findFragmentById(R.id.fragment)!=null){
            if(getFragmentManager().findFragmentById(R.id.fragment) instanceof HomeFragment) {
                HomeFragment fragment = (HomeFragment) getFragmentManager().findFragmentById(R.id.fragment);
                fragment.showBack();
            }else{
                super.onBackPressed();
            }
        }else{
            super.onBackPressed();
        }
    }

}
