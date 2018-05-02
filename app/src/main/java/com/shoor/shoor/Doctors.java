package com.shoor.shoor;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.StrictMode;
import android.print.PrintAttributes;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Doctors extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    RecyclerView DoctorsList;
    public DoctorListAdapter AdapterList;
    public static List<Doctor> Doctors = new ArrayList<>();
    public String SpecialtyName;
    public String SpecialtyID;
    public boolean isExpanded = false;
    public Button oneDollar;
    public Button twoDollar;
    public Button threeDollar;
    public String SearchDoc;
    public String sql;
    public static ProgressBar progressBar;
    public static LinearLayout progressLayout;



    // LogCat tag
    private static final String TAG = Doctors.class.getSimpleName();


    // Google client to interact with Google API
    private GoogleApiClient googleApiClient;

    private static final int PERMISSION_ACCESS_COARSE_LOCATION=1;



    // UI elements
    double latitude=0;
    double longitude=0;



    public static String Doc_Id;
    public static String Hos_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Specialty.progress.cancel();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);

        //VERY IMPORTANT LINES
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        progressLayout = (LinearLayout) findViewById(R.id.progress);

        SharedPreferences sharedpreferences = getSharedPreferences(Specialty.SpecialtyName, Context.MODE_PRIVATE);
        SpecialtyName = sharedpreferences.getString("SpecialtyName", "");

        TextView Specialtyname = (TextView) findViewById(R.id.SpecialtyName);
        Specialtyname.setText(SpecialtyName);

        SharedPreferences sharedpreferences2 = getSharedPreferences(Specialty.Specialty_ID, Context.MODE_PRIVATE);
        SpecialtyID = sharedpreferences2.getString("Specialty_ID", "");

        //get All Doctor "Normal state"
        sql = "SELECT * FROM doctor where Specialties_ID='" + SpecialtyID + "' ORDER BY Doctor_ID DESC";
        getAllDoctors();

        DoctorsList = (RecyclerView) findViewById(R.id.listofdoctors);
        AdapterList = new DoctorListAdapter( Doctors , getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        DoctorsList.setLayoutManager(mLayoutManager);
        DoctorsList.setAdapter(AdapterList);

        //filter buttons
        oneDollar = new Button(this);
        twoDollar = new Button(this);
        threeDollar = new Button(this);


        //check permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.ACCESS_COARSE_LOCATION }, PERMISSION_ACCESS_COARSE_LOCATION);
        }

        googleApiClient = new GoogleApiClient.Builder(this ,this, this).addApi(LocationServices.API).build();


        // Get the search value
        AutoCompleteTextView Doctitle = findViewById(R.id.search);
        Doctitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("") && s != null) {
                    sql = "SELECT * FROM doctor where Specialties_ID='" + SpecialtyID + "' AND DoctorName LIKE '" + s + "%' ORDER BY Doctor_ID ASC";
                    getAllDoctors();
                    if (Doctors.size() != 0) {
                        AdapterList.notifyDataSetChanged();
                    }
                    else {
                        onNewDataArrived(Doctors);
                        Toast.makeText(com.shoor.shoor.Doctors.this,"لا يوجد طبيب بهذا الاسم",Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





        //get Doctors Based on location
        Button Location = (Button) findViewById(R.id.location);
        Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (latitude!=0 && longitude!=0){
                    isNearest();

                    if (Doctors.size() != 0) {
                        AdapterList.notifyDataSetChanged();
                    }

                    else{
                        AdapterList.notifyDataSetChanged();
                        Toast error = Toast.makeText(Doctors.this, "لا يوجد أطباء قريبين من موقعك", Toast.LENGTH_SHORT);
                        error.show();
                    }


                }
                else{
                    Toast error = Toast.makeText(Doctors.this, "لم يتم التمكن من تحديد موقعك ، الرجاء تمكين تحديد الموقع ", Toast.LENGTH_SHORT);
                    error.show();
                }

            }
        });



    }


    //this method to connect with database to retrieve doctor info with certain query
    public void getAllDoctors(){
        Doctors.clear();
        try{
            //VERY IMPORTANT LINES' to build thread ploicy to allow jdbc to connect with the server'
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_Info.DB_URL,DB_Info.USER,DB_Info.PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                String hosname="", PhoneNo="---";
                float lat=24 , lng=46 , HosAvgRate=0;
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
                    HosAvgRate=rs2.getFloat("AvgRate");
                }
                Hospital hos = new Hospital(hos_ID,hosname,lat,lng,PhoneNo,HosAvgRate);
                Doctor doc  = new Doctor(Doc_id, doc_name, hos ,ratingscore,officeHours, price);
                Doctors.add(doc);
                rs2.close();
                stmt2.close();
            }
            rs.close();
            stmt.close();
            conn.close();

        }//end try
        catch (SQLException sqle){
            Toast error = Toast.makeText(this,"لا يمكن الاتصال بالخادم الآن، يرجى المحاولة في وقت لاحق",Toast.LENGTH_LONG);
            error.show();
        }
        catch (Exception e){
            Toast error = Toast.makeText(this,"لا يمكن الاتصال بالخادم الآن، يرجى المحاولة في وقت لاحق",Toast.LENGTH_LONG);
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
            threeDollar.setBackgroundResource(R.drawable.filter_left_style);

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




            oneDollar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sql  = "SELECT * FROM doctor where Specialties_ID='"+SpecialtyID+"' AND Price < 200 ORDER BY Price ASC ";

                    getAllDoctors();
                    if(Doctors.size()!=0){
                        AdapterList.notifyDataSetChanged();
                    }
                        else {
                        onNewDataArrived(Doctors);
                        Toast.makeText(getApplicationContext(),"لا يوجد أطباء بسعر رخيص",Toast.LENGTH_LONG).show();
                    }
                }
            });

            twoDollar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sql  = "SELECT * FROM doctor where Specialties_ID='"+SpecialtyID+"' AND Price >= 200 AND Price < 400  ORDER BY Price ASC ";
                    getAllDoctors();
                    if(Doctors.size()!=0){
                        AdapterList.notifyDataSetChanged();
                    }
                    else {
                        onNewDataArrived(Doctors);
                        Toast.makeText(getApplicationContext(),"لا يوجد أطباء بسعر متوسط",Toast.LENGTH_LONG).show();
                    }
                }
            });
            threeDollar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sql  = "SELECT * FROM doctor where Specialties_ID='"+SpecialtyID+"' AND Price >= 400 ORDER BY Price ASC ";
                    getAllDoctors();
                    if(Doctors.size()!=0){
                        AdapterList.notifyDataSetChanged();

                    }

                    else {
                        onNewDataArrived(Doctors);
                        Toast.makeText(getApplicationContext(),"لا يوجد أطباء بسعر غالي",Toast.LENGTH_LONG).show();
                    }
                }
            });

            //add them in the layout
            layout.addView(oneDollar);
            layout.addView(twoDollar);
            layout.addView(threeDollar);
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
            isExpanded=false;
        }
    }


    //check user permission to get the location
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                } else {

                    Toast.makeText(this, " نحتاج للوصل إلى موقعك", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    //connect to Google API Client
    @Override
    public void onConnectionSuspended(int arg0) {
        googleApiClient.connect();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }
    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Toast.makeText(this,"فشل الاتصال بخدمة المواقع",Toast.LENGTH_SHORT).show();;
    }

    //GoogleApiClient connection callback to get user location
    @Override
    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {

                latitude = lastLocation.getLatitude();
                longitude = lastLocation.getLongitude();
            }

        }
    }







    public void back(View view) {
        this.finish();
        startActivity(new Intent(com.shoor.shoor.Doctors.this, Specialty.class));
    }



    public void isNearest() {
      Doctors.clear();
        try {
            Class.forName("com.mysql.jdbc.Driver");

            Connection conn = DriverManager.getConnection(DB_Info.DB_URL, DB_Info.USER, DB_Info.PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM doctor where Specialties_ID='" + SpecialtyID + "' ORDER BY Doctor_ID DESC");
            while (rs.next()) {


                Statement stmt2 = conn.createStatement();
                String sql2 = "SELECT * FROM hospital where Hospital_ID=" + rs.getInt("Hospital_ID");
                ResultSet rs2 = stmt.executeQuery(sql2);
                double lat, lng;

                while (rs2.next()) {
                    lat = rs2.getFloat("Location_V1");
                    lng = rs2.getFloat("Location_V2");

                    double powerx = Math.pow(lat - latitude, 2);
                    double powery = Math.pow(lng - longitude, 2);
                    double distance = Math.sqrt(powerx + powery);
                    if (distance <= 0.0765) {
                        isNearest(rs2.getInt("Hospital_ID"));
                    }
                }


            }
            rs.close();
            stmt.close();
            conn.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public void isNearest(int HospitalID){
            try {
                Class.forName("com.mysql.jdbc.Driver");

                Connection conn = DriverManager.getConnection(DB_Info.DB_URL, DB_Info.USER, DB_Info.PASS);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM doctor where Specialties_ID='" + SpecialtyID + "' AND Hospital_ID='"+HospitalID+"' ");
                while(rs.next())
                {
                    String hosname="", PhoneNo="---";
                    float lat=24 , lng=46 , HosAngRate=0;
                    Statement stmt2 = conn.createStatement();
                    String sql2  = "SELECT * FROM hospital where Hospital_ID="+HospitalID;
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
                        HosAngRate=rs2.getFloat("AvgRate");
                    }
                    Hospital hospital = new Hospital(hos_ID,hosname,lat,lng,PhoneNo,HosAngRate);
                    Doctor doc  = new Doctor(Doc_id, doc_name, hospital ,ratingscore ,officeHours, price);
                    Doctors.add(doc);
                    rs2.close();
                    stmt2.close();
                }

                rs.close();
                stmt.close();
                conn.close();

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    public void Rating(View view) {
        sql = "SELECT * FROM doctor where Specialties_ID='" + SpecialtyID + "' ORDER BY AvgRate DESC";
        getAllDoctors();
        if(Doctors.size()!=0){
            onNewDataArrived(Doctors);
        }  else{
            onNewDataArrived(Doctors);
            Toast error = Toast.makeText(Doctors.this, "حدثت مشكلة حاول لاحقاً", Toast.LENGTH_SHORT);
            error.show();

        }
    }

    public void onNewDataArrived(List<Doctor> newdata) {
        List<Doctor> oldData = AdapterList.getItemes();
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtilCallback(oldData, newdata));
        AdapterList.setItemes(newdata);
        result.dispatchUpdatesTo( AdapterList);

    }


    public void NoFilter(View view) {
        //get All Doctor "Normal state"
        sql = "SELECT * FROM doctor where Specialties_ID='" + SpecialtyID + "' ORDER BY Doctor_ID DESC";
        getAllDoctors();
        AdapterList.notifyDataSetChanged();
    }
}