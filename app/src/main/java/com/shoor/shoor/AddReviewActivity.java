package com.shoor.shoor;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class AddReviewActivity extends AppCompatActivity {

    public RatingBar doc_rate;
    public RatingBar hos_rate;
    public RatingBar price_rate;
    public EditText Comment;
    public Byte pic;
    public String Doctor_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        doc_rate =(RatingBar) findViewById(R.id.rating_doctor);
        hos_rate=(RatingBar) findViewById(R.id.rating_hospital);
        price_rate=(RatingBar) findViewById(R.id.rating_price);
        Comment =(EditText)findViewById(R.id.comment);

        SharedPreferences sharedpreferences2 = getSharedPreferences(DoctorListAdapter.Doc_Id, Context.MODE_PRIVATE);
        Doctor_ID = sharedpreferences2.getString("Doctor_ID", "");

    }

    public void back(View view) {
    }

    public void SendComment(View view) {

        if(isValid()){


        }
    }

    public void AddPic(View view) {
    }

    public boolean isValid(){

        if(Comment.getText().length()==0){
            Comment.setError("يجب ملء الخانة");
            return false;
        }
        else
            if(doc_rate.getRating()==0.0 || hos_rate.getRating()==0.0|| price_rate.getRating()==0.0){
                Toast error =  Toast.makeText(this,"يجب أن يكون التقيم أكثر من 0 نجمة",Toast.LENGTH_LONG);
                error.show();
                return false;
        }
        return  true;
    }
}
