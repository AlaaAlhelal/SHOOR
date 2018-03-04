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

import static com.shoor.shoor.SignIn.user_id;

public class AddDepartment extends AppCompatActivity {
    EditText department_name ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_department);
        department_name =(EditText)findViewById(R.id.add_department);
    }


    public void Send(View view) {
        //Validate inputs
        String DepName = department_name.getText().toString();

        if (isValid(DepName)) {
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
                sql = "INSERT INTO specialties (SpecialtiesName) Values('" + DepName + "')";
                ResultSet rs = stmt.executeQuery(sql);

                //STEP 6: Clean-up environment
                rs.close();
                stmt.close();
                conn.close();
            }catch(SQLException se){
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(AddDepartment.this, "يجب أن تكون متصلاً بالإنترنت", Toast.LENGTH_SHORT);
                errorToast.show();
            }catch(Exception e){
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(AddDepartment.this, ""+e.getMessage(), Toast.LENGTH_SHORT);
                errorToast.show();
            }
        }
    }

    public boolean isValid(String DepartmentName){
        //validate all inputs
        if (DepartmentName.equals("")) {
            department_name.setError("يجب ملء الخانة");
            return false;
        }
        return true;
    }

}
