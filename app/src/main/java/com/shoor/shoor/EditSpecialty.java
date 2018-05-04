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
import java.util.regex.Pattern;

public class EditSpecialty extends AppCompatActivity {
    Spinner list;
    EditText department_name;
    List<String> department = new ArrayList<String>();
    List<Specialty> specialties = new ArrayList<Specialty>();
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_department);
        list = (Spinner) findViewById(R.id.department_list);
        department_name = (EditText) findViewById(R.id.add_department);
        RetriveData();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, department);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        list.setAdapter(adapter);

    }
/////////////////////////////////////////////////////////////////////////////////////////////////
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
            sql = "SELECT * FROM specialties";
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set
            while (rs.next()) {
                String specialtiesName= rs.getString("SpecialtiesName");
                String specialties_id= rs.getString("Specialties_ID");
                Specialty specialty = new Specialty(specialties_id,specialtiesName);
                specialties.add(specialty);
                department.add(specialtiesName);
            }
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(EditSpecialty.this, "يجب أن تكون متصلاً بالإنترنت", Toast.LENGTH_SHORT);
            errorToast.show();
        } catch (Exception e) {
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(EditSpecialty.this, "" + e.getMessage(), Toast.LENGTH_SHORT);
            errorToast.show();
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////
    public void Do(View view) {
        int index = list.getSelectedItemPosition();
        Specialty specialty = specialties.get(index);
        String DepName = specialty.getName();
        //Validate inputs
        String NewDep = department_name.getText().toString();
        if (isValid(NewDep)) {
            //VERY IMPORTANT LINES
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            boolean edited = specialty.EditSpecialty(NewDep);

                if(edited){
                    Toast done = Toast.makeText(EditSpecialty.this, "تم التعديل", Toast.LENGTH_SHORT);
                    done.show();
                    department.remove(DepName);
                    department.add(index,NewDep);
                    adapter.notifyDataSetChanged();
                    department_name.setText("");
                }
                else
                {
                    Toast done = Toast.makeText(EditSpecialty.this, "حدثت مشكلة أثناء التعديل، حاول لاحقاً", Toast.LENGTH_SHORT);
                    done.show();

                }



        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean isValid(String DepartmentName) {
        String pattrenAr = "[\\u0600-\\u06FF]+";
        Pattern pHosName = Pattern.compile(pattrenAr);
        String soecialtyname =DepartmentName.replaceAll("\\s+","");

        //validate all inputs
        if (!pHosName.matcher(soecialtyname).matches() ) {
            department_name.setError("يجب إدخال أحرف عربية فقط");
            return false;
        }
        if (DepartmentName.equals("")) {
            department_name.setError("يجب ملء الخانة");
            return false;
        }
        if(DepartmentName.length() >20)
        {
            department_name.setError("يجب أن لا يتجاوز الاسم 20 حرفاً");
            return false;
        }
        return true;
    }

    public void back(View view) {
        startActivity(new Intent(EditSpecialty.this,ManageContentActivity.class));

    }
}//End
