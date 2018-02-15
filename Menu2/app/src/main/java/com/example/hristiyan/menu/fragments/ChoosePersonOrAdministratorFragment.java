package com.example.hristiyan.menu.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hristiyan.menu.MenuApplication;
import com.example.hristiyan.menu.data.DataManager;
import com.example.hristiyan.menu.MainActivity;
import com.example.hristiyan.menu.R;
import com.example.hristiyan.menu.data.Food;
import com.example.hristiyan.menu.data.Menu;


public class ChoosePersonOrAdministratorFragment extends BaseFragment {
    public static final String DEFAULTPASSWORD = "123456";
    public static ChoosePersonOrAdministratorFragment newInstance() {
        Bundle args = new Bundle();
        ChoosePersonOrAdministratorFragment fragment = new ChoosePersonOrAdministratorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_choose_person_or_administrator;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateView() {
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
        Button administrator = view.findViewById(R.id.administrator_button);
        Button user = view.findViewById(R.id.user_button);
        administrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.custom_password, null);
                final EditText edt = dialogView.findViewById(R.id.password);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setTitle(getString(R.string.enter_password));
                dialogBuilder.setPositiveButton(getString(R.string.login), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(edt.getText().toString().equals(DEFAULTPASSWORD)){
                            ((MainActivity) getActivity()).showFragmentAndAddToBackstack(AdministratorFragment.newInstance());
                        }else{
                            Toast.makeText(getActivity(),getString(R.string.wrong_password),Toast.LENGTH_LONG).show();
                        }
                    }
                });
                dialogBuilder.setNegativeButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Menu menu = MenuApplication.getMenuApplication().getDataManager().getActiveMenu();
                if (menu != null) {
                    ((MainActivity) getActivity()).showFragmentAndAddToBackstack(MenuFragment.newInstance(menu, true,true));
                }else{
                    Toast.makeText(getActivity(),getString(R.string.no_active_menu),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
