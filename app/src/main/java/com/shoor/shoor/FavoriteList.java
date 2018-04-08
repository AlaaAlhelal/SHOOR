package com.shoor.shoor;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sun.mail.imap.protocol.ID;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class FavoriteList extends AppCompatActivity {

    public ArrayList<String> Names = new ArrayList<String>();
    public ArrayList<String> IDs = new ArrayList<String>();
    Button  edit;
    ListView lists ;
    FavoriteListAdapter AdapterList ;
    String sqlEdit ; // for the sql ;
    String editList ; // the new entered name
    int ListID ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        // edit list
        edit =(Button)findViewById(R.id.edit);

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

    public void editList () {
        lists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position , long id) {
                showInputBox(Names.get(position), IDs.get(position), position);
            }
        });
    }

    public void showInputBox(String oldList ,String Id ,final int position){
        ListID = Integer.parseInt(Id);
    final Dialog dialog = new Dialog (FavoriteList.this);
    dialog.setTitle("تعديل اسم القائمة ");
    dialog.setContentView(R.layout.input_box);
    TextView txtmessage = (TextView) dialog.findViewById(R.id.txtmessage);
    txtmessage.setText("تعديل الاسم ");
    txtmessage.setTextColor(Color.parseColor("@ff2222"));
    final EditText editText= (EditText)dialog.findViewById(R.id.txtinput);
    editText.setText(oldList);
    Button doedit = (Button) dialog.findViewById(R.id.DoEdit);
    doedit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            editList = editText.getText().toString() ;
            Names.set(position,editList);
            sqlEdit = "UPDATE list SET ListName = ('" + editList + "')WHERE List_ID=('" + ListID + "')";
            Do() ;
            AdapterList.notifyDataSetChanged();
            dialog.dismiss();

        }
    });
        Button delete = (Button) dialog.findViewById(R.id.Delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlEdit = "DELETE FROM list WHERE List_ID=('" + ListID + "')";
                Do() ;
                AdapterList.notifyDataSetChanged();
                dialog.dismiss();

            }
        });
     dialog.show();
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
                int rs = stmt.executeUpdate(sqlEdit);

                if(rs==1){
                    Toast done = Toast.makeText(FavoriteList.this, "تم التعديل", Toast.LENGTH_SHORT);
                    done.show();

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
        this.finish();
        startActivity(new Intent(FavoriteList.this,FavoriteList.class));

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

}
