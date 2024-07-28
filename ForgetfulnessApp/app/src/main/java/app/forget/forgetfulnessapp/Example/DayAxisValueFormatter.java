package app.forget.forgetfulnessapp.Example;

import android.graphics.Color;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DayAxisValueFormatter extends ValueFormatter {

    private final SimpleDateFormat sdf = new SimpleDateFormat("E", Locale.getDefault());

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        // value, x eksenindeki değerdir (0'dan başlayarak)
        // Pazar gününden başlaması için sabit olarak 0 değeri veriyoruz
        int haftaninGunu = (int) value;

        // Güncel günü temsil eden bir Calendar objesi oluşturun
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        // Calendar objesinin gününü ayarlayın
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        // Belirtilen gün kadar gün ekleyin
        calendar.add(Calendar.DAY_OF_WEEK, haftaninGunu);

        // Güncellenmiş tarihi alın
        Date guncellenmisTarih = calendar.getTime();

        // Biçimlendirilmiş gün adını döndürün
        String formattedGün = sdf.format(guncellenmisTarih);

        // XAxis'teki yazı rengini ayarla (örneğin, beyaz renk)
        ((XAxis) axis).setTextColor(Color.WHITE);

        return formattedGün;

    }
}