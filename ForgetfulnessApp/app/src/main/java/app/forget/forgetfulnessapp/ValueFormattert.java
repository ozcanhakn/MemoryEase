package app.forget.forgetfulnessapp;

import android.graphics.Color;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ValueFormattert extends ValueFormatter {

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

    @Override
    public String getAxisLabel(float value, AxisBase axis) {

        // value, x eksenindeki değerdir (0'dan başlayarak)
        // Bugünden itibaren önceki 7 günü almak için 6'dan başlayarak azaltıyoruz
        int daysAgo = 6 - (int) value;

        // Güncel günü temsil eden bir Calendar objesi oluşturun
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        // Belirtilen gün kadar gün çıkarın
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo);

        // Güncellenmiş tarihi alın
        Date guncellenmisTarih = calendar.getTime();

        // Biçimlendirilmiş tarihi döndürün
        String formattedDate = sdf.format(guncellenmisTarih);

        // XAxis'teki yazı rengini ayarla (örneğin, beyaz renk)
        ((XAxis) axis).setTextColor(Color.WHITE);

        // Texti 90 derece döndür
        ((XAxis) axis).setLabelRotationAngle(90f);


        return formattedDate;

    }
}