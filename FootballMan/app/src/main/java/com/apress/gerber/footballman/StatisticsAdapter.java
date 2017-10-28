package com.apress.gerber.footballman;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apress.gerber.footballman.Models.Event;

import java.util.List;

/**
 * Created by hriso on 9/12/2017.
 */

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.EventHolder> {
    List<Event> mEventList;

    public StatisticsAdapter(List<Event> events) {
        mEventList = events;
    }

    public static class EventHolder extends RecyclerView.ViewHolder {
        TextView hostEvent;
        ImageView hostImageEvent;

        public EventHolder(View itemView) {
            super(itemView);
            hostEvent = itemView.findViewById(R.id.host_view);
            hostImageEvent = itemView.findViewById(R.id.host_event);
        }
    }

    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.statistics_raw, parent, false);
        return new EventHolder(view);
    }

    public void setImageView(ImageView image, int type) {
        if (type == Constants.GOAL_EVENT) {
            image.setImageResource(R.mipmap.ball_icon_round);
        } else if (type == Constants.FOUL_EVENT) {
            image.setImageResource(R.mipmap.faul);
        } else if (type == Constants.YELLOW_EVENT) {
            image.setImageResource(R.mipmap.yellow);
        } else if (type == Constants.RED_EVENT) {
            image.setImageResource(R.mipmap.red);
        } else if (type == Constants.ASSIST){
            image.setImageResource(R.mipmap.assist);
        }
    }

    @Override
    public void onBindViewHolder(EventHolder holder, int position) {
        holder.hostEvent.setText(String.valueOf(mEventList.get(position).getTime() + " " + mEventList.get(position).getPlayer().getName()));
        setImageView(holder.hostImageEvent, mEventList.get(position).getType());

    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }

}
