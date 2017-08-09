package com.takemeout.android.event.projections;

import java.sql.Date;

public class EventDetailProjection {
    private int id;
    private String name;
    private String descriptionEng;
    private String descriptionIce;
    private Date time;
    private String typeDescriptionEng;
    private String typeDescriptionIce;
    private String locationName;
    private String address;
    private String access;
//  @Column(name = "eventId") private List<String> performerNames;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescriptionEng() { return descriptionEng; }
    public void setDescriptionEng(String descriptionEng) { this.descriptionEng = descriptionEng; }

    public String getDescriptionIce() { return descriptionIce; }
    public void setDescriptionIce(String descriptionIce) { this.descriptionIce = descriptionIce; }

    public Date getTime() { return time; }
    public void setTime(Date time) { this.time = time; }

    public String getTypeDescriptionEng() { return typeDescriptionEng; }
    public void setTypeDescriptionEng(String typeDescriptionEng) { this.typeDescriptionEng = typeDescriptionEng; }

    public String getTypeDescriptionIce() { return typeDescriptionIce; }
    public void setTypeDescriptionIce(String typeDescriptionIce) { this.typeDescriptionIce = typeDescriptionIce; }

    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getAccess() {return access; }
    public void setAccess(String access) { this.access = access; }

//  public List<String> getPerformerNames() { return performerNames; }
//  public void setPerformerNames(List<String> performerNames) { this.performerNames = performerNames; }
}
