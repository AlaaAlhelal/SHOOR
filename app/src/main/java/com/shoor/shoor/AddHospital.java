package com.shoor.shoor;


import android.content.Intent;
import android.os.StrictMode;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import android.support.v4.app.FragmentActivity;
import android.util.Log;


import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class AddHospital  extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {
    EditText HospitalName_input;
    EditText HospitalNumber_input;
    public Marker  marker;
    public GoogleMap Maps;
    public int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    public PlaceAutocompleteFragment placeAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hospital);
        HospitalName_input = (EditText) findViewById(R.id.Hospital_Name);
        HospitalNumber_input = (EditText) findViewById(R.id.Hospital_Number);


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

                /////////////////
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

    }

    public void Send (View view) {
        String HospitalName = HospitalName_input.getText().toString();
        String HospitalNumber = HospitalNumber_input.getText().toString();

        boolean isValid = ValidateInputs(HospitalName, HospitalNumber);
        if (isValid) {
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
                conn = DriverManager.getConnection(DB_Info.DB_URL,DB_Info.USER,DB_Info.PASS);

                //STEP 4: Execute a query
                stmt = conn.createStatement();
                String sql;
                sql = "INSERT INTO hospital (HospitalName, Location_V1, Location_V2, PhoneNumber, AvgRate) Values('" + HospitalName + "', "+ getLocation_V1() +" , "+ getLocation_V2()+" , '" + HospitalNumber +"' , 5.00) ";
                int rs = stmt.executeUpdate(sql);
                if(rs==1){
                    Toast done = Toast.makeText(AddHospital.this, "تمت الإضافة", Toast.LENGTH_SHORT);
                    done.show();
                    HospitalName_input.setText("");
                    HospitalNumber_input.setText("");
                }
                else {
                    Toast done = Toast.makeText(AddHospital.this, "حدثت مشكلة أثناء الاضافة", Toast.LENGTH_SHORT);
                    done.show();
                }

                stmt.close();
                conn.close();
            }catch(SQLException se){
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(AddHospital.this, "يجب أن تكزن متصلاً بالانترنت"+se.getMessage(), Toast.LENGTH_SHORT);
                errorToast.show();
            }catch(Exception e){
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(AddHospital.this, ""+e.getMessage(), Toast.LENGTH_SHORT);
                errorToast.show();
            }
        }//if
    }//Send
    public boolean ValidateInputs(String HospitalName , String HospitalNumber){

        if (HospitalName.equals("")) {
            HospitalName_input.setError("يجب ملء الخانة");
            return false;
        }
        if (HospitalNumber.equals("")){
            HospitalNumber_input.setError("يجب ملء الخانة");
            return false;
        }
        if(HospitalName.length()>20){
            HospitalName_input.setError("يجب ألا يتجاوز اسم المستشفى 20 حرفاً");
            return false;
        }
        if(HospitalNumber.length()>10){
            HospitalNumber_input.setError("يجب ألا يتجاوز رقم الهاتف 10 رقماً");
            return false;
        }
        // cheeck if number or not


        return true;
    }
    public void back(View view) {
        startActivity(new Intent(AddHospital.this, ManageContentActivity.class));

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
        LatLng sydney = new LatLng(24.717, 46.620);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        marker = googleMap.addMarker(new MarkerOptions().position(sydney).draggable(true));
        marker.setDraggable(true);
        googleMap.setOnMarkerDragListener(this);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i("", "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public double getLocation_V1(){
       return marker.getPosition().latitude;
    }
    public double getLocation_V2(){
        return marker.getPosition().longitude;
    }
}


