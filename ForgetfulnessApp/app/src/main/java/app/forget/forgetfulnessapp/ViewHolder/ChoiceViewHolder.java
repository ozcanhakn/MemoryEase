package app.forget.forgetfulnessapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import app.forget.forgetfulnessapp.Interface.ItemClickListener;
import app.forget.forgetfulnessapp.R;

public class ChoiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView soundName,min,type;
    private ItemClickListener itemClickListener;
    public ImageView imageView;
    public CardView cardView;


    public ChoiceViewHolder(View itemView) {
        super(itemView);

        soundName = itemView.findViewById(R.id.textSoundName);
        min = itemView.findViewById(R.id.textSoundMin);
        type = itemView.findViewById(R.id.textSoundType);
        imageView = itemView.findViewById(R.id.imageViewSounds);
        cardView = itemView.findViewById(R.id.cardChoiceItem);

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
