package app.forget.forgetfulnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import app.forget.forgetfulnessapp.Advice.MeditationActivity;
import app.forget.forgetfulnessapp.Model.Category;
import app.forget.forgetfulnessapp.Model.Choice;
import app.forget.forgetfulnessapp.Model.Meditation;
import app.forget.forgetfulnessapp.ViewHolder.CategoryViewHolder;
import app.forget.forgetfulnessapp.ViewHolder.ChoiceViewHolder;
import app.forget.forgetfulnessapp.ViewHolder.MeditationViewHolder;

public class MeditationChoiceActivity extends AppCompatActivity {
    ImageView imageViewBackground;
    ImageView imageBanner;
    TextView textHeader,textDesc1,textDesc2;
    TextView bannerHeader,bannerDesc,bannerDesc3;
    RelativeLayout buttonStart;
    String id = "";
    RecyclerView recSleep;
    FirebaseRecyclerAdapter<Choice, ChoiceViewHolder> adapter;
    RecyclerView.LayoutManager layoutManager;

    RecyclerView recCategory;
    FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapterCategory;
    RecyclerView.LayoutManager layoutManagerCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_meditation_choice);

        imageViewBackground = findViewById(R.id.imageViewBackground);
        imageBanner = findViewById(R.id.bannerMeditation);
        textHeader = findViewById(R.id.textCategoryMeditation);
        textDesc1 = findViewById(R.id.textCategoryMeditationDesc1);
        textDesc2 = findViewById(R.id.textCategoryMeditationDesc2);
        bannerDesc = findViewById(R.id.bannerDesc);
        bannerHeader = findViewById(R.id.textBannerHeader);
        bannerDesc3 = findViewById(R.id.bannerDesc1);
        buttonStart = findViewById(R.id.buttonStart);


        recSleep = findViewById(R.id.recSleep);
        recSleep.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this,2,RecyclerView.VERTICAL,false);
        recSleep.setLayoutManager(layoutManager);//layoutManager


        recCategory = findViewById(R.id.recCategories);
        recCategory.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recCategory.setLayoutManager(linearLayoutManager);
        recCategory.setItemAnimator(new DefaultItemAnimator());

        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference reference1 = database1.getReference("Categories");

        FirebaseRecyclerOptions<Category> options1 = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(reference1, Category.class)
                .build();

        adapterCategory = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(options1) {
            @Override
            protected void onBindViewHolder(CategoryViewHolder viewHolder, int position, Category model) {
                Picasso.get().load(model.getPhoto()).into(viewHolder.imageView);
                viewHolder.categoryName.setText(model.getField());

                viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MeditationChoiceActivity.this,MeditationChoiceActivity.class);
                        intent.putExtra( "choice",model.getField());
                        startActivity(intent);
                    }
                });


            }



            @Override
            public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.categories_circle_item, parent, false);
                return new CategoryViewHolder(itemView);
            }
        };
        adapterCategory.startListening();
        recCategory.setAdapter(adapterCategory);


        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("choice");
            Log.d("IDMED", "onCreate: "+id);
            if (id != null && !id.isEmpty()) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("Background").child(id);

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        String url = snapshot.child(id).getValue(String.class);
                        String banner = snapshot.child("banner").getValue(String.class);
                        String header1 = snapshot.child("header").getValue(String.class);
                        String desc = snapshot.child("desc1").getValue(String.class);
                        String desc1 = snapshot.child("desc2").getValue(String.class);
                        String bannerHeader1 = snapshot.child("bannerTitle").getValue(String.class);
                        String bannerDesc1  = snapshot.child("bannerDesc").getValue(String.class);
                        String bannerDesc2  = snapshot.child("bannerDesc1").getValue(String.class);



                        if (url != null) {
                            Picasso.get().load(url).into(imageViewBackground);
                        }

                        Picasso.get().load(banner).into(imageBanner);
                        textDesc1.setText(desc);
                        textDesc2.setText(desc1);
                        textHeader.setText(header1);
                        bannerDesc.setText(bannerDesc1);
                        bannerHeader.setText(bannerHeader1);
                        bannerDesc3.setText(bannerDesc2);


                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Handle onCancelled
                    }
                });
            }
        }

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MeditationChoiceActivity.this,SoundMeditationActivity.class);
                intent1.putExtra("mixSound","mixSound");
                intent1.putExtra("key","01");
                intent1.putExtra("category",id);
                intent1.putExtra("id",id);


                startActivity(intent1);

            }
        });


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(id);

        FirebaseRecyclerOptions<Choice> options = new FirebaseRecyclerOptions.Builder<Choice>()
                .setQuery(reference, Choice.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Choice, ChoiceViewHolder>(options) {
            @Override
            protected void onBindViewHolder(ChoiceViewHolder viewHolder, int position, Choice model) {

                Picasso.get().load(model.getPhoto()).into(viewHolder.imageView);
                viewHolder.min.setText(String.valueOf(model.getMin()));
                viewHolder.type.setText(model.getType());
                viewHolder.soundName.setText(model.getField());

                String categry = model.getForcat();

                viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MeditationChoiceActivity.this,SoundMeditationActivity.class);
                        intent.putExtra("key",adapter.getRef(position).getKey());
                        intent.putExtra("category",categry);
                        intent.putExtra("id",id);
                        startActivity(intent);
                    }
                });

            }



            @Override
            public ChoiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.choice_meditation_selected, parent, false);
                return new ChoiceViewHolder(itemView);
            }
        };
        adapter.startListening();
        recSleep.setAdapter(adapter);
    }
}