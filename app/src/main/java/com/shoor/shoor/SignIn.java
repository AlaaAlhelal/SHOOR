package com.shoor.shoor;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;


public class SignIn extends AppCompatActivity {
   EditText email_input ;
   EditText pass_input ;
   public static User user = new User();
   public static String user_id="";
   public static String admin_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        email_input =(EditText)findViewById(R.id.email_field);
        pass_input = (EditText) findViewById(R.id.pass_field);


        //check wifi
        isConnected();

        //check login state
        String userid= SaveLogin.getUserID(getApplicationContext());
         if(userid.length()!=0)
         {
             this.finish();
             startActivity(new Intent(SignIn.this,Specialties.class));
         }

    }

        //Sign in  into the app even for user or admin
    public void signIn(View view) {
        String email = email_input.getText().toString();
        String  pass = pass_input.getText().toString();

        //validate inputs
        boolean isValid =ValidateInputs(email,pass);

        if (isValid){
            String HashPass = md5(pass);
            System.out.println(HashPass);
            //VERY IMPORTANT LINES
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            user_id = user.Login(HashPass, email );


                if(user_id!=null) {
                    //set Login status
                    SaveLogin.setUserID(getApplicationContext(),user_id);
                    //redirect to home activity (spicalty)
                    startActivity(new Intent(SignIn.this, Specialties.class));
                    this.finish();
                }
                else
                {//check if admin

                    Admin admin = new Admin();
                    admin_id=admin.Login(HashPass,email);

                    if(admin_id!=null) {
                        //set Login status
                        SaveLogin.setAdminId(getApplicationContext(),admin_id);
                        //redirect to home activity (spicalty)
                        startActivity(new Intent(SignIn.this, ManageContentActivity.class));
                        this.finish();
                    }
                    else {
                        //error message
                        Toast errormessage = Toast.makeText(SignIn.this, "كلمة المرور أو اسم المستخدم غير صحيحة", Toast.LENGTH_SHORT);
                        errormessage.show();
                    }
                }






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
        this.finish();

    }

    public boolean isConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiInfo != null && wifiInfo.isConnected()) || (mobileInfo != null && mobileInfo.isConnected())) {
            return true;
        } else {
            showDialog();
            return false;
        }
    }

    private void showDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("أنت غير متصل بالانترنت هل تريد الاتصال بالانترنت أو إغلاق التطبيق؟")
                .setCancelable(false)
                .setPositiveButton("الاتصال", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("إغلاق", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
