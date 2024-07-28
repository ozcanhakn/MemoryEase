package app.forget.forgetfulnessapp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import app.forget.forgetfulnessapp.Example.DayAxisValueFormatter;
import io.reactivex.rxjava3.annotations.NonNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticFragment extends Fragment {

    RelativeLayout relativeWeek, relativeMonth, relativeAnnual;
    boolean isBackgroundChangedWeek = false;
    boolean isBackgroundChangedToday = false;
    boolean isBackgroundChangedMonth = false;
    TextView totalReminderAmount, totalReminderSetNumber;

    ImageView questionByOfTheWeek,questionByOfTheHours;

    BarChart barChart, statisticBarHour,statisticBarRegular,statisticBarSleep;


    TextView totalAmountFinance,totalAmountShopping,totalAmountEducation,totalAmountJob,totalAmountOther,totalAmountPersonal,totalAmountSocial;
    LinearLayout linearLayoutJob,linearLayoutFinance,linearLayoutEducation,linearLayoutShopping,linearLayoutOther,linearLayoutPersonal,linearLayoutSocial;

    ScrollView scrollView;
    private boolean isNavigationBarHidden = false;
    private BottomNavigationView bottomNavigationView;

    TextView textShoppingObjectTopScoreStatistic,textShoppingGameTopScoreStatistic;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StatisticFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticFragment newInstance(String param1, String param2) {
        StatisticFragment fragment = new StatisticFragment();
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
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);


        bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);



        //Tanımlama
         relativeAnnual = view.findViewById(R.id.relativeMonth);
         relativeWeek = view.findViewById(R.id.relativeToday);
         relativeMonth = view.findViewById(R.id.relativeWeek);
         totalReminderAmount = view.findViewById(R.id.totalSetReminder);
         totalReminderSetNumber = view.findViewById(R.id.totalSetReminderNumber);
         totalAmountFinance = view.findViewById(R.id.totalAmountFinance);
         totalAmountShopping = view.findViewById(R.id.totalAmountShopping);
         totalAmountEducation = view.findViewById(R.id.totalAmountEducation);
         totalAmountOther = view.findViewById(R.id.totalAmountOther);
         totalAmountSocial = view.findViewById(R.id.totalAmountSocial);
         totalAmountPersonal = view.findViewById(R.id.totalAmountPersonal);
         totalAmountJob = view.findViewById(R.id.totalAmountJob);


         linearLayoutJob = view.findViewById(R.id.linearLayoutJob);
         linearLayoutFinance = view.findViewById(R.id.linearLayoutFinance);
         linearLayoutEducation = view.findViewById(R.id.linearLayoutEducation);
         linearLayoutShopping = view.findViewById(R.id.linearLayoutShopping);
         linearLayoutOther = view.findViewById(R.id.linerLayoutOther);
         linearLayoutPersonal = view.findViewById(R.id.linearLayoutPersonal);
         linearLayoutSocial = view.findViewById(R.id.linearLayoutSocial);

         questionByOfTheWeek = view.findViewById(R.id.questionByOfTheWeek);
         questionByOfTheHours = view.findViewById(R.id.questionByOfTheHours);

         scrollView = view.findViewById(R.id.scrollViewStatistic);


        barChart = view.findViewById(R.id.statisticBarWeek);
        statisticBarHour = view.findViewById(R.id.statisticBarHour);
        statisticBarRegular = view.findViewById(R.id.statisticBarRegular);
        statisticBarSleep = view.findViewById(R.id.denemeBar);

        textShoppingGameTopScoreStatistic = view.findViewById(R.id.textShoppingGameTopScoreStatistic);
        textShoppingObjectTopScoreStatistic = view.findViewById(R.id.textShoppingObjectTopScoreStatistic);


        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    // Aşağı kaydırma, Bottom Navigation Bar'ı gizle
                    if (!isNavigationBarHidden) {
                        isNavigationBarHidden = true;
                        hideBottomNavigationBar();
                    }
                } else if (scrollY < oldScrollY) {
                    // Yukarı kaydırma, Bottom Navigation Bar'ı göster
                    if (isNavigationBarHidden) {
                        isNavigationBarHidden = false;
                        showBottomNavigationBar();
                    }
                }
            }
        });

         questionByOfTheWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showQuestionByOfTheWeek();
            }
        });

         questionByOfTheHours.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 showQuestionByOfTheHours();
             }
         });











        byOfTheWeekTotal();
        byOfTheHourTotal();
        byOfTheRegularTotal();
        sleepByDayOfTheWeek();

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d("Debug", "Selected value: " + e.toString());
            }

            @Override
            public void onNothingSelected() {
                Log.d("Debug", "Nothing selected");
            }
        });



        loadGameTopScore();
        clickChangedBackground();


        return view;
    }

    private void loadGameTopScore(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();



        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("GameScore").child(userid).child("objectTopScore");
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("GameScore").child(userid).child("shopTopScore");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int objectTopScore = snapshot.getValue(Integer.class);
                    String textObjectTopScore = String.valueOf(objectTopScore);
                    if ( textObjectTopScore ==null){
                        textShoppingObjectTopScoreStatistic.setText("0");
                    }else {
                        textShoppingObjectTopScoreStatistic.setText(String.valueOf(objectTopScore));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int shoppingTopScore = snapshot.getValue(Integer.class);

                    String textShoppingTopScore = String.valueOf(shoppingTopScore);

                    if (textShoppingTopScore == null){
                        textShoppingGameTopScoreStatistic.setText("0");
                        textShoppingObjectTopScoreStatistic.setText("0");
                    }else {
                        textShoppingGameTopScoreStatistic.setText(String.valueOf(shoppingTopScore));
                    }

                }
                textShoppingGameTopScoreStatistic.setText("0");
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    private void showQuestionByOfTheHours() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.question_by_of_the_hour);
        //Buraya metin yazılacak.Diğer barChart'a da yaz.

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

    }

    private void showQuestionByOfTheWeek() {
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.question_by_of_the_week);

            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setGravity(Gravity.CENTER);


    }

    private void clickChangedBackground(){

        Drawable originalBackgroundWeek = relativeMonth.getBackground();
        Drawable originalBackgroundToday = relativeWeek.getBackground();
        Drawable originalBackgroundMonth = relativeAnnual.getBackground();


        relativeMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isBackgroundChangedWeek) {
                    // Arka planı değiştir
                    relativeMonth.setBackgroundResource(R.drawable.statistic_box_bg_upper_selected);
                    relativeAnnual.setBackgroundResource(R.drawable.statistic_box_bg_upper);
                    relativeWeek.setBackgroundResource(R.drawable.statistic_box_bg_upper);
                    totalReminderSetAmountMonth();



                    totalAmountPersonalMonth();
                    totalAmountEducationMonth();
                    totalAmountShoppingMonth();
                    totalAmountFinanceMonth();
                    totalAmountJobMonth();
                    totalAmountSocialMonth();
                    totalAmountOtherMonth();



                    linearLayoutEducation.setVisibility(View.VISIBLE);
                    totalReminderSetNumber.setVisibility(View.VISIBLE);
                    linearLayoutShopping.setVisibility(View.VISIBLE);
                    linearLayoutOther.setVisibility(View.VISIBLE);
                    linearLayoutSocial.setVisibility(View.VISIBLE);
                    linearLayoutPersonal.setVisibility(View.VISIBLE);
                    linearLayoutJob.setVisibility(View.VISIBLE);
                    linearLayoutFinance.setVisibility(View.VISIBLE);



                } else {
                    // Arka planı eski haline döndür
                    relativeMonth.setBackground(originalBackgroundWeek);
                }

                // Durumu tersine çevir
                isBackgroundChangedWeek = !isBackgroundChangedWeek;
            }
        });

        relativeWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isBackgroundChangedToday) {
                    // Arka planı değiştir
                    relativeWeek.setBackgroundResource(R.drawable.statistic_box_bg_upper_selected);
                    relativeAnnual.setBackgroundResource(R.drawable.statistic_box_bg_upper);
                    relativeMonth.setBackgroundResource(R.drawable.statistic_box_bg_upper);
                    totalReminderSetNumberWeek();

                    totalAmountEducationWeek();

                    totalAmountShoppingWeek();
                    totalAmountFinanceWeek();
                    totalAmountJobWeek();
                    totalAmountSocialWeek();
                    totalAmountOtherWeek();
                    totalAmountPersonalWeek();



                    totalReminderSetNumber.setVisibility(View.VISIBLE);
                    linearLayoutEducation.setVisibility(View.VISIBLE);
                    linearLayoutShopping.setVisibility(View.VISIBLE);
                    linearLayoutOther.setVisibility(View.VISIBLE);
                    linearLayoutSocial.setVisibility(View.VISIBLE);
                    linearLayoutPersonal.setVisibility(View.VISIBLE);
                    linearLayoutJob.setVisibility(View.VISIBLE);
                    linearLayoutFinance.setVisibility(View.VISIBLE);








                } else {
                    // Arka planı eski haline döndür
                    relativeWeek.setBackground(originalBackgroundToday);
                }

                // Durumu tersine çevir
                isBackgroundChangedToday = !isBackgroundChangedToday;
                isBackgroundChangedMonth = !isBackgroundChangedMonth;
            }
        });

        relativeAnnual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isBackgroundChangedMonth) {
                    // Arka planı değiştir
                    relativeAnnual.setBackgroundResource(R.drawable.statistic_box_bg_upper_selected);
                    relativeMonth.setBackgroundResource(R.drawable.statistic_box_bg_upper);
                    relativeWeek.setBackgroundResource(R.drawable.statistic_box_bg_upper);

                    totalReminderSetNumberYear();



                    totalAmountEducationAnnual();
                    totalAmountFinanceAnnual();
                    totalAmountJobAnnual();
                    totalAmountSocialAnnual();
                    totalAmountShoppingAnnual();
                    totalAmountPersonalAnnual();
                    totalAmountOtherAnnual();



                    totalReminderSetNumber.setVisibility(View.VISIBLE);
                    linearLayoutEducation.setVisibility(View.VISIBLE);
                    linearLayoutShopping.setVisibility(View.VISIBLE);
                    linearLayoutOther.setVisibility(View.VISIBLE);
                    linearLayoutSocial.setVisibility(View.VISIBLE);
                    linearLayoutPersonal.setVisibility(View.VISIBLE);
                    linearLayoutJob.setVisibility(View.VISIBLE);
                    linearLayoutFinance.setVisibility(View.VISIBLE);


                } else {
                    // Arka planı eski haline döndür
                    relativeAnnual.setBackground(originalBackgroundMonth);
                }

                // Durumu tersine çevir
                isBackgroundChangedMonth = !isBackgroundChangedMonth;
            }
        });
    }

    private void totalReminderSetNumberWeek(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid );

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long reminderAmount = 0;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Calendar oneMonthAgo = Calendar.getInstance();
                oneMonthAgo.add(Calendar.DAY_OF_MONTH, -7);

                for (DataSnapshot reminderType : snapshot.getChildren()){
                    DataSnapshot reminderAmountTotal = reminderType.child("type");
                    DataSnapshot reminderCreationDate = reminderType.child("creationDate");
                    String type = reminderAmountTotal.getValue(String.class);
                    String creationDateStr = reminderCreationDate.getValue(String.class);


                    Log.d("LOG", "onDataChange: "+type);

                    if (type != null && type.equals("alarm") || type.equals("regular")) {
                        try {
                            Date creationDate = dateFormat.parse(creationDateStr);
                            if (creationDate != null && creationDate.after(oneMonthAgo.getTime())) {
                                reminderAmount++;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                totalReminderSetNumber.setText(String.valueOf(reminderAmount));
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    private void totalReminderSetAmountMonth(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long reminderAmount = 0;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                Calendar oneMonthAgo = Calendar.getInstance();
                oneMonthAgo.add(Calendar.MONTH, -1);

                for (DataSnapshot reminderType : snapshot.getChildren()){
                    DataSnapshot reminderAmountTotal = reminderType.child("type");
                    DataSnapshot reminderCreationDate = reminderType.child("creationDate");
                    String type = reminderAmountTotal.getValue(String.class);
                    String creationDateStr = reminderCreationDate.getValue(String.class);


                    Log.d("LOG", "onDataChange: "+type);

                    if (type != null && type.equals("alarm") || type.equals("regular")) {
                        try {
                            Date creationDate = dateFormat.parse(creationDateStr);
                            if (creationDate != null && creationDate.after(oneMonthAgo.getTime())) {
                                reminderAmount++;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                totalReminderSetNumber.setText(String.valueOf(reminderAmount));
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });




    }

    private void totalReminderSetNumberYear(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid );

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long reminderAmount = 0;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                Calendar oneMonthAgo = Calendar.getInstance();
                oneMonthAgo.add(Calendar.DAY_OF_YEAR, -365);

                for (DataSnapshot reminderType : snapshot.getChildren()){
                    DataSnapshot reminderAmountTotal = reminderType.child("type");
                    DataSnapshot reminderCreationDate = reminderType.child("creationDate");
                    String type = reminderAmountTotal.getValue(String.class);
                    String creationDateStr = reminderCreationDate.getValue(String.class);


                    Log.d("LOG", "onDataChange: "+type);

                    if (type != null && type.equals("alarm") || type.equals("regular")) {
                        try {
                            Date creationDate = dateFormat.parse(creationDateStr);
                            if (creationDate != null && creationDate.after(oneMonthAgo.getTime())) {
                                reminderAmount++;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                totalReminderSetNumber.setText(String.valueOf(reminderAmount));
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void totalAmountFinance(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long totalFinanceAmountNumber = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DataSnapshot totalFinanceAmount = dataSnapshot.child("category");
                    String abc = totalFinanceAmount.getValue(String.class);

                    if (abc != null && abc.equals("Finance")){
                        totalFinanceAmountNumber++;
                    }
                }
                totalAmountFinance.setText(String.valueOf(totalFinanceAmountNumber));
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    private void totalAmountOther(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long totalFinanceAmountNumber = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DataSnapshot totalFinanceAmount = dataSnapshot.child("category");
                    String abc = totalFinanceAmount.getValue(String.class);

                    if (abc != null && abc.equals("Other")){
                        totalFinanceAmountNumber++;
                    }
                }
                totalAmountOther.setText(String.valueOf(totalFinanceAmountNumber));
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    private void totalAmountSocial(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long totalFinanceAmountNumber = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DataSnapshot totalFinanceAmount = dataSnapshot.child("category");
                    String abc = totalFinanceAmount.getValue(String.class);

                    if (abc != null && abc.equals("Social")){
                        totalFinanceAmountNumber++;
                    }
                }
                totalAmountSocial.setText(String.valueOf(totalFinanceAmountNumber));
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    private void totalAmountPersonal(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long totalFinanceAmountNumber = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DataSnapshot totalFinanceAmount = dataSnapshot.child("category");
                    String abc = totalFinanceAmount.getValue(String.class);

                    if (abc != null && abc.equals("Personal")){
                        totalFinanceAmountNumber++;
                    }
                }
                totalAmountPersonal.setText(String.valueOf(totalFinanceAmountNumber));
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    private void totalAmountJob(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long totalFinanceAmountNumber = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DataSnapshot totalFinanceAmount = dataSnapshot.child("category");
                    String abc = totalFinanceAmount.getValue(String.class);

                    if (abc != null && abc.equals("Job")){
                        totalFinanceAmountNumber++;
                    }
                }
                totalAmountJob.setText(String.valueOf(totalFinanceAmountNumber));
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    private void totalAmountShopping(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long totalFinanceAmountNumber = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DataSnapshot totalFinanceAmount = dataSnapshot.child("category");
                    String abc = totalFinanceAmount.getValue(String.class);

                    if (abc != null && abc.equals("Shopping")){
                        totalFinanceAmountNumber++;
                    }
                }
                totalAmountShopping.setText(String.valueOf(totalFinanceAmountNumber));
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    private void totalAmountEducation(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long totalFinanceAmountNumber = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DataSnapshot totalFinanceAmount = dataSnapshot.child("category");
                    String abc = totalFinanceAmount.getValue(String.class);

                    if (abc != null && abc.equals("Education")){
                        totalFinanceAmountNumber++;
                    }
                }
                totalAmountEducation.setText(String.valueOf(totalFinanceAmountNumber));
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    private void totalAmountEducationWeek(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long educationReminderAmount = 0; // Initialize the counter for Education reminders
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar oneMonthAgo = Calendar.getInstance();
                oneMonthAgo.add(Calendar.DAY_OF_MONTH, -7);

                for (DataSnapshot reminderType : snapshot.getChildren()) {
                    DataSnapshot reminderAmountTotal = reminderType.child("type");
                    DataSnapshot reminderCategory = reminderType.child("category"); // Add this line to get the category
                    DataSnapshot reminderDate = reminderType.child("creationDate");
                    String type = reminderAmountTotal.getValue(String.class);
                    String category = reminderCategory.getValue(String.class); // Get the category value
                    String dateStr = reminderDate.getValue(String.class);

                    Log.d("LOG", "onDataChange: " + type);

                    if (category != null && category.equals("Education") && reminderDate != null) {
                        try {
                            Date creationDate = dateFormat.parse(dateStr);
                            if (creationDate != null && creationDate.after(oneMonthAgo.getTime())) {
                                educationReminderAmount++;
                                Log.d("LOG03", "onDataChange: "+ educationReminderAmount);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                totalAmountEducation.setText(String.valueOf(educationReminderAmount)); // Set the count for Education reminders
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    private void totalAmountFinanceWeek(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long educationReminderAmount = 0; // Initialize the counter for Education reminders
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar oneMonthAgo = Calendar.getInstance();
                oneMonthAgo.add(Calendar.DAY_OF_MONTH, -7);

                for (DataSnapshot reminderType : snapshot.getChildren()) {
                    DataSnapshot reminderAmountTotal = reminderType.child("type");
                    DataSnapshot reminderCategory = reminderType.child("category"); // Add this line to get the category
                    DataSnapshot reminderCreationDate = reminderType.child("creationDate");
                    String type = reminderAmountTotal.getValue(String.class);
                    String category = reminderCategory.getValue(String.class); // Get the category value
                    String creationDateStr = reminderCreationDate.getValue(String.class);

                    Log.d("LOG", "onDataChange: " + type);

                    if (category != null && category.equals("Finance") && creationDateStr !=null) {
                        try {
                            Date creationDate = dateFormat.parse(creationDateStr);
                            if (creationDate != null && creationDate.after(oneMonthAgo.getTime())) {
                                educationReminderAmount++;
                                Log.d("LOG03", "onDataChange: "+ educationReminderAmount);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                totalAmountFinance.setText(String.valueOf(educationReminderAmount)); // Set the count for Education reminders
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    private void totalAmountShoppingWeek(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long educationReminderAmount = 0; // Initialize the counter for Education reminders
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar oneMonthAgo = Calendar.getInstance();
                oneMonthAgo.add(Calendar.DAY_OF_MONTH, -7);

                for (DataSnapshot reminderType : snapshot.getChildren()) {
                    DataSnapshot reminderAmountTotal = reminderType.child("type");
                    DataSnapshot reminderCategory = reminderType.child("category"); // Add this line to get the category
                    DataSnapshot reminderCreationDate = reminderType.child("creationDate");
                    String type = reminderAmountTotal.getValue(String.class);
                    String category = reminderCategory.getValue(String.class); // Get the category value
                    String creationDateStr = reminderCreationDate.getValue(String.class);

                    Log.d("LOG", "onDataChange: " + type);

                    if (category != null && category.equals("Shopping") && creationDateStr != null) {
                        try {
                            Date creationDate = dateFormat.parse(creationDateStr);
                            if (creationDate != null && creationDate.after(oneMonthAgo.getTime())) {
                                educationReminderAmount++;
                                Log.d("LOG03", "onDataChange: "+ educationReminderAmount);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                totalAmountShopping.setText(String.valueOf(educationReminderAmount)); // Set the count for Education reminders
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


    }

    private void totalAmountJobWeek(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long educationReminderAmount = 0; // Initialize the counter for Education reminders
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar oneMonthAgo = Calendar.getInstance();
                oneMonthAgo.add(Calendar.DAY_OF_MONTH, -7);

                for (DataSnapshot reminderType : snapshot.getChildren()) {
                    DataSnapshot reminderAmountTotal = reminderType.child("type");
                    DataSnapshot reminderCategory = reminderType.child("category"); // Add this line to get the category
                    DataSnapshot reminderCreationDate = reminderType.child("creationDate");
                    String type = reminderAmountTotal.getValue(String.class);
                    String category = reminderCategory.getValue(String.class); // Get the category value
                    String creationDateStr = reminderCreationDate.getValue(String.class);

                    Log.d("LOG", "onDataChange: " + type);

                    if (category != null && category.equals("Job") && creationDateStr !=null) {
                        try {
                            Date creationDate = dateFormat.parse(creationDateStr);
                            if (creationDate != null && creationDate.after(oneMonthAgo.getTime())) {
                                educationReminderAmount++;
                                Log.d("LOG03", "onDataChange: "+ educationReminderAmount);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                totalAmountJob.setText(String.valueOf(educationReminderAmount)); // Set the count for Education reminders
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    private void totalAmountPersonalWeek(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long educationReminderAmount = 0; // Initialize the counter for Education reminders
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar oneMonthAgo = Calendar.getInstance();
                oneMonthAgo.add(Calendar.DAY_OF_MONTH, -7);

                for (DataSnapshot reminderType : snapshot.getChildren()) {
                    DataSnapshot reminderAmountTotal = reminderType.child("type");
                    DataSnapshot reminderCategory = reminderType.child("category"); // Add this line to get the category
                    DataSnapshot reminderCreationDate = reminderType.child("creationDate");
                    String type = reminderAmountTotal.getValue(String.class);
                    String category = reminderCategory.getValue(String.class); // Get the category value
                    String creationDateStr = reminderCreationDate.getValue(String.class);

                    Log.d("LOG", "onDataChange: " + type);

                    if (category != null && category.equals("Personal") && creationDateStr != null) {
                        try {
                            Date creationDate = dateFormat.parse(creationDateStr);
                            if (creationDate != null && creationDate.after(oneMonthAgo.getTime())) {
                                educationReminderAmount++;
                                Log.d("LOG03", "onDataChange: "+ educationReminderAmount);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                totalAmountPersonal.setText(String.valueOf(educationReminderAmount)); // Set the count for Education reminders
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    private void totalAmountSocialWeek(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long educationReminderAmount = 0; // Initialize the counter for Education reminders
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar oneMonthAgo = Calendar.getInstance();
                oneMonthAgo.add(Calendar.DAY_OF_MONTH, -7);

                for (DataSnapshot reminderType : snapshot.getChildren()) {
                    DataSnapshot reminderAmountTotal = reminderType.child("type");
                    DataSnapshot reminderCategory = reminderType.child("category"); // Add this line to get the category
                    DataSnapshot reminderCreationDate = reminderType.child("creationDate");
                    String type = reminderAmountTotal.getValue(String.class);
                    String category = reminderCategory.getValue(String.class); // Get the category value
                    String creationDateStr = reminderCreationDate.getValue(String.class);

                    Log.d("LOG", "onDataChange: " + type);

                    if (category != null && category.equals("Social") && creationDateStr != null) {
                        try {
                            Date creationDate = dateFormat.parse(creationDateStr);
                            if (creationDate != null && creationDate.after(oneMonthAgo.getTime())) {
                                educationReminderAmount++;
                                Log.d("LOG03", "onDataChange: "+ educationReminderAmount);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                totalAmountSocial.setText(String.valueOf(educationReminderAmount)); // Set the count for Education reminders
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    private void totalAmountOtherWeek(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long educationReminderAmount = 0; // Initialize the counter for Education reminders
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar oneMonthAgo = Calendar.getInstance();
                oneMonthAgo.add(Calendar.DAY_OF_MONTH, -7);

                for (DataSnapshot reminderType : snapshot.getChildren()) {
                    DataSnapshot reminderAmountTotal = reminderType.child("type");
                    DataSnapshot reminderCategory = reminderType.child("category"); // Add this line to get the category
                    DataSnapshot reminderCreationDate = reminderType.child("creationDate");
                    String type = reminderAmountTotal.getValue(String.class);
                    String category = reminderCategory.getValue(String.class); // Get the category value
                    String creationDateStr = reminderCreationDate.getValue(String.class);

                    Log.d("LOG", "onDataChange: " + type);

                    if (category != null && category.equals("Other") && creationDateStr!=null) {
                        try {
                            Date creationDate = dateFormat.parse(creationDateStr);
                            if (creationDate != null && creationDate.after(oneMonthAgo.getTime())) {
                                educationReminderAmount++;
                                Log.d("LOG03", "onDataChange: "+ educationReminderAmount);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                totalAmountOther.setText(String.valueOf(educationReminderAmount)); // Set the count for Education reminders
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    private void totalAmountEducationMonth(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long educationReminderAmount = 0; // Initialize the counter for Education reminders
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar oneMonthAgo = Calendar.getInstance();
                oneMonthAgo.add(Calendar.DAY_OF_MONTH, -30);

                for (DataSnapshot reminderType : snapshot.getChildren()) {
                    DataSnapshot reminderAmountTotal = reminderType.child("type");
                    DataSnapshot reminderCategory = reminderType.child("category"); // Add this line to get the category
                    DataSnapshot reminderCreationDate = reminderType.child("creationDate");
                    String type = reminderAmountTotal.getValue(String.class);
                    String category = reminderCategory.getValue(String.class); // Get the category value
                    String creationDateStr = reminderCreationDate.getValue(String.class);

                    Log.d("LOG", "onDataChange: " + type);

                    if (type != null && type.equals("alarm") && category != null && category.equals("Education") && creationDateStr !=null) {
                        try {
                            Date creationDate = dateFormat.parse(creationDateStr);
                            if (creationDate != null && creationDate.after(oneMonthAgo.getTime())) {
                                educationReminderAmount++;
                                Log.d("LOG03", "onDataChange: "+ educationReminderAmount);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                totalAmountEducation.setText(String.valueOf(educationReminderAmount)); // Set the count for Education reminders
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void totalAmountFinanceMonth(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long educationReminderAmount = 0; // Initialize the counter for Education reminders
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar oneMonthAgo = Calendar.getInstance();
                oneMonthAgo.add(Calendar.DAY_OF_MONTH, -30);

                for (DataSnapshot reminderType : snapshot.getChildren()) {
                    DataSnapshot reminderAmountTotal = reminderType.child("type");
                    DataSnapshot reminderCategory = reminderType.child("category"); // Add this line to get the category
                    DataSnapshot reminderCreationDate = reminderType.child("creationDate");
                    String type = reminderAmountTotal.getValue(String.class);
                    String category = reminderCategory.getValue(String.class); // Get the category value
                    String creationDateStr = reminderCreationDate.getValue(String.class);

                    Log.d("LOG", "onDataChange: " + type);

                    if (category != null && category.equals("Finance") && creationDateStr !=null) {
                        try {
                            Date creationDate = dateFormat.parse(creationDateStr);
                            if (creationDate != null && creationDate.after(oneMonthAgo.getTime())) {
                                educationReminderAmount++;
                                Log.d("LOG03", "onDataChange: "+ educationReminderAmount);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                totalAmountFinance.setText(String.valueOf(educationReminderAmount)); // Set the count for Education reminders
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void totalAmountShoppingMonth(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long educationReminderAmount = 0; // Initialize the counter for Education reminders
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar oneMonthAgo = Calendar.getInstance();
                oneMonthAgo.add(Calendar.DAY_OF_MONTH, -30);

                for (DataSnapshot reminderType : snapshot.getChildren()) {
                    DataSnapshot reminderAmountTotal = reminderType.child("type");
                    DataSnapshot reminderCategory = reminderType.child("category"); // Add this line to get the category
                    DataSnapshot reminderCreationDate = reminderType.child("creationDate");
                    String type = reminderAmountTotal.getValue(String.class);
                    String category = reminderCategory.getValue(String.class); // Get the category value
                    String creationDateStr = reminderCreationDate.getValue(String.class);

                    Log.d("LOG", "onDataChange: " + type);

                    if (type != null && type.equals("alarm") && category != null && category.equals("Shopping") && creationDateStr !=null) {
                        try {
                            Date creationDate = dateFormat.parse(creationDateStr);
                            if (creationDate != null && creationDate.after(oneMonthAgo.getTime())) {
                                educationReminderAmount++;
                                Log.d("LOG03", "onDataChange: "+ educationReminderAmount);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                totalAmountShopping.setText(String.valueOf(educationReminderAmount)); // Set the count for Education reminders
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void totalAmountJobMonth(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long educationReminderAmount = 0; // Initialize the counter for Education reminders
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar oneMonthAgo = Calendar.getInstance();
                oneMonthAgo.add(Calendar.DAY_OF_MONTH, -30);

                for (DataSnapshot reminderType : snapshot.getChildren()) {
                    DataSnapshot reminderAmountTotal = reminderType.child("type");
                    DataSnapshot reminderCategory = reminderType.child("category"); // Add this line to get the category
                    DataSnapshot reminderCreationDate = reminderType.child("creationDate");
                    String type = reminderAmountTotal.getValue(String.class);
                    String category = reminderCategory.getValue(String.class); // Get the category value
                    String creationDateStr = reminderCreationDate.getValue(String.class);

                    Log.d("LOG", "onDataChange: " + type);

                    if (type != null && type.equals("alarm") && category != null && category.equals("Job") && creationDateStr != null) {
                        try {
                            Date creationDate = dateFormat.parse(creationDateStr);
                            if (creationDate != null && creationDate.after(oneMonthAgo.getTime())) {
                                educationReminderAmount++;
                                Log.d("LOG03", "onDataChange: "+ educationReminderAmount);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                totalAmountJob.setText(String.valueOf(educationReminderAmount)); // Set the count for Education reminders
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void totalAmountPersonalMonth(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long educationReminderAmount = 0; // Initialize the counter for Education reminders
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar oneMonthAgo = Calendar.getInstance();
                oneMonthAgo.add(Calendar.DAY_OF_MONTH, -30);

                for (DataSnapshot reminderType : snapshot.getChildren()) {
                    DataSnapshot reminderAmountTotal = reminderType.child("type");
                    DataSnapshot reminderCategory = reminderType.child("category"); // Add this line to get the category
                    DataSnapshot reminderCreationDate = reminderType.child("creationDate");
                    String type = reminderAmountTotal.getValue(String.class);
                    String category = reminderCategory.getValue(String.class); // Get the category value
                    String creationDateStr = reminderCreationDate.getValue(String.class);

                    Log.d("LOG", "onDataChange: " + type);

                    if (type != null && type.equals("alarm") && category != null && category.equals("Personal") && creationDateStr !=null) {
                        try {
                            Date creationDate = dateFormat.parse(creationDateStr);
                            if (creationDate != null && creationDate.after(oneMonthAgo.getTime())) {
                                educationReminderAmount++;
                                Log.d("LOG03", "onDataChange: "+ educationReminderAmount);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                totalAmountPersonal.setText(String.valueOf(educationReminderAmount)); // Set the count for Education reminders
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void totalAmountSocialMonth(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long educationReminderAmount = 0; // Initialize the counter for Education reminders
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar oneMonthAgo = Calendar.getInstance();
                oneMonthAgo.add(Calendar.DAY_OF_MONTH, -30);

                for (DataSnapshot reminderType : snapshot.getChildren()) {
                    DataSnapshot reminderAmountTotal = reminderType.child("type");
                    DataSnapshot reminderCategory = reminderType.child("category"); // Add this line to get the category
                    DataSnapshot reminderCreationDate = reminderType.child("creationDate");
                    String type = reminderAmountTotal.getValue(String.class);
                    String category = reminderCategory.getValue(String.class); // Get the category value
                    String creationDateStr = reminderCreationDate.getValue(String.class);

                    Log.d("LOG", "onDataChange: " + type);

                    if (type != null && type.equals("alarm") && category != null && category.equals("Social") && creationDateStr != null) {
                        try {
                            Date creationDate = dateFormat.parse(creationDateStr);
                            if (creationDate != null && creationDate.after(oneMonthAgo.getTime())) {
                                educationReminderAmount++;
                                Log.d("LOG03", "onDataChange: "+ educationReminderAmount);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                totalAmountSocial.setText(String.valueOf(educationReminderAmount)); // Set the count for Education reminders
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void totalAmountOtherMonth(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long educationReminderAmount = 0; // Initialize the counter for Education reminders
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar oneMonthAgo = Calendar.getInstance();
                oneMonthAgo.add(Calendar.DAY_OF_MONTH, -30);

                for (DataSnapshot reminderType : snapshot.getChildren()) {
                    DataSnapshot reminderAmountTotal = reminderType.child("type");
                    DataSnapshot reminderCategory = reminderType.child("category"); // Add this line to get the category
                    DataSnapshot reminderCreationDate = reminderType.child("creationDate");
                    String type = reminderAmountTotal.getValue(String.class);
                    String category = reminderCategory.getValue(String.class); // Get the category value
                    String creationDateStr = reminderCreationDate.getValue(String.class);

                    Log.d("LOG", "onDataChange: " + type);

                    if (type != null && type.equals("alarm") && category != null && category.equals("Other") && creationDateStr !=null) {
                        try {
                            Date creationDate = dateFormat.parse(creationDateStr);
                            if (creationDate != null && creationDate.after(oneMonthAgo.getTime())) {
                                educationReminderAmount++;
                                Log.d("LOG03", "onDataChange: "+ educationReminderAmount);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                totalAmountOther.setText(String.valueOf(educationReminderAmount)); // Set the count for Education reminders
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void totalAmountEducationAnnual(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long educationReminderAmount = 0; // Initialize the counter for Education reminders
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar oneMonthAgo = Calendar.getInstance();
                oneMonthAgo.add(Calendar.DAY_OF_YEAR, -365);

                for (DataSnapshot reminderType : snapshot.getChildren()) {
                    DataSnapshot reminderAmountTotal = reminderType.child("type");
                    DataSnapshot reminderCategory = reminderType.child("category"); // Add this line to get the category
                    DataSnapshot reminderCreationDate = reminderType.child("creationDate");
                    String type = reminderAmountTotal.getValue(String.class);
                    String category = reminderCategory.getValue(String.class); // Get the category value
                    String creationDateStr = reminderCreationDate.getValue(String.class);

                    Log.d("LOG", "onDataChange: " + type);

                    if (type != null && type.equals("alarm") && category != null && category.equals("Education") && creationDateStr !=null) {
                        try {
                            Date creationDate = dateFormat.parse(creationDateStr);
                            if (creationDate != null && creationDate.after(oneMonthAgo.getTime())) {
                                educationReminderAmount++;
                                Log.d("LOG03", "onDataChange: "+ educationReminderAmount);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                totalAmountEducation.setText(String.valueOf(educationReminderAmount)); // Set the count for Education reminders
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void totalAmountShoppingAnnual(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long educationReminderAmount = 0; // Initialize the counter for Education reminders
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar oneMonthAgo = Calendar.getInstance();
                oneMonthAgo.add(Calendar.DAY_OF_YEAR, -365);

                for (DataSnapshot reminderType : snapshot.getChildren()) {
                    DataSnapshot reminderAmountTotal = reminderType.child("type");
                    DataSnapshot reminderCategory = reminderType.child("category"); // Add this line to get the category
                    DataSnapshot reminderCreationDate = reminderType.child("creationDate");
                    String type = reminderAmountTotal.getValue(String.class);
                    String category = reminderCategory.getValue(String.class); // Get the category value
                    String creationDateStr = reminderCreationDate.getValue(String.class);

                    Log.d("LOG", "onDataChange: " + type);

                    if (type != null && type.equals("alarm") && category != null && category.equals("Shopping") && creationDateStr != null) {
                        try {
                            Date creationDate = dateFormat.parse(creationDateStr);
                            if (creationDate != null && creationDate.after(oneMonthAgo.getTime())) {
                                educationReminderAmount++;
                                Log.d("LOG03", "onDataChange: "+ educationReminderAmount);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                totalAmountShopping.setText(String.valueOf(educationReminderAmount)); // Set the count for Education reminders
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void totalAmountJobAnnual(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long educationReminderAmount = 0; // Initialize the counter for Education reminders
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar oneMonthAgo = Calendar.getInstance();
                oneMonthAgo.add(Calendar.DAY_OF_YEAR, -365);

                for (DataSnapshot reminderType : snapshot.getChildren()) {
                    DataSnapshot reminderAmountTotal = reminderType.child("type");
                    DataSnapshot reminderCategory = reminderType.child("category"); // Add this line to get the category
                    DataSnapshot reminderCreationDate = reminderType.child("creationDate");
                    String type = reminderAmountTotal.getValue(String.class);
                    String category = reminderCategory.getValue(String.class); // Get the category value
                    String creationDateStr = reminderCreationDate.getValue(String.class);

                    Log.d("LOG", "onDataChange: " + type);

                    if (type != null && type.equals("alarm") && category != null && category.equals("Job") && creationDateStr !=null) {
                        try {
                            Date creationDate = dateFormat.parse(creationDateStr);
                            if (creationDate != null && creationDate.after(oneMonthAgo.getTime())) {
                                educationReminderAmount++;
                                Log.d("LOG03", "onDataChange: "+ educationReminderAmount);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                totalAmountJob.setText(String.valueOf(educationReminderAmount)); // Set the count for Education reminders
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void totalAmountSocialAnnual(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long educationReminderAmount = 0; // Initialize the counter for Education reminders
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar oneMonthAgo = Calendar.getInstance();
                oneMonthAgo.add(Calendar.DAY_OF_YEAR, -365);

                for (DataSnapshot reminderType : snapshot.getChildren()) {
                    DataSnapshot reminderAmountTotal = reminderType.child("type");
                    DataSnapshot reminderCategory = reminderType.child("category"); // Add this line to get the category
                    DataSnapshot reminderCreationDate = reminderType.child("creationDate");
                    String type = reminderAmountTotal.getValue(String.class);
                    String category = reminderCategory.getValue(String.class); // Get the category value
                    String creationDateStr = reminderCreationDate.getValue(String.class);

                    Log.d("LOG", "onDataChange: " + type);

                    if (type != null && type.equals("alarm") && category != null && category.equals("Social") && creationDateStr !=null) {
                        try {
                            Date creationDate = dateFormat.parse(creationDateStr);
                            if (creationDate != null && creationDate.after(oneMonthAgo.getTime())) {
                                educationReminderAmount++;
                                Log.d("LOG03", "onDataChange: "+ educationReminderAmount);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                totalAmountSocial.setText(String.valueOf(educationReminderAmount)); // Set the count for Education reminders
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void totalAmountOtherAnnual(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long educationReminderAmount = 0; // Initialize the counter for Education reminders
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar oneMonthAgo = Calendar.getInstance();
                oneMonthAgo.add(Calendar.DAY_OF_YEAR, -365);

                for (DataSnapshot reminderType : snapshot.getChildren()) {
                    DataSnapshot reminderAmountTotal = reminderType.child("type");
                    DataSnapshot reminderCategory = reminderType.child("category"); // Add this line to get the category
                    DataSnapshot reminderCreationDate = reminderType.child("creationDate");
                    String type = reminderAmountTotal.getValue(String.class);
                    String category = reminderCategory.getValue(String.class); // Get the category value
                    String creationDateStr = reminderCreationDate.getValue(String.class);

                    Log.d("LOG", "onDataChange: " + type);

                    if (type != null && type.equals("alarm") && category != null && category.equals("Other") && creationDateStr !=null) {
                        try {
                            Date creationDate = dateFormat.parse(creationDateStr);
                            if (creationDate != null && creationDate.after(oneMonthAgo.getTime())) {
                                educationReminderAmount++;
                                Log.d("LOG03", "onDataChange: "+ educationReminderAmount);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                totalAmountOther.setText(String.valueOf(educationReminderAmount)); // Set the count for Education reminders
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void totalAmountPersonalAnnual(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long educationReminderAmount = 0; // Initialize the counter for Education reminders
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar oneMonthAgo = Calendar.getInstance();
                oneMonthAgo.add(Calendar.DAY_OF_YEAR, -365);

                for (DataSnapshot reminderType : snapshot.getChildren()) {
                    DataSnapshot reminderAmountTotal = reminderType.child("type");
                    DataSnapshot reminderCategory = reminderType.child("category"); // Add this line to get the category
                    DataSnapshot reminderCreationDate = reminderType.child("creationDate");
                    String type = reminderAmountTotal.getValue(String.class);
                    String category = reminderCategory.getValue(String.class); // Get the category value
                    String creationDateStr = reminderCreationDate.getValue(String.class);

                    Log.d("LOG", "onDataChange: " + type);

                    if (type != null && type.equals("alarm") && category != null && category.equals("Personal") && creationDateStr != null) {
                        try {
                            Date creationDate = dateFormat.parse(creationDateStr);
                            if (creationDate != null && creationDate.after(oneMonthAgo.getTime())) {
                                educationReminderAmount++;
                                Log.d("LOG03", "onDataChange: "+ educationReminderAmount);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                totalAmountPersonal.setText(String.valueOf(educationReminderAmount)); // Set the count for Education reminders
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void totalAmountFinanceAnnual(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reminder").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long educationReminderAmount = 0; // Initialize the counter for Education reminders
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar oneMonthAgo = Calendar.getInstance();
                oneMonthAgo.add(Calendar.DAY_OF_YEAR, -365);

                for (DataSnapshot reminderType : snapshot.getChildren()) {
                    DataSnapshot reminderAmountTotal = reminderType.child("type");
                    DataSnapshot reminderCategory = reminderType.child("category"); // Add this line to get the category
                    DataSnapshot reminderCreationDate = reminderType.child("creationDate");
                    String type = reminderAmountTotal.getValue(String.class);
                    String category = reminderCategory.getValue(String.class); // Get the category value
                    String creationDateStr = reminderCreationDate.getValue(String.class);

                    Log.d("LOG", "onDataChange: " + type);

                    if (category != null && category.equals("Finance") && creationDateStr !=null) {
                        try {
                            Date creationDate = dateFormat.parse(creationDateStr);
                            if (creationDate != null && creationDate.after(oneMonthAgo.getTime())) {
                                educationReminderAmount++;
                                Log.d("LOG03", "onDataChange: "+ educationReminderAmount);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                totalAmountFinance.setText(String.valueOf(educationReminderAmount)); // Set the count for Education reminders
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void byOfTheWeekTotal(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference reference = database.getReference("Reminder").child(userid);

        int[] dayCounts = new int[7];


        // Firebase veritabanından veri çekme Haftanın Günlerine Göre
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String dateString = userSnapshot.child("date").getValue(String.class);
                    Log.d("LOG101", "onDataChange: alarmId: " + userSnapshot.getKey());
                    Log.d("LOG101", "onDataChange: dateString: " + dateString);


                    // Günü bulma
                    try {
                        if (dateString !=null ){
                            Date date = sdf.parse(dateString);
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; // Haftanın günü (Pazar'dan başlamak üzere)

                            // Gün sayısını artırma
                            dayCounts[dayOfWeek]++;
                        }else{


                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                        Log.e("LOG100", "onDataChange: ParseException", e);
                    }

                }

                // Verileri çubuk grafiği için hazırla
                ArrayList<BarEntry> entries = new ArrayList<>();
                for (int i = 0; i < dayCounts.length; i++) {
                    entries.add(new BarEntry(i, dayCounts[i]));
                }

                //FF852F
                BarDataSet dataSet = new BarDataSet(entries, "Alarm Sayısı");
                dataSet.setColor(Color.parseColor("#FF852F")); // #FF5548 rengi


                BarData barData = new BarData(dataSet);
                barData.setBarWidth(0.9f);

                barChart.setData(barData);

                // X eksenini özelleştirme
                XAxis xAxis = barChart.getXAxis();
                xAxis.setValueFormatter(new DayAxisValueFormatter());

                // X eksenini özelleştirme
                xAxis.setGranularity(1f); // Minimum interval between two labels
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X eksenini alt kısma yerleştir

                barChart.getDescription().setEnabled(false); // İstatistik barının altındaki açıklama kapatma

                barChart.invalidate(); // Grafik güncellendiğinde çağrılmalıdır


                barChart.invalidate(); // Grafik güncellendiğinde çağrılmalıdır
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

    }

    private void byOfTheHourTotal(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference reference = database.getReference("Reminder").child(userid);

        int[] hourCounts = new int[24];

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String timeString = userSnapshot.child("time").getValue(String.class);
                    Log.d("LOG102", "onDataChange: alarmId: " + userSnapshot.getKey());
                    Log.d("LOG102", "onDataChange: timeString: " + timeString);

                    try {
                        if (timeString != null) {
                            Date date = sdf.parse(timeString);

                            // Saat bilgisini al
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

                            // Saat bilgisine göre sayacı artır
                            hourCounts[hourOfDay]++;
                        } else {
                            //Toast.makeText(getContext(), "Hata", Toast.LENGTH_SHORT).show();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Log.e("LOG100", "onDataChange: ParseException", e);
                    }
                }

                // Verileri çubuk grafiği için hazırla
                ArrayList<BarEntry> entries = new ArrayList<>();
                for (int i = 0; i < hourCounts.length; i++) {
                    entries.add(new BarEntry(i, hourCounts[i]));
                }



                BarDataSet dataSet = new BarDataSet(entries, "Alarm Sayısı");
                dataSet.setColor(Color.parseColor("#FF5548")); // #FF5548 rengi

                BarData barData = new BarData(dataSet);
                barData.setBarWidth(0.9f);

                statisticBarHour.setData(barData);

                // X eksenini özelleştirme
                XAxis xAxis = statisticBarHour.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(getHourLabels()));
                xAxis.setTextColor(Color.WHITE);


                // X eksenini özelleştirme
                xAxis.setGranularity(1f); // Minimum interval between two labels
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X eksenini alt kısma yerleştir

                statisticBarHour.getDescription().setEnabled(false); // İstatistik barının altındaki açıklama kapatma

                statisticBarHour.invalidate(); // Grafik güncellendiğinde çağrılmalıdır
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });



    }

    private void byOfTheRegularTotal() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference reference = database.getReference("Reminder").child(userid);

        int[] dayCounts = new int[7];
        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        // Firebase veritabanından veri çekme Haftanın Günlerine Göre
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    for (int i = 0; i < daysOfWeek.length; i++) {
                        String dayString = userSnapshot.child(daysOfWeek[i]).getValue(String.class);

                        // Günlerde "yes" değeri varsa sayacı arttır
                        if ("yes".equals(dayString)) dayCounts[i]++;
                    }
                }

                // Verileri çubuk grafiği için hazırla
                ArrayList<BarEntry> entries = new ArrayList<>();
                for (int i = 0; i < dayCounts.length; i++) {
                    entries.add(new BarEntry(i, dayCounts[i]));
                }

//FF852F
                BarDataSet dataSet = new BarDataSet(entries, "Alarm Sayısı");
                dataSet.setColor(Color.parseColor("#FF852F")); // #FF5548 rengi

                BarData barData = new BarData(dataSet);
                barData.setBarWidth(0.9f);

                statisticBarRegular.setData(barData);

                    // X eksenini özelleştirme
                XAxis xAxis = statisticBarRegular.getXAxis();
                xAxis.setValueFormatter(new DayAxisValueFormatter());
                xAxis.setTextColor(Color.WHITE);


                    // X eksenini özelleştirme
                xAxis.setGranularity(1f); // Minimum interval between two labels
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X eksenini alt kısma yerleştir

                statisticBarRegular.getDescription().setEnabled(false); // İstatistik barının altındaki açıklama kapatma

                statisticBarRegular.invalidate(); // Grafik güncellendiğinde çağrılmalıdır

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void sleepByDayOfTheWeek() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("TotalSleep").child(userid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Bugünün tarihini al
                Calendar today = Calendar.getInstance();
                today.setTime(new Date());

                // Grafik verilerini hazırla
                ArrayList<BarEntry> entries = new ArrayList<>();

                // Son 7 günü döngüye al
                for (int i = 6; i >= 0; i--) {
                    Calendar day = (Calendar) today.clone();
                    day.add(Calendar.DAY_OF_YEAR, -i);

                    boolean found = false;

                    for (DataSnapshot daySnapshot : snapshot.getChildren()) {
                        if (daySnapshot.exists()) {
                            String dateValue = daySnapshot.child("date").getValue(String.class);

                            String dayString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(day.getTime());
                            Log.d("deneme2", "onDataChange: "+dayString);
                            if (dateValue.equals(dayString)) {
                                found = true;

                                String timeValue = daySnapshot.child("time").getValue(String.class);
                                Log.d("deneme", "onDataChange: " + timeValue);

                                long sleepDurationMillis = parseTimeToMillis(timeValue);

                                entries.add(new BarEntry(6 - i, sleepDurationMillis));

                                Log.d("denem", "onDataChange: "+sleepDurationMillis);

                                // i'yi x-ekseni dizini olarak kullan

                                break;
                            }
                        }
                    }

                    if (!found) {
                        // i'yi x-ekseni dizini olarak kullan
                        entries.add(new BarEntry(i, 0));
                    }

            }

                // BarEntry'leri eklerken TimeUnit kullanma, doğrudan sleepDurationHours'ü kullan
                BarDataSet dataSet = new BarDataSet(entries, "Uyku Süresi (saat)");
                dataSet.setColor(Color.parseColor("#90E1FF"));

                BarData barData = new BarData(dataSet);
                barData.setBarWidth(0.9f);

                statisticBarSleep.setData(barData);

                // X eksenini özelleştirme
                XAxis xAxis = statisticBarSleep.getXAxis();
                xAxis.setValueFormatter(new ValueFormattert());
                xAxis.setTextColor(Color.WHITE);

                // X eksenini özelleştirme
                xAxis.setGranularity(1f); // Minimum interval between two labels
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X eksenini alt kısma yerleştir

                statisticBarSleep.getDescription().setEnabled(false); // İstatistik barının altındaki açıklama kapatma

                statisticBarSleep.invalidate(); // Grafik güncellendiğinde çağrılmalıdır
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Hata durumuyla başa çık
                Log.e("FirebaseError", "Error: " + error.getMessage());
            }
        });
    }

    private long parseTimeToMillis(String timeValue) {
        String[] parts = timeValue.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int seconds = Integer.parseInt(parts[2]);

        // Saati milisaniyeye çevirme
        return TimeUnit.HOURS.toMillis(hours) + TimeUnit.MINUTES.toMillis(minutes) + TimeUnit.SECONDS.toMillis(seconds);
    }

    // Saat etiketlerini döndüren yardımcı bir metot
    private List<String> getHourLabels() {
        List<String> hourLabels = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hourLabels.add(String.format(Locale.getDefault(), "%02d:00", i));
        }
        return hourLabels;
    }

    private void hideBottomNavigationBar() {
        ViewCompat.animate(bottomNavigationView)
                .translationY(bottomNavigationView.getHeight())
                .alpha(0.0f)
                .setDuration(500) // Animasyon süresi (ms cinsinden)
                .start();
    }

    private void showBottomNavigationBar() {
        ViewCompat.animate(bottomNavigationView)
                .translationY(0)
                .alpha(1.0f)
                .setDuration(500) // Animasyon süresi (ms cinsinden)
                .start();
    }








}