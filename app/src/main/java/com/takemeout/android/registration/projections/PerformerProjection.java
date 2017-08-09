package com.takemeout.android.registration.projections;

/**
 * Created by halldorr on 4/2/17.
 */

public class PerformerProjection {
    private int id;
    private String name;
    private String descriptionEng;
    private String descriptionIce;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescriptionEng() { return descriptionEng; }
    public void setDescriptionEng(String descriptionEng) { this.descriptionEng = descriptionEng; }

    public String getDescriptionIce() { return descriptionIce; }
    public void setDescriptionIce(String descriptionIce) { this.descriptionIce = descriptionIce; }
}