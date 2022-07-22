package com.example.MarAmeer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;


public class ManageItems extends Fragment {
    private ArrayList<Item> itemsList;
    private RecyclerView recyclerView;
    public ManageItems() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.manage_items_frag, container, false);
    }

    public void onStart() {
        super.onStart();
        MainActivity ma=(MainActivity)getActivity();
        ma.setActionTitle(getResources().getString(R.string.manage_items));
        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        FireBase fb=new FireBase(getActivity());
        MaterialButton sold = (MaterialButton) (View) getView().findViewById(R.id.SoldBtn);
        sold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                SoldPage fragment = new SoldPage();
                FragmentTransaction t = fm.beginTransaction();
                t.replace(R.id.root_layout2, fragment);
                t.addToBackStack(null);
                t.commit();
            }
        });
        MainActivity.dialog= ProgressDialog.show(getActivity(),getResources().getString(R.string.loading),getResources().getString(R.string.wait),true);
        fb.getMyItems(MainActivity.user.getUsername(),this,(View) getView().findViewById(R.id.layout));
    }
    public void setupAdapter(ArrayList<Item> itemsList){
        if(!itemsList.isEmpty()) {
            FragmentManager fm = getFragmentManager();
            RecyclerAdapterManageItems adapter = new RecyclerAdapterManageItems(itemsList, this,getActivity(),(View) getView().findViewById(R.id.layout));
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }else   Toast.makeText(getActivity(), getResources().getString(R.string.cart_empty1), Toast.LENGTH_LONG).show();
    }

}