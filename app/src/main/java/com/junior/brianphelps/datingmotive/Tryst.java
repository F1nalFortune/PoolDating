package com.junior.brianphelps.datingmotive;

import java.util.Date;
import java.util.UUID;

/**
 * Created by brianphelps on 12/1/17.
 */

public class Tryst {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mTaken;
    private String mFriend;

    public Tryst() {
        this(UUID.randomUUID());
    }

    public Tryst(UUID id) {
        mId = id;
        mDate = new Date();
    }


    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isTaken() {
        return mTaken;
    }

    public void setTaken(boolean taken) {
        mTaken = taken;
    }
    public String getFriend() {
        return mFriend;
    }

    public void setFriend(String friend) {
        mFriend = friend;
    }

    public String getPhotoFilename() {
        return "IMG+" + getId().toString() + ".jpg";
    }
}
