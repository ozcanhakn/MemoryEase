package app.forget.forgetfulnessapp.SettingsActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import app.forget.forgetfulnessapp.HomeScreen;
import app.forget.forgetfulnessapp.R;

public class WhatsNewActivity extends AppCompatActivity {
    ImageView backtoBack;
    RelativeLayout backToMainScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_whats_new);

        backtoBack = findViewById(R.id.backToButton);
        backToMainScreen = findViewById(R.id.backToMainScreen);

        backToMainScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WhatsNewActivity.this, HomeScreen.class);
                startActivity(intent);
            }
        });

        backtoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WhatsNewActivity.this, HomeScreen.class);
                startActivity(intent);
            }
        });
    }
}