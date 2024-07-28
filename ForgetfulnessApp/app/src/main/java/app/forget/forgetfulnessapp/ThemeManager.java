package app.forget.forgetfulnessapp;

import android.content.Context;
import android.content.SharedPreferences;

import app.forget.forgetfulnessapp.SettingsActivity.AppearanceActivity;

public class ThemeManager {
    SharedPreferences mySharedPref;

    public ThemeManager(Context context){
        mySharedPref = context.getSharedPreferences("fileName",Context.MODE_PRIVATE);

    }
    public void setNightModeState(Boolean state){
        SharedPreferences.Editor editor = mySharedPref.edit();
        editor.putBoolean("LightMode",state);
        editor.commit();
    }
    public Boolean loadNightModeState(){
        Boolean state = mySharedPref.getBoolean("LightMode",false);
        return state;
    }


}
