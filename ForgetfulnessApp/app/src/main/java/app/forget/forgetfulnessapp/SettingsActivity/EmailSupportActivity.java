package app.forget.forgetfulnessapp.SettingsActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import app.forget.forgetfulnessapp.MailSender.EmailSender;
import app.forget.forgetfulnessapp.MainActivity;
import app.forget.forgetfulnessapp.R;

public class EmailSupportActivity extends AppCompatActivity {
    ImageView emailSupportCancelIcon;
    TextView emailSupport;

    EditText userName,userMail,userDesc;
    RelativeLayout buttonSent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_email_support);



        emailSupport = findViewById(R.id.textUpperEmailSupport);

        //Kullanıcıdan alınan veriler
        userName = findViewById(R.id.eMailSupportName);
        userMail = findViewById(R.id.emailSupportMail);
        userDesc = findViewById(R.id.eMailSupportDesc);
        buttonSent = findViewById(R.id.buttonSent);


        buttonSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSendMail();
            }
        });




        






        emailSupportCancelIcon = findViewById(R.id.emailSupportCancelIcon);
        emailSupportCancelIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmailSupportActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void eMailSender(){



    }
    private void userSendMail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        String eMail = userMail.getText().toString();
        String name = userName.getText().toString();
        String message = userDesc.getText().toString();



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Mail").child(userId);

        Map<String, Object> mail = new HashMap<>();
        mail.put("name",name);
        mail.put("mail",eMail);
        mail.put("message", message);

        reference.setValue(mail, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                if (error == null){
                    Toast.makeText(EmailSupportActivity.this, "Successfully Sent", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(EmailSupportActivity.this, "Sorry, we encountered an error, try again later", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}