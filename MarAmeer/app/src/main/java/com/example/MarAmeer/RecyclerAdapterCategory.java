package com.example.MarAmeer;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import java.util.ArrayList;

public class RecyclerAdapterCategory extends RecyclerView.Adapter<RecyclerAdapterCategory.MyViewHolder> {


    private ArrayList<Item> itemsList;
    private Context context;

    public RecyclerAdapterCategory(ArrayList<Item> itemsList) {
        this.itemsList = itemsList;
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
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_format,parent,false);
        return new MyViewHolder(itemView);
    }


    @Override
    //setup all buttons and image
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
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
