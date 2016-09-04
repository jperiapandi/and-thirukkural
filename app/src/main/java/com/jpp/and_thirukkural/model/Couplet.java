package com.jpp.and_thirukkural.model;

/**
 * Created by jperiapandi on 05-07-2016.
 */
public class Couplet {

    public static final String COUPLET_ID = "coupletID";
    private int _id;
    private String couplet;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getCouplet() {
        return couplet;
    }

    public void setCouplet(String couplet) {
        this.couplet = couplet;
    }

    public int getChapterId() {
        Double chapterID = Math.floor((this._id-1)/10)+1;
        return chapterID.intValue();
    }
}
