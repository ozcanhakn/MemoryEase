package app.forget.forgetfulnessapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import app.forget.forgetfulnessapp.Interface.ItemClickListener;
import app.forget.forgetfulnessapp.R;

public class MeditationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView field;
    private ItemClickListener itemClickListener;
    public RoundedImageView imageView;
    public CardView cardView;


    public MeditationViewHolder(View itemView) {
        super(itemView);

        field = itemView.findViewById(R.id.textMeditationFocusFirstRecycler);
        imageView = itemView.findViewById(R.id.imageViewMeditationFocusFirstRecycler);
        cardView = itemView.findViewById(R.id.cardViewMeditation);

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
