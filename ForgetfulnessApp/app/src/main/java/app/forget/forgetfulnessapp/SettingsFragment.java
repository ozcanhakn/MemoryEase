package app.forget.forgetfulnessapp;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import app.forget.forgetfulnessapp.Advice.CalenderActivity;
import app.forget.forgetfulnessapp.Example.ExampleActivity;
import app.forget.forgetfulnessapp.Game.GameActivity;
import app.forget.forgetfulnessapp.Game.SudokuActivity;
import app.forget.forgetfulnessapp.Rate.RateThisApp;
import app.forget.forgetfulnessapp.SettingsActivity.AppearanceActivity;
import app.forget.forgetfulnessapp.SettingsActivity.EmailSupportActivity;
import app.forget.forgetfulnessapp.SettingsActivity.ThemeColorActivity;
import app.forget.forgetfulnessapp.SettingsActivity.WhatsNewActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    LinearLayout linearLayout,linearLayoutAppearance,linearLayoutMemoryEaseFanClub,linearLayoutEmailSupport,
            linerLayoutAbouttheTeam,linearLayoutRateThisApp,linearLayoutAccessStatistic,linearLayoutLogOut
            ,linearLayoutWhatsNew,linearLayoutAdviceAccess,linearLayoutThemeColor,linearLayoutFAQs,linearLayoutPrivacy,linearLayoutMemoryEaseForum;

    ScrollView scrollView;

    private boolean isNavigationBarHidden = false;

    private FirebaseAuth mAuth;


    private BottomNavigationView bottomNavigationView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        View view= inflater.inflate(R.layout.fragment_settings, container, false);

        bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);


        scrollView = view.findViewById(R.id.settingsScrollView);


        linearLayoutAppearance = view.findViewById(R.id.linearLayoutAppearance);
        linearLayoutMemoryEaseFanClub = view.findViewById(R.id.linearLayoutMemoryEaseFanClub);
        linearLayoutEmailSupport = view.findViewById(R.id.linearLayoutEmailSupport);
        linerLayoutAbouttheTeam = view.findViewById(R.id.linearLayoutAboutTeam);
        linearLayoutRateThisApp = view.findViewById(R.id.linearLayoutRateApp);
        linearLayoutAccessStatistic = view.findViewById(R.id.linearLayoutAccessStatistic);
        linearLayoutLogOut = view.findViewById(R.id.linearLayoutLogOut);
        linearLayoutWhatsNew = view.findViewById(R.id.linearLayoutWhatsNews);
        linearLayoutAdviceAccess = view.findViewById(R.id.linearLayoutAdviceAccess);
        linearLayoutThemeColor = view.findViewById(R.id.linearLayoutThemeColor);
        linearLayoutFAQs = view.findViewById(R.id.linearLayoutFAQs);
        linearLayoutPrivacy = view.findViewById(R.id.linearLayoutPrivacy);
        linearLayoutMemoryEaseForum = view.findViewById(R.id.linearLayoutMemoryEaseForum);

        mAuth = FirebaseAuth.getInstance();

        linearLayoutPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PrivacyPolicy.class);
                startActivity(intent);
            }
        });

        linearLayoutThemeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userid = user.getUid();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("Premium").child(userid);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            String premium = snapshot.child("premium").getValue(String.class);
                            if (premium.equals("yes")){
                                Intent intent = new Intent(getActivity(), ThemeColorActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(getContext(), "Premium Süreniz Bitmiş", Toast.LENGTH_SHORT).show();
                                goToUpgrade();
                            }
                        }else {
                            goToUpgrade();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            }
        });

        linearLayoutAdviceAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userid = user.getUid();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("Premium").child(userid);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            String premium = snapshot.child("premium").getValue(String.class);
                            if (premium.equals("yes")){
                                goToAdviceFragment();
                            }else {
                                Toast.makeText(getContext(), "Premium Süreniz Bitmiş", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            goToUpgrade();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            }
        });

        linearLayoutMemoryEaseForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userid = user.getUid();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("Premium").child(userid);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            String premium = snapshot.child("premium").getValue(String.class);
                            if (premium.equals("yes")){
                                Toast.makeText(getContext(), getString(R.string.comingsoon), Toast.LENGTH_SHORT).show();
                            }else if (premium.equals("no")){
                                Toast.makeText(getContext(), "Premium Süreniz Bitmiş", Toast.LENGTH_SHORT).show();
                            }else {
                                //Premium Bilgisi Bulunamadı
                                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            goToUpgrade();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            }
        });

        linearLayoutAccessStatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userid = user.getUid();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("Premium").child(userid);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            String premium = snapshot.child("premium").getValue(String.class);
                            if (premium.equals("yes")){
                                goToStatisticFragment();
                            }else {
                                Toast.makeText(getContext(), "Premium Süreniz Bitmiş", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                                goToUpgrade();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            }
        });

        init();

        linearLayoutRateThisApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RateThisApp.openRateDialog(getContext());
            }
        });




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

        linearLayout = view.findViewById(R.id.linearLayoutUnits);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ExampleActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void goToStatisticFragment() {
        if (getActivity() instanceof HomeScreen) {
            HomeScreen homeScreen = (HomeScreen) getActivity();
            homeScreen.switchToStatisticFragment();
        }
    }

    public void goToAdviceFragment() {
        if (getActivity() instanceof HomeScreen) {
            HomeScreen homeScreen = (HomeScreen) getActivity();
            homeScreen.switchToAdviceFragment();
        }
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

    public void init(){
        linearLayoutMemoryEaseFanClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),UpgradeForProActivity.class);
                startActivity(intent);
            }
        });
        linearLayoutAppearance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AppearanceActivity.class);
                startActivity(intent);
            }
        });
        linearLayoutEmailSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EmailSupportActivity.class);
                startActivity(intent);
            }
        });

        linerLayoutAbouttheTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AboutTheTeam.class);
                startActivity(intent);
            }
        });

        linearLayoutLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                // Bu satırı ekleyin, böylece kullanıcı çıkış yaparken mevcut aktiviteyi kapatır.

                Toast.makeText(getContext(), "Çıkış Yapıldı", Toast.LENGTH_SHORT).show();
            }
        });

        linearLayoutWhatsNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WhatsNewActivity.class);
                startActivity(intent);
            }
        });
        linearLayoutFAQs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FAQs.class);
                startActivity(intent);
            }
        });

    }

    private void goToUpgrade(){
        Intent intent = new Intent(getActivity(),UpgradeForProActivity.class);
        startActivity(intent);
    }
}