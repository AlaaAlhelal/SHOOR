package com.shoor.shoor;
import com.shoor.shoor.R;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

public class SignIn extends AppCompatActivity {
    EditText email ;
    EditText pass ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }


    public void signIn(View view) {
        email = findViewById(R.id.email_field);
        pass = findViewById(R.id.pass_field);
    if (email.getText().equals(""))
        email.setError("Field cannot be left blank.");
    if (pass.getText().equals(""))
        pass.setError("Field cannot be left blank.");

    }



    protected String doInBackground(String... arg0) {
        try {

            String useremail = email.getText().toString();
            String password = pass.getText().toString();
            String link = "http://shoor.000webhostapp.com/login.php?useremail=" + useremail + "&password=" + password;

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(link));
            HttpResponse response = client.execute(request);
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer("");
            String line;

            while ((line = in.readLine()) != null) {
                sb.append(line);
                break;
            }

            in.close();
            return sb.toString();
        } catch (Exception e) {
            return "Exception: " + e.getMessage();

        }
    }

    protected void onPostExecute(String result){
        if(result.equals("Failed"))
        {
            AlertDialog.Builder ErrorMessage  = new AlertDialog.Builder(this);
            ErrorMessage.setMessage("wrong password or username");
            ErrorMessage.setTitle("Error Message...");
            ErrorMessage.setPositiveButton("OK", null);
            ErrorMessage.setCancelable(true);
            ErrorMessage.create().show();
            ErrorMessage.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            email.setText("");
                            pass.setText("");
                        }
                    });
        }
        else

            startActivity(new Intent(SignIn.this, Specialty.class));

    }



}
