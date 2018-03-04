package com.shoor.shoor;


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

public class EditDepartment extends AppCompatActivity {
    Spinner list;
    EditText department_name;
    ArrayList<String> department = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_department);
        RetriveData();
        list = (Spinner) findViewById(R.id.department_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, department);
        list.setAdapter(adapter);
        department_name = (EditText) findViewById(R.id.add_department);
    }

    public void RetriveData() {

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
            sql = "SELECT SpecialtiesName FROM specialties";
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set

            while (rs.next()) {
                String dp = rs.toString();
                department.add(dp);
            }

            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(EditDepartment.this, "يجب أن تكون متصلاً بالإنترنت", Toast.LENGTH_SHORT);
            errorToast.show();
        } catch (Exception e) {
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(EditDepartment.this, "" + e.getMessage(), Toast.LENGTH_SHORT);
            errorToast.show();
        }
    }

    public void Do(View view) {
        String DepName = list.getSelectedItem().toString();
        //Validate inputs
        String NewDep = department_name.getText().toString();
        if (isValid(DepName)) {
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
                sql = "UPDATE specialties SET SpecialtiesName = ('" + NewDep + "')WHERE SpecialtiesName=('" + DepName + "')";
                int rs = stmt.executeUpdate(sql);

                if(rs==1){
                    Toast done = Toast.makeText(EditDepartment.this, "تم التعديل", Toast.LENGTH_SHORT);
                    done.show();
                }
                else
                {
                    Toast done = Toast.makeText(EditDepartment.this, "حدثت مشكلة أثناء التعديل", Toast.LENGTH_SHORT);
                    done.show();
                }

                //STEP 6: Clean-up environment
                stmt.close();
                conn.close();
            } catch (SQLException se) {
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(EditDepartment.this, "يجب أن تكون متصلاً بالإنترنت", Toast.LENGTH_SHORT);
                errorToast.show();
            } catch (Exception e) {
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(EditDepartment.this, "" + e.getMessage(), Toast.LENGTH_SHORT);
                errorToast.show();
            }
        }
    }

    public boolean isValid(String DepartmentName) {
        //validate all inputs
        if (DepartmentName.equals("")) {
            department_name.setError("يجب ملء الخانة");
            return false;
        }
        return true;
    }
}
