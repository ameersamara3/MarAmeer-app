package com.example.MarAmeer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Cart extends Fragment {
    private ArrayList<Item> itemsList;
    private RecyclerView recyclerView;
    public static TextView price;
    public Cart() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cart_items_fragment, container, false);
    }

    public void onStart() {
        super.onStart();
        MainActivity ma=(MainActivity)getActivity();
        //rename title
        ma.setActionTitle(getResources().getString(R.string.my_cart));
        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        FireBase db=new FireBase(getActivity());
        MainActivity.dialog= ProgressDialog.show(getActivity(),getResources().getString(R.string.loading),getResources().getString(R.string.wait),true);
        db.getMyCart(MainActivity.user.getUsername(),this);
        price = (TextView) (View) getView().findViewById(R.id.totalprice);
        MaterialButton next = (MaterialButton) (View) getView().findViewById(R.id.checkout);

        next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                if(!itemsList.isEmpty()) {
                    MainActivity.itemsToBuy = itemsList;
                    FragmentManager fm = getFragmentManager();
                    AddressPage fragment1 = new AddressPage();
                    FragmentTransaction t1 = fm.beginTransaction();
                    t1.replace(R.id.root_layout2, fragment1);
                    t1.addToBackStack(null);
                    t1.commit();
                }
                else {
                    MainActivity.Alert(getActivity(),getResources().getString(R.string.error),getResources().getString(R.string.cart_empty));
                }
            }
        });


    }
    public void setupAdapter(ArrayList<Item> itemsList) {
        this.itemsList=itemsList;
        int sum=0;
        for(Item item:itemsList){
            sum+=item.getPrice();
        }
        price.setText(sum+"");
        if(!itemsList.isEmpty()) {
            //setting up adapter
            FragmentManager fm = getFragmentManager();
            RecyclerAdapterCart adapter = new RecyclerAdapterCart(fm, itemsList,getActivity(),(View) getView().findViewById(R.id.layout));
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }else
            Toast.makeText(getActivity(), getResources().getString(R.string.cart_empty), Toast.LENGTH_LONG).show();
    }

    }