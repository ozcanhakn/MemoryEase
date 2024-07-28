package app.forget.forgetfulnessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.rxjava3.annotations.NonNull;

public class ForgotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        final FirebaseAuth auth = FirebaseAuth.getInstance();

        final EditText emailEditText = findViewById(R.id.emailEdittextForgotPass); // EditText alanı için id'yi ayarlayın
        RelativeLayout resetPasswordButton = findViewById(R.id.continueButtonForgot); // Şifre sıfırlama butonu iç


        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = emailEditText.getText().toString();

                if (!TextUtils.isEmpty(emailAddress)) {
                    auth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Şifre sıfırlama e-postası başarıyla gönderildi
                                        Log.d("TAG", getString(R.string.forgotemailsent));
                                        Toast.makeText(ForgotPassword.this, "Şifre sıfırlama e-postası gönderildi.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Şifre sıfırlama e-postası gönderilemedi
                                        Log.d("TAG", "Şifre sıfırlama e-postası gönderilemedi: " + task.getException().getMessage());
                                        Toast.makeText(ForgotPassword.this, getString(R.string.forgotemailnotsent) + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(ForgotPassword.this, getString(R.string.entermail), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}