package com.shoor.shoor;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class FavoriteListAdapter extends BaseAdapter {
    public Context context;
    public static String List_Id;
    public static  String List_Name;
    ArrayList<String> ListNames;
    ArrayList<String> ListIDs;
    boolean done = false;
    LayoutInflater inflter;

    public FavoriteListAdapter(Context applicationContext, ArrayList<String> Listnames ,ArrayList<String> Listids ) {
        this.context = applicationContext;
        ListNames = Listnames;
        ListIDs =Listids;
        //(LayoutInflater.from(applicationContext))
        inflter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return ListNames.size();
    }

    @Override
    public Object getItem(int i) {
        return ListNames.get(i);
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.favlist, null);
        TextView name = (TextView)           view.findViewById(R.id.Listname);
        final ImageButton editMenu = (ImageButton) view.findViewById(R.id.menuOption);

        name.setText(ListNames.get(i));

        List_Id = ListIDs.get(i);

        final int index =i;
        final View finalView = view;
        editMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(context, editMenu);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popupmenu, popup.getMenu());
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("تغيير الاسم"))
                        {
                            showDialog(index  , finalView);
                            if(done){
                                Intent intent =new Intent(finalView.getContext(), FavoriteList.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity( intent);
                            }
                        }
                        else
                        {
                            showAlertDialog(index  , finalView);
                            if(done)
                            {

                                Intent intent =new Intent(finalView.getContext(), FavoriteList.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity( intent);
                            }
                        }
                        return true;
                    }
                });
                popup.show();//showing popup menu
            }
        });



        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List_Id = ListIDs.get(index);
                List_Name = ListNames.get(index);

                SharedPreferences sharedpreferences = context.getSharedPreferences(List_Id, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("List_ID",List_Id);
                editor.apply();

                SharedPreferences sharedpreferences2 = context.getSharedPreferences(List_Name, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = sharedpreferences2.edit();
                editor2.putString("List_Name",List_Name);
                editor2.apply();

                Intent intent =new Intent(v.getContext(), FavListDocActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity( intent);
            }
        });

        return view;
    }



            public void query(String sqlq , String message){

                try{
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = DriverManager.getConnection(DB_Info.DB_URL,DB_Info.USER,DB_Info.PASS);
                    Statement stmt = conn.createStatement();
                    int rs = stmt.executeUpdate(sqlq);

                    if(rs==1){
                        Toast error = Toast.makeText(context,message,Toast.LENGTH_LONG);
                        error.show();
                        done= true;
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


            @SuppressLint("ResourceAsColor")
            public void showDialog(final int listID, View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                final EditText newListName = new EditText(context);
                newListName.setTextColor(R.color.colorAccent);
                builder.setTitle("تعديل الاسم").setView(newListName).setPositiveButton("حفظ" , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(!newListName.getText().equals("")){
                        query("Update List SET ListName=('"+newListName.getText()+"') WHERE List_ID="+ListIDs.get(listID) , "تم التعديل");
                        dialog.cancel();
                        }
                        else newListName.setError("يجب ملء الخانة");
                    }
                }).setNegativeButton("إلغاء" , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }

    public void showAlertDialog(final int listID , View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
        builder.setMessage("هل أنت متاكد من الحذف؟");
        builder.setPositiveButton("حذف" , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                query("DELETE FROM List WHERE List_ID="+ListIDs.get(listID) , "تم الحذف");
                dialog.cancel();
            }
        }).setNegativeButton("إلغاء" , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

}
