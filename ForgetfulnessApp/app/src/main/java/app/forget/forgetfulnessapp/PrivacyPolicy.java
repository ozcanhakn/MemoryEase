package app.forget.forgetfulnessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PrivacyPolicy extends AppCompatActivity {
    TextView privacyPolicy;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_privacy_policy);


        privacyPolicy = findViewById(R.id.textPrivacy);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("privacy").child("policy");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String textPrivacy = snapshot.getValue(String.class);

                privacyPolicy.setText(textPrivacy);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}