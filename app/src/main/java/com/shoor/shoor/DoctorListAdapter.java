package com.shoor.shoor;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;


public class DoctorListAdapter extends BaseAdapter {
        public Context context;
        public static String Doc_Id;
        public static String Hos_ID;
        ArrayList<Doctor> Doctors;
        LayoutInflater inflter;
        public static ProgressDialog progress;

        public DoctorListAdapter(Context applicationContext, ArrayList<Doctor> doctorArrayList) {
            this.context = applicationContext;
            Doctors = doctorArrayList;
            //(LayoutInflater.from(applicationContext))
            inflter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return Doctors.size();
        }

        @Override
        public Object getItem(int i) {
            return Doctors.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
        @SuppressLint("ViewHolder")
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.doctor_card_info, null);
            TextView doc = (TextView)           view.findViewById(R.id.doctorName);
            TextView hos = (TextView)           view.findViewById(R.id.hospitalName);
            ImageView icon = (ImageView) view.findViewById(R.id.Doc_pic);
            RatingBar ratingBar = (RatingBar) view.findViewById(R.id.DoctorRate);
            ratingBar.setRating(Doctors.get(i).getAvgRate());
            doc.setText(Doctors.get(i).getDoctorName());
            hos.setText(Doctors.get(i).getHospitalName());
            icon.setBackgroundResource(R.drawable.doctor_pic);
            Doc_Id = Doctors.get(i).getDoctor_ID();
            Hos_ID =Doctors.get(i).getHospital_ID();


            final int index =i;
            final View finalView = view;
            view.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        Doc_Id = Doctors.get(index).getDoctor_ID();
        Hos_ID =Doctors.get(index).getHospital_ID();

        SharedPreferences sharedpreferences = context.getSharedPreferences(Doc_Id, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("Doctor_ID",Doc_Id);
        editor.apply();
        SharedPreferences sharedpreferences2 = context.getSharedPreferences(Hos_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedpreferences2.edit();
        editor2.putString("Hospital_ID",Hos_ID);
        editor2.apply();
        Intent intent =new Intent(v.getContext(),DoctorProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity( intent);
    }
});
            return view;
        }


}
