package app.forget.forgetfulnessapp.CreateReminder;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.icu.util.LocaleData;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import app.forget.forgetfulnessapp.NumberPickerText;
import app.forget.forgetfulnessapp.R;
import io.reactivex.rxjava3.annotations.NonNull;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateReminderTimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateReminderTimeFragment extends Fragment implements NumberPicker.OnValueChangeListener{
    private List<String> secilenGunlerListesi = new ArrayList<>();


    public double latitude, longitude;
    public String intervals, description, title;
    public String time;
    public String date;
    String selectCategory1;
    RelativeLayout addReminderButton, setReminderButton;
    FragmentManager fragmentManager;
    private AutocompleteSupportFragment autocompleteFragment;
    TextView dateButton,selectedReminderDate;
    ImageView dateButton1;
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


    RelativeLayout relativeSaturday, relativeMonday, relativeFriday, relativeSunday, relativeWednesday, relativeThursday, relativeTuesday;


    private boolean isBackgroundChanged = false; // Değişiklik durumunu takip etmek için bayrak
    private boolean isBackgroundChanged1 = false; // Değişiklik durumunu takip etmek için bayrak
    private boolean isBackgroundChanged2 = false; // Değişiklik durumunu takip etmek için bayrak
    private boolean isBackgroundChanged3 = false; // Değişiklik durumunu takip etmek için bayrak
    private boolean isBackgroundChanged4 = false; // Değişiklik durumunu takip etmek için bayrak
    private boolean isBackgroundChanged5 = false; // Değişiklik durumunu takip etmek için bayrak
    private boolean isBackgroundChanged6 = false; // Değişiklik durumunu takip etmek için bayrak


    private String selectedDays = "";
    private String selectedDate = "";

    ImageView imageView;

    Uri imageUri;
    UploadTask uploadTask;
    StorageReference storageReference;

    FirebaseDatabase databases = FirebaseDatabase.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    private static final int PICK_IMAGE = 1;
    String currentUserId,animClick;

    LottieAnimationView lottieClickTitle, clickDesc,clickAnimMicTitle,clickAnimTime, clickAnimDate,clickAnimButton,clickAnimCategory,clickAnimDay;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateReminderTimeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateReminderTimeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateReminderTimeFragment newInstance(String param1, String param2) {
        CreateReminderTimeFragment fragment = new CreateReminderTimeFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_reminder_time, container, false);



        ConstraintLayout selectReminderSound = view.findViewById(R.id.constraintSelectSound);
        relativeSaturday = view.findViewById(R.id.relativeSaturday);
        relativeMonday = view.findViewById(R.id.relativeMonday);
        relativeFriday = view.findViewById(R.id.relativeFriday);
        relativeSunday = view.findViewById(R.id.relativeSunday);
        relativeWednesday = view.findViewById(R.id.relativeWednesday);
        relativeThursday = view.findViewById(R.id.relativeThursday);
        relativeTuesday = view.findViewById(R.id.relativeTuesday);

        lottieClickTitle = view.findViewById(R.id.clickAnimTitle);
        clickDesc = view.findViewById(R.id.clickAnimDesc);
        clickAnimTime = view.findViewById(R.id.clickAnimTime);
        clickAnimDate = view.findViewById(R.id.clickAnimDate);
        clickAnimButton = view.findViewById(R.id.clickAnimButton);
        clickAnimCategory = view.findViewById(R.id.clickAnimCategory);
        clickAnimDay = view.findViewById(R.id.clickAnimDay);
        clickAnimMicTitle = view.findViewById(R.id.clickAnimMicTitle);



        selectedReminderDate = view.findViewById(R.id.selectedReminderDate);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = user.getUid();

       /* documentReference = db.collection("reminderPhoto").document(currentUserId);
        storageReference = FirebaseStorage.getInstance().getReference("Reminder images");


        imageView = view.findViewById(R.id.iv_cp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_IMAGE);

            }
        });

        */


        if (getActivity() != null) {
            Intent intent = getActivity().getIntent();
            if (intent != null) {
                animClick = intent.getStringExtra("animClick");
                if (animClick != null && animClick.equals("animClick")) {
                    lottieClickTitle.setVisibility(View.VISIBLE);
                    clickDialogTitle();
                }
            }
        }








        init();
        dateButton = view.findViewById(R.id.dateButton);
        dateButton1 = view.findViewById(R.id.dateButton1);

        // Bugünün tarihini al
        Date today = new Date();

        // Tarih formatını belirle
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        // Tarihi istediğiniz formatta bir stringe çevir
        String formattedDate = dateFormat.format(today);

        dateButton.setText(formattedDate);

        date = formattedDate;







        NumberPicker numberPicker = view.findViewById(R.id.numberPickerHour);
        NumberPicker numberPickerMinute = view.findViewById(R.id.numberPickerMinute);

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(23);
        numberPicker.setValue(7);

        numberPickerMinute.setMinValue(0);
        numberPickerMinute.setMaxValue(59);

        numberPickerMinute.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d",i);
            }
        });

        numberPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d",i);

            }
        });
        numberPicker.setOnValueChangedListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            numberPicker.setTextColor(Color.BLACK);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            numberPickerMinute.setTextColor(Color.BLACK);
        }

        setReminderButton = view.findViewById(R.id.relativeSetReminderButton);
        edtReminderDesc = view.findViewById(R.id.edtReminderDesc);
        edtReminderTitle = view.findViewById(R.id.edtReminderTitle);


        selectReminderSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SelectAlarmSoundActivity.class);
                startActivity(intent);
            }
        });

        //SESLI

        ImageView micTitle = view.findViewById(R.id.micTitle);
        ImageView micDesc = view.findViewById(R.id.micDescription);

        //Select Category
        NumberPicker selectCategory = view.findViewById(R.id.textSelectCategory);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            selectCategory.setTextColor(Color.BLACK);
        }

        NumberPickerText.initTextCategory(getContext());
        selectCategory.setMaxValue(NumberPickerText.getTextArrayList().size()-1);
        selectCategory.setMinValue(0);
        selectCategory.setDisplayedValues(NumberPickerText.categoryName());



        selectCategory.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                //String selectedCategoryName = NumberPickerText.getTextArrayList().get(i1).getName();
                selectCategory1 = NumberPickerText.getTextArrayList().get(i1).getEnglishName();

                // Bu değeri categoryList1'e at
                //selectCategory1 = selectedCategoryName;

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
        });*/


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

        String userId = user.getUid();

        Log.d("DENEME", "onReceive: " + userId);


        dateButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getActivity(),SelectAlarmSoundActivity.class);
                //startActivity(intent);
                showDatePicker();

            }
        });

        setReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                title = edtReminderTitle.getText().toString();
                description = edtReminderDesc.getText().toString();

                int selectedHour = numberPicker.getValue();
                int selectedMinute = numberPickerMinute.getValue();

                time = String.format("%02d:%02d", selectedHour, selectedMinute);

                if (secilenGunlerListesi.isEmpty()){
                    setReminderToDatabase();
                }else {
                    Log.d("DATABASE", "onClick: İKİNCİ VERSİYONA YÖNLENDİRİLDİ");
                    setReminderToDatabaseVersion2();

                }



            }
        });

        return view;
    }

    private void clickDialogTitle(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.intruction_click_layout);

        // İptal edilebilirlik özelliğini false olarak ayarla
        dialog.setCanceledOnTouchOutside(false);

        RelativeLayout buttonContinueTitle = dialog.findViewById(R.id.buttonContinueTitle);
        buttonContinueTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lottieClickTitle.setVisibility(View.GONE);
                clickDesc.setVisibility(View.VISIBLE);
                dialog.dismiss();
                clickDialogDesc();
            }
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation2;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

    private void clickDialogDesc(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.intruduction_click_desc_layout);

        // İptal edilebilirlik özelliğini false olarak ayarla
        dialog.setCanceledOnTouchOutside(false);

        RelativeLayout buttonContinueTitle = dialog.findViewById(R.id.buttonContinueTitle);
        buttonContinueTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickDesc.setVisibility(View.GONE);
                clickAnimTime.setVisibility(View.VISIBLE);
                dialog.dismiss();
                clickDialogTime();
            }
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation2;
        dialog.getWindow().setGravity(Gravity.BOTTOM);


    }

    private void clickDialogTime(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.intruduction_click_time_layout);

        // İptal edilebilirlik özelliğini false olarak ayarla
        dialog.setCanceledOnTouchOutside(false);

        RelativeLayout buttonContinueTitle = dialog.findViewById(R.id.buttonContinueTitle);
        buttonContinueTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAnimTime.setVisibility(View.GONE);
                clickAnimDate.setVisibility(View.VISIBLE);
                dialog.dismiss();
                clickDialogDate();
            }
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation2;
        dialog.getWindow().setGravity(Gravity.BOTTOM);


    }

    private void clickDialogDate(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.intruduction_click_date_layout);

        // İptal edilebilirlik özelliğini false olarak ayarla
        dialog.setCanceledOnTouchOutside(false);

        RelativeLayout buttonContinueTitle = dialog.findViewById(R.id.buttonContinueTitle);
        buttonContinueTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAnimDate.setVisibility(View.GONE);
                clickAnimDay.setVisibility(View.VISIBLE);
                dialog.dismiss();
                clickDialogDay();
            }
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation2;
        dialog.getWindow().setGravity(Gravity.BOTTOM);


    }

    private void clickDialogDay(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.intruduction_click_day_layout);

        // İptal edilebilirlik özelliğini false olarak ayarla
        dialog.setCanceledOnTouchOutside(false);

        RelativeLayout buttonContinueTitle = dialog.findViewById(R.id.buttonContinueTitle);
        buttonContinueTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAnimDay.setVisibility(View.GONE);
                clickAnimCategory.setVisibility(View.VISIBLE);
                dialog.dismiss();
                clickDialogCategory();

            }
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation2;
        dialog.getWindow().setGravity(Gravity.BOTTOM);


    }

    private void clickDialogCategory(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.intruduction_click_category_layout);

        // İptal edilebilirlik özelliğini false olarak ayarla
        dialog.setCanceledOnTouchOutside(false);

        RelativeLayout buttonContinueTitle = dialog.findViewById(R.id.buttonContinueTitle);
        buttonContinueTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAnimDay.setVisibility(View.GONE);
                clickAnimCategory.setVisibility(View.VISIBLE);
                dialog.dismiss();
                clickDialogButton();
            }
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation2;
        dialog.getWindow().setGravity(Gravity.BOTTOM);


    }

    private void clickDialogButton(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.intruduction_click_button_layout);

        // İptal edilebilirlik özelliğini false olarak ayarla
        dialog.setCanceledOnTouchOutside(false);

        RelativeLayout buttonContinueTitle = dialog.findViewById(R.id.buttonContinueTitle);
        buttonContinueTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAnimCategory.setVisibility(View.GONE);
                clickAnimButton.setVisibility(View.VISIBLE);
                dialog.dismiss();
                clickAnimButton.setVisibility(View.GONE);

            }
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation2;
        dialog.getWindow().setGravity(Gravity.BOTTOM);


    }

    private void init() {

        Drawable originalBackground = relativeWednesday.getBackground(); // Orijinal arka planı
        Drawable originalBackground1 = relativeSunday.getBackground(); // Orijinal arka planı al
        Drawable originalBackground2 = relativeSaturday.getBackground(); // Orijinal arka planı al
        Drawable originalBackground3 = relativeThursday.getBackground(); // Orijinal arka planı al
        Drawable originalBackground4 = relativeTuesday.getBackground(); // Orijinal arka planı al
        Drawable originalBackground5 = relativeMonday.getBackground(); // Orijinal arka planı al
        Drawable originalBackground6 = relativeFriday.getBackground(); // Orijinal arka planı al

        relativeSunday.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // İlk dokunmada arka planı değiştir
                        if (!isBackgroundChanged6) {
                            relativeSunday.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.week_of_day_background));
                            isBackgroundChanged6 = true;
                            secilenGunlerListesi.add(getString(R.string.p));



                        } else {
                            // İkinci dokunmada orijinal arka plana geri dön
                            relativeSunday.setBackground(originalBackground1);
                            isBackgroundChanged6 = false;
                            secilenGunlerListesi.remove(getString(R.string.p));
                        }
                        secilenGunleriGuncelle();
                        secilenGunleriYaz();
                        break;
                }
                return true;
            }
        });


        relativeTuesday.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // İlk dokunmada arka planı değiştir
                        if (!isBackgroundChanged4) {
                            relativeTuesday.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.week_of_day_background));
                            isBackgroundChanged4 = true;
                            secilenGunlerListesi.add(getString(R.string.t));


                        } else {
                            // İkinci dokunmada orijinal arka plana geri dön
                            relativeTuesday.setBackground(originalBackground4);
                            isBackgroundChanged4 = false;
                            secilenGunlerListesi.remove(getString(R.string.t));


                        }
                        secilenGunleriGuncelle();
                        secilenGunleriYaz();

                        break;
                }
                return true;
            }
        });


        relativeThursday.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // İlk dokunmada arka planı değiştir
                        if (!isBackgroundChanged1) {
                            relativeThursday.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.week_of_day_background));
                            isBackgroundChanged1 = true;
                            secilenGunlerListesi.add(getString(R.string.t1));

                        } else {
                            // İkinci dokunmada orijinal arka plana geri dön
                            relativeThursday.setBackground(originalBackground3);
                            isBackgroundChanged1 = false;
                            secilenGunlerListesi.remove(getString(R.string.t1));

                        }
                        secilenGunleriGuncelle();
                        secilenGunleriYaz();

                        break;
                }
                return true;
            }
        });


        relativeSaturday.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // İlk dokunmada arka planı değiştir
                        if (!isBackgroundChanged3) {
                            relativeSaturday.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.week_of_day_background));
                            isBackgroundChanged3 = true;
                            secilenGunlerListesi.add(getString(R.string.s));

                        } else {
                            // İkinci dokunmada orijinal arka plana geri dön
                            relativeSaturday.setBackground(originalBackground2);
                            isBackgroundChanged3 = false;
                            secilenGunlerListesi.remove(getString(R.string.s));


                        }
                        secilenGunleriGuncelle();
                        secilenGunleriYaz();

                        break;
                }
                return true;
            }
        });


        relativeMonday.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // İlk dokunmada arka planı değiştir
                        if (!isBackgroundChanged2) {
                            relativeMonday.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.week_of_day_background));
                            isBackgroundChanged2 = true;
                            secilenGunlerListesi.add(getString(R.string.m));

                        } else {
                            // İkinci dokunmada orijinal arka plana geri dön
                            relativeMonday.setBackground(originalBackground5);
                            isBackgroundChanged2 = false;
                            secilenGunlerListesi.remove(getString(R.string.m));

                        }
                        secilenGunleriGuncelle();
                        secilenGunleriYaz();

                        break;
                }
                return true;
            }
        });


        relativeFriday.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // İlk dokunmada arka planı değiştir
                        if (!isBackgroundChanged5) {
                            relativeFriday.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.week_of_day_background));
                            isBackgroundChanged5 = true;
                            secilenGunlerListesi.add(getString(R.string.f));

                        } else {
                            // İkinci dokunmada orijinal arka plana geri dön
                            relativeFriday.setBackground(originalBackground6);
                            isBackgroundChanged5 = false;
                            secilenGunlerListesi.remove(getString(R.string.f));

                        }
                        secilenGunleriGuncelle();
                        secilenGunleriYaz();

                        break;
                }
                return true;
            }
        });


        relativeWednesday.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // İlk dokunmada arka planı değiştir
                        if (!isBackgroundChanged) {
                            relativeWednesday.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.week_of_day_background));
                            isBackgroundChanged = true;
                            secilenGunlerListesi.add(getString(R.string.w));

                        } else {
                            // İkinci dokunmada orijinal arka plana geri dön
                            relativeWednesday.setBackground(originalBackground);
                            isBackgroundChanged = false;
                            secilenGunlerListesi.remove(getString(R.string.w));

                        }
                        secilenGunleriGuncelle();
                        secilenGunleriYaz();

                        break;
                }
                return true;
            }
        });

    }

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
                        dateButton.setText(String.valueOf(year + "-" + month + "-" + day));

                        selectedReminderDate.setText(String.valueOf(year+"/"+month+"/"+day));
                        isBackgroundChanged = false;
                        isBackgroundChanged1 = false;
                        isBackgroundChanged2 = false;
                        isBackgroundChanged3 = false;
                        isBackgroundChanged4 = false;
                        isBackgroundChanged5 = false;
                        isBackgroundChanged6 = false;
                        relativeSunday.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.week_of_day_unselected_background));
                        relativeMonday.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.week_of_day_unselected_background));
                        relativeThursday.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.week_of_day_unselected_background));
                        relativeTuesday.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.week_of_day_unselected_background));
                        relativeWednesday.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.week_of_day_unselected_background));
                        relativeFriday.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.week_of_day_unselected_background));
                        relativeSaturday.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.week_of_day_unselected_background));







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
                dateButton.setText(year + "-" + month + "-" + day);
                selectedReminderDate.setText(year+"/"+month+"/"+day);

                isBackgroundChanged = false;
                isBackgroundChanged1 = false;
                isBackgroundChanged2 = false;
                isBackgroundChanged3 = false;
                isBackgroundChanged4 = false;
                isBackgroundChanged5 = false;
                isBackgroundChanged6 = false;
                relativeSunday.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.week_of_day_unselected_background));
                relativeMonday.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.week_of_day_unselected_background));
                relativeThursday.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.week_of_day_unselected_background));
                relativeTuesday.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.week_of_day_unselected_background));
                relativeWednesday.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.week_of_day_unselected_background));
                relativeFriday.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.week_of_day_unselected_background));
                relativeSaturday.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.week_of_day_unselected_background));

                secilenGunlerListesi.clear();


                date = dateButton.getText().toString();

                // Saat seçimi yapılıyorsa, saat bilgisini de alıp ilgili TextView'a yazabilirsiniz
                // Örnek olarak saat seçimi için bir TimePickerDialog kullanılıyorsa, o dialogdan saat bilgisini alabilirsiniz.

                // Tarih seçimi tamamlandığında yapılacak işlemler buraya eklenir (opsiyonel)
                dialog.dismiss(); // Dialogu kapat
            }
        });


        datePickerDialog.show();
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
                        userReminder.put("intervals", intervals);
                        userReminder.put("date", date);
                        userReminder.put("time", time);
                        userReminder.put("longitude", 0);
                        userReminder.put("latitude",0);
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
                                    Log.d("FIREBASEUPLOAD", "onComplete: BİRİNCİ ");
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
                                            Toast.makeText(getContext(), getString(R.string.upgrade_info_reminder_toast), Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Alarm kurma izni ver
                                            String reminderUid = FirebaseDatabase.getInstance().getReference("Reminder").push().getKey();

                                            Map<String, Object> userReminder = new HashMap<>();
                                            userReminder.put("title", title);
                                            userReminder.put("description", description);
                                            userReminder.put("intervals", intervals);
                                            userReminder.put("date", date);
                                            userReminder.put("time", time);
                                            userReminder.put("longitude", 0);
                                            userReminder.put("latitude",0);
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
                                                        Toast.makeText(getContext(), getString(R.string.reminderinstalled), Toast.LENGTH_SHORT).show();
                                                        Log.d("FIREBASEUPLOAD", "onComplete: İKİNCİ ");

                                                    } else {
                                                        Toast.makeText(getContext(), getString(R.string.remindernotinstalled), Toast.LENGTH_SHORT).show();
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
                                        userReminder.put("intervals", intervals);
                                        userReminder.put("date", date);
                                        userReminder.put("time", time);
                                        userReminder.put("longitude", 0);
                                        userReminder.put("latitude",0);
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
                                                    Toast.makeText(getContext(), getString(R.string.reminderinstalled), Toast.LENGTH_SHORT).show();
                                                    Log.d("FIREBASEUPLOAD", "onComplete: ÜÇÜNCÜ ");

                                                } else {
                                                    Toast.makeText(getContext(), getString(R.string.remindernotinstalled), Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(getContext(), getString(R.string.upgrade_info_reminder_toast), Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Alarm kurma izni ver
                                        String reminderUid = FirebaseDatabase.getInstance().getReference("Reminder").push().getKey();

                                        Map<String, Object> userReminder = new HashMap<>();
                                        userReminder.put("title", title);
                                        userReminder.put("description", description);
                                        userReminder.put("intervals", intervals);
                                        userReminder.put("date", date);
                                        userReminder.put("longitude", 0);
                                        userReminder.put("latitude",0);
                                        userReminder.put("time", time);
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
                                                    Toast.makeText(getContext(), getString(R.string.reminderinstalled), Toast.LENGTH_SHORT).show();
                                                    Log.d("FIREBASEUPLOAD", "onComplete: DÖRDÜNCÜ ");

                                                } else {
                                                    Toast.makeText(getContext(), getString(R.string.remindernotinstalled), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }

                                } else {
                                    // En az iki tarih yoksa alarm kurma izni ver
                                    String reminderUid = FirebaseDatabase.getInstance().getReference("Reminder").push().getKey();

                                    Map<String, Object> userReminder = new HashMap<>();
                                    userReminder.put("title", title);
                                    userReminder.put("description", description);
                                    userReminder.put("intervals", intervals);
                                    userReminder.put("date", date);
                                    userReminder.put("time", time);
                                    userReminder.put("longitude", 0);
                                    userReminder.put("latitude",0);
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
                                                Toast.makeText(getContext(), getString(R.string.reminderinstalled), Toast.LENGTH_SHORT).show();
                                                Log.d("FIREBASEUPLOAD", "onComplete: BEŞİNCİ ");

                                            } else {
                                                Toast.makeText(getContext(), getString(R.string.remindernotinstalled), Toast.LENGTH_SHORT).show();
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

    private void setReminderToDatabaseVersion2(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        Date now = new Date();

        // Date nesnesini istediğiniz biçime dönüştürün
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(now);


        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        DatabaseReference reference2 = database2.getReference("Premium").child(userid);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = sdf.format(new Date());
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String premium = snapshot.child("premium").getValue(String.class);
                    if (premium.equals("yes")){
                        String reminderUid = FirebaseDatabase.getInstance().getReference("Reminder").push().getKey();



                        Map<String, Object> userReminder = new HashMap<>();
                        userReminder.put("title", title);
                        userReminder.put("description", description);
                        userReminder.put("intervals", intervals);
                        userReminder.put("date", "0");
                        userReminder.put("Monday", secilenGunlerListesi.contains(getString(R.string.m)) ? "yes" : "no");
                        userReminder.put("Saturday", secilenGunlerListesi.contains(getString(R.string.s)) ? "yes" : "no");
                        userReminder.put("Wednesday", secilenGunlerListesi.contains(getString(R.string.w)) ? "yes" : "no");
                        userReminder.put("Friday", secilenGunlerListesi.contains(getString(R.string.f)) ? "yes" : "no");
                        userReminder.put("Sunday", secilenGunlerListesi.contains(getString(R.string.p)) ? "yes" : "no");
                        userReminder.put("Tuesday", secilenGunlerListesi.contains(getString(R.string.t)) ? "yes" : "no");
                        userReminder.put("Thursday", secilenGunlerListesi.contains(getString(R.string.t1)) ? "yes" : "no");
                        userReminder.put("reminderDeadline",currentDateTime);
                        userReminder.put("time", time);
                        userReminder.put("longitude", 0);
                        userReminder.put("latitude",0);
                        userReminder.put("category", selectCategory1);
                        userReminder.put("status","not completed");
                        userReminder.put("creationDate",formattedDate);
                        userReminder.put("id",reminderUid);
                        userReminder.put("type","regular");
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
                        DatabaseReference reference2 = database.getReference("Reminder").child(userid).child(reminderUid);


                        reference2.setValue(userReminder, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError error, DatabaseReference ref) {
                                if (error == null) {
                                    Toast.makeText(getContext(), getString(R.string.reminderinstalled), Toast.LENGTH_SHORT).show();
                                    Log.d("FIREBASEUPLOAD", "onComplete: ALTINCI ");

                                } else {
                                    Toast.makeText(getContext(), getString(R.string.remindernotinstalled), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(getContext(), getString(R.string.upgrade_info_reminder_toast), Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Alarm kurma izni ver
                                            String reminderUid = FirebaseDatabase.getInstance().getReference("Reminder").push().getKey();

                                            Map<String, Object> userReminder = new HashMap<>();
                                            userReminder.put("title", title);
                                            userReminder.put("description", description);
                                            userReminder.put("intervals", intervals);
                                            userReminder.put("date", 0);
                                            userReminder.put("reminderDeadline",currentDateTime);
                                            userReminder.put("Monday", secilenGunlerListesi.contains(getString(R.string.p)) ? "yes" : "no");
                                            userReminder.put("Saturday", secilenGunlerListesi.contains(getString(R.string.s)) ? "yes" : "no");
                                            userReminder.put("Wednesday", secilenGunlerListesi.contains(getString(R.string.w)) ? "yes" : "no");
                                            userReminder.put("Friday", secilenGunlerListesi.contains(getString(R.string.f)) ? "yes" : "no");
                                            userReminder.put("Sunday", secilenGunlerListesi.contains(getString(R.string.p)) ? "yes" : "no");
                                            userReminder.put("Tuesday", secilenGunlerListesi.contains(getString(R.string.t)) ? "yes" : "no");
                                            userReminder.put("Thursday", secilenGunlerListesi.contains(getString(R.string.t1)) ? "yes" : "no");
                                            userReminder.put("time", time);
                                            userReminder.put("longitude", 0);
                                            userReminder.put("latitude",0);
                                            userReminder.put("category", selectCategory1);
                                            userReminder.put("status","not completed");
                                            userReminder.put("creationDate",formattedDate);
                                            userReminder.put("id",reminderUid);
                                            userReminder.put("type","regular");
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
                                                        Toast.makeText(getContext(), getString(R.string.reminderinstalled), Toast.LENGTH_SHORT).show();
                                                        Log.d("FIREBASEUPLOAD", "onComplete: YEDİNCİ ");

                                                    } else {
                                                        Toast.makeText(getContext(), getString(R.string.remindernotinstalled), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }

                                    } else {
                                        // En az iki tarih yoksa alarm kurma izni ver
                                        String reminderUid = FirebaseDatabase.getInstance().getReference("Reminder").push().getKey();

                                        Map<String, Object> userReminder = new HashMap<>();
                                        userReminder.put("title", title);
                                        userReminder.put("description", description);
                                        userReminder.put("intervals", intervals);
                                        userReminder.put("date", "0");
                                        userReminder.put("reminderDeadline",currentDateTime);
                                        userReminder.put("Monday", secilenGunlerListesi.contains(getString(R.string.p)) ? "yes" : "no");
                                        userReminder.put("Saturday", secilenGunlerListesi.contains(getString(R.string.s)) ? "yes" : "no");
                                        userReminder.put("Wednesday", secilenGunlerListesi.contains(getString(R.string.w)) ? "yes" : "no");
                                        userReminder.put("Friday", secilenGunlerListesi.contains(getString(R.string.f)) ? "yes" : "no");
                                        userReminder.put("Sunday", secilenGunlerListesi.contains(getString(R.string.p)) ? "yes" : "no");
                                        userReminder.put("Tuesday", secilenGunlerListesi.contains(getString(R.string.t)) ? "yes" : "no");
                                        userReminder.put("Thursday", secilenGunlerListesi.contains(getString(R.string.t1)) ? "yes" : "no");
                                        userReminder.put("time", time);
                                        userReminder.put("longitude", 0);
                                        userReminder.put("latitude",0);
                                        userReminder.put("category", selectCategory1);
                                        userReminder.put("status","not completed");
                                        userReminder.put("creationDate",formattedDate);
                                        userReminder.put("id",reminderUid);
                                        userReminder.put("type","regular");
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
                                                    Log.d("FIREBASEUPLOAD", "onComplete: SEKİZİNCİ ");

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
                                        Toast.makeText(getContext(), getString(R.string.upgrade_info_reminder_toast), Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Alarm kurma izni ver
                                        String reminderUid = FirebaseDatabase.getInstance().getReference("Reminder").push().getKey();

                                        Map<String, Object> userReminder = new HashMap<>();
                                        userReminder.put("title", title);
                                        userReminder.put("description", description);
                                        userReminder.put("intervals", intervals);
                                        userReminder.put("date", "0");
                                        userReminder.put("Monday", secilenGunlerListesi.contains(getString(R.string.p)) ? "yes" : "no");
                                        userReminder.put("Saturday", secilenGunlerListesi.contains(getString(R.string.s)) ? "yes" : "no");
                                        userReminder.put("Wednesday", secilenGunlerListesi.contains(getString(R.string.w)) ? "yes" : "no");
                                        userReminder.put("Friday", secilenGunlerListesi.contains(getString(R.string.f)) ? "yes" : "no");
                                        userReminder.put("Sunday", secilenGunlerListesi.contains(getString(R.string.p)) ? "yes" : "no");
                                        userReminder.put("Tuesday", secilenGunlerListesi.contains(getString(R.string.t)) ? "yes" : "no");
                                        userReminder.put("Thursday", secilenGunlerListesi.contains(getString(R.string.t1)) ? "yes" : "no");
                                        userReminder.put("longitude", 0);
                                        userReminder.put("latitude",0);
                                        userReminder.put("time", time);
                                        userReminder.put("reminderDeadline",currentDateTime);
                                        userReminder.put("category", selectCategory1);
                                        userReminder.put("status","not completed");
                                        userReminder.put("creationDate",formattedDate);
                                        userReminder.put("id",reminderUid);
                                        userReminder.put("type","regular");
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
                                                    Toast.makeText(getContext(), getString(R.string.reminderinstalled), Toast.LENGTH_SHORT).show();
                                                    Log.d("FIREBASEUPLOAD", "onComplete: DOKUZUNCU ");

                                                } else {
                                                    Toast.makeText(getContext(), getString(R.string.remindernotinstalled), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }

                                } else {
                                    // En az iki tarih yoksa alarm kurma izni ver
                                    String reminderUid = FirebaseDatabase.getInstance().getReference("Reminder").push().getKey();

                                    Map<String, Object> userReminder = new HashMap<>();
                                    userReminder.put("title", title);
                                    userReminder.put("description", description);
                                    userReminder.put("intervals", intervals);
                                    userReminder.put("date", "0");
                                    userReminder.put("Monday", secilenGunlerListesi.contains(getString(R.string.p)) ? "yes" : "no");
                                    userReminder.put("Saturday", secilenGunlerListesi.contains(getString(R.string.s)) ? "yes" : "no");
                                    userReminder.put("Wednesday", secilenGunlerListesi.contains(getString(R.string.w)) ? "yes" : "no");
                                    userReminder.put("Friday", secilenGunlerListesi.contains(getString(R.string.f)) ? "yes" : "no");
                                    userReminder.put("Sunday", secilenGunlerListesi.contains(getString(R.string.p)) ? "yes" : "no");
                                    userReminder.put("Tuesday", secilenGunlerListesi.contains(getString(R.string.t)) ? "yes" : "no");
                                    userReminder.put("Thursday", secilenGunlerListesi.contains(getString(R.string.t1)) ? "yes" : "no");
                                    userReminder.put("time", time);
                                    userReminder.put("longitude", 0);
                                    userReminder.put("reminderDeadline",currentDateTime);
                                    userReminder.put("latitude",0);
                                    userReminder.put("category", selectCategory1);
                                    userReminder.put("status","not completed");
                                    userReminder.put("creationDate",formattedDate);
                                    userReminder.put("id",reminderUid);
                                    userReminder.put("type","regular");
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
                                                Toast.makeText(getContext(), getString(R.string.reminderinstalled), Toast.LENGTH_SHORT).show();
                                                Log.d("FIREBASEUPLOAD", "onComplete: ONUNCU ");

                                            } else {
                                                Toast.makeText(getContext(), getString(R.string.remindernotinstalled), Toast.LENGTH_SHORT).show();
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


        /*OnActivity fotoğraf seçme
        try {
            if (requestCode == PICK_IMAGE || resultCode == RESULT_OK ||
                    data != null || data.getData() != null){
                imageUri = data.getData();

                Picasso.get().load(imageUri).into(imageView);

            }

        }catch (Exception e){
            Toast.makeText(getContext(), "Hata Kodu"+e, Toast.LENGTH_SHORT).show();
        }
        */

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

    //Fotoğraf seçme
    private String getFileExt(Uri uri){
        Context context = null;
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
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

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {

    }

    private LocalDate gunuBul(String gunAdi) {
        Map<String, DayOfWeek> turkishDayMapping = new HashMap<>();
        turkishDayMapping.put(getString(R.string.p), DayOfWeek.SUNDAY);
        turkishDayMapping.put(getString(R.string.m), DayOfWeek.MONDAY);
        turkishDayMapping.put(getString(R.string.t), DayOfWeek.TUESDAY);
        turkishDayMapping.put(getString(R.string.w), DayOfWeek.WEDNESDAY);
        turkishDayMapping.put(getString(R.string.t1), DayOfWeek.THURSDAY);
        turkishDayMapping.put(getString(R.string.f), DayOfWeek.FRIDAY);
        turkishDayMapping.put(getString(R.string.s), DayOfWeek.SATURDAY);

        LocalDate simdikiTarih = LocalDate.now();
        // Eşleme kullanarak doğru DayOfWeek enum değerini al
        DayOfWeek dayOfWeek = turkishDayMapping.get(gunAdi);
        if (dayOfWeek == null) {
            // Eşleme gün adını içermiyorsa hata işle
            throw new IllegalArgumentException("Geçersiz gün adı: " + gunAdi);
        }

        LocalDate bulunanTarih = simdikiTarih.with(dayOfWeek);

        // Eğer bulunan tarih şu andan önceyse, bir hafta ekleyerek en yakın gelecek tarihi bul
        if (bulunanTarih.isBefore(simdikiTarih) || bulunanTarih.isEqual(simdikiTarih)) {
            bulunanTarih = bulunanTarih.plusDays(0);

            Log.d("GUN50", "gunuBul: "+bulunanTarih.toString());
        }

        return bulunanTarih;
    }

    private void secilenGunleriGuncelle() {
        for (String secilenGun : secilenGunlerListesi) {
            LocalDate bulunanTarih = gunuBul(secilenGun);
            Log.d("son", "Tarih: " + bulunanTarih.toString() + " - Gun Adi: " + secilenGun);
        }
    }

    private void secilenGunleriYaz() {
        if (secilenGunlerListesi.isEmpty()) {
            selectedReminderDate.setText("");
        } else {
            selectedReminderDate.setText("Her " + TextUtils.join(", ", secilenGunlerListesi));
        }
    }



}