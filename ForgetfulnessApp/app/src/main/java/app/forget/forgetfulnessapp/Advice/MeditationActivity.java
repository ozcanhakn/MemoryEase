package app.forget.forgetfulnessapp.Advice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import app.forget.forgetfulnessapp.AllReminders;
import app.forget.forgetfulnessapp.CreateReminder.FullScreenMeditationActivity;
import app.forget.forgetfulnessapp.MeditationChoiceActivity;
import app.forget.forgetfulnessapp.Model.Card;
import app.forget.forgetfulnessapp.Model.Meditation;
import app.forget.forgetfulnessapp.Model.Reminder;
import app.forget.forgetfulnessapp.R;
import app.forget.forgetfulnessapp.ViewHolder.MeditationViewHolder;
import app.forget.forgetfulnessapp.ViewHolder.ReminderViewHolder;
import pl.droidsonroids.gif.GifImageView;

public class MeditationActivity extends AppCompatActivity {

    RecyclerView recMeditation;
    FirebaseRecyclerAdapter<Meditation, MeditationViewHolder> adapter;
    RecyclerView.LayoutManager layoutManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_meditation);



        //All Reminders Yüklenmesi
        recMeditation = findViewById(R.id.recyclerFocusChoose);
        recMeditation.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recMeditation.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));//layoutManager


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Meditation");

        FirebaseRecyclerOptions<Meditation> options = new FirebaseRecyclerOptions.Builder<Meditation>()
                .setQuery(reference, Meditation.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Meditation, MeditationViewHolder>(options) {
            @Override
            protected void onBindViewHolder(MeditationViewHolder viewHolder, int position, Meditation model) {

                Picasso.get().load(model.getPhoto()).into(viewHolder.imageView);
                viewHolder.field.setText(model.getField());


                viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Kartı döndürmek için animasyon oluştur
                        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(view, View.ROTATION, 0f, 360f);
                        rotationAnimator.setDuration(3000); // 3 saniye boyunca döndür

                        // Kartı büyütmek için animasyon oluştur
                        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1.0f, 1.5f);
                        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.0f, 1.5f);
                        AnimatorSet scaleUpAnimator = new AnimatorSet();
                        scaleUpAnimator.playTogether(scaleX, scaleY);
                        scaleUpAnimator.setDuration(1000); // 1 saniye boyunca büyüt

                        // Kartı küçültmek için animasyon oluştur
                        ObjectAnimator scaleXSmall = ObjectAnimator.ofFloat(view, View.SCALE_X, 1.5f, 1.0f);
                        ObjectAnimator scaleYSmall = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.5f, 1.0f);
                        AnimatorSet scaleDownAnimator = new AnimatorSet();
                        scaleDownAnimator.playTogether(scaleXSmall, scaleYSmall);
                        scaleDownAnimator.setDuration(1000); // 1 saniye boyunca küçült

                        // Dönme animasyonunu başlat
                        rotationAnimator.start();

                        // Dönme animasyonu bittiğinde büyütme animasyonunu başlat
                        rotationAnimator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                scaleUpAnimator.start();
                            }
                        });

                        // Büyütme animasyonu bittikten sonra küçültme animasyonunu başlat
                        scaleUpAnimator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                scaleDownAnimator.start();
                            }
                        });

                        // Küçültme animasyonu bittikten sonra yeni aktiviteye geçiş yap
                        scaleDownAnimator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                // Yeni aktiviteye geçiş için Intent oluştur
                                Intent intent = new Intent(MeditationActivity.this, MeditationChoiceActivity.class);
                                // Geçiş yap
                                intent.putExtra("choice",adapter.getRef(position).getKey());
                                startActivity(intent);
                            }
                        });
                    }
                });



            }

            @Override
            public MeditationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.meditation_item_layout, parent, false);
                return new MeditationViewHolder(itemView);
            }
        };
        adapter.startListening();
        recMeditation.setAdapter(adapter);

    }
}