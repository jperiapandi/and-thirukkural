package com.jpp.and.thirukkural.model;

/**
 * Created by jperiapandi on 05-07-2016.
 */
public class SearchHistory implements ListItem {
    private int _id;
    private String query;
    private int softDelete;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getSoftDelete2() {
        return softDelete;
    }

    public void setSoftDelete(int softDelete) {
        this.softDelete = softDelete;
    }

    @Override
    public ListItemType getListItemType() {
        return ListItemType.SEARCH_HISTORY;
    }
}
