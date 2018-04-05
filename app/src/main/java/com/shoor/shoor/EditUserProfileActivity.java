package com.shoor.shoor;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class EditUserProfileActivity extends AppCompatActivity {
    EditText username,useremail;
    public String gender="M";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        username = ((EditText)findViewById(R.id.user_name));
        useremail= ((EditText)findViewById(R.id.user_email));

    }

///////////////////////////////////////////////////////////////////////////////////////////////////////
    public void Do(View view) {

        String username1= username.getText().toString();
        String useremail1= useremail.getText().toString();
        boolean isValid =ValidateInputs(username1 ,useremail1);
        if (isValid){

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
                    String userid =SaveLogin.getUserID(getApplicationContext());
                    //STEP 4: Execute a query
                    stmt = conn.createStatement();
                    String sql;
                    sql = "UPDATE user SET UserName = ('" + username1 + "'), UserEmail=('"+useremail1+"'),Gender=('"+gender+"') WHERE User_ID=('" + userid + "')";
                    int rs = stmt.executeUpdate(sql);

                    if(rs==1){
                        Toast done = Toast.makeText(EditUserProfileActivity.this, "تم التعديل", Toast.LENGTH_SHORT);
                        done.show();

                    }
                    else
                    {
                        Toast done = Toast.makeText(EditUserProfileActivity.this, "حدثت مشكلة أثناء التعديل", Toast.LENGTH_SHORT);
                        done.show();
                    }

                    //STEP 6: Clean-up environment
                    stmt.close();
                    conn.close();
                } catch (SQLException se) {
                    //SHOW SERVER FAILED MESSAGE
                    Toast errorToast = Toast.makeText(EditUserProfileActivity.this, "يجب أن تكون متصلاً بالإنترنت", Toast.LENGTH_SHORT);
                    errorToast.show();
                } catch (Exception e) {
                    //SHOW SERVER FAILED MESSAGE
                    Toast errorToast = Toast.makeText(EditUserProfileActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT);
                    errorToast.show();
                }
            }
        }


/////////////////////////////////////////////////////////////////////////////////////////////////////
    public void Gender_Clicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();


        switch(view.getId()) {
            case R.id.male:
                if (checked)
                    gender="M";
                break;
            case R.id.female:
                if (checked)
                    gender="F";
                break;
        }



    }

//////////////////////////////////////////////////////////////////////////////////////
    public boolean ValidateInputs(String username1 ,String useremail1){

        //validate all inputs
        if (useremail1.equals("")) {
            useremail.setError("يجب ملء الخانة");
            return false;
        }


        if (username1.equals("")){
            username.setError("يجب ملء الخانة");
            return false;
        }

        if(useremail1.length()==40){
            useremail.setError("يجب ألا يتجاوز البريد الإلكتروني 40 حرفاً");
            return false;
        }


        if(username1.length()==20){
            username.setError("يجب ألا يتجاوز اسم المستخدم 20 حرفاً");
            return false;
        }

        return true;
    }

    ////////////////////////////////////////////////////////////////////////////


    public void back(View view) {
        startActivity(new Intent(EditUserProfileActivity.this,ProfileActivity.class));
    }

    public void editpass(View view) {
        startActivity(new Intent(EditUserProfileActivity.this,EditPasswordActivity.class));
        this.finish();

    }
}
