package com.apress.gerber.footballman.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.apress.gerber.footballman.LegueRecyclerView;
import com.apress.gerber.footballman.MainActivity;
import com.apress.gerber.footballman.Models.DataManager;
import com.apress.gerber.footballman.Models.Game;
import com.apress.gerber.footballman.Models.League;
import com.apress.gerber.footballman.R;


public class HomeFragment extends BaseFragment implements LegueRecyclerView.LeagueListener {
    View view;
    FloatingActionButton startMach;
    DataManager dataManager = DataManager.getDataInstance();
    public static final String HIDE = "HIDE";
    public static HomeFragment newInstance(Boolean hide){
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putSerializable(HIDE,hide);
        fragment.setArguments(args);
        return fragment;
    }
    @Override public boolean onOptionsItemSelected(MenuItem item){
        boolean status = false;
        MainActivity activity = (MainActivity) getActivity();
        if(item.getItemId()==android.R.id.home){
            if(!hide) {
                DrawerLayout drawerLayout = activity.drawer;
                drawerLayout.openDrawer(Gravity.LEFT);
            }else{
                hide = false;
                startMach.setVisibility(View.VISIBLE);
                setRecyclerView(hide);
                menu.removeItem(1);
                menu.add(0,0,Menu.NONE,R.string.add);
            }
            status=true;
        }
        if(item.getItemId() == 0){
            ((MainActivity) getActivity()).commitFragment(AddLeagueFragment.newInstance(null), true);

        }
        return status;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        startMach = view.findViewById(R.id.floatingActionButton);
        setRecyclerView(hide);
        startMach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide =true;
                ((MainActivity) getActivity()).startGame(new Game());
                menu.removeItem(Menu.FIRST);
                startMach.setVisibility(View.GONE);
                setRecyclerView(hide);
                menu.removeItem(0);
                menu.add(0,1,Menu.NONE,R.string.next).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            }
        });
        return view;
    }
    @Override
    public void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);
        setHasOptionsMenu(true);
        setActivity();
        if(getArguments()!=null){
            hide = (boolean) getArguments().getSerializable(HIDE);
        }
    }

    @Override
    public void onLeagueClicked(League league,boolean hide) {
        ((MainActivity) getActivity()).commitFragment(TeamsFragment.newInstance(league,hide), true);
    }
    public void setRecyclerView(boolean hide){
        RecyclerView recyleLeagueView = view.findViewById(R.id.recycle_leagues);
        LegueRecyclerView adapter = new LegueRecyclerView(dataManager.getLeagues(), this,hide);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyleLeagueView.setLayoutManager(manager);
        recyleLeagueView.setAdapter(adapter);
        setLayout(view,dataManager.getLeagues().size(),R.string.no_leagues);
        if(!hide)
            startMach.setVisibility(View.VISIBLE);
    }
    @Override
    public void deleteLeague(League league) {
        dataManager.removeLeague(league);
        setLayout(view,dataManager.getLeagues().size(),R.string.no_leagues);

    }
    @Override
    public void updateLeague(League league){
        ((MainActivity) getActivity()).commitFragment(AddLeagueFragment.newInstance(league),true);
    }

}
