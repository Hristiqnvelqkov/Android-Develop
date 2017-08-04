package example.streetview.stamsoft.app.streetviewexample.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import example.streetview.stamsoft.app.streetviewexample.R;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsVH> {

    private List<String> items;

    public SettingsAdapter(List<String> devices) {
        items = new ArrayList<>();
        items.addAll(devices);
        for (int i = 0; i < 6 - devices.size(); ++i) {
            items.add("");
        }
    }

    @Override
    public SettingsVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_row, parent, false);

        return new SettingsVH(view);
    }

    @Override
    public void onBindViewHolder(final SettingsVH holder, final int position) {
        if (items.get(position) != null) {
            holder.item.setText(items.get(position));
        }
        holder.item.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                items.set(position, charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class SettingsVH extends RecyclerView.ViewHolder {

        EditText item;

        public SettingsVH(View itemView) {
            super(itemView);

            item = (EditText) itemView.findViewById(R.id.settingsEdit);
        }
    }

    public List<String> getItems() {
        return items;
    }
}
