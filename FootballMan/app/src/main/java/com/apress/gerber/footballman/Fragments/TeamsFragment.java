package com.apress.gerber.footballman.Fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apress.gerber.footballman.Constants;
import com.apress.gerber.footballman.MainActivity;
import com.apress.gerber.footballman.Models.DataManager;
import com.apress.gerber.footballman.Models.Game;
import com.apress.gerber.footballman.Models.League;
import com.apress.gerber.footballman.Models.Team;
import com.apress.gerber.footballman.R;
import com.apress.gerber.footballman.TeamsAdapter;

import java.util.List;


public class TeamsFragment extends BaseFragment implements TeamsAdapter.onClickedTeam, DataManager.OnTeamsLoaded {
    List<Team> mTeams;
    View view;
    Game game;
    static final String ARG_LEAGuE = "ARG_LEAGuE";
    static final String HIDE = "HIDE";

    public static TeamsFragment newInstance(League league, boolean hide) {
        TeamsFragment fragment = new TeamsFragment();
        Bundle args = new Bundle();
        args.putSerializable(HIDE, hide);
        args.putSerializable(ARG_LEAGuE, league);
        fragment.setArguments(args);
        return fragment;
    }

    private League league;

    League getLeague() {
        return league;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setActivity();
        if (getArguments() != null) {
            league = (League) getArguments().getSerializable(ARG_LEAGuE);
            hide = (boolean) getArguments().getSerializable(HIDE);
        }
    }

    private void setRecycleView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.teams);
        TeamsAdapter adapter = new TeamsAdapter(mTeams, this, hide);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        setLayout(view, mTeams.size(), R.string.no_teams);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_teams, container, false);
        ((MainActivity) getActivity()).setUpToolBar(league.getName());
        mManager.getTeamsForLeague(league, this);
        return view;
    }

    @Override
    public void onClickTeam(Team team) {
        ((MainActivity) getActivity()).commitFragment(PersonsFragment.newInstance(team, hide), true);
    }

    @Override
    public void deleteTeam(final Team team) {
        mManager.removeTeam(team);
        setLayout(view, mTeams.size(), R.string.no_teams);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean status = false;
        if (item.getItemId() == android.R.id.home) {
            ((MainActivity) getActivity()).commitFragment(HomeFragment.newInstance(hide), false);
            status = true;
        }
        if (item.getItemId() == Constants.MENU_NEXT) {
            game = mActivity.getGame();
            if ((game.readyHost() && game.readyGuest())) {
                mActivity.commitFragment(StartMatchFragment.newInstance(game), true);
            } else {
                Toast.makeText(getContext(), "First choose 2 teams", Toast.LENGTH_SHORT).show();
            }
        }
        if (item.getItemId() == Constants.MENU_ADD) {
            ((MainActivity) getActivity()).commitFragment(AddTeamFragment.newInstance(league, null), true);
            status = true;
        }
        return status;
    }

    @Override
    public void updateTeam(Team team) {
        ((MainActivity) getActivity()).commitFragment(AddTeamFragment.newInstance(league, team), true);
    }

    @Override
    public void onTeamLoaded(List<Team> teams) {
        mTeams = teams;
        setRecycleView(view);
    }
}
