package com.apress.gerber.footballman.Fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.apress.gerber.footballman.Constants;
import com.apress.gerber.footballman.ExportData;
import com.apress.gerber.footballman.GameAdapter;
import com.apress.gerber.footballman.MainActivity;
import com.apress.gerber.footballman.Models.DataManager;
import com.apress.gerber.footballman.Models.Game;
import com.apress.gerber.footballman.R;

import java.util.List;


public class GamesFragment extends BaseFragment implements GameAdapter.onGameClicked,DataManager.onGamesLoaded{
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_games, container, false);
        mManager.getGames(this);
        return view;
    }
    @Override
    public void onCreate(Bundle savedState){
        super.onCreate(savedState);
        setHasOptionsMenu(true);
    }
    @Override
    public void openStat(Game game) {
        ((MainActivity) getActivity()).commitFragment(StatisticsFragment.newInstance(game,false),true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        boolean status = false;
        if(item.getItemId() == Constants.EXPORT_GAMES){
            ExportData.getInstance().writeToFile("Game.csv",getActivity(),Constants.EXPORT_GAMES);
            status = true;
        }
        return status;
    }
    @Override
    public void onGamesLoaded(List<Game> games){
        RecyclerView recyclerGames =  view.findViewById(R.id.all_games);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        GameAdapter adapter = new GameAdapter(games,this);
        recyclerGames.setAdapter(adapter);
        recyclerGames.setLayoutManager(manager);
    }
}
