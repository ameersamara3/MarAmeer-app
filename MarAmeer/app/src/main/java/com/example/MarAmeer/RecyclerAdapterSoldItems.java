package com.example.MarAmeer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class RecyclerAdapterSoldItems extends RecyclerView.Adapter<RecyclerAdapterSoldItems.MyViewHolder> {


    private ArrayList<Item> itemsList;
    private SoldPage parent;
    private Context context;
    private Activity activity;

    public RecyclerAdapterSoldItems(ArrayList<Item> itemsList, SoldPage parent, Activity activity) {
        this.parent=parent;
        this.itemsList = itemsList;
        this.activity=activity;

    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView priceTxt;
        private TextView nameTxt;
        private ImageView image;

        public  MyViewHolder(final View view){
            super(view);
            image=view.findViewById(R.id.imageIcon);
            priceTxt=view.findViewById(R.id.price);
            nameTxt=view.findViewById(R.id.name);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sold_format,parent,false);
        return new MyViewHolder(itemView);
    }


    @Override
    //setup all buttons and image
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint({"Recycler View", "RecyclerView"}) int position) {
        String username = itemsList.get(position).getName();
        Log.println(Log.DEBUG,context.getResources().getString(R.string.debug), username);
        holder.nameTxt.setText(username);
        int price = itemsList.get(position).getPrice();
        holder.priceTxt.setText(price+context.getResources().getString(R.string.shekel));
        Bitmap bitmap=itemsList.get(position).getImg().copy(itemsList.get(position).getImg().getConfig(), true);;
        bitmap = MainActivity.getResizedBitmap(bitmap, 500, 500);
        holder.image.setImageBitmap(bitmap);
    }


    @Override
    public int getItemCount() {
        return itemsList.size();
    }
}
