package com.jpp.and.thirukkural.model;

/**
 * Created by jperiapandi on 05-07-2016.
 */
public class Section implements ListItem {
    private int _id;
    private String title;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public ListItemType getListItemType() {
        return ListItemType.SECTION;
    }
}
