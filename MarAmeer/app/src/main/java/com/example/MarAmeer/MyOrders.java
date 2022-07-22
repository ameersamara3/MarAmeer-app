package com.example.MarAmeer;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;

import android.app.Fragment;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;


public class MyOrders extends Fragment {
    private ArrayList<Order> ordersList;

    public MyOrders() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_items, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onStart() {
        super.onStart();

        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        FireBase db=new FireBase(getActivity());
        //getting which category is requested
        MainActivity ma=(MainActivity)getActivity();
        ma.setActionTitle(getResources().getString(R.string.my_orders));
        ordersList=db.getOrders(MainActivity.user.getUsername());
        if(!ordersList.isEmpty()) {
            //setting up adapter
            RecyclerAdapterOrders adapter = new RecyclerAdapterOrders(ordersList,getFragmentManager());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }else   Toast.makeText(getActivity(), getResources().getString(R.string.no_orders), Toast.LENGTH_LONG).show();
    }
}