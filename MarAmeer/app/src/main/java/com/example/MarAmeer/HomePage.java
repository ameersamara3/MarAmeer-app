package com.example.MarAmeer;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;


public class HomePage extends Fragment {
    private Item item;

    public HomePage() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {

        super.onStart();
        MainActivity ma=(MainActivity)getActivity();
        //setting up title
        ma.setActionTitle(getResources().getString(R.string.hi)+" "+MainActivity.user.getFname());
        MaterialButton categorybtn = (MaterialButton) (View) getView().findViewById(R.id.categoryBtn);
        MaterialButton addbtn = (MaterialButton) (View) getView().findViewById(R.id.addNewItemBtn);
        MaterialButton manageitems = (MaterialButton) (View) getView().findViewById(R.id.ManageItemsBtn);
        MaterialButton myorders = (MaterialButton) (View) getView().findViewById(R.id.myorders);
        FireBase db=new FireBase(getActivity());
        categorybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getFragmentManager();
                CategoryPage fragment = new CategoryPage();
                FragmentTransaction t = fm.beginTransaction();
                t.replace(R.id.root_layout2, fragment);
                t.addToBackStack(null);
                t.commit();
            }

        });
        myorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getFragmentManager();
                MyOrders fragment = new MyOrders();
                FragmentTransaction t = fm.beginTransaction();
                t.replace(R.id.root_layout2, fragment);
                t.addToBackStack(null);
                t.commit();
            }

        });
        manageitems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    FragmentManager fm = getFragmentManager();
                    ManageItems fragment = new ManageItems();
                    FragmentTransaction t = fm.beginTransaction();
                    t.replace(R.id.root_layout2, fragment);
                    t.addToBackStack(null);
                    t.commit();
            }

        });
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getFragmentManager();
                AddItem fragment = new AddItem();
                FragmentTransaction t = fm.beginTransaction();
                t.replace(R.id.root_layout2, fragment);
                t.addToBackStack(null);
                t.commit();
            }

        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_home, container, false);
    }
}