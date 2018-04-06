package com.shoor.shoor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.Calendar;
import java.util.Date;

public class Specialty extends AppCompatActivity {


    public static String SpecialtyName="";
    public static String Specialty_ID;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialty);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 15);
        System.out.println(cal.getTime());

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);



    }

    public void dentist_doctors(View view) {
        setSpecialtyClicked("أسنان","20");
    }

    public void Internal_Medicine_doctors(View view) {
        setSpecialtyClicked("باطنية", "22");
    }

    public void Otolaryngology_doctor(View view) {
        setSpecialtyClicked("أنف وأذن وحنجرة","21");
    }

    public void Dermatology_doctor(View view) {
        setSpecialtyClicked("جلدية","18");
    }

    public void Pediatrics_doctors(View view) {
        setSpecialtyClicked("أطفال","19");
    }

    public void setSpecialtyClicked(String sp ,String id) {
        SpecialtyName =sp;
        Specialty_ID=  id;
        //set session SpecialtyClicked
        SharedPreferences sharedpreferences = getSharedPreferences(SpecialtyName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("SpecialtyName",SpecialtyName);
        editor.apply();

        //set session SpecialtyClicked
        SharedPreferences sharedpreferences2 = getSharedPreferences(Specialty_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedpreferences2.edit();
        editor2.putString("Specialty_ID",Specialty_ID);
        editor2.apply();
        //redirect to doctors
        startActivity(new Intent(Specialty.this, Doctors.class));

    }

    public void linkToFavorite(View view) {
        startActivity(new Intent(Specialty.this,FavoriteList.class));

    }

    public void linkToMyprofile(View view) {
        startActivity(new Intent(Specialty.this,ProfileActivity.class));
    }

    public void linkToSpecialty(View view) {
        //redirect to suggest activity
        startActivity(new Intent(Specialty.this, Specialty.class));
    }

    public void linkToSuggest(View view) {
        //redirect to suggest activity
        startActivity(new Intent(Specialty.this, SuggestDoctorActivity.class));
    }
}
