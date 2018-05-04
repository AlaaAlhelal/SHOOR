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
import java.util.*;

public class DeleteSpecialty extends AppCompatActivity {
    Spinner list ;
    ArrayList<String> department = new ArrayList<String>() ;
    public  ArrayAdapter<String> adapter;
    java.util.List<Specialty> specialties = new ArrayList<Specialty>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_department);
        list =(Spinner)findViewById(R.id.department_list);
        RetriveData();
        adapter =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item , department);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        list.setAdapter(adapter);
    }


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
                sql = "SELECT * FROM specialties";
                ResultSet rs = stmt.executeQuery(sql);

                //STEP 5: Extract data from result set

                while(rs.next())
                {
                    String specialtiesName= rs.getString("SpecialtiesName");
                    String specialties_id= rs.getString("Specialties_ID");
                    Specialty specialty = new Specialty(specialties_id,specialtiesName);
                    specialties.add(specialty);
                    department.add(specialtiesName);
                }

                //STEP 6: Clean-up environment
                rs.close();
                stmt.close();
                conn.close();
            }catch(SQLException se){
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(DeleteSpecialty.this, "يجب أن تكون متصلاً بالإنترنت", Toast.LENGTH_SHORT);
                errorToast.show();
            }catch(Exception e){
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(DeleteSpecialty.this, ""+e.getMessage(), Toast.LENGTH_SHORT);
                errorToast.show();
            }
        }


    public void Do(View view) {
        int index = list.getSelectedItemPosition();
        final Specialty specialty = specialties.get(index);
        AlertDialog aDialog = new AlertDialog.Builder(this).setMessage("هل أنت متأكد من حذف التخصص؟").setTitle("")
                .setNeutralButton("نعم", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,
                                        final int which) {

                        //VERY IMPORTANT LINES
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                boolean deleted = specialty.DeleteSpecialty();

                if(deleted){
                    Toast done = Toast.makeText(DeleteSpecialty.this, "تم  الحذف ", Toast.LENGTH_SHORT);
                    done.show();
                    adapter.remove(specialty.getName());
                    list.setAdapter(adapter);
                }
                else
                {
                    Toast done = Toast.makeText(DeleteSpecialty.this, "حدثت مشكلة أثناء الحذف، حاول لاحقاً", Toast.LENGTH_SHORT);
                    done.show();
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
        startActivity(new Intent(DeleteSpecialty.this,ManageContentActivity.class));
    }
}//End class