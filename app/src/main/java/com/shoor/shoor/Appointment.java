package com.shoor.shoor;

import java.sql.Time;
import java.util.Date;

public class Appointment {
    private String ID;
    private Date date;
    private Time time;
    private String Note;

    public Appointment(String ID, Date date, Time time, String note) {
        this.ID = ID;
        this.date = date;
        this.time = time;
        Note = note;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }
}
