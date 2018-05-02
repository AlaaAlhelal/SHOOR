package com.shoor.shoor;


public class Specialties {
    private String ID;
    private String Name;

    public Specialties(String ID, String name) {
        this.ID = ID;
        Name = name;
    }

    public Specialties(String ID) {
        this.ID = ID;
    }

    public Specialties() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
