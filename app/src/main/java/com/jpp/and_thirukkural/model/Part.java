package com.jpp.and_thirukkural.model;

/**
 * Created by jperiapandi on 05-07-2016.
 */
public class Part {
    private int _id;
    private String title;
    private Section section;

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

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }
}
