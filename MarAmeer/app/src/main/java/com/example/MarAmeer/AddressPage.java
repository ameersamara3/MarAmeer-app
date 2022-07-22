package com.example.MarAmeer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AddressPage extends Fragment {
    public AddressPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.address_layout, container, false);
    }
    public void onStart() {
        super.onStart();
        MainActivity ma=(MainActivity)getActivity();
        ma.setActionTitle(getResources().getString(R.string.address_pg));
        TextView city = (TextView) (View) getView().findViewById(R.id.city);
        TextView mobile = (TextView) (View) getView().findViewById(R.id.MobileNumber);
        TextView address = (TextView) (View) getView().findViewById(R.id.address);
        TextView zip = (TextView) (View) getView().findViewById(R.id.zip);
        TextView comment = (TextView) (View) getView().findViewById(R.id.comment);
        MaterialButton next = (MaterialButton) (View) getView().findViewById(R.id.btnNext);

        next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                //check data and add user and login
                List<TextView> al= Arrays.asList(city, mobile, address,zip,comment);

                for(TextView t : al){
                    if(t.getText().toString().trim().equals("")) {
                        MainActivity.Alert(getActivity(),getResources().getString(R.string.error),getResources().getString(R.string.empty));
                        return;
                    }
                    FragmentManager fm = getFragmentManager();
                    paymentPage fragment1= new paymentPage();
                    Bundle bundle = new Bundle();
                    bundle.putString(getResources().getString(R.string.city), city.getText().toString().trim());
                    bundle.putString(getResources().getString(R.string.mobile), mobile.getText().toString().trim());
                    bundle.putString(getResources().getString(R.string.address), address.getText().toString().trim());
                    bundle.putString(getResources().getString(R.string.zip), zip.getText().toString().trim());
                    bundle.putString(getResources().getString(R.string.comment), comment.getText().toString().trim());
                    fragment1.setArguments(bundle);
                    FragmentTransaction t1= fm.beginTransaction();
                    t1.replace(R.id.root_layout2, fragment1);
                    t1.addToBackStack(null);
                    t1.commit();
                }

            }
        });

    }
}