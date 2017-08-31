package com.apress.gerber.footballman.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.apress.gerber.footballman.MainActivity;
import com.apress.gerber.footballman.Models.DataManager;
import com.apress.gerber.footballman.Models.League;
import com.apress.gerber.footballman.Models.Team;
import com.apress.gerber.footballman.R;


public class AddTeamFragment extends BaseFragment {
    DataManager manager = DataManager.getDataInstance();
    View view;
    Button save;

    public static AddTeamFragment newInstance(League league,Team team) {
        Bundle args = new Bundle();
        AddTeamFragment fragment = new AddTeamFragment();
        args.putSerializable("test", league);
        args.putSerializable("leagueTeam",team);
        fragment.setArguments(args);
        return fragment;
    }
    private League league;
    private Team team;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_team, container, false);
        save =  view.findViewById(R.id.save_team);
        ((MainActivity) getActivity()).invisibleHome();
        final TextInputLayout team_name = view.findViewById(R.id.team_name);
        team_name.requestFocus();
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((league!=null) && (team==null)){
                    Team team = new Team();
                    team.setTeamName(team_name.getEditText().getText().toString());
                    team.setLeague(league);
                    manager.addTeam(league,team);
                }
                else{
                    team.setTeamName(team_name.getEditText().getText().toString());

                }
                ((MainActivity) getActivity()).visibleHome();
                imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS,0);
                ((MainActivity) getActivity()).commitFragment(TeamsFragment.newInstance(league),true);
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        league = (League )getArguments().getSerializable("test");
        team = (Team) getArguments().getSerializable("leagueTeam");
    }

}
