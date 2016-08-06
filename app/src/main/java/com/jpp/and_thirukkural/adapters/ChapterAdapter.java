package com.jpp.and_thirukkural.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jpp.and_thirukkural.R;
import com.jpp.and_thirukkural.model.Chapter;

/**
 * Created by jperiapandi on 06-08-2016.
 */
public class ChapterAdapter extends ArrayAdapter<Chapter> {

    private final Context context;
    private final Chapter[] values;

    public ChapterAdapter(Context context, Chapter[] values){
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
            rowView = inflater.inflate(R.layout.row_chapter, parent, false);
        }
        // Otherwise, use the recycled row
        else {
            rowView = convertView;
        }

        TextView chapter_id = (TextView) rowView.findViewById(R.id.chapter_id);
        TextView chapter_title = (TextView) rowView.findViewById(R.id.chapter_title);
        TextView chapter_serial = (TextView) rowView.findViewById(R.id.chapter_serial);

        chapter_id.setText(values[position].get_id()+"");
        chapter_title.setText(values[position].getTitle());
        chapter_serial.setText(values[position].getSerial());

        return rowView;
    }
}
