package com.apress.gerber.footballman.Fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apress.gerber.footballman.LegueRecyclerView;
import com.apress.gerber.footballman.MainActivity;
import com.apress.gerber.footballman.Models.DataManager;
import com.apress.gerber.footballman.Models.League;
import com.apress.gerber.footballman.R;

import java.util.List;


public class HomeFragment extends BaseFragment implements LegueRecyclerView.LeagueListener {
    View view;
    RelativeLayout empty_layout;
    DataManager dataManager = DataManager.getDataInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        setRecyclerView();
        return view;
    }
    @Override
    public void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onLeagueClicked(League league) {
        ((MainActivity) getActivity()).commitFragment(TeamsFragment.newInstance(league), true);
    }
    public void setRecyclerView(){
        RecyclerView recyleLeagueView = view.findViewById(R.id.recycle_leagues);
        LegueRecyclerView adapter = new LegueRecyclerView(dataManager.getLeagues(), this);
        setLayout(view,recyleLeagueView,adapter,R.string.no_leagues);
    }
    @Override
    public void deleteLeague(League league) {
        dataManager.removeLeague(league);
        setRecyclerView();
    }
    @Override
    public void updateLeague(League league){
        ((MainActivity) getActivity()).commitFragment(AddLeagueFragment.newInstance(league),true);
    }
}
