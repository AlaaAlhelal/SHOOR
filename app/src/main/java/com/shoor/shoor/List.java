package com.shoor.shoor;

public class List {

    private String ListID;
    private String ListName;

    public List(String listID, String listName) {
        ListID = listID;
        ListName = listName;
    }

    public String getListID() {
        return ListID;
    }

    public void setListID(String listID) {
        ListID = listID;
    }

    public String getListName() {
        return ListName;
    }

    public void setListName(String listName) {
        ListName = listName;
    }
}
