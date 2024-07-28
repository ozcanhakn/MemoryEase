package app.forget.forgetfulnessapp.ViewHolder;

import android.content.ClipData;
import android.content.ClipDescription;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import app.forget.forgetfulnessapp.Interface.ItemClickListener;
import app.forget.forgetfulnessapp.Model.Product;
import app.forget.forgetfulnessapp.R;
import io.reactivex.rxjava3.annotations.NonNull;

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.GamesViewHolder>  {
    private List<Product> productList;
    private boolean isDraggable;

    public GamesAdapter(List<Product> productList, boolean isDraggable) {
        this.productList = productList;
        this.isDraggable = isDraggable;
    }
    @NonNull
    @Override
    public GamesAdapter.GamesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_recycler_item, parent, false);
        return new GamesAdapter.GamesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GamesAdapter.GamesViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }




    class GamesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView productImage;
        private TextView productName;
        private ItemClickListener itemClickListener;


        public GamesViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.imageViewGameRecycler);
            productName = itemView.findViewById(R.id.textGameOfNameRecycler);
        }

        public void bind(Product product) {
            productName.setText(product.getName());
            Glide.with(itemView.getContext()).load(product.getImageResId()).into(productImage);

            if (isDraggable) {
                itemView.setOnLongClickListener(v -> {
                    ClipData.Item item = new ClipData.Item(product.getName());
                    ClipData dragData = new ClipData(product.getName(), new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(itemView);

                    itemView.startDragAndDrop(dragData, shadowBuilder, product, 0);
                    return true;
                });
            }
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }


        @Override
        public void onClick(View v) {
            if (itemClickListener != null){
                itemClickListener.onClick(v,getAdapterPosition(),false);
            }
        }
    }
}