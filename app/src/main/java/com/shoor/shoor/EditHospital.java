package com.shoor.shoor;


import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EditHospital extends AppCompatActivity {
    Spinner list;
    Spinner list1;
    EditText HospitalName;
    EditText HospitalNumber;
    List<String> hospital = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hospital);
        list = (Spinner) findViewById(R.id.hospital_list);
        HospitalName = (EditText) findViewById(R.id.HospitalName);
        HospitalNumber = (EditText) findViewById(R.id.HospitalNum);

        RetriveData();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hospital);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        list.setAdapter(adapter);
    }

    public void RetriveData() {
    //VERY IMPORTANT LINES
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    //SETUP CONNECTION
    Connection conn = null;
    Statement stmt = null;
        try

    {
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
            String dp = rs.getString("HospitalName");
            hospital.add(dp);
        }
        //STEP 6: Clean-up environment
        rs.close();
        stmt.close();
        conn.close();
    } catch(SQLException se){
        //SHOW SERVER FAILED MESSAGE
        Toast errorToast = Toast.makeText(EditHospital.this, "يجب أن تكون متصلاً بالإنترنت", Toast.LENGTH_SHORT);
        errorToast.show();
    } catch(Exception e){
        //SHOW SERVER FAILED MESSAGE
        Toast errorToast = Toast.makeText(EditHospital.this, "" + e.getMessage(), Toast.LENGTH_SHORT);
        errorToast.show();
    }
}//End RetriveData
    public void Do(View view) {
        String Name = list.getSelectedItem().toString();
        String Number = list.getSelectedItem().toString();
        //Validate inputs
        String NewName = HospitalName.getText().toString();
        String NewNum = HospitalNumber.getText().toString();
        if (ValidateInputs(NewName,NewNum)) {
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
                sql = "UPDATE hospital SET HospitalName = ('" + NewName  + "'),PhoneNumber = ('" + NewNum  + "')WHERE HospitalName = ('" + Name  + "'),PhoneNumber = ('" + Number  + "')";
                int rs = stmt.executeUpdate(sql);

                if(rs==1){
                    Toast done = Toast.makeText(EditHospital.this, "تم التعديل", Toast.LENGTH_SHORT);
                    done.show();
                    HospitalName.setText("");
                }
                else
                {
                    Toast done = Toast.makeText(EditHospital.this, "حدثت مشكلة أثناء التعديل", Toast.LENGTH_SHORT);
                    done.show();
                }

                //STEP 6: Clean-up environment
                stmt.close();
                conn.close();
            } catch (SQLException se) {
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(EditHospital.this, "يجب أن تكون متصلاً بالإنترنت??", Toast.LENGTH_SHORT);
                errorToast.show();
            } catch (Exception e) {
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(EditHospital.this, "" + e.getMessage(), Toast.LENGTH_SHORT);
                errorToast.show();
            }
        }
    }//End Do

    public boolean ValidateInputs(String Name , String Number){

        if (Name.equals("")) {
            HospitalName.setError("يجب ملء الخانة");
            return false;
        }
        if (Number.equals("")){
            HospitalNumber.setError("يجب ملء الخانة");
            return false;
        }
        if(Name.length()>20){
            HospitalName.setError("يجب ألا يتجاوز اسم المستشفى 20 حرفاً");
            return false;
        }
        if(Number.length()>10){
            HospitalNumber.setError("يجب ألا يتجاوز رقم الهاتف 10 رقماً");
            return false;
        }
        // cheeck if number or not


        return true;
    }

    public void back(View view) {
        startActivity(new Intent(EditHospital.this,ManageContentActivity.class));

    }
}//End class
