package com.apress.gerber.footballman.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.apress.gerber.footballman.Constants;
import com.apress.gerber.footballman.MainActivity;
import com.apress.gerber.footballman.Models.Team;
import com.apress.gerber.footballman.R;

/**
 * Created by hristiyan on 03.04.18.
 */

public class PersonsFragmentWhileInGame extends PersonsFragment {

    public static PersonsFragmentWhileInGame newInstance(Team team, boolean hide) {
        PersonsFragmentWhileInGame fragment = new PersonsFragmentWhileInGame();
        Bundle args = new Bundle();
        args.putSerializable("HIDE", hide);
        args.putSerializable("teams", team);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, Constants.MENU_ADD, Menu.NONE, R.string.add);
        menu.getItem(Constants.MENU_ADD).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, Constants.MENU_NEXT, Menu.NONE, R.string.next).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean status = false;
        if (item.getItemId() == android.R.id.home) {
            getMainActivity().getMyFragmentManager().popBackStack();
            status = true;
        }
        if (item.getItemId() == Constants.MENU_ADD) {
            if (hide) {
                if (getMainActivity().getModeForGame() == MainActivity.FIRST_TEAM) {
                    ((MainActivity) getActivity()).commitFragment(AddPersonFragment.newInstanceForGame(game, true, team), true);
                } else {
                    ((MainActivity) getActivity()).commitFragment(AddPersonFragment.newInstanceForGame(game, false, team), true);
                }
            } else {
                ((MainActivity) getActivity()).commitFragment(AddPersonFragment.newInstance(team, null, hide), true);
            }
            status = true;
        }
        if (item.getItemId() == Constants.MENU_NEXT) {
            if ((game.getHost() != null && game.getGuest() != null)) {
                mActivity.commitFragment(StartMatchFragment.newInstance(game), true);
            } else {
                Toast.makeText(getContext(), "First choose 2 teams", Toast.LENGTH_SHORT).show();
            }
        }
        return status;
    }
}
