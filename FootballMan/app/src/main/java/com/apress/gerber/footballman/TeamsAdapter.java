package com.apress.gerber.footballman;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apress.gerber.footballman.Fragments.BaseFragment;
import com.apress.gerber.footballman.Models.League;
import com.apress.gerber.footballman.Models.Player;
import com.apress.gerber.footballman.Models.Team;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hriso on 8/25/2017.
 */

public class TeamsAdapter extends RecyclerView.Adapter<TeamsAdapter.ViewHolder> {
    private List<Team> leagueTeams ;
    private onClickedTeam team;
    private boolean hideButtons = false;
    public TeamsAdapter(List<Team> teams, onClickedTeam listner,boolean hide) {
        leagueTeams = teams;
        team = listner;
        this.hideButtons = hide;
    }



    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView view;
        ImageView delete;
        ImageView update;
        ViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.team_raw);
            delete = itemView.findViewById(R.id.delete);
            update = itemView.findViewById(R.id.update);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_raw, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.view.setText(leagueTeams.get(position).getName());
        if(!hideButtons) {
            final AlertDialog alert = new AlertDialog.Builder(holder.delete.getContext()).create();
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alert.setTitle(R.string.raw);
                    alert.setButton(DialogInterface.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            team.deleteTeam(leagueTeams.get(position));
                        }
                    });
                    alert.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new AlertDialogButtons(alert));
                    alert.show();
                }
            });
            holder.update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alert.setTitle(R.string.update_raw);
                    alert.setButton(DialogInterface.BUTTON_POSITIVE, "Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            team.updateTeam(leagueTeams.get(position));
                        }
                    });
                    alert.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new AlertDialogButtons(alert));
                    alert.show();
                }
            });
        }else {
            holder.update.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                team.onClickTeam(leagueTeams.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(leagueTeams!=null){
            return leagueTeams.size();
        }else{
            return 0;
        }
    }
    public interface onClickedTeam{
        void onClickTeam(Team team);
        void deleteTeam(Team team);
        void updateTeam(Team team);
    }
}
