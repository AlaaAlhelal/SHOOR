package com.shoor.shoor;

public class Doctor {
    private String Doctor_ID;
    private String DoctorName;
    private String HospitalName;
    private float AvgRate;
    private String OfficeHours;
    private double Price;

    public Doctor(String doctor_ID, String doctorName, String hospitalName, float avgRate, String officeHours, double price) {
        Doctor_ID = doctor_ID;
        DoctorName = doctorName;
        HospitalName = hospitalName;
        AvgRate = avgRate;
        OfficeHours = officeHours;
        Price = price;
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

    public String getHospitalName() {
        return HospitalName;
    }

    public void setHospitalName(String hospitalName) {
        HospitalName = hospitalName;
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

    public void setPrice(double price) {
        Price = price;
    }
}
