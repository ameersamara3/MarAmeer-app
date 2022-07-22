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


public class paymentPage extends Fragment {
    private String city;
    private String mobile;
    private String address;
    private String zip;
    private String comment;
    private int sum=0;


    public paymentPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.payment_layout, container, false);
    }
    public void onStart() {
        super.onStart();
        MainActivity ma = (MainActivity) getActivity();
        ma.setActionTitle(getResources().getString(R.string.payment_pg));
        city = this.getArguments().getString(getResources().getString(R.string.city));
        mobile = this.getArguments().getString(getResources().getString(R.string.mobile));
        address = this.getArguments().getString(getResources().getString(R.string.address));
        zip = this.getArguments().getString(getResources().getString(R.string.zip));
        comment = this.getArguments().getString(getResources().getString(R.string.comment));

        TextView cardHolderName = (TextView) (View) getView().findViewById(R.id.cardHolderName);
        TextView cardNum = (TextView) (View) getView().findViewById(R.id.cardNum);
        TextView cardExpDate = (TextView) (View) getView().findViewById(R.id.cardExpDate);
        TextView cardCVV = (TextView) (View) getView().findViewById(R.id.cardCVV);
        TextView totalprice = (TextView) (View) getView().findViewById(R.id.totalprice2);
        MaterialButton buy = (MaterialButton) (View) getView().findViewById(R.id.btnBuy);
        for (Item item : MainActivity.itemsToBuy) {
            sum += item.getPrice();
        }
        paymentPage p =this;
        totalprice.setText(sum + "");
        buy.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                //check data and add user and login
                List<TextView> al = Arrays.asList(cardHolderName, cardNum, cardExpDate, cardCVV);

                for (TextView t : al) {
                    if (t.getText().toString().trim().equals("")) {
                        MainActivity.Alert(getActivity(), getResources().getString(R.string.error), getResources().getString(R.string.empty));
                        return;
                    }
                }
                FireBase fb = new FireBase(getActivity());
                fb.getMyCart2(MainActivity.user.getUsername(),p);

            }

        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void handleOrder(ArrayList<Item> itemsList2){
        FireBase fb = new FireBase(getActivity());
        int sum2=0;
            for(Item item:itemsList2){
                sum2+=item.getPrice();
            }
            if (sum2!=sum) {
                MainActivity.Custom((View) getView().findViewById(R.id.layout), getActivity(),getResources().getString(R.string.cart_error));
                FragmentManager fm = getFragmentManager();
                FailedPage fragment1 = new FailedPage();
                FragmentTransaction t1 = fm.beginTransaction();
                t1.replace(R.id.root_layout2, fragment1);
                t1.addToBackStack(null);
                t1.commit();
            } else {
                if (fb.addOrder(city, mobile, address, zip, comment, MainActivity.itemsToBuy, MainActivity.user.getUsername(), sum)) {
                    MainActivity.Success((View) getView().findViewById(R.id.layout), getActivity());
                    FragmentManager fm = getFragmentManager();
                    SuccessPage fragment1 = new SuccessPage();
                    FragmentTransaction t1 = fm.beginTransaction();
                    t1.replace(R.id.root_layout2, fragment1);
                    t1.addToBackStack(null);
                    t1.commit();
                } else {
                    MainActivity.Fail((View) getView().findViewById(R.id.layout), getActivity());
                    FragmentManager fm = getFragmentManager();
                    FailedPage fragment1 = new FailedPage();
                    FragmentTransaction t1 = fm.beginTransaction();
                    t1.replace(R.id.root_layout2, fragment1);
                    t1.addToBackStack(null);
                    t1.commit();
                }
            }
        }
    }
