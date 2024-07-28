package app.forget.forgetfulnessapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import app.forget.forgetfulnessapp.Advice.CalenderActivity;
import app.forget.forgetfulnessapp.Alarm.AlarmReceiver;
import app.forget.forgetfulnessapp.Alarm.AlarmService;
import app.forget.forgetfulnessapp.Example.AlarmmReceiver;
import app.forget.forgetfulnessapp.Game.ObjectSequenceActivity;
import app.forget.forgetfulnessapp.Game.ShoppingCartActivity;
import app.forget.forgetfulnessapp.Game.StartGameActivity;
import app.forget.forgetfulnessapp.Model.Reminder;
import app.forget.forgetfulnessapp.ViewHolder.ReminderViewHolder;
import io.reactivex.rxjava3.annotations.NonNull;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    private List<String> secilenGunlerListesi = new ArrayList<>();
    private List<String> enYakinGununTarihi = new ArrayList<>();
    RelativeLayout addReminderButton, setReminderButton, plannerButton, voiceRecorderButton,addBillReminderButton;
    FragmentManager fragmentManager;
    TextView dateButton, timeButton, textIntervalsCertain, seeAllReminders;

    private SharedPreferences sharedPreferences;
    private boolean isCongrateLayoutShown =false;
    private boolean isCongrateLayoutShown1 =false;



    TextView click;
    RelativeLayout okeyButtonFirstReminder;
    ImageView shine;

    LocalDate deneme;

    private LocalDate bulunanTarih;


    Long distanceSs;

    EditText locationDistance;

    RelativeLayout unlockExclusiveButton;

    ImageView questionHowSetReminder;

    public double latitude, longitude;
    public String intervals, description, title;
    public String time;
    public String date;
    String selectCategory1;

    FirebaseDatabase database2;
    private AutocompleteSupportFragment autocompleteFragment;


    EditText edtReminderTitle, edtReminderDesc;
    PendingIntent pendingIntent, pendingIntent1;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private static final int REQUEST_CODE_SPEECH_INPUT_DESC = 1001;

    String[] categoryList = {"Personal", "Social", "Education", "Job", "Finance", "Shopping", "Other"};

    TextView myLastReminderTitle, myLastReminderDesc, statusIndicator;
    ImageView myLastReminderImage, voiceButton, editButton;

    TextToSpeech mTTS;

    String textSpeech, textSpeechDesc;
    String textSpeech1, textSpeechDesc1;

    RelativeLayout statusCompleteButton;

    String key;

    RecyclerView recyclerViewRemindersNotYet;
    FirebaseRecyclerAdapter<Reminder, ReminderViewHolder> adapter;
    RecyclerView.LayoutManager layoutManager;

    private List<String> matchedIds;

    LottieAnimationView lottieAnimationView;

    String[] notificationSounds = {
            "ses2.mp3",
            "al_sevgilim_kir_kalbimi_noti.mp3",
            "ses1.mp3",
            "notification_sound.mp3",

    };

    DatabaseReference reference2;

    SharedPreferences sharedPreferences1;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public MainFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        //Kullanıcıdan konum için izin alma***
        requestLocationPermission();

        lottieAnimationView = view.findViewById(R.id.lottieAnimationView1);


        recyclerViewRemindersNotYet = view.findViewById(R.id.recyclerRemindersNotYet);
        recyclerViewRemindersNotYet.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerViewRemindersNotYet.setLayoutManager(layoutManager);//layoutManager

        matchedIds = new ArrayList<>();

        database2 = FirebaseDatabase.getInstance();
        reference2 = database2.getReference("Reminder").child(userId);



        //Yönlendirme Burada
        checkAndShowCongrateLayout();


        //Tanımlamalar
        myLastReminderDesc = view.findViewById(R.id.myLastReminderDesc);
        myLastReminderImage = view.findViewById(R.id.myLastReminderImage);
        myLastReminderTitle = view.findViewById(R.id.myLastReminderTitle);
        questionHowSetReminder = view.findViewById(R.id.questionHowSetReminder);
        plannerButton = view.findViewById(R.id.addPlannerButton);
        voiceRecorderButton = view.findViewById(R.id.addVoiceButton);
        addBillReminderButton = view.findViewById(R.id.addBillReminderButton);

        addBillReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RemindBillActivity.class);
                startActivity(intent);
            }
        });

        voiceRecorderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), VoiceRecordActivity.class);
                startActivity(intent);
            }
        });

        plannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent intent = new Intent(getActivity(), CalenderActivity.class);
                Intent intent = new Intent(getActivity(), CalenderActivity.class);

                startActivity(intent);
            }
        });

        questionHowSetReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogHowSetReminder();
            }
        });


        statusIndicator = view.findViewById(R.id.statusIndicator);
        voiceButton = view.findViewById(R.id.voiceButton);
        editButton = view.findViewById(R.id.editButton);
        statusCompleteButton = view.findViewById(R.id.completeStatusButton);


        unlockExclusiveButton = view.findViewById(R.id.unlockExclusiveButton);


        seeAllReminders = view.findViewById(R.id.seeAllButton);
        seeAllReminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AllReminders.class);
                startActivity(intent);
            }
        });
        RelativeLayout unlockExclusiveButton = view.findViewById(R.id.unlockExclusiveButton);

        addReminderButton = view.findViewById(R.id.addReminderButton);

        Intent alarmIntent = new Intent(getContext(), AlarmReceiver.class);
        Intent alarmIntent1 = new Intent(getContext(), AlarmmReceiver.class);
        if (Build.VERSION.SDK_INT >= 31) {
            pendingIntent = PendingIntent.getBroadcast(getContext(), 0, alarmIntent, PendingIntent.FLAG_MUTABLE);
            pendingIntent1 = PendingIntent.getBroadcast(getContext(), 0, alarmIntent1, PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getBroadcast(getContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            pendingIntent1 = PendingIntent.getBroadcast(getContext(), 0, alarmIntent1, PendingIntent.FLAG_UPDATE_CURRENT);

        }


        // AlarmManager'ı alın ve alarmı ayarlayın (örneğin, 10 saniye sonra tetiklenmesini istiyorsak)
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        AlarmManager alarmManager1 = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        lastReminderData();
        getLocation();
        textToSpeech();

        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speakk();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLastReminderData();
            }
        });

        statusCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completeStatus();
            }
        });


        FirebaseMessaging.getInstance().subscribeToTopic("News")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        String msg = "Done";
                        if (!task.isSuccessful()){
                            msg = "Failed";
                        }
                    }
                });

        // Firebase'den tarih ve saat bilgilerini çekin


        FirebaseDatabase database4 = FirebaseDatabase.getInstance();
        DatabaseReference reference4 = database4.getReference("Bill").child(userId);

        reference4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot billSnapshot : snapshot.getChildren()){
                    String alarmTime = billSnapshot.child("time").getValue(String.class);
                    String alarmTime1 = billSnapshot.child("time1").getValue(String.class);
                    String status = billSnapshot.child("status").getValue(String.class);
                    String status1= billSnapshot.child("status").getValue(String.class);
                    String alarmDate = billSnapshot.child("date").getValue(String.class);

                    if (status.equals("not completed") && status1.equals("not completed")){

                        if (alarmTime!=null){

                                 Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                            try {
                                Date date = sdf.parse(alarmDate + " " + alarmTime);
                                if (date != null) {
                                    calendar.setTime(date);

                                    // Alarm zamanı geçtiyse hemen çağırın
                                    if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                                        Log.d("BILL", "onDataChange: " + "mainFragment alarm zamanı geldi geçiyo");
                                        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent);
                                    } else {
                                        Log.d("BILL", "onDataChange: " + "Alarm zamanı henüz gelmedi(saat/tarih)");
                                        // Alarm zamanı henüz gelmedi, belirlediğiniz zamanı kullanın
                                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                                    }
                                } else {
                                    Log.d("BILL", "onDataChange: Tarih bulunamadı");
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                    else if (status.equals("completed") && status1.equals("not completed")){
                        if (alarmTime1!=null) {
                            String alarmDateTime = alarmDate + " " + alarmTime1;
                            if (alarmTime!=null) {

                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                                try {
                                    Date date = sdf.parse(alarmDate + " " + alarmTime1);
                                    if (date != null) {
                                        calendar.setTime(date);

                                        // Alarm zamanı geçtiyse hemen çağırın
                                        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                                            Log.d("BILL", "onDataChange: " + "mainFragment alarm zamanı geldi geçiyo");
                                            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent);
                                        } else {
                                            Log.d("BILL", "onDataChange: " + "Alarm zamanı henüz gelmedi(saat/tarih)");
                                            // Alarm zamanı henüz gelmedi, belirlediğiniz zamanı kullanın
                                            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                                        }
                                    } else {
                                        Log.d("LOG", "onDataChange: Tarih bulunamadı");
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Reminder").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot reminderSnapshot : snapshot.getChildren()) {
                    String alarmDate = reminderSnapshot.child("date").getValue(String.class);
                    String alarmTime = reminderSnapshot.child("time").getValue(String.class);
                    String status = reminderSnapshot.child("status").getValue(String.class);
                    String type = reminderSnapshot.child("type").getValue(String.class);


                    if (status.equals("not completed") && type.equals("alarm")) {
                        if (alarmDate != null && alarmTime != null) {
                            // Alarm için bir takvim oluşturun
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            try {
                                Date date = sdf.parse(alarmDate + " " + alarmTime);
                                if (date != null) {
                                    calendar.setTime(date);

                                    // Alarm zamanı geçtiyse hemen çağırın
                                    if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                                        Log.d("LOG", "onDataChange: " + "mainFragment alarm zamanı geldi geçiyo");
                                        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent);
                                    } else {
                                        Log.d("LOG", "onDataChange: " + "Alarm zamanı henüz gelmedi(saat/tarih)");
                                        // Alarm zamanı henüz gelmedi, belirlediğiniz zamanı kullanın
                                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                                    }
                                } else {
                                    Log.d("LOG", "onDataChange: Tarih bulunamadı");
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d("LOG", "onDataChange: değer bulunamadı");
                        }
                    } else {
                        Log.d("LOG", "onDataChange: time değer zaten Completed 01");
                    }


                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getContext(), "Firebase veri çekme hatası", Toast.LENGTH_SHORT).show();
            }
        });


        FirebaseDatabase database3 = FirebaseDatabase.getInstance();
        DatabaseReference reference3 = database3.getReference("Reminder").child(userId);

        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dayOfWeekSnapshot : snapshot.getChildren()) {
                    String alarmType = dayOfWeekSnapshot.child("type").getValue(String.class);
                    String time = dayOfWeekSnapshot.child("time").getValue(String.class);
                    String friday = dayOfWeekSnapshot.child("Friday").getValue(String.class);
                    String monday = dayOfWeekSnapshot.child("Monday").getValue(String.class);
                    String saturday = dayOfWeekSnapshot.child("Saturday").getValue(String.class);
                    String wednesday = dayOfWeekSnapshot.child("Wednesday").getValue(String.class);
                    String tuesday = dayOfWeekSnapshot.child("Tuesday").getValue(String.class);
                    String thursday = dayOfWeekSnapshot.child("Thursday").getValue(String.class);
                    String sunday = dayOfWeekSnapshot.child("Sunday").getValue(String.class);
                    String status = dayOfWeekSnapshot.child("status").getValue(String.class);
                    String alarmId = dayOfWeekSnapshot.child("id").getValue(String.class);
                    String reminderDeadline = dayOfWeekSnapshot.child("reminderDeadline").getValue(String.class);


                    if (alarmType.equals("regular")) {

                        if (sunday.equals("yes")) {
                            secilenGunlerListesi.add("SUNDAY");
                        } else {
                            Log.d("LOG", "onDataChange: empty(Thursday)");
                        }
                        if (monday.equals("yes")) {
                            secilenGunlerListesi.add("MONDAY");
                        } else {
                            Log.d("LOG", "onDataChange: empty(Sunday)");
                        }
                        if (wednesday.equals("yes")) {
                            secilenGunlerListesi.add("WEDNESDAY");
                        } else {
                            Log.d("LOG", "onDataChange: empty(Wednesday)");
                        }
                        if (friday.equals("yes")) {
                            secilenGunlerListesi.add("FRIDAY");
                        } else {
                            Log.d("LOG", "onDataChange: empty(Friday)");
                        }
                        if (thursday.equals("yes")) {
                            secilenGunlerListesi.add("THURSDAY");
                        } else {
                            Log.d("LOG", "onDataChange: empty(Thursday)");
                        }
                        if (tuesday.equals("yes")) {
                            secilenGunlerListesi.add("TUESDAY");
                        } else {
                            Log.d("LOG", "onDataChange: empty(Tuesday)");
                        }
                        if (saturday.equals("yes")) {
                            secilenGunlerListesi.add("SATURDAY");
                        } else {
                            Log.d("LOG", "onDataChange: empty(Monday)");
                        }

                        for (String secilenGun : secilenGunlerListesi) {
                            LocalDate bulunanTarih = gunuBul(secilenGun);

                            Log.d("DÜZENLİ", "onDataChange: " + bulunanTarih.toString());

                            LocalDate simdikiTarih = LocalDate.now();
                            Calendar calendar1 = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


                            try {
                                Date date1 = sdf.parse(bulunanTarih.toString() + " " + time);
                                Date deadlineDate = sdf1.parse(reminderDeadline);


                                //Bu da alarmın çalmasını istenilen zamanın time millisi
                                long currentTimeMillis = date1.getTime();

                                //Anın current Time Millisi
                                long nowTimeMillis = System.currentTimeMillis();


                                long deadlineMillis = deadlineDate.getTime();


                                if (currentTimeMillis <= nowTimeMillis) {
                                    if (status.equals("not completed")) {
                                        Log.d("DÜZENLİ", "onDataChange: not completed değerde");
                                        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 100, pendingIntent);
                                        Log.d("DÜZENLİ", "onDataChange: triggerladı ve receivere yönlendirildi,üst");
                                    } else if (status.equals("Completed")) {
                                        if (deadlineMillis < currentTimeMillis) {
                                            Log.d("DÜZENLİ BUGÜN ALARM ZATEN ÇALDI", "onDataChange: ");
                                        } else {
                                            reference3.child(alarmId).child("status").setValue("not completed");
                                            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 100, pendingIntent);
                                        }
                                    } else {
                                        Log.d("DÜZENLİ", "onDataChange: status değeri farklı");
                                    }
                                } else {
                                    alarmManager.set(AlarmManager.RTC_WAKEUP, currentTimeMillis, pendingIntent);
                                    Log.d("DÜZENLİ", "onDataChange: Zaman gelmedi belirli zamana triggerlandı");
                                }



                                /*Log.d("DÜZENLİ", "onDataChange: Ayrıştırılmış tarih: " + date1);
                                assert date1 != null;
                                calendar1.setTime(date1);
                                LocalDate deadlineDate1 = LocalDate.parse(reminderDeadline);
                                Log.d("DÜZENLİ", "onDataChange: Ayrıştırılmış son tarih: " + deadlineDate1);

                                if (bulunanTarih.isBefore(deadlineDate1)){
                                    Log.d("DÜZENLİ", "onDataChange: deadlineDate1 bulunan tarihten önce");
                                    if (status.equals("not completed")){


                                    }else if (status.equals("Completed")){
                                        reference3.child(alarmId).setValue("not completed");

                                        alarmManager1.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 100, pendingIntent1);
                                        Log.d("DÜZENLİ", "onDataChange: triggerladı ve receivere yönlendirildi");

                                    }
                                }*/

                            } catch (ParseException e) {
                                e.printStackTrace();
                                Log.e("DÜZENLİ", "onDataChange: Tarih ayrıştırma hatası", e);
                            }
                        }

                    } else {

                    }


                }

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        unlockExclusiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UpgradeForProActivity.class);
                startActivity(intent);
            }
        });


        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateReminderActivity.class);
                startActivity(intent);
                //showAddReminderDialog();
                secilenGunleriGuncelle();

            }
        });


        return view;
    }

        //Kodlar gelecek box'ı göstermek için
        // Kodlar gelecek box'ı göstermek için
    private void checkAndShowCongrateLayout() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user != null ? user.getUid() : null;

        // Kullanıcının daha önce dialogu gördüğünü kontrol etmek için bayrak
        //sharedPreferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        isCongrateLayoutShown = sharedPreferences.getBoolean("congrate_layout_shown", false);


        if (userId != null) {
            Log.d("den", "checkAndShowCongrateLayout: checkandshow");
            reference2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Kullanıcının hatırlatıcıları var
                        long reminderCount = dataSnapshot.getChildrenCount();

                        //Duruma göre isCongrateLa.. kaldır
                        if (reminderCount == 1 && isCongrateLayoutShown) {
                            Log.d("COunt", "onDataChange: 1 e eşit");
                            // Kullanıcının sadece bir hatırlatıcısı var ve congrate_layout daha önce gösterilmediyse
                            // Dialogu gösterildi olarak işaretle
                            // showCongrateLayout() metodu, SharedPreferences'taki bayrağa göre çağrılıyor
                            if (isCongrateLayoutShown == false) {
                                showCongrateLayout();
                                Log.d("Count", "onDataChange: gözükmedi");
                                Log.d("CongrateLayout", "showCongrateLayout() called because congrate layout is not shown yet.");
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("congrate_layout_shown", true);
                                editor.apply();
                            }
                            // showCongrateLayout() metodu, SharedPreferences'taki bayrağa göre çağrılıyor
                            else {
                                Log.d("den1", "Congrate layout is already shown, skipping showCongrateLayout() call.");
                            }


                        }

                        else {


                        }
                    } else {
                        sharedPreferences1 = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        isCongrateLayoutShown1 = sharedPreferences1.getBoolean("congrate_layout_shown1", false);



                            if (isCongrateLayoutShown1 == false) {

                                Log.d("TAG", "onDataChange: gösterilmemiş");
                                // Kullanıcının hatırlatıcısı yok
                                // Burada gerekirse uygun bir işlem yapılabilir
                                //Hatırlatıcı kurmaya gönder ve animasyon ile anlat
                                //BURAYADA BİR KONTROL GELECEK SADECE BİR DEFA GÖSTERMEK ADINA BİR KONTROL EKLE SHAREDPREFSLERİ TEST ET.
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(getActivity(),CreateReminderActivity.class);
                                        intent.putExtra("animClick","animClick");
                                        startActivity(intent);
                                        //Click efekti create
                                    }
                                });

                                SharedPreferences.Editor editor = sharedPreferences1.edit();
                                editor.putBoolean("congrate_layout_shown1", true);
                                editor.apply();
                            }else {
                                Log.d("TAG", "onDataChange: gösterilmiş");
                            }








                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Veri okuma hatası
                    databaseError.toException().printStackTrace();
                }
            });
        }

    }
