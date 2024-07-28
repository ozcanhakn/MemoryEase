package app.forget.forgetfulnessapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import app.forget.forgetfulnessapp.Interface.ItemClickListener;
import app.forget.forgetfulnessapp.R;

public class RelatedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView soundName,min,type;
    private ItemClickListener itemClickListener;
    public ImageView imageView;
    public CardView cardView;


    public RelatedViewHolder(View itemView) {
        super(itemView);

        soundName = itemView.findViewById(R.id.textSoundName1);
        min = itemView.findViewById(R.id.textSoundMin1);
        type = itemView.findViewById(R.id.textSoundType1);
        imageView = itemView.findViewById(R.id.imageViewSounds1);
        cardView = itemView.findViewById(R.id.cardChoiceItem1);

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