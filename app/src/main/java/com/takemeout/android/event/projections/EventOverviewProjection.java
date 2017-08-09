package com.takemeout.android.event.projections;

import java.sql.Date;

public class EventOverviewProjection {
    private int id;
    private String name;
    private Date time;
    private String access;
    private String typeDescriptionEng;
    private String typeDescriptionIce;
    private int typeId;
    private String locationName;
    private String address;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Date getTime() { return time; }
    public void setTime(Date time) { this.time = time; }

    public String getAccess() {return access; }
    public void setAccess(String access) { this.access = access; }

    public String getTypeDescriptionEng() { return typeDescriptionEng; }
    public void setTypeDescriptionEng(String typeDescriptionEng) { this.typeDescriptionEng = typeDescriptionEng; }

    public String getTypeDescriptionIce() { return typeDescriptionIce; }
    public void setTypeDescriptionIce(String typeDescriptionIce) { this.typeDescriptionIce = typeDescriptionIce; }

    public int getTypeId() { return typeId; }
    public void setTypeId(int typeId) { this.typeId = typeId; }

    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