/*
    private void showCongrateLayout() {
        // Congrate layout'unu göstermek için gerekli işlemleri yapın

        // Congrate layout'unu gösterdikten sonra bayrağı güncelle
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("congrate_layout_shown", true);
        editor.apply();
    }


 */


    private void showCongrateLayout() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.first_reminder_show_box_layout);


        RelativeLayout okButton = dialog.findViewById(R.id.okeyButtonFirstReminder);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userId = user.getUid();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("Premium").child(userId);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            dialog.dismiss();

                        }else {
                            showSubsLayout();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });

            }
        });
        // Dialog içerisindeki ImageView'leri bulun
        ImageView imageView = dialog.findViewById(R.id.imageLogoFirstReminderCong);
        ImageView imageView1 = dialog.findViewById(R.id.imageLogoPart1FirstReminderCong);
        ImageView imageView2 = dialog.findViewById(R.id.imageLogoPart2FirstReminderCong);
        ImageView imageView3 = dialog.findViewById(R.id.imageLogoPart3FirstReminderCong);

        // Animasyonları yükleyin
        Animation scatterAnimation1 = createScatterAnimation((View) imageView1.getParent());
        Animation scatterAnimation2 = createScatterAnimation((View) imageView2.getParent());
        Animation scatterAnimation3 = createScatterAnimation((View) imageView3.getParent());

        // Animasyonu ImageView'lere uygulayın
        imageView1.setVisibility(View.VISIBLE);
        imageView2.setVisibility(View.VISIBLE);
        imageView3.setVisibility(View.VISIBLE);

        imageView1.startAnimation(scatterAnimation1);
        imageView2.startAnimation(scatterAnimation2);
        imageView3.startAnimation(scatterAnimation3);

        // Dialog gösterimi
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation2;
        dialog.getWindow().setGravity(Gravity.CENTER);

        // Animasyonun bitişini dinleyin
        scatterAnimation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView1.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        scatterAnimation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView2.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        scatterAnimation3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView3.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    private void showSubsLayout() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.user_not_subs_layout);

        okeyButtonFirstReminder = dialog.findViewById(R.id.okeyButtonNotSubs);
        shine = dialog.findViewById(R.id.shineImage);
        click = dialog.findViewById(R.id.textClick);


        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),UpgradeForProActivity.class);
                startActivity(intent);
            }
        });

        final ImageView personUnsPhoto = dialog.findViewById(R.id.personUnsPhoto);
        final TextView textView = dialog.findViewById(R.id.textViewUns);

        final int[] images = {R.drawable.unsplash_subs, R.drawable.unsplashsubs_stat, R.drawable.unsplash_subs_adv}; // Değiştireceğiniz resim kaynaklarını tanımlayın
        final String[] texts = {"Sınırsız Hatırlatıcı Kurabilirsiniz", "İstatistiklerinizi Görebilirsiniz", "Çeşitli Tavsiyelerden\nYararlanabilirsiniz"}; // Değiştireceğiniz metinleri tanımlayın
        final int[] currentIndex = {0}; // Başlangıç ​​olarak ilk resim ve metni göstermek için bir indeks belirtin

        // Zamanlayıcı oluşturun
        ScheduledExecutorService scheduledExecutorService1 =
                Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService1.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // ImageView'in ve TextView'in içeriğini değiştirin
                        personUnsPhoto.setImageResource(images[currentIndex[0]]);
                        textView.setText(texts[currentIndex[0]]);

                        // Sonraki resim ve metni göstermek için indeksi güncelleyin
                        currentIndex[0] = (currentIndex[0] + 1) % images.length;
                    }
                });
            }
        }, 4, 4, TimeUnit.SECONDS); // Her 5 saniyede bir çalışacak şekilde zamanlayıcıyı ayarlayın




        ScheduledExecutorService scheduledExecutorService =
                Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shineStart();
                        vibrationStartText();
                    }
                });
            }
        },1,3,TimeUnit.SECONDS);





        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation2;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

    private void shineStart(){
        Animation animation = new TranslateAnimation(
                0,okeyButtonFirstReminder.getWidth()+shine.getWidth(),0,0
        );
        animation.setDuration(700);
        animation.setFillAfter(false);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());

        shine.startAnimation(animation);
    }

    private void vibrationStartText(){
            Animation animTitreme = new TranslateAnimation(-10, 10, 0, 0); // X ekseninde -10'dan 10'a hareket
            animTitreme.setInterpolator(new CycleInterpolator(5)); // Hareketi beş kez tekrarla
            animTitreme.setDuration(1000); // 1 saniye sürsün

            click.startAnimation(animTitreme); // TextView'e animasyonu uygula


    }



    // Rastgele bir animasyon oluşturmak için yardımcı yöntem
    private Animation createScatterAnimation(View parentView) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float parentWidth = parentView.getWidth();
        float parentHeight = parentView.getHeight();

        float fromXDelta = (float) (Math.random() * parentWidth * 2) - parentWidth;
        float toXDelta = (float) (Math.random() * parentWidth * 2) - parentWidth;
        float fromYDelta = (float) (Math.random() * parentHeight * 2) - parentHeight;
        float toYDelta = (float) (Math.random() * parentHeight * 2) - parentHeight;

        Animation scatterAnimation = new TranslateAnimation(
                Animation.ABSOLUTE, fromXDelta,
                Animation.ABSOLUTE, toXDelta,
                Animation.ABSOLUTE, fromYDelta,
                Animation.ABSOLUTE, toYDelta
        );
        scatterAnimation.setDuration(2000);
        scatterAnimation.setInterpolator(new AccelerateInterpolator());
        return scatterAnimation;
    }

    private void showDialogHowSetReminder() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.how_to_set_alarm_info);




        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation1;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

    //Hatırlatıcı kurulum kutucuğu
    private void showAddReminderDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.reminder_layout_alert_item);

        // Places API'yi başlatın
        fragmentManager = getActivity().getSupportFragmentManager();

        if (fragmentManager.findFragmentById(R.id.autocomplete_fragment1) == null) {
            autocompleteFragment = AutocompleteSupportFragment.newInstance();

            // Yeni bir ID atanabilir
            int fragmentContainerId = View.generateViewId();

            fragmentManager.beginTransaction()
                    .add(fragmentContainerId, autocompleteFragment)
                    .commit();
        }

        SwitchCompat switchLocation = dialog.findViewById(R.id.switchLocation);


        dateButton = dialog.findViewById(R.id.textSelectDate);
        timeButton = dialog.findViewById(R.id.textSelectClock);
        textIntervalsCertain = dialog.findViewById(R.id.textIntervalsCertain);


        setReminderButton = dialog.findViewById(R.id.relativeSetReminderButton);
        edtReminderDesc = dialog.findViewById(R.id.edtReminderDesc);
        edtReminderTitle = dialog.findViewById(R.id.edtReminderTitle);


        locationDistance = dialog.findViewById(R.id.locationDistance);

        String distances = locationDistance.getText().toString();

        distanceSs = Long.valueOf(distances);


        //SESLI

        ImageView micTitle = dialog.findViewById(R.id.micTitle);
        ImageView micDesc = dialog.findViewById(R.id.micDescription);

        //Select Category
        TextView selectCategory = dialog.findViewById(R.id.textSelectCategory);

        selectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Select Category");
                builder.setItems(categoryList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectCategory1 = categoryList[which];
                        selectCategory.setText(selectCategory1);
                        dialog.dismiss();


                    }
                });
                builder.show();
            }
        });


        micTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak();
            }
        });

        micDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speakDesc();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        Log.d("DENEME", "onReceive: " + userId);


        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                fragmentManager.findFragmentById(R.id.autocomplete_fragment1);

        switchLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    autocompleteFragment.getView().setVisibility(View.VISIBLE);
                } else {
                    autocompleteFragment.getView().setVisibility(View.GONE);
                }
            }
        });


        // Tarih Seçme Butonu için Tıklama Olayı
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        // Saat ve Dakika Seçme Butonu için Tıklama Olayı
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });


        textIntervalsCertain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showIntervals();
            }
        });


        // Places API'yi başlatın ve API anahtarınızı ekleyin
        String apiKey = "AIzaSyAZMsbmM0dBJeKi0dyh9tjMxp_gAexmjMA";
        Places.initialize(getContext(), apiKey);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                if (place != null && place.getLatLng() != null) {
                    LatLng latLng = place.getLatLng();
                    latitude = latLng.latitude;
                    longitude = latLng.longitude;

                    // Enlem ve boylam bilgileri mevcut, kullanabilirsiniz
                    Log.d("Enlem", String.valueOf(latitude));
                    Log.d("Boylam", String.valueOf(longitude));
                } else {
                    Log.d("Hata", "Enlem ve boylam bilgileri mevcut değil.");
                }
            }

            @Override
            public void onError(Status status) {
                // Hata durumu burada ele alınabilir.
            }
        });


        setReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                title = edtReminderTitle.getText().toString();
                description = edtReminderDesc.getText().toString();

                setReminderToDatabase();
                dialog.dismiss();


            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

    }

    /*//Hatırlatıcı kurulum kutucuğu
    private void showAddReminderDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.reminder_layout_alert_item);

        // Places API'yi başlatın
        fragmentManager = getActivity().getSupportFragmentManager();

        if (fragmentManager.findFragmentById(R.id.autocomplete_fragment1) == null) {
            autocompleteFragment = AutocompleteSupportFragment.newInstance();

            // Yeni bir ID atanabilir
            int fragmentContainerId = View.generateViewId();

            fragmentManager.beginTransaction()
                    .add(fragmentContainerId, autocompleteFragment)
                    .commit();
        }

        SwitchCompat switchLocation = dialog.findViewById(R.id.switchLocation);


        dateButton = dialog.findViewById(R.id.textSelectDate);
        timeButton = dialog.findViewById(R.id.textSelectClock);
        textIntervalsCertain = dialog.findViewById(R.id.textIntervalsCertain);


        setReminderButton = dialog.findViewById(R.id.relativeSetReminderButton);
        edtReminderDesc = dialog.findViewById(R.id.edtReminderDesc);
        edtReminderTitle = dialog.findViewById(R.id.edtReminderTitle);


        locationDistance = dialog.findViewById(R.id.locationDistance);

        String distances = locationDistance.getText().toString();

        distanceSs = Long.valueOf(distances);


        //SESLI

        ImageView micTitle = dialog.findViewById(R.id.micTitle);
        ImageView micDesc = dialog.findViewById(R.id.micDescription);

        //Select Category
        TextView selectCategory = dialog.findViewById(R.id.textSelectCategory);

        selectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Select Category");
                builder.setItems(categoryList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectCategory1 = categoryList[which];
                        selectCategory.setText(selectCategory1);
                        dialog.dismiss();


                    }
                });
                builder.show();
            }
        });


        micTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak();
            }
        });

        micDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speakDesc();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        Log.d("DENEME", "onReceive: " + userId);


        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                fragmentManager.findFragmentById(R.id.autocomplete_fragment1);

        switchLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    autocompleteFragment.getView().setVisibility(View.VISIBLE);
                } else {
                    autocompleteFragment.getView().setVisibility(View.GONE);
                }
            }
        });


        // Tarih Seçme Butonu için Tıklama Olayı
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        // Saat ve Dakika Seçme Butonu için Tıklama Olayı
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });


        textIntervalsCertain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showIntervals();
            }
        });


        // Places API'yi başlatın ve API anahtarınızı ekleyin
        String apiKey = "AIzaSyAZMsbmM0dBJeKi0dyh9tjMxp_gAexmjMA";
        Places.initialize(getContext(), apiKey);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                if (place != null && place.getLatLng() != null) {
                    LatLng latLng = place.getLatLng();
                    latitude = latLng.latitude;
                    longitude = latLng.longitude;

                    // Enlem ve boylam bilgileri mevcut, kullanabilirsiniz
                    Log.d("Enlem", String.valueOf(latitude));
                    Log.d("Boylam", String.valueOf(longitude));
                } else {
                    Log.d("Hata", "Enlem ve boylam bilgileri mevcut değil.");
                }
            }

            @Override
            public void onError(Status status) {
                // Hata durumu burada ele alınabilir.
            }
        });


        setReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                title = edtReminderTitle.getText().toString();
                description = edtReminderDesc.getText().toString();

                setReminderToDatabase();
                dialog.dismiss();


            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

    }*/

    // Saat ve tarih kutucukları
    private void showIntervals() {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                R.style.CustomTimePickerDialog,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Seçilen saat ve dakika işlenebilir, burada değişkenlere atayabilirsiniz

                        int selectedHour = hourOfDay;
                        int selectedMinute = minute;

                        @SuppressLint("DefaultLocale") String timeText = String.format("%02d:%02d", hourOfDay, minute);

                        intervals = timeText;

                        textIntervalsCertain.setText(timeText);


                        // Seçilen saati ve dakikayı kullanma veya işleme alma işlemleri burada yapılabilir
                    }
                },
                hourOfDay, minute, true
        );
        timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

                // Zaman seçimi tamamlandığında yapılacak diğer işlemler buraya eklenir (opsiyonel)

            }
        });


        timePickerDialog.show();


    }

    // Tarih Seçme İşlevi
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                R.style.CustomTimePickerDialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Seçilen tarih işlenebilir, burada değişkenlere atayabilirsiniz
                        int selectedYear = year;
                        int selectedMonth = monthOfYear + 1; // Ay 0'dan başlar, 1 ekleyerek düzeltiliyor.
                        int selectedDay = dayOfMonth;


                        Log.d("tarih", "onDateSet: " + year);
                        dateButton.setText(String.valueOf(year + "/" + month + "/" + day));


                        // Seçilen tarihi kullanma veya işleme alma işlemleri burada yapılabilir
                    }
                },
                year, month, day
        );
        // "Tamam" butonunu ekleyin ve tarih seçimi tamamlandığında dialogu kapatın
        datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                DatePicker datePicker = datePickerDialog.getDatePicker();
                int year = datePicker.getYear();
                int month = datePicker.getMonth() + 1; // Aylar 0-11 arasında olduğu için +1 eklenir
                int day = datePicker.getDayOfMonth();

                // Seçilen tarihi TextView'a yaz
                dateButton.setText(year + "/" + month + "/" + day);

                date = dateButton.getText().toString();

                // Saat seçimi yapılıyorsa, saat bilgisini de alıp ilgili TextView'a yazabilirsiniz
                // Örnek olarak saat seçimi için bir TimePickerDialog kullanılıyorsa, o dialogdan saat bilgisini alabilirsiniz.

                // Tarih seçimi tamamlandığında yapılacak işlemler buraya eklenir (opsiyonel)
                dialog.dismiss(); // Dialogu kapat
            }
        });


        datePickerDialog.show();
    }

    // Saat ve Dakika Seçme İşlevi
    private void showTimePicker() {

        Log.d("Work", "showTimePicker: Dialog çalıştı");
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                R.style.CustomTimePickerDialog,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Seçilen saat ve dakika işlenebilir, burada değişkenlere atayabilirsiniz

                        int selectedHour = hourOfDay;
                        int selectedMinute = minute;

                        @SuppressLint("DefaultLocale") String timeText = String.format("%02d:%02d", hourOfDay, minute);

                        time = timeText;
                        timeButton.setText(timeText);


                        // Seçilen saati ve dakikayı kullanma veya işleme alma işlemleri burada yapılabilir
                    }
                },
                hourOfDay, minute, true
        );
        timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

                // Zaman seçimi tamamlandığında yapılacak diğer işlemler buraya eklenir (opsiyonel)

            }
        });


        timePickerDialog.show();
    }

    //Alarm'ı veritabanına yükleme
    private void setReminderToDatabase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        Date now = new Date();

        // Date nesnesini istediğiniz biçime dönüştürün
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(now);


        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference reference1 = database1.getReference("Premium").child(userid);
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String premium = snapshot.child("premium").getValue(String.class);
                    if (premium.equals("yes")){
                        String reminderUid = FirebaseDatabase.getInstance().getReference("Reminder").push().getKey();

                        Map<String, Object> userReminder = new HashMap<>();
                        userReminder.put("title", title);
                        userReminder.put("description", description);
                        userReminder.put("longitude", longitude);
                        userReminder.put("latitude", latitude);
                        userReminder.put("intervals", intervals);
                        userReminder.put("date", date);
                        userReminder.put("time", time);
                        userReminder.put("category", selectCategory1);
                        userReminder.put("status","not completed");
                        userReminder.put("creationDate",formattedDate);
                        userReminder.put("distance", distanceSs*1000);
                        userReminder.put("id",reminderUid);
                        userReminder.put("type","alarm");
                        if ("Job".equals(selectCategory1)){
                            userReminder.put("photo","https://images.unsplash.com/photo-1542626991-cbc4e32524cc?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8am9ifGVufDB8fDB8fHww");
                        }
                        else if ("Education".equals(selectCategory1)){
                            userReminder.put("photo","https://images.unsplash.com/photo-1601807576163-587225545555?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTR8fGVkdWNhdGlvbnxlbnwwfHwwfHx8MA%3D%3D");
                        }
                        else if ("Social".equals(selectCategory1)){
                            userReminder.put("photo","https://images.unsplash.com/photo-1485182708500-e8f1f318ba72?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTV8fHNvY2lhbHxlbnwwfHwwfHx8MA%3D%3D");

                        }else if ("Shopping".equals(selectCategory1)){
                            userReminder.put("photo","https://images.unsplash.com/photo-1481437156560-3205f6a55735?auto=format&fit=crop&q=80&w=1795&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D");
                        }
                        else if ("Finance".equals(selectCategory1)){
                            userReminder.put("photo","https://images.unsplash.com/photo-1554224154-26032ffc0d07?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mzh8fGZpbmFuY2V8ZW58MHx8MHx8fDA%3D");
                        }
                        else if ("Personal".equals(selectCategory1)){
                            userReminder.put("photo","https://images.unsplash.com/photo-1534239100122-c3703b109359?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nnx8cGVyc29uYWx8ZW58MHx8MHx8fDA%3D");
                        }
                        else if ("Other".equals(selectCategory1)){
                            userReminder.put("photo","https://images.unsplash.com/photo-1507925921958-8a62f3d1a50d?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8b3RoZXJ8ZW58MHx8MHx8fDA%3D");
                        }

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference reference = database.getReference("Reminder").child(userid).child(reminderUid);


                        reference.setValue(userReminder, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError error, DatabaseReference ref) {
                                if (error == null) {
                                    Toast.makeText(getContext(), "Reminder successfully installed", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Error installing reminder", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else {
                        //Premium değilse(Premium Süresi Bittiyse)

                        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                        DatabaseReference reference2 = database2.getReference("Reminder").child(userid);
                        reference2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                                List<String> creationDates = new ArrayList<>();
                                for (DataSnapshot creationDateSnapshot : dataSnapshot.getChildren()){
                                    String creationDate = creationDateSnapshot.child("creationDate").getValue(String.class);
                                    creationDates.add(creationDate);
                                }
                                Collections.sort(creationDates, Collections.reverseOrder());
                                Log.d("SORT", "onDataChange: ");

                                for (String sortedDate : creationDates){
                                    Log.d("SORTED", "SIRALANMIŞ TARİHLER: " + sortedDate);
                                }
                                // Şu anki tarihi al
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                Date currentDate = new Date();

                                int alarmCount = 0;

                                try {
                                    // En az iki tarih varsa kontrol et
                                    if (creationDates.size() >= 2) {
                                        for (String date : creationDates) {
                                            Date alarmDate = sdf.parse(date);
                                            long differenceInMilliseconds = currentDate.getTime() - alarmDate.getTime();
                                            long differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInMilliseconds);

                                            // Eğer iki tarih arasındaki fark bir hafta içindeyse, sayaç artır
                                            if (differenceInDays <= 7) {
                                                alarmCount++;
                                            }
                                        }

                                        // Eğer iki tarih arasındaki fark bir hafta içindeyse alarm kurma izni verme
                                        if (alarmCount >= 2) {
                                            // Alarm kurma izni verme
                                            System.out.println("1 hafta içinde iki tane alarm kuruldu, yeni alarm kuramazsınız.");
                                            Toast.makeText(getContext(), "1 hafta içinde iki tane alarm kuruldu, yeni alarm kuramazsınız.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Alarm kurma izni ver
                                            Toast.makeText(getContext(), "Son bir haftada 2'den az alarm kuruldu", Toast.LENGTH_SHORT).show();
                                            String reminderUid = FirebaseDatabase.getInstance().getReference("Reminder").push().getKey();

                                            Map<String, Object> userReminder = new HashMap<>();
                                            userReminder.put("title", title);
                                            userReminder.put("description", description);
                                            userReminder.put("longitude", longitude);
                                            userReminder.put("latitude", latitude);
                                            userReminder.put("intervals", intervals);
                                            userReminder.put("date", date);
                                            userReminder.put("time", time);
                                            userReminder.put("category", selectCategory1);
                                            userReminder.put("status","not completed");
                                            userReminder.put("distance", distanceSs*1000);
                                            userReminder.put("creationDate",formattedDate);
                                            userReminder.put("id",reminderUid);
                                            userReminder.put("type","alarm");
                                            if ("Job".equals(selectCategory1)){
                                                userReminder.put("photo","https://images.unsplash.com/photo-1542626991-cbc4e32524cc?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8am9ifGVufDB8fDB8fHww");
                                            }
                                            else if ("Education".equals(selectCategory1)){
                                                userReminder.put("photo","https://images.unsplash.com/photo-1601807576163-587225545555?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTR8fGVkdWNhdGlvbnxlbnwwfHwwfHx8MA%3D%3D");
                                            }
                                            else if ("Social".equals(selectCategory1)){
                                                userReminder.put("photo","https://images.unsplash.com/photo-1485182708500-e8f1f318ba72?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTV8fHNvY2lhbHxlbnwwfHwwfHx8MA%3D%3D");

                                            }else if ("Shopping".equals(selectCategory1)){
                                                userReminder.put("photo","https://images.unsplash.com/photo-1481437156560-3205f6a55735?auto=format&fit=crop&q=80&w=1795&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D");
                                            }
                                            else if ("Finance".equals(selectCategory1)){
                                                userReminder.put("photo","https://images.unsplash.com/photo-1554224154-26032ffc0d07?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mzh8fGZpbmFuY2V8ZW58MHx8MHx8fDA%3D");
                                            }
                                            else if ("Personal".equals(selectCategory1)){
                                                userReminder.put("photo","https://images.unsplash.com/photo-1534239100122-c3703b109359?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nnx8cGVyc29uYWx8ZW58MHx8MHx8fDA%3D");
                                            }
                                            else if ("Other".equals(selectCategory1)){
                                                userReminder.put("photo","https://images.unsplash.com/photo-1507925921958-8a62f3d1a50d?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8b3RoZXJ8ZW58MHx8MHx8fDA%3D");
                                            }

                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference reference = database.getReference("Reminder").child(userid).child(reminderUid);


                                            reference.setValue(userReminder, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError error, DatabaseReference ref) {
                                                    if (error == null) {
                                                        Toast.makeText(getContext(), "Reminder successfully installed", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(getContext(), "Error installing reminder", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }

                                    } else {
                                        // En az iki tarih yoksa alarm kurma izni ver
                                        Toast.makeText(getContext(), "Toplam 2 den az alarm var.Son bir haftada 2'den az alarm kuruldu", Toast.LENGTH_SHORT).show();
                                        String reminderUid = FirebaseDatabase.getInstance().getReference("Reminder").push().getKey();

                                        Map<String, Object> userReminder = new HashMap<>();
                                        userReminder.put("title", title);
                                        userReminder.put("description", description);
                                        userReminder.put("longitude", longitude);
                                        userReminder.put("latitude", latitude);
                                        userReminder.put("intervals", intervals);
                                        userReminder.put("date", date);
                                        userReminder.put("time", time);
                                        userReminder.put("category", selectCategory1);
                                        userReminder.put("status","not completed");
                                        userReminder.put("creationDate",formattedDate);
                                        userReminder.put("distance", distanceSs*1000);
                                        userReminder.put("id",reminderUid);
                                        userReminder.put("type","alarm");
                                        if ("Job".equals(selectCategory1)){
                                            userReminder.put("photo","https://images.unsplash.com/photo-1542626991-cbc4e32524cc?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8am9ifGVufDB8fDB8fHww");
                                        }
                                        else if ("Education".equals(selectCategory1)){
                                            userReminder.put("photo","https://images.unsplash.com/photo-1601807576163-587225545555?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTR8fGVkdWNhdGlvbnxlbnwwfHwwfHx8MA%3D%3D");
                                        }
                                        else if ("Social".equals(selectCategory1)){
                                            userReminder.put("photo","https://images.unsplash.com/photo-1485182708500-e8f1f318ba72?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTV8fHNvY2lhbHxlbnwwfHwwfHx8MA%3D%3D");

                                        }else if ("Shopping".equals(selectCategory1)){
                                            userReminder.put("photo","https://images.unsplash.com/photo-1481437156560-3205f6a55735?auto=format&fit=crop&q=80&w=1795&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D");
                                        }
                                        else if ("Finance".equals(selectCategory1)){
                                            userReminder.put("photo","https://images.unsplash.com/photo-1554224154-26032ffc0d07?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mzh8fGZpbmFuY2V8ZW58MHx8MHx8fDA%3D");
                                        }
                                        else if ("Personal".equals(selectCategory1)){
                                            userReminder.put("photo","https://images.unsplash.com/photo-1534239100122-c3703b109359?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nnx8cGVyc29uYWx8ZW58MHx8MHx8fDA%3D");
                                        }
                                        else if ("Other".equals(selectCategory1)){
                                            userReminder.put("photo","https://images.unsplash.com/photo-1507925921958-8a62f3d1a50d?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8b3RoZXJ8ZW58MHx8MHx8fDA%3D");
                                        }

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference reference = database.getReference("Reminder").child(userid).child(reminderUid);


                                        reference.setValue(userReminder, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError error, DatabaseReference ref) {
                                                if (error == null) {
                                                    Toast.makeText(getContext(), "Reminder successfully installed", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getContext(), "Error installing reminder", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }catch (Exception e){

                                }

                            }

                            @Override
                            public void onCancelled(@NotNull DatabaseError databaseError) {

                            }
                        });

                    }
                }else {
                    //ID Bulunmuyorsa
                    FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                    DatabaseReference reference2 = database2.getReference("Reminder").child(userid);
                    reference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                            List<String> creationDates = new ArrayList<>();
                            for (DataSnapshot creationDateSnapshot : dataSnapshot.getChildren()){
                                String creationDate = creationDateSnapshot.child("creationDate").getValue(String.class);
                                creationDates.add(creationDate);
                            }
                            Collections.sort(creationDates, Collections.reverseOrder());
                            Log.d("SORT", "onDataChange: ");

                            for (String sortedDate : creationDates){
                                Log.d("SORTED", "SIRALANMIŞ TARİHLER: " + sortedDate);
                            }
                            // Şu anki tarihi al
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                            Date currentDate = new Date();

                            int alarmCount = 0;

                            try {
                                // En az iki tarih varsa kontrol et
                                if (creationDates.size() >= 2) {
                                    for (String date : creationDates) {
                                        Date alarmDate = sdf.parse(date);
                                        long differenceInMilliseconds = currentDate.getTime() - alarmDate.getTime();
                                        long differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInMilliseconds);

                                        // Eğer iki tarih arasındaki fark bir hafta içindeyse, sayaç artır
                                        if (differenceInDays <= 7) {
                                            alarmCount++;
                                        }
                                    }

                                    // Eğer iki tarih arasındaki fark bir hafta içindeyse alarm kurma izni verme
                                    if (alarmCount >= 2) {
                                        // Alarm kurma izni verme
                                        System.out.println("1 hafta içinde iki tane alarm kuruldu, yeni alarm kuramazsınız.");
                                        Toast.makeText(getContext(), "1 hafta içinde iki tane alarm kuruldu, yeni alarm kuramazsınız.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Alarm kurma izni ver
                                        Toast.makeText(getContext(), "Son bir haftada 2'den az alarm kuruldu", Toast.LENGTH_SHORT).show();
                                        String reminderUid = FirebaseDatabase.getInstance().getReference("Reminder").push().getKey();

                                        Map<String, Object> userReminder = new HashMap<>();
                                        userReminder.put("title", title);
                                        userReminder.put("description", description);
                                        userReminder.put("longitude", longitude);
                                        userReminder.put("latitude", latitude);
                                        userReminder.put("intervals", intervals);
                                        userReminder.put("date", date);
                                        userReminder.put("time", time);
                                        userReminder.put("category", selectCategory1);
                                        userReminder.put("status","not completed");
                                        userReminder.put("distance", distanceSs*1000);
                                        userReminder.put("creationDate",formattedDate);
                                        userReminder.put("id",reminderUid);
                                        userReminder.put("type","alarm");
                                        if ("Job".equals(selectCategory1)){
                                            userReminder.put("photo","https://images.unsplash.com/photo-1542626991-cbc4e32524cc?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8am9ifGVufDB8fDB8fHww");
                                        }
                                        else if ("Education".equals(selectCategory1)){
                                            userReminder.put("photo","https://images.unsplash.com/photo-1601807576163-587225545555?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTR8fGVkdWNhdGlvbnxlbnwwfHwwfHx8MA%3D%3D");
                                        }
                                        else if ("Social".equals(selectCategory1)){
                                            userReminder.put("photo","https://images.unsplash.com/photo-1485182708500-e8f1f318ba72?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTV8fHNvY2lhbHxlbnwwfHwwfHx8MA%3D%3D");

                                        }else if ("Shopping".equals(selectCategory1)){
                                            userReminder.put("photo","https://images.unsplash.com/photo-1481437156560-3205f6a55735?auto=format&fit=crop&q=80&w=1795&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D");
                                        }
                                        else if ("Finance".equals(selectCategory1)){
                                            userReminder.put("photo","https://images.unsplash.com/photo-1554224154-26032ffc0d07?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mzh8fGZpbmFuY2V8ZW58MHx8MHx8fDA%3D");
                                        }
                                        else if ("Personal".equals(selectCategory1)){
                                            userReminder.put("photo","https://images.unsplash.com/photo-1534239100122-c3703b109359?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nnx8cGVyc29uYWx8ZW58MHx8MHx8fDA%3D");
                                        }
                                        else if ("Other".equals(selectCategory1)){
                                            userReminder.put("photo","https://images.unsplash.com/photo-1507925921958-8a62f3d1a50d?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8b3RoZXJ8ZW58MHx8MHx8fDA%3D");
                                        }

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference reference = database.getReference("Reminder").child(userid).child(reminderUid);


                                        reference.setValue(userReminder, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError error, DatabaseReference ref) {
                                                if (error == null) {
                                                    Toast.makeText(getContext(), "Reminder successfully installed", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getContext(), "Error installing reminder", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }

                                } else {
                                    // En az iki tarih yoksa alarm kurma izni ver
                                    Toast.makeText(getContext(), "Toplam 2 den az alarm var.Son bir haftada 2'den az alarm kuruldu", Toast.LENGTH_SHORT).show();
                                    String reminderUid = FirebaseDatabase.getInstance().getReference("Reminder").push().getKey();

                                    Map<String, Object> userReminder = new HashMap<>();
                                    userReminder.put("title", title);
                                    userReminder.put("description", description);
                                    userReminder.put("longitude", longitude);
                                    userReminder.put("latitude", latitude);
                                    userReminder.put("intervals", intervals);
                                    userReminder.put("date", date);
                                    userReminder.put("time", time);
                                    userReminder.put("category", selectCategory1);
                                    userReminder.put("status","not completed");
                                    userReminder.put("distance", distanceSs*1000);
                                    userReminder.put("creationDate",formattedDate);
                                    userReminder.put("id",reminderUid);
                                    userReminder.put("type","alarm");
                                    if ("Job".equals(selectCategory1)){
                                        userReminder.put("photo","https://images.unsplash.com/photo-1542626991-cbc4e32524cc?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8am9ifGVufDB8fDB8fHww");
                                    }
                                    else if ("Education".equals(selectCategory1)){
                                        userReminder.put("photo","https://images.unsplash.com/photo-1601807576163-587225545555?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTR8fGVkdWNhdGlvbnxlbnwwfHwwfHx8MA%3D%3D");
                                    }
                                    else if ("Social".equals(selectCategory1)){
                                        userReminder.put("photo","https://images.unsplash.com/photo-1485182708500-e8f1f318ba72?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTV8fHNvY2lhbHxlbnwwfHwwfHx8MA%3D%3D");

                                    }else if ("Shopping".equals(selectCategory1)){
                                        userReminder.put("photo","https://images.unsplash.com/photo-1481437156560-3205f6a55735?auto=format&fit=crop&q=80&w=1795&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D");
                                    }
                                    else if ("Finance".equals(selectCategory1)){
                                        userReminder.put("photo","https://images.unsplash.com/photo-1554224154-26032ffc0d07?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mzh8fGZpbmFuY2V8ZW58MHx8MHx8fDA%3D");
                                    }
                                    else if ("Personal".equals(selectCategory1)){
                                        userReminder.put("photo","https://images.unsplash.com/photo-1534239100122-c3703b109359?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nnx8cGVyc29uYWx8ZW58MHx8MHx8fDA%3D");
                                    }
                                    else if ("Other".equals(selectCategory1)){
                                        userReminder.put("photo","https://images.unsplash.com/photo-1507925921958-8a62f3d1a50d?auto=format&fit=crop&q=60&w=600&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8b3RoZXJ8ZW58MHx8MHx8fDA%3D");
                                    }

                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference reference = database.getReference("Reminder").child(userid).child(reminderUid);


                                    reference.setValue(userReminder, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError error, DatabaseReference ref) {
                                            if (error == null) {
                                                Toast.makeText(getContext(), "Reminder successfully installed", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), "Error installing reminder", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }catch (Exception e){

                            }

                        }

                        @Override
                        public void onCancelled(@NotNull DatabaseError databaseError) {

                        }
                    });



                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });




    }

    //Lokasyon İzni Alma
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    //try-catch yaz
    private void getLocation() {
        Log.d("LOG", "getLocation: getLocation çalıştı");
        Intent alarmIntent = new Intent(getContext(), AlarmReceiver.class);
        if (Build.VERSION.SDK_INT >= 31) {
            pendingIntent = PendingIntent.getBroadcast(getContext(), 0, alarmIntent, PendingIntent.FLAG_MUTABLE);
        }else {
            pendingIntent = PendingIntent.getBroadcast(getContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);


        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Reminder").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot locationSnapshot : snapshot.getChildren()) {
                    Log.d("LOG", "onDataChange: location addValueEvent çalıştı");
                    Double longitude1 = locationSnapshot.child("longitude").getValue(Double.class);
                    Double latitude1 = locationSnapshot.child("latitude").getValue(Double.class);
                    String status = locationSnapshot.child("status").getValue(String.class);
                    Long distanceS = locationSnapshot.child("distance").getValue(Long.class);


                    if (status.equals("not completed")) {
                        Log.d("LOG", "onDataChange: " + latitude1 + "/" + longitude1);
                        if (longitude1 != null && latitude1 != null) {
                            Context context = getContext();
                            if (context != null && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                                    Log.d("LOG", "onDataChange: çalıştı");
                                    if (location != null) {
                                        Log.d("LOG", "onDataChange: Metot çalıştı");
                                        try {
                                            double userLatitude = location.getLatitude();
                                            double userLongitude = location.getLongitude();

                                            double targetLatitude = latitude1;
                                            double targetLongitude = longitude1;

                                            float[] results = new float[1];
                                            Location.distanceBetween(userLatitude, userLongitude, targetLatitude, targetLongitude, results);
                                            float distanceInMeters = results[0];


                                            if (distanceInMeters <= 1000) {
                                                Log.d("LOG01", "onDataChange: Kullanıcının seçtiği uzaklık"+distanceS);
                                                Log.d("LOG", "onDataChange: " + "Hedef Konuma 1KM'den az kaldı /" + distanceInMeters);
                                                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent);
                                            } else {
                                                Log.d("LOG", "getLocation: " + "Kullanıcı hedeften en az 1KM uzaklıkta bulunmakta.");
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }
                    } else {
                        Log.d("LOGMAIN", "onDataChange: Bu alarm zaten tamamlanmış 01");
                    }

                }

                }


            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    //Konuşarak veriyi yazdırma
    private void speakDesc(){
        //intent to show speech to text dialog
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");

        //start intent
        try {
            // in there was no error
            // show dialog
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT_DESC);

        }catch (Exception e){
            // if there was some error
            // get message of error and show
            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    //Konuşarak veriyi yazdırma
    private void speak(){
        //intent to show speech to text dialog
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");

        //start intent
        try {
            // in there was no error
            // show dialog
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);

        }catch (Exception e){
            // if there was some error
            // get message of error and show
            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }

    //Konuşarak veriyi yazdırma
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null!=data){
                    // get text array from voice intent
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    edtReminderTitle.setText(result.get(0));
                }
                break;
            } case REQUEST_CODE_SPEECH_INPUT_DESC: {
                if (resultCode == RESULT_OK && null !=data){

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    edtReminderDesc.setText(result.get(0));


                }
                break;
            }
        }
    }

    private void lastReminderData(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Reminder").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Reminder nearestReminder = null;
                long currentTimeInMillis = System.currentTimeMillis();
                long nearestTimeDifference = Long.MAX_VALUE;

                    for (DataSnapshot reminderSnapshot : dataSnapshot.getChildren()) {
                        Reminder reminder = reminderSnapshot.getValue(Reminder.class);
                        String reminderDateTime = reminder.getCreationDate();

                        // Hatırlatma tarihini ve zamanını milisaniyeye çevir
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            Date reminderDateTimeObj = sdf.parse(reminderDateTime);
                            long reminderTimeInMillis = reminderDateTimeObj.getTime();

                            // Şu anki zaman ile hatırlatma zamanı arasındaki farkı hesapla
                            long timeDifference = Math.abs(reminderTimeInMillis - currentTimeInMillis);

                            // En yakın zamanı kontrol et
                            if (timeDifference < nearestTimeDifference) {
                                nearestTimeDifference = timeDifference;
                                nearestReminder = reminder;
                                Log.d("LOG", "onDataChange:Mainnn "+nearestReminder);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    if (nearestReminder != null) {
                        // En yakın hatırlatıcıyı buldunuz, burada istediğiniz işlemi yapabilirsiniz
                        // nearestReminder nesnesi, en yakın hatırlatıcıyı temsil eder

                        String title = nearestReminder.getTitle();
                        String description = nearestReminder.getDescription();
                        String url = nearestReminder.getPhoto();
                        String status = nearestReminder.getStatus();


                        key = nearestReminder.getId();





                        textSpeech = title;
                        textSpeechDesc = description;

                        // title ve description değerlerini TextView'lara ayarlayın
                        myLastReminderTitle.setText(title);
                        myLastReminderDesc.setText(description);
                        statusIndicator.setText(status);

                        Picasso.get().load(url).into(myLastReminderImage);
                        editButton.setVisibility(View.VISIBLE);


                    } else {
                        // Herhangi bir hatırlatıcı bulunamadı

                            myLastReminderTitle.setText(getString(R.string.notyetsetupreminder));
                            myLastReminderDesc.setText(getString(R.string.lastreminder1));
                            statusIndicator.setText("");
                            statusCompleteButton.setVisibility(View.GONE);
                            editButton.setVisibility(View.GONE);

                    }
                }



                @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }



    private void textToSpeech(){
        mTTS = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    int result = mTTS.setLanguage(Locale.getDefault());



                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS","Language not supported");
                    }else {

                    }
                }else {
                    Log.e("TTS","Initialization failed");
                }
            }
        });
    }


    private void speakk(){
        mTTS.speak("title: "+textSpeech + "  " +"description: " + textSpeechDesc,TextToSpeech.QUEUE_FLUSH,null);
    }


    @Override
    public void onDestroy() {

        if (mTTS !=null){
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();

        if (autocompleteFragment != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(autocompleteFragment);
            fragmentTransaction.commit();
            fragmentManager.executePendingTransactions();
            autocompleteFragment = null;
        }

    }


    private void changeLastReminderData(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_reminder_litem);


        EditText changeTitleBox = dialog.findViewById(R.id.changeTitleBox);
        EditText changeDescBox = dialog.findViewById(R.id.changeDescBox);
        RelativeLayout buttonChange = dialog.findViewById(R.id.relativeOkeyButton);
        RelativeLayout buttonCancel = dialog.findViewById(R.id.relativeCancelButton);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String currentUserId = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Reminder").child(currentUserId).child(key);
        Log.d("LOG", "changeLastReminderData: "+ key);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String title1 = dataSnapshot.child("title").getValue(String.class);
                String description1 = dataSnapshot.child("description").getValue(String.class);


                changeDescBox.setText(description1);
                changeTitleBox.setText(title1);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        buttonChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newTitle = changeTitleBox.getText().toString();
                String newDesc = changeDescBox.getText().toString();


                reference.child("title").setValue(newTitle);
                reference.child("description").setValue(newDesc);

                dialog.dismiss();

            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }


    private void completeStatus(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String currentUserId = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Reminder").child(currentUserId).child(key);
        Log.d("LOG", "changeLastReminderData: "+ key);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String statusValue = snapshot.child("status").getValue(String.class);

                if ("not completed".equals(statusValue) && statusValue != null){
                    reference.child("status").setValue("Complete");
                    statusIndicator.setTextColor(Color.parseColor("#FFFFFF"));
                    statusIndicator.setText("Complete");
                }else {

                }



            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();




        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Reminder").child(userId);


        Query query = reference.orderByChild("status").equalTo("not completed");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String reminderId = dataSnapshot.getKey();
                    matchedIds.add(reminderId);
                    Log.d("LOG1", "onDataChange: "+ matchedIds);


                    FirebaseRecyclerOptions<Reminder> options = new FirebaseRecyclerOptions.Builder<Reminder>()
                            .setQuery(query, Reminder.class)
                            .build();
                    Log.d("LOG10", "onDataChange: sorgu tamam 01"+query);

                    adapter = new FirebaseRecyclerAdapter<Reminder, ReminderViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(ReminderViewHolder viewHolder, int position, Reminder model) {
                            viewHolder.title.setText(model.getTitle());
                            viewHolder.description.setText(model.getDescription());
                            Picasso.get().load(model.getPhoto()).into(viewHolder.imageView);
                            viewHolder.completeStatus.setText(model.getStatus());

                            Log.d("LOG01", "onBindViewHolder: veriler çekildi 01");


                            textSpeech1 = model.getTitle();
                            textSpeechDesc1 = model.getDescription();

                            if ("Education".equals(model.getCategory())) {
                                viewHolder.cardViewMyLastReminder.setBackgroundColor(Color.parseColor("#FFB3B5"));
                                viewHolder.title.setTextColor(Color.parseColor("#000000"));
                                viewHolder.description.setTextColor(Color.parseColor("#000000"));

                            } else if ("Shopping".equals(model.getCategory())) {
                                viewHolder.cardViewMyLastReminder.setBackgroundColor(Color.parseColor("#ff0000"));
                                viewHolder.title.setTextColor(Color.parseColor("#000000"));
                                viewHolder.description.setTextColor(Color.parseColor("#000000"));
                            } else if ("Personal".equals(model.getCategory())) {
                                viewHolder.cardViewMyLastReminder.setBackgroundColor(Color.parseColor("#F8BC48"));
                                viewHolder.description.setTextColor(Color.parseColor("#000000"));
                                viewHolder.title.setTextColor(Color.parseColor("#000000"));
                            } else if ("Other".equals(model.getCategory())) {
                                viewHolder.cardViewMyLastReminder.setBackgroundColor(Color.parseColor("#5C3F3F"));
                                viewHolder.description.setTextColor(Color.parseColor("#000000"));
                                viewHolder.title.setTextColor(Color.parseColor("#000000"));
                            } else if ("Job".equals(model.getCategory())) {
                                viewHolder.cardViewMyLastReminder.setBackgroundColor(Color.parseColor("#EDE0DF"));
                                viewHolder.description.setTextColor(Color.parseColor("#000000"));
                                viewHolder.title.setTextColor(Color.parseColor("#000000"));
                            } else if ("Social".equals(model.getCategory())) {
                                viewHolder.cardViewMyLastReminder.setBackgroundColor(Color.parseColor("#EDE0DF"));
                                viewHolder.description.setTextColor(Color.parseColor("#000000"));
                                viewHolder.title.setTextColor(Color.parseColor("#000000"));
                            } else if ("Finance".equals(model.getCategory())) {
                                viewHolder.cardViewMyLastReminder.setBackgroundColor(Color.parseColor("#322B1D"));
                                viewHolder.description.setTextColor(Color.parseColor("#ffffff"));
                                viewHolder.title.setTextColor(Color.parseColor("#ffffff"));
                            }

                            viewHolder.completeButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DatabaseReference itemRef = reference.child(getRef(position).getKey());
                                    itemRef.child("status").setValue("Complete");
                                    viewHolder.completeStatus.setTextColor(Color.parseColor("#FFFFFF"));


                                }
                            });


                            viewHolder.pencilButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Düzenleme arayüzünü açmak için bir AlertDialog kullanabilirsiniz
                                    final Dialog dialog = new Dialog(getContext());
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setContentView(R.layout.edit_reminder_litem);

                                    EditText changeTitleBox = dialog.findViewById(R.id.changeTitleBox);
                                    EditText changeDescBox = dialog.findViewById(R.id.changeDescBox);
                                    RelativeLayout buttonChange = dialog.findViewById(R.id.relativeOkeyButton);
                                    RelativeLayout buttonCancel = dialog.findViewById(R.id.relativeCancelButton);



                                    changeTitleBox.setText(model.getTitle());
                                    changeDescBox.setText(model.getDescription());

                                    buttonChange.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String newTitle = changeTitleBox.getText().toString();
                                            String newDesc = changeDescBox.getText().toString();

                                            DatabaseReference itemRef = reference.child(getRef(position).getKey());
                                            itemRef.child("title").setValue(newTitle);
                                            itemRef.child("description").setValue(newDesc);

                                            notifyDataSetChanged();
                                        }
                                    });


                                    buttonCancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });







                                    dialog.show();
                                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                    dialog.getWindow().setGravity(Gravity.CENTER);
                                }
                            });

                            viewHolder.voiceButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    speakNotCompleted();
                                }
                            });

                        }

                        @Override
                        public ReminderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            View itemView = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.reminder_item, parent, false);
                            return new ReminderViewHolder(itemView);
                        }
                    };
                    adapter.startListening();
                    recyclerViewRemindersNotYet.setAdapter(adapter);


                }
                checkRecyclerViewIsEmpty();

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference reference1 = database1.getReference("Premium").child(userId);

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String premium = dataSnapshot.child("premium").getValue(String.class);
                    if (premium.equals("yes")){
                        unlockExclusiveButton.setVisibility(View.GONE);
                    }else {
                        unlockExclusiveButton.setVisibility(View.VISIBLE);
                    }

                }else {
                    unlockExclusiveButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });




    }


    private void speakNotCompleted(){
        mTTS.speak("title: "+textSpeech1 + "  " +"description: " + textSpeechDesc1,TextToSpeech.QUEUE_FLUSH,null);



    }



    private void checkRecyclerViewIsEmpty() {
        Log.d("LOG", "checkRecyclerViewIsEmpty: çalıştı");

        Log.d("LOG", "checkRecyclerViewIsEmpty: "+matchedIds);

       if (matchedIds == null || matchedIds.isEmpty()){
           lottieAnimationView.setVisibility(View.VISIBLE);
           Log.d("LOG", "checkRecyclerViewIsEmpty: rec boş");
       }else {
           lottieAnimationView.setVisibility(View.GONE);
           Log.d("LOG", "checkRecyclerViewIsEmpty: rec dolu");
       }
    }

    private boolean isWeekPassed(long lastAlarmTimestamp) {
        // Kullanıcının son alarmını eklediği tarihi al
        LocalDateTime lastAlarmDate = Instant.ofEpochMilli(lastAlarmTimestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        // Şu anki tarihi al
        LocalDateTime currentDate = LocalDateTime.now();

        // Son alarm tarihinden bu yana geçen gün sayısını kontrol et
        long daysPassed = java.time.temporal.ChronoUnit.DAYS.between(lastAlarmDate, currentDate);

        // Eğer geçen gün sayısı 7'den büyükse bir hafta geçmiştir
        return daysPassed >= 7;
    }


    private static LocalDate gunuBul(String gunAdi) {
        Log.d("LOGGG", "gunuBul: main Çalıştı");
        LocalDate simdikiTarih = LocalDate.now();
        LocalDate bulunanTarih = simdikiTarih.with(DayOfWeek.valueOf(gunAdi));

        if (bulunanTarih.isBefore(simdikiTarih) || bulunanTarih.isEqual(simdikiTarih)){
            bulunanTarih = bulunanTarih.plusDays(0);
            Log.d("LOGGG", "gunuBul: "+bulunanTarih);
        }
        return bulunanTarih;

    }

    private void secilenGunleriGuncelle() {
        for (String secilenGun : secilenGunlerListesi) {
            LocalDate bulunanTarih = gunuBul(secilenGun);
            Log.d("GUN", "Tarih: " + bulunanTarih.toString() + " - Gun Adi: " + secilenGun);
            Log.d("GUN1", "secilenGunleriGuncelle: çalıştı"+ bulunanTarih);


        }
    }

}
