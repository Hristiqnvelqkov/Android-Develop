package com.apress.gerber.footballman.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.apress.gerber.footballman.MainActivity;
import com.apress.gerber.footballman.Models.DataManager;
import com.apress.gerber.footballman.Models.League;
import com.apress.gerber.footballman.Models.Player;
import com.apress.gerber.footballman.Models.Team;
import com.apress.gerber.footballman.R;

import io.realm.Realm;


public class AddPersonFragment extends BaseFragment {
    DataManager manager = DataManager.getDataInstance();
    Button save;
    Team mTeam;
    public int personId=0;
    View fragment_view;
    Player mPlayer;
    TextInputLayout player_name;
    public static AddPersonFragment newInstance(Team team, Player player) {
        AddPersonFragment fragment = new AddPersonFragment();
        Bundle args = new Bundle();
        args.putSerializable("team", team);
        args.putSerializable("player",player);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragment_view = inflater.inflate(R.layout.fragment_add_person, container, false);
        save = fragment_view.findViewById(R.id.save_player);
        final TextInputLayout player_name = fragment_view.findViewById(R.id.player_name);
        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
        player_name.requestFocus();
        final TextInputLayout player_number = fragment_view.findViewById(R.id.player_number);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        ((MainActivity) getActivity()).invisibleHome();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPlayer==null) {
                    mManager.addPlayer(mTeam,(player_name.getEditText().getText().toString()),Integer.parseInt(player_number.getEditText().getText().toString()));
                }else {
                    mManager.updatePlayer(mPlayer,(player_name.getEditText().getText().toString()),(Integer.parseInt(player_number.getEditText().getText().toString())));
                }
                ((MainActivity) getActivity()).visibleHome();
                imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS,0);
                ((MainActivity) getActivity()).commitFragment(PersonsFragment.newInstance(mTeam,false),true);
            }
        });
        return fragment_view;
    }

    @Override public void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
        mTeam =(Team) getArguments().getSerializable("team");
        mPlayer = (Player) getArguments().getSerializable("player");
    }
}
