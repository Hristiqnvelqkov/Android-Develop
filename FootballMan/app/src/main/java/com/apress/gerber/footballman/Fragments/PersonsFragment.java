package com.apress.gerber.footballman.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apress.gerber.footballman.Constants;
import com.apress.gerber.footballman.MainActivity;
import com.apress.gerber.footballman.Models.DataManager;
import com.apress.gerber.footballman.Models.Game;
import com.apress.gerber.footballman.Models.Player;
import com.apress.gerber.footballman.Models.Team;
import com.apress.gerber.footballman.PersonsAdapter;
import com.apress.gerber.footballman.R;

import java.util.List;


public class PersonsFragment extends BaseFragment implements PersonsAdapter.PlayerClicked, DataManager.OnPlayersLoaded {
    Team team;
    List<Player> mPlayers;
    FloatingActionButton mActionButton;
    View view;
    Game game ;
    public static PersonsFragment newInstance(Team team, boolean hide) {
        PersonsFragment fragment = new PersonsFragment();
        Bundle args = new Bundle();
        args.putSerializable("HIDE",hide);
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
        team =(Team) getArguments().getSerializable("teams");
        game = ((MainActivity)getActivity()).getGame();
        setActivity();
        hide = (boolean) getArguments().getSerializable("HIDE");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mManager.getPlayersForTeam(team,this);
        view = inflater.inflate(R.layout.fragment_persons, container, false);
        mActionButton = view.findViewById(R.id.next_team);
        mActionButton.setOnClickListener(new View.OnClickListener(){
           public void onClick(View view){
               ((MainActivity) getActivity()).commitFragment(HomeFragment.newInstance(true),true);
               game.setHostReady();
           }
        });

        ((MainActivity) getActivity()).setUpToolBar(team.getName());
        return view;
    }

    @Override
    public void addPlayerToGame(Player player) {
        if(game!=null) {
            if (game.getHost() == null) {
                mActionButton.setVisibility(View.VISIBLE);
                game.setHost(team);
            } else if (game.readyHost()) {
                mActionButton.setVisibility(View.GONE);
                if (game.getGuest() == null) {
                    game.setGuest(team);
                }
            }
            if ((game.getHost() != null) && (game.getGuest() == null)) {
                System.out.println(game.getHost().getName());
                game.addHostPlayer(player);
            }
            if ((game.getHost() != null) && (game.getGuest() != null)) {
                System.out.println(game.getGuest().getName());
                System.out.println(game.getHost().getName());
                game.addGuestPlayer(player);
            }
        }

    }
    @Override public boolean onOptionsItemSelected(MenuItem item){
        boolean status = false;
        if(item.getItemId()==android.R.id.home){
            ((MainActivity) getActivity()).commitFragment(TeamsFragment.newInstance(team.getLeague(),hide),false);
            if(game!=null) {
                if (game.getGuest() != null) {
                    game.setGuest(null);
                    game.removeAllGuestPlayers();
                }else{
                    game.setHost(null);
                    game.removeAllHostPlayers();
                }
                mActivity.startGame(game);
            }
            status=true;
        }
        if(item.getItemId() == Constants.MENU_ADD) {
            ((MainActivity) getActivity()).commitFragment(AddPersonFragment.newInstance(team,null), true);
            status = true;
        }
        if(item.getItemId() ==Constants.MENU_NEXT ){
            if((game.getHost()!=null && game.getGuest()!=null)) {
                mActivity.commitFragment(StartMatchFragment.newInstance(game),true);
            }else{
                Toast.makeText(getContext(),"First choose 2 teams",Toast.LENGTH_SHORT).show();
            }
        }
        return status;
    }
    public void setRecyclerView() {
        RecyclerView recyclerView = view.findViewById(R.id.team_players);
        PersonsAdapter adapter = new PersonsAdapter(mPlayers, this,hide);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        if(!hide){
            mActionButton.setVisibility(View.GONE);
        }
       setLayout(view,mPlayers.size(),R.string.no_players);
    }
    @Override
    public void updatePlayer(Player player) {
        ((MainActivity) getActivity()).commitFragment(AddPersonFragment.newInstance(team,player),true);
    }
    @Override
    public void removePlayerFromGame(Player player){
        if((game.getHost()!=null) && (game.getGuest()==null)) {
            game.removeHostPlayer(player);
        }
        if((game.getHost()!=null) && (game.getGuest()!=null)) {
            game.removeGuestPlayer(player);
        }
    }
    @Override
    public void deletePlayer(Player player) {
        mManager.removePlayer(player);
        setLayout(view,mPlayers.size(),R.string.no_players);

    }


    @Override
    public void onPlayersLoaded(List<Player> players) {
        mPlayers = players;
        setRecyclerView();
        setLayout(view,mPlayers.size(),R.string.no_players);
    }
}
