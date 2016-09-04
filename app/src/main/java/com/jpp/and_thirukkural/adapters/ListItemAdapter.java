package com.jpp.and_thirukkural.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jpp.and_thirukkural.R;
import com.jpp.and_thirukkural.db.DataLoadHelper;
import com.jpp.and_thirukkural.model.Chapter;
import com.jpp.and_thirukkural.model.ListItem;
import com.jpp.and_thirukkural.model.ListItemType;
import com.jpp.and_thirukkural.model.Part;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jperiapandi on 08-08-2016.
 */
public class ListItemAdapter extends ArrayAdapter<ListItem> {
    private Context context;
    private ListItem[] values;

    public ListItemAdapter(Context context, ListItem[] values) {
        super(context, -1, values);

        this.context = context;
        this.values = values;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = null;
        ListItem item = values[position];

        // Get the right type of view depending on the type of data

        switch (item.getListItemType())
        {
            case PART:
                rowView = inflater.inflate(R.layout.row_part, parent, false);
                renderPart(rowView, (Part) item);
                break;
            case CHAPTER:
                rowView = inflater.inflate(R.layout.row_chapter, parent, false);
                renderChapter(rowView, (Chapter) item);
                break;
        }

        return rowView;
    }

    private void renderPart(View rowView, Part part){
        TextView partName = (TextView) rowView.findViewById(R.id.partName);
        TextView chaptersCount = (TextView) rowView.findViewById(R.id.chaptersCount);

        String partNameStr = part.get_id()+"  "+part.getTitle()+"";
        String chaptersCountStr = part.getNumOfChapters()+" "+getContext().getResources().getString(R.string.chapters);
        if(part.getNumOfChapters() == 1){
            chaptersCountStr = part.getNumOfChapters()+" "+getContext().getResources().getString(R.string.chapter);
        }
        partName.setText(partNameStr);
        chaptersCount.setText(chaptersCountStr);
    }
    private void renderChapter(View rowView, Chapter chapter){

        TextView chapter_id = (TextView) rowView.findViewById(R.id.chapter_id);
        TextView chapter_title = (TextView) rowView.findViewById(R.id.chapter_title);
        TextView chapter_serial = (TextView) rowView.findViewById(R.id.chapter_serial);

        chapter_id.setText(chapter.get_id()+"");
        chapter_title.setText(chapter.getTitle());
        chapter_serial.setText(chapter.getSerial());
    }
}
