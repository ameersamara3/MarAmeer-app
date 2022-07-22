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

import java.util.Arrays;
import java.util.List;


public class SignUpFrag extends Fragment {

        public SignUpFrag() {
          // Required empty public constructor
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_sign_up, container, false);
        }
        public void onStart() {
        super.onStart();
        TextView username = (TextView) (View) getView().findViewById(R.id.username);
        TextView password = (TextView) (View) getView().findViewById(R.id.password);
        TextView confirmPass = (TextView) (View) getView().findViewById(R.id.passwordConfirm);
        TextView fname = (TextView) (View) getView().findViewById(R.id.fname);
        TextView lname = (TextView) (View) getView().findViewById(R.id.lname);
        MaterialButton back = (MaterialButton) (View) getView().findViewById(R.id.back);
        MaterialButton signupbtn = (MaterialButton) (View) getView().findViewById(R.id.signupbtn);

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                //check data and add user and login
                List<TextView> al= Arrays.asList(username, password, confirmPass,fname,lname);

                    for(TextView t : al){
                        if(t.getText().toString().trim().equals("")) {
                            MainActivity.Alert(getActivity(),getResources().getString(R.string.error),getResources().getString(R.string.empty));
                            return;
                        }

                    }
                if(!confirmPass.getText().toString().equals(password.getText().toString()))
                    MainActivity.Alert(getActivity(),getResources().getString(R.string.error),getResources().getString(R.string.password_fail));
                else{
                    FireBase fb=new FireBase(getActivity());
                    fb.register(username.getText().toString().trim(),password.getText().toString().trim(),fname.getText().toString().trim(),lname.getText().toString().trim(),(View) getView().findViewById(R.id.layout));

                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to sign up
                FragmentManager fm = getFragmentManager();
                SignIn fragment1= new SignIn();
                FragmentTransaction t= fm.beginTransaction();
                t.replace(R.id.root_layout, fragment1);
                t.addToBackStack(null);
                t.commit();
            }


        });
    }
}