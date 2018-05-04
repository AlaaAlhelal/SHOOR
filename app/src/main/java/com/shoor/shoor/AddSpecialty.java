package com.shoor.shoor;


import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

public class AddSpecialty extends AppCompatActivity {
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
            Specialty specialty = new Specialty();
            boolean Added = specialty.AddSpecialty(DepName);
            if(Added){
                Toast done = Toast.makeText(AddSpecialty.this, "تمت الإضافة", Toast.LENGTH_SHORT);
                done.show();
                department_name.setText("");
            }else {
                Toast done = Toast.makeText(AddSpecialty.this, "حدثت مشكلة أثناء الإضافة، حاول لاحقاً", Toast.LENGTH_SHORT);
                done.show();
            }
        }
    }

    public boolean isValid(String DepartmentName){

        String pattrenAr = "[\\u0600-\\u06FF]+";
        Pattern pHosName = Pattern.compile(pattrenAr);
        String soecialtyname =DepartmentName.replaceAll("\\s+","");

        //validate all inputs
        if (!pHosName.matcher(soecialtyname).matches() ) {
            department_name.setError("يجب إدخال أحرف عربية فقط");
            return false;
        }
        if (DepartmentName.equals("")  ) {
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
        this.finish();
        startActivity(new Intent(AddSpecialty.this, ManageContentActivity.class));

    }
}//END class
