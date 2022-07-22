package com.example.MarAmeer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class SignIn extends Fragment {


    public SignIn() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }
    public void onStart() {
        super.onStart();

        TextView username =(TextView)(View)getView().findViewById(R.id.username);
        TextView password =(TextView) (View)getView().findViewById(R.id.password);
        MaterialButton loginbtn = (MaterialButton) (View)getView().findViewById(R.id.loginbtn2);
        MaterialButton signupbtn = (MaterialButton) (View)getView().findViewById(R.id.loginbtn);

       loginbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //check user and login
               FireBase fb=new FireBase(getActivity());
               fb.SignIn(username.getText().toString().trim(),password.getText().toString().trim());
           }
        });
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //go to sign up
                FragmentManager fm = getFragmentManager();
                SignUpFrag fragment= new SignUpFrag();
                FragmentTransaction t= fm.beginTransaction();
                t.replace(R.id.root_layout, fragment);
                t.addToBackStack(null);
                t.commit();
            }



        });


    }

}
