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
import java.util.regex.Pattern;


public class AddDoctor  extends AppCompatActivity {
    public EditText DoctorName, DoctorHours;
    public Spinner hospitallist,departmentlist;

    ArrayList<String> department = new ArrayList<String>() ;
    ArrayList<String> hospital = new ArrayList<String>() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);

        DoctorName=(EditText)findViewById(R.id.Doctor_Name);
        DoctorHours=(EditText)findViewById(R.id.Doctor_Hours);
        hospitallist=(Spinner) findViewById(R.id.hospital_list);
        departmentlist=(Spinner) findViewById(R.id.department_list);

        //for department
        RetriveData_department();
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, department);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentlist.setAdapter(adapter1);

        //for hospital
        RetriveData_hospital();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hospital);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hospitallist.setAdapter(adapter2);

    }
    //////////////////////////////////////////////////////////////////////////////////////////////////

    public void Send(View view) {


        //Validate inputs
        String DocName  = DoctorName.getText().toString();
        String DocHours = DoctorHours.getText().toString();
        String DepName  = departmentlist.getSelectedItem().toString();
        String HosName  = hospitallist.getSelectedItem().toString();

        if (isValid(DocName,DocHours )) {//============================================

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
                sql1= "SELECT * FROM specialty WHERE SpecialtiesName= ('" + DepName + "')";
                Result_getID_dep = stmt1.executeQuery(sql1);
                    int speId=0, hosId=0;
                while (Result_getID_dep.next()){
                    speId = Result_getID_dep.getInt("Specialties_ID");
                }
                stmt1.close();
                stmt2 = conn.createStatement();
                String sql2;
                sql2= "SELECT * FROM hospital WHERE HospitalName= ('" + HosName+ "')";
                Result_getID_hos = stmt2.executeQuery(sql2);
                while (Result_getID_hos.next()){
                    hosId = Result_getID_hos.getInt("Hospital_ID");
                }
                stmt2.close();
                stmt3= conn.createStatement();
                String sql3;
                sql3= "INSERT INTO doctor(Doctor_ID, DoctorName, Specialties_ID, Hospital_ID, OfficeHours) VALUES ('','"+DocName+"', '"+speId+"','"+hosId+"','"+DocHours+"')";
                int rs = stmt3.executeUpdate(sql3);

                if(rs==1){
                    Toast done = Toast.makeText(AddDoctor.this, "تمت الإضافة", Toast.LENGTH_SHORT);
                    done.show();
                    DoctorName.setText("");
                    DoctorHours.setText("");

                }
                else
                {
                    Toast done = Toast.makeText(AddDoctor.this, "حدثت مشكلة أثناء الاضافة", Toast.LENGTH_SHORT);
                    done.show();
                }
                //STEP 6: Clean-up environment
                Result_getID_dep.close();
                Result_getID_hos.close();
                stmt3.close();
                conn.close();
            }catch(SQLException se){
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(AddDoctor.this, "يجب أن تكون متصلاً بالانترنت"+se.getMessage(), Toast.LENGTH_SHORT);
                errorToast.show();
            }catch(Exception e){
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(AddDoctor.this, ""+e.getMessage(), Toast.LENGTH_SHORT);
                errorToast.show();
            }
        }// END IF =============================================================

    }
    //////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean isValid(String Namedoctor,String DocHours   ){

String pattrenAr = "[\\u0600-\\u06FF]+";
Pattern p = Pattern.compile(pattrenAr);
        String docname =Namedoctor.replaceAll("\\s+","");

        //validate all inputs
        if (!p.matcher(docname).matches() ) {
            DoctorName.setError("يجب إدخال أحرف عربية فقط");
            return false;
        }
        if (Namedoctor.equals("") ) {
            DoctorName.setError("يجب ملء الخانة");
            return false;
        }
        if(Namedoctor.length() >30)
        {
            DoctorName.setError("يجب أن لا يتجاوز الاسم 30 حرفاً");
            return false;
        }

        if(DocHours.length()>400)
        {
            DoctorHours.setError("يجب أن لا يتجاوز الاسم 400 حرفاً");
            return false;
        }

        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
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
            sql = "SELECT * FROM specialty";
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
            Toast errorToast = Toast.makeText(AddDoctor.this, "يجب أن تكون متصلاً بالإنترنت", Toast.LENGTH_SHORT);
            errorToast.show();
        } catch (Exception e) {
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(AddDoctor.this, "" + e.getMessage(), Toast.LENGTH_SHORT);
            errorToast.show();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
            Toast errorToast = Toast.makeText(AddDoctor.this, "يجب أن تكون متصلاً بالإنترنت", Toast.LENGTH_SHORT);
            errorToast.show();
        } catch (Exception e) {
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(AddDoctor.this, "" + e.getMessage(), Toast.LENGTH_SHORT);
            errorToast.show();
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void back(View view) {
        this.finish();
        startActivity(new Intent(AddDoctor.this, ManageContentActivity.class));
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}// end class

