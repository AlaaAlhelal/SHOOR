package com.shoor.shoor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.EventDay;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.applandeo.materialcalendarview.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyAppointmentActivity extends AppCompatActivity {
    public String userID;
    private long date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointment);
        //get session
        SharedPreferences sharedpreferences = getSharedPreferences(SignIn.user_id, Context.MODE_PRIVATE);
        userID = sharedpreferences.getString("user_id", "");
        //Display Appointments
    }
    public void displayAppointments(){

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
            sql = "SELECT * FROM appointment WHERE User_ID="+userID;
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()){
                Date date = rs.getDate("Date");
                String time = rs.getString("Time");
                String note = rs.getString("Note");
                CalendarView userCalendar = (CalendarView)findViewById(R.id.UserCalendar);

                /*Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                List<EventDay> mEventDays = new ArrayList<>();
                MyEventDay myEventDay = new  MyEventDay(cal, 0 , "Time:"+time+"\n"+note);
                mEventDays.add(myEventDay);
               userCalendar.setEvents();
               */

                List<EventDay> events = new ArrayList<>();

                Calendar calendar = Calendar.getInstance();
                events.add(new EventDay(calendar, R.drawable.ic_event));

                CalendarView calendarView = (CalendarView) findViewById(R.id.UserCalendar);
                //calendarView.setEvents(events);

            }
            //STEP 6: Clean-up environment
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(MyAppointmentActivity.this, "يجب أن تكون متصلاً بالإنترنت", Toast.LENGTH_SHORT);
            errorToast.show();
        }catch(Exception e){
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(MyAppointmentActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT);
            errorToast.show();
        }


    }



}
