package example.streetview.stamsoft.app.streetviewexample.fragments;

import android.content.DialogInterface;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import example.streetview.stamsoft.app.streetviewexample.LumiManager;
import example.streetview.stamsoft.app.streetviewexample.R;
import example.streetview.stamsoft.app.streetviewexample.Utils;
import example.streetview.stamsoft.app.streetviewexample.adapters.SettingsAdapter;

public class SettingsFragment extends BaseFragment {

    @BindView(R.id.list)
    RecyclerView list;
    SettingsAdapter adapter;
    @BindView(R.id.settingsSelectedTabler)
    TextView selectedTablet;

    @Override
    protected int getLayoutId() {
        return R.layout.settings_fragment;
    }

    @Override
    protected void onCreateView() {
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new SettingsAdapter(Utils.getSavedDevice(getActivity()));
        list.setAdapter(adapter);

        setHasOptionsMenu(true);
        getMainActivity().getSupportActionBar().show();

        selectedTablet.setText(getString(R.string.select_tablet,
                PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt(getString(R.string.pref_selected_tablet), 1)));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.toolbar_main, menu);

        MenuItem settings = menu.findItem(R.id.menu_item_settings);
        settings.setTitle(getString(R.string.save));

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_item_settings:
                LumiManager.getInstance(getActivity()).getDevicesName().clear();
                LumiManager.getInstance(getActivity()).getDevices().clear();
                for (String str : adapter.getItems()) {
                    if (!TextUtils.isEmpty(str)) {
                        LumiManager.getInstance(getActivity()).getDevicesName().add(str);
                    }
                }
                Utils.saveDevices(LumiManager.getInstance(getActivity()).getDevicesName(), getActivity());
                getMainActivity().onBackPressed();
                break;
            case android.R.id.home:
                getMainActivity().finish();
                break;
        }

        return true;
    }

    @OnClick(R.id.settingsSelectedTabler)
    void onTabletSelectClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        int selectedItem = PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt(getString(R.string.pref_selected_tablet), 1);
        String[] choices = getResources().getStringArray(R.array.tablet_selects);
        builder.setSingleChoiceItems(choices,
                selectedItem - 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putInt(getString(R.string.pref_selected_tablet), i + 1).commit();
                        selectedTablet.setText(getString(R.string.select_tablet,
                                PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt(getString(R.string.pref_selected_tablet), 1)));
                    }
                });
        builder.create().show();
    }

    @Override
    protected void setupToolbar() {

    }
}
