package app.forget.forgetfulnessapp.Game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.DragEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import app.forget.forgetfulnessapp.Model.Product;
import app.forget.forgetfulnessapp.R;
import app.forget.forgetfulnessapp.RemindBillActivity;
import app.forget.forgetfulnessapp.ShoppingGameActivity;
import app.forget.forgetfulnessapp.ViewHolder.MarketGameAdapter;
import app.forget.forgetfulnessapp.ViewHolder.ProductAdapter;

public class ShoppingCartActivity extends AppCompatActivity {
    private RecyclerView recyclerViewList;
    private RecyclerView recyclerViewShelves;
    private Button btnComplete;
    private Button btnCheck;
    private ImageView cart;
    private LinearLayout paperLayout;
    private TextView paperText;

    private List<Product> productList;
    private List<Product> shelfList;
    private List<Product> selectedProducts;
    private List<Product> currentShoppingList;

    private int currentLevel = 1;
    private int currentRound = 1;
    private int score = 0;

    private TextView textScoreShoppingCart;

    private TextView tvTimer; // TextView for countdown timer

    // Countdown timer variables
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 30000; // 30 seconds
    private boolean timerRunning;

    ConstraintLayout playAgainButton;

    LinearLayout linearGameShopping;

    FrameLayout frameGame;


    private int stepCount = 0; // Adım sayacı

