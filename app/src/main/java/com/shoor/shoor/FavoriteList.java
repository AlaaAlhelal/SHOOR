package com.shoor.shoor;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sun.mail.imap.protocol.ID;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class FavoriteList extends AppCompatActivity {

    public ArrayList<String> Names = new ArrayList<String>();
    public ArrayList<String> IDs = new ArrayList<String>();
    ListView lists ;
    FavoriteListAdapter AdapterList ;
    String sql ; // for the sql ;
    int ListID ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);




        // get list
        getAllLists();
        if(Names.size()!=0){
         lists =(ListView) findViewById(R.id.FavoriteLists);
         AdapterList = new FavoriteListAdapter(getApplicationContext(), Names, IDs);
        lists.setAdapter(AdapterList); }
        else
        {
            TextView message = new TextView(this);
            message.setText("لا يوجد لديك قوائم");
            message.setGravity(Gravity.CENTER);
            message.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT));
            LinearLayout view = (LinearLayout) findViewById(R.id.messagefav);
            view.addView(message);
        }


    }


    public void getAllLists(){
        try{

            //VERY IMPORTANT LINES
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_Info.DB_URL,DB_Info.USER,DB_Info.PASS);
            Statement stmt = conn.createStatement();
            String sqlq ="SELECT * From list Where User_ID="+SaveLogin.getUserID(getApplicationContext());
            ResultSet rs = stmt.executeQuery(sqlq);
            while (rs.next()){
                Names.add(rs.getString("ListName"));
                IDs.add(rs.getString("List_ID"));
            }

            stmt.close();
            conn.close();

        }//end try
        catch (SQLException sqle){
            Toast error = Toast.makeText(this,sqle.getMessage(),Toast.LENGTH_LONG);
            error.show();
        }
        catch (Exception e){
            Toast error = Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG);
            error.show();
        }
    }





    public void Do() {

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
                int rs = stmt.executeUpdate(sql);

                if(rs==1){
                    Toast done = Toast.makeText(FavoriteList.this, "تم الإضافة", Toast.LENGTH_SHORT);
                    done.show();
                    this.recreate();

                }
                else
                {
                    Toast done = Toast.makeText(FavoriteList.this, "حدثت مشكلة أثناء التعديل", Toast.LENGTH_SHORT);
                    done.show();
                }

                //STEP 6: Clean-up environment
                stmt.close();
                conn.close();
            } catch (SQLException se) {
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(FavoriteList.this, "يجب أن تكون متصلاً بالإنترنت", Toast.LENGTH_SHORT);
                errorToast.show();
            } catch (Exception e) {
                //SHOW SERVER FAILED MESSAGE
                Toast errorToast = Toast.makeText(FavoriteList.this, "" + e.getMessage(), Toast.LENGTH_SHORT);
                errorToast.show();
            }

    }

    public void linkToFavorite(View view) {
      //User in this page
    }

    public void linkToMyprofile(View view) {
        this.finish();
        startActivity(new Intent(FavoriteList.this,ProfileActivity.class));
    }

    public void linkToSpecialty(View view) {
        this.finish();
        startActivity(new Intent(FavoriteList.this, Specialty.class));
    }

    public void linkToSuggest(View view) {
        this.finish();
        startActivity(new Intent(FavoriteList.this, SuggestDoctorActivity.class));
    }

    public void AddList(View view) {

        ImageButton add = (ImageButton) findViewById(R.id.add);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("أدخل اسم القائمة");
                final EditText editText = new EditText(this);
                builder.setView(editText);
                builder.setPositiveButton("حفظ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(editText.getText().length()!=0){
                           sql= "Insert into List (ListName , User_ID) Values('"+editText.getText()+"' , '"+SaveLogin.getUserID(getApplicationContext())+"')";
                           Do();
                           dialog.cancel();
                        }
                        else {
                            editText.setError("يجب ملء الخانة");
                        }
                    }
                });
                builder.show();


    }
}
