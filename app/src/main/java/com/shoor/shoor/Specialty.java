package com.shoor.shoor;

import android.os.StrictMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Specialty {
    private String ID;
    private String Name;
    private ArrayList<Doctor> ListOfDoctors;
    public Specialty(String ID, String name) {
        this.ID = ID;
        Name = name;
        ListOfDoctors=new ArrayList<>();

    }

    public Specialty(String ID) {
        this.ID = ID;
    }

    public Specialty() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public ArrayList<Doctor> getListOfDoctors(){return ListOfDoctors;}

    public boolean AddSpecialty(String Name){
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
            sql = "INSERT INTO specialties (SpecialtiesName) Values('" + Name + "')";
            int rs = stmt.executeUpdate(sql);

            if(rs==1){
                stmt.close();
                conn.close();
              return true;
            }
            else
            {
               return false;
            }



        }catch(SQLException se){
            return false;
        }catch(Exception e){
            return false;
        }


    }//end Add Specialty

    public boolean EditSpecialty(String Name){
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
            sql = "UPDATE specialties SET SpecialtiesName = ('" + Name + "')WHERE Specialties_ID=('" + getID() + "')";
            int rs = stmt.executeUpdate(sql);

            if(rs==1){
                this.setName(Name);
                stmt.close();
                conn.close();
                return true;
            }
            else
            {
              return false;
        }


        } catch (SQLException se) {
            return false;

        } catch (Exception e) {
            return false;

        }
    }

    public boolean DeleteSpecialty(){
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
            sql = "DELETE FROM specialties WHERE Specialties_ID=('" + getID() + "')";

            int rs = stmt.executeUpdate(sql);

            if(rs==1){
                stmt.close();
                conn.close();
                return true;
            }
            else
            {
                return false;
            }


        } catch (SQLException se) {
            return false;

        } catch (Exception e) {
            return false;

        }
    }

    public ArrayList<Doctor> SelectSpecialty(String Name){

        try{
            //VERY IMPORTANT LINES' to build thread ploicy to allow jdbc to connect with the server'
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_Info.DB_URL,DB_Info.USER,DB_Info.PASS);
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM doctor where Specialties_ID='" + this.getID() + "' ORDER BY Doctor_ID DESC";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                String hosname="", PhoneNo="---";
                float lat=24 , lng=46 , HosAvgRate=0;
                Statement stmt2 = conn.createStatement();
                String sql2  = "SELECT * FROM hospital where Hospital_ID="+rs.getInt("Hospital_ID");
                ResultSet rs2 = stmt2.executeQuery(sql2);
                String hos_ID = rs.getString("Hospital_ID");
                String Doc_id = rs.getString("Doctor_ID");
                String doc_name=rs.getString("DoctorName");
                float ratingscore = rs.getFloat("AvgRate");
                String officeHours = rs.getString("OfficeHours");
                double price = rs.getDouble("Price");
                while (rs2.next()) {
                    hosname =rs2.getString("HospitalName");
                    lat = rs2.getFloat("Location_V1");
                    lng = rs2.getFloat("Location_V2");
                    PhoneNo=rs2.getString("PhoneNumber");
                    HosAvgRate=rs2.getFloat("AvgRate");
                }
                Hospital hos = new Hospital(hos_ID,hosname,lat,lng,PhoneNo,HosAvgRate);
                Doctor doc  = new Doctor(Doc_id, doc_name, hos ,ratingscore,officeHours, price);
                doc.setSpecialty(new Specialty(getID(),Name));
                ListOfDoctors.add(doc);
                rs2.close();
                stmt2.close();
            }
            rs.close();
            stmt.close();
            conn.close();

        }//end try
        catch (SQLException sqle){
            return null;
        }
        catch (Exception e){
            return null;
            }

            return getListOfDoctors();
    }



}
