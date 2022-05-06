package edu.uoc.expensemanager.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.uoc.expensemanager.R;
import edu.uoc.expensemanager.model.ExpenseInfo;
import edu.uoc.expensemanager.model.UserInfo;
import edu.uoc.expensemanager.ui.ExpenseActivity;


public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.ViewHolder>{
    private ExpenseInfo[] listdata;
    private Context activityContext;
    ArrayList<UserInfo> users;
    // RecyclerView recyclerView;
    public ExpenseListAdapter(ExpenseInfo[] listData, Context context, ArrayList<UserInfo> users) {
        this.listdata = listData;
        this.users = users;
        this.activityContext = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item_expense, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ExpenseInfo myListData = listdata[position];
        holder.textView_Desc.setText(listdata[position].description);
        holder.textView_Date.setText(listdata[position].date);
        holder.textView_Amount.setText(""+listdata[position].totalAmount + " €");
        //holder.imageView.setImageResource(listdata[position].());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(activityContext, ExpenseActivity.class);
                k.putExtra("Description",myListData.description);
                k.putExtra("Date",myListData.date);
                k.putExtra("Amount",myListData.totalAmount);
                k.putExtra("Users", users);
                activityContext.startActivity(k);

                //Toast.makeText(view.getContext(),"click on item: "+myListData.description,Toast.LENGTH_LONG).show();
                //listdata[holder.getAdapterPosition()].setDescription("KAKAK");
                //notifyItemChanged(holder.getAdapterPosition());
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView_Desc;
        public TextView textView_Date;
        public TextView textView_Amount;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textView_Desc = (TextView) itemView.findViewById(R.id.textView_description);
            this.textView_Date = (TextView) itemView.findViewById(R.id.textView_date);
            this.textView_Amount = (TextView) itemView.findViewById(R.id.textView_amount);

            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}
