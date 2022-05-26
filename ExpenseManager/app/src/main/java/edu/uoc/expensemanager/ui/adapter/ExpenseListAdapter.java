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
import edu.uoc.expensemanager.ui.TripViewActivity;


public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.ViewHolder>{
    private ArrayList<ExpenseInfo> listdata;
    private TripViewActivity activity;
    ArrayList<UserInfo> users;
    String tripID;
    // RecyclerView recyclerView;
    public ExpenseListAdapter(ArrayList<ExpenseInfo> listData, TripViewActivity activity, ArrayList<UserInfo> users, String tripID) {
        this.listdata = listData;
        this.users = users;
        this.activity = activity;
        this.tripID = tripID;

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
        final ExpenseInfo expenseData = listdata.get(position);
        holder.textView_Desc.setText(listdata.get(position).description);
        holder.textView_Date.setText(listdata.get(position).date);
        holder.textView_Amount.setText(""+listdata.get(position).totalAmount + " €");

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(activity, ExpenseActivity.class);
                k.putExtra("Description",expenseData.description);
                k.putExtra("Date",expenseData.date);
                k.putExtra("Amount",expenseData.totalAmount);
                k.putExtra("Payers", expenseData.payers);
                k.putExtra("Users", users);
                k.putExtra("TripID", tripID);
                k.putExtra("ExpenseID", expenseData.expenseID);


                activity.mStartForResult.launch(k);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView_Desc;
        public TextView textView_Date;
        public TextView textView_Amount;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.userAvatar);
            this.textView_Desc = (TextView) itemView.findViewById(R.id.textView_description);
            this.textView_Date = (TextView) itemView.findViewById(R.id.textView_date);
            this.textView_Amount = (TextView) itemView.findViewById(R.id.textView_amount);

            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}
