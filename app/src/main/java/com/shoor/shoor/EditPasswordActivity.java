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

public class EditPasswordActivity extends AppCompatActivity {
    public EditText Old_pass,New_pass=null;
    public static final String KEY="Key_email";
    public static String user_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        Old_pass = ((EditText)findViewById(R.id.Old_password));
        New_pass= ((EditText)findViewById(R.id.New_password));

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void Do(View view) {

        String Value_email=null;
        String oldpass= Old_pass.getText().toString();
        String newpass= New_pass.getText().toString();

System.out.println("Enter Do()");
        //validate input
        boolean isValid =ValidateInputs(oldpass,newpass);
        if (isValid){
            //VERY IMPORTANT LINES
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //SETUP CONNECTION
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs;
            int one=0;
            try {
                //STEP 2: Register JDBC driver
                Class.forName("com.mysql.jdbc.Driver");

                //STEP 3: Open a connection
                conn = DriverManager.getConnection(DB_Info.DB_URL, DB_Info.USER, DB_Info.PASS);
                String userid =SaveLogin.getUserID(getApplicationContext());
                //STEP 4: Execute a query
                stmt = conn.createStatement();
                String sql;
                sql = "SELECT * FROM user where User_ID=" + userid ;
                rs = stmt.executeQuery(sql);
                String v =null;
                while (rs.next()) {
                    v= rs.getString("Password");
                }

                assert v != null;
               /* if(v.equals(oldpass))
                {
                    Old_pass.setError("");
                    Toast done = Toast.makeText(EditPasswordActivity.this, "right" , Toast.LENGTH_SHORT);
                    done.show();
                }
                else
                {
                    Toast done = Toast.makeText(EditPasswordActivity.this, "wrong", Toast.LENGTH_SHORT);
                    done.show();
                }*/

                if(v.equals(oldpass))
                {//IF equal -------------------------------------------------------------------------
                    stmt = conn.createStatement();
                    String sql1;
                    sql1 = "UPDATE user SET Password= ('" + newpass + "') WHERE User_ID= ('" + userid+ "')";
                    int rs1 = stmt.executeUpdate(sql1);

                    if(rs1==1){
                        Toast done = Toast.makeText(EditPasswordActivity.this, "تم التعديل", Toast.LENGTH_SHORT);
                        done.show();
                    }
                    else
                    {
                        Toast done = Toast.makeText(EditPasswordActivity.this, "حدثت مشكلة أثناء التعديل", Toast.LENGTH_SHORT);
                        done.show();
                    }
                }//End equal ---------------------------------------------------------------------------------

                else {
                        //error message
                        Toast errormessage = Toast.makeText(EditPasswordActivity.this, "كلمة المرور السابقة غير صحيحة", Toast.LENGTH_SHORT);
                        errormessage.show();

                }
                //STEP 6: Clean-up environment

                rs.close();
                stmt.close();
                conn.close();
            }catch(SQLException se){
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(EditPasswordActivity.this, "يجب أن تكون متصلاً بالإنترنت", Toast.LENGTH_SHORT);
                errorToast.show();
            }catch(Exception e){
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(EditPasswordActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT);
                errorToast.show();
            }finally{
                //finally block used to close resources
                try{
                    if(stmt!=null)
                        stmt.close();
                }catch(SQLException se2){
                    //SHOW SERVER FAILED MESSAGE
                    Toast errorToast = Toast.makeText(EditPasswordActivity.this, "أرجو المحاولة لاحقاً", Toast.LENGTH_SHORT);
                    errorToast.show();
                }// nothing we can do
                try{
                    if(conn!=null)
                        conn.close();
                }catch(SQLException se){
                    //SHOW SERVER FAILED MESSAGE
                    Toast errorToast = Toast.makeText(EditPasswordActivity.this, "أرجو المحاولة لاحقاً", Toast.LENGTH_SHORT);
                    errorToast.show();
                }//end finally try
            }//end try


        }//end if
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////


    public boolean ValidateInputs(String passOld ,String passNew){

        System.out.println("Enter validate");
        if (passOld.equals("")){
            Old_pass.setError("يجب ملء الخانة");
            return false;
        }

        if(passOld.length()==20){
            Old_pass.setError("يجب ألا تتجاوز كلمة المرور 20 حرفاً");
            return false;
        }

        if(passOld.length()<8){
            Old_pass.setError("يجب ألا تقل كلمة المرور عن 8 أحرف");
            return false;
        }
        if (passNew.equals("")){
            New_pass.setError("يجب ملء الخانة");
            return false;
        }

        if(passNew.length()==20){
            New_pass.setError("يجب ألا تتجاوز كلمة المرور 20 حرفاً");
            return false;
        }

        if(passNew.length()<8){
            New_pass.setError("يجب ألا تقل كلمة المرور عن 8 أحرف");
            return false;
        }


        return true;

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void back(View view) {
        startActivity(new Intent(EditPasswordActivity.this,ManageContentActivity.class));

    }
}//END CLASS

