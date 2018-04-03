package com.shoor.shoor;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Doctors extends AppCompatActivity {
    ListView DoctorsList;
    public static ArrayList<Doctor> Doctors = new ArrayList<>();
    public String SpecialtyName;
    public String SpecialtyID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);

        SharedPreferences sharedpreferences = getSharedPreferences(Specialty.SpecialtyName, Context.MODE_PRIVATE);
        SpecialtyName = sharedpreferences.getString("SpecialtyName", "");

        SharedPreferences sharedpreferences2 = getSharedPreferences(Specialty.Specialty_ID, Context.MODE_PRIVATE);
        SpecialtyID = sharedpreferences2.getString("Specialty_ID", "");


        getAllDoctors();
        System.out.println("----------------------"+Doctors.size());
        DoctorsList = (ListView) findViewById(R.id.listofdoctors);
        DoctorListAdapter AdapterList = new DoctorListAdapter(getApplicationContext(), Doctors);
        DoctorsList.setAdapter(AdapterList);
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
            String sql  = "SELECT * FROM doctor where Specialties_ID='"+SpecialtyID+"' ORDER BY Doctor_ID ASC";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                String hosname="", PhoneNo="---";
                float lat=24 , lng=46;
                Statement stmt2 = conn.createStatement();
                String sql2  = "SELECT * FROM hospital where Hospital_ID="+rs.getInt("Hospital_ID");
                ResultSet rs2 = stmt2.executeQuery(sql2);
                String hos_ID = rs.getString("Hospital_ID");
                String Doc_id = rs.getString("Doctor_ID");
                String doc_name=rs.getString("DoctorName");
                float ratingscore = rs.getFloat("AvgRate");
                String officeHours = rs.getString("OfficeHours");
                double price = rs.getDouble("Price");
                while (rs2.next()) {
                    hosname =rs2.getString("HospitalName");
                    lat = rs2.getFloat("Location_V1");
                    lng = rs2.getFloat("Location_V2");
                    PhoneNo=rs2.getString("PhoneNumber");
                }
                Doctor doc  = new Doctor(Doc_id, doc_name, hos_ID ,hosname, lat , lng, PhoneNo ,ratingscore,officeHours, price);
                Doctors.add(doc);
                rs2.close();
                stmt2.close();
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



}
