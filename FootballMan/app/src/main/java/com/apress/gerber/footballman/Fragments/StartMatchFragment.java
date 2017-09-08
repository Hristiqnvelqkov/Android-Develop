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
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.apress.gerber.footballman.GamePlayersAdapter;
import com.apress.gerber.footballman.MainActivity;
import com.apress.gerber.footballman.Models.Game;
import com.apress.gerber.footballman.Models.Player;
import com.apress.gerber.footballman.R;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;


public class StartMatchFragment extends Fragment implements GamePlayersAdapter.onPlayerClicked{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String START_MATCH = "START_MATCH";
    View view;
    TextView hostResult,guestResut;
    // TODO: Rename and change types of parameters
    Game mGame;


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
            mGame =(Game) getArguments().getSerializable(START_MATCH);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_start_match, container, false);
        initializeViews();
        TimerTask task = new TimerTask() {
            int seconds=0;
            @Override
            public void run() {
                seconds++;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity) getActivity()).setUpToolBar(String.valueOf(seconds/60)+" : "+ String.valueOf(seconds%60));
                    }
                });

            }
        };
        Timer timer = new Timer(true);
        timer.schedule(task,1000,1000);
        return view;
    }
    private void initializeViews(){
        RecyclerView hostPlayers = view.findViewById(R.id.home_players);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        LinearLayoutManager manager1 = new LinearLayoutManager(getActivity());
        RecyclerView guestPlayers = view.findViewById(R.id.guest_players);
        guestResut = view.findViewById(R.id.guest_result);
        TextView hostName = view.findViewById(R.id.host_name);
        TextView guestName = view.findViewById(R.id.guest_name);
        hostName.setText(mGame.getHost().getName());
        guestName.setText(mGame.getGuest().getName());
        hostResult = view.findViewById(R.id.home_result);
        GamePlayersAdapter hostAdapter = new GamePlayersAdapter(mGame,mGame.getHostPlayers(),this);
        GamePlayersAdapter guestAdapter = new GamePlayersAdapter(mGame,mGame.getGuestTeamPlayers(),this);
        hostPlayers.setLayoutManager(manager);
        guestPlayers.setLayoutManager(manager1);
        hostPlayers.setAdapter(hostAdapter);
        guestPlayers.setAdapter(guestAdapter);
    }

    @Override
    public void addRedCard(Player player) {
        mGame.addRedCard(player);
    }

    @Override
    public void addYellowCard(Player player) {
        mGame.addYellowCard(player);
    }

    @Override
    public void addGoal(Player player) {
        mGame.addGoal(player);
        if(mGame.getHostPlayers().contains(player)){
            hostResult.setText(String.valueOf(mGame.updateResult(mGame.getHost())));
        }else{
            guestResut.setText(String.valueOf(mGame.updateResult(mGame.getGuest())));
        }
    }

    @Override
    public void addAssist(Player player) {
    }

    @Override
    public void addFaul(Player player) {
        mGame.addFaul(player);
    }
}
