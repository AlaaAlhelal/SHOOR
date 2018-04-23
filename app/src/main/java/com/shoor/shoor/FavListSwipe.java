package com.shoor.shoor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class FavListSwipe extends RecyclerView.Adapter<FavListSwipe.ViewHolder> {

        private List<Doctor> doctorList;
        public static String Doc_Id;
        public static String Hos_ID;
        public Context context;
        public String listID;
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


        public FavListSwipe(List<Doctor> moviesList, Context cox) {
            this.doctorList = moviesList;
            context=cox;
        }

        @Override
        public FavListSwipe.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            final View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.favlistswipe, parent, false);



            LinearLayout info = (LinearLayout)itemView.findViewById(R.id.docInfo);
            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Doc_Id = doctorList.get(parent.indexOfChild(itemView)).getDoctor_ID();
                    Hos_ID =doctorList.get(parent.indexOfChild(itemView)).getHospital_ID();

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
                }});


//Swipe to delete

            SwipeLayout swipeLayout =  (SwipeLayout)itemView.findViewById(R.id.sample1);

            //set show mode.
            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

            swipeLayout.addDrag(SwipeLayout.DragEdge.Left, itemView.findViewById(R.id.bottom_wrapper));

            swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onClose(SwipeLayout layout) {
                    //when the SurfaceView totally cover the BottomView.
                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                    //you are swiping.
                }

                @Override
                public void onStartOpen(SwipeLayout layout) {

                }

                @Override
                public void onOpen(final SwipeLayout layout) {
                    layout.findViewById(R.id.bottom_wrapper).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String docid = doctorList.get(parent.indexOfChild(itemView)).getDoctor_ID();
                            SharedPreferences sharedpreferences = context.getSharedPreferences(FavoriteListAdapter.List_Id, Context.MODE_PRIVATE);
                             listID = sharedpreferences.getString("List_ID","");
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                            builder.setMessage("هل أنت متاكد من الحذف؟");
                            builder.setPositiveButton("حذف" , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                            query("DELETE FROM listofdoctors WHERE List_ID='"+listID+"' AND Doctor_ID='"+docid+"' AND User_ID='"+SaveLogin.getUserID(context)+"'", "تم الحذف");
                            dialog.cancel();
                                    Intent intent =new Intent(context,FavListDocActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity( intent);
                                }
                            }).setNegativeButton("إلغاء" , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();

                        }
                    });
                }

                @Override
                public void onStartClose(SwipeLayout layout) {

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                    //when user's hand released.
                }
            });









            return new FavListSwipe.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(FavListSwipe.ViewHolder holder, int position) {
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

    public void query(String sqlq , String message){

        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_Info.DB_URL,DB_Info.USER,DB_Info.PASS);
            Statement stmt = conn.createStatement();
            int rs = stmt.executeUpdate(sqlq);

            if(rs==1){
                Toast error = Toast.makeText(context,message,Toast.LENGTH_LONG);
                error.show();
            }
            stmt.close();
            conn.close();

        }//end try
        catch (SQLException sqle){
            Toast error = Toast.makeText(context,sqle.getMessage(),Toast.LENGTH_LONG);
            error.show();
        }
        catch (Exception e){Toast error = Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG);
            error.show();
        }

    }

}
