package br.com.quixada.aniheart.persistence;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ContextoLocalDataSource {

    public static void limparContexto(Activity activity){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        sharedPref.edit().remove("email");
        sharedPref.edit().remove("name");
    }


    public static String getEmail(Activity activity) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        //SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String email = sharedPref.getString("email", "");
        return email;
    }

    public static void setEmail(String email, Activity activity) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);

        //SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", email);
        editor.commit();
    }

    public static void removerEmail(Activity activity){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        sharedPref.edit().remove("email");
    }

    public static String getName(Activity activity) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        //SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String email = sharedPref.getString("name", "");
        return email;
    }

    public static void setName(String name, Activity activity) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);

        //SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name", name);
        editor.commit();
    }

    public static void removerName(Activity activity){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        sharedPref.edit().remove("name");
    }


}
