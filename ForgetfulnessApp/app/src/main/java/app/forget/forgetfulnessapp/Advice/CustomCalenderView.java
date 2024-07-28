package app.forget.forgetfulnessapp.Advice;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CalendarView;

import java.util.HashMap;

public class CustomCalenderView extends CalendarView {

    private HashMap<Long, String> notesHashMap = new HashMap<>();


    public CustomCalenderView(Context context) {
        super(context);
    }

    public CustomCalenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCalenderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomCalenderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void addNoteForDate(long dateInMillis, String note) {
        notesHashMap.put(dateInMillis, note);
    }

    public String getNoteForDate(long dateInMillis) {
        return notesHashMap.get(dateInMillis);
    }
}