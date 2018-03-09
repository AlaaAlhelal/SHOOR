package com.shoor.shoor;


import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EditDoctor extends AppCompatActivity {
    public EditText DoctorName, DoctorHours,DoctorPrice;
    public Spinner  doctorlist,hospitallist,departmentlist;

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
        DoctorPrice=(EditText)findViewById(R.id.Doctor_Price);
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
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////
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
                String dp= rs.getString("DoctorName");
                doctors.add(dp);
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
    //////////////////////////////////////////////////////////////////////////////////
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
    ///////////////////////////////////////////////////////////////////////////////////////////////////
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
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void Do(View view) {
        //Validate inputs

        String DocName  = doctorlist.getSelectedItem().toString();
        String DocHours = DoctorHours.getText().toString();
        String DocPrice = DoctorPrice.getText().toString();
        String NewDecName = DoctorName.getText().toString();
        String DepName  = departmentlist.getSelectedItem().toString();
        String HosName  = hospitallist.getSelectedItem().toString();

        if (isValid(DocName,DocHours,DocPrice )) {//============================================

            //VERY IMPORTANT LINES
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //SETUP CONNECTION
            Connection conn = null;
            Statement stmt1,stmt2,stmt3 = null;
            ResultSet Result_getID_dep,Result_getID_hos;
            try{
                //STEP 2: Register JDBC driver
                Class.forName("com.mysql.jdbc.Driver");

                //STEP 3: Open a connection
                conn = DriverManager.getConnection(DB_Info.DB_URL,DB_Info.USER,DB_Info.PASS);

                //STEP 4: Execute a query
                stmt1 = conn.createStatement();
                String sql1;
                sql1= "SELECT Specialties_ID FROM specialties WHERE SpecialtiesName= ('" + DepName + "')";
                Result_getID_dep = stmt1.executeQuery(sql1);

                stmt2 = conn.createStatement();
                String sql2;
                sql2= "SELECT Hospital_ID FROM hospital WHERE HospitalName= ('" + HosName+ "')";
                Result_getID_hos = stmt2.executeQuery(sql2);

                stmt3= conn.createStatement();
                String sql3;
                sql3= "UPDATE doctor SET DoctorName =('"+NewDecName +"'), Specialties_ID= ('"+Result_getID_dep+"'),Hospital_ID=('Result_getID_hos'),AvgRate='',OfficeHours=('"+DocHours+"'),Price=('"+DocPrice+"')WHERE DoctorName=('" + DocName  + "')";
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
                Result_getID_dep.close();
                Result_getID_hos.close();
                stmt3.close();
                conn.close();
            }catch(SQLException se){
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(EditDoctor.this, "يجب أن تكون متصلاً بالانترنت(send)"+se.getMessage(), Toast.LENGTH_SHORT);
                errorToast.show();
            }catch(Exception e){
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(EditDoctor.this, ""+e.getMessage(), Toast.LENGTH_SHORT);
                errorToast.show();
            }
        }// END IF =============================================================

    }
    //////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean isValid(String Namedoctor,String DocHours ,String DocPrice ){
        //validate all inputs
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
        if(DocPrice.length() >12)//// edit
        {
            DoctorName.setError("يجب أن لا يتجاوز السعر 12 رقم");
            return false;
        }

        return true;


    }

//////////////////////////////////////////////////////////////////////////////////////
public void back(View view) {
    startActivity(new Intent(EditDoctor.this,ManageContentActivity.class));
}


}//END CLASS
