package com.example.MarAmeer;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class RecyclerAdapterOrders extends RecyclerView.Adapter<RecyclerAdapterOrders.MyViewHolder> {


    private ArrayList<Order> ordersList;
    private Context context;
    private FragmentManager fm;


    public RecyclerAdapterOrders(ArrayList<Order> ordersList,FragmentManager fm) {
        this.ordersList = ordersList;
        this.fm=fm;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView priceTxt;
        private TextView nameTxt;
        private TextView dateTxt;
        private Order order;

        private MaterialButton seeitems;
        public  MyViewHolder(final View view){
            super(view);
            priceTxt=view.findViewById(R.id.price);
            nameTxt=view.findViewById(R.id.name);
            dateTxt=view.findViewById(R.id.date);
            seeitems=view.findViewById(R.id.seeitems);
            seeitems.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    //open requested edit page
                    Bundle bundle = new Bundle();
                    String id = order.getId();
                    bundle.putString(context.getResources().getString(R.string.id), id);
                    Boolean flag;
                    if(ChronoUnit.MINUTES.between(order.getDt(), LocalDateTime.now())>=5) flag=true;
                    else flag=false;
                    bundle.putBoolean(context.getResources().getString(R.string.flag), flag);
                    OrderItems fragment = new OrderItems();
                    fragment.setArguments(bundle);
                    FragmentTransaction t = fm.beginTransaction();
                    t.replace(R.id.root_layout2, fragment);
                    t.addToBackStack(null);
                    t.commit();

                }
            });
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_format,parent,false);
        return new MyViewHolder(itemView);
    }


    @Override
    //setup all buttons and image
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String id = ordersList.get(position).getId();
        Log.println(Log.DEBUG,context.getResources().getString(R.string.debug), id);
        holder.nameTxt.setText(id);
        int price = ordersList.get(position).getTotalprice();
        holder.priceTxt.setText(price+context.getResources().getString(R.string.shekel));
        LocalDateTime date = ordersList.get(position).getDt();
        holder.dateTxt.setText(date.toString());
        holder.order=ordersList.get(position);

    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }
}
