package app.forget.forgetfulnessapp.Game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import app.forget.forgetfulnessapp.Manager.RandomLayoutManager;
import app.forget.forgetfulnessapp.R;
import app.forget.forgetfulnessapp.ViewHolder.ObjectsAdapter;

public class ObjectSequenceActivity extends AppCompatActivity {

    private TextView textViewScore,textObjectSequenceTopScore;
    private RecyclerView recyclerViewObjects;
    private ObjectsAdapter objectsAdapter;
    private List<String> previousSelections;
    private int level;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_object_sequence);

        textViewScore = findViewById(R.id.textViewScore);
        recyclerViewObjects = findViewById(R.id.recyclerViewObjects);
        textObjectSequenceTopScore = findViewById(R.id.textObjectSequenceTopScore);

        previousSelections = new ArrayList<>();
        level = 3;  // Başlangıç seviyesi
        score = 0;

        objectsAdapter = new ObjectsAdapter(this, level, new ObjectsAdapter.OnObjectClickListener() {
            @Override
            public void onObjectClick(String objectId) {
                if (!previousSelections.contains(objectId)) {
                    score += 3;
                    textViewScore.setText("Skor: " + score);
                    previousSelections.add(objectId);
                    level++;
                    if (level > 50) {
                        level = 50; // Maksimum 50 seviye
                    }
                    objectsAdapter.setLevel(level);
                } else {
                    // Oyun bitti
                    Toast.makeText(ObjectSequenceActivity.this, R.string.game_over_score+ score, Toast.LENGTH_LONG).show();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String userId = user.getUid();

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("GameScore").child(userId).child("objectTopScore");

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NotNull DataSnapshot snapshot) {
                            if (!snapshot.exists()){
                                saveScoreToDatabase();
                            }else {
                                int score1 = snapshot.child("objectTopScore").getValue(Integer.class);

                                if (score>score1) {
                                    saveScoreToDatabase();
                                    }
                                }
                            }
                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });
                    resetGame();
                }
            }
        });

        recyclerViewObjects.setLayoutManager(new RandomLayoutManager(this));
        recyclerViewObjects.setAdapter(objectsAdapter);

        loadTopScore();

    }

    private void resetGame() {
        previousSelections.clear();
        level = 3;
        score = 0;
        textViewScore.setText("Skor: " + score);
        objectsAdapter.setLevel(level);
    }

    private void saveScoreToDatabase(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("GameScore").child(userId);



        Map<String, Object> map = new HashMap();
        map.put("objectTopScore",score);

        reference.setValue(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                if (error == null){
                }else {
                }
            }
        });
    }

    private void loadTopScore(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("GameScore").child(userId).child("objectTopScore");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int topScore = snapshot.getValue(Integer.class);
                    textObjectSequenceTopScore.setText(String.valueOf(topScore));
                }else {
                    textObjectSequenceTopScore.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }


}