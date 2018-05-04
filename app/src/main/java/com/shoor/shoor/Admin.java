package com.shoor.shoor;

import android.content.Context;
import android.content.SharedPreferences;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Admin {

    private String Admin_ID;
    private String Password;
    private String AdminEmail;

    public Admin() {
    }

    public Admin(String password, String adminEmail) {
        Password = password;
        AdminEmail = adminEmail;
    }

    public Admin(String admin_ID, String password, String adminEmail) {
        Admin_ID = admin_ID;
        Password = password;
        AdminEmail = adminEmail;
    }

    public String getAdmin_ID() {
        return Admin_ID;
    }

    public void setAdmin_ID(String admin_ID) {
        Admin_ID = admin_ID;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getAdminEmail() {
        return AdminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        AdminEmail = adminEmail;
    }

    public String Login(String password, String adminEmail){

        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            Connection conn = DriverManager.getConnection(DB_Info.DB_URL, DB_Info.USER, DB_Info.PASS);

            Statement stmt2 = conn.createStatement();
            String sql2 = "SELECT * FROM admin where Email='" + adminEmail + "' AND Password='" + password + "'";
            ResultSet result = stmt2.executeQuery(sql2);
            int count2 = 0;
            String admin_id=null;
            while (result.next()) {
                admin_id = result.getString(1);
                ++count2;
            }

            if (count2 == 1) {
                result.close();
                stmt2.close();
                conn.close();
                    return admin_id;
            } else {
                return null;
            }
        }catch(SQLException se){
            return null;

        }catch(Exception e){
            return null;

        }
    }

    public  void Logout(){
        //clear id
        SignIn.admin_id="";
    }
}

