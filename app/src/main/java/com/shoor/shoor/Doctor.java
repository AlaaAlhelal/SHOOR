package com.shoor.shoor;

public class Doctor {
    private String Doctor_ID;
    private String DoctorName;
    private float AvgRate;
    private String OfficeHours;
    private double Price;
    private Hospital hospital;
    private Specialty specialty;

    public Doctor(){}
    public Doctor(String doctor_ID, String doctorName, Hospital hospital, float avgRate, String officeHours, double price) {
        Doctor_ID = doctor_ID;
        DoctorName = doctorName;
        this.hospital=hospital;
        AvgRate = avgRate;
        OfficeHours = officeHours;
        Price = price;
    }

    public Doctor(String doctor_ID, String doctorName, Hospital hospital, String officeHours, Specialty specialty) {
        Doctor_ID = doctor_ID;
        DoctorName = doctorName;
        this.hospital=hospital;
        OfficeHours = officeHours;
        this.specialty = specialty;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public String getHospital_ID() {
        return hospital.getHospitalID();
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
        return hospital.getLocationLat();
    }


    public float getLocationLng() {
        return hospital.getLocationLng();
    }

    public float getAvgRate() {
        return AvgRate;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    public void setAvgRate(float avgRate) {
        AvgRate = avgRate;
    }

    public String getOfficeHours() {
        return OfficeHours;
    }

    public double getPrice() {
        return Price;
    }

    public String getHospitalName() {
        return hospital.getHospitalName();
    }

    public String getPhoneNo() {
        return hospital.getPhoneNo();
    }

    public float getHoAngRate(){return hospital.getAvgRate();}

public Doctor GetDoctorInfo(){ return this;}
}
