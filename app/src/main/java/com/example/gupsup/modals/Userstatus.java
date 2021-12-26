package com.example.gupsup.modals;

import java.util.ArrayList;

public class Userstatus {
    private String name,profileimage;
    private long lastupdate;
    private ArrayList<Status> statuses;

    public Userstatus() {
    }
    public Userstatus(String name, String profileimage, long lastupdate, ArrayList<Status> statuses) {
        this.name = name;
        this.profileimage = profileimage;
        this.lastupdate = lastupdate;
        this.statuses = statuses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public long getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(long lastupdate) {
        this.lastupdate = lastupdate;
    }

    public ArrayList<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(ArrayList<Status> statuses) {
        this.statuses = statuses;
    }

}
