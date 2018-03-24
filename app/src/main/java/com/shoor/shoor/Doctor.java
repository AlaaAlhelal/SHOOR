package com.shoor.shoor;

public class Doctor {

    public int doc_id;
    public int hospital_id;
    public int department_id;
    public double rate ;
    public double price ;
    public String docname ;
    public String officeHours ;

    public Doctor ( int doc_id, int hospital_id, int department_id, double rate, double price, String docname , String officeHours)
    {    this.doc_id = doc_id;
        this.hospital_id =hospital_id ;
        this.department_id =department_id;
        this.rate =rate;
        this.price =price;
        this.docname = docname;
        this.officeHours = officeHours;
    }

    public int getDoc_id (){
        return doc_id;
    }

    public int getHospital_id (){
        return hospital_id ;
    }

    public int getDepartment_id (){
        return department_id ;
    }

    public double getRate(){
        return rate;
    }

    public double getPrice (){
        return price ;
    }

    public String getDocname(){
        return docname;
    }

    public String getOfficeHours(){
        return officeHours;
    }








}
