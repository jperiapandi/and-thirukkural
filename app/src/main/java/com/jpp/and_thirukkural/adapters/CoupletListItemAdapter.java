package com.jpp.and_thirukkural.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jpp.and_thirukkural.R;
import com.jpp.and_thirukkural.model.Chapter;
import com.jpp.and_thirukkural.model.Couplet;

/**
 * Created by jperiapandi on 06-08-2016.
 */
public class CoupletListItemAdapter extends ArrayAdapter<Couplet> {

    private final Context context;
    private final Couplet[] values;

    public CoupletListItemAdapter(Context context, Couplet[] values){
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView;
        // Inflate views if this is a new row
        if (convertView == null) {
            // Get the right type of view depending on the type of data
            rowView = inflater.inflate(R.layout.row_couplet, parent, false);
        }
        // Otherwise, use the recycled row
        else {
            rowView = convertView;
        }

        TextView couplet_id = (TextView) rowView.findViewById(R.id.couplet_id);
        TextView couplet_text = (TextView) rowView.findViewById(R.id.couplet_text);

        couplet_id.setText(values[position].get_id()+"");
        couplet_text.setText(values[position].getCouplet());

        return rowView;
    }
}
