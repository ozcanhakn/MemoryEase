package app.forget.forgetfulnessapp.CreateReminder;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import app.forget.forgetfulnessapp.AllReminders;
import app.forget.forgetfulnessapp.Model.PreviousModel;
import app.forget.forgetfulnessapp.R;
import app.forget.forgetfulnessapp.ViewHolder.PreviousReminderViewHolder;
import io.reactivex.rxjava3.annotations.NonNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreviousReminderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreviousReminderFragment extends Fragment {
    RecyclerView recyclerViewPrevious;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<PreviousModel, PreviousReminderViewHolder> adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PreviousReminderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PreviousReminderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PreviousReminderFragment newInstance(String param1, String param2) {
        PreviousReminderFragment fragment = new PreviousReminderFragment();
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
        View view =  inflater.inflate(R.layout.fragment_previous_reminder, container, false);


        //All Reminders Yüklenmesi
        recyclerViewPrevious = view.findViewById(R.id.recyclerPreviousReminder);
        recyclerViewPrevious.setHasFixedSize(true);

        // GridLayoutManager kullanarak iki sütunlu bir düzen ayarla
        layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerViewPrevious.setLayoutManager(layoutManager);




        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Reminder").child(userId);


        FirebaseRecyclerOptions<PreviousModel> options = new FirebaseRecyclerOptions.Builder<PreviousModel>()
                .setQuery(reference.orderByChild("creationDate").limitToFirst(20), PreviousModel.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<PreviousModel, PreviousReminderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(PreviousReminderViewHolder viewHolder, int position, PreviousModel model) {
                if (model.getTime() != null){
                    viewHolder.textviewTime.setText(model.getTime());

                }else {
                    viewHolder.textviewTime.setVisibility(View.GONE);
                    viewHolder.textTimeZone.setText(R.string.bylocation);
                }
                viewHolder.title.setText(model.getTitle());
                String deger = model.getMonday();
                Log.d("RENK", "onBindViewHolder: "+deger);



                if (model.getTime() != null){
                    String time = model.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.getDefault());

                    try {
                        Date date = sdf.parse(time);

                        // Saatin 12:00'den önce veya sonra olup olmadığını kontrol et
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);

                        if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
                            viewHolder.textTimeZone.setText("AM");
                        } else {
                            viewHolder.textTimeZone.setText("PM");
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    viewHolder.textviewTime.setVisibility(View.GONE);
                }


                if ("alarm".equals(model.getType())){
                    viewHolder.linearLayoutDayOfWeek.setVisibility(View.GONE);
                    if ("not completed".equals(model.getStatus())){
                        viewHolder.textStatus.setText(getString(R.string.not_completed));
                    }else {
                        viewHolder.textStatus.setText(getString(R.string.completed));
                        int renk = Color.parseColor("#FF90BC");
                        viewHolder.textStatus.setTextColor(renk);

                    }


                }else {
                    viewHolder.linearLayoutDayOfWeek.setVisibility(View.VISIBLE);
                    viewHolder.textStatus.setVisibility(View.INVISIBLE);

                }


                if ("yes".equals(model.getMonday()) && model.getMonday() != null){
                    int renk = Color.parseColor("#FF90BC");
                    viewHolder.textMonday.setTextColor(renk);
                }else {
                    Log.d("RENK", "onBindViewHolder: değer null veya no");
                }
                if ("yes".equals(model.getFriday()) && model.getFriday() != null){
                    int renk = Color.parseColor("#FF90BC");
                    viewHolder.textFriday.setTextColor(renk);
                }else {
                    Log.d("RENK", "onBindViewHolder: değer null veya no");
                }
                if ("yes".equals(model.getSaturday()) && model.getSaturday() != null){
                    int renk = Color.parseColor("#FF90BC");
                    viewHolder.textSaturday.setTextColor(renk);
                }else {
                    Log.d("RENK", "onBindViewHolder: değer null veya no");
                }
                if ("yes".equals(model.getSunday()) && model.getSunday() != null){
                    int renk = Color.parseColor("#FF90BC");
                    viewHolder.textSunday.setTextColor(renk);
                }else {
                    Log.d("RENK", "onBindViewHolder: değer null veya no");
                }
                if ("yes".equals(model.getWednesday()) && model.getWednesday() != null){
                    int renk = Color.parseColor("#FF90BC");
                    viewHolder.textWednesday.setTextColor(renk);
                }else {
                    Log.d("RENK", "onBindViewHolder: değer null veya no");
                }
                if ("yes".equals(model.getThursday()) && model.getThursday() != null){
                    int renk = Color.parseColor("#FF90BC");
                    viewHolder.textThursday.setTextColor(renk);
                }else {
                    Log.d("RENK", "onBindViewHolder: değer null veya no");
                }
                if ("yes".equals(model.getTuesday()) && model.getTuesday() != null){
                    int renk = Color.parseColor("#FF90BC");
                    viewHolder.textTuesday.setTextColor(renk);
                }else {
                    Log.d("RENK", "onBindViewHolder: değer null veya no");
                }


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
                                    Toast.makeText(getContext(), getString(R.string.delete_reminder), Toast.LENGTH_SHORT).show();
                                } else {
                                    // Silme başarısız olduysa burada gerekli işlemleri yapabilirsiniz
                                    Toast.makeText(getContext(), getString(R.string.unable_reminder), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                });

                viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog = new Dialog(getContext());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.edit_reminder_item_previous);


                        EditText changeTitleBox = dialog.findViewById(R.id.previousChangeTitleBox);
                        EditText changeDescBox = dialog.findViewById(R.id.previousChanceDescBox);
                        RelativeLayout buttonChange = dialog.findViewById(R.id.relativeOkeyButtonPre);
                        RelativeLayout buttonCancel = dialog.findViewById(R.id.relativeCancelButtonPre);


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



            }



            @Override
            public PreviousReminderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.previous_reminder_item, parent, false);

                return new PreviousReminderViewHolder(itemView);
            }
        };
        adapter.startListening();
        recyclerViewPrevious.setAdapter(adapter);

        return view;
    }
}