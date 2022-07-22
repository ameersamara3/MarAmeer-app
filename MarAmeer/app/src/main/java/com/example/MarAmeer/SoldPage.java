package com.example.MarAmeer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;


public class SoldPage extends Fragment {
    private ArrayList<Item> itemsList;
    private TextView total;
    private RecyclerView recyclerView;
    private MaterialButton addinfobtn;
    public SoldPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.sold_items_frag, container, false);
    }

    public void onStart() {
        super.onStart();
        MainActivity ma=(MainActivity)getActivity();
        ma.setActionTitle(getResources().getString(R.string.manage_items));
        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        FireBase fb=new FireBase(getActivity());
        total = (TextView) (View) getView().findViewById(R.id.totalearn);
        addinfobtn = (MaterialButton) (View) getView().findViewById(R.id.editaddinfo);

        MainActivity.dialog= ProgressDialog.show(getActivity(),getResources().getString(R.string.loading),getResources().getString(R.string.wait),true);
        fb.getMySoldItems(MainActivity.user.getUsername(),this,(View) getView().findViewById(R.id.layout));


    }
    public void setupAdapter(ArrayList<Item> itemsList) {
        int sum=0;
        //itemsList=db.getMyItems(MainActivity.user.getUsername());
        if(itemsList!=null) {
            FragmentManager fm = getFragmentManager();
            RecyclerAdapterSoldItems adapter = new RecyclerAdapterSoldItems(itemsList, this,getActivity());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            for(Item item : itemsList){
                sum+=item.getPrice();
            }
        }else   Toast.makeText(getActivity(), getResources().getString(R.string.cart_empty1), Toast.LENGTH_LONG).show();
        total.setText(sum+"");
        addinfobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getFragmentManager();
                BankInfo fragment = new BankInfo();
                FragmentTransaction t = fm.beginTransaction();
                t.replace(R.id.root_layout2, fragment);
                t.addToBackStack(null);
                t.commit();
            }

        });
    }


}