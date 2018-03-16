package com.shoor.shoor;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Date;

public class AddAppointmentActivity extends AppCompatActivity {
    public String userID="";
    public DatePicker DatePickerByUser;
    public TimePicker Time;
    public EditText Note ;
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        Time = findViewById(R.id.timePicker);
        Note = findViewById(R.id.notes);
        DatePickerByUser = findViewById(R.id.datePicker);

        //get session
        SharedPreferences sharedpreferences = getSharedPreferences(SignIn.user_id, Context.MODE_PRIVATE);
        userID = sharedpreferences.getString("user_id", "");
    }


    public void AddAppoint(View view) {
        int day = DatePickerByUser.getDayOfMonth();
        int month = DatePickerByUser.getMonth();
        int year = DatePickerByUser.getYear()-1900;
        Date dateselected = new Date(year,month,day);
        int hour =Time.getCurrentHour();
        int min = Time.getCurrentMinute();
        String note_str = Note.getText().toString();
        java.sql.Time time = new Time(hour,min,00);

        //validate inputs
        boolean isvalid=validation(dateselected,note_str);
    if(isvalid)
    {
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
            sql = "INSERT INTO appointment ( Date, Time , Note , User_ID ) VALUES ( '"+dateselected+"' , '"+time+"' , '"+note_str+"' , "+userID+") ";
            int rs = stmt.executeUpdate(sql);

            if(rs==1) {
            Note.setText("");
                Toast errorToast = Toast.makeText(AddAppointmentActivity.this, "تمت الإضافة", Toast.LENGTH_LONG);
                errorToast.show();
            }
            //STEP 6: Clean-up environment
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(AddAppointmentActivity.this, ""+se.getMessage(), Toast.LENGTH_LONG);
            errorToast.show();
        }catch(Exception e){
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(AddAppointmentActivity.this, ""+e.getMessage(), Toast.LENGTH_LONG);
            errorToast.show();
        }


    }


    }

    public boolean validation(Date date, String note){
        java.util.Date t =new java.util.Date();
        Date today = new Date(t.getYear(),t.getMonth(),t.getDate());

        if(today.compareTo(date)>0){
            Toast errorToast=   Toast.makeText(getApplicationContext(),"يجب أن يكون التاريخ أكبر من اليوم",Toast.LENGTH_LONG);
            errorToast.show();
            return false;
        }
        if(note.equals("")){
            Note.setError("يجب ملئ الخانة");
            return false;
        }
        return true;
    }

    public void back(View view) {
        startActivity(new Intent(AddAppointmentActivity.this,MyAppointmentActivity.class));

    }
}
