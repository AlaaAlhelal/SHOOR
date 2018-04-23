package com.shoor.shoor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddDoctorReviewActivity extends AppCompatActivity {

    public RatingBar doc_rate;
    public EditText Comment;
    public String Doctor_ID;
    public EditText Price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor_review);
        doc_rate =(RatingBar) findViewById(R.id.rating_doctor);
        Comment =(EditText)findViewById(R.id.comment);
        Price =(EditText)findViewById(R.id.PriceValue);

        SharedPreferences sharedpreferences2 = getSharedPreferences(DoctorListAdapter.Doc_Id, Context.MODE_PRIVATE);
        Doctor_ID = sharedpreferences2.getString("Doctor_ID", "");




    }

    public void back(View view) {
        this.finish();
        startActivity(new Intent(AddDoctorReviewActivity.this, DoctorProfileActivity.class));
    }



    public boolean isValid(){
        if(Comment.getText().length()==0){
            Comment.setError("يجب ملء الخانة");
            return false;
        }
        else
            if(doc_rate.getRating()==0.0 ){
                Toast error =  Toast.makeText(this,"يجب أن يكون التقيم أكثر من 0 نجمة",Toast.LENGTH_LONG);
                error.show();
                return false;
        }
        else
            if(Price.getText().length()==0){
                Price.setError("يجب ملء الخانة");
                return false;
            }
            else if(Integer.parseInt(String.valueOf(Price.getText()))<50){
                Price.setError("يجب أن يكون سعر الكشفية يبدأ من 50 وأكثر");
                return false;
            }
        return  true;
    }




    public void SendReview(View view) {
        int count=0;
        if(isValid()){
            double price =0, Rate=0 , userRate = doc_rate.getRating(), userPrice=Double.parseDouble(String.valueOf(Price.getText()));
            String comment =Comment.getText().toString();

            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DB_Info.DB_URL,DB_Info.USER,DB_Info.PASS);
                Statement stmt = conn.createStatement();
                String sql  = "Select * From doctorreview where Doctor_ID="+Doctor_ID;
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()){
                    count++;
                    Rate+=rs.getDouble("DoctorRate");
                    price+=rs.getDouble("Price");
                }
                rs.close();
                stmt.close();

                //set new Review
                String user_ID = SaveLogin.getUserID(getApplicationContext());
                Statement stm = conn.createStatement();
                String sqlq  = "INSERT into doctorreview (DoctorComment ,  DoctorRate ,Price ,Doctor_ID , User_ID) Values ( '"+comment+"' , '"+userRate+"' , '"+userPrice+"', '"+Doctor_ID+"' , '"+user_ID+"' )";
                int queryresult = stm.executeUpdate(sqlq);
            if(queryresult==1){
                stm.close();
                //update doctor Rate
                double newAvgRate=(Rate+userRate)/(count+1);
                double newAvgPrice =(price+userPrice)/(count+1);
                Statement statement = conn.createStatement();
                String query = "Update Doctor SET AvgRate=('"+newAvgRate+"'), Price=('"+newAvgPrice+"') Where Doctor_ID="+Doctor_ID;
                int result =statement.executeUpdate(query);
                Toast error = Toast.makeText(this,"تمت إضافة تعليقك",Toast.LENGTH_LONG);
                error.show();
                startActivity(new Intent(AddDoctorReviewActivity.this, DoctorProfileActivity.class));
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
}
