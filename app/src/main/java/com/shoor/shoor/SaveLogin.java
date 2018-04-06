package com.shoor.shoor;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveLogin {
    static final String PREF_USER_ID= "user_id";
    static final String PREF_ADMIN_ID="admin_id";
        static SharedPreferences getSharedPreferences(Context ctx) {
            return PreferenceManager.getDefaultSharedPreferences(ctx);
        }

        public static void setUserID(Context ctx, String userName)
        {
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            editor.putString(PREF_USER_ID, userName);
            editor.commit();
        }
        public static String getUserID(Context ctx)
        {
            return getSharedPreferences(ctx).getString(PREF_USER_ID, "");
        }

            public static String getAdminId(Context ctx)
            {
                return getSharedPreferences(ctx).getString(PREF_USER_ID, "");
            }

        public static void setAdminId(Context ctx, String userName)
        {
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            editor.putString(PREF_USER_ID, userName);
            editor.commit();
        }


}
