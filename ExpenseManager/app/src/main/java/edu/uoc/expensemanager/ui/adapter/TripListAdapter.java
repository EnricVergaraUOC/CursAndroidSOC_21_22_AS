package edu.uoc.expensemanager.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.URL;

import edu.uoc.expensemanager.R;
import edu.uoc.expensemanager.Utilities.DownLoadImageTask;
import edu.uoc.expensemanager.model.TripInfo;
import edu.uoc.expensemanager.ui.TripViewActivity;


public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.ViewHolder>{
    private TripInfo[] listdata;
    private Context activityContext;
    // RecyclerView recyclerView;
    public TripListAdapter(TripInfo[] listdata, Context context) {
        this.listdata = listdata;
        this.activityContext = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item_trip, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final TripInfo myListData = listdata[position];
        holder.textView_Desc.setText(listdata[position].decription);
        holder.textView_Date.setText(listdata[position].date);
        if (listdata[position].image_url.compareTo("") == 0){
            holder.imageView.setImageResource(R.drawable.trip);
        }else{
            //Uri uri = Uri.parse(listdata[position].image_url);
            //holder.imageView.setImageURI(uri);
            new DownLoadImageTask(holder.imageView).execute(listdata[position].image_url);
        }
        //holder.imageView.setImageResource(listdata[position].());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(activityContext, TripViewActivity.class);
                k.putExtra("Description",myListData.decription);
                k.putExtra("Date",myListData.date);


                activityContext.startActivity(k);

                //Toast.makeText(view.getContext(),"click on item: "+myListData.decription,Toast.LENGTH_LONG).show();
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
        public Button btn_Amount;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textView_Desc = (TextView) itemView.findViewById(R.id.textView_description);
            this.textView_Date = (TextView) itemView.findViewById(R.id.textView_date);
            this.btn_Amount = (Button) itemView.findViewById(R.id.btn_amount);

            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }


}
