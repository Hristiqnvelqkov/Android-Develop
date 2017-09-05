package com.apress.gerber.footballman.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apress.gerber.footballman.MainActivity;
import com.apress.gerber.footballman.Models.DataManager;
import com.apress.gerber.footballman.Models.Game;
import com.apress.gerber.footballman.Models.Player;
import com.apress.gerber.footballman.Models.Team;
import com.apress.gerber.footballman.PersonsAdapter;
import com.apress.gerber.footballman.R;

import java.util.List;


public class PersonsFragment extends BaseFragment implements PersonsAdapter.PlayerClicked {
    Team team;
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
        game = ((MainActivity)getActivity()).getGame();
        team = (Team) getArguments().getSerializable("teams");

        hide = (boolean) getArguments().getSerializable("HIDE");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_persons, container, false);
        mActionButton = view.findViewById(R.id.next_team);
        setRecyclerView();
        ((MainActivity) getActivity()).setUpToolBar(team.getName());
        return view;
    }

    @Override
    public void addPlayerToGame(Player player) {
        if(game!=null) {
            if (game.getHost() == null) {
                game.setHost(team);
                System.out.println("Yeeeee");
            } else if (game.readyHost()) {
                System.out.println("qkooo");
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
                game.addHostPlayer(player);
            }
        }

    }
    @Override public boolean onOptionsItemSelected(MenuItem item){
        boolean status = false;
        if(item.getItemId()==android.R.id.home){
            ((MainActivity) getActivity()).commitFragment(TeamsFragment.newInstance(team.getLeague(),hide),false);
            status=true;
        }
        if(item.getItemId() == 0) {
            ((MainActivity) getActivity()).commitFragment(AddPersonFragment.newInstance(team,null), true);
            status = true;
        }
        if(item.getItemId() ==1 ){
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
        PersonsAdapter adapter = new PersonsAdapter(team.getPlayers(), this,hide);
        if(!hide){
            mActionButton.setVisibility(View.GONE);
        }
        setLayout(view,recyclerView,adapter,R.string.no_players);
    }
    @Override
    public void updatePlayer(Player player) {
        ((MainActivity) getActivity()).commitFragment(AddPersonFragment.newInstance(team,player),true);
    }
    @Override
    public void removePlayerFromGame(Player player){
        if((game.getHost()!=null) && (game.getGuest()==null)) {
            game.removeGuestPlayer(player);
        }
        if((game.getHost()!=null) && (game.getGuest()!=null)) {
            game.removeGuestPlayer(player);
        }
    }
    @Override
    public void deletePlayer(Player player) {
        DataManager.getDataInstance().removePlayer(team,player);
        setRecyclerView();

    }


}
