package com.alma.smartgarden;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link OnOffObject}.
 */
public class OnOffAdapter extends ArrayAdapter<OnOffObject> {

    private final List<OnOffObject> values;
    private final List<String> names;
    private final Context context;
    private final int resourceLayout;

    public OnOffAdapter(Context context, int resourceLayout, List<OnOffObject> values, List<String> names) {
        super(context, resourceLayout, values);
        this.values = values;
        this.context = context;
        this.resourceLayout = resourceLayout;
        this.names = names;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(this.context);
            v = vi.inflate(this.resourceLayout, null);
        }

        OnOffObject p = this.getItem(position);

        if (p != null) {
            TextView tt1 = v.findViewById(R.id.attributeName);
            SwitchMaterial tt2 = v.findViewById(R.id.switchButton);
            OnOffViewHolder vh = new OnOffViewHolder(names.get(position),tt1,tt2,p);
            vh.create();
        }
        return v;
    }
}