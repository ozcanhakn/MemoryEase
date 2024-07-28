package app.forget.forgetfulnessapp.Game;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import app.forget.forgetfulnessapp.R;

public class StartGameActivity extends AppCompatActivity {
    RelativeLayout relativePlayButton;
    ImageView memoryEaseLogo;
    TextView textView,gameDescription;
    LinearLayout imageIcons;

    ImageView iconBanana, iconMoon, iconTiger, iconSunflower, iconBlossom, iconMagicWand, iconWaterMelon,
    iconCupcake, iconBTennis, iconUnicorn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start_game);

        relativePlayButton = findViewById(R.id.relativePlayButton);
        relativePlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateAndTransition();
            }
        });

        // Animasyonları yükle
        Animation scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);


        memoryEaseLogo = findViewById(R.id.memoryEaseLogo);
        iconBanana = findViewById(R.id.iconBanana);
        iconMoon = findViewById(R.id.iconMoon);
        iconSunflower = findViewById(R.id.iconSunflower);
        iconUnicorn = findViewById(R.id.iconUnicorn);
        iconTiger = findViewById(R.id.iconTiger);
        iconBlossom = findViewById(R.id.iconBlossom);
        iconMagicWand = findViewById(R.id.iconMagicWand);
        iconWaterMelon = findViewById(R.id.iconWatermelon);
        iconCupcake = findViewById(R.id.iconCupcake);
        iconBTennis = findViewById(R.id.iconBTennis);

        imageIcons= findViewById(R.id.imageIcons);

        gameDescription = findViewById(R.id.gameDescription);




        // Butonun onTouchListener'ını ayarla
        relativePlayButton.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.startAnimation(scaleDown);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.startAnimation(scaleUp);
                    break;
            }
            return false;
        });

         textView = findViewById(R.id.memoryHeroText);
        // Gradient ayarları
        int[] colors = {0xFFFAF5CD, 0xFFFFFFFF}; // Gradient renkleri
        float[] positions = {0.0f, 1.0f}; // Gradient pozisyonları
        Shader textShader = new LinearGradient(0, 0, 0, textView.getTextSize(),
                colors, positions, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(textShader);

        // Shadow ayarları
        textView.setShadowLayer(1.5f, -1, 1, 0xFF172B44); // 1.5f blur radius, (-1, 1) offset ve shadow rengi




        // 15dp to pixel conversion
        float translationDistance3 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());

        ObjectAnimator moveUp3 = ObjectAnimator.ofFloat(gameDescription, "translationY", 0, -translationDistance3);
        moveUp3.setDuration(2500); // 2.5 seconds

        ObjectAnimator moveDown3 = ObjectAnimator.ofFloat(gameDescription, "translationY", -translationDistance3, translationDistance3);
        moveDown3.setDuration(2500); // 2.5 seconds

        ObjectAnimator moveBackToCenter3 = ObjectAnimator.ofFloat(gameDescription, "translationY", translationDistance3, 0);
        moveBackToCenter3.setDuration(2500); // 2.5 seconds

        AnimatorSet animatorSet3 = new AnimatorSet();
        animatorSet3.playSequentially(moveUp3, moveDown3, moveBackToCenter3);
        animatorSet3.setStartDelay(0);
        animatorSet3.start();


        // 15dp to pixel conversion
        float translationDistance2 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());

        ObjectAnimator moveUp2 = ObjectAnimator.ofFloat(textView, "translationY", 0, -translationDistance2);
        moveUp2.setDuration(2500); // 2.5 seconds

        ObjectAnimator moveDown2 = ObjectAnimator.ofFloat(textView, "translationY", -translationDistance2, translationDistance2);
        moveDown2.setDuration(2500); // 2.5 seconds

        ObjectAnimator moveBackToCenter2 = ObjectAnimator.ofFloat(textView, "translationY", translationDistance2, 0);
        moveBackToCenter2.setDuration(2500); // 2.5 seconds

        AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.playSequentially(moveUp2, moveDown2, moveBackToCenter2);
        animatorSet2.setStartDelay(0);
        animatorSet2.start();



        // 15dp to pixel conversion
        float translationDistance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());

        ObjectAnimator moveUp = ObjectAnimator.ofFloat(relativePlayButton, "translationY", 0, -translationDistance);
        moveUp.setDuration(2500); // 2.5 seconds

        ObjectAnimator moveDown = ObjectAnimator.ofFloat(relativePlayButton, "translationY", -translationDistance, translationDistance);
        moveDown.setDuration(2500); // 2.5 seconds

        ObjectAnimator moveBackToCenter = ObjectAnimator.ofFloat(relativePlayButton, "translationY", translationDistance, 0);
        moveBackToCenter.setDuration(2500); // 2.5 seconds

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(moveUp, moveDown, moveBackToCenter);
        animatorSet.setStartDelay(0);
        animatorSet.start();



        // 15dp to pixel conversion
        float translationDistance1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());

        ObjectAnimator moveUp1 = ObjectAnimator.ofFloat(imageIcons, "translationY", 0, -translationDistance1);
        moveUp1.setDuration(2500); // 2.5 seconds

        ObjectAnimator moveDown1 = ObjectAnimator.ofFloat(imageIcons, "translationY", -translationDistance1, translationDistance1);
        moveDown1.setDuration(2500); // 2.5 seconds

        ObjectAnimator moveBackToCenter1 = ObjectAnimator.ofFloat(imageIcons, "translationY", translationDistance1, 0);
        moveBackToCenter1.setDuration(2500); // 2.5 seconds

        AnimatorSet animatorSet1 = new AnimatorSet();
        animatorSet1.playSequentially(moveUp1, moveDown1, moveBackToCenter1);
        animatorSet1.setStartDelay(0);
        animatorSet1.start();

        // Repeat the animation indefinitely
        animatorSet.addListener(new AnimatorSet.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSet.start(); // restart the animation
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        // Repeat the animation indefinitely
        animatorSet1.addListener(new AnimatorSet.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSet1.start(); // restart the animation
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }



    private void animateAndTransition() {
        // ImageView'in büyütülmesi için animasyon
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(memoryEaseLogo, "scaleX", 1f, 5f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(memoryEaseLogo, "scaleY", 1f, 5f);

        scaleXAnimator.setDuration(1000);
        scaleYAnimator.setDuration(1000);

        scaleXAnimator.setInterpolator(new DecelerateInterpolator());
        scaleYAnimator.setInterpolator(new DecelerateInterpolator());

        scaleXAnimator.start();
        scaleYAnimator.start();

        scaleYAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Animasyon bittiğinde yeni aktiviteye geç
                Intent intent = new Intent(StartGameActivity.this, ObjectSequenceActivity.class);
                startActivity(intent);
                // Geçiş animasyonu olmadan yeni aktiviteyi başlat
                overridePendingTransition(0, 0);
            }
        });
    }

}