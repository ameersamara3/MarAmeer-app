package com.example.MarAmeer;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;

import android.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;


public class OrderItems extends Fragment {
    private ArrayList<Item> itemsList;
    private RecyclerView recyclerView;
    private boolean flag;
    public OrderItems() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_items, container, false);
    }

    public void onStart() {
        super.onStart();
        String id = this.getArguments().getString(getResources().getString(R.string.id));
        flag = this.getArguments().getBoolean(getResources().getString(R.string.flag));

        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        FireBase db = new FireBase(getActivity());
        //getting which category is requested
        MainActivity ma = (MainActivity) getActivity();
        ma.setActionTitle(getResources().getString(R.string.order));
        MainActivity.dialog= ProgressDialog.show(getActivity(),getResources().getString(R.string.loading),getResources().getString(R.string.wait),true);
        db.getOrderItems(id,this);

    }

    public void setupAdapter(ArrayList<Item> itemsList) {
        FireBase db = new FireBase(getActivity());
        if (!itemsList.isEmpty()) {
            //setting up adapter
            RecyclerAdapterOrderItems adapter = new RecyclerAdapterOrderItems(itemsList, flag, db, getActivity(), (View) getView().findViewById(R.id.layout));
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        } else
            Toast.makeText(getActivity(), getResources().getString(R.string.db_error), Toast.LENGTH_LONG).show();
    }
}