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


public class CategoryItems extends Fragment {
    private ArrayList<Item> itemsList;
    private RecyclerView recyclerView;
    public CategoryItems() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_items, container, false);
    }

    public void onStart() {
        super.onStart();

        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        FireBase db=new FireBase(getActivity());
        //getting which category is requested
        String category =this.getArguments().getString(getResources().getString(R.string.category));
        MainActivity ma=(MainActivity)getActivity();
        ma.setActionTitle(category);
        MainActivity.dialog= ProgressDialog.show(getActivity(),"Loading","Please Wait...",true);
        db.getItems(category,this);

    }
    public void setupAdapter(ArrayList<Item> itemsList){
        if(!itemsList.isEmpty()) {
            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    //making every items clickable to view it's page
                    Bundle bundle = new Bundle();
                    String id = itemsList.get(position).getId();
                    bundle.putString(getResources().getString(R.string.id), id);
                    FragmentManager fm = getFragmentManager();
                    itemPage fragment = new itemPage();
                    fragment.setArguments(bundle);
                    FragmentTransaction t = fm.beginTransaction();
                    t.replace(R.id.root_layout2, fragment);
                    t.addToBackStack(null);
                    t.commit();

                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
            //setting up adapter
            RecyclerAdapterCategory adapter = new RecyclerAdapterCategory(itemsList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }else   Toast.makeText(getActivity(), getResources().getString(R.string.category_empty), Toast.LENGTH_LONG).show();
    }
}