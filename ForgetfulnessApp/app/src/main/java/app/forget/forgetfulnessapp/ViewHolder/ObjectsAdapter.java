package app.forget.forgetfulnessapp.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import app.forget.forgetfulnessapp.R;

public class ObjectsAdapter extends RecyclerView.Adapter<ObjectsAdapter.ObjectViewHolder> {

    private Context context;
    private int level;
    private OnObjectClickListener listener;
    private int[] images = {
            R.drawable.object_fball, R.drawable.object_moon, R.drawable.object_sun,
            R.drawable.object_cupcake, R.drawable.object_bball,R.drawable.object_sunflower,
            R.drawable.object_blossom,R.drawable.object_drink,R.drawable.object_rose,R.drawable.object_tball,
            R.drawable.object_tiger,R.drawable.object_monkey,R.drawable.object_cat,
            R.drawable.object_lion,R.drawable.object_hot_dog,R.drawable.object_fish,
            R.drawable.object_dog,R.drawable.object_balloon,R.drawable.object_planet,
            R.drawable.object_pineapple,R.drawable.object_palm_tree,R.drawable.object_cherryblossom,
            R.drawable.object_apple, R.drawable.object_unicorn, R.drawable.object_star,
            R.drawable.object_rabbit, R.drawable.object_magic_wand, R.drawable.object_top_hat,
            R.drawable.object_banana,R.drawable.object_watermelon
    };
    private List<Integer> objectIds;

    public ObjectsAdapter(Context context, int level, OnObjectClickListener listener) {
        this.context = context;
        this.level = level;
        this.listener = listener;
        generateObjectIds();
    }

    public void setLevel(int level) {
        this.level = level;
        generateObjectIds();
        notifyDataSetChanged();
    }

    private void generateObjectIds() {
        objectIds = new ArrayList<>();
        int numObjects = Math.min(level, 9); // Max 8 objects
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < images.length; i++) {
            indices.add(i);
        }
        Collections.shuffle(indices);

        for (int i = 0; i < numObjects; i++) {
            objectIds.add(indices.get(i));
        }
    }

    @NonNull
    @Override
    public ObjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.objects_item, parent, false);
        return new ObjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObjectViewHolder holder, int position) {
        int imageResId = images[objectIds.get(position)];
        holder.imageView.setImageResource(imageResId);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onObjectClick("image_" + imageResId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return objectIds.size();
    }

    public class ObjectViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ObjectViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewObject);
        }
    }

    public interface OnObjectClickListener {
        void onObjectClick(String objectId);
    }
}