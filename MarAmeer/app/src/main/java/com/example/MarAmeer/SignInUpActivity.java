package com.example.MarAmeer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class SignInUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_up);
        if(!FireBase.isNetworkAvailable(this)){
            MainActivity.Custom(findViewById(R.id.layout),this,getResources().getString(R.string.no_net));
        }

    }

    protected void onResume() {
        MainActivity.user=null;
        super.onResume();
        FragmentManager fm = getFragmentManager();
        SignIn fragment1 = new SignIn();
        FragmentTransaction t = fm.beginTransaction();
        t.replace(R.id.root_layout, fragment1);
        t.commit();
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                new FireBase(this).setMainUser(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            }
        }

}

        //MaterialButton loginbtn = (MaterialButton) findViewById(R.id.loginbtn2);


        //admin and admin

        //loginbtn.setOnClickListener(new View.OnClickListener() {
           // @Override
           // public void onClick(View v) {


                /*if(username.getText().toString().equals("admin") && password.getText().toString().equals("admin")){
                    //correct
                    Toast.makeText(SignInUpActivity.this,"LOGIN SUCCESSFUL",Toast.LENGTH_SHORT).show();
                }else
                    //incorrect
                    Toast.makeText(SignInUpActivity.this,"LOGIN FAILED !!!",Toast.LENGTH_SHORT).show();*/
           // }
        //});

