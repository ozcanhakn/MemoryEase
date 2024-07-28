package app.forget.forgetfulnessapp.SettingsActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;

import app.forget.forgetfulnessapp.MainActivity;
import app.forget.forgetfulnessapp.R;
import app.forget.forgetfulnessapp.ThemeManager;
import app.forget.forgetfulnessapp.Utils.ThemePreferences;

public class AppearanceActivity extends AppCompatActivity {
    ImageView backButtonAppearance;
    ThemeManager sharedPref;
    ConstraintLayout matchSunshineConstraint,matchDarkConstraint;
    SwitchCompat aSwitch;
    TextView textAppearance, textChoose,textMatchSystem,
            textLight,textDark;

    ImageView checkDark, checkLight, checkSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (ThemePreferences.isLightModeEnabled(this)) {
            setTheme(R.style.AppTheme);
            Log.d("LOG105", "onCreate: Uygulama Aydınlık Modda");
        } else {
            setTheme(R.style.AppThemeNight);
            Log.d("LOG105", "onCreate: Uygulama Karanlık Modda");

        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



        setContentView(R.layout.activity_appearance);


        checkDark = findViewById(R.id.constraintDarkCheck);
        checkLight = findViewById(R.id.constraintLightCheck);
        checkSystem = findViewById(R.id.constraintCheck);




        backButtonAppearance = findViewById(R.id.backButtonAppearance);
        matchSunshineConstraint = findViewById(R.id.matchSunshineConstraint);
        matchDarkConstraint  = findViewById(R.id.matchDarkConstraint);
        textAppearance = findViewById(R.id.textAppearance);

        textChoose = findViewById(R.id.textChoose);
        textMatchSystem = findViewById(R.id.textMatchSystem);
        textLight = findViewById(R.id.textLight);
        textDark = findViewById(R.id.textDark);


        textMatchSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Akşam 7'den sonrasını kontrol et
                Calendar cal = Calendar.getInstance();
                int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);

                if (hourOfDay >= 19) {
                    // Açık mod
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                } else {
                    // Karanlık mod
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                }
                checkSystem.setVisibility(View.VISIBLE);
                checkDark.setVisibility(View.INVISIBLE);
                checkLight.setVisibility(View.INVISIBLE);

            }
        });

        matchSunshineConstraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);


                checkSystem.setVisibility(View.INVISIBLE);
                checkDark.setVisibility(View.INVISIBLE);
                checkLight.setVisibility(View.VISIBLE);

            }
        });

        matchDarkConstraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                checkSystem.setVisibility(View.INVISIBLE);
                checkDark.setVisibility(View.VISIBLE);
                checkLight.setVisibility(View.INVISIBLE);
            }
        });





        backButtonAppearance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppearanceActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });




    }



}