package com.apress.gerber.footballman;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.apress.gerber.footballman.Fragments.GamesFragment;
import com.apress.gerber.footballman.Models.Game;

import java.util.List;

/**
 * Created by hriso on 9/21/2017.
 */

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {
    List<Game> mGames;
    GamesFragment mListner;
    public GameAdapter(List<Game> gameList, GamesFragment listner) {
        mListner = listner;
        mGames = gameList;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView hostName,gameResult,guestName;
        public ViewHolder(View itemView) {
            super(itemView);
            hostName = itemView.findViewById(R.id.game_host_name);
            guestName = itemView.findViewById(R.id.game_guest_name);
            gameResult = itemView.findViewById(R.id.game_result);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_raw,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListner.openStat(mGames.get(position));
            }
        });
        holder.hostName.setText(mGames.get(position).getHost().getName());
        holder.gameResult.setText(String.valueOf(mGames.get(position).getHostResult() +" : " + mGames.get(position).getGuestResult()));
        holder.guestName.setText(String.valueOf(mGames.get(position).getGuest().getName()));
    }

    @Override
    public int getItemCount() {
        return mGames.size();
    }
    public interface onGameClicked{
        void openStat(Game game);
    }
}
