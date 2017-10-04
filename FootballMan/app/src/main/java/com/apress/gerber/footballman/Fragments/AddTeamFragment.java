package com.apress.gerber.footballman.Fragments;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.apress.gerber.footballman.MainActivity;
import com.apress.gerber.footballman.Models.League;
import com.apress.gerber.footballman.Models.Team;
import com.apress.gerber.footballman.R;


public class AddTeamFragment extends BaseFragment {

    View view;
    Button save;

    public static AddTeamFragment newInstance(League league,Team team) {
        Bundle args = new Bundle();
        AddTeamFragment fragment = new AddTeamFragment();
        args.putString("test", league.getId());
        if(team!=null) {
            args.putString("leagueTeam", team.getId());
        }
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
                if(team==null) {
                    mManager.addTeam(league,(team_name.getEditText().getText().toString()));
                }else {
                    mManager.updateTeam(team,(team_name.getEditText().getText().toString()));
                }
                ((MainActivity) getActivity()).visibleHome();
                imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS,0);
                ((MainActivity) getActivity()).commitFragment(TeamsFragment.newInstance(league,false),true);
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        String teamId = getArguments().getString("leagueTeam");
        String leagueId = getArguments().getString("test");
        league = mManager.getLeagueById(leagueId);
        team = mManager.getTeamById(teamId);
    }

}
