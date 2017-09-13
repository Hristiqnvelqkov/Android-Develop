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
import android.widget.TextView;

import com.apress.gerber.footballman.MainActivity;
import com.apress.gerber.footballman.Models.Game;
import com.apress.gerber.footballman.R;
import com.apress.gerber.footballman.StatisticsAdapter;

public class StatisticsFragment extends BaseFragment {
    private static final String GAME = "GAME";
    Game mGame;
    View view;
    // TODO: Rename and change types and number of parameters
    public static StatisticsFragment newInstance(Game game) {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle args = new Bundle();
        args.putSerializable(GAME, game);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGame = (Game) getArguments().getSerializable(GAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_statistics, container, false);
        initializeViews();
        return view;
    }
    public void initializeViews(){
        RecyclerView recyclerView = view.findViewById(R.id.factsRecyler);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        StatisticsAdapter adapter = new StatisticsAdapter(mGame.getEvents());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }
}
