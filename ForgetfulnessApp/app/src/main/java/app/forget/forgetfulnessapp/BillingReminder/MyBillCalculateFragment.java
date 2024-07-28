package app.forget.forgetfulnessapp.BillingReminder;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;

import app.forget.forgetfulnessapp.R;
import io.reactivex.rxjava3.annotations.NonNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyBillCalculateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyBillCalculateFragment extends Fragment {
    TextView textTotal;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyBillCalculateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyBillCalculateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyBillCalculateFragment newInstance(String param1, String param2) {
        MyBillCalculateFragment fragment = new MyBillCalculateFragment();
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
        View view  = inflater.inflate(R.layout.fragment_my_bill_calculate, container, false);

        PieChart pieChart = view.findViewById(R.id.pieChart);

        textTotal = view.findViewById(R.id.textTotal);



        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference userBillRef = databaseReference.child("Bill").child(uid);

        userBillRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // dataSnapshot içindeki verileri işleyin
                ArrayList<PieEntry> entries = new ArrayList<>();
                int totalPrice = 0; // Toplam fiyatı saklamak için bir değişken oluşturun
                for (DataSnapshot billSnapshot : dataSnapshot.getChildren()) {
                    // Her bir faturayı alın
                    String title = billSnapshot.child("title").getValue(String.class);
                    String priceString = billSnapshot.child("price").getValue(String.class);


                    // String olarak alınan price değerini integer'a dönüştürün
                    try {
                        Float price = Float.parseFloat(priceString);
                        // PieEntry listesine ekleyin
                        entries.add(new PieEntry(price, title));

                        // Toplam fiyata ekleme yapın
                        totalPrice += price;
                    } catch (NumberFormatException e) {
                        // Dönüştürme hatası durumunda burası çalışır
                        // Hata yönetimi burada yapılabilir
                        e.printStackTrace();

                }
                    textTotal.setText(String.valueOf(totalPrice));


                }

                // PieChart'i güncelleyin
                PieDataSet pieDataSet = new PieDataSet(entries, "");
                pieDataSet.setDrawIcons(false);
                pieDataSet.setSliceSpace(3f);
                pieDataSet.setIconsOffset(new MPPointF(0, 40));
                pieDataSet.setSelectionShift(5f);
                pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);





                PieData pieData = new PieData(pieDataSet);
                pieData.setValueTextColor(Color.BLACK);
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setUsePercentValues(true);
                pieChart.setExtraOffsets(5, 10, 5, 5);
                //pieChart.animateY(1000);
                pieChart.setDragDecelerationFrictionCoef(0.95f);

                pieChart.setTransparentCircleColor(Color.BLACK);
                pieChart.setTransparentCircleAlpha(110);

                pieChart.setHoleRadius(58f);
                pieChart.setTransparentCircleRadius(61f);

                pieChart.setDrawCenterText(true);

                pieChart.setRotationAngle(0);
                // enable rotation of the chart by touch
                pieChart.setRotationEnabled(true);
                pieChart.setHighlightPerTapEnabled(true);

                pieChart.animateY(1400, Easing.EaseInOutQuad);
                pieChart.setDrawHoleEnabled(true);
                pieChart.setHoleColor(Color.BLACK);
                pieChart.invalidate();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Hata durumunda işlemleri gerçekleştirin
                Log.w("TAG", "Veri okuma işlemi iptal edildi.", databaseError.toException());
            }
        });

        return view;

    }

}