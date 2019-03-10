package com.jpp.and.thirukkural.model;

import java.util.ArrayList;

/**
 * Created by jperiapandi on 09-09-2016.
 */
public class SearchResult {
    private String q;
    private ArrayList<Section> sections;
    private ArrayList<Part> parts;
    private ArrayList<Chapter> chapters;
    private ArrayList<Couplet> couplets;

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    public void setSections(ArrayList<Section> sections) {
        this.sections = sections;
    }

    public ArrayList<Part> getParts() {
        return parts;
    }

    public void setParts(ArrayList<Part> parts) {
        this.parts = parts;
    }

    public ArrayList<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(ArrayList<Chapter> chapters) {
        this.chapters = chapters;
    }

    public ArrayList<Couplet> getCouplets() {
        return couplets;
    }

    public void setCouplets(ArrayList<Couplet> couplets) {
        this.couplets = couplets;
    }

    public boolean isSearchResultFound(){
        boolean r = false;
        boolean sectionsFound = false;
        boolean partsFound = false;
        boolean chaptersFound = false;
        boolean coupletsFound = false;

        if(sections != null && sections.size() > 0)
        {
            sectionsFound = true;
        }

        if(parts != null && parts.size() > 0)
        {
            partsFound = true;
        }

        if(chapters != null && chapters.size() > 0)
        {
            chaptersFound = true;
        }

        if(couplets != null && couplets.size() > 0)
        {
            coupletsFound = true;
        }

        r = sectionsFound || partsFound || chaptersFound || coupletsFound;
        return r;
    }
}
