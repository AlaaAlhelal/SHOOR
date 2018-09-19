package com.shoor.shoor;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
        this.finish();
        startActivity(new Intent(ProfileActivity.this, SuggestDoctorActivity.class));

    }

    public void linkToSpecialty(View view) {
        this.finish();
        startActivity(new Intent(ProfileActivity.this, Specialties.class));

    }

    public void linkToFavorite(View view) {
        this.finish();
        startActivity(new Intent(ProfileActivity.this,FavoriteList.class));

    }

    public void linkToMyprofile(View view) {
//user in this page
    }

    public void EditProfile(View view) {
        this.finish();
        startActivity(new Intent(ProfileActivity.this,EditUserProfileActivity.class));
    }

    public void LinkToAppoint(View view) {
        this.finish();
        startActivity(new Intent(ProfileActivity.this,MyAppointmentActivity.class));

    }

    public void logout(View view) {
    //get session
    SharedPreferences sharedpreferences = getSharedPreferences(SignIn.user_id, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor= sharedpreferences.edit();
    editor.remove("user_id");
    editor.apply();
    //clear user id
    SaveLogin.setUserID(getApplicationContext(),"");
    this.finish();
    startActivity(new Intent(ProfileActivity.this,SignIn.class));

    }

    public void DisplayInfo(){

        //VERY IMPORTANT LINES
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String userID =  SaveLogin.getUserID(getApplicationContext());

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_Info.DB_URL, DB_Info.USER, DB_Info.PASS);
System.out.println("--------------------------------------------"+userID);
            Statement stmt2 = conn.createStatement();
            String sql = "SELECT * FROM user where User_ID=" + userID ;
            ResultSet result = stmt2.executeQuery(sql);
            int count = 0;
            String user_id = null;
            while (result.next()) {
                ((TextView)findViewById(R.id.username)).setText( result.getString("UserName"));
                ((TextView)findViewById(R.id.EmailLabel)).setText( result.getString("UserEmail"));
                String Gender = result.getString("Gender");
                if(Gender.equals("M"))
                    ((TextView)findViewById(R.id.genderLabel)).setText("ذكر");

                else
                    ((TextView)findViewById(R.id.genderLabel)).setText("أنثى");

                ++count;
            }
            if(count<1){

                Toast.makeText(this,"تأكد من اتصالك بالانترنت",Toast.LENGTH_SHORT).show();
            }
        }catch (ClassNotFoundException e) {
            Toast.makeText(this,"تأكد من اتصالك بالانترنت",Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            Toast.makeText(this,"تأكد من اتصالك بالانترنت",Toast.LENGTH_SHORT).show();
        }


    }
}
