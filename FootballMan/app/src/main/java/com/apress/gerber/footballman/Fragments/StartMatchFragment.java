package com.apress.gerber.footballman.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.apress.gerber.footballman.Constants;
import com.apress.gerber.footballman.GamePlayersAdapter;
import com.apress.gerber.footballman.MainActivity;
import com.apress.gerber.footballman.Models.Game;
import com.apress.gerber.footballman.Models.Player;
import com.apress.gerber.footballman.R;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;


public class StartMatchFragment extends BaseFragment implements GamePlayersAdapter.onPlayerClicked {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String START_MATCH = "START_MATCH";
    View view;
    MyTask task;
    Timer timer;
    TextView hostResult, guestResut, hostTeamRedCard, guestTeamRedCard, hostTeamYellowCard, guestTeamYellowCard, hostTeamFauls, guestTeamFauls;
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
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mGame = (Game) getArguments().getSerializable(START_MATCH);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        System.out.println("Stana losho");
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        menu.removeItem(Constants.MENU_ADD);
        menu.add(0, Constants.START, Menu.NONE, R.string.start).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean status = false;
        if (item.getItemId() == Constants.START) {
            menu.removeItem(Constants.START);
            timer.schedule(task, 1000, 1000);
            menu.add(0, Constants.HALF_TIME, Menu.NONE, R.string.end_first).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            status = true;
        }
        if (item.getItemId() == Constants.HALF_TIME) {
            timer.cancel();
            menu.add(0, Constants.START_SECOND_HALF, Menu.NONE, R.string.start).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.removeItem(Constants.HALF_TIME);
            status = true;
        }
        if (item.getItemId() == Constants.START_SECOND_HALF) {
            menu.removeItem(Constants.START_SECOND_HALF);
            timer = new Timer();
            task = new MyTask();
            timer.schedule(task, 1000, 1000);
            menu.add(0, Constants.END_MATCH, Menu.NONE, R.string.full_time);
            status = true;
        }
        if (item.getItemId() == Constants.END_MATCH) {
            status = true;
            timer.cancel();
            ((MainActivity) getActivity()).commitFragment(StatisticsFragment.newInstance(mGame),true);
        }
        return status;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_start_match, container, false);
        initializeViews();
        task = new MyTask();
        timer = new Timer(true);
        return view;
    }

    private void initializeViews() {
        RecyclerView hostPlayers = view.findViewById(R.id.home_players);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        LinearLayoutManager manager1 = new LinearLayoutManager(getActivity());
        RecyclerView guestPlayers = view.findViewById(R.id.guest_players);
        guestResut = view.findViewById(R.id.guest_result);
        TextView hostName = view.findViewById(R.id.host_name);
        hostTeamFauls = view.findViewById(R.id.team_fauls_host);
        hostTeamRedCard = view.findViewById(R.id.red_card_number_host);
        guestTeamRedCard = view.findViewById(R.id.red_card_number);
        hostTeamYellowCard = view.findViewById(R.id.yellow_card_number_host);
        guestTeamYellowCard = view.findViewById(R.id.yellow_card_number);
        guestTeamFauls = view.findViewById(R.id.team_fauls);

        TextView guestName = view.findViewById(R.id.guest_name);
        hostName.setText(mGame.getHost().getName());
        guestName.setText(mGame.getGuest().getName());
        hostResult = view.findViewById(R.id.home_result);

        GamePlayersAdapter hostAdapter = new GamePlayersAdapter(mGame, mGame.getHostPlayers(), this);
        GamePlayersAdapter guestAdapter = new GamePlayersAdapter(mGame, mGame.getGuestTeamPlayers(), this);
        hostPlayers.setLayoutManager(manager);
        guestPlayers.setLayoutManager(manager1);
        hostPlayers.setAdapter(hostAdapter);
        guestPlayers.setAdapter(guestAdapter);
    }

    @Override
    public void addRedCard(Player player) {
        mGame.addRedCard(player,task.getSeconds());
        if (mGame.getHostPlayers().contains(player)) {
            hostTeamRedCard.setText(String.valueOf(mGame.getTeamRedCards(mGame.getHostPlayers())));
        } else {
            guestTeamRedCard.setText(String.valueOf(mGame.getTeamRedCards(mGame.getGuestTeamPlayers())));
        }
    }

    @Override
    public void addYellowCard(Player player) {
        mGame.addYellowCard(player,task.getSeconds());
        if (mGame.getHostPlayers().contains(player)) {
            hostTeamYellowCard.setText(String.valueOf(mGame.getTeamYellowCards(mGame.getHostPlayers())));
        } else {
            guestTeamYellowCard.setText(String.valueOf(mGame.getTeamYellowCards(mGame.getGuestTeamPlayers())));
        }
    }

    @Override
    public void addGoal(Player player) {
        mGame.addGoal(player,task.getSeconds());
        if (mGame.getHostPlayers().contains(player)) {
            hostResult.setText(String.valueOf(mGame.updateResult(mGame.getHost())));
        } else {
            guestResut.setText(String.valueOf(mGame.updateResult(mGame.getGuest())));
        }
    }

    @Override
    public void addAssist(Player player) {
        mGame.addAssist(player);
    }

    @Override
    public void addFaul(Player player) {
        mGame.addFaul(player,task.getSeconds());
        if (mGame.getHostPlayers().contains(player)) {
            hostTeamFauls.setText(String.valueOf(mGame.getTeamFauls(mGame.getHostPlayers())));
        } else {
            guestTeamFauls.setText(String.valueOf(mGame.getTeamFauls(mGame.getGuestTeamPlayers())));
        }
    }

    class MyTask extends TimerTask {
        int seconds=0;
        public int getSeconds(){
            return seconds;
        }
        @Override
        public void run() {
            seconds++;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((MainActivity) getActivity()).getSupportActionBar().setTitle(String.valueOf(seconds / 60) + " : " + String.valueOf(seconds % 60));
                }
            });

        }
    }

    ;
}