    TextView textScoreTopShoppingCartText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_shopping_cart);



        //TANIMLAMALAR
        recyclerViewList = findViewById(R.id.recyclerViewList);
        recyclerViewShelves = findViewById(R.id.recyclerViewShelves);
        btnComplete = findViewById(R.id.btnComplete);
        btnCheck = findViewById(R.id.btnCheck);
        cart = findViewById(R.id.cart);
        paperLayout = findViewById(R.id.paperLayout);
        paperText = findViewById(R.id.paperText);
        playAgainButton = findViewById(R.id.playAgainButton);
        textScoreShoppingCart = findViewById(R.id.textScoreShoppingCart);
        tvTimer = findViewById(R.id.tvTimer); // Initialize TextView for countdown timer
        playAgainButton = findViewById(R.id.playAgainButton);
        linearGameShopping = findViewById(R.id.linearGameShopping);
        frameGame = findViewById(R.id.frameGame);
        textScoreTopShoppingCartText = findViewById(R.id.textScoreTopShoppingCartText);

        //Oluşturmalar
        productList = new ArrayList<>();
        shelfList = new ArrayList<>();
        selectedProducts = new ArrayList<>();
        currentShoppingList = new ArrayList<>();

        init();
        //Metot Aktarmalar
        loadProducts();
        setupRecyclerViews();
        setupDragAndDrop();

        generateShoppingList();
        updateUIForNewLevel();
        loadTopScore();



    }

    private void loadTopScore(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("GameScore").child(userId).child("shopTopScore");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int topScore = snapshot.getValue(Integer.class);
                    textScoreTopShoppingCartText.setText(String.valueOf(topScore));

                }else {
                    textScoreTopShoppingCartText.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    private void saveScoreToFirebase(){
        //Firebase Tanımalamaları ve Bağlanma/UID çekme
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("GameScore").child(userId);

                Map<String, Object> map = new HashMap();
                map.put("shopTopScore",score);

                reference.setValue(map, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError error, DatabaseReference ref) {
                        if (error == null){
                        }else {
                        }
                    }
                });


    }

    private void init(){
        //Tıklama Olayları
        btnComplete.setOnClickListener(v -> {
            paperLayout.setVisibility(View.GONE);
            recyclerViewList.setVisibility(View.GONE);
            btnComplete.setVisibility(View.GONE);

            recyclerViewShelves.setVisibility(View.VISIBLE);
            cart.setVisibility(View.VISIBLE);
            btnCheck.setVisibility(View.VISIBLE);

            // Timer'ı sıfırla ve başlat
            startTimer();
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkShoppingList();

            }
        });

        playAgainButton.setOnClickListener(v -> gameRestart());
    }

    private void gameRestart() {
        finish();
        startActivity(getIntent());
    }

    // Countdown timer methods
    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                Toast.makeText(ShoppingCartActivity.this, R.string.times_up, Toast.LENGTH_SHORT).show();
                playAgainButton.setVisibility(View.VISIBLE);
                tvTimer.setVisibility(View.GONE);
                linearGameShopping.setVisibility(View.GONE);
                paperLayout.setVisibility(View.GONE);
                btnComplete.setVisibility(View.GONE);
                frameGame.setVisibility(View.GONE);
                cart.setVisibility(View.GONE);
                btnCheck.setVisibility(View.GONE);
            }
        }.start();
        timerRunning = true;
    }

    private void resetAndStartTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timeLeftInMillis = 30000; // 30 seconds
        updateCountdownText();
        startTimer();
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timeLeftInMillis = 30000; // 30 seconds
        updateCountdownText();
        timerRunning = false;
    }

    private void updateCountdownText() {
        int seconds = (int) (timeLeftInMillis / 1000);
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", seconds / 60, seconds % 60);
        tvTimer.setText(timeLeftFormatted);
    }

    private void loadProducts() {
        productList.add(new Product(getString(R.string.apple), R.drawable.red_apple_object));
        productList.add(new Product(getString(R.string.banana), R.drawable.banana_orange_object));
        productList.add(new Product(getString(R.string.grapes), R.drawable.grapes_object));
        productList.add(new Product(getString(R.string.mango), R.drawable.banana_mango_object));
        productList.add(new Product(getString(R.string.pineapple), R.drawable.pineapple_object));
        productList.add(new Product(getString(R.string.strawberry), R.drawable.strawberry_object));
        productList.add(new Product(getString(R.string.watermelon), R.drawable.watermelon_64_object));
        productList.add(new Product(getString(R.string.biscuit), R.drawable.cookies_object));
        productList.add(new Product(getString(R.string.drink), R.drawable.tropical_drink_object));
        productList.add(new Product(getString(R.string.rice), R.drawable.rice_object));
        productList.add(new Product(getString(R.string.almond), R.drawable.almond_object));
        productList.add(new Product(getString(R.string.olive_oil), R.drawable.olive_oil_object));
        productList.add(new Product(getString(R.string.pasta), R.drawable.pasta_object));
        productList.add(new Product(getString(R.string.milk), R.drawable.milk_object));
        productList.add(new Product(getString(R.string.chocolate), R.drawable.chocolate_object));
        productList.add(new Product(getString(R.string.peanut), R.drawable.peanut_object));
        productList.add(new Product(getString(R.string.nut), R.drawable.nut_object));
        productList.add(new Product(getString(R.string.beans), R.drawable.beans_object));
        productList.add(new Product(getString(R.string.shampoo), R.drawable.shampoo_object));
        productList.add(new Product(getString(R.string.toothpaste), R.drawable.toothpaste_object));
        productList.add(new Product(getString(R.string.flour), R.drawable.flour_object));
        productList.add(new Product(getString(R.string.cheese), R.drawable.chesee_object));
        productList.add(new Product(getString(R.string.coffee), R.drawable.coffee_object));

        shelfList.addAll(productList);
        Collections.shuffle(shelfList);
    }

    private void setupRecyclerViews() {
        ProductAdapter listAdapter = new ProductAdapter(currentShoppingList, false);
        recyclerViewList.setLayoutManager(new GridLayoutManager(this, 4,GridLayoutManager.VERTICAL, false));
        recyclerViewList.setAdapter(listAdapter);

        int itemCount = 8 + (currentRound - 1); // Rafta gösterilecek ürün sayısı

        // Market rafı için adapter oluştur
        MarketGameAdapter shelfAdapter = new MarketGameAdapter(shelfList.subList(0, itemCount), true);
        recyclerViewShelves.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerViewShelves.setAdapter(shelfAdapter);
    }

    private void setupDragAndDrop() {
        cart.setOnDragListener((v, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DROP:
                    Product product = (Product) event.getLocalState();
                    if (!selectedProducts.contains(product)) {
                        selectedProducts.add(product);
                    }
                    return true;
                default:
                    return true;
            }
        });
    }

    private void generateShoppingList() {
        currentShoppingList.clear();
        Random random = new Random();

        int listSize = 3 + (currentRound - 1); // Alışveriş listesinin boyutunu belirle
        int shelfSize = 8 + (currentRound - 1); // Rafta gösterilen ürün sayısını belirle

        // Alışveriş listesini oluştur
        while (currentShoppingList.size() < listSize) {
            Product randomProduct = shelfList.get(random.nextInt(shelfList.size()));
            if (!currentShoppingList.contains(randomProduct)) {
                currentShoppingList.add(randomProduct);
            }
        }

        // Market rafındaki ürünleri karıştır
        Collections.shuffle(shelfList);

        // Alışveriş listesindeki ürünlerin market rafında görünmesini sağla
        List<Product> displayedProducts = new ArrayList<>();
        displayedProducts.addAll(currentShoppingList);

        while (displayedProducts.size() < shelfSize) {
            Product randomProduct = shelfList.get(random.nextInt(shelfList.size()));
            if (!displayedProducts.contains(randomProduct)) {
                displayedProducts.add(randomProduct);
            }
        }

        // Market rafındaki ürünleri karıştır
        Collections.shuffle(displayedProducts);

        // Market rafı için adapter oluştur
        MarketGameAdapter shelfAdapter = new MarketGameAdapter(displayedProducts, true);
        recyclerViewShelves.setAdapter(shelfAdapter);
    }

    private void updateUIForNewLevel() {
        paperLayout.setVisibility(View.VISIBLE);
        recyclerViewList.setVisibility(View.VISIBLE);
        btnComplete.setVisibility(View.VISIBLE);

        recyclerViewShelves.setVisibility(View.GONE);
        cart.setVisibility(View.GONE);
        btnCheck.setVisibility(View.GONE);

        ProductAdapter listAdapter = new ProductAdapter(currentShoppingList, false);
        recyclerViewList.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

    private void checkShoppingList() {
        if (selectedProducts.containsAll(currentShoppingList) && selectedProducts.size() == currentShoppingList.size()) {
            Toast.makeText(ShoppingCartActivity.this, R.string.great_game, Toast.LENGTH_SHORT).show();
            score += 3;
            textScoreShoppingCart.setText(String.valueOf(score));
            resetTimer();

            //Firebase Tanımalamaları ve Bağlanma/UID çekme
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String userId = user.getUid();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("GameScore").child(userId);
            //Firebase'den veri çek önceki skor bu skordan küçükse eklesin.

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (!snapshot.exists()){
                        saveScoreToFirebase();
                    }else {
                        int score1 = snapshot.child("shopTopScore").getValue(Integer.class);

                        if (score>score1) {
                            saveScoreToFirebase();
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });


            stepCount++;

            // Eğer 3 adım tamamlandıysa, gösterilen ürün sayısını artır
            if (stepCount % 3 == 0) {
                currentRound++;
            }

            advanceToNextLevel();
        } else {
            Toast.makeText(ShoppingCartActivity.this, "Yanlış! Sepette eksik bir şeyler olabilir.", Toast.LENGTH_SHORT).show();
        }
    }

    private void advanceToNextLevel() {
        currentLevel++;
        if (currentLevel > 3) {
            currentLevel = 1;
        }

        selectedProducts.clear();
        generateShoppingList();
        updateUIForNewLevel();
    }


}
