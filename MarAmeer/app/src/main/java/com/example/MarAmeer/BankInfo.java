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


public class BankInfo extends Fragment {
    public BankInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bank_info_frag, container, false);
    }
    public void onStart() {
        super.onStart();
        MainActivity ma=(MainActivity)getActivity();
        ma.setActionTitle(getResources().getString(R.string.address_pg));
        TextView accountNum = (TextView) (View) getView().findViewById(R.id.accountNum);
        TextView owner_name = (TextView) (View) getView().findViewById(R.id.Owner_name);
        TextView bank_number = (TextView) (View) getView().findViewById(R.id.Bank_Number);
        TextView Branch_Number = (TextView) (View) getView().findViewById(R.id.Branch_Number);
        MaterialButton add = (MaterialButton) (View) getView().findViewById(R.id.addinfo);

        add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                List<TextView> al= Arrays.asList(accountNum,owner_name,bank_number,Branch_Number);
                //check for errors
                for(TextView t : al){
                    if(t.getText().toString().trim().equals("")) {
                        MainActivity.Alert(getActivity(),getResources().getString(R.string.error),getResources().getString(R.string.empty));
                        return;
                    }

                }
                FireBase fb=new FireBase(getActivity());
                if(fb.updateBank(MainActivity.user.getUsername(),owner_name.getText().toString().trim(),accountNum.getText().toString().trim(),
                        Branch_Number.getText().toString().trim(),bank_number.getText().toString().trim())){
                    //MainActivity.Alert(getActivity(),getResources().getString(R.string.success_message),getResources().getString(R.string.success_added));
                    MainActivity.Success((View) getView().findViewById(R.id.layout),getActivity());
                    getActivity().onBackPressed();
                }
                else{
                    MainActivity.Fail((View) getView().findViewById(R.id.layout),getActivity());
                   // MainActivity.Alert(getActivity(),getResources().getString(R.string.error),getResources().getString(R.string.database_fail));
                }
                }

        });

    }
}