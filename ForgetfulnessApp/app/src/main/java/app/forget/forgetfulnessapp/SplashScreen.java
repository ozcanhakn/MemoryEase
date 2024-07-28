package app.forget.forgetfulnessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SplashScreen extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        NumberPicker numberPicker = findViewById(R.id.numberPickerHour);
        NumberPicker numberPickerMinute = findViewById(R.id.numberPickerMinute);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(userId);


        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(23);
        numberPicker.setValue(7);

        numberPickerMinute.setMinValue(0);
        numberPickerMinute.setMaxValue(59);

        numberPickerMinute.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d",i);
            }
        });

        numberPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d",i);

            }
        });
        numberPicker.setOnValueChangedListener(this);

        Button button = findViewById(R.id.button111);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedHour = numberPicker.getValue();

                saveToDatabase(selectedHour);
            }
        });
    }
    private void saveToDatabase(int selectedHour) {
        // Veritabanına ekleme işlemleri
        // Örnek olarak, veritabanında bir "hours" düğümü altında saat ve dakikayı kaydediyoruz
        databaseReference.child("hours").setValue(selectedHour);

    }
    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {

    }


}