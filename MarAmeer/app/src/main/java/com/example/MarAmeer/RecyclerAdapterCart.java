package com.example.MarAmeer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class RecyclerAdapterCart extends RecyclerView.Adapter<RecyclerAdapterCart.MyViewHolder> {


    private ArrayList<Item> itemsList;
    private FragmentManager fm;
    private Activity activity;
    private View v;

    public RecyclerAdapterCart(FragmentManager fm, ArrayList<Item> itemsList, Activity activity,View v) {
        this.itemsList = itemsList;
        this.fm=fm;
        this.activity=activity;
        this.v=v;

    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView priceTxt;
        private TextView nameTxt;
        private ImageView image;
        private MaterialButton deletebtn;

        public  MyViewHolder(final View view){
            super(view);
            image=view.findViewById(R.id.imageIcon3);
            priceTxt=view.findViewById(R.id.price3);
            nameTxt=view.findViewById(R.id.name3);
            deletebtn = view.findViewById(R.id.deleteBtn3);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart,parent,false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint({"Recycler View", "RecyclerView"}) int position) {
        String username = itemsList.get(position).getName();
        holder.nameTxt.setText(username);
        int price = itemsList.get(position).getPrice();
        holder.priceTxt.setText(price+" ₪");
        Bitmap bitmap=itemsList.get(position).getImg().copy(itemsList.get(position).getImg().getConfig(), true);;
        bitmap = MainActivity.getResizedBitmap(bitmap, 500, 500);
        holder.image.setImageBitmap(bitmap);
        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireBase db=new FireBase(activity);
                db.deleteCartItem(itemsList.get(position).getId(), MainActivity.user.getUsername());
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage(v.getContext().getResources().getString(R.string.delete_confirm));
                builder.setTitle(v.getContext().getResources().getString(R.string.delete_warning));
                builder.setCancelable(false);
                builder.setNegativeButton(v.getContext().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton(v.getContext().getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(db.deleteCartItem(itemsList.get(position).getId(),MainActivity.user.getUsername())) {
                            Cart.price.setText(Integer.parseInt(Cart.price.getText().toString())-itemsList.get(position).getPrice()+"");
                            MainActivity.Success(v,activity);
                            itemsList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, itemsList.size());
                            notifyDataSetChanged();
                        }
                        else{
                            MainActivity.Fail(v,activity);
                        }

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();


            }
        });


            }


    @Override
    public int getItemCount() {
        return itemsList.size();
    }
}
