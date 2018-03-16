package com.shoor.shoor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class EditUserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
    }

    public void Gender_Clicked(View view) {
    }

    public void Do(View view) {
    }

    public void back(View view) {
        startActivity(new Intent(EditUserProfileActivity.this,ProfileActivity.class));
    }
}
