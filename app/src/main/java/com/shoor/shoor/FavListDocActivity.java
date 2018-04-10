package com.shoor.shoor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class FavListDocActivity extends AppCompatActivity {
    ListView DoctorsList;
    public static ArrayList<Doctor> Doctors = new ArrayList<>();
    public String SpecialtyID , sql ,List_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_list_doc);

        SharedPreferences sharedpreferences2 = getSharedPreferences(FavoriteListAdapter.List_Name, Context.MODE_PRIVATE);
        String ListName = sharedpreferences2.getString("List_Name", "");
        TextView title = ((TextView) findViewById(R.id.title));
        title.setText(ListName);

        SharedPreferences sharedpreferences  = getSharedPreferences(FavoriteListAdapter.List_Id, Context.MODE_PRIVATE);
        List_ID = sharedpreferences.getString("List_ID", "");
        sql = "SELECT * FROM  listofdoctors WHERE List_ID='" + List_ID + "' AND User_ID="+SaveLogin.getUserID(getApplicationContext());
        getAllDoctors();
        System.out.println("----------------------" + Doctors.size());
        if(Doctors.size()!=0){
        DoctorsList = (ListView) findViewById(R.id.listofdoctors);
        DoctorListAdapter AdapterList = new DoctorListAdapter(getApplicationContext(), Doctors);
        DoctorsList.setAdapter(AdapterList);
        }
        else
        {
            TextView message = new TextView(this);
            message.setText("لا يوجد لديك أطباء في هذه القائمة");
            message.setPadding(10,10,10,10);
            ((LinearLayout) findViewById(R.id.Message)).addView(message);
        }
    }

    public void getAllDoctors(){
        Doctors.clear();
        try{
            //VERY IMPORTANT LINES
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_Info.DB_URL,DB_Info.USER,DB_Info.PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String hosname = "", PhoneNo = "---";
                float lat = 24, lng = 46;

                Statement stmt3 = conn.createStatement();
                ResultSet rs3 = stmt3.executeQuery("SELECT * FROM doctor where Doctor_ID=" + rs.getString("Doctor_ID"));
                while (rs3.next()) {
                    Statement stmt2 = conn.createStatement();
                    String sql2 = "SELECT * FROM hospital where Hospital_ID=" + rs3.getInt("Hospital_ID");
                    ResultSet rs2 = stmt2.executeQuery(sql2);
                    String hos_ID = rs3.getString("Hospital_ID");
                    String Doc_id = rs3.getString("Doctor_ID");
                    String doc_name = rs3.getString("DoctorName");
                    float ratingscore = rs3.getFloat("AvgRate");
                    String officeHours = rs3.getString("OfficeHours");
                    double price = rs3.getDouble("Price");
                    while (rs2.next()) {
                        hosname = rs2.getString("HospitalName");
                        lat = rs2.getFloat("Location_V1");
                        lng = rs2.getFloat("Location_V2");
                        PhoneNo = rs2.getString("PhoneNumber");
                    }
                    Doctor doc = new Doctor(Doc_id, doc_name, hos_ID, hosname, lat, lng, PhoneNo, ratingscore, officeHours, price);
                    Doctors.add(doc);
                    rs2.close();
                    stmt2.close();
                }
                rs3.close();
                stmt3.close();
            }
            rs.close();
            stmt.close();
            conn.close();

        }//end try
        catch (SQLException sqle){
            Log.e("",sqle.getMessage());
            Toast error = Toast.makeText(this,sqle.getMessage(),Toast.LENGTH_LONG);
            error.show();
        }
        catch (Exception e){
            Log.e("",e.getMessage());
            Toast error = Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG);
            error.show();}
    }

    public void back(View view) {
        this.finish();
        startActivity(new Intent(FavListDocActivity.this , FavoriteList.class));
    }
}
