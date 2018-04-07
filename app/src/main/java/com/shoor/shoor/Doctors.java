package com.shoor.shoor;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Doctors extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    ListView DoctorsList;
    public static ArrayList<Doctor> Doctors = new ArrayList<>();
    public String SpecialtyName;
    public String SpecialtyID;
    public boolean isExpanded=false;
    public Button oneDollar ;
    public Button twoDollar;
    public Button threeDollar;
    public Button fourDollar ;
    public String SearchDoc;
    public String sql ;

    // LogCat tag
    private static final String TAG = Doctors.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    // UI elements
    double latitude ;
    double longitude ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);
        sql  = "SELECT * FROM doctor where Specialties_ID='"+SpecialtyID+"' ORDER BY Doctor_ID ASC";

        // First we need to check availability of play services
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
        }

        oneDollar= new Button(this);
        twoDollar= new Button(this);
        threeDollar= new Button(this);
        fourDollar= new Button(this);

        SharedPreferences sharedpreferences = getSharedPreferences(Specialty.SpecialtyName, Context.MODE_PRIVATE);
        SpecialtyName = sharedpreferences.getString("SpecialtyName", "");

        SharedPreferences sharedpreferences2 = getSharedPreferences(Specialty.Specialty_ID, Context.MODE_PRIVATE);
        SpecialtyID = sharedpreferences2.getString("Specialty_ID", "");

       // Get the search value
        AutoCompleteTextView Doctitle  = findViewById(R.id.search);
        SearchDoc= Doctitle+"";
        if (! SearchDoc.equals(null))
        {
            sql  = "SELECT * FROM doctor where Specialties_ID='"+SpecialtyID+"' AND DoctorName='"+SearchDoc+"' ORDER BY Doctor_ID ASC";
        }

        Button Location = (Button)findViewById(R.id.location);
        Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayLocation();

                sql = " SELECT * FROM doctor WHERE Specialties_ID='"+SpecialtyID+"' LEFT JOIN hospital ON doctor.Hospital_ID = hospital.Hospital_ID WHERE " + "Location_V1 >=('"+latitude+"' * .9) " + "AND Location_V2 <=('"+longitude+"' * 1.1) ORDER BY abs(latitude - '"+latitude+"') + abs(longitude - '"+longitude+"') limit 20 ";

            }});

        Button rate = (Button)findViewById(R.id.rate);
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sql  = "SELECT * FROM doctor where Specialties_ID='"+SpecialtyID+"' ORDER BY AvgRate DESC";
            }});

        getAllDoctors();
        System.out.println("----------------------"+Doctors.size());
        DoctorsList = (ListView) findViewById(R.id.listofdoctors);
        DoctorListAdapter AdapterList = new DoctorListAdapter(getApplicationContext(), Doctors);
        DoctorsList.setAdapter(AdapterList);
    }

    private void displayLocation() {

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
             latitude = mLastLocation.getLatitude();
             longitude = mLastLocation.getLongitude();
        } else {
            Toast error = Toast.makeText(Doctors.this, "لم يتم التمكن من تحديد موقعك ، الرجاء تمكين تحديد الموقع ", Toast.LENGTH_SHORT);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
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
                    sql  = "SELECT * FROM doctor where Specialties_ID='"+SpecialtyID+"' AND Price < 200 ORDER BY Price ASC ";
                }
            });

            twoDollar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //here the filter code
                    sql  = "SELECT * FROM doctor where Specialties_ID='"+SpecialtyID+"' AND Price >= 200 AND Price < 400  ORDER BY Price ASC ";
                }
            });
            threeDollar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //here the filter code
                    sql  = "SELECT * FROM doctor where Specialties_ID='"+SpecialtyID+"' AND Price >= 400 ORDER BY Price ASC ";
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

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {
        // Once connected with google api, get the location
        displayLocation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }
}