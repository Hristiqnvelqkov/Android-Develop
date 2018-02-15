package com.example.hristiyan.menu;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.hristiyan.menu.data.Food;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hristiyan on 07.02.18.
 */

public class FoodsAdapter extends RecyclerView.Adapter<FoodsAdapter.FoodViewHolder> {
    private List<Food> foods = new LinkedList<>();
    private boolean activeMenu = false;
    public static final int BLOCK_BUTTON_TIME = 4000;
    private boolean openFromUser = false;
    private Activity activity;
    private Handler handler;
    private UserTappedOverFood listner;

    public FoodsAdapter(boolean activeMenu, boolean openFromUser, UserTappedOverFood listner) {
        this.activeMenu = activeMenu;
        this.openFromUser = openFromUser;
        activity = ((Fragment) listner).getActivity();
        this.listner = listner;
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        Button foodName;
        TextView timesSelected;

        public FoodViewHolder(View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.food_name);
            timesSelected = itemView.findViewById(R.id.nums_selected);
        }
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_raw, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FoodViewHolder holder, final int position) {
        holder.foodName.setText(foods.get(position).getName());
        if(foods.get(position).getName().length() > 15){
            holder.foodName.setTextSize(30);
            if(foods.get(position).getName().length() > 20){
                holder.foodName.setTextSize(25);
            }
        }
        if (!openFromUser) {
            holder.timesSelected.setText(String.valueOf(foods.get(position).getTimesSelected()));
            holder.foodName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Tools.showAlertDialog(activity, activity.getString(R.string.do_you_want_to_updateFood), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listner.updateFood(foods.get(position));
                        }

                    });
                }
            });
            holder.foodName.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Tools.showAlertDialog(activity, activity.getString(R.string.do_you_want_to_delete_food), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listner.deleteFood(foods.get(position));
                        }
                    });
                    return false;
                }
            });
        } else {
            holder.foodName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.userTappedOverFood(foods.get(position));
                    holder.foodName.setEnabled(false);
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.foodName.setEnabled(true);
                        }
                    }, BLOCK_BUTTON_TIME);
                }
            });
        }
    }

    public void clearAdapter() {
        if (handler != null)
            handler.removeCallbacks(null);
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public void updateFoods(List<Food> foods) {
        this.foods = foods;
        notifyDataSetChanged();
    }

    public interface UserTappedOverFood {
        void userTappedOverFood(Food food);

        void deleteFood(Food food);

        void updateFood(Food food);
    }
}
