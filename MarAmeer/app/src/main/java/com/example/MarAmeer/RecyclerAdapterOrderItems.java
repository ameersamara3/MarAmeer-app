package com.example.MarAmeer;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class RecyclerAdapterOrderItems extends RecyclerView.Adapter<RecyclerAdapterOrderItems.MyViewHolder> {


    private ArrayList<Item> itemsList;
    private boolean readyToRate;
    private Context context;
    private FireBase fb;
    private Activity activity;
    private View vie;



    public RecyclerAdapterOrderItems(ArrayList<Item> itemsList,boolean readyToRate,FireBase fb,Activity activity,View vie) {
        this.itemsList = itemsList;
        this.readyToRate=readyToRate;
        this.fb=fb;
        this.vie=vie;
        this.activity=activity;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView priceTxt;
        private TextView nameTxt;
        private ImageView image;
        private TextView ratingMsg;
        private MaterialButton btn;
        private RatingBar bar;
        private Item item;


        public  MyViewHolder(final View view){
            super(view);
            image=view.findViewById(R.id.imageIcon);
            priceTxt=view.findViewById(R.id.price);
            nameTxt=view.findViewById(R.id.name);
            ratingMsg=view.findViewById(R.id.ratingmessage);
            ratingMsg.setVisibility(View.INVISIBLE);
            btn=view.findViewById(R.id.Ratingbtn);
            bar=view.findViewById(R.id.ratingBar2);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(btn.getText().toString().equals(context.getResources().getString(R.string.rate))){
                        if(fb.addRating(item.getId(),item.getOwnerID(),bar.getRating())) {
                            btn.setText(context.getResources().getString(R.string.rate_reset));
                            ratingMsg.setText(context.getResources().getString(R.string.your_rate) + bar.getRating());
                            ratingMsg.setVisibility(View.VISIBLE);
                            bar.setVisibility(View.INVISIBLE);
                       }else{
                           MainActivity.Fail(vie,activity);
                       }
                   }
                    else{
                        if(fb.deleteRating(item.getId(),item.getOwnerID())) {
                            btn.setText(context.getResources().getString(R.string.rate));
                            bar.setVisibility(View.VISIBLE);
                            ratingMsg.setVisibility(View.INVISIBLE);
                        }else{
                            MainActivity.Alert(context,context.getResources().getString(R.string.error),context.getResources().getString(R.string.rate_fail));
                        }

                   }

                }
            });

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderitems_format,parent,false);
        return new MyViewHolder(itemView);
    }


    @Override
    //setup all buttons and image
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.item=itemsList.get(position);
        String username = itemsList.get(position).getName();
        Log.println(Log.DEBUG,context.getResources().getString(R.string.debug), username);
        holder.nameTxt.setText(username);
        int price = itemsList.get(position).getPrice();
        holder.priceTxt.setText(price+context.getResources().getString(R.string.shekel));
        Bitmap bitmap=itemsList.get(position).getImg().copy(itemsList.get(position).getImg().getConfig(), true);;
        bitmap = MainActivity.getResizedBitmap(bitmap, 500, 500);
        holder.image.setImageBitmap(bitmap);
        if(!readyToRate){
            holder.btn.setVisibility(View.INVISIBLE);
            holder.bar.setVisibility(View.INVISIBLE);
            holder.ratingMsg.setText(context.getResources().getString(R.string.rate_msg));
            holder.ratingMsg.setVisibility(View.VISIBLE);

        }else {
            float res = fb.alreadyRated(holder.item.getId(), holder.item.getOwnerID());
            if (res >= 0) {
                holder.btn.setText(context.getResources().getString(R.string.rate_reset));
                holder.ratingMsg.setText(context.getResources().getString(R.string.your_rate) + res);
                holder.ratingMsg.setVisibility(View.VISIBLE);
                holder.bar.setVisibility(View.INVISIBLE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }
}
