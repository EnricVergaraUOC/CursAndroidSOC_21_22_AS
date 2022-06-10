package edu.uoc.mapgame.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
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
import edu.uoc.mapgame.ui.SinglePlayer;

public class LevelListAdapter extends RecyclerView.Adapter<LevelListAdapter.ViewHolder>{
    private ArrayList<Level> listData;
    private SinglePlayer activity;
    // RecyclerView recyclerView;
    public LevelListAdapter(ArrayList<Level> listData, SinglePlayer context) {
        this.listData = listData;
        this.activity = context;
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
                if (myListData.unlocked){
                    Intent k = new Intent(activity, MapsActivity.class);
                    k.putExtra("levelInfo",myListData);
                    k.putExtra("lastLevelUnlocked",activity.lastLevelUnlocked);
                    k.putExtra("indexCurrentLevel",position);

                    activity.startActivity(k);
                }else{
                    new android.app.AlertDialog.Builder(activity)
                            .setTitle(myListData.name + " is locked")
                            .setMessage("You need to unlocked the previous leves before to start this level")

                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Nothing to do
                                }
                            })

                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

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