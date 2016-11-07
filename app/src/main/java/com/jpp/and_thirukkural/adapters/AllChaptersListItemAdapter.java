package com.jpp.and_thirukkural.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jpp.and_thirukkural.R;
import com.jpp.and_thirukkural.content.ContentHlpr;
import com.jpp.and_thirukkural.model.Chapter;
import com.jpp.and_thirukkural.model.ListItem;
import com.jpp.and_thirukkural.model.Part;
import com.jpp.and_thirukkural.model.Section;

/**
 * Created by jperiapandi on 07-11-2016.
 */

public class AllChaptersListItemAdapter extends ListItemAdapter {

    public AllChaptersListItemAdapter(Context context, ListItem[] values) {
        super(context, values);
    }

   @Override
    protected View renderChapter(Chapter data, int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.row_chapter, parent, false);
        TextView chapter_id = (TextView) rowView.findViewById(R.id.chapter_id);
        TextView chapter_title = (TextView) rowView.findViewById(R.id.chapter_title);
        TextView chapter_serial = (TextView) rowView.findViewById(R.id.chapter_serial);
       String moreInfo = "";

       Section section = ContentHlpr.SECTIONS.get(data.getSectionId()-1);
       Part part = ContentHlpr.PARTS.get(data.getPartId()-1);
       moreInfo = section.getTitle()+" / "+part.getTitle()+" / "+getContext().getResources().getString(R.string.serial)+": "+data.getSerial();

        chapter_id.setText(data.get_id()+"");
        chapter_title.setText(data.getTitle());
        chapter_serial.setText(moreInfo);

        return rowView;
    }
}
