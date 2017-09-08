package com.apress.gerber.footballman;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apress.gerber.footballman.Fragments.PersonsFragment;
import com.apress.gerber.footballman.Models.Player;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hriso on 8/25/2017.
 */

public class PersonsAdapter extends RecyclerView.Adapter<PersonsAdapter.ViewHolder> {
    private List<Player> players = new LinkedList<>();
    private PersonsFragment mListener;
    boolean hideButtons = false;
    public PersonsAdapter(List<Player> players, PersonsFragment listner,boolean hideButtons){
        this.players=players;
        mListener = listner;
        this.hideButtons = hideButtons;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView number;
        ImageView update;
        ImageView ball;
        ImageView delete;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.person_name);
            number = itemView.findViewById(R.id.person_number);
            update = itemView.findViewById(R.id.update);
            delete = itemView.findViewById(R.id.delete);
            ball = itemView.findViewById(R.id.ball);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_raw,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.ball.setVisibility(View.GONE);
        holder.name.setText(String.valueOf(players.get(position).getName()));
        holder.number.setText(String.valueOf(players.get(position).getNumber()));
        if (!hideButtons) {
            holder.update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog dialog = new AlertDialog.Builder(holder.itemView.getContext()).create();
                    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new AlertDialogButtons(dialog));
                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mListener.updatePlayer(players.get(position));
                        }
                    });
                    dialog.show();
                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                final AlertDialog dialog = new AlertDialog.Builder(holder.itemView.getContext()).create();

                @Override
                public void onClick(View view) {
                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mListener.deletePlayer(players.get(position));
                            notifyDataSetChanged();
                        }
                    });
                    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new AlertDialogButtons(dialog));
                    dialog.show();
                }
            });
        }else{
            holder.delete.setVisibility(View.GONE);
            holder.update.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.ball.getVisibility()==View.GONE) {
                        holder.ball.setVisibility(View.VISIBLE);
                        mListener.addPlayerToGame(players.get(position));
                    }else{
                        holder.ball.setVisibility(View.GONE);
                        mListener.removePlayerFromGame(players.get(position));
                    }
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return players.size();
    }
    public interface PlayerClicked{
         void addPlayerToGame(Player palyer);
         void removePlayerFromGame(Player player);
         void updatePlayer(Player player);
         void deletePlayer(Player palyer);
    }
}
