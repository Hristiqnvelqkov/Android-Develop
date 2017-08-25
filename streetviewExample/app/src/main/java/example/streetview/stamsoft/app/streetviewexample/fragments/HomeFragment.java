package example.streetview.stamsoft.app.streetviewexample.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.android.gms.maps.model.StreetViewPanoramaLink;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;
import com.google.android.gms.maps.model.StreetViewPanoramaOrientation;

import butterknife.OnClick;
import example.streetview.stamsoft.app.streetviewexample.RequestService;
import example.streetview.stamsoft.app.streetviewexample.LumiManager;
import example.streetview.stamsoft.app.streetviewexample.R;
import example.streetview.stamsoft.app.streetviewexample.Utils;
import example.streetview.stamsoft.app.streetviewexample.interfaces.LumiBleListener;

import static android.view.View.VISIBLE;

/**
 * Created by Stanislav on 7/15/2017.
 */

public class HomeFragment extends BaseFragment implements LumiBleListener{

    public static final int REQUEST_CODE_ASK_LOCATION = 1002;
    private StreetViewPanorama mStreetViewPanorama;
    public static final String STRING_EXTRA="city";
    // George St, Sydney
    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
    // Cole St, San Fran
    private static final LatLng SAN_FRAN = new LatLng(37.769263, -122.450727);
    // Santorini, Greece
    private static final String SANTORINI = "WddsUw1geEoAAAQIt9RnsQ";
    // LatLng with no panorama
    private static final LatLng INVALID = new LatLng(-45.125783, 151.276417);
    /**
     * The amount in degrees by which to scroll the camera
     */
    private static final int PAN_BY_DEG = 30;
    private static final float ZOOM_BY = 0.5f;
    EditText searchPlace ;
    Button goToWorld;
    Button search;
    Handler handler ;
    Runnable hideTask;
    private InputMethodManager imm;
    Button sydney,sanFran,santorini;
    ImageView imageLogo;
    BroadcastReceiver brUpdate;
    long lastMove;
    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler= new Handler(Looper.myLooper());
        hideTask = new Runnable() {
            @Override
            public void run() {
                getAllInvisible();
            }
        };
        LumiManager.getInstance(getActivity()).getDevicesName().addAll(Utils.getSavedDevice(getActivity()));
        RequestReceiver receiver = new RequestReceiver();
        IntentFilter filter =  new IntentFilter(RequestService.BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver,filter);
        brUpdate = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                handler.removeCallbacks(null);
                updateUi();
            }
        };
    }

    private void hideElements() {
        handler.removeCallbacks(hideTask);
        handler.postDelayed(hideTask, 20000);
    }
    private void setAllVisible(){
        setDestinationsVisible();
        goToWorld.setVisibility(View.VISIBLE);
        imageLogo.setVisibility(View.VISIBLE);
    }
    public void showBack(){
        if(sydney.getVisibility()==VISIBLE){
            getActivity().finish();
        }else {
            searchPlace.setVisibility(View.GONE);
            setDestinationsVisible();
        }
    }
    private void updateUi() {
        if (lastMove == 0 || System.currentTimeMillis() - lastMove > 2000) {
            lastMove = System.currentTimeMillis();
            onMovePosition();
        }
    }
    private void getAllInvisible(){
        setDestinationsInVisible();
        goToWorld.setVisibility(View.GONE);
        searchPlace.setVisibility(View.GONE);
        imageLogo.setVisibility(View.GONE);
    }
    @SuppressLint("ResourceType")
    @Override
    protected void onCreateView() {

        SupportStreetViewPanoramaFragment streetViewPanoramaFragment =
                 SupportStreetViewPanoramaFragment.newInstance();
        getMainActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.streetviewpanorama, streetViewPanoramaFragment)
                .commitAllowingStateLoss();
        initializeViews();
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(
                new OnStreetViewPanoramaReadyCallback() {
                    @Override
                    public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
                        mStreetViewPanorama = panorama;
                        mStreetViewPanorama.setOnStreetViewPanoramaClickListener(new StreetViewPanorama.OnStreetViewPanoramaClickListener() {
                            @Override
                            public void onStreetViewPanoramaClick(StreetViewPanoramaOrientation streetViewPanoramaOrientation) {
                                setAllVisible();
                                hideElements();
                            }
                        });
                        // Only set the panorama to SYDNEY on startup (when no panoramas have been
                        // loaded which is when the savedInstanceState is null).
                        mStreetViewPanorama.setPosition(SAN_FRAN);
                    }
                });

        searchPlace.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH){
                    startService(searchPlace);
                }
                return false;
            }
        });

        if (Build.VERSION.SDK_INT >= 23) {
            int hasLocationPermission = getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.WAKE_LOCK, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN},
                        REQUEST_CODE_ASK_LOCATION);
            } else {
                LumiManager.getInstance(getActivity()).setupBle(this);
                LumiManager.getInstance(getActivity()).scanLeDevice(true, null);
            }
        } else {
            LumiManager.getInstance(getActivity()).setupBle(this);
            LumiManager.getInstance(getActivity()).scanLeDevice(true, null);
        }

    }
    void startService(EditText search){
        String city= search.getText().toString();
        Intent intent = new Intent(getActivity(), RequestService.class);
        intent.putExtra(STRING_EXTRA,city);
        getActivity().startService(intent);
    }
    public void initializeViews(){
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "font/handkerchief.ttf");
        imageLogo = (ImageView) mainView.findViewById(R.id.image_logo);
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        sydney = (Button) mainView.findViewById(R.id.sydney);
        sydney.setTypeface(font);
        santorini = (Button) mainView.findViewById(R.id.santorini);
        santorini.setTypeface(font);
        sanFran = (Button) mainView.findViewById(R.id.sanfran);
        RelativeLayout layout = (RelativeLayout) mainView.findViewById(R.id.relativeLayout);
        sanFran.setTypeface(font);
        layout.setVisibility(View.GONE);
        Button movePosition = (Button) mainView.findViewById(R.id.move_position);
        movePosition.setVisibility(View.GONE);
        Button settings = (Button) mainView.findViewById(R.id.settings);
        settings.setVisibility(View.GONE);
        searchPlace=(EditText)mainView.findViewById(R.id.place_name);
        goToWorld=(Button)mainView.findViewById(R.id.everywhere);
        goToWorld.setTypeface(font);
        search = (Button)mainView.findViewById(R.id.search);
        search.setVisibility(View.GONE);
        searchPlace.setVisibility(View.GONE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_ASK_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LumiManager.getInstance(getActivity()).setupBle(this);
                LumiManager.getInstance(getActivity()).scanLeDevice(true, null);
            }
        }
    }

    @OnClick(R.id.image_logo)
    public void onSettings() {
        getMainActivity().showFragmentAndAddToBackstack(new SettingsFragment());
    }

    @Override
    protected void setupToolbar() {
        getMainActivity().getSupportActionBar().hide();
    }

    /**
     * When the panorama is not ready the PanoramaView cannot be used. This should be called on
     * all entry points that call methods on the Panorama API.
     */
    private boolean checkReady() {
        if (mStreetViewPanorama == null) {
            Toast.makeText(getActivity(), R.string.panorama_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Called when the Go To San Fran button is clicked.
     */
    @OnClick(R.id.sanfran)
    public void onGoToSanFran(View view) {
        if (!checkReady()) {
            return;
        }
        mStreetViewPanorama.setPosition(SAN_FRAN, 30);
    }

    /**
     * Called when the Animate To Sydney button is clicked.
     */
    private void setDestinationsVisible(){
        sanFran.setVisibility(VISIBLE);
        santorini.setVisibility(VISIBLE);
        sydney.setVisibility(VISIBLE);
    }
    private void setDestinationsInVisible(){
        sanFran.setVisibility(View.GONE);
        santorini.setVisibility(View.GONE);
        sydney.setVisibility(View.GONE);
    }
    @OnClick(R.id.sydney)
    public void onGoToSydney(View view) {
        if (!checkReady()) {
            return;
        }
        mStreetViewPanorama.setPosition(SYDNEY);
    }

    @OnClick(R.id.everywhere)
    public void onGoToEveryWhere(View view){
        if(searchPlace.getVisibility()== VISIBLE){
            searchPlace.setVisibility(View.GONE);
            setDestinationsVisible();

            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
        }else {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            searchPlace.setText("");
            setDestinationsInVisible();
            searchPlace.setVisibility(VISIBLE);
            searchPlace.post(new Runnable() {
                @Override
                public void run() {
                    searchPlace.requestFocus();
                }
            });
        }

    }

    @OnClick(R.id.search)
    public void searchPlace(View view) {
        startService(searchPlace);
    }
    private class RequestReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String latLng = intent.getStringExtra(RequestService.EXTRA);
            if(!latLng.equals("")) {
                String lat = latLng.split(",")[0];
                String lng = latLng.split(",")[1];
                LatLng mlatLng = new LatLng(Float.parseFloat(lat), Float.parseFloat(lng));
                mStreetViewPanorama.setPosition(mlatLng);
                search.setVisibility(View.GONE);
                setDestinationsVisible();
                searchPlace.setVisibility(View.GONE);
            }else{
                Toast.makeText(getActivity(),"Place was't found",Toast.LENGTH_SHORT).show();
            }
        }
    }
    /**
     * Called when the Animate To Santorini button is clicked.
     */
    @OnClick(R.id.santorini)
    public void onGoToSantorini(View view) {
        if (!checkReady()) {
            return;
        }
        mStreetViewPanorama.setPosition(SANTORINI);
    }

    /**
     * Called when the Animate To Invalid button is clicked.
     */
    public void onGoToInvalid(View view) {
        if (!checkReady()) {
            return;
        }
        mStreetViewPanorama.setPosition(INVALID);
    }

    public void onZoomIn(View view) {
        if (!checkReady()) {
            return;
        }

        mStreetViewPanorama.animateTo(
                new StreetViewPanoramaCamera.Builder().zoom(
                        mStreetViewPanorama.getPanoramaCamera().zoom + ZOOM_BY)
                        .tilt(mStreetViewPanorama.getPanoramaCamera().tilt)
                        .bearing(mStreetViewPanorama.getPanoramaCamera().bearing)
                        .build(), getDuration());
    }

    public void onZoomOut(View view) {
        if (!checkReady()) {
            return;
        }

        mStreetViewPanorama.animateTo(
                new StreetViewPanoramaCamera.Builder().zoom(
                        mStreetViewPanorama.getPanoramaCamera().zoom - ZOOM_BY)
                        .tilt(mStreetViewPanorama.getPanoramaCamera().tilt)
                        .bearing(mStreetViewPanorama.getPanoramaCamera().bearing)
                        .build(), getDuration());
    }

    @OnClick(R.id.pan_left)
    public void onPanLeft(View view) {
        if (!checkReady()) {
            return;
        }

        mStreetViewPanorama.animateTo(
                new StreetViewPanoramaCamera.Builder().zoom(
                        mStreetViewPanorama.getPanoramaCamera().zoom)
                        .tilt(mStreetViewPanorama.getPanoramaCamera().tilt)
                        .bearing(mStreetViewPanorama.getPanoramaCamera().bearing - PAN_BY_DEG)
                        .build(), getDuration());
    }

    @OnClick(R.id.pan_right)
    public void onPanRight(View view) {
        if (!checkReady()) {
            return;
        }

        mStreetViewPanorama.animateTo(
                new StreetViewPanoramaCamera.Builder().zoom(
                        mStreetViewPanorama.getPanoramaCamera().zoom)
                        .tilt(mStreetViewPanorama.getPanoramaCamera().tilt)
                        .bearing(mStreetViewPanorama.getPanoramaCamera().bearing + PAN_BY_DEG)
                        .build(), getDuration());

    }

    public int getDuration() {
        return 1000;
    }

    @OnClick(R.id.pan_up)
    public void onPanUp(View view) {
        if (!checkReady()) {
            return;
        }

        float currentTilt = mStreetViewPanorama.getPanoramaCamera().tilt;
        float newTilt = currentTilt + PAN_BY_DEG;

        newTilt = (newTilt > 90) ? 90 : newTilt;

        mStreetViewPanorama.animateTo(
                new StreetViewPanoramaCamera.Builder()
                        .zoom(mStreetViewPanorama.getPanoramaCamera().zoom)
                        .tilt(newTilt)
                        .bearing(mStreetViewPanorama.getPanoramaCamera().bearing)
                        .build(), getDuration());
    }

    @OnClick(R.id.pan_down)
    public void onPanDown(View view) {
        if (!checkReady()) {
            return;
        }

        float currentTilt = mStreetViewPanorama.getPanoramaCamera().tilt;
        float newTilt = currentTilt - PAN_BY_DEG;

        newTilt = (newTilt < -90) ? -90 : newTilt;

        mStreetViewPanorama.animateTo(
                new StreetViewPanoramaCamera.Builder()
                        .zoom(mStreetViewPanorama.getPanoramaCamera().zoom)
                        .tilt(newTilt)
                        .bearing(mStreetViewPanorama.getPanoramaCamera().bearing)
                        .build(), getDuration());
    }

    public void onRequestPosition(View view) {
        if (!checkReady()) {
            return;
        }
        if (mStreetViewPanorama.getLocation() != null) {
            Toast.makeText(view.getContext(), mStreetViewPanorama.getLocation().position.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(brUpdate, new IntentFilter(LumiManager.ACTION_DATA_UPDATE));
        hideElements();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(brUpdate);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LumiManager.getInstance(getActivity()).scanLeDevice(false, null);
        LumiManager.getInstance(getActivity()).disconnect(false, null);
        handler.removeCallbacks(hideTask);
    }

    @OnClick(R.id.move_position)
    public void onMovePosition() {
        StreetViewPanoramaLocation location = mStreetViewPanorama.getLocation();
        StreetViewPanoramaCamera camera = mStreetViewPanorama.getPanoramaCamera();
        if (location != null && location.links != null) {
            StreetViewPanoramaLink link = findClosestLinkToBearing(location.links, camera.bearing);
            mStreetViewPanorama.setPosition(link.panoId);
        }
    }

    public static StreetViewPanoramaLink findClosestLinkToBearing(StreetViewPanoramaLink[] links,
                                                                  float bearing) {
        float minBearingDiff = 360;
        StreetViewPanoramaLink closestLink = links[0];
        for (StreetViewPanoramaLink link : links) {
            if (minBearingDiff > findNormalizedDifference(bearing, link.bearing)) {
                minBearingDiff = findNormalizedDifference(bearing, link.bearing);
                closestLink = link;
            }
        }
        return closestLink;
    }

    // Find the difference between angle a and b as a value between 0 and 180
    public static float findNormalizedDifference(float a, float b) {
        float diff = a - b;
        float normalizedDiff = diff - (float) (360 * Math.floor(diff / 360.0f));
        return (normalizedDiff < 180.0f) ? normalizedDiff : 360.0f - normalizedDiff;
    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onDisconnected() {

    }
}
