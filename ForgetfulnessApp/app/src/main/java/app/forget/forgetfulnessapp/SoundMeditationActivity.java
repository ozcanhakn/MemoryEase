package app.forget.forgetfulnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import app.forget.forgetfulnessapp.Model.Choice;
import app.forget.forgetfulnessapp.ViewHolder.ChoiceViewHolder;
import app.forget.forgetfulnessapp.ViewHolder.RelatedViewHolder;

public class SoundMeditationActivity extends AppCompatActivity {

    RelativeLayout playSoundButton,pauseBtn,backButton;

    boolean isStreaming = false;
    MediaPlayer mediaPlayer;

    String url,key,category;
    String soundMix;
    ImageView imageViewSoundMeditation;
    String id = "";

    TextView textTitle,textMin,textType,textDesc;

    RecyclerView recRelated;
    FirebaseRecyclerAdapter<Choice, RelatedViewHolder> adapter;
    RecyclerView.LayoutManager layoutManager;

    private ScrollView scrollView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sound_meditation);



        scrollView = findViewById(R.id.scrollView);


        recRelated = findViewById(R.id.recRelated);
        recRelated.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        recRelated.setLayoutManager(layoutManager);//layoutManager

        imageViewSoundMeditation = findViewById(R.id.imageViewSoundMeditation);
        pauseBtn = findViewById(R.id.stopSoundButton);
        textMin = findViewById(R.id.textMinute);
        textTitle = findViewById(R.id.textTitle);
        textType = findViewById(R.id.textType);
        textDesc = findViewById(R.id.textSoundDesc);

        backButton = findViewById(R.id.relativeSoundBackButton);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SoundMeditationActivity.this,MeditationChoiceActivity.class);
                intent.putExtra("choice",id);
                startActivity(intent);
            }
        });



        Intent intent = getIntent();
        if (intent != null) {
            key = intent.getStringExtra("key");
            category = intent.getStringExtra("category");
            soundMix = intent.getStringExtra("mixSound");

            id = intent.getStringExtra("id");

            Log.d("IDMED", "onCreate: "+key);
            if (key != null && !key.isEmpty() && soundMix == null) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("Sounds").child(key);

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        String photo = snapshot.child("photo").getValue(String.class);
                        url = snapshot.child("url").getValue(String.class);
                        String title = snapshot.child("field").getValue(String.class);
                      //  int min = snapshot.child("min").getValue(Integer.class);
                        String desc = snapshot.child("desc").getValue(String.class);

                        Log.d("SOUNDKat", "onDataChange: else "+photo+"  "+title);




                        textTitle.setText(title);
                        textDesc.setText(desc);


                        if (photo != null) {
                            Picasso.get().load(photo).into(imageViewSoundMeditation);
                        }

                        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                        DatabaseReference reference1 = database1.getReference(category).child(key);

                        reference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                int minute = snapshot.child("min").getValue(Integer.class);
                                String type = snapshot.child("type").getValue(String.class);

                                textMin.setText(String.valueOf(minute));
                                textType.setText(type);
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Handle onCancelled
                    }
                });
            }if (soundMix != null && !soundMix.isEmpty()){
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference(soundMix).child(category).child(key);

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        String photo = snapshot.child("photo").getValue(String.class);
                        url = snapshot.child("url").getValue(String.class);
                        String title = snapshot.child("field").getValue(String.class);
                        String desc = snapshot.child("desc").getValue(String.class);

                        Log.d("SOUNDKat", "onDataChange: else if"+photo+"  "+title);



                        textTitle.setText(title);
                        textDesc.setText(desc);


                        if (photo != null) {
                            Picasso.get().load(photo).into(imageViewSoundMeditation);
                        }

                        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                        DatabaseReference reference1 = database1.getReference("mixSound").child(category).child(key);

                        reference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                int minute = snapshot.child("min").getValue(Integer.class);
                                String type = snapshot.child("type").getValue(String.class);

                                textMin.setText(String.valueOf(minute));
                                textType.setText(type);
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            }
        }






        // View ağacı çizildikten sonra otomatik kaydırma işlemini gerçekleştir
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Scroll view'da içerik boyutunu al
                int contentHeight = scrollView.getChildAt(0).getHeight();
                int scrollViewHeight = scrollView.getHeight();

                // Eğer içerik boyutu, scroll view'ın boyutundan büyükse
                if (contentHeight > scrollViewHeight) {
                    // En aşağıya kaydır
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });

                    // 1 saniye bekleyip yukarıya kaydır
                    scrollView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(View.FOCUS_UP);
                        }
                    }, 1000);
                }

                // Listener'ı kaldır
                scrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });





        playSoundButton = findViewById(R.id.playSoundButton);

        playSoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Play button'un görünürlüğünü kaldır
                playSoundButton.setVisibility(View.INVISIBLE);

                // Play button'un boyutunu hızlıca küçült
                ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(playSoundButton, "scaleX", 0.5f);
                ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(playSoundButton, "scaleY", 0.5f);
                scaleDownX.setDuration(1000);
                scaleDownY.setDuration(1000);

                AnimatorSet scaleDown = new AnimatorSet();
                scaleDown.play(scaleDownX).with(scaleDownY);

                scaleDown.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        // Stop button'un boyutunu hızlıca artırıp ortaya çıksın
                        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(pauseBtn, "scaleX", 1f);
                        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(pauseBtn, "scaleY", 1f);
                        scaleUpX.setDuration(1000);
                        scaleUpY.setDuration(1000);

                        AnimatorSet scaleUp = new AnimatorSet();
                        scaleUp.play(scaleUpX).with(scaleUpY);

                        // Stop button'u görünür hale getir
                        pauseBtn.setVisibility(View.VISIBLE);

                        scaleUp.start();
                    }
                });

                scaleDown.start();

                // Ses çalma metodu
                playAudio();
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking the media player
                // if the audio is playing or not.
                if (mediaPlayer.isPlaying()) {
                    // pausing the media player if media player
                    // is playing we are calling below line to
                    // stop our media player.
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    pauseBtn.setVisibility(View.GONE);

                    // Play button'u görünür hale getir ve boyutunu tekrar büyüt
                    playSoundButton.setVisibility(View.VISIBLE);
                    playSoundButton.setScaleX(1f);
                    playSoundButton.setScaleY(1f);

                    // below line is to display a message
                    // when media player is paused.
                    Toast.makeText(SoundMeditationActivity.this, "Audio has been paused", Toast.LENGTH_SHORT).show();
                } else {
                    // this method is called when media
                    // player is not playing.
                    Toast.makeText(SoundMeditationActivity.this, "Audio has not played", Toast.LENGTH_SHORT).show();
                }
            }
        });





    }


    private void playAudio() {


        // initializing media player
        mediaPlayer = new MediaPlayer();

        // below line is use to set the audio
        // stream type for our media player.
        mediaPlayer.setAudioAttributes(
                new AudioAttributes
                        .Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build());

        // below line is use to set our
        // url to our media player.
        try {
            mediaPlayer.setDataSource(url);
            // below line is use to prepare
            // and start our media player.
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        // below line is use to display a toast message.
        Toast.makeText(this, "Audio started playing..", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Grup numarasını belirle


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(category);


        FirebaseRecyclerOptions<Choice> options = new FirebaseRecyclerOptions.Builder<Choice>()
                .setQuery(reference, Choice.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<Choice, RelatedViewHolder>(options) {
            @Override
            protected void onBindViewHolder(RelatedViewHolder viewHolder, int position, Choice model) {
                // ViewHolder'ı doldur
                Picasso.get().load(model.getPhoto()).into(viewHolder.imageView);
                viewHolder.min.setText(String.valueOf(model.getMin()));
                viewHolder.type.setText(model.getType());
                viewHolder.soundName.setText(model.getField());



                viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(SoundMeditationActivity.this,SoundMeditationActivity.class);
                                intent.putExtra("key",adapter.getRef(position).getKey());
                                intent.putExtra("category",model.getForcat());
                                startActivity(intent);
                            }
                        });
                    }
                });
            }

            @Override
            public RelatedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // ViewHolder'ı oluştur
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.related_item_layout, parent, false);
                return new RelatedViewHolder(itemView);
            }
        };
        adapter.startListening();
        recRelated.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        }
    }
}