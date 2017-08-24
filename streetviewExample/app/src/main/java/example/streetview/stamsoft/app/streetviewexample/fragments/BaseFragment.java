package example.streetview.stamsoft.app.streetviewexample.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import example.streetview.stamsoft.app.streetviewexample.MainActivity;

public abstract class BaseFragment extends Fragment {
    private final static String ARG_IS_POPUP = "ARG_IS_POPUP";

    private boolean isPopup;

    protected View mainView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(getLayoutId(), null);
        ButterKnife.bind(this, mainView);
        if (getArguments() != null) {
            isPopup = getArguments().getBoolean(ARG_IS_POPUP, false);
        }
        onCreateView();
        setupToolbar();

        return mainView;
    }

    protected abstract int getLayoutId();

    protected abstract void onCreateView();

    protected abstract void setupToolbar();


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }
}
