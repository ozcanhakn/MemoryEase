package app.forget.forgetfulnessapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import app.forget.forgetfulnessapp.Advice.MeditationActivity;
import app.forget.forgetfulnessapp.Example.ExampleActivity;
import app.forget.forgetfulnessapp.Example.NotificationActivity;
import app.forget.forgetfulnessapp.Game.Solver;
import app.forget.forgetfulnessapp.Game.SudokuBoard;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdviceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdviceFragment extends Fragment {
    TextView textAerobicExercise,textAerobic1,textAerobic2,textAerobic3,textAerobic4,textSleep,textMeditation,textCrossword;
    TextView textMeditation1,textMeditation2,textMeditation3,textMeditation4,textMeditation5,textMeditation6,textMeditation7;
    TextView textSleep1,textSleep2,textSleep3,textSleep4,textSleep5,textSleep6,textSleep7,textSleep8;

    TextView useReminder;

    CardView cardAerobic,cardSleep,cardMeditation,cardCrossword,cardReminder,gotoMeditation,gotoSleep;
    ImageView questionAerobic,questionReminder,questionSleep,questionPuzzle,questionMeditation;



    BottomNavigationView bottomNavigationView;

    private boolean isNavigationBarHidden = false;

    boolean isBackgroundChanged = false;
    boolean isBackgroundChanged1 = false;
    boolean isBackgroundChanged2 = false;
    boolean isBackgroundChanged3 = false;


    ScrollView scrollView;


    RelativeLayout relativeGoToGames;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdviceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdviceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdviceFragment newInstance(String param1, String param2) {
        AdviceFragment fragment = new AdviceFragment();
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
        View view = inflater.inflate(R.layout.fragment_advice, container, false);

        textAerobic1 = view.findViewById(R.id.textAerobic1);
        textAerobic2 = view.findViewById(R.id.textAerobic2);
        textAerobic3 = view.findViewById(R.id.textAerobic3);
        textAerobic4 = view.findViewById(R.id.textAerobic4);

        textMeditation1 = view.findViewById(R.id.textMeditation1);
        textMeditation2 = view.findViewById(R.id.textMeditation2);
        textMeditation3 = view.findViewById(R.id.textMeditation3);
        textMeditation4 = view.findViewById(R.id.textMeditation4);
        textMeditation5 = view.findViewById(R.id.textMeditation5);
        textMeditation6 = view.findViewById(R.id.textMeditation6);
        textMeditation7 = view.findViewById(R.id.textMeditation7);

        gotoMeditation = view.findViewById(R.id.gotoMeditation);

        gotoMeditation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MeditationActivity.class);
                startActivity(intent);
            }
        });



        textAerobicExercise = view.findViewById(R.id.textAerobicExercise);
        textCrossword = view.findViewById(R.id.textCrosswordPuzzle);
        textMeditation = view.findViewById(R.id.textMeditation);
        textSleep = view.findViewById(R.id.textSleep);

        textSleep1 = view.findViewById(R.id.textSleep1);
        textSleep2 = view.findViewById(R.id.textSleep2);
        textSleep3 = view.findViewById(R.id.textSleep3);
        textSleep4 = view.findViewById(R.id.textSleep4);
        textSleep5 = view.findViewById(R.id.textSleep5);
        textSleep6 = view.findViewById(R.id.textSleep6);
        textSleep7 = view.findViewById(R.id.textSleep7);
        textSleep8 = view.findViewById(R.id.textSleep8);

        gotoSleep = view.findViewById(R.id.gotoSleep);
        gotoSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SleepTrackerActivity.class);
                startActivity(intent);
            }
        });

        cardAerobic = view.findViewById(R.id.cardAerobicExercise);
        cardCrossword = view.findViewById(R.id.cardCrosswordPuzzle);
        cardMeditation = view.findViewById(R.id.cardMeditation);
        cardSleep = view.findViewById(R.id.cardSleep);
        cardReminder = view.findViewById(R.id.cardUseReminder);
        useReminder = view.findViewById(R.id.textUseReminder);


        questionAerobic = view.findViewById(R.id.questionButtonAerobic);
        questionSleep = view.findViewById(R.id.questionButtonSleep);
        questionMeditation = view.findViewById(R.id.questionButtonMeditation);
        questionReminder = view.findViewById(R.id.questionButtonUseReminder);
        questionPuzzle = view.findViewById(R.id.questionButtonPuzzle);



        relativeGoToGames = view.findViewById(R.id.relativeGoToGames);

        relativeGoToGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                startActivity(intent);
            }
        });


        questionMeditation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showQuestionMeditationBuilderDialog();
            }
        });

        questionAerobic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showQuestionExerciseBuilderDialog();
            }
        });

        questionSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showQuestionSleepBuilderDialog();
            }
        });

        questionPuzzle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showQuestionCrosswordBuilderDialog();
            }
        });



        bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);

        scrollView = view.findViewById(R.id.scrollViewAdvice);

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


        cardAerobic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isBackgroundChanged) {
                    // Arka planı değiştir
                    textAerobic1.setVisibility(View.VISIBLE);
                    textAerobic2.setVisibility(View.VISIBLE);
                    textAerobic3.setVisibility(View.VISIBLE);
                    textAerobic4.setVisibility(View.VISIBLE);

                    cardCrossword.setVisibility(View.GONE);
                    cardMeditation.setVisibility(View.GONE);
                    cardSleep.setVisibility(View.GONE);
                    cardReminder.setVisibility(View.GONE);
                    textCrossword.setVisibility(View.GONE);
                    useReminder.setVisibility(View.GONE);
                    textSleep.setVisibility(View.GONE);
                    textMeditation.setVisibility(View.GONE);
                    questionAerobic.setVisibility(View.GONE);
                    questionSleep.setVisibility(View.GONE);
                    questionReminder.setVisibility(View.GONE);
                    questionPuzzle.setVisibility(View.GONE);
                    questionMeditation.setVisibility(View.GONE);
                    relativeGoToGames.setVisibility(View.GONE);
                    gotoMeditation.setVisibility(View.GONE);


                } else {
                    // Arka planı eski haline döndür
                    textAerobic1.setVisibility(View.GONE);
                    textAerobic2.setVisibility(View.GONE);
                    textAerobic3.setVisibility(View.GONE);
                    textAerobic4.setVisibility(View.GONE);

                    cardCrossword.setVisibility(View.VISIBLE);
                    cardMeditation.setVisibility(View.VISIBLE);
                    cardSleep.setVisibility(View.VISIBLE);
                    cardReminder.setVisibility(View.VISIBLE);
                    textCrossword.setVisibility(View.VISIBLE);
                    textSleep.setVisibility(View.VISIBLE);
                    textMeditation.setVisibility(View.VISIBLE);
                    useReminder.setVisibility(View.VISIBLE);
                    questionAerobic.setVisibility(View.VISIBLE);
                    questionSleep.setVisibility(View.VISIBLE);
                    questionReminder.setVisibility(View.VISIBLE);
                    questionPuzzle.setVisibility(View.VISIBLE);
                    questionMeditation.setVisibility(View.VISIBLE);
                    relativeGoToGames.setVisibility(View.GONE);
                }

                // Durumu tersine çevir
                isBackgroundChanged = !isBackgroundChanged;
            }
        });

        cardMeditation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isBackgroundChanged1) {
                    // Arka planı değiştir

                    textMeditation1.setVisibility(View.VISIBLE);
                    textMeditation2.setVisibility(View.VISIBLE);
                    textMeditation3.setVisibility(View.VISIBLE);
                    textMeditation4.setVisibility(View.VISIBLE);
                    textMeditation5.setVisibility(View.VISIBLE);
                    textMeditation6.setVisibility(View.VISIBLE);
                    textMeditation7.setVisibility(View.VISIBLE);
                    gotoMeditation.setVisibility(View.VISIBLE);


                    useReminder.setVisibility(View.GONE);
                    cardCrossword.setVisibility(View.GONE);
                    cardAerobic.setVisibility(View.GONE);
                    cardSleep.setVisibility(View.GONE);
                    cardReminder.setVisibility(View.GONE);
                    textAerobicExercise.setVisibility(View.GONE);
                    textCrossword.setVisibility(View.GONE);
                    textSleep.setVisibility(View.GONE);
                    textMeditation.setVisibility(View.GONE);
                    questionAerobic.setVisibility(View.GONE);
                    questionSleep.setVisibility(View.GONE);
                    questionReminder.setVisibility(View.GONE);
                    questionPuzzle.setVisibility(View.GONE);
                    questionMeditation.setVisibility(View.GONE);
                    relativeGoToGames.setVisibility(View.GONE);



                } else {
                    // Arka planı eski haline döndür
                    textMeditation1.setVisibility(View.GONE);
                    textMeditation2.setVisibility(View.GONE);
                    textMeditation3.setVisibility(View.GONE);
                    textMeditation4.setVisibility(View.GONE);
                    textMeditation5.setVisibility(View.GONE);
                    textMeditation6.setVisibility(View.GONE);
                    textMeditation7.setVisibility(View.GONE);
                    gotoMeditation.setVisibility(View.GONE);

                    useReminder.setVisibility(View.VISIBLE);
                    cardCrossword.setVisibility(View.VISIBLE);
                    cardMeditation.setVisibility(View.VISIBLE);
                    cardSleep.setVisibility(View.VISIBLE);
                    cardAerobic.setVisibility(View.VISIBLE);
                    cardReminder.setVisibility(View.VISIBLE);
                    textCrossword.setVisibility(View.VISIBLE);
                    textSleep.setVisibility(View.VISIBLE);
                    textAerobicExercise.setVisibility(View.VISIBLE);
                    textMeditation.setVisibility(View.VISIBLE);
                    questionAerobic.setVisibility(View.VISIBLE);
                    questionSleep.setVisibility(View.VISIBLE);
                    questionReminder.setVisibility(View.VISIBLE);
                    questionPuzzle.setVisibility(View.VISIBLE);
                    questionMeditation.setVisibility(View.VISIBLE);
                    relativeGoToGames.setVisibility(View.GONE);
                }

                // Durumu tersine çevir
                isBackgroundChanged1 = !isBackgroundChanged1;
            }
        });

        cardSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isBackgroundChanged2) {
                    // Arka planı değiştir

                    textSleep1.setVisibility(View.VISIBLE);
                    textSleep2.setVisibility(View.VISIBLE);
                    textSleep3.setVisibility(View.VISIBLE);
                    textSleep4.setVisibility(View.VISIBLE);
                    textSleep5.setVisibility(View.VISIBLE);
                    textSleep6.setVisibility(View.VISIBLE);
                    textSleep7.setVisibility(View.VISIBLE);
                    textSleep8.setVisibility(View.VISIBLE);
                    gotoSleep.setVisibility(View.VISIBLE);


                    useReminder.setVisibility(View.GONE);
                    cardAerobic.setVisibility(View.GONE);
                    textAerobicExercise.setVisibility(View.GONE);
                    cardCrossword.setVisibility(View.GONE);
                    cardMeditation.setVisibility(View.GONE);
                    cardReminder.setVisibility(View.GONE);
                    textCrossword.setVisibility(View.GONE);
                    textSleep.setVisibility(View.GONE);
                    textMeditation.setVisibility(View.GONE);
                    questionAerobic.setVisibility(View.GONE);
                    questionSleep.setVisibility(View.GONE);
                    questionReminder.setVisibility(View.GONE);
                    questionPuzzle.setVisibility(View.GONE);
                    questionMeditation.setVisibility(View.GONE);
                    relativeGoToGames.setVisibility(View.GONE);




                } else {
                    // Arka planı eski haline döndür
                    textSleep1.setVisibility(View.GONE);
                    textSleep2.setVisibility(View.GONE);
                    textSleep3.setVisibility(View.GONE);
                    textSleep4.setVisibility(View.GONE);
                    textSleep5.setVisibility(View.GONE);
                    textSleep6.setVisibility(View.GONE);
                    textSleep7.setVisibility(View.GONE);
                    textSleep8.setVisibility(View.GONE);
                    gotoSleep.setVisibility(View.GONE);



                    useReminder.setVisibility(View.VISIBLE);
                    cardCrossword.setVisibility(View.VISIBLE);
                    cardMeditation.setVisibility(View.VISIBLE);
                    cardAerobic.setVisibility(View.VISIBLE);
                    cardReminder.setVisibility(View.VISIBLE);
                    textCrossword.setVisibility(View.VISIBLE);
                    textSleep.setVisibility(View.VISIBLE);
                    textMeditation.setVisibility(View.VISIBLE);
                    textAerobicExercise.setVisibility(View.VISIBLE);
                    questionAerobic.setVisibility(View.VISIBLE);
                    questionSleep.setVisibility(View.VISIBLE);
                    questionReminder.setVisibility(View.VISIBLE);
                    questionPuzzle.setVisibility(View.VISIBLE);
                    questionMeditation.setVisibility(View.VISIBLE);
                    relativeGoToGames.setVisibility(View.GONE);
                }

                // Durumu tersine çevir
                isBackgroundChanged2 = !isBackgroundChanged2;
            }
        });


        cardCrossword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isBackgroundChanged3) {
                    // Arka planı değiştir

                    relativeGoToGames.setVisibility(View.VISIBLE);

                    cardSleep.setVisibility(View.GONE);
                    useReminder.setVisibility(View.GONE);
                    textAerobicExercise.setVisibility(View.GONE);
                    cardAerobic.setVisibility(View.GONE);
                    cardMeditation.setVisibility(View.GONE);
                    cardReminder.setVisibility(View.GONE);
                    textCrossword.setVisibility(View.GONE);
                    textSleep.setVisibility(View.GONE);
                    textMeditation.setVisibility(View.GONE);
                    questionAerobic.setVisibility(View.GONE);
                    questionSleep.setVisibility(View.GONE);
                    questionReminder.setVisibility(View.GONE);
                    questionPuzzle.setVisibility(View.GONE);
                    questionMeditation.setVisibility(View.GONE);




                } else {
                    // Arka planı eski haline döndür

                    relativeGoToGames.setVisibility(View.GONE);

                    cardSleep.setVisibility(View.VISIBLE);
                    useReminder.setVisibility(View.VISIBLE);
                    cardCrossword.setVisibility(View.VISIBLE);
                    cardMeditation.setVisibility(View.VISIBLE);
                    cardAerobic.setVisibility(View.VISIBLE);
                    cardReminder.setVisibility(View.VISIBLE);
                    textCrossword.setVisibility(View.VISIBLE);
                    textSleep.setVisibility(View.VISIBLE);
                    textMeditation.setVisibility(View.VISIBLE);
                    textAerobicExercise.setVisibility(View.VISIBLE);
                    questionAerobic.setVisibility(View.VISIBLE);
                    questionSleep.setVisibility(View.VISIBLE);
                    questionReminder.setVisibility(View.VISIBLE);
                    questionPuzzle.setVisibility(View.VISIBLE);
                    questionMeditation.setVisibility(View.VISIBLE);
                }

                // Durumu tersine çevir
                isBackgroundChanged3 = !isBackgroundChanged3;

            }
        });

        return view;
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


    private void showQuestionMeditationBuilderDialog(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.question_alert_box_item_basic);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

    }

    private void showQuestionExerciseBuilderDialog(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.question_alert_box_item_basic_exercise);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);


    }

    private void showQuestionSleepBuilderDialog(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.question_alert_box_item_basic_sleep);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

    private void showQuestionCrosswordBuilderDialog(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.question_alert_box_item_basic_crossword);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

}