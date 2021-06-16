package com.placements.abhyaas.models;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;

public class RecordModel implements Serializable {
    @DocumentId
    String recordId;
    String videoUrl;
    String Description;
    String Name;

    public String getTime() {
        return Time;
    }

    public void setTime(String Time) {
        Time = Time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        Date = Date;
    }

    String Time;
    String Date;
    @ServerTimestamp
    Date datetime;

    public RecordModel() {
    }

 
    public String getName() {
        return Name;
    }
    public String getDescription() {
        return Description;
    }

    public String getRecordId() {
        return recordId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
    

    public Date getDatetime() {
        return datetime;
    }
}
