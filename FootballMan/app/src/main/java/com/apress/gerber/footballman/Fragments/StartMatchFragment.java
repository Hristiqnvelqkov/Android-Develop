package com.apress.gerber.footballman.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apress.gerber.footballman.Constants;
import com.apress.gerber.footballman.GamePlayersAdapter;
import com.apress.gerber.footballman.GoToHomeDialog;
import com.apress.gerber.footballman.LandGameAdapter;
import com.apress.gerber.footballman.MainActivity;
import com.apress.gerber.footballman.Models.Game;
import com.apress.gerber.footballman.Models.Player;
import com.apress.gerber.footballman.R;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


public class StartMatchFragment extends BaseFragment implements GamePlayersAdapter.onPlayerClicked {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String START_MATCH = "START_MATCH";
    ViewGroup view;
    int stateChange = 0;
    RecyclerView hostPlayers;
    RecyclerView guestPlayers;
    MyTask task;
    int halftime=1;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;
    LandGameAdapter mLandHostAdapter = null;
    LandGameAdapter mLandGuestAdapter = null;
    GamePlayersAdapter hostAdapter = null;
    GamePlayersAdapter guestAdapter;
    Timer timer;
    boolean isTablet = false;
    Player readyForOutPlayer;
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
        mSharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        if (getArguments() != null) {
            mGame = (Game) getArguments().getSerializable(START_MATCH);
            int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
            if(screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE){
                isTablet = true;
                hostAdapter = new GamePlayersAdapter(mGame, mGame.getHostPlayers(), this, true);
                guestAdapter = new GamePlayersAdapter(mGame, mGame.getGuestTeamPlayers(), this, true);
                mLandHostAdapter = new LandGameAdapter(mGame, mGame.getHostPlayers(), this,true);
                mLandGuestAdapter = new LandGameAdapter(mGame, mGame.getGuestTeamPlayers(), this,true);
            }else {
                isTablet = false;
                hostAdapter = new GamePlayersAdapter(mGame, mGame.getHostPlayers(), this, false);
                guestAdapter = new GamePlayersAdapter(mGame, mGame.getGuestTeamPlayers(), this, false);
                mLandHostAdapter = new LandGameAdapter(mGame, mGame.getHostPlayers(), this,false);
                mLandGuestAdapter = new LandGameAdapter(mGame, mGame.getGuestTeamPlayers(), this,false);
            }
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        menu.removeItem(Constants.MENU_ADD);
        menu.add(0, Constants.START, Menu.NONE, R.string.start).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    public boolean checkMatchStarted() {
        if (task.getSeconds() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean status = false;
        if(item.getItemId() == android.R.id.home){
            GoToHomeDialog dialog = new GoToHomeDialog((MainActivity) getActivity(),this);
        }
        if (item.getItemId() == Constants.START) {
            menu.removeItem(Constants.START);
            timer.schedule(task, 1000, 1000);
            menu.add(0, Constants.HALF_TIME, Menu.NONE, R.string.end_first).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            status = true;
        }
        if (item.getItemId() == Constants.HALF_TIME) {
            timer.cancel();
            task.setHalfTime();
            halftime=2;
            menu.add(0, Constants.START_SECOND_HALF, Menu.NONE, R.string.start).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.removeItem(Constants.HALF_TIME);
            status = true;
        }
        if (item.getItemId() == Constants.START_SECOND_HALF) {
            menu.removeItem(Constants.START_SECOND_HALF);
            timer = new Timer(false);
            task = new MyTask();
            timer.schedule(task, 1000, 1000);
            menu.add(0, Constants.END_MATCH, Menu.NONE, R.string.full_time);
            status = true;
        }
        if (item.getItemId() == Constants.END_MATCH) {
            status = true;
            timer.cancel();
            final EditText editText = new EditText(getContext());
            String venue = mSharedPreferences.getString("venue","");
            editText.setText(venue);
            AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
            dialog.setTitle("Enter Venue");
            dialog.setView(editText);
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mGame.setVenue(editText.getText().toString());
                    mEditor.putString("venue",mGame.getVenue());
                    mEditor.commit();
                    Calendar calendar = Calendar.getInstance();
                    String mounth = String.valueOf(calendar.get(Calendar.MONTH));
                    String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                    String year = String.valueOf(calendar.get(Calendar.YEAR));
                    String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
                    String minutes = String.valueOf(calendar.get(Calendar.MINUTE));
                    if(Integer.parseInt(hour)< 10){
                        hour ="0" + hour;
                    }
                    if(Integer.parseInt(minutes) < 10){
                        hour = "0" +minutes;
                    }
                    mGame.setStartTime(hour+":"+minutes+":00");
                    mGame.setMatchDate(year+"/"+mounth+"/"+day);
                    mManager.addGame(mGame);
                }
            });
            dialog.show();
            ((MainActivity) getActivity()).commitFragment(StatisticsFragment.newInstance(mGame,true), true);
        }
        return status;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = (FrameLayout) inflater.inflate(R.layout.frame_layout_match, container, false);
        View childview = inflater.inflate(R.layout.fragment_start_match, container, false);
        view.addView(childview);

