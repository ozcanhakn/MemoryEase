package app.forget.forgetfulnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private String textToAnimate = "Create Quick Reminders";
    private String textToAnimate2 ="Make it Personal";
    private String textToAnimate3 ="MemoryEase";
    private ImageView orangeDot;

    private int charIndex = 0;
    private android.os.Handler handler = new Handler();
    ConstraintLayout constraintLayout;

    RelativeLayout withGoogle, withEmail, loginButton;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        constraintLayout = findViewById(R.id.constraintLayout); // ConstraintLayout'unuzun ID'sini kullanın
        constraintLayout.setBackgroundColor(Color.WHITE);

         mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("505784942919-s5qd0551kjmaqk4j4egjmhe1qeijtvs8.apps.googleusercontent.com")

                //AIzaSyAZMsbmM0dBJeKi0dyh9tjMxp_gAexmjMA
                .requestEmail()
                .build();
        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(MainActivity.this, googleSignInOptions);

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    // Kullanıcı oturum açmışsa ana ekrana yönlendirin
                    Intent intent = new Intent(MainActivity.this, HomeScreen.class);
                    startActivity(intent);
                    finish();
                } else {

                }
            }
        });


        withEmail = findViewById(R.id.withMail);
        loginButton = findViewById(R.id.logInButton);
        withGoogle = findViewById(R.id.withGoogle);


        textView = findViewById(R.id.textview);
        textView.setText(textToAnimate); // Metin içeriğini ayarlayın
        textView.setTextSize(27); // Metin boyutunu ayarlayın
            // Diğer özellikleri ayarlayın...

        orangeDot = findViewById(R.id.orangeDot);


        // Metin animasyonunu başlat
        startTextAnimation();
        init();
    }


    private void startTextAnimation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (charIndex < textToAnimate.length()) {
                    textView.setText(textToAnimate.substring(0, charIndex + 1));
                    orangeDot.setVisibility(View.VISIBLE); // Görünür hale getir
                    moveOrangeDot(); // Turuncu noktanın hareketini güncelle
                    charIndex++;

                    // Arka plan rengi animasyonu
                    int currentBackgroundColor = ((ColorDrawable) constraintLayout.getBackground()).getColor();
                    int targetBackgroundColor = Color.parseColor("#FFA500"); // Turuncu rengin kodu
                    ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), currentBackgroundColor, targetBackgroundColor);
                    colorAnimator.setDuration(900);
                    colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            constraintLayout.setBackgroundColor((int) animator.getAnimatedValue());
                        }
                    });
                    colorAnimator.start();

                    handler.postDelayed(this, 200); // 200ms'de bir güncelleme yap
                } else {
                    // Tüm metni yazdıktan sonra geri silme animasyonunu başlat
                    startEraseAnimation();
                }
            }
        }, 200);
    }

    private void startEraseAnimation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (charIndex > 0) {
                    charIndex--;
                    textView.setText(textToAnimate.substring(0, charIndex));
                    moveOrangeDot(); // Turuncu noktanın hareketini güncelle

                    // Arka plan rengini beyaza döndürme animasyonu
                    int currentBackgroundColor = ((ColorDrawable) constraintLayout.getBackground()).getColor();
                    int targetBackgroundColor = Color.WHITE; // Beyaz rengin kodu
                    ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), currentBackgroundColor, targetBackgroundColor);
                    colorAnimator.setDuration(900);
                    colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            constraintLayout.setBackgroundColor((int) animator.getAnimatedValue());
                        }
                    });
                    colorAnimator.start();

                    handler.postDelayed(this, 100); // 100ms'de bir geri silme yap
                } else {
                    // İlk metin tamamen silindiğinde ikinci metni başlat
                    startSecondTextAnimation();
                }
            }
        }, 100);
    }

    private void startSecondTextAnimation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (charIndex < textToAnimate2.length()) {
                    textView.setText(textToAnimate2.substring(0, charIndex + 1));
                    orangeDot.setVisibility(View.VISIBLE); // Görünür hale getir
                    moveOrangeDot(); // Turuncu noktanın hareketini güncelle
                    charIndex++;

                    // Arka plan rengi animasyonu (turuncudan başka bir renge geçiş)
                    int currentBackgroundColor = ((ColorDrawable) constraintLayout.getBackground()).getColor();
                    int targetBackgroundColor = Color.parseColor("#A932BD"); // Örnek başka bir renk kodu (Yeşil)
                    ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), currentBackgroundColor, targetBackgroundColor);
                    colorAnimator.setDuration(900);
                    colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            constraintLayout.setBackgroundColor((int) animator.getAnimatedValue());
                        }
                    });
                    colorAnimator.start();

                    handler.postDelayed(this, 200); // 200ms'de bir güncelleme yap
                } else {
                    // İkinci metin tamamlandığında metni silme animasyonunu başlat
                    startEraseSecondTextAnimation();
                }
            }
        }, 200);
    }

    private void startEraseSecondTextAnimation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (charIndex > 0) {
                    charIndex--;
                    textView.setText(textToAnimate2.substring(0, charIndex));
                    moveOrangeDot(); // Turuncu noktanın hareketini güncelle

                    // Arka plan rengini beyaza döndürme animasyonu
                    int currentBackgroundColor = ((ColorDrawable) constraintLayout.getBackground()).getColor();
                    int targetBackgroundColor = Color.WHITE; // Beyaz rengin kodu
                    ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), currentBackgroundColor, targetBackgroundColor);
                    colorAnimator.setDuration(900);
                    colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            constraintLayout.setBackgroundColor((int) animator.getAnimatedValue());
                        }
                    });
                    colorAnimator.start();

                    handler.postDelayed(this, 100); // 100ms'de bir geri silme yap
                } else {
                    // İkinci metin tamamen silindiğinde görünürlüğü kapat
                    orangeDot.setVisibility(View.INVISIBLE);
                    startThirdTextAnimation();
                }
            }
        }, 100);
    }

    private void startThirdTextAnimation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (charIndex < textToAnimate3.length()) {
                    textView.setText(textToAnimate3.substring(0, charIndex + 1));
                    orangeDot.setVisibility(View.VISIBLE); // Görünür hale getir
                    moveOrangeDot(); // Turuncu noktanın hareketini güncelle
                    charIndex++;

                    // Arka plan rengi animasyonu (turuncudan başka bir renge geçiş)
                    int currentBackgroundColor = ((ColorDrawable) constraintLayout.getBackground()).getColor();
                    int targetBackgroundColor = Color.parseColor("#A932BD"); // Örnek başka bir renk kodu (Yeşil)
                    ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), currentBackgroundColor, targetBackgroundColor);
                    colorAnimator.setDuration(900);
                    colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            constraintLayout.setBackgroundColor((int) animator.getAnimatedValue());
                        }
                    });
                    colorAnimator.start();

                    handler.postDelayed(this, 200); // 200ms'de bir güncelleme yap
                } else {
                    // İkinci metin tamamlandığında metni silme animasyonunu başlat
                }
            }
        }, 200);
    }

    private void moveOrangeDot() {
        float startFrom = orangeDot.getX();
        float textWidth = textView.getPaint().measureText(textToAnimate);

        if (charIndex < textToAnimate.length()) {
            float charWidth = textView.getPaint().measureText(textToAnimate.substring(charIndex, charIndex + 1));
            float endAt = textView.getX() + textWidth - charWidth;

            TranslateAnimation animation = new TranslateAnimation(startFrom, endAt, 0, 0);
            animation.setDuration(2000);
            animation.setFillAfter(true);
            orangeDot.startAnimation(animation);
        }
    }

    private void init(){

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        withEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CreateAccountEmail.class);
                startActivity(intent);
            }
        });



        withGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = googleSignInClient.getSignInIntent();
                //   Initialize sign in intent

                // Start activity for result
                startActivityForResult(intent, 100);

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check condition
        if (requestCode == 100) {
            // When request code is equal to 100 initialize task
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            // check condition
            if (signInAccountTask.isSuccessful()) {
                // When google sign in successful initialize string
                String s = "Google sign in successful";
                // Display Toast
                displayToast(s);
                // Initialize sign in account
                try {
                    // Initialize sign in account
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                    // Check condition
                    if (googleSignInAccount != null) {
                        // When sign in account is not equal to null initialize auth credential
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                        // Check credential
                        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // Check condition
                                if (task.isSuccessful()) {
                                    // When task is successful redirect to profile activity display Toast
                                    startActivity(new Intent(MainActivity.this, HomeScreen.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                    displayToast("Firebase authentication successful");
                                } else {
                                    // When task is unsuccessful display Toast
                                    displayToast("Authentication Failed :" + task.getException().getMessage());
                                }
                            }
                        });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }



}
