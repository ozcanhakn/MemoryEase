package app.forget.forgetfulnessapp.Advice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

import app.forget.forgetfulnessapp.R;

public class CalendarAdapter extends ArrayAdapter<String> {
    private final Context mContext;
    private final ArrayList<String> mDaysList;

    public CalendarAdapter(Context context, ArrayList<String> daysList) {
        super(context, 0, daysList);
        this.mContext = context;
        this.mDaysList = daysList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.gridview_item, parent, false);
        }

        TextView textView = view.findViewById(R.id.textViewGridItem);
        textView.setText(mDaysList.get(position));

        return view;
    }
}
