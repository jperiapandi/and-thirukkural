package com.jpp.and.thirukkural.model;

/**
 * Created by jperiapandi on 05-07-2016.
 */
public class Part implements ListItem {

    private int _id;
    private String title;
    private int sectionId;


    private int numOfChapters = 0;

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

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }


    public int getNumOfChapters() {
        return numOfChapters;
    }

    public void setNumOfChapters(int numOfChapters) {
        this.numOfChapters = numOfChapters;
    }

    @Override
    public ListItemType getListItemType() {
        return ListItemType.PART;
    }
}
