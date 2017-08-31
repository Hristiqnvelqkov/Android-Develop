package com.apress.gerber.footballman.Fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apress.gerber.footballman.MainActivity;
import com.apress.gerber.footballman.Models.DataManager;
import com.apress.gerber.footballman.Models.League;
import com.apress.gerber.footballman.Models.Player;
import com.apress.gerber.footballman.Models.Team;
import com.apress.gerber.footballman.PersonsAdapter;
import com.apress.gerber.footballman.R;
import com.apress.gerber.footballman.TeamsAdapter;

import java.util.LinkedList;
import java.util.List;


public class TeamsFragment extends BaseFragment implements TeamsAdapter.onClickedTeam {
    DataManager dataManager = DataManager.getDataInstance();
    View view;
    static final String ARG_LEAGuE = "ARG_LEAGuE";
    public static TeamsFragment newInstance(League league) {
        TeamsFragment fragment = new TeamsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LEAGuE, league);
        fragment.setArguments(args);
        return fragment;
    }
    private League league;

    League getLeague(){
        return league;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            league = (League) getArguments().getSerializable(ARG_LEAGuE);
        }
    }
    private void setRecycleView(View view){
        RecyclerView recyclerView =view.findViewById(R.id.teams);
        TeamsAdapter adapter = new TeamsAdapter(league.getTeamList(),this);
        setLayout(view,recyclerView,adapter,R.string.no_teams);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_teams, container, false);
        setRecycleView(view);
        ((MainActivity) getActivity()).setUpToolBar(league.getName());
        return view;
    }

    @Override
    public void onClickTeam(Team team) {
        ((MainActivity) getActivity()).commitFragment(PersonsFragment.newInstance(team),true);
    }
    @Override
    public void deleteTeam(Team team){
        dataManager.removeTeam(league,team);
        setRecycleView(view);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        boolean status = false;
        if(item.getItemId()==android.R.id.home){
            ((MainActivity) getActivity()).commitFragment(new HomeFragment(),false);
            status=true;
        }
        if(item.getItemId() == R.id.test) {
            ((MainActivity) getActivity()).commitFragment(AddTeamFragment.newInstance(league, null), true);
            status = true;
        }
        return status;
    }
    @Override
    public void updateTeam(Team team){
        ((MainActivity) getActivity()).commitFragment(AddTeamFragment.newInstance(league,team),true);
    }
}
