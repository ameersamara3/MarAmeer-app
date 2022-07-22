package com.example.MarAmeer;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AddItem extends Fragment {
    public static Bitmap bitmap=null;
    ImageView image;

    public AddItem() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_item, container, false);
    }
    public void onStart() {
        super.onStart();
        MainActivity ma=(MainActivity)getActivity();
        //setting title
        ma.setActionTitle(getResources().getString(R.string.add_item));
        //getting all vars
        FireBase db=new FireBase(getActivity());
        MaterialButton addbtn = (MaterialButton) (View) getView().findViewById(R.id.addbtn);
        TextView name = (TextView) (View) getView().findViewById(R.id.name);
        TextView desc = (TextView) (View) getView().findViewById(R.id.description);
        TextView price = (TextView) (View) getView().findViewById(R.id.price);
        image = (ImageView) (View)getView().findViewById(R.id.image);
        MainActivity.image=image;
        Spinner spinner = (Spinner) (View)getView().findViewById(R.id.spinner);
        image.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {//pick a photo from gallery or camera
                boolean  pick =true;
                if(pick==false){
                    if(!checkCameraPremision())
                        requestCameraPremision();
                    else PickImage();
                }else{
                    if(!checkStoragePremision())
                        requestStoragePremision();
                    else PickImage();


                }
            }
        });
        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();//for spinner
        categories.add(getResources().getString(R.string.clothes));
        categories.add(getResources().getString(R.string.toys));
        categories.add(getResources().getString(R.string.traveling));
        categories.add(getResources().getString(R.string.other));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<TextView> al= Arrays.asList(name,desc,price);
                //check for errors
                for(TextView t : al){
                    if(t.getText().toString().trim().equals("")) {
                        MainActivity.Alert(getActivity(),getResources().getString(R.string.error),getResources().getString(R.string.empty));
                        return;
                    }

                }
                if(bitmap==null){
                    MainActivity.Alert(getActivity(),getResources().getString(R.string.error),getResources().getString(R.string.add_photo));

                    return;
                }
                //convert photo to bytes
                bitmap=MainActivity.getResizedBitmap(bitmap,500,500);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                //adding to database
                FireBase fb=new FireBase(getActivity());
                boolean flag=fb.addItem(name.getText().toString().trim(),
                        spinner.getSelectedItem().toString(),
                        price.getText().toString().trim(),
                        desc.getText().toString().trim(),
                        byteArray, MainActivity.user.getUsername());

                if(flag){
                    MainActivity.Success((View) getView().findViewById(R.id.layout),getActivity());
                    getActivity().onBackPressed();
                }
                else{
                    MainActivity.Fail((View) getView().findViewById(R.id.layout),getActivity());
                }


            }
        });
    }

    private void PickImage() {
        CropImage.activity().start(getActivity());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePremision() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPremision() {
        requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
    }

    private boolean checkStoragePremision() {
        return ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;

    }

    private boolean checkCameraPremision() {
        boolean res1= ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED;
        boolean res2= ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
        return res1 && res2;

    }

}