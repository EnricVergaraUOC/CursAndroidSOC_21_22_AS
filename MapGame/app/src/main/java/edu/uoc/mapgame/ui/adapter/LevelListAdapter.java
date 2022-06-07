package edu.uoc.mapgame.ui.adapter;

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

import edu.uoc.mapgame.R;
import edu.uoc.mapgame.model.Level;
import edu.uoc.mapgame.ui.MapsActivity;

public class LevelListAdapter extends RecyclerView.Adapter<LevelListAdapter.ViewHolder>{
    private ArrayList<Level> listData;
    private Context activityContext;
    // RecyclerView recyclerView;
    public LevelListAdapter(ArrayList<Level> listData, Context context) {
        this.listData = listData;
        this.activityContext = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item_level, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Level myListData = listData.get(position);
        holder.textView_Desc.setText(listData.get(position).name);
        if (listData.get(position).unlocked){
            holder.imageView.setImageResource(R.drawable.unlocked);
        }else {
            holder.imageView.setImageResource(R.drawable.locked);
        }


        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(activityContext, MapsActivity.class);
                //k.putExtra("tripInfo",myListData);
                activityContext.startActivity(k);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView_Desc;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.img_status);
            this.textView_Desc = (TextView) itemView.findViewById(R.id.textView_description);

            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }


}