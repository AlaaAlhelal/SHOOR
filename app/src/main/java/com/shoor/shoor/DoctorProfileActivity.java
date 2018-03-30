package com.shoor.shoor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class DoctorProfileActivity extends AppCompatActivity implements OnMapReadyCallback {
    public Marker marker;
    public GoogleMap Maps;
    public static ArrayList<Review> Doctorreviews = new ArrayList<>();
    public static ArrayList<Review> Hospitalreviews = new ArrayList<>();
    RecyclerView.LayoutManager dLayoutManager;
    RecyclerView.Adapter dAdapter;
    RecyclerView.LayoutManager hLayoutManager;
    RecyclerView.Adapter hAdapter;
    public String DoctorName , DoctorDescription , userID, Doctor_ID;
    public float AvgRate , Lat ,Lng;
    public double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        SharedPreferences sharedpreferences = getSharedPreferences(SignIn.user_id, Context.MODE_PRIVATE);
         userID = sharedpreferences.getString("user_id", "");

        SharedPreferences sharedpreferences2 = getSharedPreferences(DoctorListAdapter.Doc_Id, Context.MODE_PRIVATE);
        Doctor_ID = sharedpreferences2.getString("Doctor_ID", "");

        //reterive  doctor info
        DoctorInfo();
        //set map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapLocation);
        mapFragment.getMapAsync(this);

        if(Doctorreviews.size()==0)
        {
            LinearLayout ScrollContent = (LinearLayout)findViewById(R.id.ScrollContent);
            TextView unavailable = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            unavailable.setLayoutParams(params);
            unavailable.setText(" لا توجد تقييمات لهذا الطبيب");
            ScrollContent.addView(unavailable,4);
            ((TextView)findViewById(R.id.DoctorReviewsMore)).setVisibility(View.INVISIBLE);

        }
        else {
            //retrieve doctor reviews and hospital reviews
            DoctorReviews();

            // Calling the RecyclerView of doctor and hospital reviews
            RecyclerView dRecyclerView = (RecyclerView) findViewById(R.id.DoctorReviewsHorizontal);
            dRecyclerView.setHasFixedSize(true);

            // The number of Columns
            dLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            dRecyclerView.setLayoutManager(dLayoutManager);

            //set adapter
            dAdapter = new HLVAdapter(DoctorProfileActivity.this, Doctorreviews);
            dRecyclerView.setAdapter(dAdapter);
        }

        if(Hospitalreviews.size()==0){
            LinearLayout ScrollContent = (LinearLayout)findViewById(R.id.ScrollContent);
            TextView unavailable = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            unavailable.setLayoutParams(params);
            unavailable.setText("لا توجد تقييمات لهذا المستشفى");
            ((TextView)findViewById(R.id.HospitalReviewsMore)).setVisibility(View.INVISIBLE);
            ScrollContent.addView(unavailable);
        }else {
            //retrieve doctor reviews and hospital reviews
            HospitalReviews();

            // Calling the RecyclerView of doctor and hospital reviews
            RecyclerView hRecyclerView = (RecyclerView) findViewById(R.id.HospitalReviewsHorizontal);
            hRecyclerView.setHasFixedSize(true);

            // The number of Columns
            hLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            hRecyclerView.setLayoutManager(hLayoutManager);

            //set adapter
            hAdapter = new HLVAdapter(DoctorProfileActivity.this, Hospitalreviews);
            hRecyclerView.setAdapter(hAdapter);

        }

    }




    public void Share(View view) {
        String message ="محتوى الرساله"+DoctorName;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(share, "Title "));
    }

    public void Favorite(View view) {
        ArrayList<String> ListsName = new ArrayList<String>();
        final ArrayList<String> ListsID = new ArrayList<String>();
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_Info.DB_URL,DB_Info.USER,DB_Info.PASS);
            Statement stmt = conn.createStatement();
            String sql  = "Select From List where User_ID="+userID;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                ListsName.add(rs.getString("ListName"));
                ListsID.add(rs.getString("List_ID"));
            }

            rs.close();
            stmt.close();
            conn.close();

        }//end try
        catch (SQLException sqle){
            Toast error = Toast.makeText(this,sqle.getMessage(),Toast.LENGTH_LONG);
            error.show();
        }
        catch (Exception e){Toast error = Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG);
            error.show();
        }

        final CharSequence[] Items = ListsName.toArray(new CharSequence[ListsName.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("إضافة إلى..");
        builder.setItems(Items , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = DriverManager.getConnection(DB_Info.DB_URL,DB_Info.USER,DB_Info.PASS);
                    Statement stmt = conn.createStatement();
                    String sql  = "Insert into List (List_ID, Doctor_ID , User_ID) Values('"+ListsID.get(which)+"', '"+Doctor_ID+"' , '"+userID+"')";
                    int rs = stmt.executeUpdate(sql);
                    stmt.close();
                    conn.close();
                }//end try
                catch (SQLException sqle){
                    Toast error = Toast.makeText(getApplicationContext() ,sqle.getMessage() ,Toast.LENGTH_LONG);
                    error.show();
                } catch (ClassNotFoundException e) {
                    Toast error = Toast.makeText(getApplicationContext() ,e.getMessage() ,Toast.LENGTH_LONG);
                    error.show();
                }
            }
        });
        builder.setPositiveButton("إضافة قائمة جديدة" ,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setTitle("أدخل اسم القائمة");
                final EditText view = new EditText(getApplicationContext());
                builder.setView(view);
                builder.setPositiveButton("حفظ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(view.getText().length()!=0){
                        try{
                            Class.forName("com.mysql.jdbc.Driver");
                            Connection conn = DriverManager.getConnection(DB_Info.DB_URL,DB_Info.USER,DB_Info.PASS);
                            Statement stmt = conn.createStatement();
                            String sql  = "Insert into List (ListName , User_ID) Values('"+view.getText()+"' , '"+userID+"')";
                            int rs = stmt.executeUpdate(sql);
                            stmt.close();
                            conn.close();
                        }//end try
                        catch (SQLException sqle){
                            Toast error = Toast.makeText(getApplicationContext() ,sqle.getMessage() ,Toast.LENGTH_LONG);
                            error.show();
                        } catch (ClassNotFoundException e) {
                            Toast error = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
                            error.show();
                        }   }
                        else {
                            view.setError("يجب ملء الخانة");
                        }
                    }
                });
                builder.show();
            }
        });
        builder.show();



    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Maps = googleMap;
        LatLng sydney = new LatLng(Lat, Lng);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        marker = googleMap.addMarker(new MarkerOptions().position(sydney).draggable(false));
        marker.setDraggable(false);
    }



    public void DoctorReviews(){
        Doctorreviews.clear();
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_Info.DB_URL,DB_Info.USER,DB_Info.PASS);
            Statement stmt = conn.createStatement();
            String sql  = "SELECT * FROM doctorreview where Doctor_ID="+Doctor_ID;
            ResultSet rs = stmt.executeQuery(sql);
            int size = 0;
            while(rs.next()&& size<4)
            {
                String user_name="";
                Statement stmt2 = conn.createStatement();
                String sql2  = "SELECT * FROM User where User_ID="+rs.getInt("User_ID");
                ResultSet rs2 = stmt2.executeQuery(sql2);
                String comments=rs.getString("DoctorComment");
                while (rs2.next()) {
                     user_name = rs2.getString("UserName");
                }
                float ratingscore = rs.getFloat("DoctorRate");
                Review docrorReview = new Review(user_name, comments, ratingscore);
                Doctorreviews.add(docrorReview);

                size++;
                rs2.close();
                stmt2.close();
            }
            rs.close();
            stmt.close();
            conn.close();

        }//end try
        catch (SQLException sqle){
            Toast error = Toast.makeText(this,sqle.getMessage(),Toast.LENGTH_LONG);
        error.show();
        }
        catch (Exception e){Toast error = Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG);
            error.show();}
    }


    public void HospitalReviews(){
        Hospitalreviews.clear();
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_Info.DB_URL,DB_Info.USER,DB_Info.PASS);
            Statement stmt = conn.createStatement();
            String sql  = "SELECT * FROM hospitalreview where Hospital_ID=4";
            ResultSet rs = stmt.executeQuery(sql);
            int size=0;
            while(rs.next()&& size<4)
            {
                String user_name="";
                Statement stmt2 = conn.createStatement();
                String sql2  = "SELECT * FROM User where User_ID="+rs.getInt("User_ID");
                ResultSet rs2 = stmt2.executeQuery(sql2);
                String comments=rs.getString("HospitalComment");
                while (rs2.next()) {
                    user_name = rs2.getString("UserName");
                }
                float ratingscore = rs.getFloat("HospitalRate");
                Review HospitalReview = new Review(user_name, comments, ratingscore);
                Hospitalreviews.add(HospitalReview);
                size++;
                rs2.close();
                stmt2.close();
            }
            rs.close();
            stmt.close();
            conn.close();

        }//end try
        catch (SQLException sqle){
            Toast error = Toast.makeText(this,sqle.getMessage(),Toast.LENGTH_LONG);
            error.show();
        }
        catch (Exception e){Toast error = Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG);
            error.show();}
    }


    public void DoctorInfo(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_Info.DB_URL,DB_Info.USER,DB_Info.PASS);
            Statement stmt = conn.createStatement();
            String sql  = "SELECT * FROM Doctor where Doctor_ID="+Doctor_ID;
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                DoctorName = rs.getString("Doctor_Name");
      //          DoctorDescription = rs.getString("");
                AvgRate = rs.getFloat("AvgRate");
                price =rs.getDouble("Price");
                Lat =rs.getFloat("LocationV1");
                Lng =rs.getFloat("LocationV2");
            }
            rs.close();
            stmt.close();
            conn.close();

        }//end try
        catch (SQLException sqle){
            Toast error = Toast.makeText(this,sqle.getMessage(),Toast.LENGTH_LONG);
            error.show();
        }
        catch (Exception e){Toast error = Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG);
            error.show();}
    }



    public void GoToMoreDoctorReviews(View view) {
        startActivity(new Intent(DoctorProfileActivity.this,DoctorReviews.class));
    }


    public void GoToMoreHospitalReviews(View view) {
        startActivity(new Intent(DoctorProfileActivity.this,HospitalReviews.class));

    }
}
