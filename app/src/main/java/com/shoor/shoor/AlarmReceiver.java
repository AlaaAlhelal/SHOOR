package com.shoor.shoor;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class AlarmReceiver extends BroadcastReceiver {

    String Message="";
    @Override
    public void onReceive(Context context, Intent intent) {

    if(AppointmentToday(context)) {
        Intent resultIntent = new Intent(context, Specialty.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(Specialty.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
            .setContentTitle("شـــور - مواعيد")
            .setContentText(Message)
            .setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setContentIntent(resultPendingIntent)
            .setSmallIcon(R.drawable.ic_date);


    NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    assert mNotificationManager != null;
    mNotificationManager.notify(0, mBuilder.build());
        }

    }


    public  boolean  AppointmentToday(Context context){
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

            String User_ID = SaveLogin.getUserID(context);
            //STEP 4: Execute a query
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM appointment where User_ID="+User_ID;
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set
            int count=0;
            while(rs.next())
            {

                Date date = rs.getDate("Date");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date today = new Date();
                String date_str = dateFormat.format(date);
                String today_str = dateFormat.format(today);
                System.out.println("Today is "+today_str+"  The date in DB is "+date);
                String note = rs.getString("Note");
                Time time = rs.getTime("Time");
                if(date_str.equals(today_str)) {
                    Message = " لديك  " + note + " اليوم في الساعة " + time + " \n";
                    count++;
                }
            }
            if(count>=1)
                return true;



            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){ System.out.println(se.getMessage());}
        catch(Exception e){ System.out.println(e.getMessage()); }

        return false;
    }
}