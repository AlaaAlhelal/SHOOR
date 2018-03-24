package com.shoor.shoor;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Doctors extends AppCompatActivity {

    ArrayList<Doctor> doctor = new ArrayList<Doctor>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);
        ScrollView View = (ScrollView) findViewById(R.id.sview);
        RetriveData();


    }

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
            sql = "SELECT * FROM doctor";
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set

            while (rs.next()) {
                String docid = rs.getString("Doctor_ID");
                String hos = rs.getString("Hospital_ID");
                String dep = rs.getString("Specialties_ID");
                String rat = rs.getString("AvgRate");
                String prs = rs.getString("Price");
                String docName = rs.getString("DoctorName");
                String officeHoures = rs.getString("OfficeHours");

                int doc_id = Integer.parseInt(docid);
                int hos_id = Integer.parseInt(hos);
                int dep_id = Integer.parseInt(dep);

                double rate = Double.parseDouble(rat);
                double price = Double.parseDouble(prs);

                Doctor doc = new Doctor(doc_id, hos_id, dep_id, rate, price, docName, officeHoures);
                doctor.add(doc);
            }

            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(Doctors.this, "يجب أن تكون متصلاً بالإنترنت", Toast.LENGTH_SHORT);
            errorToast.show();
        } catch (Exception e) {
            //SHOW SERVER FAILED MESSAGE
            Toast errorToast = Toast.makeText(Doctors.this, "" + e.getMessage(), Toast.LENGTH_SHORT);
            errorToast.show();
        }
    }//End RetriveData

    class coustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return doctor.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return null;
        }
    }
}
