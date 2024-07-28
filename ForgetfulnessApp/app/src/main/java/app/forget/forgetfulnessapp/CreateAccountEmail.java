package app.forget.forgetfulnessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class CreateAccountEmail extends AppCompatActivity {
    RelativeLayout continueButton, textLogin;
    EditText emailEdittext, edittextPassword;
    TextView loginText;
    FirebaseAuth firebaseAuth;

    //AIzaSyAZMsbmM0dBJeKi0dyh9tjMxp_gAexmjMA
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_account_email);


        loginText = findViewById(R.id.textLogIn);
        emailEdittext = findViewById(R.id.emailEdittext);
        edittextPassword = findViewById(R.id.passwordEdittext);
        continueButton = findViewById(R.id.continueButton);
        // Kullanıcının girdiği e-posta adresini alın


        firebaseAuth = FirebaseAuth.getInstance();


        init();

    }

    private void init() {
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateAccountEmail.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        // Continue butonuna tıklama işlemini dinle
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kullanıcının girdiği e-posta adresini alın
                String email = emailEdittext.getText().toString().trim();

                // E-posta adresi boş mu kontrol et
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.please_enter_mail), Toast.LENGTH_SHORT).show();
                    return;
                }

                // Firebase Authentication ile e-posta adresinin daha önce kaydolup olmadığını sorgula
                FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                if (task.isSuccessful()) {
                                    SignInMethodQueryResult result = task.getResult();
                                    List<String> signInMethods = result.getSignInMethods();

                                    if (signInMethods != null && signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)) {
                                        // E-posta adresi ile kayıt olunmuşsa, kullanıcıyı uyarmak veya giriş yapmasını sağlamak isterseniz burada gerekli işlemleri yapabilirsiniz.
                                        Toast.makeText(getApplicationContext(), getString(R.string.mailalreadyexist), Toast.LENGTH_SHORT).show();
                                    } else {
                                        // E-posta adresi ile daha önce kayıt olunmamışsa, kullanıcıyı şifre belirlemesi için yönlendirin
                                        // Şifre belirleme ekranının görünür hale gelmesi veya yeni bir aktivite başlatılması gibi işlemleri burada yapabilirsiniz.
                                        // İsterseniz bu kısmı kendi uygulamanıza uygun şekilde özelleştirebilirsiniz.
                                        edittextPassword.setVisibility(View.VISIBLE);
                                        registerNewUser();
                                    }
                                } else {
                                    // Sorgu başarısız olduysa
                                    //Toast.makeText(getApplicationContext(), "Sorgu başarısız. Hata: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
    private void registerNewUser() {
        String email, password;
        email = emailEdittext.getText().toString();
        password = edittextPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), getString(R.string.please_enter_mail), Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), getString(R.string.please_enter_password), Toast.LENGTH_LONG).show();
            return;
        }

        // Şifrenin uzunluğunu kontrol et
        if (password.length() < 8) {
            Toast.makeText(getApplicationContext(), getString(R.string.passlong), Toast.LENGTH_LONG).show();
            return;
        }

        // Şifrede en az bir büyük harf, bir küçük harf ve bir rakam bulunmalıdır
        if (!password.matches(".*[A-Z].*") || !password.matches(".*[a-z].*") || !password.matches(".*\\d.*")) {
            Toast.makeText(getApplicationContext(), getString(R.string.passlong1), Toast.LENGTH_LONG).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), getString(R.string.sucregis), Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(CreateAccountEmail.this, HomeScreen.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.sucfailed), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}