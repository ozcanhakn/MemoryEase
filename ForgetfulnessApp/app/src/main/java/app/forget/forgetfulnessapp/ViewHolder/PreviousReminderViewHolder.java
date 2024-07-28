package app.forget.forgetfulnessapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import app.forget.forgetfulnessapp.Interface.ItemClickListener;
import app.forget.forgetfulnessapp.R;

public class PreviousReminderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView title, description;
    public ImageView editButton,deleteButton;
    public CardView cardViewPrevious;
    public TextView textviewTime;
    public TextView textMonday,textSaturday,textFriday,textSunday,textTuesday,textThursday,textWednesday;
    public TextView textTimeZone,textStatus;
    private ItemClickListener itemClickListener;
    public LinearLayout linearLayoutDayOfWeek;

    public PreviousReminderViewHolder(View itemView) {
        super(itemView);


        title = itemView.findViewById(R.id.previousItemTitle);
        cardViewPrevious = itemView.findViewById(R.id.cardViewPrevious);
        textviewTime = itemView.findViewById(R.id.textviewTime);
        textMonday = itemView.findViewById(R.id.textMonday);
        textSaturday = itemView.findViewById(R.id.textSaturday);
        textFriday = itemView.findViewById(R.id.textFriday);
        textSunday = itemView.findViewById(R.id.textSunday);
        textTuesday = itemView.findViewById(R.id.textTuesday);
        textThursday = itemView.findViewById(R.id.textThursday);
        textWednesday = itemView.findViewById(R.id.textWednesday);
        textTimeZone = itemView.findViewById(R.id.textTimeZone);
        editButton = itemView.findViewById(R.id.editPencilButtonPrevious);
        deleteButton = itemView.findViewById(R.id.deleteReminder);
        linearLayoutDayOfWeek = itemView.findViewById(R.id.linearLayoutDayOfWeek);
        textStatus = itemView.findViewById(R.id.textStatus);



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
