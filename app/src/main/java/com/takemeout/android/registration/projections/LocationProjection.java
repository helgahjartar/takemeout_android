package com.takemeout.android.registration.projections;

/**
 * Created by halldorr on 4/2/17.
 */

public class LocationProjection {
    private int id;
    private String name;
    private String address;
    private String access;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getAccess() {return access; }
    public void setAccess(String access) { this.access = access; }
}
