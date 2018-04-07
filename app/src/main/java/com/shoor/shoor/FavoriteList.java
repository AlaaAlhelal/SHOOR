package com.shoor.shoor;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sun.mail.imap.protocol.ID;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class FavoriteList extends AppCompatActivity {

    public ArrayList<String> Names = new ArrayList<String>();
    public ArrayList<String> IDs = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        getAllLists();
        if(Names.size()!=0){
        ListView lists =(ListView) findViewById(R.id.FavoriteLists);
        FavoriteListAdapter AdapterList = new FavoriteListAdapter(getApplicationContext(), Names, IDs);
        lists.setAdapter(AdapterList); }
        else
        {
            TextView message = new TextView(this);
            message.setText("لا يوجد لديك قوائم");
            LinearLayout view = (LinearLayout) findViewById(R.id.messagefav);
            view.addView(message);
        }

    }


    public void getAllLists(){
        try{

            //VERY IMPORTANT LINES
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_Info.DB_URL,DB_Info.USER,DB_Info.PASS);
            Statement stmt = conn.createStatement();
            String sqlq ="SELECT * From list Where User_ID="+SaveLogin.getUserID(getApplicationContext());
            ResultSet rs = stmt.executeQuery(sqlq);
            while (rs.next()){
                Names.add(rs.getString("ListName"));
                IDs.add(rs.getString("List_ID"));
            }

            stmt.close();
            conn.close();

        }//end try
        catch (SQLException sqle){
            Toast error = Toast.makeText(this,sqle.getMessage(),Toast.LENGTH_LONG);
            error.show();
        }
        catch (Exception e){
            Toast error = Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG);
            error.show();
        }
    }

    public void linkToFavorite(View view) {
        this.finish();
        startActivity(new Intent(FavoriteList.this,FavoriteList.class));

    }

    public void linkToMyprofile(View view) {
        this.finish();

        startActivity(new Intent(FavoriteList.this,ProfileActivity.class));
    }

    public void linkToSpecialty(View view) {
        this.finish();
        startActivity(new Intent(FavoriteList.this, Specialty.class));
    }

    public void linkToSuggest(View view) {
        this.finish();
        startActivity(new Intent(FavoriteList.this, SuggestDoctorActivity.class));
    }

}
