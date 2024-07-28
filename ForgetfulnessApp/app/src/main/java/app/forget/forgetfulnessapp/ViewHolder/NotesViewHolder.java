package app.forget.forgetfulnessapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import app.forget.forgetfulnessapp.Interface.ItemClickListener;
import app.forget.forgetfulnessapp.R;

public class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView dates, notes, lessButton,moreButton;
    private ItemClickListener itemClickListener;
    public ImageView viewMore,deleteButton,editButton;
    public TextView header;

    public NotesViewHolder(View itemView) {
        super(itemView);


        dates = itemView.findViewById(R.id.itemDates);
        notes = itemView.findViewById(R.id.itemNotes);
        header = itemView.findViewById(R.id.itemHeader);
        viewMore = itemView.findViewById(R.id.itemViewMore);
        deleteButton = itemView.findViewById(R.id.deleteTrashButton);
        editButton = itemView.findViewById(R.id.editPencilButton);
        lessButton = itemView.findViewById(R.id.textLessButton);
        moreButton = itemView.findViewById(R.id.textMoreButton);




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