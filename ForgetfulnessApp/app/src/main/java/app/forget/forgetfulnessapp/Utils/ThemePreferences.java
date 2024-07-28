package app.forget.forgetfulnessapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemePreferences {
    private static final String PREF_NAME = "ThemePreferences";
    private static final String KEY_IS_LIGHT_MODE_ENABLED = "isLightModeEnabled";

    public static boolean isLightModeEnabled(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_IS_LIGHT_MODE_ENABLED, false);
    }

    public static void setLightModeEnabled(Context context, boolean isEnabled) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_IS_LIGHT_MODE_ENABLED, isEnabled);
        editor.apply();
    }


}
