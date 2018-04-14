package com.shoor.shoor;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.util.TimeFormatException;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class DeleteAppointmentActivity extends AppCompatActivity  {
public String userID;
public Spinner Applist;
public  List<String> List_of_Appoint;
public  ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_appointment);
        //get session
        userID =SaveLogin.getUserID(getApplicationContext());
        Applist = (Spinner) findViewById(R.id.AppList);
        ListAppointment();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, List_of_Appoint);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Applist.setAdapter(adapter);

    }

    public void  ListAppointment(){

        List_of_Appoint  = new ArrayList<String>();

        ////////////////////retrive all appointments
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
            //            sql = "DELETE FROM appointment WHERE User_ID="+userID;

            sql = "Select * FROM appointment WHERE User_ID="+userID;
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()){
                String Date = String.format(rs.getDate("Date").toString(), "dd-MM-yyyy");
                String Time = String.format(rs.getTime("Time").toString(), "HH:mm a");
                String appInfo=Date+" "+Time;
                List_of_Appoint.add(appInfo);
            }

            if(rs.wasNull())
            {
                Toast done = Toast.makeText(DeleteAppointmentActivity.this, "حدثت مشكلة أثناء استرجاع بياناتك حاول مجدداً", Toast.LENGTH_SHORT);
                done.show();
            }

            //STEP 6: Clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(DeleteAppointmentActivity.this, "يجب أن تكون متصلاً بالإنترنت", Toast.LENGTH_SHORT);
            errorToast.show();
        } catch (Exception e) {
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(DeleteAppointmentActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT);
            errorToast.show();
        }
        ///////////////////end




    }//end method List


    public void back(View view) {
        startActivity(new Intent(DeleteAppointmentActivity.this,MyAppointmentActivity.class));
        this.finish();

    }

    public void Delete(View view)  {
        AlertDialog aDialog = new AlertDialog.Builder(this).setMessage("هل أنت متأكد من حذف الموعد؟").setTitle("")
                .setNeutralButton("نعم", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,
                                        final int which) {

                        String appointment = Applist.getSelectedItem().toString();
                        int pos =  appointment.indexOf(" ");
                        String date_srt = appointment.substring(0, pos);
                        String Time_str = appointment.substring(pos + 1);
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
                            sql = "DELETE FROM appointment WHERE User_ID="+userID+" AND Date='"+date_srt+"' AND Time='"+Time_str+"' ";
                            int rs = stmt.executeUpdate(sql);

                            if(rs==1){
                                Toast done = Toast.makeText(DeleteAppointmentActivity.this, "تم  الحذف ", Toast.LENGTH_SHORT);
                                done.show();
                                adapter.remove(appointment);
                                Applist.setAdapter(adapter);
                                DeleteAppointmentActivity.this.finish();
                                startActivity(new Intent(DeleteAppointmentActivity.this,MyAppointmentActivity.class));
                            }
                            else
                            {
                                Toast done = Toast.makeText(DeleteAppointmentActivity.this, "حدثت مشكلة أثناء الحذف", Toast.LENGTH_SHORT);
                                done.show();
                            }
                            //STEP 6: Clean-up environment
                            stmt.close();
                            conn.close();
                        }catch(SQLException se){
                            //SHOW SERVER FAILED MESSAGE
                            Toast errorToast = Toast.makeText(DeleteAppointmentActivity.this, "يجب أن تكون متصلاً بالانترنت", Toast.LENGTH_SHORT);
                            errorToast.show();
                        }catch(Exception e){
                            //SHOW SERVER FAILED MESSAGE
                            Toast errorToast = Toast.makeText(DeleteAppointmentActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT);
                            errorToast.show();
                        }

                    }

                    }).setNegativeButton("إلغاء",new DialogInterface.OnClickListener(){
                    public void onClick(final DialogInterface dialog,  final int which) {
                        // User cancelled the action
                    }
                    }).create();

        aDialog.show();




    }//end method delete


}
