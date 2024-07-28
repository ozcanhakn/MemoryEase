package app.forget.forgetfulnessapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import app.forget.forgetfulnessapp.Interface.ItemClickListener;
import app.forget.forgetfulnessapp.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView categoryName;
    private ItemClickListener itemClickListener;
    public ImageView imageView;
    public CardView cardView;


    public CategoryViewHolder(View itemView) {
        super(itemView);

        categoryName = itemView.findViewById(R.id.nameCategory);
        imageView = itemView.findViewById(R.id.imageViewCategory);
        cardView = itemView.findViewById(R.id.cardCircleCategory);

        itemView.setOnClickListener(this);


    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @Override
    public void onClick(View view) {
        if (itemClickListener != null){
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }
    }

}
