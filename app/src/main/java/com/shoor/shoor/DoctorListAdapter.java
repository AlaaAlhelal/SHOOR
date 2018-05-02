package com.shoor.shoor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.ViewHolder> {

    private List<Doctor> doctorList;
    public static String Doc_Id;
    public static String Hos_ID;
    public Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView doc, hos;
        public RatingBar ratingBar;
        public  String DocID;
        public ViewHolder(View view) {
            super(view);
             doc = (TextView) view.findViewById(R.id.doctorName);
             hos = (TextView)           view.findViewById(R.id.hospitalName);
             ratingBar = (RatingBar) view.findViewById(R.id.DoctorRate);
        }
        public String getID(){return DocID;}
    }


    public DoctorListAdapter(List<Doctor> moviesList, Context cox) {
        this.doctorList = moviesList;
        context=cox;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_card_info, parent, false);




        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Doc_Id = doctorList.get(parent.indexOfChild(v)).getDoctor_ID();
                Hos_ID =doctorList.get(parent.indexOfChild(v)).getHospital_ID();

                SharedPreferences sharedpreferences = context.getSharedPreferences(Doc_Id, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("Doctor_ID",Doc_Id);
                editor.apply();
                SharedPreferences sharedpreferences2 = context.getSharedPreferences(Hos_ID, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = sharedpreferences2.edit();
                editor2.putString("Hospital_ID",Hos_ID);
                editor2.apply();
                Doctors.progressLayout.setVisibility(View.VISIBLE);
                Doctors.progressBar.setVisibility(View.VISIBLE);
                TimerTask timerTask =new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent =new Intent(context,DoctorProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity( intent);                    }
                };
                Timer timer =new Timer();
                timer.schedule( timerTask, (long) (0.02*60*1000));


            }});

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Doctor doc = doctorList.get(position);
        holder.doc.setText(doc.getDoctorName());
        holder.hos.setText(doc.getHospitalName());
        holder.ratingBar.setRating(doc.getAvgRate());
        holder.DocID = doc.getDoctor_ID();
        Doc_Id = doctorList.get(position).getDoctor_ID();
        Hos_ID =doctorList.get(position).getHospital_ID();

    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public  List<Doctor> getItemes(){return doctorList;}

    public void  setItemes(List<Doctor> newList){doctorList=newList;}


}
