package com.example.hristiyan.menu;


import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.hristiyan.menu.data.Menu;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hristiyan on 07.02.18.
 */

public class MenusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Menu> menus = new LinkedList<>();
    private OnMenuItemClicked listner;
    private Activity activity;
    private final int BUTTON_ADD_RAW = 1;
    private final int MENU_RAW = 2;
    public  MenusAdapter(OnMenuItemClicked listner) {
        this.listner = listner;
        activity = ((Fragment) listner).getActivity();
    }
    public static class MenuViewHolder extends RecyclerView.ViewHolder{
        TextView menuName;
        TextView date;
        ImageView active;
        MenuViewHolder(View itemView){
            super(itemView);
            menuName = itemView.findViewById(R.id.menu_row_name);
            active = itemView.findViewById(R.id.isActiveMenu);
            date = itemView.findViewById(R.id.menu_date);
        }
    }

    public static class ButtonAddRawHolder extends RecyclerView.ViewHolder{
        Button addButton;
        ButtonAddRawHolder(View itemView){
            super(itemView);
            addButton = itemView.findViewById(R.id.add_button);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == MENU_RAW ) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_raw, parent, false);
            return new MenuViewHolder(view);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.button_add_raw,parent,false);
            return new ButtonAddRawHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MenuViewHolder) {
            final int currentMenuPosition = position -1;
            ((MenuViewHolder) holder).menuName.setText(menus.get(currentMenuPosition).getName());
            ((MenuViewHolder)holder).date.setText(Tools.getSimpleDate(menus.get(currentMenuPosition).getDate()));
            if (menus.get(currentMenuPosition).isActive()) {
                ((MenuViewHolder) holder).active.setVisibility(View.VISIBLE);
            } else {
                ((MenuViewHolder) holder).active.setVisibility(View.GONE);
            }

            ((MenuViewHolder) holder).menuName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.onItemClicked(menus.get(currentMenuPosition));
                }
            });
            ((MenuViewHolder) holder).menuName.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Tools.showAlertDialog(activity, activity.getString(R.string.do_you_want_to_delete_menu), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listner.deleteMenu(menus.get(currentMenuPosition));

                        }
                    });
                    return false;
                }
            });
        }else{
            if (holder instanceof ButtonAddRawHolder){
                ((ButtonAddRawHolder) holder).addButton.setText(R.string.add);
                ((ButtonAddRawHolder) holder).addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listner.onAddMenuButtonClicked();
                    }
                });
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        if (position == 0){
            return BUTTON_ADD_RAW;
        }else {
            return MENU_RAW;
        }
    }

    @Override
    public int getItemCount() {
        return menus.size() + 1;
    }



    public void updateMenus(List<Menu> menus){
        this.menus = menus;
        notifyDataSetChanged();
    }

    public interface OnMenuItemClicked{
        void onItemClicked(Menu menu);
        void onAddMenuButtonClicked();
        void deleteMenu(Menu menu);
    }
}
