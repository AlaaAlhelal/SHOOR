package com.shoor.shoor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User {
    private String User_ID;
    private String User_Email;
    private String Password;
    private String Gender;
    private String UserName;

    public User(String user_ID, String user_Email, String password, String gender, String userName) {
        User_ID = user_ID;
        User_Email = user_Email;
        Password = password;
        Gender = gender;
        UserName = userName;
    }

    public User() {
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }

    public String getUser_Email() {
        return User_Email;
    }

    public void setUser_Email(String user_Email) {
        User_Email = user_Email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public  void Logout(){
        //clear id
        SignIn.user_id="";
        SignIn.user=null;
        SignUp.user=null;
    }

    public String Login(String password, String UserEmail){

        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            Connection conn = DriverManager.getConnection(DB_Info.DB_URL, DB_Info.USER, DB_Info.PASS);

            Statement stmt2 = conn.createStatement();
            String sql = "SELECT * FROM user where UserEmail='"+UserEmail+"' AND Password='"+password+"'";
            ResultSet result = stmt2.executeQuery(sql);
            int count = 0;
            String user_id=null;
            while (result.next()) {
                user_id = result.getString(1);
                User_ID = user_id;
                User_Email = UserEmail;
                Password = password;
                Gender = result.getString("Gender");
                UserName = result.getString("UserName");
                ++count;
            }

            if (count == 1) {
                result.close();
                stmt2.close();
                conn.close();
                return user_id;
            } else {
                return null;
            }
        }catch(SQLException se){
            System.out.println("ex:*************"+se.getMessage());
            return null;

        }catch(Exception e){
            return null;

        }

    }

    public String Register(String user_Email, String password, String gender, String userName){
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
            String query = "SELECT * FROM admin where Email='" + user_Email + "'";
            ResultSet resultset = statm.executeQuery(query);
            int isthere = 0;
            while (resultset.next()) {
                isthere++;
            }
            if (isthere == 1) {
                return null;

            } else {

                //STEP 4: Execute a query
                stmt = conn.createStatement();
                String sql, user_id=null;

                sql = "INSERT INTO User (UserEmail, UserName, Password, Gender) Values('" + user_Email + "','" + userName + "','" + password + "','" + gender + "')";
                int rs = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
                ResultSet keys = stmt.getGeneratedKeys();
                stmt.close();
                if (rs == 1) {//num of row affected
                    //set session user_id
                    while (keys.next()) {
                        user_id = keys.getString(1);
                    }
                    User_ID=user_id;
                    User_Email=user_Email;
                    Gender=gender;
                    UserName=userName;
                    Password=password;

                    keys.close();
                    stmt.close();
                    resultset.close();
                    statm.close();
                    conn.close();
                    return user_id;
                } else {
                    return null;
                }


            }
        }catch(SQLException se){
            return null;
        }catch(Exception e){
            return null;
        }
    }

    public void EditProfileUser(String UserName, String UserEmail,String Gender){
    User_Email=UserEmail;
    this.Gender=Gender;
    this.UserName=UserName;
    }


}
