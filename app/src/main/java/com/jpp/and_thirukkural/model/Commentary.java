package com.jpp.and_thirukkural.model;

/**
 * Created by jperiapandi on 11-09-2016.
 */
public class Commentary implements ListItem {
    private String commentaryBy;
    private String commentary;

    @Override
    public ListItemType getListItemType() {
        return ListItemType.COMMENTARY;
    }

    public String getCommentaryBy() {
        return commentaryBy;
    }

    public void setCommentaryBy(String commentaryBy) {
        this.commentaryBy = commentaryBy;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }
}
