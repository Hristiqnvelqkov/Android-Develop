package com.apress.gerber.footballman.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.apress.gerber.footballman.Constants;
import com.apress.gerber.footballman.LegueRecyclerView;
import com.apress.gerber.footballman.MainActivity;
import com.apress.gerber.footballman.Models.DataManager;
import com.apress.gerber.footballman.Models.Game;
import com.apress.gerber.footballman.Models.League;
import com.apress.gerber.footballman.R;

import java.util.List;


public class HomeFragment extends BaseFragment implements LegueRecyclerView.LeagueListener,DataManager.OnLeagesLoaded {
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
              //  DrawerLayout drawerLayout = activity.drawer;
             //   drawerLayout.openDrawer(Gravity.LEFT);
            }else{
                ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                hide = false;
                startMach.setVisibility(View.VISIBLE);
                setRecyclerView(hide);
                menu.removeItem(Constants.MENU_NEXT);
                menu.add(0, Constants.MENU_ADD,Menu.NONE,R.string.add).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            }
            status=true;
        }
        if(item.getItemId() == Constants.MENU_ADD){
            ((MainActivity) getActivity()).commitFragment(AddLeagueFragment.newInstance(null), true);

        }
//        if(item.getItemId() == Constants.EXPORT_LEAGUES){
//            exportToDataBase("testaaa.txt",Constants.EXPORT_LEAGUES);
//        }
        if(item.getItemId() == Constants.ALL_GAMES){
            ((MainActivity) getActivity()).commitFragment(new GamesFragment(),true);
        }
        return status;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        startMach = view.findViewById(R.id.floatingActionButton);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("");
        ((MainActivity)getActivity()).getSupportActionBar().setLogo(R.drawable.spl_cropped_logo);
        if(!hide){
            ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        mManager.getLeagues(this);
        startMach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide =true;
                ((MainActivity) getActivity()).startGame(new Game());
                ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    @Override
    public void onLeaguesLoaded(List<League> leagues) {
        mLeagues = leagues;
        setRecyclerView(hide);
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        ((MainActivity) getActivity()).getSupportActionBar().setLogo(null);
    }
    @Override
    public boolean onBackPressed(){
        if(hide){
            getMainActivity().setModeForGame(MainActivity.FIRST_TEAM);
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            menu.removeItem(Constants.MENU_NEXT);
            menu.add(0,Constants.MENU_ADD,Menu.NONE,R.string.add).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            hide = false;
            return true;
        }else
            return  false;
    }
}
