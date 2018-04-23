package com.shoor.shoor;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.Calendar;
import java.util.Date;

public class Specialty extends AppCompatActivity {


    public static String SpecialtyName="";
    public static String Specialty_ID;
    public static ProgressDialog  progress;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialty);

        //check wifi connection
        isConnected();


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
        progress=new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(50);
        progress.setMessage("يرجى الانتظار...");
        progress.show();

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
        this.finish();
        startActivity(new Intent(Specialty.this,FavoriteList.class));

    }

    public void linkToMyprofile(View view) {
        this.finish();
        startActivity(new Intent(Specialty.this,ProfileActivity.class));
    }

    public void linkToSpecialty(View view) {
        //redirect to suggest activity
        this.finish();
        startActivity(new Intent(Specialty.this, Specialty.class));
    }

    public void linkToSuggest(View view) {
        //redirect to suggest activity
        this.finish();
        startActivity(new Intent(Specialty.this, SuggestDoctorActivity.class));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing the user from back to previous activity
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean isConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiInfo != null && wifiInfo.isConnected()) || (mobileInfo != null && mobileInfo.isConnected())) {
            return true;
        } else {
            showDialog();
            return false;
        }
    }

    private void showDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("أنت غير متصل بالانترنت هل تريد الاتصال بالانترنت أو إغلاق التطبيق؟")
                .setCancelable(false)
                .setPositiveButton("الاتصال", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("إغلاق", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
