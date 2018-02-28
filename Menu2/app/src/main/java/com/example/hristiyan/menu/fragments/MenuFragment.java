package com.example.hristiyan.menu.fragments;

import android.app.AlertDialog;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hristiyan.menu.Tools;
import com.example.hristiyan.menu.livedatas.FoodViewModel;
import com.example.hristiyan.menu.MainActivity;
import com.example.hristiyan.menu.MenuApplication;
import com.example.hristiyan.menu.FoodsAdapter;
import com.example.hristiyan.menu.R;
import com.example.hristiyan.menu.data.Food;
import com.example.hristiyan.menu.data.Menu;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


public class MenuFragment extends BaseFragment implements FoodsAdapter.UserTappedOverFood {
    public static final String MENU = "MENU";
    public static final String ACTIVE_MENU = "ACTIVE_MENU";
    public static final String IS_OPEN_FROM_USER = "IS_OPEN_FROM_USER";
    public static final int SPAN_COUNT = 2;
    private boolean activeMenu;
    private Menu menu;
    private List<Food> menuFoods;
    private LinearLayout menuLayout;
    private FoodsAdapter adapter;
    FoodViewModel foodViewModel;
    Observer foodObserver;
    private boolean openFromUser = false;

    public static MenuFragment newInstance(Menu menu, boolean activeMenu, boolean openFromUser) {
        Bundle args = new Bundle();
        MenuFragment fragment = new MenuFragment();
        args.putSerializable(MENU, menu);
        args.putBoolean(ACTIVE_MENU, activeMenu);
        args.putBoolean(IS_OPEN_FROM_USER, openFromUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.menu = (Menu) getArguments().getSerializable(MENU);
            this.activeMenu = getArguments().getBoolean(ACTIVE_MENU);
            this.openFromUser = getArguments().getBoolean(IS_OPEN_FROM_USER);
            adapter = new FoodsAdapter(activeMenu, openFromUser, this);
            adapter.setMenuTitle(menu.getName());
        }
        foodViewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(FoodViewModel.class);
        foodObserver = new Observer<List<Food>>() {
            @Override
            public void onChanged(@Nullable List<Food> foods) {
                menuFoods = foods;
                adapter.updateFoods(foods);
            }
        };
        foodViewModel.observe((LifecycleOwner) getActivity(), foodObserver);
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (!openFromUser) {
            inflater.inflate(R.menu.menu, menu);
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean status = false;
        if (item.getItemId() == R.id.set_active) {
            status = true;
            for (Menu men : MenuApplication.getMenuApplication().getDataManager().getLocalMenus()) {
                if (men.isActive()) {
                    men.setActive(false);
                    MenuApplication.getMenuApplication().getDataManager().updateMenu(men);
                }
            }
            ///
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
            dialogBuilder.setView(dialogView);
            final EditText edt = dialogView.findViewById(R.id.edit1);
            edt.setText(menu.getName());
            edt.setSelection(menu.getName().length());
            dialogBuilder.setTitle(getString(R.string.add_menu));
            dialogBuilder.setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Menu newCopiedMenu = new Menu();
                    newCopiedMenu.setDate(System.currentTimeMillis());
                    newCopiedMenu.setName(edt.getText().toString());
                    newCopiedMenu.setActive(true);
                    MenuApplication.getMenuApplication().getDataManager().addMenu(newCopiedMenu);
                    for (Food food : menuFoods) {
                        Food foodToNewMenu = new Food(newCopiedMenu.getId());
                        foodToNewMenu.setName(food.getName());
                        newCopiedMenu.addFood(foodToNewMenu);
                        foodViewModel.addFoodWithoutNotifyUi(foodToNewMenu);
                    }
                    ((MainActivity) getActivity()).getFragmentManager().popBackStack();
                    ((MainActivity) getActivity()).showFragmentAndAddToBackstack(MenuFragment.newInstance(newCopiedMenu, newCopiedMenu.isActive(), false));
                }
            });
            dialogBuilder.setNegativeButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });
            AlertDialog b = dialogBuilder.create();
            b.show();
            ///
        } else if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
        }
        return status;
    }

    @Override
    public void onCreateView() {
        menuLayout = view.findViewById(R.id.menu_layoout);
        RecyclerView foodsRecyclerView = view.findViewById(R.id.foods);
        foodViewModel.loadFoodsForMenu(menu.getId());
        final GridLayoutManager manager = new GridLayoutManager(getActivity(), SPAN_COUNT);
        foodsRecyclerView.setAdapter(adapter);
        foodsRecyclerView.setLayoutManager(manager);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return SPAN_COUNT;
                }
                return 1;
            }
        });
        if (!openFromUser) {

        } else {
            menuLayout.setBackgroundResource(Tools.generatePic(getActivity()));
            ((MainActivity) getActivity()).getSupportActionBar().hide();
        }
    }

    public void addFoodDialog(final Food food) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);
        final EditText edt = dialogView.findViewById(R.id.edit1);
        if (food == null) {
            dialogBuilder.setTitle(getString(R.string.add_food));
        } else {
            dialogBuilder.setTitle(getString(R.string.update));
            edt.setText(food.getName());
        }
        dialogBuilder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Food updatedFood = food;
                if (updatedFood == null) {
                    updatedFood = new Food(menu.getId());
                }
                updatedFood.setName(edt.getText().toString());
                if (food == null) {
                    menu.addFood(updatedFood);
                }
                foodViewModel.addOrUpdateFood(updatedFood);
            }
        });
        dialogBuilder.setNegativeButton(getString(R.string.close), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    @Override
    public void userTappedOverFood(Food food) {
        menu.selectFood(food);
        foodViewModel.updateFoodWithoutNotify(food);

    }

    @Override
    public void deleteFood(Food food) {
        foodViewModel.deleteFood(food);
    }

    @Override
    public void updateFood(Food food) {
        addFoodDialog(food);

    }

    @Override
    public void addFood() {
        addFoodDialog(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter.clearAdapter();
    }

}
