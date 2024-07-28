package app.forget.forgetfulnessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;

public class UpgradeForProActivity extends AppCompatActivity implements PurchasesUpdatedListener {
    RelativeLayout goTop;
    ImageView goBack;

    ScrollView scrollView;
    private BillingClient billingClient;
    private Map<String, SkuDetails> skuDetailsMap = new HashMap<>();

    RelativeLayout oneTimeSubs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_upgradefor_pro);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String currentId = user.getUid();


        TextView textView = findViewById(R.id.textBestDeal);
        animateTextView(textView);

        goTop = findViewById(R.id.goTop);
        goBack = findViewById(R.id.backButton);

        oneTimeSubs = findViewById(R.id.oneTimeSubs);

        scrollView = findViewById(R.id.scrollview2);


        oneTimeSubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPurchaseFlow("onetimeplan1");

            }
        });


        setupBillingClient();



        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpgradeForProActivity.this,HomeScreen.class);
                startActivity(intent);
            }
        });
        goTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mevcut ekranın durumunu kontrol et
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }


    private void animateTextView(TextView textView) {
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(textView, "scaleX", 1.0f, 1.2f);
        scaleXAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        scaleXAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        scaleXAnimator.setDuration(600);

        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(textView, "scaleY", 1.0f, 1.2f);
        scaleYAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        scaleYAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        scaleYAnimator.setDuration(600);

        scaleXAnimator.start();
        scaleYAnimator.start();
    }

    private void setupBillingClient() {
        billingClient = BillingClient.newBuilder(this)
                .setListener(this)
                .enablePendingPurchases()
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    querySkuDetails();

                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
    }

    private void querySkuDetails() {
        List<String> skuList = Arrays.asList("onetimeplan1");
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);

        billingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(@NonNull BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                    for (SkuDetails skuDetails : skuDetailsList) {
                        skuDetailsMap.put(skuDetails.getSku(), skuDetails);
                    }
                }
            }
        });
    }






    private void startPurchaseFlow(String skuId) {
        SkuDetails skuDetails = skuDetailsMap.get(skuId);
        if (skuDetails != null) {
            Log.d("PurchaseFlow", "Starting purchase flow for SKU: " + skuId);
            BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(skuDetails)
                    .build();
            billingClient.launchBillingFlow(this, billingFlowParams);
        } else {
            Log.e("PurchaseFlow", "SkuDetails is null for SKU: " + skuId);
        }
    }


    @Override
    public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (com.android.billingclient.api.Purchase purchase : purchases) {
                handlePurchase(purchase);

            }
        }
    }

    private void handlePurchase(Purchase purchase) {
        // Handle the purchase here (e.g., grant the user access to the purchased item)
        if (!purchase.isAcknowledged()) {
            // Acknowledge the purchase
            acknowledgePurchase(purchase.getPurchaseToken());

        }

        grantAccessToProduct(purchase);




    }







    private void acknowledgePurchase(String purchaseToken) {
        AcknowledgePurchaseParams acknowledgePurchaseParams =
                AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchaseToken)
                        .build();
        billingClient.acknowledgePurchase(acknowledgePurchaseParams, new AcknowledgePurchaseResponseListener() {
            @Override
            public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // Purchase acknowledged successfully
                    Log.d("PRO4", "startPurchaseFlow: ");




                    Toast.makeText(UpgradeForProActivity.this, "Satın alım başarılı", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void grantAccessToProduct(Purchase purchase) {
        ArrayList<String> skuList = purchase.getSkus();
        for (String sku : skuList) {
            switch (sku) {
                case "onetimeplan1":
                    grantAccessToOneTimePlan();
                    break;
                default:
                    // Handle unknown product
            }
        }
    }


    private void grantAccessToOneTimePlan() {
        Log.d("ABONE1", "grantAccessToOneTimePlan: metot çalıştı");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Premium").child(currentUserId);
        Log.d("ABONE1", "grantAccessToOneTimePlan: "+currentUserId);

        // Abonelik başlangıç tarihini al (örneğin, şu anki tarih)
        String subsDate = getCurrentDate();

        // Abonelik değerini belirle (örneğin, "premium")
        String subsValue = "one time";

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("ABONE1", "grantAccessToOneTimePlan: değişti");

                   reference.child("subsDate").setValue(subsDate);
                    reference.child("subsValue").setValue(subsValue);
                    reference.child("premium").setValue("yes");

                    Toast.makeText(UpgradeForProActivity.this, "Abonelik eklendi", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


    }

    private String getCurrentDate() {
        // Şu anki tarihi al ve belirli bir formata dönüştür
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

}