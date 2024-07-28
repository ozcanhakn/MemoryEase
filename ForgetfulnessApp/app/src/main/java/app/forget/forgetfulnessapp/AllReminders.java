package app.forget.forgetfulnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import app.forget.forgetfulnessapp.Model.Reminder;
import app.forget.forgetfulnessapp.ViewHolder.ReminderViewHolder;
import io.reactivex.rxjava3.annotations.NonNull;

public class AllReminders extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Reminder, ReminderViewHolder> adapter;

    ImageView backButton;

    LottieAnimationView lottieAnimationView;

    String textSpeech,textSpeechDesc;

    TextToSpeech mTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_all_reminders);


        backButton = findViewById(R.id.arrowBack);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllReminders.this, HomeScreen.class);
                startActivity(intent);
            }
        });

        textToSpeech();
        lottieAnimationView = findViewById(R.id.lottieAnimationView);


        //All Reminders Yüklenmesi
        recyclerView = findViewById(R.id.recyclerAllReminders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);//layoutManager



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Reminder").child(userId);

        FirebaseRecyclerOptions<Reminder> options = new FirebaseRecyclerOptions.Builder<Reminder>()
                .setQuery(reference, Reminder.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Reminder, ReminderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(ReminderViewHolder viewHolder, int position, Reminder model) {
                viewHolder.title.setText(model.getTitle());
                viewHolder.description.setText(model.getDescription());
                viewHolder.completeStatus.setText(model.getStatus());
                Picasso.get().load(model.getPhoto()).into(viewHolder.imageView);

                textSpeech = model.getTitle();
                textSpeechDesc = model.getDescription();

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
                       final Dialog dialog = new Dialog(AllReminders.this);
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
                        speak();
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
        recyclerView.setAdapter(adapter);

        checkRecyclerViewIsEmpty();

    }

    private void checkRecyclerViewIsEmpty() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String currentUserId = user.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Reminder").child(currentUserId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    lottieAnimationView.setVisibility(View.GONE);


                } else {
                    lottieAnimationView.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Hata durumunda yapılacaklar
                Toast.makeText(AllReminders.this, "Bir hata oluştu " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void textToSpeech(){
        mTTS = new TextToSpeech(getBaseContext(), new TextToSpeech.OnInitListener() {
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


    private void speak(){
        mTTS.speak("title: "+textSpeech + "  " +"description: " + textSpeechDesc,TextToSpeech.QUEUE_FLUSH,null);
    }

    @Override
    protected void onDestroy() {

        if (mTTS !=null){
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();

    }
}