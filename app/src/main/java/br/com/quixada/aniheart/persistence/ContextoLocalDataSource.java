package br.com.quixada.aniheart.persistence;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ContextoLocalDataSource {

    static final String EMAIL = "email";

    public static void limparContexto(Activity activity){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        sharedPref.edit().remove(EMAIL);
        sharedPref.edit().remove("name");
    }


    public static String getEmail(Activity activity) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        return sharedPref.getString(EMAIL, "");
    }

    public static void setEmail(String email, Activity activity) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(EMAIL, email);
        editor.commit();
    }

    public static void removerEmail(Activity activity){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        sharedPref.edit().remove(EMAIL);
    }

    public static String getName(Activity activity) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        return sharedPref.getString("name", "");
    }

    public static void setName(String name, Activity activity) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name", name);
        editor.commit();
    }

    public static void removerName(Activity activity){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        sharedPref.edit().remove("name");
    }

    private ContextoLocalDataSource() {
    }
}