        task = new MyTask();
        ((MainActivity) getActivity()).setUpToolBar(String.valueOf(task.getSeconds()));
        timer = new Timer(true);
        initializeViews();
        setAdapter(false);
        return view;
    }

    public void setAdapter(boolean land) {
        if (land) {
            hostPlayers.setAdapter(mLandHostAdapter);
            guestPlayers.setAdapter(mLandGuestAdapter);
        } else {
            hostPlayers.setAdapter(hostAdapter);
            guestPlayers.setAdapter(guestAdapter);
        }
    }

    private void initializeViews() {
        hostPlayers = view.findViewById(R.id.home_players);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        DividerItemDecoration devider = new DividerItemDecoration(getContext(),manager.getOrientation());
        LinearLayoutManager manager1 = new LinearLayoutManager(getActivity());
        guestPlayers = view.findViewById(R.id.guest_players);
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
        guestResut.setText(String.valueOf(mGame.getGuestResult()));
        hostResult.setText(String.valueOf(mGame.getHostResult()));

        hostTeamFauls.setText(String.valueOf(mGame.getTeamFauls(mGame.getHostPlayers())));
        guestTeamFauls.setText(String.valueOf(mGame.getTeamFauls(mGame.getGuestTeamPlayers())));

        hostTeamYellowCard.setText(String.valueOf(mGame.getTeamYellowCards(mGame.getHostPlayers())));
        guestTeamYellowCard.setText(String.valueOf(mGame.getTeamYellowCards(mGame.getGuestTeamPlayers())));

        hostTeamRedCard.setText(String.valueOf(mGame.getTeamRedCards(mGame.getHostPlayers())));
        guestTeamRedCard.setText(String.valueOf(mGame.getTeamRedCards(mGame.getGuestTeamPlayers())));
        hostPlayers.setLayoutManager(manager);
        hostPlayers.addItemDecoration(devider);
        guestPlayers.addItemDecoration(devider);
        guestPlayers.setLayoutManager(manager1);

    }

    @Override
    public void addRedCard(Player player) {
        mGame.addRedCard(player, task.getSeconds(),halftime);
        if (mGame.getHostPlayers().contains(player)) {
            hostTeamRedCard.setText(String.valueOf(mGame.getTeamRedCards(mGame.getHostPlayers())));
        } else {
            guestTeamRedCard.setText(String.valueOf(mGame.getTeamRedCards(mGame.getGuestTeamPlayers())));
        }
    }

    @Override
    public void addYellowCard(Player player) {
        mGame.addYellowCard(player, task.getSeconds(),halftime);
        if (mGame.getHostPlayers().contains(player)) {
            hostTeamYellowCard.setText(String.valueOf(mGame.getTeamYellowCards(mGame.getHostPlayers())));
        } else {
            guestTeamYellowCard.setText(String.valueOf(mGame.getTeamYellowCards(mGame.getGuestTeamPlayers())));
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        view.removeAllViews();
        View childView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_start_match, null);
        view.addView(childView);
        initializeViews();
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setAdapter(true);
        } else {
            setAdapter(false);
        }
    }

    @Override
    public void addGoal(Player player) {
        mGame.addGoal(player, task.getSeconds(),halftime);
        if (mGame.getHostPlayers().contains(player)) {
            hostResult.setText(String.valueOf(mGame.updateResult(mGame.getHost())));
        } else {
            guestResut.setText(String.valueOf(mGame.updateResult(mGame.getGuest())));
        }
    }

    @Override
    public void addAssist(Player player) {
        mGame.addAssist(player,task.getSeconds(),halftime);
    }

    @Override
    public int onChangeListner(Player player) {
        int stat = 0;
        if (stateChange == 1) {
            mGame.outOfGame(readyForOutPlayer);
            mGame.enterInGame(player);
            readyForOutPlayer = null;
            stateChange = 0;
            stat = Constants.IN;
        } else {
            if (task.getSeconds() == 0) {
                if (mGame.getHostPlayersInGame().contains(player) || mGame.getGuestPlayersInGame().contains(player)) {
                    mGame.outOfGame(player);
                    stat = Constants.OUT;
                } else {
                    mGame.enterInGame(player);
                    stat = Constants.IN;
                }
            } else {
                if (mGame.getHostPlayersInGame().contains(player) || mGame.getGuestPlayersInGame().contains(player)) {
                    stat = Constants.READY_FOR_CHANGE;
                    readyForOutPlayer = player;
                    stateChange = 1;
                }

            }
        }
        return stat;
    }
    public void stopTask(){
        timer.cancel();
        task.cancel();
    }
    @Override
    public void addFaul(Player player) {
        mGame.addFaul(player, task.getSeconds(),halftime);
        if (mGame.getHostPlayers().contains(player)) {
            hostTeamFauls.setText(String.valueOf(mGame.getTeamFauls(mGame.getHostPlayers())));
        } else {
            guestTeamFauls.setText(String.valueOf(mGame.getTeamFauls(mGame.getGuestTeamPlayers())));
        }
    }

    @Override
    public void addPenalty(Player player) {
        mGame.addPenalty(player,task.getSeconds(),halftime);
    }

    @Override
    public void autoGoal(Player player) {
        mGame.addAutoGoal(player,task.getSeconds(),halftime);
        Toast.makeText(getContext(),getString(R.string.autogoal),Toast.LENGTH_SHORT).show();
    }

    class MyTask extends TimerTask {
        int seconds = 0;

        public int getSeconds() {
            return seconds;
        }

        public void setHalfTime() {
            seconds = 0;
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
