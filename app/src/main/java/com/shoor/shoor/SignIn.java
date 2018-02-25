package com.shoor.shoor;


import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.sql.*;


public class SignIn extends AppCompatActivity {
   EditText email_input ;
   EditText pass_input ;
   public static String user_id="";
   public static String admin_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        email_input =(EditText)findViewById(R.id.email_field);
        pass_input = (EditText) findViewById(R.id.pass_field);
    }


    public void signIn(View view) {
        String email = email_input.getText().toString();
        String  pass = pass_input.getText().toString();

        //validate input
        boolean isValid =ValidateInputs(email,pass);
        if (isValid){
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
                sql = "SELECT * FROM user where UserEmail='"+email+"' AND Password='"+pass+"'";
                ResultSet rs = stmt.executeQuery(sql);

                //STEP 5: Extract data from result set
                int count=0;
                while(rs.next())
                {
                    user_id=rs.getString(1);
                    count++;
                }
                if(count==1) {
                    //set session user_id
                    SharedPreferences sharedpreferences = getSharedPreferences(user_id, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("user_id",user_id);
                    editor.commit();
                    //redirect to home activity (spicalty)
                    startActivity(new Intent(SignIn.this, Specialty.class));
                }
                else
                {//check if admin
                    Statement stmt2 = conn.createStatement();
                    String sql2  = "SELECT * FROM admin where Email='"+email+"' AND Password='"+pass+"'";
                    ResultSet result = stmt.executeQuery(sql);
                    int count2=0;
                    while(rs.next())
                    {
                        admin_id=rs.getString(1);
                        count2++;
                    }
                    if(count==1) {
                        //set session admin_id
                        SharedPreferences sharedpreferences = getSharedPreferences(admin_id, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("admin_id",admin_id);
                        editor.commit();
                        //redirect to home activity (spicalty)
                        startActivity(new Intent(SignIn.this, ManageContentActivity.class));
                    }
                    else {
                        //error message
                        Toast errorToast = Toast.makeText(SignIn.this, "كلمة المرور أو اسم المستخدم غير صحيحة", Toast.LENGTH_SHORT);
                        errorToast.show();
                    }
                }
                //STEP 6: Clean-up environment
                rs.close();
                stmt.close();
                conn.close();
            }catch(SQLException se){
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(SignIn.this, "أرجو المحاولة لاحقاً", Toast.LENGTH_SHORT);
                errorToast.show();
            }catch(Exception e){
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(SignIn.this, "أرجو المحاولة لاحقاً", Toast.LENGTH_SHORT);
                errorToast.show();
            }finally{
                //finally block used to close resources
                try{
                    if(stmt!=null)
                        stmt.close();
                }catch(SQLException se2){
                    //SHOW SERVER FAILED MESSAGE
                    Toast errorToast = Toast.makeText(SignIn.this, "أرجو المحاولة لاحقاً", Toast.LENGTH_SHORT);
                    errorToast.show();
                }// nothing we can do
                try{
                    if(conn!=null)
                        conn.close();
                }catch(SQLException se){
                    //SHOW SERVER FAILED MESSAGE
                    Toast errorToast = Toast.makeText(SignIn.this, "أرجو المحاولة لاحقاً", Toast.LENGTH_SHORT);
                    errorToast.show();
                }//end finally try
            }//end try


        }//end if


    }


    //this method to validate user sign in inputs
public boolean ValidateInputs(String useremail , String password){
    if (useremail.equals("")) {
        email_input.setError("يجب ملء الخانة");
        return false;
    }
    if (password.equals("")){
        pass_input.setError("يجب ملء الخانة");
        return false;
    }
    if(useremail.length()==40){
        email_input.setError("يجب ألا يتجاوز البريد الإلكتروني 40 حرفاً");
        return false;
    }
    if(password.length()==20){
        pass_input.setError("يجب ألا تتجاوز كلمة المرور 20 حرفاً");
        return false;
    }

    if(password.length()<8){
        pass_input.setError("يجب ألا تقل كلمة المرور عن 8 أحرف");
        return false;
    }


    return true;
}


    public void ToSignUp(View view) {
        startActivity(new Intent(SignIn.this, SignUp.class));

    }
}
