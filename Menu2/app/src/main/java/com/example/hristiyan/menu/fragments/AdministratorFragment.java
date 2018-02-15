package com.example.hristiyan.menu.fragments;

import android.app.AlertDialog;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.hristiyan.menu.MenuApplication;
import com.example.hristiyan.menu.livedatas.MenuViewModel;
import com.example.hristiyan.menu.MainActivity;
import com.example.hristiyan.menu.MenusAdapter;
import com.example.hristiyan.menu.R;
import com.example.hristiyan.menu.data.Menu;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


public class AdministratorFragment extends BaseFragment implements MenusAdapter.OnMenuItemClicked {
    private MenuViewModel menuViewModel;
    private Observer menuObserver;
    MenusAdapter adapter;
    List<Menu> curretntMenus = new LinkedList<>();

    public static AdministratorFragment newInstance() {
        Bundle args = new Bundle();
        AdministratorFragment fragment = new AdministratorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_administrator;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new MenusAdapter(this);
        menuViewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(MenuViewModel.class);

        menuObserver = new Observer<List<Menu>>() {
            @Override
            public void onChanged(@Nullable List<Menu> menus) {
                curretntMenus = menus;
                MenuApplication.getMenuApplication().getDataManager().setLocalMenus(menus);
                Collections.sort(menus, new Comparator<Menu>() {
                    @Override
                    public int compare(Menu o1, Menu o2) {
                        return o1.getDate() > o2.getDate() ? -1 : (o1.getDate() < o2.getDate()) ? 1 : 0;
                    }
                });
                adapter.updateMenus(menus);
            }
        };
        menuViewModel.observe((LifecycleOwner) getActivity(), menuObserver);
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean status = false;
        if(item.getItemId() == android.R.id.home){
            getActivity().onBackPressed();
            status = true;
        }
        return status;
    }
    @Override
    public void onCreateView() {
        final RecyclerView menus = view.findViewById(R.id.all_menus);
        menus.setAdapter(adapter);
        menuViewModel.loadMenus();
        Button addMenu = view.findViewById(R.id.add_button);
        addMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                dialogBuilder.setView(dialogView);
                final EditText edt = dialogView.findViewById(R.id.edit1);
                dialogBuilder.setTitle(getString(R.string.add_menu));
                dialogBuilder.setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        for(Menu men : curretntMenus){
                            if(men.isActive()){
                                men.setActive(false);
                                MenuApplication.getMenuApplication().getDataManager().updateMenu(men);
                            }
                        }
                        Menu menu = new Menu();
                        menu.setName(edt.getText().toString());
                        menu.setDate(System.currentTimeMillis());
                        menu.setActive(true);
                        MenuApplication.getMenuApplication().getDataManager().addMenu(menu);
                        ((MainActivity) getActivity()).showFragmentAndAddToBackstack(MenuFragment.newInstance(menu, true, false));
                    }
                });
                dialogBuilder.setNegativeButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //pass
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();
            }
        });
        GridLayoutManager recyclerManager = new GridLayoutManager(getActivity(),2);
        menus.setLayoutManager(recyclerManager);
    }

    @Override
    public void onItemClicked(Menu menu) {
        ((MainActivity) getActivity()).showFragmentAndAddToBackstack(MenuFragment.newInstance(menu, menu.isActive(), false)); //now false hardcoded means not show active menu
    }

    @Override
    public void deleteMenu(Menu menu) {
        menuViewModel.deleteMenu(menu);
    }

}
