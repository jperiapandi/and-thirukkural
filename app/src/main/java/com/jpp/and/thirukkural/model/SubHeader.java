package com.jpp.and.thirukkural.model;

/**
 * Created by jperiapandi on 11-09-2016.
 */
public class SubHeader implements ListItem {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public ListItemType getListItemType() {
        return ListItemType.SUB_HEADER;
    }
}