package edu.uoc.expensemanager.ui.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.uoc.expensemanager.R;
import edu.uoc.expensemanager.Utilities.DownLoadImageTask;
import edu.uoc.expensemanager.model.PayerInfo;
import edu.uoc.expensemanager.ui.ExpenseActivity;


public class PayerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<PayerInfo> listdata;
    private ExpenseActivity activity;

    public PayerListAdapter(ArrayList<PayerInfo> payers, ExpenseActivity activity) {
        this.listdata = payers;
        this.activity = activity;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0: {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View listItem= layoutInflater.inflate(R.layout.list_item_empty, parent, false);

                return new ViewHolderEmpty(listItem);
            }
            case 1:
            default:
            {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View listItem= layoutInflater.inflate(R.layout.list_item_payer, parent, false);

                return new ViewHolderGeneral(listItem);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (listdata.size() == 0){
            return 0;
        }
        return 1;
    }
    @Override
    public int getItemCount() {
        if (listdata.size() == 0){
            return 1;
        }
        return listdata.size();
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder_, int position) {

        switch (holder_.getItemViewType()) {
            case 0: {
                ViewHolderEmpty holder = (ViewHolderEmpty)holder_;
                holder.textView_Info.setText("The list of payers is empty, to fill it use the button 'Add Payer' ");
            }
            break;
            case 1: {
                ViewHolderGeneral holder = (ViewHolderGeneral)holder_;
                //String image, String name, String email, int amount){


                holder.textView_Desc.setText(listdata.get(position).name);
                holder.textView_Date.setText(listdata.get(position).email + " " + listdata.get(position).amount);
                if (listdata.get(position).image_url.compareTo("") == 0){
                    holder.imageView.setImageResource(R.drawable.trip);
                }else{
                    new DownLoadImageTask(holder.imageView).execute(listdata.get(position).image_url);
                }
                holder.btn_amount.setText(""+listdata.get(position).amount + " €");
                holder.btn_amount.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        int pos = holder.getAdapterPosition();
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle("Amount for " +  listdata.get(pos).name + ":");
                        View viewInflated = LayoutInflater.from(activity).inflate(R.layout.amount_input, (ViewGroup) null, false);
                        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
                        input.setText(""+listdata.get(pos).amount);
                        builder.setView(viewInflated);

                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int pos = holder.getAdapterPosition();
                                dialog.dismiss();
                                String str = input.getText().toString();
                                try{
                                    int number = Integer.parseInt(str);
                                    System.out.println(number);
                                    listdata.get(pos).amount = number;
                                    PayerListAdapter.this.notifyItemChanged(pos);
                                    activity.updateLabelWarning();
                                }
                                catch (NumberFormatException ex){
                                    ex.printStackTrace();
                                }
                            }
                        });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();
                    }
                });


                holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        new AlertDialog.Builder(activity)
                                .setTitle("Do you really want to delete the payer " +  listdata.get(pos).name + "?")
                                .setMessage("")

                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Continue with delete operation
                                        int pos = holder.getAdapterPosition();
                                        listdata.remove(pos);
                                        notifyItemRemoved(pos);
                                        activity.updateLabelWarning();
                                    }
                                })

                                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });


                holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(view.getContext(),"click on item: "+myListData.name,Toast.LENGTH_LONG).show();
                        //listdata[holder.getAdapterPosition()].setDescription("KAKAK");
                        //notifyItemChanged(holder.getAdapterPosition());
                    }
                });
            }
        }

    }




    public static class ViewHolderGeneral extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView_Desc;
        public TextView textView_Date;
        public Button btn_amount;
        public Button btn_delete;
        public RelativeLayout relativeLayout;
        public ViewHolderGeneral(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.userAvatar);
            this.textView_Desc = (TextView) itemView.findViewById(R.id.textView_description);
            this.textView_Date = (TextView) itemView.findViewById(R.id.textView_date);
            this.btn_amount = (Button) itemView.findViewById(R.id.btn_amount);
            this.btn_delete = (Button) itemView.findViewById(R.id.btn_delete);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }

    public static class ViewHolderEmpty extends RecyclerView.ViewHolder {
        public TextView textView_Info;
        public ViewHolderEmpty(View itemView) {
            super(itemView);
            this.textView_Info = (TextView) itemView.findViewById(R.id.text_info);

        }
    }


}
