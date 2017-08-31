package com.apress.gerber.footballman.Fragments;

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
import com.apress.gerber.footballman.Models.League;
import com.apress.gerber.footballman.Models.Team;
import com.apress.gerber.footballman.R;
import com.apress.gerber.footballman.TeamsAdapter;

import java.util.List;


public class BaseFragment extends Fragment {

    protected InputMethodManager imm ;
    public static OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

    }
    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override public boolean onOptionsItemSelected(MenuItem item){
        boolean status = false;
        if(item.getItemId()==android.R.id.home){
            MainActivity activity = (MainActivity) getActivity();
            DrawerLayout drawerLayout = activity.drawer;
            drawerLayout.openDrawer(Gravity.LEFT);
            status=true;
        }
        if(item.getItemId() == R.id.test){
            if(this instanceof HomeFragment) {
                ((MainActivity) getActivity()).commitFragment(AddLeagueFragment.newInstance(null), true);
            }
        }
        return status;
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
