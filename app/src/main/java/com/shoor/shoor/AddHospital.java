package com.shoor.shoor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddHospital  extends AppCompatActivity {
    EditText HospitalName_input;
    EditText HospitalNumber_input;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hospital);
        HospitalName_input = (EditText) findViewById(R.id.Hospital_Name);
        HospitalNumber_input = (EditText) findViewById(R.id.Hospital_Number);
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
                sql = "INSERT INTO hospital (HospitalName, PhoneNumber) Values('" + HospitalName + "','" + HospitalNumber + "')";
                int rs = stmt.executeUpdate(sql);
                if(rs==1){
                    Toast done = Toast.makeText(AddHospital.this, "تمت الإضافة", Toast.LENGTH_SHORT);
                    done.show();
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

}


