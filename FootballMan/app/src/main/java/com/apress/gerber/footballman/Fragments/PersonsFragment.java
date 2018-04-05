package com.apress.gerber.footballman.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

import java.util.LinkedList;
import java.util.List;


public class PersonsFragment extends BaseFragment implements PersonsAdapter.PlayerClicked, DataManager.OnPlayersLoaded {
    Team team;
    List<Player> mPlayers = new LinkedList<>();
    FloatingActionButton mActionButton;
    View view;
    Game game;
    PersonsAdapter adapter;

    public static PersonsFragment newInstance(Team team, boolean hide) {
        PersonsFragment fragment = new PersonsFragment();
        Bundle args = new Bundle();
        args.putSerializable("HIDE", hide);
        args.putSerializable("teams", team);
        fragment.setArguments(args);
        return fragment;
    }

    public Team getTeam() {
        return team;
    }

    @Override
    public void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        setHasOptionsMenu(true);
        team = (Team) getArguments().getSerializable("teams");
        game = ((MainActivity) getActivity()).getGame();
        setActivity();
        hide = (boolean) getArguments().getSerializable("HIDE");
        adapter = new PersonsAdapter(mPlayers, this, hide);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_persons, container, false);
        mActionButton = view.findViewById(R.id.next_team);
        if (hide) {
            if (getMainActivity().getModeForGame() == MainActivity.FIRST_TEAM && !(this instanceof PersonsFragmentWhileInGame)) {
                mActionButton.setVisibility(View.VISIBLE);
            } else  {
                mActionButton.setVisibility(View.GONE);
            }
        }
        setRecyclerView();
        mActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ((MainActivity) getActivity()).commitFragment(HomeFragment.newInstance(true), true);
                ((MainActivity) getActivity()).setModeForGame(MainActivity.SECOND_TEAM);
                game.setHostReady();
                game.setHost(team);
            }
        });
        mManager.getPlayersForTeam(team, this);
        ((MainActivity) getActivity()).setUpToolBar(team.getName());
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, Constants.MENU_ADD, Menu.NONE, R.string.add);
        menu.getItem(Constants.MENU_ADD).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        if (hide) {
            menu.add(0, Constants.MENU_NEXT, Menu.NONE, R.string.next).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
    }

    @Override
    public void addPlayerToGame(Player player) {
        if (game != null) {
            if (getMainActivity().getModeForGame() == MainActivity.FIRST_TEAM) {
                if (!game.getHostPlayers().contains(player)) {
                    game.addHostPlayer(player);
                }
            }
            if (getMainActivity().getModeForGame() == MainActivity.SECOND_TEAM) {
                if (!game.getGuestTeamPlayers().contains(player)) {
                    game.addGuestPlayer(player);
                }
            }
        }

    }
    public boolean ifPlayerIsAdded(Player player){
        return (game.getHostPlayers().contains(player) || game.getGuestTeamPlayers().contains(player));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean status = false;
        if (item.getItemId() == android.R.id.home) {
            getMainActivity().getMyFragmentManager().popBackStack();
            if (game != null) {
                if (getMainActivity().getModeForGame() == MainActivity.SECOND_TEAM) {
                    game.setGuest(null);
                    game.removeAllGuestPlayers();
                } else if (getMainActivity().getModeForGame() == MainActivity.FIRST_TEAM) {
                    game.setHost(null);
                    game.removeAllHostPlayers();
                    mActivity.startGame(game);
                }
            }
            status = true;
        }
        if (item.getItemId() == Constants.MENU_ADD) {
            if (hide) {
                if ((game.getHost() == null) || (game.getHost() != null && game.getGuest() == null)) {
                    ((MainActivity) getActivity()).commitFragment(AddPersonFragment.newInstanceForGame(game, true, team), true);
                } else {
                    ((MainActivity) getActivity()).commitFragment(AddPersonFragment.newInstanceForGame(game, false, team), true);
                }
            } else {
                ((MainActivity) getActivity()).commitFragment(AddPersonFragment.newInstance(team, null, hide), true);
            }
            status = true;
        }
        if (item.getItemId() == Constants.MENU_NEXT) {
            if (getMainActivity().getModeForGame() == MainActivity.SECOND_TEAM) {
                game.setGuest(team);
            }
            if ((game.getHost() != null && game.getGuest() != null)) {
                mActivity.commitFragment(StartMatchFragment.newInstance(game), true);
            } else {
                Toast.makeText(getContext(), "First choose 2 teams", Toast.LENGTH_SHORT).show();
            }
        }
        return status;
    }

    public void setRecyclerView() {
        RecyclerView recyclerView = view.findViewById(R.id.team_players);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        if (!hide) {
            mActionButton.setVisibility(View.GONE);
        }
        setLayout(view, mPlayers.size(), R.string.no_players);
    }

    @Override
    public void updatePlayer(Player player) {
        ((MainActivity) getActivity()).commitFragment(AddPersonFragment.newInstance(team, player, hide), true);
    }

    @Override
    public void removePlayerFromGame(Player player) {
        if (MainActivity.FIRST_TEAM == getMainActivity().getModeForGame()) {
            game.removeHostPlayer(player);
        }
        if (MainActivity.SECOND_TEAM == getMainActivity().getModeForGame()) {
            game.removeGuestPlayer(player);
        }
    }

    @Override
    public void deletePlayer(Player player) {
        mPlayers.remove(player);
        mManager.removePlayer(player);
        adapter.updateElements(mPlayers);
        setLayout(view, mPlayers.size(), R.string.no_players);

    }

    @Override
    public void onPlayersLoaded(List<Player> players) {
        mPlayers = players;
        adapter.updateElements(players);
        setLayout(view, mPlayers.size(), R.string.no_players);
    }
}

