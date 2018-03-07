package com.shoor.shoor;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import static com.shoor.shoor.SignIn.user_id;

public class AddDoctor  extends AppCompatActivity {
    public EditText DoctorName, DoctorHours,DoctorPrice;
    public  Spinner hospitallist,departmentlist;
    ArrayList<String> department = new ArrayList<String>() ;
    ArrayList<String> hospital = new ArrayList<String>() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);
        RetriveData();
        DoctorName=(EditText)findViewById(R.id.Doctor_Name);
        DoctorHours=(EditText)findViewById(R.id.Doctor_Hours);
        DoctorPrice=(EditText)findViewById(R.id.Doctor_Price);
        hospitallist=(Spinner) findViewById(R.id.hospital_list);
        departmentlist=(Spinner) findViewById(R.id.department_list);
        ArrayAdapter<String> department_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, department);
        department_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentlist.setAdapter(department_adapter);

        ArrayAdapter<String> hospital_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hospital);
        department_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hospitallist.setAdapter(hospital_adapter);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////

    public void Send(View view) {
        //Validate inputs
        String DocName  = DoctorName.getText().toString();
        String DocHours = DoctorHours.getText().toString();
        String DocPrice = DoctorPrice.getText().toString();
        String DepName  = departmentlist.getSelectedItem().toString();
        String HosName  = hospitallist.getSelectedItem().toString();

        if (isValid(DocName,DocHours,DocPrice )) {
            //VERY IMPORTANT LINES
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //SETUP CONNECTION
            Connection conn = null;
            Statement stmt = null;
            try{
                //STEP 2: Register JDBC driver
                Class.forName("com.mysql.jdbc.Driver");

                //STEP 3: Open a connection
                conn = DriverManager.getConnection(DB_Info.DB_URL,DB_Info.USER,DB_Info.PASS);

                //STEP 4: Execute a query
                stmt = conn.createStatement();
                String sql;
                sql = "INSERT INTO doctor(DoctorName, Specialties_ID, Hospital_ID, AvgRate, OfficeHours, Price) VALUES ('"+DocName+"',18,1,0.00,'"+DocHours+"',"+DocPrice+")";////////=========================????????????????????
                int rs = stmt.executeUpdate(sql);

                if(rs==1){
                    Toast done = Toast.makeText(AddDoctor.this, "تمت الإضافة", Toast.LENGTH_SHORT);
                    done.show();
                }
                else
                {
                    Toast done = Toast.makeText(AddDoctor.this, "حدثت مشكلة أثناء الاضافة", Toast.LENGTH_SHORT);
                    done.show();
                }

                //STEP 6: Clean-up environment

                stmt.close();
                conn.close();
            }catch(SQLException se){
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(AddDoctor.this, "يجب أن تكزن متصلاً بالانترنت"+se.getMessage(), Toast.LENGTH_SHORT);
                errorToast.show();
            }catch(Exception e){
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(AddDoctor.this, ""+e.getMessage(), Toast.LENGTH_SHORT);
                errorToast.show();
            }
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean isValid(String Namedoctor,String DocHours ,String DocPrice ){//??
        //validate all inputs
        if (Namedoctor.equals("") ) {
            DoctorName.setError("يجب ملء الخانة");
            return false;
        }
        if(Namedoctor.length() >20)
        {
            DoctorName.setError("يجب أن لا يتجاوز الاسم 20 حرفاً");
            return false;
        }
        if(DocHours.length() >15)
        {
            DoctorName.setError("يجب أن لا يتجاوز الاسم 20 حرفاً");
            return false;
        }


        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void RetriveData() {

        //VERY IMPORTANT LINES
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //SETUP CONNECTION
        Connection conn = null;
        Statement stmt = null , stmt2=null;
        try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            conn = DriverManager.getConnection(DB_Info.DB_URL,DB_Info.USER,DB_Info.PASS);

            //STEP 4: Execute a query
            stmt = conn.createStatement();
            stmt2=conn.createStatement();
            String sql_department,sql_hospital;
            sql_department = "SELECT * FROM specialties";
            ResultSet rs1 = stmt.executeQuery(sql_department);
            sql_hospital   ="SELECT * FROM hospital";
            ResultSet rs2 = stmt2.executeQuery( sql_hospital);

            //STEP 5: Extract data from result set
            while(rs1.next())
            {   String dp = rs1.getString("SpecialtiesName");
                department.add(dp);
            }
            while(rs2.next())
            {   String Ho = rs2.getString("HospitalName");
                hospital.add(Ho);
            }


            //STEP 6: Clean-up environment
            rs1.close();
            rs2.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(AddDoctor.this, "يجب أن تكون متصلاً بالإنترنت", Toast.LENGTH_SHORT);
            errorToast.show();
        }catch(Exception e){
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(AddDoctor.this, ""+e.getMessage(), Toast.LENGTH_SHORT);
            errorToast.show();
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    public void back(View view) {
        startActivity(new Intent(AddDoctor.this, ManageContentActivity.class));
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
}// end class

