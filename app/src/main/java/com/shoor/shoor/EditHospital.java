package com.shoor.shoor;


import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class EditHospital extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener{
    EditText HospitalName_input;
    EditText HospitalNumber_input;
    ArrayList<Hospital> Hospitals = new ArrayList<>();
    ArrayList<String> hospital = new ArrayList<String>() ;
    Spinner hospitallist;
    public Marker marker;
    public GoogleMap Maps;
    double lat=0 , lng=0;
    public int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    public PlaceAutocompleteFragment placeAutoComplete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hospital);
        HospitalName_input = (EditText) findViewById(R.id.HospitalName);
        HospitalNumber_input = (EditText) findViewById(R.id.HospitalNum);
        hospitallist=(Spinner) findViewById(R.id.hospital_list);

        //for hospital
        RetriveData_hospital();
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hospital);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hospitallist.setAdapter(adapter3);

        placeAutoComplete = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete);


        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            Log.d("Maps", "An error occurred: " + e.getMessage());
        }

        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                marker.remove();
                MarkerOptions markerOptions = new MarkerOptions().position(place.getLatLng()).draggable(true);
                marker = Maps.addMarker(markerOptions);
                Log.d("Maps", "Place selected: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                Log.d("Maps", "An error occurred: " + status);
            }

        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        hospitallist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SetRetrivedInfo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker markerEnd) {
        LatLng position=markerEnd.getPosition();

        // Remove Old Marker
        marker.remove();
        // Add New marker position
        MarkerOptions markerOptions =  new MarkerOptions().position(position).draggable(true);
        markerOptions.draggable(true);
        marker = Maps.addMarker(markerOptions);
        marker.showInfoWindow();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Maps = googleMap;
        LatLng riyadh = new LatLng(24.717, 46.620);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(riyadh));
        marker = googleMap.addMarker(new MarkerOptions().position(riyadh).draggable(true));
        marker.setDraggable(true);
        googleMap.setOnMarkerDragListener(this);

        //retrivied data
        SetRetrivedInfo();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i("", "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i("", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }



    public void Send (View view) {
        String HospitalName = HospitalName_input.getText().toString();
        String HospitalNumber = HospitalNumber_input.getText().toString();

        boolean isValid = ValidateInputs(HospitalName, HospitalNumber);
        if (isValid) {

            int index = hospitallist.getSelectedItemPosition();

            //SETUP CONNECTION
            Connection conn = null;
            Statement stmt = null;

            try {
                //STEP 2: Register JDBC driver
                Class.forName("com.mysql.jdbc.Driver");

                //STEP 3: Open a connection
                conn = DriverManager.getConnection(DB_Info.DB_URL,DB_Info.USER,DB_Info.PASS);

                //STEP 4: Execute a query
                stmt = conn.createStatement();
                String sql;
                sql = "Update hospital SET HospitalName=('"+HospitalName+"') , Location_V1=('"+getLocation_V1()+"'), Location_V2=('"+getLocation_V2()+"'), PhoneNumber=('"+HospitalNumber+"') WHERE Hospital_ID=('"+Hospitals.get(index).getHospitalID()+"') ";
                int rs = stmt.executeUpdate(sql);
                if(rs==1){
                    Toast done = Toast.makeText(EditHospital.this, "تمت التعديل", Toast.LENGTH_SHORT);
                    done.show();
                    HospitalName_input.setText("");
                    HospitalNumber_input.setText("");
                }
                else {
                    Toast done = Toast.makeText(EditHospital.this, "حدثت مشكلة أثناء التعديل", Toast.LENGTH_SHORT);
                    done.show();
                }

                stmt.close();
                conn.close();
            }catch(SQLException se){
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(EditHospital.this, "يجب أن تكون متصلاً بالانترنت"+se.getMessage(), Toast.LENGTH_SHORT);
                errorToast.show();
            }catch(Exception e){
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(EditHospital.this, ""+e.getMessage(), Toast.LENGTH_SHORT);
                errorToast.show();
            }
        }//if
    }//Send
    public boolean ValidateInputs(String HospitalName , String HospitalNumber){
        String MobilePattern = "[0-9]{10}";
        Pattern p = Pattern.compile(MobilePattern);

        String pattrenAr = "[\\u0600-\\u06FF]+";
        Pattern pHosName = Pattern.compile(pattrenAr);

        //validate all inputs
        if (!pHosName.matcher(HospitalName).matches() ) {
            HospitalName_input.setError("يجب إدخال أحرف عربية فقط");
            return false;
        }
        if (HospitalName.equals("")) {
            HospitalName_input.setError("يجب ملء الخانة");
            return false;
        }
        if (HospitalNumber.equals("")){
            HospitalNumber_input.setError("يجب ملء الخانة");
            return false;
        }
        if(HospitalName.length()>30){
            HospitalName_input.setError("يجب ألا يتجاوز اسم المستشفى 30 حرفاً");
            return false;
        }
        if(HospitalNumber.length()>10){
            HospitalNumber_input.setError("يجب ألا يتجاوز رقم الهاتف 10 رقماً");
            return false;
        }
        // cheeck if number or not
        if(!p.matcher(HospitalNumber).matches()){
            HospitalNumber_input.setError("يجب إدخال أرقام فقط");
            return false;
        }
        if(getLocation_V1()==24.717 || getLocation_V2()==46.620)
        {
            Toast.makeText(this,"يجب اختيار الموقع",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void RetriveData_hospital() {

        //VERY IMPORTANT LINES
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //SETUP CONNECTION
        Connection conn = null;
        Statement stmt = null;
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            conn = DriverManager.getConnection(DB_Info.DB_URL, DB_Info.USER, DB_Info.PASS);

            //STEP 4: Execute a query
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM hospital";
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set
            while (rs.next()) {
                String name= rs.getString("HospitalName");
                Hospital hospitalInfo = new Hospital(rs.getString("Hospital_ID"),name ,rs.getFloat("Location_V1"),rs.getFloat("Location_V2"),rs.getString("PhoneNumber"),0);
                Hospitals.add(hospitalInfo);
                hospital.add(name);
            }
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(EditHospital.this, "يجب أن تكون متصلاً بالإنترنت", Toast.LENGTH_SHORT);
            errorToast.show();
        } catch (Exception e) {
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(EditHospital.this, "" + e.getMessage(), Toast.LENGTH_SHORT);
            errorToast.show();
        }
    }

    public void SetRetrivedInfo(){
        int index = hospitallist.getSelectedItemPosition();
        LatLng hospos = new LatLng(Hospitals.get(index).getLocationLat(),Hospitals.get(index).getLocationLng());
        marker.setPosition(hospos);
        HospitalName_input.setText(Hospitals.get(index).getHospitalName());
        HospitalNumber_input.setText(Hospitals.get(index).getPhoneNo());

    }

    public void back(View view) {
        this.finish();
        startActivity(new Intent(EditHospital.this, ManageContentActivity.class));

    }

    public double getLocation_V1(){
        return marker.getPosition().latitude;
    }
    public double getLocation_V2(){
        return marker.getPosition().longitude;
    }
}
