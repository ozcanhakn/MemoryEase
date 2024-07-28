package app.forget.forgetfulnessapp.Example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.forget.forgetfulnessapp.Game.Game5x4Activity;
import app.forget.forgetfulnessapp.Game.Game6x5Activity;
import app.forget.forgetfulnessapp.Game.GameMatch3Activity;
import app.forget.forgetfulnessapp.Game.ObjectSequenceActivity;
import app.forget.forgetfulnessapp.Game.StartGameActivity;
import app.forget.forgetfulnessapp.Model.Product;
import app.forget.forgetfulnessapp.R;
import app.forget.forgetfulnessapp.ShoppingGameActivity;
import app.forget.forgetfulnessapp.ViewHolder.GamesAdapter;
import app.forget.forgetfulnessapp.ViewHolder.ProductAdapter;

public class NotificationActivity extends AppCompatActivity {
    ImageView memoryEaseLogoShuffle,memoryEaseLogoObject,memoryEaseLogoShopping,memoryEaseLogoShuffle3,memoryEaseLogoShuffle6;
    CardView cardObjectCardGame,cardShoppingGame,cardShuffleCardGame,cardShuffleCardGame6,cardShuffleCardGame3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        //Tanımalamalar
        memoryEaseLogoShuffle = findViewById(R.id.memoryEaseLogoBlurGameShuffleCard);
        memoryEaseLogoShuffle3 = findViewById(R.id.memoryEaseLogoBlurGameShuffleCard3);
        memoryEaseLogoShuffle6 = findViewById(R.id.memoryEaseLogoBlurGameShuffleCard6);

        memoryEaseLogoObject = findViewById(R.id.memoryEaseLogoBlurGameObject);
        memoryEaseLogoShopping = findViewById(R.id.memoryEaseLogoBlurShoppingGame);
        cardObjectCardGame = findViewById(R.id.cardObjectCardGame);
        cardShoppingGame = findViewById(R.id.cardShoppingGame);
        cardShuffleCardGame = findViewById(R.id.cardShuffleCardGame);
        cardShuffleCardGame6 = findViewById(R.id.cardShuffleCardGame6);
        cardShuffleCardGame3 = findViewById(R.id.cardShuffleCardGame3);




        animationMemoryEaseLogo();
        init();
    }
    public void animationMemoryEaseLogo(){
        // RotateAnimation oluştur
        RotateAnimation rotate = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotate.setDuration(2000); // 2 saniye sürer
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setRepeatCount(Animation.INFINITE); // Sonsuz tekrar

        // Animasyonu başlat
        memoryEaseLogoShuffle.startAnimation(rotate);
        memoryEaseLogoShopping.startAnimation(rotate);
        memoryEaseLogoObject.startAnimation(rotate);
        memoryEaseLogoShuffle3.startAnimation(rotate);
        memoryEaseLogoShuffle6.startAnimation(rotate);

    }

    public void init(){
        //Card tıklama olayları
        cardObjectCardGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationActivity.this, StartGameActivity.class);
                startActivity(intent);

            }
        });

        cardShoppingGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationActivity.this, ShoppingGameActivity.class);
                startActivity(intent);

            }
        });

        cardShuffleCardGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationActivity.this, Game5x4Activity.class);
                startActivity(intent);

            }
        });

        cardShuffleCardGame6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationActivity.this, Game6x5Activity.class);
                startActivity(intent);

            }
        });

        cardShuffleCardGame3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationActivity.this, GameMatch3Activity.class);
                startActivity(intent);

            }
        });

    }
}