package app.forget.forgetfulnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import app.forget.forgetfulnessapp.Advice.CalenderActivity;
import app.forget.forgetfulnessapp.BillingReminder.MyAllBillFragment;
import app.forget.forgetfulnessapp.BillingReminder.MyBillCalculateFragment;
import app.forget.forgetfulnessapp.CreateReminder.CreateReminderLocationFragment;
import app.forget.forgetfulnessapp.CreateReminder.CreateReminderTimeFragment;
import app.forget.forgetfulnessapp.CreateReminder.PreviousReminderFragment;
import app.forget.forgetfulnessapp.CreateReminder.VPAdapter;
import app.forget.forgetfulnessapp.Model.Bill;
import app.forget.forgetfulnessapp.Model.Reminder;
import app.forget.forgetfulnessapp.SettingsActivity.EmailSupportActivity;
import app.forget.forgetfulnessapp.ViewHolder.BillViewHolder;
import app.forget.forgetfulnessapp.ViewHolder.ReminderViewHolder;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;

public class RemindBillActivity extends AppCompatActivity {

    private Calendar currentDate;
    private TextView tvMonthYear;
    private GridView gridView;
    String date,day;
    int selectedYear, selectedMonth;
    String title,desc,price,automaticormanuel;
    EditText etTitle,etDesc,etPrice,etAutomaticOrManuel;
    String[] typeList = {"Automatic","Manuel"};



    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_remind_bill);

        currentDate = Calendar.getInstance();



        tabLayout = findViewById(R.id.tabLayoutBill);
        viewPager = findViewById(R.id.viewPagerBill);

        viewPager.setBackgroundColor(Color.WHITE);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"));
        tabLayout.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));

        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new MyAllBillFragment(),getString(R.string.billremind1));
        vpAdapter.addFragment(new MyBillCalculateFragment(), getString(R.string.billgraphic));
        viewPager.setAdapter(vpAdapter);




        tvMonthYear = findViewById(R.id.tvMonthYear);
        gridView = findViewById(R.id.gridView);
        ImageView btnPrevious = findViewById(R.id.btnPrevious);
        ImageView btnNext = findViewById(R.id.btnNext);

        updateCalendar();

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, -1);
                updateCalendar();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, 1);
                updateCalendar();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Tüm öğelerin kenar rengini varsayılan renge (siyah) ayarla
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View childView = parent.getChildAt(i);
                    TextView textView = childView.findViewById(R.id.textViewGridItem);
                    textView.setTextColor(Color.WHITE);
                    textView.setBackground(getResources().getDrawable(R.drawable.grid_item_border));
                }

                // Tıklanan kutucuğun kenar rengini istenen renge ayarla
                TextView textView = view.findViewById(R.id.textViewGridItem);
                textView.setBackground(getResources().getDrawable(R.drawable.selected_grid_item_border));
                textView.setTextColor(Color.WHITE);

                day = textView.getText().toString();

                SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate.getTime());
                selectedYear = calendar.get(Calendar.YEAR);
                selectedMonth = calendar.get(Calendar.MONTH) + 1; // Ay 0'dan başlar, bu yüzden 1 ekliyoruz

                // Yılı ve ayı TextView'a yazdır



                setBillReminderDialog();
            }
        });

    }

    private void updateCalendar() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        tvMonthYear.setText(sdf.format(currentDate.getTime()));

        ArrayList<String> daysList = new ArrayList<>();
        Calendar calendar = (Calendar) currentDate.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        calendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth);

        while (daysList.size() < 35) { // to show 5 weeks always
            daysList.add(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.gridview_item, R.id.textViewGridItem, daysList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView textView = view.findViewById(R.id.textViewGridItem);
                String day = daysList.get(position);

                // Mevcut ayın dışındaki günleri belirle
                Calendar currentCalendar = Calendar.getInstance();
                currentCalendar.setTime(currentDate.getTime());
                int currentMonth = currentCalendar.get(Calendar.MONTH);

                if (position < firstDayOfMonth || currentMonth != currentDate.get(Calendar.MONTH)) {
                    // Mevcut ayın dışındaki günleri farklı bir arka plan rengiyle göster
                    textView.setBackgroundResource(R.drawable.other_gridview_item);
                    textView.setTextColor(Color.BLACK);

                } else {
                    // Mevcut ayın günlerini varsayılan arka plan rengiyle göster
                    textView.setBackgroundResource(R.drawable.grid_item_border);
                }

                // Mevcut günü seçili olarak işaretle
                int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);
                if (day.equals(String.valueOf(currentDay))) {
                    textView.setBackgroundResource(R.drawable.selected_grid_item_border);
                }

                return view;
            }
        };
        gridView.setAdapter(adapter);
    }

    private void setBillReminderDialog(){
        final Dialog dialog = new Dialog(RemindBillActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_box_bill_reminder_layout);

        ImageView dialogCancelButton = dialog.findViewById(R.id.dialogCancelButton);
        TextView textSelectedDate = dialog.findViewById(R.id.textSelectedDate);
        etTitle = dialog.findViewById(R.id.etTitle);
        etDesc = dialog.findViewById(R.id.etDesc);
        etPrice = dialog.findViewById(R.id.etPrice);
        etAutomaticOrManuel = dialog.findViewById(R.id.etAutomaticOrManuel);
        RelativeLayout setButton = dialog.findViewById(R.id.relativeButtonSetBill);

        textSelectedDate.setText(selectedYear + " / " + selectedMonth+" / "+day);

        etAutomaticOrManuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(R.string.make_a_choice);

                builder.setItems(typeList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String whatisitherefor1 = typeList[which];
                        etAutomaticOrManuel.setText(whatisitherefor1);
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });




        dialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = etTitle.getText().toString().trim();
                desc = etDesc.getText().toString().trim();
                price = etPrice.getText().toString().trim();
                automaticormanuel = etAutomaticOrManuel.getText().toString().trim();

                setDataBase();
                dialog.dismiss();
            }
        });






        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

    private void setDataBase(){
        date = selectedYear+"/"+selectedMonth+"/"+day;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        String billUid = FirebaseDatabase.getInstance().getReference("Bill").push().getKey();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Bill").child(userId).child(billUid);

        Map<String, Object> bill = new HashMap<>();
        bill.put("title",title);
        bill.put("desc",desc);
        bill.put("price", price);
        bill.put("date",date);
        bill.put("type",automaticormanuel);
        bill.put("status","not completed");
        bill.put("status1","not completed");
        bill.put("id",billUid);
        bill.put("time","09:00");
        bill.put("time1","15:00");

        reference.setValue(bill, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                if (error == null){
                    Toast.makeText(RemindBillActivity.this, R.string.sucessfull_estab, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(RemindBillActivity.this, "Sorry, we encountered an error, try again later", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}



