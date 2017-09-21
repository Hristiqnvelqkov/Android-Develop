package com.apress.gerber.footballman.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apress.gerber.footballman.GameAdapter;
import com.apress.gerber.footballman.MainActivity;
import com.apress.gerber.footballman.Models.Game;
import com.apress.gerber.footballman.R;


public class GamesFragment extends BaseFragment implements GameAdapter.onGameClicked{
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_games, container, false);
        RecyclerView games =  view.findViewById(R.id.all_games);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        GameAdapter adapter = new GameAdapter(mManager.getGames(),this);
        games.setAdapter(adapter);
        games.setLayoutManager(manager);
        return view;
    }

    @Override
    public void openStat(Game game) {
        ((MainActivity) getActivity()).commitFragment(StatisticsFragment.newInstance(game),true);
    }
}
