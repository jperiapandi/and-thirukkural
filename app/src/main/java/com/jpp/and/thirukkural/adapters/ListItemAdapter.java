package com.jpp.and.thirukkural.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;

import com.jpp.and.thirukkural.R;
import com.jpp.and.thirukkural.model.Chapter;
import com.jpp.and.thirukkural.model.Couplet;
import com.jpp.and.thirukkural.model.ListItem;
import com.jpp.and.thirukkural.model.Part;
import com.jpp.and.thirukkural.model.SearchHistory;
import com.jpp.and.thirukkural.model.Section;
import com.jpp.and.thirukkural.model.SubHeader;

/**
 * Created by jperiapandi on 08-08-2016.
 */
public class ListItemAdapter extends ArrayAdapter<ListItem> {
    protected Context context;
    protected ListItem[] values;
    protected LayoutInflater inflater;

    public ListItemAdapter(Context context, ListItem[] values) {
        super(context, -1, values);

        this.context = context;
        this.values = values;
        inflater  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = null;
        ListItem item = values[position];

        // Get the right type of view depending on the type of data

        switch (item.getListItemType())
        {
            case SUB_HEADER:
                rowView = renderSubHeader((SubHeader) item, position, convertView, parent);
                break;

            case COUPLET:
                rowView = renderCouplet((Couplet) item, position, convertView, parent);
                break;

            case CHAPTER:
                rowView = renderChapter((Chapter) item, position,convertView,parent);
                break;

            case PART:
                rowView = renderPart((Part) item, position,convertView,parent);
                break;

            case SECTION:
                rowView = renderSection((Section) item, position,convertView,parent);
                break;

            case SEARCH_HISTORY:
                rowView = renderSearchHistory((SearchHistory) item, position, convertView, parent);
                break;
        }

        return rowView;
    }


    protected View renderSubHeader(SubHeader data, int position, View convertView, ViewGroup parent){
        View rowView = inflater.inflate(R.layout.row_sub_header, parent, false);
        TextView subHeaderTitle = (TextView) rowView.findViewById(R.id.sub_header_title);
        subHeaderTitle.setText(data.getTitle());

        return rowView;
    }

    protected View renderCouplet(Couplet data, int position, View convertView, ViewGroup parent){

        View rowView = inflater.inflate(R.layout.row_couplet, parent, false);

        TextView couplet_id = (TextView) rowView.findViewById(R.id.couplet_id);
        TextView couplet_text = (TextView) rowView.findViewById(R.id.couplet_text);

        couplet_id.setText(data.get_id()+"");
        couplet_text.setText(data.getCouplet());
        int color = ContextCompat.getColor(parent.getContext(), R.color.fav_couplete_color);
        if(data.getFav() == 1){
            couplet_text.setTextColor(color);
        }
        
        return rowView;
    }


    protected View renderChapter(Chapter data, int position, View convertView, ViewGroup parent){
        View rowView = inflater.inflate(R.layout.row_chapter, parent, false);
        TextView chapter_id = (TextView) rowView.findViewById(R.id.chapter_id);
        TextView chapter_title = (TextView) rowView.findViewById(R.id.chapter_title);
        TextView chapter_serial = (TextView) rowView.findViewById(R.id.chapter_serial);

        chapter_id.setText(data.get_id()+"");
        chapter_title.setText(data.getTitle());
        chapter_serial.setText(data.getSerial());

        return rowView;
    }

    protected View renderPart(Part data, int position, View convertView, ViewGroup parent){
        View rowView = inflater.inflate(R.layout.row_part, parent, false);
        TextView partName = (TextView) rowView.findViewById(R.id.partName);
        TextView chaptersCount = (TextView) rowView.findViewById(R.id.chaptersCount);

        String partNameStr = data.get_id()+"  "+data.getTitle()+"";
        String chaptersCountStr = data.getNumOfChapters()+" "+getContext().getResources().getString(R.string.chapters);
        if(data.getNumOfChapters() == 1){
            chaptersCountStr = data.getNumOfChapters()+" "+getContext().getResources().getString(R.string.chapter);
        }
        partName.setText(partNameStr);
        chaptersCount.setText(chaptersCountStr);

        return rowView;
    }

    protected View renderSection(Section data, int position, View convertView, ViewGroup parent){
        View rowView = inflater.inflate(R.layout.row_section, parent, false);

        TextView titleText = (TextView) rowView.findViewById(R.id.section_title);
        titleText.setText(data.getTitle());
        return rowView;
    }

    protected View renderSearchHistory(SearchHistory data, int position, View convertView, ViewGroup parent){
        View rowView = inflater.inflate(R.layout.row_search_history, parent, false);

        TextView query = (TextView) rowView.findViewById(R.id.query_text_view);
        query.setText(data.getQuery());
        return rowView;
    }

}
