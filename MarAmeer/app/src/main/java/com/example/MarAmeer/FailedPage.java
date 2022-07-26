package com.example.MarAmeer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.MarAmeer.FireBase;
import com.example.MarAmeer.Item;
import com.example.MarAmeer.MainActivity;
import com.example.MarAmeer.R;
import com.example.MarAmeer.SignIn;
import com.google.android.material.button.MaterialButton;

import java.util.Arrays;
import java.util.List;

public class FailedPage extends Fragment {

    public FailedPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.payment_failed_layout, container, false);
    }
    public void onStart() {
        super.onStart();
        MainActivity ma=(MainActivity)getActivity();
        ma.setActionTitle(getResources().getString(R.string.fail_pg));
        MaterialButton tryagn = (MaterialButton) (View) getView().findViewById(R.id.tryAgainBtn);

        tryagn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                AddressPage fragment1 = new AddressPage();
                FragmentTransaction t1 = fm.beginTransaction();
                t1.replace(R.id.root_layout2, fragment1);
                t1.addToBackStack(null);
                t1.commit();


            }
        });

    }
}