package com.takemeout.android.registration.requests;

/**
 * Created by halldorr on 4/2/17.
 */

import java.sql.Date;
import java.util.List;

public class RegisterEventRequest {
    private String name;
    private String descriptionEng;
    private String descriptionIce;
    private Date time;
    private int typeId;
    private int locationId;
    private List<Integer> performerIds;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescriptionEng() { return descriptionEng; }
    public void setDescriptionEng(String descriptionEng) { this.descriptionEng = descriptionEng; }

    public String getDescriptionIce() { return descriptionIce; }
    public void setDescriptionIce(String descriptionIce) { this.descriptionIce = descriptionIce; }

    public Date getTime() { return time; }
    public void setTime(Date time) { this.time = time; }

    public int getTypeId() { return typeId; }
    public void setTypeId(int typeId) { this.typeId = typeId; }

    public int getLocationId() { return locationId; }
    public void setLocationId(int locationId) { this.locationId = locationId; }

    public List<Integer> getPerformerIds() { return performerIds; }
    public void setPerformerIds(List<Integer> performerIds) { this.performerIds = performerIds; }
}
