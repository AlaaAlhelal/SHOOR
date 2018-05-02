package com.shoor.shoor;


import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class EditDoctor extends AppCompatActivity {
    public EditText DoctorName, DoctorHours;
    public Spinner  doctorlist,hospitallist,departmentlist;
    ArrayList<Doctor> Doctors = new ArrayList<Doctor>();
    ArrayList<Hospital> Hospitals = new ArrayList<Hospital>();
    ArrayList<Specialties> Specialty = new ArrayList<Specialties>();

    ArrayList<String> doctors = new ArrayList<String>() ;
    ArrayList<String> department = new ArrayList<String>() ;
    ArrayList<String> hospital = new ArrayList<String>() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_doctor);

        doctorlist=(Spinner) findViewById(R.id.doctors_list);
        DoctorName=(EditText)findViewById(R.id.Doctor_Name);
        DoctorHours=(EditText)findViewById(R.id.Doctor_Hours);
        hospitallist=(Spinner) findViewById(R.id.hospital_list);
        departmentlist=(Spinner) findViewById(R.id.department_list);


        //for doctor
        RetriveData_doctor_name();
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, doctors);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctorlist.setAdapter(adapter1);

        //for department
        RetriveData_department();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, department);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentlist.setAdapter(adapter2);

        //for hospital
        RetriveData_hospital();
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hospital);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hospitallist.setAdapter(adapter3);


        SetRetrivedInfo();


        doctorlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SetRetrivedInfo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void RetriveData_doctor_name() {

        //VERY IMPORTANT LINES
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //SETUP CONNECTION
        Connection conn = null;
        Statement stmt = null;
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            conn = DriverManager.getConnection(DB_Info.DB_URL, DB_Info.USER, DB_Info.PASS);

            //STEP 4: Execute a query
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM doctor";
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set
            while (rs.next()) {
                String docName= rs.getString("DoctorName");
                Hospital hospital = new Hospital(rs.getString("Hospital_ID"));
                Specialties specialties = new Specialties(rs.getString("specialties_ID"));
                Doctor doc = new Doctor( rs.getString("Doctor_ID"),  docName,  hospital,  rs.getString("OfficeHours"),  specialties);
                Doctors.add(doc);
                doctors.add(docName);
            }
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(EditDoctor.this, "يجب أن تكون متصلاً بالإنترنت", Toast.LENGTH_SHORT);
            errorToast.show();
        } catch (Exception e) {
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(EditDoctor.this, "" + e.getMessage(), Toast.LENGTH_SHORT);
            errorToast.show();
        }
    }


    public void RetriveData_department() {

        //VERY IMPORTANT LINES
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //SETUP CONNECTION
        Connection conn = null;
        Statement stmt = null;
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            conn = DriverManager.getConnection(DB_Info.DB_URL, DB_Info.USER, DB_Info.PASS);

            //STEP 4: Execute a query
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM specialties";
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set
            while (rs.next()) {
                String dp= rs.getString("SpecialtiesName");
                Specialties specialties  = new Specialties();
                specialties.setID(rs.getString("Specialties_ID"));
                specialties.setName(dp);
                Specialty.add(specialties);
                department.add(dp);
            }
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(EditDoctor.this, "يجب أن تكون متصلاً بالإنترنت", Toast.LENGTH_SHORT);
            errorToast.show();
        } catch (Exception e) {
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(EditDoctor.this, "" + e.getMessage(), Toast.LENGTH_SHORT);
            errorToast.show();
        }
    }


    public void RetriveData_hospital() {

        //VERY IMPORTANT LINES
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //SETUP CONNECTION
        Connection conn = null;
        Statement stmt = null;
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            conn = DriverManager.getConnection(DB_Info.DB_URL, DB_Info.USER, DB_Info.PASS);

            //STEP 4: Execute a query
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM hospital";
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set
            while (rs.next()) {
                String dp= rs.getString("HospitalName");
                Hospital hos = new Hospital();
                hos.setHospitalID(rs.getString("Hospital_ID"));
                hos.setHospitalName(dp);
                Hospitals.add(hos);
                hospital.add(dp);
            }
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(EditDoctor.this, "يجب أن تكون متصلاً بالإنترنت", Toast.LENGTH_SHORT);
            errorToast.show();
        } catch (Exception e) {
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(EditDoctor.this, "" + e.getMessage(), Toast.LENGTH_SHORT);
            errorToast.show();
        }
    }


    public void Do(View view) {
        //Validate inputs

        int Docindex = doctorlist.getSelectedItemPosition();
        int Speindex = departmentlist.getSelectedItemPosition();
        int Hosindex = hospitallist.getSelectedItemPosition();

        String DocHours = DoctorHours.getText().toString();
        String NewDecName = DoctorName.getText().toString();

        if (isValid(NewDecName,DocHours )) {//============================================

            //VERY IMPORTANT LINES
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //SETUP CONNECTION
            Connection conn = null;
            Statement stmt3 = null;
            ResultSet Result_getID_dep,Result_getID_hos;
            try{
                //STEP 2: Register JDBC driver
                Class.forName("com.mysql.jdbc.Driver");

                //STEP 3: Open a connection
                conn = DriverManager.getConnection(DB_Info.DB_URL,DB_Info.USER,DB_Info.PASS);
                stmt3= conn.createStatement();
                String sql3;
                sql3= "UPDATE doctor SET DoctorName =('"+NewDecName +"') , Specialties_ID= ('"+Specialty.get(Speindex).getID()+"'), Hospital_ID=('"+Hospitals.get(Hosindex).getHospitalID()+"') , OfficeHours=('"+DocHours+"') WHERE Doctor_ID=('" + Doctors.get(Docindex).getDoctor_ID()  + "')";
                int rs = stmt3.executeUpdate(sql3);
                if(rs==1){
                    Toast done = Toast.makeText(EditDoctor.this, "تم التعديل", Toast.LENGTH_SHORT);
                    done.show();
                }
                else
                {
                    Toast done = Toast.makeText(EditDoctor.this, "حدثت مشكلة أثناء التعديل", Toast.LENGTH_SHORT);
                    done.show();
                }
                //STEP 6: Clean-up environment
                stmt3.close();
                conn.close();
            }catch(SQLException se){
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(EditDoctor.this, "يجب أن تكون متصلاً بالانترنت "+se.getMessage(), Toast.LENGTH_SHORT);
                errorToast.show();
            }catch(Exception e){
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(EditDoctor.this, ""+e.getMessage(), Toast.LENGTH_SHORT);
                errorToast.show();
            }
        }// END IF =============================================================

    }



    public boolean isValid(String Namedoctor,String DocHours  ){

        //validate all inputs
        String pattrenAr = "[\\u0600-\\u06FF]+";
        Pattern pHosName = Pattern.compile(pattrenAr);

        //validate all inputs
        if (!pHosName.matcher(Namedoctor).matches() ) {
            DoctorName.setError("يجب إدخال أحرف عربية فقط");
            return false;
        }
        if (Namedoctor.equals("") ) {
            DoctorName.setError("يجب ملء الخانة");
            return false;
        }
        if(Namedoctor.length() >20)
        {
            DoctorName.setError("يجب أن لا يتجاوز الاسم 20 حرفاً");
            return false;
        }
        if(DocHours.length() >15)
        {
            DoctorName.setError("يجب أن لا يتجاوز الاسم 20 حرفاً");
            return false;
        }

        return true;
    }

    public void SetRetrivedInfo(){
        int index = doctorlist.getSelectedItemPosition();
        DoctorName.setText(Doctors.get(index).getDoctorName());
        DoctorHours.setText(Doctors.get(index).getOfficeHours());
        for (int i=0 ; i<Hospitals.size();i++) {
            if (Hospitals.get(i).getHospitalID().equals(Doctors.get(index).getHospital_ID())){
                hospitallist.setSelection(i);
            break;}

        }

        for (int i=0 ; i<Specialty.size();i++) {
            if (Specialty.get(i).getID().equals(Doctors.get(index).getSpecialties().getID())){
                departmentlist.setSelection(i);
                break;}

        }
    }

//////////////////////////////////////////////////////////////////////////////////////
public void back(View view) {
        this.finish();
    startActivity(new Intent(EditDoctor.this,ManageContentActivity.class));
}


}//END CLASS
