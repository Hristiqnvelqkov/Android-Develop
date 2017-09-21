package com.apress.gerber.footballman;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apress.gerber.footballman.Fragments.StartMatchFragment;
import com.apress.gerber.footballman.Models.Game;
import com.apress.gerber.footballman.Models.Player;

import java.util.List;

/**
 * Created by hriso on 9/18/2017.
 */

public class LandGameAdapter extends GamePlayersAdapter {
    public LandGameAdapter(Game game, List<Player> players, StartMatchFragment listner) {
        super(game, players, listner);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.land_player_in_game_raw, parent, false);
        return new CustomViewHolder(view);
    }
}
