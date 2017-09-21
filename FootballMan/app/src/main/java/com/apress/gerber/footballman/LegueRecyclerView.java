package com.apress.gerber.footballman;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apress.gerber.footballman.Fragments.BaseFragment;
import com.apress.gerber.footballman.Fragments.HomeFragment;
import com.apress.gerber.footballman.Models.Game;
import com.apress.gerber.footballman.Models.League;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by hriso on 8/23/2017.
 */

public class LegueRecyclerView extends RecyclerView.Adapter<LegueRecyclerView.ViewHolder> {
    private static List<League> mLeagues;

    private boolean hideButtons = false;

    public LegueRecyclerView(List<League> leaguesData, LeagueListener listener, boolean hideButtons) {
        mLeagues = leaguesData;
        this.listener = listener;
        this.hideButtons = hideButtons;
    }

    private LeagueListener listener;
    Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView leagueName;
        ImageView delete;
        ImageView update;
        ImageView ball;

        ViewHolder(View itemView) {
            super(itemView);
            leagueName = itemView.findViewById(R.id.league);
            delete = itemView.findViewById(R.id.delete);
            update = itemView.findViewById(R.id.update);
            ball = itemView.findViewById(R.id.ball);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        mContext = parent.getContext();
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.league_raw, parent, false);
        }
        if (viewType == 2) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.league_match_raw, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (mLeagues.size() > 0) {
            holder.ball.setVisibility(View.GONE);
            holder.leagueName.setText(mLeagues.get(position).getName());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onLeagueClicked(mLeagues.get(position), hideButtons);
                }
            }
        });
        if (!hideButtons) {
            holder.delete.setOnClickListener(new View.OnClickListener() {
                final AlertDialog alertDialog = new AlertDialog.Builder(holder.delete.getContext()).create();

                @Override
                public void onClick(View view) {
                    alertDialog.setTitle(R.string.raw);
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            listener.deleteLeague(mLeagues.get(position));
                            notifyDataSetChanged();
                        }
                    });
                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new AlertDialogButtons(alertDialog));
                    alertDialog.show();
                }
            });
            holder.update.setOnClickListener(new View.OnClickListener() {
                final AlertDialog alertDialog = new AlertDialog.Builder(holder.delete.getContext()).create();

                @Override
                public void onClick(View view) {
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            listener.updateLeague(mLeagues.get(position));
                        }
                    });
                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new AlertDialogButtons(alertDialog));
                    alertDialog.show();
                }
            });
        } else {
            holder.update.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mLeagues.size();
    }

    public interface LeagueListener {
        void onLeagueClicked(League league, boolean hide);

        void deleteLeague(League league);

        void updateLeague(League league);
    }
}
