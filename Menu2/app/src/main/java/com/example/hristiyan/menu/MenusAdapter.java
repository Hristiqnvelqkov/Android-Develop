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

public class MenusAdapter extends RecyclerView.Adapter<MenusAdapter.MenuViewHolder> {
    private List<Menu> menus = new LinkedList<>();
    private OnMenuItemClicked listner;
    private Activity activity;
    public  MenusAdapter(OnMenuItemClicked listner) {
        this.listner = listner;
        activity = ((Fragment) listner).getActivity();
    }
    public static class MenuViewHolder extends RecyclerView.ViewHolder{
        Button menuName;
        TextView date;
        ImageView active;
        MenuViewHolder(View itemView){
            super(itemView);
            menuName = itemView.findViewById(R.id.menu_name);
            active = itemView.findViewById(R.id.isActiveMenu);
            date = itemView.findViewById(R.id.menu_date);
        }
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_raw,parent,false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuViewHolder holder, final int position) {
        holder.menuName.setText(menus.get(position).getName());
        holder.date.setText(Tools.getSimpleDate(menus.get(position).getDate()));
        if(menus.get(position).getName().length() > 12){
            holder.menuName.setTextSize(30);
            if(menus.get(position).getName().length() > 18){
                holder.menuName.setTextSize(25);
            }
        }
        if(menus.get(position).isActive()){
            holder.active.setVisibility(View.VISIBLE);
        }else{
            holder.active.setVisibility(View.GONE);
        }
        holder.menuName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onItemClicked(menus.get(position));
            }
        });
        holder.menuName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Tools.showAlertDialog(activity,activity.getString(R.string.do_you_want_to_delete_menu), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listner.deleteMenu(menus.get(position));

                    }
                });
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }
    public void updateMenus(List<Menu> menus){
        this.menus = menus;
        notifyDataSetChanged();
    }

    public interface OnMenuItemClicked{
        void onItemClicked(Menu menu);
        void deleteMenu(Menu menu);
    }
}
