package com.shoor.shoor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddHospitalReviewActivity extends AppCompatActivity {
    public RatingBar hos_rate;
    public EditText Comment;
    public byte[] pic =null;
    public String Hospital_ID;
    public static final int GET_FROM_GALLERY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hospital_review);
        SharedPreferences sharedpreferences = getSharedPreferences(DoctorListAdapter.Hos_ID, Context.MODE_PRIVATE);
        Hospital_ID = sharedpreferences.getString("Hospital_ID", "");

        hos_rate = (RatingBar)findViewById(R.id.rating_hospital);
        Comment =(EditText)findViewById(R.id.comment);

    }




    public void SendReview(View view) {
        double Rate=0 , userRate = hos_rate.getRating();
        int count=0;
        String comment =Comment.getText().toString();
        if(isValid()){

        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_Info.DB_URL,DB_Info.USER,DB_Info.PASS);
            Statement stmt = conn.createStatement();
            String sql  = "Select * From hospitalreview where Hospital_ID="+Hospital_ID;
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()){
                count++;
                Rate+=rs.getDouble("HospitalRate");
            }
            rs.close();
            stmt.close();

        //set new Review
        String user_ID = SaveLogin.getUserID(getApplicationContext());
        Statement stm = conn.createStatement();
            String sqlq="";
        if(pic!=null) {
            sqlq = "INSERT into hospitalreview (HospitalComment , HospitalRate , HospitalPicture , Hospital_ID , User_ID) Values ( '" + comment + "' , '" + userRate + "' , '" + pic + "' , '" + Hospital_ID + "' , '" + user_ID + "' )";
        }else {
            sqlq = "INSERT into hospitalreview (HospitalComment , HospitalRate , Hospital_ID , User_ID) Values ( '" + comment + "' , '" + userRate + "' ,  '" + Hospital_ID + "' , '" + user_ID + "' )";

        }
        int queryresult = stm.executeUpdate(sqlq);
        if(queryresult==1){
            stm.close();
            //update doctor Rate
            double newAvgRate=(Rate+userRate)/(count+1);
            Statement statement = conn.createStatement();
            String query = "Update Hospital SET AvgRate=('"+newAvgRate+"')  Where Hospital_ID="+Hospital_ID;
            int result =statement.executeUpdate(query);
            Toast error = Toast.makeText(this,"تمت إضافة تعليقك",Toast.LENGTH_LONG);
            error.show();
            startActivity(new Intent(AddHospitalReviewActivity.this, DoctorProfileActivity.class));
            this.finish();

        }
        else
        {
            Toast error = Toast.makeText(this,"حدثت مشكلة حاول لاحقاً",Toast.LENGTH_LONG);
            error.show();
        }

        conn.close();

    }//end try
    catch (SQLException sqle){
        Toast error = Toast.makeText(this,sqle.getMessage(),Toast.LENGTH_LONG);
        error.show();
    }
    catch (Exception e){Toast error = Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG);
        error.show();
            }
        }
    }

    public void AddPic(View view) {
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                pic = getBytes(bitmap);
        } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }





    public boolean isValid(){
        if(Comment.getText().length()==0){
            Comment.setError("يجب ملء الخانة");
            return false;
        }
        else
        if(hos_rate.getRating()==0.0 ){
            Toast error =  Toast.makeText(this,"يجب أن يكون التقيم أكثر من 0 نجمة",Toast.LENGTH_LONG);
            error.show();
            return false;
        }


     return  true;
    }

    public void back(View view) {
        this.finish();
        startActivity(new Intent(AddHospitalReviewActivity.this, DoctorProfileActivity.class));

    }
}
