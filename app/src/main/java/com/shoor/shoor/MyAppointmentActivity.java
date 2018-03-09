package com.shoor.shoor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MyAppointmentActivity extends AppCompatActivity {
    public String userID;
    public CompactCalendarView UserCalendarView ;
    public int CurrentMonth;
    public int CurrentYear;
    public TextView MonthLable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointment);
       UserCalendarView = (CompactCalendarView) findViewById(R.id.UserCalendar);
       UserCalendarView.setFirstDayOfWeek(Calendar.SUNDAY);
       Date today = new Date();
       CurrentMonth=today.getMonth()+1;
       String yearformat = today.getYear()+"";
       CurrentYear=Integer.parseInt(yearformat.substring(1))+2000;
       setMonth(CurrentMonth);

        //get session
        SharedPreferences sharedpreferences = getSharedPreferences(SignIn.user_id, Context.MODE_PRIVATE);
        userID = sharedpreferences.getString("user_id", "");
        //Display Appointments
        displayAppointments();

        //when user click day
        UserCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = UserCalendarView.getEvents(dateClicked);
                String eventsNotes ="";

                for(int i=0 ; i< events.size();i++)
                    eventsNotes+=events.get(i).getData()+"\n_____________________\n";

                ((TextView)findViewById(R.id.note)).setText(eventsNotes);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Log.d("", "Month was scrolled to: " + firstDayOfNewMonth);
            }
        });


        ///////////////////////////////////
        //Add
        final FloatingActionButton addAppoint = new FloatingActionButton(getBaseContext());
        addAppoint.setColorNormal(Color.parseColor("#016CE7"));
        addAppoint.setImageResource(R.drawable.ic_add);
        addAppoint.setPadding(0,0,0,5);
        addAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  addAppoint.setVisibility(addAppoint.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                startActivity(new Intent(MyAppointmentActivity.this,AddAppointmentActivity.class));

            }
        });
        //Edit
        final FloatingActionButton EditAppoint = new FloatingActionButton(getBaseContext());
        EditAppoint.setColorNormal(Color.parseColor("#F2AC1F"));
        EditAppoint.setImageResource(R.drawable.ic_edit);
        EditAppoint.setPadding(0,0,0,5);
        EditAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // EditAppoint.setVisibility(EditAppoint.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                startActivity(new Intent(MyAppointmentActivity.this,AddAppointmentActivity.class));

            }
        });
        //delete
        final FloatingActionButton delAppoint = new FloatingActionButton(getBaseContext());
        delAppoint.setColorNormal(Color.parseColor("#DE3F3F"));
        delAppoint.setImageResource(R.drawable.ic_delete);
        delAppoint.setPadding(0,0,0,5);
        delAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyAppointmentActivity.this,DeleteAppointmentActivity.class));            }
        });


        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.ManageAppoint);
        menuMultipleActions.addButton(delAppoint);
        menuMultipleActions.addButton(EditAppoint);
        menuMultipleActions.addButton(addAppoint);
        ///////////////////////////////////

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
                String time = String.format(rs.getTime("Time").toString(), "HH:mm a");

                String note = rs.getString("Note");
                Event event = new Event(Color.BLUE ,date.getTime(), "الوقت: "+time+"\n"+note);
                UserCalendarView.addEvent(event);

            }
            //STEP 6: Clean-up environment
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(MyAppointmentActivity.this, "يجب أن تكون متصلاً بالانترنت", Toast.LENGTH_SHORT);
            errorToast.show();
        }catch(Exception e){
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(MyAppointmentActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT);
            errorToast.show();
        }


    }

public void setMonth(int monthNum){
    TextView month = (TextView) findViewById(R.id.MonthName);
    switch(monthNum){
        case 1: month.setText("Jan-"+CurrentYear);   break;
        case 2: month.setText("Feb-"+CurrentYear);   break;
        case 3: month.setText("Mar-"+CurrentYear);   break;
        case 4: month.setText("Apr-"+CurrentYear);   break;
        case 5: month.setText("May-"+CurrentYear);   break;
        case 6: month.setText("Jun-"+CurrentYear);   break;
        case 7: month.setText("Jul-"+CurrentYear);   break;
        case 8: month.setText("Aug-"+CurrentYear);   break;
        case 9: month.setText("Sep-"+CurrentYear);   break;
        case 10: month.setText("Oct-"+CurrentYear);   break;
        case 11: month.setText("Nov-"+CurrentYear);   break;
        case 12: month.setText("Dec-"+CurrentYear);   break;
    }

}

    public void back(View view) {
        startActivity(new Intent(MyAppointmentActivity.this,ProfileActivity.class));

    }

    public void NextMonth(View view) {
    if(CurrentMonth==12) {
        CurrentYear+=1;
        setMonth(1);
        CurrentMonth=1;
    }
    else
        setMonth(++CurrentMonth);
    }

    public void PreMonth(View view) {
    if(CurrentMonth==1) {
        CurrentYear-=1;
        setMonth(12);
        CurrentMonth=12;
    }
    else
        setMonth(--CurrentMonth);
    }


    public void Menu(View view) {

    }
}
