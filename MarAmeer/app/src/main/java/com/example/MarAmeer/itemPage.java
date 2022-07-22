package com.example.MarAmeer;

import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;


public class itemPage extends Fragment {
private Item item;

    public itemPage() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        /*Bundle bundle = new Bundle();
        String myMessage = "Stackoverflow is cool!";
        bundle.putString("message", myMessage );
        fragment.setArguments(bundle);*/

        super.onStart();
        String id = this.getArguments().getString(getResources().getString(R.string.id));
        MainActivity ma=(MainActivity)getActivity();
        ma.setActionTitle(getResources().getString(R.string.item_page));
        FireBase db=new FireBase(getActivity());

        Item item=db.getItemById(id,true);
        TextView sellerName =(TextView)(View)getView().findViewById(R.id.sellerName);
        TextView name =(TextView)(View)getView().findViewById(R.id.itemName);
        TextView price =(TextView) (View)getView().findViewById(R.id.itemPrice);
        TextView desc =(TextView) (View)getView().findViewById(R.id.itemDesc);
        ImageView image =(ImageView) (View)getView().findViewById(R.id.itemImage);
        TextView rating =(TextView) (View)getView().findViewById(R.id.sellerrating);

        MaterialButton actbtn = (MaterialButton) (View) getView().findViewById(R.id.itemATC);
        name.setText(item.getName());
        price.setText(item.getPrice()+getResources().getString(R.string.shekel));
        desc.setText(item.getDesc());
        float res=db.getAverageRating(item.getOwnerID());
        String rate;
        if(res==-1){
            rate=getResources().getString(R.string.no_reviews);
        }else{
            rate=res+" / 5";
        }
        rating.setText(rate);
        sellerName.setText(item.getOwnerID());
        image.setImageBitmap(MainActivity.getResizedBitmap(item.getImg(), 1000, 1000));
        actbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checking if user can add to cart

                    if(!db.checkIfMyItem(MainActivity.user.getUsername(), item.getId())) {
                        if (db.addItemToCart(MainActivity.user.getUsername(), item.getId())) {
                            MainActivity.Success((View) getView().findViewById(R.id.layout),getActivity());

                        } else {
                            //MainActivity.Alert(getActivity(), getResources().getString(R.string.error), getResources().getString(R.string.item_exist_in_cart));
                            MainActivity.Custom((View) getView().findViewById(R.id.layout),getActivity(),getResources().getString(R.string.item_exist_in_cart));


                        }
                    }
                else{
                    //MainActivity.Alert(getActivity(),getResources().getString(R.string.error),getResources().getString(R.string.add_to_cart_fail));
                    MainActivity.Custom((View) getView().findViewById(R.id.layout),getActivity(),getResources().getString(R.string.add_to_cart_fail));
                    }
            }

        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_page, container, false);
    }
}