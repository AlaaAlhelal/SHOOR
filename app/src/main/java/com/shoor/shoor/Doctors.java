package com.shoor.shoor;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.StrictMode;
import android.print.PrintAttributes;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
    public boolean isExpanded=false;
    public Button oneDollar ;
    public Button twoDollar;
    public Button threeDollar;
    public Button fourDollar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);

        oneDollar= new Button(this);
        twoDollar= new Button(this);
        threeDollar= new Button(this);
        fourDollar= new Button(this);

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


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void Expand(View view) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.price_layout);
        Button price = (Button) view;


        if(!isExpanded) {
            LinearLayout.LayoutParams vp = (LinearLayout.LayoutParams) price.getLayoutParams();
            vp.setMargins(0,15,0,0);
            price.setLayoutParams(vp);

            //set and change background
            view.setBackgroundResource(R.drawable.filter_right_style);
            oneDollar.setBackgroundResource(R.drawable.filter_center_style);
            twoDollar.setBackgroundResource(R.drawable.filter_center_style);
            threeDollar.setBackgroundResource(R.drawable.filter_center_style);
            fourDollar.setBackgroundResource(R.drawable.filter_left_style);

            LinearLayout.LayoutParams p =new LinearLayout.LayoutParams(60, 90);
            p.setMargins(0,30,5,0);
            oneDollar.setLayoutParams(p);
            oneDollar.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.dollar),null,null,null);


            LinearLayout.LayoutParams p2 =new LinearLayout.LayoutParams(90, 90);
            p2.setMargins(0,30,5,0);
            twoDollar.setLayoutParams(p2);
            twoDollar.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.dollar),null,getDrawable(R.drawable.dollar),null);

            LinearLayout.LayoutParams p3 =new LinearLayout.LayoutParams(120, 90);
            p3.setMargins(0,30,5,0);
            threeDollar.setLayoutParams(p3);
            threeDollar.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.threedollars),null,null,null);

            LinearLayout.LayoutParams p4 =new LinearLayout.LayoutParams(150, 90);
            p4.setMargins(0,30,5,0);
            fourDollar.setLayoutParams(p4);
            fourDollar.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.fourdollars),null,null,null);


            oneDollar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
              //here the filter code
                }
            });

            twoDollar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //here the filter code
                }
            });
            threeDollar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //here the filter code
                }
            });
            fourDollar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //here the filter code
                }
            });




            //add them in the layout
            layout.addView(oneDollar);
            layout.addView(twoDollar);
            layout.addView(threeDollar);
            layout.addView(fourDollar);
            isExpanded=true;
        }
        //Conscall
        else {
            LinearLayout.LayoutParams vp = (LinearLayout.LayoutParams) price.getLayoutParams();
            vp.setMargins(0,30,0,0);
            price.setLayoutParams(vp);
            layout.removeView(oneDollar);
            layout.removeView(twoDollar);
            layout.removeView(threeDollar);
            layout.removeView(fourDollar);
            isExpanded=false;
        }


    }
}
