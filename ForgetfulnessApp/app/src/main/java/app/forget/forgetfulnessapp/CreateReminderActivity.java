package app.forget.forgetfulnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import app.forget.forgetfulnessapp.Alarm.AlarmReceiver;
import app.forget.forgetfulnessapp.CreateReminder.CreateReminderLocationFragment;
import app.forget.forgetfulnessapp.CreateReminder.CreateReminderTimeFragment;
import app.forget.forgetfulnessapp.CreateReminder.PreviousReminderFragment;
import app.forget.forgetfulnessapp.CreateReminder.VPAdapter;

public class CreateReminderActivity extends AppCompatActivity {


    private TabLayout tabLayout;
    private ViewPager viewPager;


    public double latitude, longitude;
    public String intervals, description, title;
    public String time;
    public String date;
    String selectCategory1;
    RelativeLayout addReminderButton, setReminderButton;
    FragmentManager fragmentManager;
    TextView dateButton, timeButton, textIntervalsCertain,seeAllReminders;
    private AutocompleteSupportFragment autocompleteFragment;


    EditText edtReminderTitle, edtReminderDesc;
    PendingIntent pendingIntent;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private static final int REQUEST_CODE_SPEECH_INPUT_DESC = 1001;

    String[] categoryList = {"Personal","Social","Education","Job","Finance","Shopping","Other"};

    TextView myLastReminderTitle, myLastReminderDesc,statusIndicator;
    ImageView myLastReminderImage,voiceButton,editButton;

    TextToSpeech mTTS;

    String textSpeech,textSpeechDesc;
    String textSpeech1,textSpeechDesc1;

    RelativeLayout statusCompleteButton;

    String key,animClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_reminder);



        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        viewPager.setBackgroundColor(Color.WHITE);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#000000"));
        tabLayout.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));

        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new CreateReminderTimeFragment(),getString(R.string.by_time));
        vpAdapter.addFragment(new CreateReminderLocationFragment(), getString(R.string.by_location));
        vpAdapter.addFragment(new PreviousReminderFragment(), getString(R.string.previous_reminder));
        viewPager.setAdapter(vpAdapter);



    }
}