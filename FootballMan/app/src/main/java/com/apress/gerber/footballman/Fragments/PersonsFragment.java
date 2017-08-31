package com.apress.gerber.footballman.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apress.gerber.footballman.MainActivity;
import com.apress.gerber.footballman.Models.DataManager;
import com.apress.gerber.footballman.Models.Player;
import com.apress.gerber.footballman.Models.Team;
import com.apress.gerber.footballman.PersonsAdapter;
import com.apress.gerber.footballman.R;

import java.util.List;


public class PersonsFragment extends BaseFragment implements PersonsAdapter.PlayerClicked {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    Team team;
    View view;
    public static PersonsFragment newInstance(Team team) {
        PersonsFragment fragment = new PersonsFragment();
        Bundle args = new Bundle();
        args.putSerializable("teams",team);
        fragment.setArguments(args);
        return fragment;
    }

    public Team getTeam(){
        return team;
    }

    @Override public void onCreate(Bundle savedInstancestate){
        super.onCreate(savedInstancestate);
        setHasOptionsMenu(true);
        team = (Team) getArguments().getSerializable("teams");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_persons, container, false);
        setRecyclerView();
        ((MainActivity) getActivity()).setUpToolBar(team.getName());
        return view;
    }

    @Override
    public void openPlayer() {

    }
    @Override public boolean onOptionsItemSelected(MenuItem item){
        boolean status = false;
        if(item.getItemId()==android.R.id.home){
            ((MainActivity) getActivity()).commitFragment(TeamsFragment.newInstance(team.getLeague()),false);
            status=true;
        }
        if(item.getItemId() == R.id.test) {
            ((MainActivity) getActivity()).commitFragment(AddPersonFragment.newInstance(team,null), true);
            status = true;
        }
        return status;
    }
    public void setRecyclerView() {
        RecyclerView recyclerView = view.findViewById(R.id.team_players);
        PersonsAdapter adapter = new PersonsAdapter(team.getPlayers(), this);
        setLayout(view,recyclerView,adapter,R.string.no_players);
    }
    @Override
    public void updatePlayer(Player player) {
        ((MainActivity) getActivity()).commitFragment(AddPersonFragment.newInstance(team,player),true);
    }

    @Override
    public void deletePlayer(Player player) {
        DataManager.getDataInstance().removePlayer(team,player);
        setRecyclerView();

    }


}
