package edu.uoc.expensemanager.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

import edu.uoc.expensemanager.R;
import edu.uoc.expensemanager.model.PayerInfo;
import edu.uoc.expensemanager.ui.ExpenseActivity;
import edu.uoc.expensemanager.ui.LoginActivity;
import edu.uoc.expensemanager.ui.TripListActivity;


public class PayerListAdapter extends RecyclerView.Adapter<PayerListAdapter.ViewHolder>{
    private ArrayList<PayerInfo> listdata;
    private ExpenseActivity activity;

    public PayerListAdapter(ArrayList<PayerInfo> payers, ExpenseActivity activity) {
        this.listdata = payers;
        this.activity = activity;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item_payer, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PayerInfo myListData = listdata.get(position);
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

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Amount for " +  myListData.name + ":");
                View viewInflated = LayoutInflater.from(activity).inflate(R.layout.amount_input, (ViewGroup) null, false);
                final EditText input = (EditText) viewInflated.findViewById(R.id.input);
                input.setText(""+myListData.amount);
                builder.setView(viewInflated);

                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String str = input.getText().toString();
                        try{
                            int number = Integer.parseInt(str);
                            System.out.println(number);
                            myListData.amount = number;
                            int pos = holder.getAdapterPosition();
                            PayerListAdapter.this.notifyItemChanged(pos);
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

                new AlertDialog.Builder(activity)
                        .setTitle("Do you really want to delete the payer " +  myListData.name + "?")
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


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView_Desc;
        public TextView textView_Date;
        public Button btn_amount;
        public Button btn_delete;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textView_Desc = (TextView) itemView.findViewById(R.id.textView_description);
            this.textView_Date = (TextView) itemView.findViewById(R.id.textView_date);
            this.btn_amount = (Button) itemView.findViewById(R.id.btn_amount);
            this.btn_delete = (Button) itemView.findViewById(R.id.btn_delete);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }

    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }
}
