package com.apress.gerber.footballman.Fragments;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apress.gerber.footballman.Constants;
import com.apress.gerber.footballman.ExportData;
import com.apress.gerber.footballman.MainActivity;
import com.apress.gerber.footballman.Models.DataManager;
import com.apress.gerber.footballman.Models.League;
import com.apress.gerber.footballman.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class BaseFragment extends Fragment {
    protected InputMethodManager imm;
    public DataManager mManager;
    public static OnFragmentInteractionListener mListener;
    protected Menu menu;
    MainActivity mActivity;
    public boolean hide = false;
    DatabaseReference mFirebaseDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("leagues");
        super.onCreate(savedInstanceState);
        mManager = DataManager.getDataInstance();
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        System.out.println(hide);
        inflater.inflate(R.menu.menu, menu);
        if(this instanceof StatisticsFragment){
            menu.add(0,Constants.EXPORT_GAME,Menu.NONE,R.string.export_game);
            super.onCreateOptionsMenu(menu, inflater);
            return;
        }
        if(this instanceof GamesFragment){
            menu.removeItem(Constants.MENU_ADD);
            menu.add(0,Constants.EXPORT_GAMES,Menu.NONE,R.string.export_games).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }else {
            if ((!hide)) {
                menu.add(0, Constants.MENU_ADD, Menu.NONE, R.string.add);
                menu.getItem(Constants.MENU_ADD).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            } else {
                menu.removeItem(Constants.MENU_ADD);
                menu.add(0, Constants.MENU_NEXT, Menu.NONE, R.string.next).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            }
            if (this instanceof HomeFragment) {
                menu.add(0, Constants.ALL_GAMES, Menu.NONE, R.string.games).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            } else
                menu.removeItem(Constants.ALL_GAMES);
        }
        super.onCreateOptionsMenu(menu, inflater);

    }

    public void setActivity() {
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
    public void exportToDataBase(String fileName,int exportType){
        ExportData data = ExportData.getInstance();
        data.writeToFile(fileName,getActivity(),exportType);
    }
    protected void setLayout(View mainView, int size, int i) {
        RelativeLayout emptyLayout = mainView.findViewById(R.id.empty_layout);
        if (size > 0) {
            emptyLayout.setVisibility(View.GONE);
        } else {
            emptyLayout.setVisibility(View.VISIBLE);
            TextView emptyTeams = mainView.findViewById(R.id.textView);
            emptyTeams.setText(i);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
    public MainActivity getMainActivity(){
        return ((MainActivity) getActivity());
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void openLeague(League league);
    }

    public boolean onBackPressed(){
        if(hide){
            return true;
        }else
         return  false;
    }

}
