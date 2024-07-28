package app.forget.forgetfulnessapp.Game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Locale;

import app.forget.forgetfulnessapp.R;

public class InstructionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_instructions);
        Locale locale = getResources().getConfiguration().locale;
        System.out.println(locale);

        Resources res = getBaseContext().getResources();
        res.updateConfiguration(new Configuration(), res.getDisplayMetrics());

        locale = getResources().getConfiguration().locale;
        System.out.println(locale);
    }

    public void onGoBackButtonClicked(View view) {
        finish();
    }
}