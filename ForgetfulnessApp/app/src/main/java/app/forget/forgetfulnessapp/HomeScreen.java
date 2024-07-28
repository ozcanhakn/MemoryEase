package app.forget.forgetfulnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import app.forget.forgetfulnessapp.Alarm.AlarmReceiver;
import app.forget.forgetfulnessapp.Alarm.AlarmService;

public class HomeScreen extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FragmentTransaction fragmentTransaction;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_screen);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, new MainFragment());
        fragmentTransaction.commit();


        createNotification();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Premium").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String premium = snapshot.child("premium").getValue(String.class);
                    if (premium.equals("yes")) {
                        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                            @Override
                            public boolean onNavigationItemSelected(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.main:
                                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.frame_layout, new MainFragment());
                                        fragmentTransaction.commit();
                                        return true;
                                    case R.id.advice:
                                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.frame_layout, new AdviceFragment());
                                        fragmentTransaction.commit();
                                        return true;
                                    case R.id.statistic:
                                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.frame_layout, new StatisticFragment());
                                        fragmentTransaction.commit();
                                        return true;
                                    case R.id.settings:
                                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.frame_layout, new SettingsFragment());
                                        fragmentTransaction.commit();
                                        return true;
                                }
                                return false;
                            }
                        });
                    }else {
                        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                            @Override
                            public boolean onNavigationItemSelected(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.main:
                                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.frame_layout, new MainFragment());
                                        fragmentTransaction.commit();
                                        return true;
                                    case R.id.advice:
                                        Intent intent1 = new Intent(HomeScreen.this, UpgradeForProActivity.class);
                                        startActivity(intent1);
                                        return true;
                                    case R.id.statistic:
                                        Intent intent = new Intent(HomeScreen.this, UpgradeForProActivity.class);
                                        startActivity(intent);
                                        return true;
                                    case R.id.settings:
                                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.frame_layout, new SettingsFragment());
                                        fragmentTransaction.commit();
                                        return true;
                                }
                                return false;
                            }
                        });
                    }
                    /*3 gün kullanma özelliğini yapmak için
                    yapmam gereken şeyler:
                    Kullanıcı abone değilse,
                    ilk tıklama zamanını ve saatini al her tıklamada tıklanma zamanını al ve
                    veritabanındaki ile karşılaştır eğer zaman geldiyse tıklamasına izin verdirme ve abone ol
                    sayfasına yönelt.


                    * */

                }
                else {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference refTrialPeriod = database.getReference("TrialPeriod").child(userId);


                    bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem item) {
                            long currentTime = System.currentTimeMillis();
                            switch (item.getItemId()) {
                                case R.id.main:
                                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.frame_layout, new MainFragment());
                                    fragmentTransaction.commit();
                                    return true;
                                case R.id.advice:
                                    handleAdviceClick(refTrialPeriod, userId, currentTime);
                                    return true;
                                case R.id.statistic:
                                    handleStatisticClick(refTrialPeriod, userId, currentTime);
                                    return true;
                                case R.id.settings:
                                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.frame_layout, new SettingsFragment());
                                    fragmentTransaction.commit();
                                    return true;
                            }
                            return false;
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });



        Log.d("DENEME", "onReceive: "+ userId+ "HOME SCREEN");







        Intent serviceIntent = new Intent(this,AlarmReceiver.class);
        ContextCompat.startForegroundService(this,serviceIntent);

        bottomNavigationView.setSelectedItemId(R.id.main);






    }

    private void handleAdviceClick(DatabaseReference ref, String userId, long currentTime) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    createAdviceNode(ref, userId, currentTime);
                    Toast.makeText(HomeScreen.this, R.string.trialstart, Toast.LENGTH_SHORT).show();
                    navigateToAdviceFragment();
                } else {
                    checkAdviceClickTime(dataSnapshot, currentTime);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", "Database error: " + databaseError.getMessage());
                Toast.makeText(HomeScreen.this, "Bir hata oluştu. Lütfen tekrar deneyin.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createAdviceNode(DatabaseReference ref, String userId, long currentTime) {
        ref.getParent().child(userId).child("advice").child("time").setValue(currentTime);
        ref.getParent().child(userId).child("advice").child("click").setValue(true);
        Log.d("AdviceClick", "Advice node created with time: " + currentTime);
    }

    private void checkAdviceClickTime(DataSnapshot dataSnapshot, long currentTime) {
        DataSnapshot adviceSnapshot = dataSnapshot.child("advice");
        if (adviceSnapshot.exists() && adviceSnapshot.child("time").exists()) {
            long firstClickTime = adviceSnapshot.child("time").getValue(Long.class);
            long elapsedTime = currentTime - firstClickTime;
            long threeDaysInMillis =  3 * 24 * 60 * 60 * 1000;

            if (elapsedTime >= threeDaysInMillis) {
                Toast.makeText(this, R.string.expiredtrial, Toast.LENGTH_SHORT).show();
                navigateToUpgradeActivity();
            } else {
                navigateToAdviceFragment();
            }
        } else {
            createAdviceNode(dataSnapshot.getRef(), dataSnapshot.getKey(), currentTime);
            navigateToAdviceFragment();
        }
    }

    private void navigateToAdviceFragment() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, new AdviceFragment());
        fragmentTransaction.commit();
    }

    private void navigateToUpgradeActivity() {
        Intent intent = new Intent(HomeScreen.this, UpgradeForProActivity.class);
        startActivity(intent);
    }

    private void handleStatisticClick(DatabaseReference ref, String userId, long currentTime) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    ref.getParent().child(userId).child("statistic").child("time").setValue(currentTime);
                    ref.getParent().child(userId).child("statistic").child("click").setValue(true);
                    // İlk tıklama, AdviceFragment'a yönlendir
                    Toast.makeText(HomeScreen.this, R.string.trialstart, Toast.LENGTH_SHORT).show();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, new StatisticFragment());
                    fragmentTransaction.commit();
                } else {
                    DataSnapshot adviceSnapshot = dataSnapshot.child("statistic");
                    if (adviceSnapshot.exists() && adviceSnapshot.child("time").exists()) {
                        long firstClickTime = adviceSnapshot.child("time").getValue(Long.class);
                        long elapsedTime = currentTime - firstClickTime;
                        long threeDaysInMillis =  3 * 24 * 60 * 60 * 1000;

                        if (elapsedTime >= threeDaysInMillis) {
                            // 3 gün geçmiş, UpgradeForProActivity'e yönlendir
                            Toast.makeText(HomeScreen.this, R.string.expiredtrial, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(HomeScreen.this, UpgradeForProActivity.class);
                            startActivity(intent);
                        } else {
                            // 3 gün geçmemiş, AdviceFragment'a yönlendir
                            fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.frame_layout, new StatisticFragment());
                            fragmentTransaction.commit();
                        }
                    } else {
                        // İlk tıklama, zaman kaydet ve AdviceFragment'a yönlendir
                        ref.child("statistic").child("time").setValue(currentTime);
                        ref.child("statistic").child("click").setValue(true);
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_layout, new StatisticFragment());
                        fragmentTransaction.commit();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void createNotification() {
        // AlarmService sınıfından bir nesne oluşturularak createNotification metodu çağrılır
        Intent serviceIntent = new Intent(this,AlarmService.class);

        startForegroundService(serviceIntent);
        Log.d("HOME", "createNotification: yönlendirdi");
    }

    public void switchToStatisticFragment() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, new StatisticFragment());
        fragmentTransaction.commit();
    }

    public void switchToAdviceFragment(){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, new AdviceFragment());
        fragmentTransaction.commit();
    }

    public void switchToSettingsFragment(){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, new AdviceFragment());
        fragmentTransaction.commit();
    }

}