package com.apress.gerber.footballman.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.apress.gerber.footballman.Constants;
import com.apress.gerber.footballman.MainActivity;
import com.apress.gerber.footballman.Models.DataManager;
import com.apress.gerber.footballman.Models.Game;
import com.apress.gerber.footballman.Models.Player;
import com.apress.gerber.footballman.Models.Team;
import com.apress.gerber.footballman.R;


public class AddPersonFragment extends BaseFragment {
    DataManager manager = DataManager.getDataInstance();
    Button save;
    Team mTeam;
    public int personId = 0;
    View fragment_view;
    Player mPlayer;
    TextInputLayout player_name;
    Game game = null;
    boolean host = false;

    public static AddPersonFragment newInstance(Team team, Player player, boolean hide) {
        AddPersonFragment fragment = new AddPersonFragment();
        Bundle args = new Bundle();
        args.putBoolean("hide", hide);
        args.putSerializable("team", team);
        if (player != null) {
            args.putSerializable("player", player);
        }
        fragment.setArguments(args);
        return fragment;
    }

    public static AddPersonFragment newInstanceForGame(Game game, boolean host, Team team) {
        AddPersonFragment fragment = new AddPersonFragment();
        Bundle args = new Bundle();
        args.putSerializable("GAME", game);
        args.putSerializable("team", team);
        args.putBoolean("IFHOST", host);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragment_view = inflater.inflate(R.layout.fragment_add_person, container, false);
        save = fragment_view.findViewById(R.id.save_player);
        final TextInputLayout player_name = fragment_view.findViewById(R.id.player_name);
        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
        player_name.requestFocus();
        final TextInputLayout player_number = fragment_view.findViewById(R.id.player_number);
        if (mPlayer != null) {
            player_name.getEditText().setText(mPlayer.getName());
            player_number.getEditText().setText(String.valueOf(mPlayer.getNumber()));
        }
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        ((MainActivity) getActivity()).invisibleHome();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num = player_number.getEditText().getText().toString();
                String name = player_name.getEditText().getText().toString();
                if (name.equals("")) {
                    player_name.setErrorEnabled(true);
                    player_name.setError("Трябва да въведете име на играча");
                } else {
                    player_name.setErrorEnabled(false);
                    if (num.equals("")) {
                        player_number.setErrorEnabled(true);
                        player_number.setError("Трябва да въведете номер на играча");
                    } else {
                        if (game == null) {
                            if (mPlayer == null) {
                                mManager.addPlayer(mTeam, (player_name.getEditText().getText().toString()), Integer.parseInt(player_number.getEditText().getText().toString()));
                            } else {
                                mManager.updatePlayer(mPlayer, (player_name.getEditText().getText().toString()), (Integer.parseInt(player_number.getEditText().getText().toString())));
                            }
                        } else {
                            Player player = new Player();
                            player.setName(name);
                            player.setNumber(Integer.parseInt(num));
                            if (host) {
                                game.addHostPlayer(player);
                                mManager.addPlayer(game.getHost(), player.getName(), player.getNumber());
                            } else {
                                game.addGuestPlayer(player);
                                mManager.addPlayer(game.getGuest(), player.getName(), player.getNumber());
                            }
                        }
                        ((MainActivity) getActivity()).visibleHome();
                        imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                        if (game != null) {
                            ((MainActivity) getActivity()).commitFragment(PersonsFragment.newInstance(mTeam, true), true);
                        } else {
                            ((MainActivity) getActivity()).commitFragment(PersonsFragment.newInstance(mTeam, false), true);
                        }
                    }
                }
            }
        });
        return fragment_view;
    }

    @Override
    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        mTeam = (Team) getArguments().getSerializable("team");
        mPlayer = (Player) getArguments().getSerializable("player");
        hide = getArguments().getBoolean("hide");
        game = (Game) getArguments().getSerializable("GAME");
        host = getArguments().getBoolean("IFHOST");
    }
}
