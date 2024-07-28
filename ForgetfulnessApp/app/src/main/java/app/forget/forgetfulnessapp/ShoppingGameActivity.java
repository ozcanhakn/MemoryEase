package app.forget.forgetfulnessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import app.forget.forgetfulnessapp.Game.ObjectSequenceActivity;
import app.forget.forgetfulnessapp.Game.ShoppingCartActivity;
import app.forget.forgetfulnessapp.Game.StartGameActivity;

public class ShoppingGameActivity extends AppCompatActivity {
    ImageView memoryEaseLogo;
    TextView gameDescription;
    Button startShoppingGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_shopping_game);

        //Tanımlamalar
        memoryEaseLogo = findViewById(R.id.memoryEaseLogoShoppingGame);
        gameDescription = findViewById(R.id.gameDescription1);
        startShoppingGame = findViewById(R.id.startShoppingGame);

        //Metotlar
        init();
        animation();
        itemAnimation();






    }
    public void init(){
        startShoppingGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memoryEaseLogoAnimateAndTransition();
            }
        });
    }

    public void animation(){
        // Animasyonları yükle
        Animation scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);


        // Butonun onTouchListener'ını ayarla
        startShoppingGame.setOnTouchListener((v, event) -> {
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
    }

    public void itemAnimation(){
        // 15dp to pixel conversion
        float translationDistance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());

        ObjectAnimator moveUp = ObjectAnimator.ofFloat(gameDescription, "translationY", 0, -translationDistance);
        moveUp.setDuration(2500); // 2.5 seconds

        ObjectAnimator moveDown = ObjectAnimator.ofFloat(gameDescription, "translationY", -translationDistance, translationDistance);
        moveDown.setDuration(2500); // 2.5 seconds

        ObjectAnimator moveBackToCenter = ObjectAnimator.ofFloat(gameDescription, "translationY", translationDistance, 0);
        moveBackToCenter.setDuration(2500); // 2.5 seconds

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(moveUp, moveDown, moveBackToCenter);
        animatorSet.setStartDelay(0);
        animatorSet.start();

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

    }

    public void memoryEaseLogoAnimateAndTransition(){
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
                Intent intent = new Intent(ShoppingGameActivity.this, ShoppingCartActivity.class);
                startActivity(intent);
                // Geçiş animasyonu olmadan yeni aktiviteyi başlat
                overridePendingTransition(0, 0);
            }
        });
    }
}