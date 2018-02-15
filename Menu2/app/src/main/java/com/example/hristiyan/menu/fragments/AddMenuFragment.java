package com.example.hristiyan.menu.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hristiyan.menu.R;


public class AddMenuFragment extends BaseFragment {

    public static AddMenuFragment newInstance() {
        Bundle args = new Bundle();
        AddMenuFragment fragment = new AddMenuFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public int getLayoutId() {
        return R.layout.fragment_add_menu;
    }

    @Override
    public void onCreateView() {

    }
}
