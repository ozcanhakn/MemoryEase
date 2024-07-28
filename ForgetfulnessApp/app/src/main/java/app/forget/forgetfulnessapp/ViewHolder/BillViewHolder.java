package app.forget.forgetfulnessapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import app.forget.forgetfulnessapp.Interface.ItemClickListener;
import app.forget.forgetfulnessapp.R;

public class BillViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

    public TextView billDate, billType, billPayType,billAmount;
    private ItemClickListener itemClickListener;
    public ImageView deleteBill;


    public BillViewHolder(View itemView) {
        super(itemView);

        billDate = itemView.findViewById(R.id.textBillPayDate);
        billAmount = itemView.findViewById(R.id.textBillAmount);
        billPayType = itemView.findViewById(R.id.textBillPayType);
        billType = itemView.findViewById(R.id.textBillType);
        deleteBill = itemView.findViewById(R.id.deleteBill);

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
