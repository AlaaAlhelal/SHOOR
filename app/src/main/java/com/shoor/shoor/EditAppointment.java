package com.shoor.shoor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class EditAppointment extends AppCompatActivity {
    public String userID;
    public Spinner Applist;
    public List<String> List_of_Appoint;
    public ArrayAdapter<String> adapter;
    public EditText Note ;
    public DatePicker DatePickerByUser;
    public TimePicker Time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_appointment);
        Time = findViewById(R.id.timePicker);
        Note = findViewById(R.id.notes);
        DatePickerByUser = findViewById(R.id.datePicker);
        //get session
        SharedPreferences sharedpreferences = getSharedPreferences(SignIn.user_id, Context.MODE_PRIVATE);
        userID = sharedpreferences.getString("user_id", "");
        Applist = (Spinner) findViewById(R.id.AppList);
        ListAppointment();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, List_of_Appoint);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Applist.setAdapter(adapter);
        Retrive();


        Applist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                Retrive();
            }

            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

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
                Toast done = Toast.makeText(EditAppointment.this, "حدثت مشكلة أثناء استرجاع بياناتك حاول مجدداً", Toast.LENGTH_SHORT);
                done.show();
            }

            //STEP 6: Clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(EditAppointment.this, "يجب أن تكون متصلاً بالإنترنت", Toast.LENGTH_SHORT);
            errorToast.show();
        } catch (Exception e) {
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(EditAppointment.this, "" + e.getMessage(), Toast.LENGTH_SHORT);
            errorToast.show();
        }
        ///////////////////end




    }//end method List

    public void EditAppoint(View view) {
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

if(isvalid) {
    String appointment = Applist.getSelectedItem().toString();
    int pos = appointment.indexOf(" ");
    String date_srt = appointment.substring(0, pos);
    String Time_str = appointment.substring(pos + 1);
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
        sql = "Update appointment SET Date='" + dateselected + "', Time='" + time + "', Note='"+note_str+"' WHERE User_ID=" + userID + " AND Date='" + date_srt + "' AND Time='" + Time_str + "' ";
        int rs = stmt.executeUpdate(sql);

        if (rs == 1) {
            Toast done = Toast.makeText(EditAppointment.this, "تم  الحذف ", Toast.LENGTH_LONG);
            done.show();
            adapter.remove(appointment);
            Applist.setAdapter(adapter);
            //  this.recreate();
        } else {
            Toast done = Toast.makeText(EditAppointment.this, "حدثت مشكلة أثناء الحذف", Toast.LENGTH_LONG);
            done.show();
        }
        //STEP 6: Clean-up environment
        stmt.close();
        conn.close();
    } catch (SQLException se) {
        //SHOW SERVER FAILED MESSAGE
        Toast errorToast = Toast.makeText(EditAppointment.this, "" + se.getMessage(), Toast.LENGTH_LONG);
        errorToast.show();
    } catch (Exception e) {
        //SHOW SERVER FAILED MESSAGE
        Toast errorToast = Toast.makeText(EditAppointment.this, "" + e.getMessage(), Toast.LENGTH_LONG);
        errorToast.show();
    }

}//end if
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
        startActivity(new Intent(EditAppointment.this,MyAppointmentActivity.class));

    }

    public void Retrive(){
        String selectedApp = Applist.getSelectedItem().toString();
        int pos = selectedApp.indexOf(" ");
        String date_srt = selectedApp.substring(0, pos);
        String Time_str = selectedApp.substring(pos + 1);
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
            sql = "SELECT * FROM appointment WHERE User_ID="+userID+" And Date='"+date_srt+"' And Time='"+Time_str+"'";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()){
                Note.setText(rs.getString("Note"));
            }

            if(rs.wasNull())
            {
                Toast done = Toast.makeText(EditAppointment.this, "حدثت مشكلة أثناء استرجاع بياناتك حاول مجدداً", Toast.LENGTH_SHORT);
                done.show();
            }

            //STEP 6: Clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(EditAppointment.this, "يجب أن تكون متصلاً بالإنترنت", Toast.LENGTH_SHORT);
            errorToast.show();
        } catch (Exception e) {
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(EditAppointment.this, "" + e.getMessage(), Toast.LENGTH_SHORT);
            errorToast.show();
        }
        ///////////////////end
    }
}
