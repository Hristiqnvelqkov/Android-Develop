package com.apress.gerber.footballman;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    protected boolean tablet = false;
    int rebind = 0;

    public int getRebind() {
        return rebind;
    }

    public GamePlayersAdapter(Game game, List<Player> players, StartMatchFragment listner, boolean tabletSize) {
        mPlayers = players;
        tablet = tabletSize;
        this.game = game;
        this.listner = listner;
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView redCards, yellowCards, goals, assists, fauls, penalties;
        TextView playerName;
        ImageView redCard;
        ImageView yellowCard;
        ImageView faul;
        ImageView assist;
        ImageView goal;
        ImageView gameStat;
        TextView playerNumber;
        ImageView penalty;

        public CustomViewHolder(View itemView) {
            super(itemView);
            playerName = itemView.findViewById(R.id.player_name);
            redCard = itemView.findViewById(R.id.make_red_card);
            redCards = itemView.findViewById(R.id.get_red_cards);
            yellowCard = itemView.findViewById(R.id.make_yellow_card);
            penalty = itemView.findViewById(R.id.make_penalty);
            penalties = itemView.findViewById(R.id.get_penalties);
            yellowCards = itemView.findViewById(R.id.get_yellow_cards);
            faul = itemView.findViewById(R.id.make_faul);
            fauls = itemView.findViewById(R.id.get_fauls);
            assist = itemView.findViewById(R.id.make_assist);
            playerNumber = itemView.findViewById(R.id.player_number);
            assists = itemView.findViewById(R.id.get_assist);
            goal = itemView.findViewById(R.id.make_goal);
            goals = itemView.findViewById(R.id.get_goals);
            // gameStat = itemView.findViewById(R.id.game_stat);
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (tablet) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_in_game_raw_tablet, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_in_game_raw, parent, false);
        }
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        holder.playerName.setText(mPlayers.get(position).getName());
        holder.playerNumber.setText(String.valueOf(mPlayers.get(position).getNumber()));
//        if (!game.checkPlayerInGame(mPlayers.get(position))) {
//            holder.gameStat.setImageResource(R.drawable.red);
//        } else {
//            holder.gameStat.setImageResource(R.drawable.green);
//        }
        if ((game.getRedCards(mPlayers.get(position))) == 1) {
            holder.playerName.setTextColor(Color.RED);
        } else if ((game.getYellowCards(mPlayers.get(position))) == 1) {
            holder.playerName.setTextColor(Color.YELLOW);
        } else{
            holder.playerName.setTextColor(Color.DKGRAY);
        }

        holder.penalties.setText((String.valueOf(game.getPenalties(mPlayers.get(position)))));
        holder.redCards.setText(String.valueOf(game.getRedCards(mPlayers.get(position))));
        holder.yellowCards.setText(String.valueOf(game.getYellowCards(mPlayers.get(position))));
        holder.assists.setText(String.valueOf(game.getAssist(mPlayers.get(position))));
        holder.fauls.setText(String.valueOf(game.getFauls(mPlayers.get(position))));
        holder.goals.setText(String.valueOf(game.getGoals(mPlayers.get(position))));
        holder.redCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listner.checkMatchStarted()) {
                    //  if (game.checkPlayerInGame(mPlayers.get(position))) {
                    listner.addRedCard(mPlayers.get(position));
                    holder.redCards.setText(String.valueOf(game.getRedCards(mPlayers.get(position))));
                    holder.playerName.setTextColor(Color.RED);
                    //}
                }
            }
        });
        holder.penalty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (game.getRedCards(mPlayers.get(position)) == 0) {
                    if (listner.checkMatchStarted()) {
                        //            if (game.checkPlayerInGame(mPlayers.get(position))) {
                        listner.addPenalty(mPlayers.get(position));
                        holder.penalties.setText(String.valueOf(game.getPenalties(mPlayers.get(position))));
                        //          }
                    }
                }
            }
        });
        holder.goal.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (game.getRedCards(mPlayers.get(position)) == 0) {
                    if (listner.checkMatchStarted()) {
                        //            if (game.checkPlayerInGame(mPlayers.get(position))) {
                        listner.autoGoal(mPlayers.get(position));
                        return true;
                    }
                }
                return false;
            }
        });
//        holder.gameStat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (rebind > 0) {
//                    rebind = 0;
//                    notifyDataSetChanged();
//                }
//                int stat = listner.onChangeListner(mPlayers.get(position));
//                if (stat == Constants.IN) {
//                    holder.gameStat.setImageResource(R.drawable.green);
//                }
//                if (stat == Constants.OUT) {
//                    holder.gameStat.setImageResource(R.drawable.red);
//                }
//                if (stat == Constants.READY_FOR_CHANGE) {
//                    holder.gameStat.setImageResource(R.drawable.yellow);
//                    rebind++;
//                }
//            }
//        });

        holder.yellowCard.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if (listner.checkMatchStarted()) {
                    //                if (game.checkPlayerInGame(mPlayers.get(position))) {
                    listner.addYellowCard(mPlayers.get(position));
                    if (game.getYellowCards(mPlayers.get(position)) == 1 && (game.getRedCards(mPlayers.get(position)) == 0)) {
                        holder.playerName.setTextColor(Color.YELLOW);
                    }
                    if (game.getRedCards(mPlayers.get(position)) == 1) {
                        holder.playerName.setTextColor(Color.RED);
                    }
                    holder.redCards.setText(String.valueOf(game.getRedCards(mPlayers.get(position))));
                    holder.yellowCards.setText(String.valueOf(game.getYellowCards(mPlayers.get(position))));
                }
                //          }
            }
        });
        holder.goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (game.getRedCards(mPlayers.get(position)) == 0) {
                    if (listner.checkMatchStarted()) {
                        //            if (game.checkPlayerInGame(mPlayers.get(position))) {
                        listner.addGoal(mPlayers.get(position));
                        holder.goals.setText(String.valueOf(game.getGoals(mPlayers.get(position))));
                        //          }
                    }
                }
            }
        });
        holder.assist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (game.getRedCards(mPlayers.get(position)) == 0) {
                    if (listner.checkMatchStarted()) {
                        //   if (game.checkPlayerInGame(mPlayers.get(position))) {
                        listner.addAssist(mPlayers.get(position));
                        holder.assists.setText(String.valueOf(game.getAssist(mPlayers.get(position))));
                        // }
                    }
                }
            }
        });
        holder.faul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (game.getRedCards(mPlayers.get(position)) == 0) {
                    if (listner.checkMatchStarted()) {
                        //   if (game.checkPlayerInGame(mPlayers.get(position))) {
                        listner.addFaul(mPlayers.get(position));
                        holder.fauls.setText(String.valueOf(game.getFauls(mPlayers.get(position))));
                        // }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlayers.size();
    }

    public interface onPlayerClicked {

        int onChangeListner(Player player);

        void removeLastEvent();

        void addRedCard(Player player);

        void addYellowCard(Player player);

        void addGoal(Player player);

        void addAssist(Player player);

        void addFaul(Player player);

        void addPenalty(Player player);

        void autoGoal(Player player);
    }
}

