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

public class FoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Food> foods = new LinkedList<>();
    private boolean activeMenu = false;
    public static final int BLOCK_BUTTON_TIME = 4000;
    private boolean openFromUser = false;
    private Activity activity;
    private Handler handler;
    private UserTappedOverFood listner;
    private final int BUTTON_ADD_RAW = 1;
    private final int FOOD_ROW = 2;
    private String menuTitle;


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

    public static class AddButtonViewHolder extends RecyclerView.ViewHolder {
        Button addButton;
        TextView menuName;

        public AddButtonViewHolder(View itemView) {
            super(itemView);
            addButton = itemView.findViewById(R.id.add_food);
            menuName = itemView.findViewById(R.id.menu_name);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == FOOD_ROW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_raw, parent, false);
            return new FoodViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.button_add_raw_and_date, parent, false);
            return new AddButtonViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof FoodViewHolder) {
            final int currentPos = position - 1;
            ((FoodViewHolder) holder).foodName.setText(foods.get(currentPos).getName());
            if (foods.get(currentPos).getName().length() > 15) {
                ((FoodViewHolder) holder).foodName.setTextSize(30);
                if (foods.get(currentPos).getName().length() > 20) {
                    ((FoodViewHolder) holder).foodName.setTextSize(25);
                }
            }
            if (!openFromUser) {
                ((FoodViewHolder) holder).timesSelected.setText(String.valueOf(foods.get(currentPos).getTimesSelected()));
                ((FoodViewHolder) holder).foodName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Tools.showAlertDialog(activity, activity.getString(R.string.do_you_want_to_updateFood), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listner.updateFood(foods.get(currentPos));
                            }

                        });
                    }
                });
                ((FoodViewHolder) holder).foodName.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Tools.showAlertDialog(activity, activity.getString(R.string.do_you_want_to_delete_food), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listner.deleteFood(foods.get(currentPos));
                            }
                        });
                        return false;
                    }
                });
            } else {
                ((FoodViewHolder) holder).foodName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listner.userTappedOverFood(foods.get(currentPos));
                        ((FoodViewHolder) holder).foodName.setEnabled(false);
                        handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ((FoodViewHolder) holder).foodName.setEnabled(true);
                            }
                        }, BLOCK_BUTTON_TIME);
                    }
                });
            }
        } else {
            ((AddButtonViewHolder) holder).menuName.setText(menuTitle );
            if(!openFromUser) {
                ((AddButtonViewHolder) holder).addButton.setText(R.string.add);
                ((AddButtonViewHolder) holder).addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listner.addFood();
                    }
                });
            }else{
                ((AddButtonViewHolder) holder).addButton.setVisibility(View.GONE);
            }
        }
    }

    public void clearAdapter() {
        if (handler != null)
            handler.removeCallbacks(null);
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        if (position == 0) {
            return BUTTON_ADD_RAW;
        } else {
            return FOOD_ROW;
        }
    }

    @Override
    public int getItemCount() {
        return foods.size() + 1;
    }

    public void updateFoods(List<Food> foods) {
        this.foods = foods;
        notifyDataSetChanged();
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public interface UserTappedOverFood {
        void userTappedOverFood(Food food);

        void deleteFood(Food food);

        void updateFood(Food food);

        void addFood();
    }
}
