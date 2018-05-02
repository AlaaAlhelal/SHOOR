package com.shoor.shoor;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DeleteDoctor extends AppCompatActivity {
    Spinner list ;
    ArrayList<String> doctor = new ArrayList<String>();
    ArrayList<Doctor> doctorObject = new ArrayList<Doctor>();

    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_doctor);
        list =(Spinner)findViewById(R.id.Doctor_list);
        RetriveData();
        adapter =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item , doctor);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        list.setAdapter(adapter);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void RetriveData() {

        //VERY IMPORTANT LINES
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //SETUP CONNECTION
        Connection conn = null;
        Statement stmt = null;
        try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            conn = DriverManager.getConnection(DB_Info.DB_URL,DB_Info.USER,DB_Info.PASS);

            //STEP 4: Execute a query
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM doctor";
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set

            while(rs.next())
            {
                String doctorName = rs.getString("DoctorName");
            Doctor doctor1 = new Doctor();
            doctor1.setDoctor_ID(rs.getString("Doctor_ID"));
            doctor1.setDoctorName(doctorName);
            doctorObject.add(doctor1);
                doctor.add(doctorName);
            }

            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(DeleteDoctor.this, "يجب أن تكون متصلاً بالإنترنت", Toast.LENGTH_SHORT);
            errorToast.show();
        }catch(Exception e){
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(DeleteDoctor.this, ""+e.getMessage(), Toast.LENGTH_SHORT);
            errorToast.show();
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void Do(View view) {



        AlertDialog aDialog = new AlertDialog.Builder(this).setMessage("هل أنت متأكد من حذف الطبيب؟").setTitle("")
                .setNeutralButton("نعم", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,
                                        final int which) {


        int index = list.getSelectedItemPosition();

        //SETUP CONNECTION
        Connection conn = null;
        Statement stmt = null;
        try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            conn = DriverManager.getConnection(DB_Info.DB_URL,DB_Info.USER,DB_Info.PASS);

            //STEP 4: Execute a query
            stmt = conn.createStatement();
            String sql;
            sql = "DELETE FROM doctor WHERE Doctor_ID= ('" + doctorObject.get(index).getDoctor_ID() + "')";
            int rs = stmt.executeUpdate(sql);

            if(rs==1){
                Toast done = Toast.makeText(DeleteDoctor.this, "تم  الحذف ", Toast.LENGTH_SHORT);
                done.show();
                adapter.remove(doctorObject.get(index).getDoctorName());
                list.setAdapter(adapter);
            }
            else
            {
                Toast done = Toast.makeText(DeleteDoctor.this, "حدثت مشكلة أثناء الحذف", Toast.LENGTH_SHORT);
                done.show();
            }
            //STEP 6: Clean-up environment
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(DeleteDoctor.this, "يجب أن تكون متصلاً بالإنترنت", Toast.LENGTH_SHORT);
            errorToast.show();
        }catch(Exception e){
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(DeleteDoctor.this, ""+e.getMessage(), Toast.LENGTH_SHORT);
            errorToast.show();
        }

                    }

                }).setNegativeButton("إلغاء",new DialogInterface.OnClickListener(){
                    public void onClick(final DialogInterface dialog,  final int which) {
                        // User cancelled the action
                    }
                }).create();

        aDialog.show();
    }

    public void back(View view) {
        startActivity(new Intent(DeleteDoctor.this,ManageContentActivity.class));
    }
}//End class
