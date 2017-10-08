package com.apress.gerber.footballman.Fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apress.gerber.footballman.GameAdapter;
import com.apress.gerber.footballman.MainActivity;
import com.apress.gerber.footballman.Models.Game;
import com.apress.gerber.footballman.R;

import java.util.List;


public class GamesFragment extends BaseFragment implements GameAdapter.onGameClicked{
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_games, container, false);
        mManager.getGames(this);
        return view;
    }

    @Override
    public void openStat(Game game) {
        ((MainActivity) getActivity()).commitFragment(StatisticsFragment.newInstance(game,false),true);
    }
    public void onGamesLoaded(List<Game> games){
        RecyclerView recyclerGames =  view.findViewById(R.id.all_games);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        GameAdapter adapter = new GameAdapter(games,this);
        recyclerGames.setAdapter(adapter);
        recyclerGames.setLayoutManager(manager);
    }
}
