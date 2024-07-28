package app.forget.forgetfulnessapp.Advice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import app.forget.forgetfulnessapp.HomeScreen;
import app.forget.forgetfulnessapp.Model.Notes;
import app.forget.forgetfulnessapp.R;
import app.forget.forgetfulnessapp.UpgradeForProActivity;
import app.forget.forgetfulnessapp.ViewHolder.NotesViewHolder;
import io.reactivex.rxjava3.annotations.NonNull;

public class CalenderActivity extends AppCompatActivity {
    private CalendarView calendarView;
    private TextView noteTextView;
    private int clickCount = 0;
    private ImageView backButtonCalender;

    EditText changeTitleBox,changeDescBox;

    private DatabaseReference databaseReference;
    String title,desc;

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private static final int REQUEST_CODE_SPEECH_INPUT_DESC = 1001;

    String selectedDate, note, header;


    RecyclerView recNotes;

    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Notes, NotesViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_calender);

        calendarView = findViewById(R.id.calendarView);
        noteTextView = findViewById(R.id.noteTextView);
        backButtonCalender = findViewById(R.id.backButtonCalender);

        recNotes = findViewById(R.id.recyclerNotes);

        //All Reminders Yüklenmesi
        recNotes.setHasFixedSize(true);

        // GridLayoutManager kullanarak iki sütunlu bir düzen ayarla
        layoutManager = new LinearLayoutManager(this);
        recNotes.setLayoutManager(layoutManager);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Notes").child(userId);

        FirebaseRecyclerOptions<Notes> options = new FirebaseRecyclerOptions.Builder<Notes>()
                .setQuery(reference, Notes.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Notes, NotesViewHolder>(options) {
            private static final int MAX_LINES_COLLAPSED = 1; // Başlangıçta kaç satır gösterileceğini belirleyin
            private static final int MAX_LINES_EXPANDED = Integer.MAX_VALUE; // Tüm notu göstermek için çok büyük bir değer

            @Override
            protected void onBindViewHolder(NotesViewHolder viewHolder, int position, Notes model) {
                viewHolder.dates.setText(model.getDates());
                viewHolder.notes.setText(model.getNotes());
                viewHolder.header.setText(model.getHeaders());

                if (model.getNotes().length() > 20) {
                    viewHolder.moreButton.setVisibility(View.VISIBLE);

                    viewHolder.notes.setText(model.getNotes().substring(0, 20)); // İlk 20 karakteri göster

                    viewHolder.moreButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            viewHolder.notes.setMaxLines(Integer.MAX_VALUE);
                            viewHolder.notes.setText(model.getNotes()); // Tam notu göster
                            viewHolder.moreButton.setVisibility(View.GONE);
                            viewHolder.lessButton.setVisibility(View.VISIBLE);

                            // Yüksekliği güncelle
                            updateItemHeight(viewHolder.itemView, ViewGroup.LayoutParams.WRAP_CONTENT);
                        }
                    });

                    viewHolder.lessButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //viewHolder.notes.setMaxLines(MAX_LINES_COLLAPSED);
                            viewHolder.notes.setText(model.getNotes().substring(0, 20)); // İlk 20 karakteri göster
                            viewHolder.moreButton.setVisibility(View.VISIBLE);
                            viewHolder.lessButton.setVisibility(View.GONE);

                            // Yüksekliği güncelle
                           //updateItemHeight(viewHolder.itemView, ViewGroup.LayoutParams.WRAP_CONTENT);
                        }
                    });
                } else {
                    // Not 20 karakterden kısa ise "More" veya "Less" butonları gizli olsun
                    viewHolder.moreButton.setVisibility(View.GONE);
                    viewHolder.lessButton.setVisibility(View.GONE);
                    viewHolder.notes.setText(model.getNotes());

                    // Yüksekliği güncelle
                    //updateItemHeight(viewHolder.itemView, ViewGroup.LayoutParams.WRAP_CONTENT);
                }


                Log.d("NOTES", "onBindViewHolder: " + model.getNotes());

                viewHolder.viewMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickCount++;

                        if (clickCount % 2 == 1) {
                            // Tek sayıda tıklama (ilk tıklama)
                            viewHolder.editButton.setVisibility(View.VISIBLE);
                            viewHolder.deleteButton.setVisibility(View.VISIBLE);
                        } else {
                            // Çift sayıda tıklama (ikinci tıklama)
                            viewHolder.editButton.setVisibility(View.GONE);
                            viewHolder.deleteButton.setVisibility(View.GONE);
                        }

                    }
                });

                viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog = new Dialog(CalenderActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.edit_calendar_item);


                        EditText changeTitleBox = dialog.findViewById(R.id.calenderChangeTitleBox);
                        EditText changeDescBox = dialog.findViewById(R.id.calenderChanceDescBox);
                        RelativeLayout buttonChange = dialog.findViewById(R.id.relativeOkeyButtonCalender);
                        RelativeLayout buttonCancel = dialog.findViewById(R.id.relativeCancelButtonCalender);


                        changeTitleBox.setText(model.getHeaders());
                        changeDescBox.setText(model.getNotes());


                        buttonChange.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String newTitle = changeTitleBox.getText().toString();
                                String newDesc = changeDescBox.getText().toString();

                                DatabaseReference itemRef = reference.child(getRef(position).getKey());
                                itemRef.child("headers").setValue(newTitle);
                                itemRef.child("notes").setValue(newDesc);

                                Toast.makeText(CalenderActivity.this, R.string.change, Toast.LENGTH_SHORT).show();

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

                viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference itemRef = reference.child(getRef(position).getKey());

                        // Veritabanındaki ilgili öğeyi sil
                        itemRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Silme başarılı olduysa burada gerekli işlemleri yapabilirsiniz
                                    Toast.makeText(CalenderActivity.this, getString(R.string.delete_reminder), Toast.LENGTH_SHORT).show();
                                } else {
                                    // Silme başarısız olduysa burada gerekli işlemleri yapabilirsiniz
                                    Toast.makeText(CalenderActivity.this, getString(R.string.unable_reminder), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });


                onDataChanged();

            }


            @Override
            public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.notes_item_layout, parent, false);

                return new NotesViewHolder(itemView);
            }
        };


        recNotes.setAdapter(adapter);


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {


                final Dialog dialog = new Dialog(CalenderActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.add_calendar_item);

                changeTitleBox = dialog.findViewById(R.id.calenderAddTitleBox);
                changeDescBox = dialog.findViewById(R.id.calenderAddDescBox);
                RelativeLayout buttonChange = dialog.findViewById(R.id.relativeOkeyButtonCalenderAdd);
                RelativeLayout buttonCancel = dialog.findViewById(R.id.relativeCancelButtonCalenderAdd);
                TextView selectedDateText = dialog.findViewById(R.id.selectedDateText);
                ImageView plannerMicHeader = dialog.findViewById(R.id.plannerMic);
                ImageView plannerMicNote = dialog.findViewById(R.id.plannerMic1);



                plannerMicNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        speak();
                    }
                });

                plannerMicHeader.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        speakHeader();
                    }
                });



                selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;
                selectedDateText.setText(selectedDate);


                buttonChange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        header = changeTitleBox.getText().toString().trim();
                        note = changeDescBox.getText().toString().trim();
                        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                        DatabaseReference reference1 = database1.getReference("Premium").child(userId);

                        reference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                String premiumValue = snapshot.child("premium").getValue(String.class);
                                if ("yes".equals(premiumValue)){

                                    saveNotesToDatabase();
                                }else {
                                    List<String> creationDates = new ArrayList<>();
                                    for (DataSnapshot creationDateSnapshot : snapshot.getChildren()){
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

                                    int noteCount = 0;

                                    try {
                                        if (creationDates.size() >= 2) {
                                            for (String date : creationDates) {
                                                Date alarmDate = sdf.parse(date);
                                                long differenceInMilliseconds = currentDate.getTime() - alarmDate.getTime();
                                                long differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInMilliseconds);

                                                // Eğer iki tarih arasındaki fark bir hafta içindeyse, sayaç artır
                                                if (differenceInDays <= 7) {
                                                    noteCount++;
                                                }
                                            }
                                            if (noteCount >=2){
                                                Toast.makeText(CalenderActivity.this, getString(R.string.upgrade_info_toast), Toast.LENGTH_SHORT).show();
                                                //Yönlendir
                                                Intent intent = new Intent(CalenderActivity.this, UpgradeForProActivity.class);
                                                startActivity(intent);

                                            }else {
                                                saveNotesToDatabase();
                                            }
                                        }
                                    }catch (Exception e){

                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                            }
                        });

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
        });


        backButtonCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalenderActivity.this, HomeScreen.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();


    }

    //Kısıtlama Getir Haftada 2 adet ekleyebilme özelliği... Belki minimal bir bildirimle hatırlatılabilir.
    public void saveNotesToDatabase()
    {

        Date now = new Date();

        // Date nesnesini istediğiniz biçime dönüştürün
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(now);

        String notesUid = FirebaseDatabase.getInstance().getReference("Notes").push().getKey();

        Map<String, Object> userReminder = new HashMap<>();
        userReminder.put("dates", selectedDate);
        userReminder.put("notes", note);
        userReminder.put("headers", header);
        userReminder.put("creationDate",formattedDate);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();

        // Firebase Realtime Database referansını al
        databaseReference = FirebaseDatabase.getInstance().getReference("Notes").child(userid).child(notesUid);


        databaseReference.setValue(userReminder, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                if (error == null) {
                    Toast.makeText(CalenderActivity.this, R.string.sucadded, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CalenderActivity.this, R.string.failadd, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void updateItemHeight(View itemView, int newHeight) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        params.height = newHeight;
        itemView.setLayoutParams(params);
        itemView.requestLayout();
    }

    private void updateRecyclerViewHeight() {
        int itemCount = adapter.getItemCount();
        int totalHeight = 0;

        for (int i = 0; i < itemCount; i++) {
            View childView = recNotes.getChildAt(i);
            totalHeight += childView.getHeight();
        }

        recNotes.getLayoutParams().height = totalHeight;
        recNotes.requestLayout();
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
            Toast.makeText(CalenderActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }

    private void speakHeader(){

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
                Toast.makeText(CalenderActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();


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

                    desc = (result.get(0));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            changeDescBox.setText(desc);
                        }
                    });
                    Log.d("TAG", "onActivityResult: "+desc);
                }
                break;
            } case REQUEST_CODE_SPEECH_INPUT_DESC: {
                if (resultCode == RESULT_OK && null !=data){

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    title = result.get(0);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            changeTitleBox.setText(title);
                        }
                    });


                }
                break;
            }
        }
    }

}