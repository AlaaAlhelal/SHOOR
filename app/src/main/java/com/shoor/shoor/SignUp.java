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
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

        //check wifi connection
        isConnected();

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

                    String hashpass = md5(userp);
                    sql = "INSERT INTO User (UserEmail, UserName, Password, Gender) Values('" + usere + "','" + usern + "','" + hashpass + "','" + gender + "')";
                    int rs = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
                    ResultSet keys = stmt.getGeneratedKeys();
                    stmt.close();
                    if (rs == 1) {//num of row affected

                        //set session user_id
                        while (keys.next()) {
                            user_id = keys.getString(1);
                        }
                        SaveLogin.setUserID(getApplicationContext(),user_id);

                        //redirect to home activity (spicalty)
                        startActivity(new Intent(SignUp.this, Specialty.class));
                    } else {
                        //error message
                        Toast errorToast = Toast.makeText(SignUp.this, "لقد تم التسجيل بهذا البريد مسبقاً", Toast.LENGTH_SHORT);
                        errorToast.show();
                    }

                    keys.close();
                    stmt.close();

                }
                resultset.close();
                statm.close();
                conn.close();
                }catch(SQLException se){
                    //SHOW SERVER FAILED MESSAGE
                    Toast errorToast = Toast.makeText(SignUp.this, "لا يمكنك التسجيل الآن، أرجو المحاولة لاحقا", Toast.LENGTH_SHORT);
                    errorToast.show();
                }catch(Exception e){
                    //SHOW SERVER FAILED MESSAGE
                    Toast errorToast = Toast.makeText(SignUp.this, "لا يمكنك التسجيل الآن، أرجو المحاولة لاحقا", Toast.LENGTH_SHORT);
                    errorToast.show();
                                }//end try

        }
    }

    public boolean isValid(String username , String  useremail, String userpass){

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
        if(userpass.length()==20){
            UserPass.setError("يجب ألا تتجاوز كلمة المرور 20 حرفاً");
            return false;
        }

        if(userpass.length()<8){
            UserPass.setError("يجب ألا تقل كلمة المرور عن 8 أحرف");
            return false;
        }
        if(username.length()==20){
            UserName.setError("يجب ألا يتجاوز اسم المستخدم 20 حرفاً");
            return false;
        }

        try {
            if(useremail.contains("@")) {
                String Emailvalidation = useremail.substring(useremail.indexOf('@'));
                String pattren = Emailvalidation.toLowerCase();
                switch (Emailvalidation) {
                    case "@hotmail.com":
                        return true;
                    case "@gmail.com":
                        return true;
                    case "@outlook.com":
                        return true;
                    case "@icloud.com":
                        return true;
                    case "@yahoo.com":
                        return true;
                    default:  UserEmail.setError("البريد الإلكتروني غير صحيح");
                        return false;
                }
            }
            else  {
                UserEmail.setError("البريد الإلكتروني غير صحيح");
                return false;
            }

        }catch (Exception e ){
            Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_LONG).show();
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

        this.finish();
        startActivity(new Intent(SignUp.this, SignIn.class));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing the user from back to previous activity
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void isConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiInfo != null && wifiInfo.isConnected()) || (mobileInfo != null && mobileInfo.isConnected())) {
        } else {
            showDialog();
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
