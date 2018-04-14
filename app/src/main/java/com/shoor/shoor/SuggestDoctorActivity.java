package com.shoor.shoor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Transport;

public class SuggestDoctorActivity extends AppCompatActivity {
    public EditText Doctor_name ;
    public EditText Hosital_name ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_doctor);
        Doctor_name = findViewById(R.id.Suggest_Doctor_Name);
        Hosital_name= findViewById(R.id.Suggest_Doctor_Hospital);
    }

    public void linkToMyprofile(View view) {
        this.finish();
        startActivity(new Intent(SuggestDoctorActivity.this,ProfileActivity.class));

    }

    public void linkToFavorite(View view) {
        this.finish();
        startActivity(new Intent(SuggestDoctorActivity.this,FavoriteList.class));

    }

    public void linkToSpecialty(View view) {
        this.finish();
        startActivity(new Intent(SuggestDoctorActivity.this,Specialty.class));

    }

    public void linkToSuggest(View view) {
        //user in this page
    }

    public void Send_Suggest(View view) {
       String docName = Doctor_name.getText().toString();
       String HosName = Hosital_name.getText().toString();

       if(docName.equals(""))
           Doctor_name.setError("يجب ملء الخانة");
       if(HosName.equals(""))
           Hosital_name.setError("يجب ملء الخانة");
        if(!docName.equals("") && !HosName.equals("")){

                   // Recipient's email ID needs to be mentioned.
                   String to = "shoorapp@gmail.com";

                    //subject
                   String title="اقتراح جديد";
                   //body
                   String body= "تم إرسال إقتراح جديد ..." + "\n"+"اسم الطبيب : "+docName+"\n"+"اسم المستشفى : "+HosName;

                   //host or IP
                   String SMTP_SERVER = "smtp.gmail.com";
                   //Get the session object
                   Properties properties = System.getProperties();
                   properties.put("mail.smtp.host", SMTP_SERVER);
                   properties.put("mail.smtp.password", "SHOOR1439");
                   properties.put("mail.smtp.starttls.enable", "true");  // needed for gmail
                   properties.put("mail.smtp.auth", "true"); // needed for gmail
                   properties.put("mail.smtp.port", "587");  // gmail smtp port
                   Session session = Session.getInstance(properties,
                           new javax.mail.Authenticator() {
                               protected PasswordAuthentication getPasswordAuthentication() {
                                   return new PasswordAuthentication("shoorapp@gmail.com", "SHOOR1439");
                               }
                           });
                   //compose the message
                   try{
                       MimeMessage message = new MimeMessage(session);

                       message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
                       message.setSubject(title);
                       message.setText(body);
                       Transport transport = session.getTransport("smtp");

                       // Send message
                       Transport.send(message);
                       //clear
                       Toast errorToast = Toast.makeText(SuggestDoctorActivity.this, "تم إرسال اقتراحك", Toast.LENGTH_SHORT);
                       errorToast.show();
                       Doctor_name.setText("");
                       Hosital_name.setText("");

                   }catch (MessagingException mex) {
                       Toast errorToast = Toast.makeText(SuggestDoctorActivity.this, ""+mex.getMessage(), Toast.LENGTH_SHORT);
                       errorToast.show();
                   }




               }//end if

    }
}
