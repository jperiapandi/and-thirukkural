package com.jpp.and.thirukkural.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.jpp.and.thirukkural.R;
import com.jpp.and.thirukkural.content.ContentHlpr;
import com.jpp.and.thirukkural.model.Chapter;
import com.jpp.and.thirukkural.model.Couplet;
import com.jpp.and.thirukkural.model.ListItem;
import com.jpp.and.thirukkural.model.Part;
import com.jpp.and.thirukkural.model.Section;

public class AllCoupletsListItemAdapter extends ListItemAdapter {
    public AllCoupletsListItemAdapter(Context context, ListItem[] values) {
        super(context, values);
    }

    @Override
    protected View renderCouplet(Couplet couplet, int position, View convertView, ViewGroup parent) {
        Chapter chapter = ContentHlpr.CHAPTERS.get(couplet.getChapterId()-1);
        Section section = ContentHlpr.SECTIONS.get(chapter.getSectionId()-1);
        Part part = ContentHlpr.PARTS.get(chapter.getPartId()-1);

        String detail = getContext().getResources().getString(R.string.couplet)+" "+couplet.get_id()+" : "+section.getTitle();
        detail += " / "+part.getTitle();
        detail += " / "+chapter.getTitle();

        View rowView = inflater.inflate(R.layout.row_couplet2, parent, false);

//        TextView couplet_id = (TextView) rowView.findViewById(R.id.couplet_id);
        TextView couplet_text = (TextView) rowView.findViewById(R.id.couplet_text);
        TextView couplet_detail = (TextView) rowView.findViewById(R.id.couplet_detail_text);

//        couplet_id.setText(data.get_id()+"");
        couplet_text.setText(couplet.getCouplet());
        couplet_detail.setText(detail);

        int color = ContextCompat.getColor(parent.getContext(), R.color.fav_couplete_color);
        if(couplet.getFav() == 1){
            couplet_text.setTextColor(color);
        }

        return rowView;
    }
}
