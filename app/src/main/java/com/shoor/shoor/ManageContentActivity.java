package com.shoor.shoor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ManageContentActivity extends AppCompatActivity {

    private String redirectTo="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_content);
    }

    public void AddDoctor(View view) {
        startActivity(new Intent(ManageContentActivity.this, AddDoctor.class));
    }
    public void EditDoctor(View view) {
        startActivity(new Intent(ManageContentActivity.this, EditDoctor.class));
    }
    public void DeleteDoctor(View view) {
        startActivity(new Intent(ManageContentActivity.this, DeleteDoctor.class));
    }

    public void AddSpecialty(View view) {
        startActivity(new Intent(ManageContentActivity.this, AddDepartment.class));
    }
    public void EditSpecialty(View view) {
        startActivity(new Intent(ManageContentActivity.this, EditDepartment.class));
    }
    public void DeleteSpecialty(View view) {
        startActivity(new Intent(ManageContentActivity.this, DeleteDepartment.class));
    }

    public void AddHospital(View view) {
        startActivity(new Intent(ManageContentActivity.this, AddHospital.class));
    }
    public void EditHospital(View view) {
        startActivity(new Intent(ManageContentActivity.this, EditHospital.class));
    }
    public void DeleteHospital(View view) {
        startActivity(new Intent(ManageContentActivity.this, DeleteHospital.class));
    }


}
