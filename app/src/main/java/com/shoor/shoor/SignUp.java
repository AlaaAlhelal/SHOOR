package com.shoor.shoor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SignUp extends AppCompatActivity {
    public EditText UserName = null;
    public EditText UserPass = null;
    public EditText UserEmail= null;
    public String gender="M";
    public String user_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        UserName = ((EditText)findViewById(R.id.username));
        UserPass = ((EditText)findViewById(R.id.password));
        UserEmail= ((EditText)findViewById(R.id.useremail));

    }


    public void SignUp(View view) {
        //Validate inputs
        String usern=UserName.getText().toString();
        String usere=UserEmail.getText().toString();
        String userp=UserPass.getText().toString();
        if(isValid( usern,  usere,  userp)){
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

                //check if the email not for admin
                Statement statm = conn.createStatement();
                String query = "SELECT * FROM admin where Email='" + usere + "'";
                ResultSet resultset = statm.executeQuery(query);
                int isthere = 0;
                while (resultset.next()) {
                    isthere++;
                }
                if (isthere == 1) {
                    //error message
                    Toast errorToast = Toast.makeText(SignUp.this, "فشل التسجيل أرجوا التسجيل ببريد إلكتروني آخر", Toast.LENGTH_SHORT);
                    errorToast.show();

                } else {

                    //STEP 4: Execute a query
                    stmt = conn.createStatement();
                    String sql;
                    sql = "INSERT INTO User (UserEmail, UserName, Password, Gender) Values('" + usere + "','" + usern + "','" + userp + "','" + gender + "')";
                    int rs = stmt.executeUpdate(sql);
                    stmt.close();
                    if (rs == 1) {//num of row affected
                        String sql2 = "SELECT * FROM user where UserEmail='" + usere + "' AND Password='" + userp + "'";
                        Statement stmt2 = conn.createStatement();
                        ResultSet result = stmt2.executeQuery(sql2);
                        while (result.next()) {
                            user_id = result.getString("User_id");
                        }
                        result.close();
                        stmt2.close();
                        //set session user_id
                        SaveLogin.setUserID(getApplicationContext(),user_id);

                        //redirect to home activity (spicalty)
                        startActivity(new Intent(SignUp.this, Specialty.class));
                    } else {
                        //error message
                        Toast errorToast = Toast.makeText(SignUp.this, "لقد تم التسجيل بهذا البريد مسبقاً", Toast.LENGTH_SHORT);
                        errorToast.show();
                    }


                }
                resultset.close();
                statm.close();
                stmt.close();
                conn.close();
                }catch(SQLException se){
                    //SHOW SERVER FAILED MESSAGE
                    Toast errorToast = Toast.makeText(SignUp.this, "لا يمكنك التسجيل الآن، أرجو المحاولة لاحقا", Toast.LENGTH_SHORT);
                    errorToast.show();
                }catch(Exception e){
                    //SHOW SERVER FAILED MESSAGE
                    Toast errorToast = Toast.makeText(SignUp.this, "لا يمكنك التسجيل الآن، أرجو المحاولة لاحقا", Toast.LENGTH_SHORT);
                    errorToast.show();
                }finally{
                    //finally block used to close resources
                    try {
                        if (stmt != null)
                            stmt.close();
                    } catch (SQLException se2) {
                        //SHOW SERVER FAILED MESSAGE
                        Toast errorToast = Toast.makeText(SignUp.this, "لا يمكنك التسجيل الآن، أرجو المحاولة لاحقاً", Toast.LENGTH_SHORT);
                        errorToast.show();
                    }// nothing we can do
                    try {
                        if (conn != null)
                            conn.close();
                    } catch (SQLException se) {
                        //SHOW SERVER FAILED MESSAGE
                        Toast errorToast = Toast.makeText(SignUp.this, "لا يمكنك التسجيل الآن، أرجو المحاولة لاحقاً", Toast.LENGTH_SHORT);
                        errorToast.show();
                    }//end finally try
                }//end try

        }
    }

    public boolean isValid(String useremail, String username, String userpass){
        //validate all inputs
        if (useremail.equals("")) {
            UserEmail.setError("يجب ملء الخانة");
            return false;
        }
        if (userpass.equals("")){
            UserPass.setError("يجب ملء الخانة");
            return false;
        }
        if (username.equals("")){
            UserName.setError("يجب ملء الخانة");
            return false;
        }
        if(useremail.length()==40){
            UserEmail.setError("يجب ألا يتجاوز البريد الإلكتروني 40 حرفاً");
            return false;
        }
        if(username.length()==20){
            UserEmail.setError("يجب ألا يتجاوز اسم المستخدم 20 حرفاً");
            return false;
        }
        if(userpass.length()==20){
            UserPass.setError("يجب ألا تتجاوز كلمة المرور 20 حرفاً");
            return false;
        }

        if(userpass.length()<8){
            UserPass.setError("يجب ألا تقل كلمة المرور عن 8 أحرف");
            return false;
        }


        return true;
    }


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



    public void backToSignIn(View view) {
        startActivity(new Intent(SignUp.this, SignIn.class));
    }

}
