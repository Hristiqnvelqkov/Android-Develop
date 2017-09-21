package com.apress.gerber.footballman.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.apress.gerber.footballman.MainActivity;
import com.apress.gerber.footballman.Models.DataManager;
import com.apress.gerber.footballman.Models.League;
import com.apress.gerber.footballman.R;

import io.realm.Realm;


public class AddLeagueFragment extends BaseFragment  {
    DataManager mManager = DataManager.getDataInstance();
    View view;
    Button save;
    League mLeague=null;
    public static AddLeagueFragment newInstance(League league){
        Bundle args = new Bundle();
        AddLeagueFragment fragment = new AddLeagueFragment();
        args.putSerializable("update",league);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_league, container, false);
        save  = (Button) view.findViewById(R.id.save_league);
        final TextInputLayout legue_input =  view.findViewById(R.id.league_name);
        legue_input.requestFocus();
        ((MainActivity) getActivity()).invisibleHome();
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mLeague==null) {
                    mManager.addLeague((legue_input.getEditText().getText().toString()));
                }else {
                    mManager.updateLeague(mLeague,(legue_input.getEditText().getText().toString()));
                }
                //mLeague.setName(legue_input.getEditText().getText().toString());
                ((MainActivity) getActivity()).visibleHome();
                ((MainActivity) getActivity()).commitFragment(HomeFragment.newInstance(false),true);
                imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS,0);
            }
        });
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mLeague = (League) getArguments().getSerializable("update");
    }
}
