package com.example.MarAmeer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class RecyclerAdapterManageItems extends RecyclerView.Adapter<RecyclerAdapterManageItems.MyViewHolder> {


    private ArrayList<Item> itemsList;
    private ManageItems parent;
    private Context context;
    private Activity activity;
    private View v;


    public RecyclerAdapterManageItems(ArrayList<Item> itemsList, ManageItems parent,Activity activity,View v) {
        this.parent=parent;
        this.itemsList = itemsList;
        this.activity=activity;
        this.v=v;

    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView priceTxt;
        private TextView nameTxt;
        private ImageView image;
        private MaterialButton editbtn;
        private MaterialButton deletebtn;

        public  MyViewHolder(final View view){
            super(view);
            image=view.findViewById(R.id.imageIcon);
            priceTxt=view.findViewById(R.id.price);
            nameTxt=view.findViewById(R.id.name);
            editbtn = view.findViewById(R.id.editBtn);
            deletebtn = view.findViewById(R.id.deleteBtn);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manage_format,parent,false);
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
        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireBase db=new FireBase(activity);
                //are you sure Alert
                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                builder.setMessage(context.getResources().getString(R.string.delete_confirm));
                builder.setTitle(context.getResources().getString(R.string.delete_warning));
                builder.setCancelable(false);
                builder.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton(context.getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if(db.deleteItem(itemsList.get(position).getId())) {
                            //remove and refresh
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
        holder.editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open requested edit page
                EditItem fragment=new EditItem(parent);
                fragment.show(MainActivity.fm,context.getResources().getString(R.string.edit_item));
                Bundle bundle = new Bundle();
                String id = itemsList.get(position).getId();
                bundle.putString(context.getResources().getString(R.string.id), id );
                fragment.setArguments(bundle);

            }
        });

            }


    @Override
    public int getItemCount() {
        return itemsList.size();
    }
}
