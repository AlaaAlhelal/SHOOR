package com.shoor.shoor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static  ArrayList<String> ListsName  ;
    public static   ArrayList<String> ListsID ;

    private String mParam1;
    private String mParam2;

    public static String userID;
    public static String Doctor_ID;
    private OnFragmentInteractionListener mListener;

    public ListFragment() {
        // Required empty public constructor
    }



    public static ListFragment newInstance(String param1, String param2, ArrayList<String> names ,ArrayList<String>  ids, String userid, String doctor_ID) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        ListsID=ids;
        ListsName=names;
        userID =userid;
        Doctor_ID =doctor_ID;
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =   inflater.inflate(R.layout.fragment_list, container, false);
        ListView listView =(ListView) v.findViewById(R.id.ListNames);
        ArrayAdapter AdapterList = new ArrayAdapter<String>(v.getContext(), R.layout.list_names, R.id.ListName, ListsName );
        listView.setAdapter((android.widget.ListAdapter) AdapterList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try{
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = DriverManager.getConnection(DB_Info.DB_URL,DB_Info.USER,DB_Info.PASS);
                    Statement stmt = conn.createStatement();
                    String sql  = "Insert into ListOfDoctors ( List_ID,  User_ID, Doctor_ID ) Values( '"+ListsID.get(position)+"' , '"+userID+"' , '"+Doctor_ID+"' )";
                    int rs = stmt.executeUpdate(sql);
                    if(rs==1) {
                        Toast error = Toast.makeText(getContext(), "تمت الإضافة", Toast.LENGTH_LONG);
                        error.show();

                        //end fragment
                        closeFragment();
                    }
                    else {
                        Toast error = Toast.makeText(getContext() ,"حدثت مشكلة حاول لاحقاً" ,Toast.LENGTH_LONG);
                        error.show();
                    }
                    stmt.close();
                    conn.close();
                }//end try
                catch (SQLException sqle){
                    Toast error = Toast.makeText(getContext() ,sqle.getMessage() ,Toast.LENGTH_LONG);
                    error.show();
                } catch (ClassNotFoundException e) {
                    Toast error = Toast.makeText(getContext() ,e.getMessage() ,Toast.LENGTH_LONG);
                    error.show();
                }
            }
        });
        ImageButton imgB = (ImageButton) v.findViewById(R.id.AddList);
        imgB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("أدخل اسم القائمة");
                final EditText view = new EditText(getContext());
                builder.setView(view);
                builder.setPositiveButton("حفظ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(view.getText().length()!=0){
                            try{
                                Class.forName("com.mysql.jdbc.Driver");
                                Connection conn = DriverManager.getConnection(DB_Info.DB_URL,DB_Info.USER,DB_Info.PASS);
                                String sql  = "Insert into List (ListName , User_ID) Values('"+view.getText()+"' , '"+userID+"')";
                                PreparedStatement  stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                                int rs = stmt.executeUpdate(sql);
                                ResultSet List_ID = stmt.getGeneratedKeys();
                                if(rs==1){
                                    while (List_ID.next()) {
                                        Statement stmt3 = conn.createStatement();
                                        String sql3 = "Insert into ListOfDoctors (List_ID,  User_ID, Doctor_ID ) Values('" + List_ID.getInt(1) + "' , '" + userID + "' , '" + Doctor_ID + "')";
                                        int rs3 = stmt3.executeUpdate(sql3);
                                        Toast error = Toast.makeText(getContext(), "تمت الإضافة", Toast.LENGTH_LONG);
                                        error.show();
                                    }
                                }
                                stmt.close();
                                conn.close();
                            }//end try
                            catch (SQLException sqle){
                                Toast error = Toast.makeText(getContext() ,sqle.getMessage() ,Toast.LENGTH_LONG);
                                error.show();
                            } catch (ClassNotFoundException e) {
                                Toast error = Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);
                                error.show();
                            }
                            //end fragment
                            closeFragment();
                        }
                        else {
                            view.setError("يجب ملء الخانة");
                        }
                    }
                });
                builder.show();

            }
        });
        return v;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void closeFragment(){
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();                    }

}
