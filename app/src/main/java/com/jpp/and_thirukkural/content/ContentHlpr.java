package com.jpp.and_thirukkural.content;

import com.jpp.and_thirukkural.db.DataLoadHelper;
import com.jpp.and_thirukkural.model.Chapter;
import com.jpp.and_thirukkural.model.Couplet;
import com.jpp.and_thirukkural.model.Part;
import com.jpp.and_thirukkural.model.Section;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jperiapandi on 31-10-2016.
 */
public class ContentHlpr {

    public static ArrayList<Section> SECTIONS;
    public static ArrayList<Part> PARTS;
    public static ArrayList<Chapter> CHAPTERS;
    public static ArrayList<Couplet> COUPLETS;

    private static HashMap<String, Chapter> chapterHashMap = new HashMap<String, Chapter>();
    private static HashMap<String, ArrayList<Part>> partsOfSection = new HashMap<String, ArrayList<Part>>();
    private static HashMap<String, ArrayList<Chapter>> chaptersOfSection = new HashMap<String, ArrayList<Chapter>>();
    private static HashMap<String, ArrayList<Chapter>> chaptersOfPart = new HashMap<String, ArrayList<Chapter>>();

    public static final void init(){
        DataLoadHelper dlh = DataLoadHelper.getInstance();
        SECTIONS = dlh.getAllSections();
        PARTS = dlh.getAllParts();
        CHAPTERS = dlh.getAllChapters();
        COUPLETS = dlh.getAllCouplets(true);

        //Generate hasMaps

        for(Part p:PARTS){
            ArrayList<Part> parts = partsOfSection.get(p.getSectionId()+"");
            if(parts == null){
                parts = new ArrayList<>();
                partsOfSection.put(p.getSectionId()+"", parts);
            }
            parts.add(p);
        }

        for(Chapter c:CHAPTERS){
            chapterHashMap.put(""+c.get_id(), c);

            ArrayList<Chapter> chapters1 = chaptersOfSection.get(c.getSectionId() + "");
            if(chapters1 == null){
                chapters1 = new ArrayList<>();
                chaptersOfSection.put(c.getSectionId() + "", chapters1);
            }

            ArrayList<Chapter> chapters2 = chaptersOfPart.get(c.getPartId() + "");
            if(chapters2 == null){
                chapters2 = new ArrayList<>();
                chaptersOfPart.put(c.getPartId() + "", chapters2);
            }

            chapters1.add(c);
            chapters2.add(c);
        }
    }
    public static ArrayList<Part> getPartsBySection(int sectionId){
        return partsOfSection.get(""+sectionId);
    }

    public static ArrayList<Chapter> getChaptersBySection(int sectionId){
        return chaptersOfSection.get(""+sectionId);
    }
    public static ArrayList<Chapter> getChaptersByPart(int partId){
        return chaptersOfPart.get(""+partId);
    }

}
