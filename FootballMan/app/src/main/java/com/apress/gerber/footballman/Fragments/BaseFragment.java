package com.apress.gerber.footballman.Fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apress.gerber.footballman.MainActivity;
import com.apress.gerber.footballman.Models.Game;
import com.apress.gerber.footballman.Models.League;
import com.apress.gerber.footballman.Models.Team;
import com.apress.gerber.footballman.R;
import com.apress.gerber.footballman.TeamsAdapter;

import java.util.List;


public class BaseFragment extends Fragment {

    protected InputMethodManager imm ;
    public static OnFragmentInteractionListener mListener;
    protected Menu menu;
    MainActivity mActivity;
    public boolean hide= false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

    }
    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        this.menu = menu;
        System.out.println(hide);
        inflater.inflate(R.menu.menu,menu);
        super.onCreateOptionsMenu(menu,inflater);
        if(!hide) {
            menu.add(0, 0, Menu.NONE, R.string.add);
            menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }else{
            menu.removeItem(0);
            menu.add(0,1,Menu.NONE,R.string.next).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
    }
    public void setActivity(){
        mActivity = ((MainActivity) getActivity());
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    protected void setLayout(View mainView,RecyclerView view, RecyclerView.Adapter adapter,int i){
        RelativeLayout emptyLayout = mainView.findViewById(R.id.empty_layout);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        if(adapter.getItemCount()>0) {
            view.setLayoutManager(manager);
            view.setAdapter(adapter);
            emptyLayout.setVisibility(View.GONE);
        }else{
            TextView emptyTeams = mainView.findViewById(R.id.textView);
            emptyTeams.setText(i);
        }
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void openLeague(League league);
    }
}
