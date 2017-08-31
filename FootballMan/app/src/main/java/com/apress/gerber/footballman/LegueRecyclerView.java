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
import com.apress.gerber.footballman.Models.League;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by hriso on 8/23/2017.
 */

public class LegueRecyclerView extends RecyclerView.Adapter<LegueRecyclerView.ViewHolder> {
    private static List<League> mLeagues;

    public LegueRecyclerView(List<League> leaguesData, LeagueListener listener) {
        mLeagues = leaguesData;
        this.listener = listener;
    }

    private LeagueListener listener;
    Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView leagueName;
        ImageView delete;
        ImageView update;

        ViewHolder(View itemView) {
            super(itemView);
            leagueName = (TextView) itemView.findViewById(R.id.league);
            delete = itemView.findViewById(R.id.delete);
            update = itemView.findViewById(R.id.update);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.league_raw, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (mLeagues.size() > 0) {
            holder.leagueName.setText(mLeagues.get(position).getName());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onLeagueClicked(mLeagues.get(position));
                }
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            final AlertDialog alertDialog = new AlertDialog.Builder(holder.delete.getContext()).create();

            @Override
            public void onClick(View view) {
                alertDialog.setTitle(R.string.raw);
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.deleteLeague(mLeagues.get(position));
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

    }

    @Override
    public int getItemCount() {
        return mLeagues.size();
    }

    public interface LeagueListener {
        void onLeagueClicked(League league);

        void deleteLeague(League league);

        void updateLeague(League league);
    }
}
