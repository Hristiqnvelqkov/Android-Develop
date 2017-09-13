package com.apress.gerber.footballman;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apress.gerber.footballman.Fragments.StartMatchFragment;
import com.apress.gerber.footballman.Models.Game;
import com.apress.gerber.footballman.Models.Player;

import java.util.List;

/**
 * Created by hriso on 9/7/2017.
 */

public class GamePlayersAdapter extends RecyclerView.Adapter<GamePlayersAdapter.CustomViewHolder> {
    List<Player> mPlayers;
    StartMatchFragment listner;
    Game game;
    public GamePlayersAdapter(Game game,List<Player> players , StartMatchFragment listner){
        mPlayers = players;
        this.game = game;
        this.listner = listner;
    }
    public static class CustomViewHolder extends RecyclerView.ViewHolder{
        TextView redCards,yellowCards,goals,assists,fauls;
        TextView playerName;
        ImageView redCard;
        ImageView yellowCard;
        ImageView faul;
        ImageView assist;
        ImageView goal;
        public CustomViewHolder(View itemView) {
            super(itemView);
            playerName = itemView.findViewById(R.id.player_name);
            redCard = itemView.findViewById(R.id.make_red_card);
            redCards = itemView.findViewById(R.id.get_red_cards);
            yellowCard = itemView.findViewById(R.id.make_yellow_card);
            yellowCards = itemView.findViewById(R.id.get_yellow_cards);
            faul = itemView.findViewById(R.id.make_faul);
            fauls = itemView.findViewById(R.id.get_fauls);
            assist = itemView.findViewById(R.id.make_assist);
            assists = itemView.findViewById(R.id.get_assist);
            goal = itemView.findViewById(R.id.make_goal);
            goals = itemView.findViewById(R.id.get_goals);
        }
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_in_game_raw,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        holder.playerName.setText(mPlayers.get(position).getName());
        holder.redCards.setText(String.valueOf(game.getRedCards(mPlayers.get(position))));
        holder.yellowCards.setText(String.valueOf(game.getYellowCards(mPlayers.get(position))));
        holder.assists.setText(String.valueOf(game.getAssist(mPlayers.get(position))));
        holder.fauls.setText(String.valueOf(game.getFauls(mPlayers.get(position))));
        holder.goals.setText(String.valueOf(game.getGoals(mPlayers.get(position))));
        holder.redCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.addRedCard(mPlayers.get(position));
                holder.redCards.setText(String.valueOf(game.getRedCards(mPlayers.get(position))));
            }
        });
        holder.yellowCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.addYellowCard(mPlayers.get(position));
                holder.yellowCards.setText(String.valueOf(game.getYellowCards(mPlayers.get(position))));
            }
        });
        holder.goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.addGoal(mPlayers.get(position));
                holder.goals.setText(String.valueOf(game.getGoals(mPlayers.get(position))));
            }
        });
        holder.assist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.addAssist(mPlayers.get(position));
                holder.assists.setText(String.valueOf(game.getAssist(mPlayers.get(position))));
            }
        });
        holder.faul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.addFaul(mPlayers.get(position));
                holder.fauls.setText(String.valueOf(game.getFauls(mPlayers.get(position))));
            }
        });
    }
    @Override
    public int getItemCount() {
        return mPlayers.size();
    }
    public interface onPlayerClicked{
        void addRedCard(Player player);
        void addYellowCard(Player player);
        void addGoal(Player player);
        void addAssist(Player player);
        void addFaul(Player player);
    }
}
