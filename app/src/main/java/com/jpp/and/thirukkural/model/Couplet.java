package com.jpp.and.thirukkural.model;

/**
 * Created by jperiapandi on 05-07-2016.
 */
public class Couplet implements ListItem{

    public static final String COUPLET_ID = "coupletID";
    private int _id;
    private String couplet;

    private int fav;

    private String couplet_en;
    private String expln_muva;
    private String expln_pappaiah;
    private String expln_manakudavar;
    private String expln_karunanidhi;
    private String expln_en;

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


    public int getFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    public int getChapterId() {
        double chapterID = Math.floor((this._id-1)/10)+1;
        return (int) chapterID;
    }


    public String getCouplet_en() {
        return couplet_en;
    }

    public void setCouplet_en(String couplet_en) {
        this.couplet_en = couplet_en;
    }

    public String getExpln_muva() {
        return expln_muva;
    }

    public void setExpln_muva(String expln_muva) {
        this.expln_muva = expln_muva;
    }

    public String getExpln_pappaiah() {
        return expln_pappaiah;
    }

    public void setExpln_pappaiah(String expln_pappaiah) {
        this.expln_pappaiah = expln_pappaiah;
    }

    public String getExpln_manakudavar() {
        return expln_manakudavar;
    }

    public void setExpln_manakudavar(String expln_manakudavar) {
        this.expln_manakudavar = expln_manakudavar;
    }

    public String getExpln_karunanidhi() {
        return expln_karunanidhi;
    }

    public void setExpln_karunanidhi(String expln_karunanidhi) {
        this.expln_karunanidhi = expln_karunanidhi;
    }

    public String getExpln_en() {
        return expln_en;
    }

    public void setExpln_en(String expln_en) {
        this.expln_en = expln_en;
    }

    @Override
    public ListItemType getListItemType() {
        return ListItemType.COUPLET;
    }
}
