package app.forget.forgetfulnessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class FAQs extends AppCompatActivity {
    private RelativeLayout relativeHowitWorks,relativeFeatureReq,relativeWidgets,relativeBillings,relativeCanceling;
    private ScrollView scrollView;
    private View textHowitWorks,textFeatureReq,textWidget,textBillingReq,textCancellingReq;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_f_a_qs);


        // XML dosyanızdaki view'leri bulma
        relativeHowitWorks = findViewById(R.id.relativeHowitWorks);
        scrollView = findViewById(R.id.scrollview1);
        textHowitWorks = findViewById(R.id.textHowitWorks);
        textFeatureReq = findViewById(R.id.textFeatureReq);
        relativeFeatureReq = findViewById(R.id.relativeFeatureReq);
        textWidget = findViewById(R.id.textWidgetReq);
        relativeWidgets = findViewById(R.id.relativeWidgets);
        relativeBillings = findViewById(R.id.relativeBillings);
        textBillingReq = findViewById(R.id.textupgradebillingReq);
        relativeCanceling = findViewById(R.id.relativeCanceling);
        textCancellingReq = findViewById(R.id.textCancellingReq);
        backButton = findViewById(R.id.backFaqstoSettings);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FAQs.this,HomeScreen.class);
                startActivity(intent);
            }
        });


        relativeCanceling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // textHowitWorks TextView'inin hizasına kaydırma işlemi
                scrollView.smoothScrollTo(0, textCancellingReq.getTop());
            }
        });


        relativeBillings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // textHowitWorks TextView'inin hizasına kaydırma işlemi
                scrollView.smoothScrollTo(0, textBillingReq.getTop());
            }
        });



        relativeWidgets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // textHowitWorks TextView'inin hizasına kaydırma işlemi
                scrollView.smoothScrollTo(0, textWidget.getTop());
            }
        });




        // relativeHowitWorks'a tıklama olayını ekleme
        relativeHowitWorks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // textHowitWorks TextView'inin hizasına kaydırma işlemi
                scrollView.smoothScrollTo(0, textHowitWorks.getTop());
            }
        });

        relativeFeatureReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.smoothScrollTo(0, textFeatureReq.getTop() - scrollView.getPaddingTop());

            }
        });
    }
}