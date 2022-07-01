package com.alma.smartgarden;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link IntensityObject}.
 */
public class IntensityObjectAdapter extends ArrayAdapter<IntensityObject> {

    private final List<IntensityObject> values;
    private final Context context;
    private final int resourceLayout;
    private final List<String> names;

    public IntensityObjectAdapter(Context context, int resourceLayout, List<IntensityObject> values, List<String> names) {
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

        IntensityObject p = this.getItem(position);

        if (p != null) {
            TextView tt1 = v.findViewById(R.id.attributeName);
            TextView tt2 = v.findViewById(R.id.intValue);
            View tt3 = v.findViewById(R.id.minus);
            View tt4 = v.findViewById(R.id.plus);
            IntensityViewHolder vh = new IntensityViewHolder(this.names.get(position),
                    tt3,tt4,tt2,tt1,p);
            vh.create();
        }
        return v;
    }
}