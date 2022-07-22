package com.example.MarAmeer;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;


public class CategoryPage extends Fragment {
    private Item item;

    public CategoryPage() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {


        super.onStart();
        MainActivity ma=(MainActivity)getActivity();
        ma.setActionTitle(getResources().getString(R.string.category_title));
        MaterialButton clothingbtn = (MaterialButton) (View) getView().findViewById(R.id.showClothing);
        MaterialButton toysbtn = (MaterialButton) (View) getView().findViewById(R.id.showToys);
        MaterialButton travelbtn = (MaterialButton) (View) getView().findViewById(R.id.showTraveling);
        MaterialButton otherbtn = (MaterialButton) (View) getView().findViewById(R.id.showOther);
        FireBase db=new FireBase(getActivity());
        clothingbtn.setOnClickListener(new View.OnClickListener() {
            //every button goes to the same page but different argument is passed
            @Override
            public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(getResources().getString(R.string.category), getResources().getString(R.string.clothes));
                    FragmentManager fm = getFragmentManager();
                    CategoryItems fragment = new CategoryItems();
                    fragment.setArguments(bundle);
                    FragmentTransaction t = fm.beginTransaction();
                    t.replace(R.id.root_layout2, fragment);
                    t.addToBackStack(null);
                    t.commit();
            }
        });
        otherbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(getResources().getString(R.string.category), getResources().getString(R.string.other));
                    FragmentManager fm = getFragmentManager();
                    CategoryItems fragment = new CategoryItems();
                    fragment.setArguments(bundle);
                    FragmentTransaction t = fm.beginTransaction();
                    t.replace(R.id.root_layout2, fragment);
                    t.addToBackStack(null);
                    t.commit();

            }
        });
        toysbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putString(getResources().getString(R.string.category), getResources().getString(R.string.toys));
                    FragmentManager fm = getFragmentManager();
                    CategoryItems fragment = new CategoryItems();
                    fragment.setArguments(bundle);
                    FragmentTransaction t = fm.beginTransaction();
                    t.replace(R.id.root_layout2, fragment);
                    t.addToBackStack(null);
                    t.commit();
            }
        });
        travelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(getResources().getString(R.string.category), getResources().getString(R.string.traveling));
                    FragmentManager fm = getFragmentManager();
                    CategoryItems fragment = new CategoryItems();
                    fragment.setArguments(bundle);
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
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }
}