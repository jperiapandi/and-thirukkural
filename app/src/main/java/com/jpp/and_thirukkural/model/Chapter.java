package com.jpp.and_thirukkural.model;

/**
 * Created by jperiapandi on 05-07-2016.
 */
public class Chapter {
    private int _id;
    private String title;
    private int partId;
    private  int sectionId;

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

    public int getPartId() {
        return partId;
    }

    public void setPartId(int partId) {
        this.partId = partId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public CharSequence getSerial() {
        return (((this._id-1)*10)+1)+" - "+(this._id*10);
    }
}
