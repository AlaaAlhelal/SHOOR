package com.shoor.shoor;


public class Hospital {
    private String HospitalID;
    private String HospitalName;
    private float LocationLat;
    private float LocationLng;
    private String PhoneNo;
    private float AvgRate;

    public Hospital(String hospitalID, String hospitalName, float locationLat, float locationLng, String phoneNo, float avgRate) {
        HospitalID = hospitalID;
        HospitalName = hospitalName;
        LocationLat = locationLat;
        LocationLng = locationLng;
        PhoneNo = phoneNo;
        AvgRate = avgRate;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public float getLocationLat() {
        return LocationLat;
    }

    public float getLocationLng() {
        return LocationLng;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public float getAvgRate() {
        return AvgRate;
    }

    public String getHospitalID() {
        return HospitalID;
    }
}
