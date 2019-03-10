package com.jpp.and.thirukkural.adapters;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jpp.and.thirukkural.R;
import com.jpp.and.thirukkural.model.Chapter;
import com.jpp.and.thirukkural.model.ListItem;
import com.jpp.and.thirukkural.model.Part;

/**
 * Created by jperiapandi on 08-08-2016.
 */
public class SearchResultListItemAdapter extends ListItemAdapter{

    public SearchResultListItemAdapter(Context context, ListItem[] values) {
        super(context, values);
    }

    @Override
    protected View renderChapter(Chapter data, int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.row_2line, parent, false);

        TextView title = (TextView) rowView.findViewById(R.id.title);
        TextView info = (TextView) rowView.findViewById(R.id.info);

        title.setText(data.getTitle());

        String information = getContext().getResources().getString(R.string.chapter_info);
        information = String.format(information, data.get_id(), data.getSerial());
        info.setText(Html.fromHtml(information));

        return rowView;
    }

    @Override
    protected View renderPart(Part data, int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.row_section, parent, false);

        TextView titleText = (TextView) rowView.findViewById(R.id.section_title);
        titleText.setText(data.getTitle());
        return rowView;
    }
}
