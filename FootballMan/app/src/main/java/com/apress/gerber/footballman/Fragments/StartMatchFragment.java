package com.apress.gerber.footballman.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apress.gerber.footballman.Models.Game;
import com.apress.gerber.footballman.R;


public class StartMatchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String START_MATCH = "START_MATCH";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Game mGame;

    public StartMatchFragment() {
        // Required empty public constructor
    }

    public static StartMatchFragment newInstance(Game game) {
        StartMatchFragment fragment = new StartMatchFragment();
        Bundle args = new Bundle();
        args.putSerializable(START_MATCH, game);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(START_MATCH);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_match, container, false);
    }


}
