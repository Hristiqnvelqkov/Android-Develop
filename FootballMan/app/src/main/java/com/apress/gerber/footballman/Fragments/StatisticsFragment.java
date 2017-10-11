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
import com.apress.gerber.footballman.MainActivity;
import com.apress.gerber.footballman.Models.Game;
import com.apress.gerber.footballman.R;
import com.apress.gerber.footballman.StatisticsAdapter;

import java.util.Calendar;

public class StatisticsFragment extends BaseFragment {
    private static final String GAME = "GAME";
    private static final String STAT_AFTER_GAME= "STATAFTERGAME";
    Game mGame;
    boolean statAfterGame;
    View view;
    public static StatisticsFragment newInstance(Game game,boolean statAfterGame) {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle args = new Bundle();
        args.putSerializable(GAME, game);
        args.putBoolean(STAT_AFTER_GAME,statAfterGame);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mGame = (Game) getArguments().getSerializable(GAME);
            statAfterGame = getArguments().getBoolean(STAT_AFTER_GAME);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        boolean status = false;
        if(item.getItemId() == Constants.EXPORT_GAME){
            ExportData.getInstance().setLastExportedGame(mGame);
            Calendar calendar = Calendar.getInstance();
            String data = String.valueOf(calendar.getTime());
            System.out.println(data);
            ExportData.getInstance().writeToFile(mGame.getHost().getName()+"_"+mGame.getGuest().getName()+"_"+data+".csv",getActivity(),Constants.EXPORT_GAME);
        }
        if(item.getItemId() == android.R.id.home){
            if(statAfterGame) {
                ((MainActivity) getActivity()).commitFragment(HomeFragment.newInstance(false), true);
            }else{
                getFragmentManager().popBackStack();
            }
            status = true;
        }
        return status;
    }
}
