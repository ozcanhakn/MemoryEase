package app.forget.forgetfulnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;

public class SleepTrackerActivity extends AppCompatActivity {
    Button uyan,uyu;
    FirebaseDatabase database;
    DatabaseReference reference;
    TextView uykuZamanıToplam;

    LottieAnimationView lottieSun,lottieMoon;
    ConstraintLayout backGround;

    boolean isSleepNow = false;
    
    ImageView question;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sleep_tracker);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();

        uyu = findViewById(R.id.buttonSleep);
        uyan = findViewById(R.id.buttonWakeUp);
        uykuZamanıToplam = findViewById(R.id.zaman);
        lottieMoon = findViewById(R.id.sleepingMoon);
        lottieSun = findViewById(R.id.weakUpSun);
        backGround = findViewById(R.id.backGround);
        question = findViewById(R.id.questionButtonSleepStatistic);

        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(SleepTrackerActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.question_alert_box_item_sleep);

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.CENTER);
            }
        });

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Sleep").child(userid);

        preferences = getSharedPreferences("MyPrefs2", MODE_PRIVATE);
        editor = preferences.edit();

        // SharedPreferences'ten isSleeping değerini al
        boolean isSleeping = preferences.getBoolean("isSleeping", false);

        if (isSleeping) {
            // Eğer isSleeping true ise, uyan düğmesini görünür yap, uyu düğmesini gizle
            uyu.setVisibility(View.GONE);
            uyan.setVisibility(View.VISIBLE);
            lottieMoon.setVisibility(View.VISIBLE);
            lottieSun.setVisibility(View.GONE);
        } else {
            // Eğer isSleeping false ise, uyu düğmesini görünür yap, uyan düğmesini gizle
            uyu.setVisibility(View.VISIBLE);
            uyan.setVisibility(View.GONE);
            lottieMoon.setVisibility(View.GONE);
            lottieSun.setVisibility(View.VISIBLE);
        }




        uyu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("isSleeping", true);
                editor.apply();
                // Date nesnesini istediğiniz biçime dönüştürün
                    lottieMoon.setVisibility(View.VISIBLE);
                    uyu.setVisibility(View.GONE);
                    uyan.setVisibility(View.VISIBLE);

                int screenWidth = getResources().getDisplayMetrics().widthPixels;
                int screenHeight = getResources().getDisplayMetrics().heightPixels;

                // Lottie animasyonunun başlangıç konumunu belirle (ekranın dışında, sağ köşeden)
                lottieMoon.setTranslationX(screenWidth);

                lottieMoon.animate()
                        .translationX(screenWidth / 2 - lottieMoon.getWidth() / 2) // X ekseninde ekranın ortasına taşı
                        .setInterpolator(new AccelerateDecelerateInterpolator()) // Yavaş başla, yavaş bitir
                        .setDuration(1500) // Hareket süresi (milisaniye cinsinden)
                        .start();
                lottieSun.setVisibility(View.GONE);
                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = dateFormat.format(now);
                backGround.setBackgroundColor(Color.BLACK);
                String sleepUid = FirebaseDatabase.getInstance().getReference("Sleep").push().getKey();


                Map<String, Object> userReminder = new HashMap<>();
                userReminder.put("sleepingTime", formattedDate);
                userReminder.put("wakeUpTime", "0");
                userReminder.put("key", sleepUid);



                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference2 = database.getReference("Sleep").child(userid).child(sleepUid);

                reference2.setValue(userReminder, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError error, DatabaseReference ref) {
                        if (error == null) {
                            Toast.makeText(SleepTrackerActivity.this, getString(R.string.reminderinstalled), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SleepTrackerActivity.this, getString(R.string.remindernotinstalled), Toast.LENGTH_SHORT).show();
                        }
                    }
                });




            }
        });

        uyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // SharedPreferences'te uyku durumu değerini güncelle
                editor.putBoolean("isSleeping", false);
                editor.apply();

                uyu.setVisibility(View.VISIBLE);
                uyan.setVisibility(View.GONE);
                lottieMoon.setVisibility(View.GONE);
                lottieSun.setVisibility(View.VISIBLE);
                backGround.setBackgroundColor(Color.BLACK);
                lottieSun.setVisibility(View.VISIBLE);
                // Ekran boyutlarını al
                int screenWidth = getResources().getDisplayMetrics().widthPixels;
                int screenHeight = getResources().getDisplayMetrics().heightPixels;

                // Lottie animasyonunun başlangıç konumunu belirle (ekranın dışında)
                lottieSun.setTranslationY(-lottieSun.getHeight());

                lottieSun.animate()
                        .translationYBy(screenHeight / 2) // Y ekseninde aşağıya taşı, yarı yüksekliği kadar
                        .setInterpolator(new BounceInterpolator()) // Zıplama efekti için BounceInterpolator kullan
                        .setDuration(1000) // Zıplama süresi (milisaniye cinsinden)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                // Animasyon tamamlandığında ekranın tam ortasında durmasını sağla
                                int centerX = screenWidth / 2 - lottieSun.getWidth() / 2;
                                int centerY = screenHeight / 2 - lottieSun.getHeight() / 2;


                                lottieSun.animate()
                                        .translationX(centerX)
                                        .translationY(centerY)
                                        .setInterpolator(new AccelerateDecelerateInterpolator()) // Hareketin başlangıcı ve sonu yavaş, ortası hızlı olsun
                                        .setDuration(1000) // Hareket süresi (milisaniye cinsinden)
                                        .start();
                            }
                        })
                        .start();


                // Date nesnesini istediğiniz biçime dönüştürün
                Date now1 = new Date();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = dateFormat.format(now1);



                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference2 = database.getReference("Sleep").child(userid);
                reference2.orderByChild("wakeUpTime").equalTo("0").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot sleepSnapshot : snapshot.getChildren()) {
                                String sleepUid = sleepSnapshot.getKey();
                                Log.d("TAG1500", "onDataChange: "+sleepUid);
                                // İlgili düğümdeki wakeUpTime'ı güncelle
                                DatabaseReference specificReference = reference.child(sleepUid);
                                specificReference.child("wakeUpTime").setValue(formattedDate);

                                // Diğer düğümleri güncellemeye gerek yok, çıkabiliriz
                                deneme();
                                break;
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });

            }
        });

        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference reference1 = database1.getReference("TotalSleep").child(userid);

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int totalHours = 0;
                int totalMinutes = 0;
                int totalSeconds = 0;

                // Bugünün tarihini al
                String todayString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String dateValue = userSnapshot.child("date").getValue(String.class);
                        String timeValue = userSnapshot.child("time").getValue(String.class);

                        Log.d("ad3", "onDataChange: "+dateValue+ " " +timeValue);

                        if (dateValue != null && dateValue.equals(todayString) && timeValue != null) {
                            Log.d("ad1", "onDataChange: "+dateValue);
                            Log.d("ad2", "onDataChange: "+timeValue);
                            // "time" değerini "HH:mm:ss" formatından ayrıştır
                            String[] timeParts = timeValue.split(":");
                            if (timeParts.length == 3) {
                                totalHours += Integer.parseInt(timeParts[0]);
                                totalMinutes += Integer.parseInt(timeParts[1]);
                                totalSeconds += Integer.parseInt(timeParts[2]);
                            }
                        }
                    }


                // Toplam süreyi saniye cinsine çevir
                long totalSecondsLong = totalHours * 3600 + totalMinutes * 60 + totalSeconds;

                // Toplam süreyi "HH:mm:ss" formatında bir stringe dönüştür
                String totalSleepString = secondsToTime(totalSecondsLong);
                // TextView'a yazdır
                uykuZamanıToplam.setText(totalSleepString);


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });


    }

    private String secondsToTime(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }

    public void deneme(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();
        DatabaseReference sleepReference = database.getReference("Sleep").child(userid);
        DatabaseReference totalSleepReference = database.getReference("TotalSleep").child(userid);
        Log.d("c1000", "deneme: ");



        sleepReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Tarihler için toplam uyku süresini hesaplamak için bir harita oluştur
                    Map<String, Long> totalSleepDurationMap = new HashMap<>();

                    // Her uyku kaydını gezmek
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String sleepingTime = userSnapshot.child("sleepingTime").getValue(String.class);
                        String wakeUpTime = userSnapshot.child("wakeUpTime").getValue(String.class);
                        Log.d("c4", "onDataChange: "+sleepingTime);


                            if (sleepingTime != null && !sleepingTime.isEmpty() && wakeUpTime != null && !wakeUpTime.isEmpty()) {
                                try {
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date sleepDate = dateFormat.parse(sleepingTime);

                                    // Tarihi al
                                    SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    String dateKey = dateOnlyFormat.format(sleepDate);
                                    Log.d("c3", "onDataChange: "+dateKey);

                                    // Uyku süresini hesapla
                                    long sleepDurationInMillis = dateFormat.parse(wakeUpTime).getTime() - sleepDate.getTime();
                                    Log.d("c2", "onDataChange: "+sleepDurationInMillis);

                                    // Haritada toplam süreyi güncelle
                                    if (totalSleepDurationMap.containsKey(dateKey)) {
                                        totalSleepDurationMap.put(dateKey, totalSleepDurationMap.get(dateKey) + sleepDurationInMillis);
                                    } else {
                                        totalSleepDurationMap.put(dateKey, sleepDurationInMillis);
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }


                    // Toplam uyku sürelerini TotalSleep düğümüne kaydet
                    for (Map.Entry<String, Long> entry : totalSleepDurationMap.entrySet()) {
                        String dateKey = entry.getKey();
                        Log.d("c5", "onDataChange: "+dateKey);
                        long totalSleepDurationInMillis = entry.getValue();
                        Log.d("c1000", "onDataChange: "+ String.valueOf(totalSleepDurationInMillis));

                        // Toplam uyku süresini saat, dakika ve saniye olarak çevir
                        long hours = TimeUnit.MILLISECONDS.toHours(totalSleepDurationInMillis);
                        long minutes = TimeUnit.MILLISECONDS.toMinutes(totalSleepDurationInMillis - TimeUnit.HOURS.toMillis(hours));
                        long seconds = TimeUnit.MILLISECONDS.toSeconds(totalSleepDurationInMillis - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes));

                        // Toplam uyku süresini string'e çevir
                        String totalSleepDurationString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                        Log.d("c1500", "onDataChange: "+totalSleepDurationString);

                        Map<String, Object> sleepTracker = new HashMap<>();
                        sleepTracker.put("date", dateKey);
                        sleepTracker.put("time", totalSleepDurationString);

                        String sleepUid = FirebaseDatabase.getInstance().getReference("TotalSleep").push().getKey();

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference reference = database.getReference("TotalSleep").child(userid).child(sleepUid);


                        reference.setValue(sleepTracker, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError error, DatabaseReference ref) {
                                if (error == null) {
                                    Toast.makeText(SleepTrackerActivity.this, getString(R.string.reminderinstalled), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SleepTrackerActivity.this, getString(R.string.remindernotinstalled), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                        // TotalSleep düğümüne kaydet
                        //totalSleepReference.child(dateKey).setValue(totalSleepDurationString);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Hata durumuyla başa çık
            }
        });
    }


}