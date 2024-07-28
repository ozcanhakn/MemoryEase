package app.forget.forgetfulnessapp.ViewHolder;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;

import app.forget.forgetfulnessapp.Interface.ItemClickListener;
import app.forget.forgetfulnessapp.R;

public class ReminderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView title,description;
    public ImageView imageView;
    public CardView cardViewMyLastReminder;
    public RelativeLayout completeButton;
    public TextView completeStatus;
    private ItemClickListener itemClickListener;

    public ImageView voiceButton, pencilButton;

    public ReminderViewHolder(View itemView) {
        super(itemView);


        title = itemView.findViewById(R.id.textTitle);
        description = itemView.findViewById(R.id.desc);
        imageView = itemView.findViewById(R.id.imageVieww);
        cardViewMyLastReminder = itemView.findViewById(R.id.cardViewMyLastReminder);
        completeButton = itemView.findViewById(R.id.completeButton);
        completeStatus = itemView.findViewById(R.id.textCompleteStatus);

        voiceButton = itemView.findViewById(R.id.iconVoiceSpeech);
        pencilButton = itemView.findViewById(R.id.iconEditPencil);

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