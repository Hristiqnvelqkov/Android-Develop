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

import com.apress.gerber.footballman.Constants;
import com.apress.gerber.footballman.LegueRecyclerView;
import com.apress.gerber.footballman.MainActivity;
import com.apress.gerber.footballman.Models.Game;
import com.apress.gerber.footballman.Models.League;
import com.apress.gerber.footballman.R;

import java.util.List;


public class HomeFragment extends BaseFragment implements LegueRecyclerView.LeagueListener {
    View view;
    FloatingActionButton startMach;
    public static final String HIDE = "HIDE";
    List<League> mLeagues;
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
                menu.removeItem(Constants.MENU_NEXT);
                menu.add(0, Constants.MENU_ADD,Menu.NONE,R.string.add);
            }
            status=true;
        }
        if(item.getItemId() == Constants.MENU_ADD){
            ((MainActivity) getActivity()).commitFragment(AddLeagueFragment.newInstance(null), true);

        }
        if(item.getItemId() == Constants.ALL_GAMES){
            ((MainActivity) getActivity()).commitFragment(new GamesFragment(),true);
        }
        return status;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLeagues = mManager.getLeagues();
        view = inflater.inflate(R.layout.fragment_home, container, false);
        view.setTag("Home");
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
                menu.removeItem(Constants.MENU_ADD);
                menu.add(0,Constants.MENU_NEXT,Menu.NONE,R.string.next).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            }
        });
        return view;
    }
    @Override
    public void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);
        setHasOptionsMenu(true);
        setActivity();
        mLeagues = mManager.initList();
        if(mLeagues.size()>0) {
            for(League league : mLeagues){
//                DatabaseReference leagueChield = leagues.child(league.getName());
//                leagueChield.setValue(fakeLeague);
               // DatabaseReference teamChild = leagueChield.child(league.getLeaguesList().get(0).getName());
               // DatabaseReference player = teamChild.child(league.getTeamList().get(0).getPlayers().get(0).getName());
              //  player.child("Number").setValue(league.getTeamList().get(0).getPlayers().get(0).getNumber());
            }
        }
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
        LegueRecyclerView adapter = new LegueRecyclerView(mLeagues, this,hide);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyleLeagueView.setLayoutManager(manager);
        recyleLeagueView.setAdapter(adapter);
        setLayout(view,mLeagues.size(),R.string.no_leagues);
        if(!hide)
            startMach.setVisibility(View.VISIBLE);
    }
    @Override
    public void deleteLeague(final League league) {
        mManager.removeLeague(league);
        setLayout(view,mLeagues.size(),R.string.no_leagues);

    }
    @Override
    public void updateLeague(League league){
        ((MainActivity) getActivity()).commitFragment(AddLeagueFragment.newInstance(league),true);
    }

}
