package app.forget.forgetfulnessapp.Example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import app.forget.forgetfulnessapp.Game.Game5x4Activity;
import app.forget.forgetfulnessapp.Game.Game6x5Activity;
import app.forget.forgetfulnessapp.Game.GameDifficultyActivity;
import app.forget.forgetfulnessapp.Game.GameMatch3Activity;
import app.forget.forgetfulnessapp.Game.InstructionsActivity;
import app.forget.forgetfulnessapp.Game.NewBoardActivity;
import app.forget.forgetfulnessapp.Game.SudokuActivity;
import app.forget.forgetfulnessapp.HomeScreen;
import app.forget.forgetfulnessapp.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class ExampleActivity extends AppCompatActivity {
    Button button5x4;
    Button button6x5;
    Button buttonMatch3;

    private boolean currentEnglish = true;
    private final String TAG = "MainActivity";

    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_example);

        checkCurrentLocale();


        back = findViewById(R.id.exampleBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExampleActivity.this, HomeScreen.class);
                startActivity(intent);
            }
        });
        button5x4 = (Button) findViewById(R.id.button1);
        button5x4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openGame5x4Activity();
            }
        });

        button6x5 = (Button) findViewById(R.id.button2);
        button6x5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openGame6x5Activity();
            }
        });

        buttonMatch3 = (Button) findViewById(R.id.button3);
        buttonMatch3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openGameMatch3Activity();
            }
        });
    }

    public void openGame5x4Activity(){
        Intent intent5x4 = new Intent(this, Game5x4Activity.class);
        startActivity(intent5x4);
    }

    public void openGame6x5Activity(){
        Intent intent6x5 = new Intent(this, Game6x5Activity.class);
        startActivity(intent6x5);
    }

    public void openGameMatch3Activity(){
        Intent intentMatch3 = new Intent(this, GameMatch3Activity.class);
        startActivity(intentMatch3);
    }
    private void checkCurrentLocale() {

        refreshViewLanguages();
    }
    private void refreshViewLanguages() {
        Log.i(TAG, "Refreshing View Languages");
        Button buttonStartNewGame = findViewById(R.id.buttonStartNewGame);
        buttonStartNewGame.setText(R.string.new_game);
        Button buttonAddNewBoard = findViewById(R.id.buttonAddNewBoard);
        buttonAddNewBoard.setText(R.string.add_new_board);

    }

    public void onStartNewGameButtonClicked(View view) {
        Intent intent = new Intent(ExampleActivity.this, GameDifficultyActivity.class);
        startActivity(intent);
    }

    public void onAddNewBoardButtonClicked(View view) {
        Intent intent = new Intent(ExampleActivity.this, NewBoardActivity.class);
        startActivity(intent);
    }

    public void onShowInstructionsButtonClicked(View view) {
        Intent intent = new Intent(ExampleActivity.this, InstructionsActivity.class);
        startActivity(intent);
    }
}