package com.shoor.shoor;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //display user info
        DisplayInfo();
    }

    public void linkToSuggest(View view) {
        startActivity(new Intent(ProfileActivity.this, SuggestDoctorActivity.class));

    }

    public void linkToSpecialty(View view) {
        startActivity(new Intent(ProfileActivity.this, Specialty.class));

    }

    public void linkToFavorite(View view) {
        startActivity(new Intent(ProfileActivity.this,FavoriteList.class));

    }

    public void linkToMyprofile(View view) {
        startActivity(new Intent(ProfileActivity.this,ProfileActivity.class));

    }

    public void EditProfile(View view) {
        startActivity(new Intent(ProfileActivity.this,EditUserProfileActivity.class));
    }

    public void LinkToAppoint(View view) {
        startActivity(new Intent(ProfileActivity.this,MyAppointmentActivity.class));

    }

    public void logout(View view) {
    //get session
    SharedPreferences sharedpreferences = getSharedPreferences(SignIn.user_id, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor= sharedpreferences.edit();
    editor.remove("user_id");
    editor.commit();
    startActivity(new Intent(ProfileActivity.this,SignIn.class));

    }

    public void DisplayInfo(){
        //get session
        SharedPreferences sharedpreferences = getSharedPreferences(SignIn.user_id, Context.MODE_PRIVATE);
        String userID = sharedpreferences.getString("user_id", "");
        try{

            Class.forName("com.mysql.jdbc.Driver");

            Connection conn = DriverManager.getConnection(DB_Info.DB_URL,DB_Info.USER,DB_Info.PASS);

            Statement stmt = conn.createStatement();
            String sql  = "SELECT * FROM user where User_ID="+userID;
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next())
            {
                ((TextView)findViewById(R.id.username)).setText(rs.getString("UserName"));
                ((TextView)findViewById(R.id.EmailLabel)).setText(rs.getString("UserEmail"));
                String genderType =rs.getString("Gender");
                if(genderType.equals("M"))
                ((TextView)findViewById(R.id.genderLabel)).setText("ذكر");
                else
                if(genderType.equals("F"))
                    ((TextView)findViewById(R.id.genderLabel)).setText("أنثى");

            }

        }//end try
            catch (SQLException sqle){  Log.e("",sqle.getMessage());}
            catch (Exception e){ Log.e("",e.getMessage());}
    }
}
