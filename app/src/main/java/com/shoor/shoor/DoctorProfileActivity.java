package com.shoor.shoor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class DoctorProfileActivity extends FragmentActivity implements OnMapReadyCallback , ListFragment.OnFragmentInteractionListener {
    public Marker marker;
    public GoogleMap Maps;
    public static ArrayList<Review> Doctorreviews = new ArrayList<>();
    public static ArrayList<Review> Hospitalreviews = new ArrayList<>();
    RecyclerView.LayoutManager dLayoutManager;
    RecyclerView.Adapter dAdapter;
    RecyclerView.LayoutManager hLayoutManager;
    RecyclerView.Adapter hAdapter;
    public String   userID, Doctor_ID ,DoctorName ,HospitalName , SpecialtyName;
    public static String Hospital_ID;
    public float Lat , Lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        //Doctors.progressdialog.cancel();
        //VERY IMPORTANT LINES
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences sharedpreferences2 = getSharedPreferences(DoctorListAdapter.Doc_Id, Context.MODE_PRIVATE);
        Doctor_ID = sharedpreferences2.getString("Doctor_ID", "");

        SharedPreferences sharedPreferences = getSharedPreferences(Specialty.SpecialtyName,Context.MODE_PRIVATE);
        SpecialtyName = sharedPreferences.getString("SpecialtyName","");
        userID = SaveLogin.getUserID(getApplicationContext());

        //retrieve  doctor info
        DoctorInfo();
        //set map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapLocation);
        mapFragment.getMapAsync(this);



        //check if this doctor in the favorite list or not
        boolean In = InTheFavoriteList();
        if(In)
        {
            ImageButton fav = ((ImageButton)findViewById(R.id.favorite));
            fav.setImageResource(R.drawable.ic_favorite_fill);
            fav.setClickable(false);
        }
        //retrieve doctor reviews and hospital reviews
        DoctorReviews();

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

        //retrieve doctor reviews and hospital reviews
        HospitalReviews();
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


            // Calling the RecyclerView of doctor and hospital reviews
            RecyclerView hRecyclerView = (RecyclerView) findViewById(R.id.HospitalReviewsHorizontal);
            hRecyclerView.setHasFixedSize(true);

            // The number of Columns
            hLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            hRecyclerView.setLayoutManager(hLayoutManager);

            //set adapter
            hAdapter = new HospitalReviewAdapter(DoctorProfileActivity.this, Hospitalreviews);
            hRecyclerView.setAdapter(hAdapter);

        }

    }




    public void Share(View view) {
        String message ="أنصحك بالطبيب "+DoctorName+" في "+HospitalName+" يمكنك معرفة المزيد من المعلومات على تطبيق شور" ;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(share, "مشاركة مع "));
    }




    public void Favorite(View view) {
        ArrayList<String> ListsName = new ArrayList<String>();
        final ArrayList<String> ListsID = new ArrayList<String>();
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_Info.DB_URL,DB_Info.USER,DB_Info.PASS);
            Statement stmt = conn.createStatement();
            String sql  = "Select * From List where User_ID="+userID;
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

        Fragment fragment = ListFragment.newInstance("","" , ListsName ,ListsID, userID, Doctor_ID);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.DoctorProfile, fragment).addToBackStack(null).commit();

    }//end


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
            String sql  = "SELECT * FROM hospitalreview where Hospital_ID="+Hospital_ID;
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
                String pic = rs.getString("HospitalPicture");


                Review HospitalReview = new Review(user_name, comments, ratingscore, pic);
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
        List<Doctor> DocList;

        if(Doctors.Doctors.size()==0)
            DocList=FavListDocActivity.Doctors;
            else
            DocList = Doctors.Doctors;

        int size = DocList.size();
        for (int i = 0 ; i<size ;i++){
            String id =DocList.get(i).getDoctor_ID();
            if(id.equals(Doctor_ID)){
                DoctorName =DocList.get(i).getDoctorName();
                Hospital_ID=DocList.get(i).getHospital_ID();
                ((TextView)findViewById(R.id.DoctorName)).setText(DoctorName);
                ((TextView)findViewById(R.id.DoctorDescription)).setText("طبيب "+SpecialtyName);
                HospitalName = DocList.get(i).getHospitalName();
                ((TextView)findViewById(R.id.Hospital)).setText(HospitalName);
                if(DocList.get(i).getOfficeHours()!=null ||DocList.get(i).getOfficeHours()!= null)
                ((TextView)findViewById(R.id.WorkingHours)).setText(DocList.get(i).getOfficeHours());
                ((TextView)findViewById(R.id.PhoneNo)).setText("رقم المستشفى: "+DocList.get(i).getPhoneNo());
                ((RatingBar)findViewById(R.id.AvgRate)).setRating(DocList.get(i).getAvgRate());
                ((RatingBar)findViewById(R.id.HosAvgRate)).setRating(DocList.get(i).getHoAngRate());


                double price = DocList.get(i).getPrice();
                if(price>=200 && price <400)
                    ((ImageView)findViewById(R.id.Price)).setImageResource(R.drawable.pricerange2);
                else
                 if(price>=400 )
                     ((ImageView)findViewById(R.id.Price)).setImageResource(R.drawable.pricerange3);

                Lat = DocList.get(i).getLocationLat();
                Lng = DocList.get(i).getLocationLng();
                break;
            }
        }
    }



    public void GoToMoreDoctorReviews(View view) {
        startActivity(new Intent(DoctorProfileActivity.this,DoctorReviews.class));
        this.finish();
    }


    public void GoToMoreHospitalReviews(View view) {

        startActivity(new Intent(DoctorProfileActivity.this,HospitalReviews.class));
        this.finish();

    }

    public void AddReview(View view) {
        startActivity(new Intent(DoctorProfileActivity.this, AddDoctorReviewActivity.class));
        this.finish();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public boolean  InTheFavoriteList(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_Info.DB_URL,DB_Info.USER,DB_Info.PASS);
            Statement stmt = conn.createStatement();
            String sql  = "Select * From listofdoctors where User_ID='"+userID+"' AND Doctor_ID='"+Doctor_ID+"' ";
            ResultSet rs = stmt.executeQuery(sql);
            int count=0;

            while (rs.next()){
                count++;
            }

            if(count!=0)
                return true;

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

        return false;
    }

    public void AddHospitalReview(View view) {
        this.finish();
        startActivity(new Intent(DoctorProfileActivity.this,AddHospitalReviewActivity.class));
    }

    public void AddDoctorReview(View view) {
        this.finish();
        startActivity(new Intent(DoctorProfileActivity.this,AddDoctorReviewActivity.class));


    }
}
