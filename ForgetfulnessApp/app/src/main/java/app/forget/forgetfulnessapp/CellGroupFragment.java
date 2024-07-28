package app.forget.forgetfulnessapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CellGroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CellGroupFragment extends Fragment {
    private int groupId;
    private OnFragmentInteractionListener mListener;
    public View view;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CellGroupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CellGroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CellGroupFragment newInstance(String param1, String param2) {
        CellGroupFragment fragment = new CellGroupFragment();
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
        // Bu fragment için düzeni şişir
        view = inflater.inflate(R.layout.fragment_cell_group, container, false);

        // Textview tıklama dinleyicilerini ayarla
        int textViews[] = new int[]{R.id.textView1, R.id.textView2, R.id.textView3, R.id.textView4,
                R.id.textView5, R.id.textView6, R.id.textView7, R.id.textView8, R.id.textView9};

        for (int textView1 : textViews) {
            TextView textView = view.findViewById(textView1);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onFragmentInteraction(groupId, Integer.parseInt(view.getTag().toString()), view);
                }
            });
        }

        return view;
    }


    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public void setValue(int position, int value) {
        int textViews[] = new int[]{R.id.textView1, R.id.textView2, R.id.textView3, R.id.textView4,
                R.id.textView5, R.id.textView6, R.id.textView7, R.id.textView8, R.id.textView9};
        TextView currentView = view.findViewById(textViews[position]);
        currentView.setText(String.valueOf(value));
        currentView.setTextColor(Color.BLACK);
        currentView.setTypeface(null, Typeface.BOLD);
    }

    public boolean checkGroupCorrect() {
        ArrayList<Integer> numbers = new ArrayList<>();
        int textViews[] = new int[]{R.id.textView1, R.id.textView2, R.id.textView3, R.id.textView4,
                R.id.textView5, R.id.textView6, R.id.textView7, R.id.textView8, R.id.textView9};
        for (int textView1 : textViews) {
            TextView textView = view.findViewById(textView1);
            int number = Integer.parseInt(textView.getText().toString());
            if (numbers.contains(number)) {
                return false;
            } else {
                numbers.add(number);
            }
        }
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int groupId, int cellId, View view);
    }
}