package com.shoor.shoor;

public class Doctor {
    private String Doctor_ID;
    private String DoctorName;
    private float AvgRate;
    private float LocationLat;
    private float LocationLng;
    private String OfficeHours;
    private String HospitalName;
    private String Hospital_ID;
    private String PhoneNo;

    private double Price;

    public Doctor(String doctor_ID, String doctorName, String hospital_ID ,String hospitalName, float locationLat , float locationLng, String phoneNo, float avgRate, String officeHours, double price) {
        Doctor_ID = doctor_ID;
        DoctorName = doctorName;
        Hospital_ID=hospital_ID;
        HospitalName =hospitalName;
        LocationLat=locationLat;
        LocationLng=locationLng;
        PhoneNo=phoneNo;
        AvgRate = avgRate;
        OfficeHours = officeHours;
        Price = price;
    }

    public String getHospital_ID() {
        return Hospital_ID;
    }

    public void setHospital_ID(String hospital_ID) {
        Hospital_ID = hospital_ID;
    }

    public String getDoctor_ID() {
        return Doctor_ID;
    }

    public void setDoctor_ID(String doctor_ID) {
        Doctor_ID = doctor_ID;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }

    public float getLocationLat() {
        return LocationLat;
    }

    public void setLocationLat(float locationLat) {
        LocationLat = locationLat;
    }

    public float getLocationLng() {
        return LocationLng;
    }

    public void setLocationLng(float locationLng) {
        LocationLng = locationLng;
    }

    public float getAvgRate() {
        return AvgRate;
    }

    public void setAvgRate(float avgRate) {
        AvgRate = avgRate;
    }

    public String getOfficeHours() {
        return OfficeHours;
    }

    public void setOfficeHours(String officeHours) {
        OfficeHours = officeHours;
    }

    public double getPrice() {
        return Price;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public void setHospitalName(String hospitalName) {
        HospitalName = hospitalName;
    }

    public void setPrice(double price) {
        Price = price;
    }
}
