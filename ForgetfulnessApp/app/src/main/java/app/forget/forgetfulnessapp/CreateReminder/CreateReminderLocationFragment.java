package app.forget.forgetfulnessapp.CreateReminder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
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

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

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
import app.forget.forgetfulnessapp.NumberPickerText;
import app.forget.forgetfulnessapp.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateReminderLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateReminderLocationFragment extends Fragment {
    public double latitude, longitude;
    public String intervals, description, title;
    public String time;
    public String date;
    String selectCategory1;
    RelativeLayout addReminderButton, setReminderButton;
    FragmentManager fragmentManager;
    private AutocompleteSupportFragment autocompleteFragment;

    TextView dateButton, timeButton, textIntervalsCertain,seeAllReminders;



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

    String key;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateReminderLocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateReminderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateReminderLocationFragment newInstance(String param1, String param2) {
        CreateReminderLocationFragment fragment = new CreateReminderLocationFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_reminder, container, false);


        // Places API'yi başlatın
        fragmentManager = getChildFragmentManager();

        if (fragmentManager.findFragmentById(R.id.autocomplete_fragment2) == null) {
            autocompleteFragment = AutocompleteSupportFragment.newInstance();

            // Yeni bir ID atanabilir
            int fragmentContainerId = View.generateViewId();

            fragmentManager.beginTransaction()
                    .add(fragmentContainerId, autocompleteFragment)
                    .commit();
        }



        textIntervalsCertain = view.findViewById(R.id.textIntervalsCertain);


        setReminderButton = view.findViewById(R.id.relativeSetReminderButton);
        edtReminderDesc = view.findViewById(R.id.edtReminderDesc);
        edtReminderTitle = view.findViewById(R.id.edtReminderTitle);



        //SESLI

        ImageView micTitle = view.findViewById(R.id.micTitle);
        ImageView micDesc = view.findViewById(R.id.micDescription);

        NumberPicker selectCategory = view.findViewById(R.id.textSelectCategory);

        NumberPickerText.initTextCategory(getContext());
        selectCategory.setMaxValue(NumberPickerText.getTextArrayList().size()-1);
        selectCategory.setMinValue(0);
        selectCategory.setDisplayedValues(NumberPickerText.categoryName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            selectCategory.setTextColor(Color.BLACK);
        }


        selectCategory.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
               // String selectedCategoryName = NumberPickerText.getTextArrayList().get(i1).getName();
                selectCategory1 = NumberPickerText.getTextArrayList().get(i1).getEnglishName();

                // Bu değeri categoryList1'e at
               // selectCategory1 = selectedCategoryName;
            }
        });

        /*selectCategory.setOnClickListener(new View.OnClickListener() {
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

         */


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


        autocompleteFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment2);









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
        autocompleteFragment.setTypeFilter(null);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                if (place != null && place.getLatLng() != null) {
                    LatLng latLng = place.getLatLng();
                    latitude = latLng.latitude;
                    longitude = latLng.longitude;

                    // Enlem ve boylam bilgileri mevcut, kullanabilir
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


            }
        });

        return view;
    }

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
                        userReminder.put("category", selectCategory1);
                        userReminder.put("status","not completed");
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

                        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                        DatabaseReference reference1 = database1.getReference("Reminder").child(userid).child(reminderUid);


                        reference1.setValue(userReminder, new DatabaseReference.CompletionListener() {
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
                        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
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
                                            userReminder.put("category", selectCategory1);
                                            userReminder.put("status","not completed");
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
                                        userReminder.put("category", selectCategory1);
                                        userReminder.put("status","not completed");
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
                }else {
                    //ID Bulunmuyorsa
                    FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                    DatabaseReference reference2 = database2.getReference("Reminder").child(userid);
                    reference2.addListenerForSingleValueEvent(new ValueEventListener() {
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
                                        userReminder.put("category", selectCategory1);
                                        userReminder.put("status","not completed");
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
                                    userReminder.put("category", selectCategory1);
                                    userReminder.put("status","not completed");
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
        Intent alarmIntent = new Intent(getActivity(), AlarmReceiver.class);
        if (Build.VERSION.SDK_INT >= 31) {
            pendingIntent = PendingIntent.getBroadcast(getContext(), 0, alarmIntent, PendingIntent.FLAG_MUTABLE);
        }else {
            pendingIntent = PendingIntent.getBroadcast(getContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);


        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

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

    }
}