package com.shoor.shoor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

public class Specialty extends AppCompatActivity {

    private TextView mTextMessage;
    private String SpecialtyClicked="";
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.linktosuggest:
                    //redirect to suggest activity
                    return true;
                case R.id.linktospecialty:
                    //redirect to specialty activity
                    return true;
                case R.id.linktofavorite:
                    //redirect to favorite activity
                    return true;
                case R.id.linktomyprofile:
                    //redirect to myprofile
                    // activity
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialty);
    }

    public void dentist_doctors(View view) {
        setSpecialtyClicked("dentist");
    }

    public void Internal_Medicine_doctors(View view) {
        setSpecialtyClicked("Internal_Medicine");
    }

    public void Otolaryngology_doctor(View view) {
        setSpecialtyClicked("Otolaryngology");
    }

    public void Dermatology_doctor(View view) {
        setSpecialtyClicked("Dermatology");
    }

    public void Pediatrics_doctors(View view) {
        setSpecialtyClicked("Pediatrics");
    }

    public void setSpecialtyClicked(String sp) {
        SpecialtyClicked =sp;
        //set session SpecialtyClicked
        SharedPreferences sharedpreferences = getSharedPreferences(SpecialtyClicked, Context.MODE_PRIVATE);
        //redirect to doctors
        startActivity(new Intent(Specialty.this, Doctors.class));

    }
}
