package com.shoor.shoor;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
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

public class DeleteDepartment extends AppCompatActivity {
    Spinner list ;
    ArrayList<String> department = new ArrayList<String>() ;
    public  ArrayAdapter<String> adapter;

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
                sql = "SELECT * FROM specialties";
                ResultSet rs = stmt.executeQuery(sql);

                //STEP 5: Extract data from result set

                while(rs.next())
                {   String dp = rs.getString("SpecialtiesName");
                  department.add(dp);
                }

                //STEP 6: Clean-up environment
                rs.close();
                stmt.close();
                conn.close();
            }catch(SQLException se){
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(DeleteDepartment.this, "يجب أن تكون متصلاً بالإنترنت", Toast.LENGTH_SHORT);
                errorToast.show();
            }catch(Exception e){
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(DeleteDepartment.this, ""+e.getMessage(), Toast.LENGTH_SHORT);
                errorToast.show();
            }
        }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void Do(View view) {
        String DepName = list.getSelectedItem().toString();

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
                sql = "DELETE FROM specialties WHERE SpecialtiesName= ('" + DepName + "')";
                int rs = stmt.executeUpdate(sql);

                if(rs==1){
                    Toast done = Toast.makeText(DeleteDepartment.this, "تم  الحذف ", Toast.LENGTH_SHORT);
                    done.show();
                    adapter.remove(DepName);
                    list.setAdapter(adapter);
                  //  this.recreate();
                }
                else
                {
                    Toast done = Toast.makeText(DeleteDepartment.this, "حدثت مشكلة أثناء الحذف", Toast.LENGTH_SHORT);
                    done.show();
                }
                //STEP 6: Clean-up environment
                stmt.close();
                conn.close();
            }catch(SQLException se){
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(DeleteDepartment.this, "يجب أن تكون متصلاً بالإنترنت", Toast.LENGTH_SHORT);
                errorToast.show();
            }catch(Exception e){
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(DeleteDepartment.this, ""+e.getMessage(), Toast.LENGTH_SHORT);
                errorToast.show();
            }
        }

    public void back(View view) {
        startActivity(new Intent(DeleteDepartment.this,ManageContentActivity.class));
    }
}//End class