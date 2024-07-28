package app.forget.forgetfulnessapp.BillingReminder;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import app.forget.forgetfulnessapp.Model.Bill;
import app.forget.forgetfulnessapp.R;
import app.forget.forgetfulnessapp.RemindBillActivity;
import app.forget.forgetfulnessapp.ViewHolder.BillViewHolder;
import io.reactivex.rxjava3.annotations.NonNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyAllBillFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyAllBillFragment extends Fragment {
    RecyclerView recBill;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Bill, BillViewHolder> adapter;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyAllBillFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyAllBillFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyAllBillFragment newInstance(String param1, String param2) {
        MyAllBillFragment fragment = new MyAllBillFragment();
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
        View view =  inflater.inflate(R.layout.fragment_my_all_bill, container, false);

        //All Reminders Yüklenmesi
        recBill = view.findViewById(R.id.recBill);
        recBill.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recBill.setLayoutManager(layoutManager);//layoutManager

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Bill").child(userId);

        FirebaseRecyclerOptions<Bill> options = new FirebaseRecyclerOptions.Builder<Bill>()
                .setQuery(reference, Bill.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Bill, BillViewHolder>(options) {
            @Override
            protected void onBindViewHolder(BillViewHolder viewHolder, int position, Bill model) {

                viewHolder.billDate.setText(model.getDate());
                viewHolder.billType.setText(model.getTitle());
                viewHolder.billPayType.setText(model.getType());
                viewHolder.billAmount.setText(model.getPrice());

                Log.d("LOGBILL", "onBindViewHolder: "+model.getPrice());

                viewHolder.deleteBill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference reference1 = reference.child(getRef(position).getKey());


                        // Veritabanındaki ilgili öğeyi sil
                        reference1.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
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

            }



            @Override
            public BillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.bill_reminder_rec_bg, parent, false);
                return new BillViewHolder(itemView);
            }
        };
        adapter.startListening();
        recBill.setAdapter(adapter);

        return view;
    }
}