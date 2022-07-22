package com.example.MarAmeer;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class EditItem extends DialogFragment {
    public static Bitmap bitmap=null;
    private ImageView image;
    private ManageItems parent;
    private int i=0;

    public EditItem(ManageItems parent) {
        this.parent=parent;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_item, container, false);
    }
    public void onStart() {
        //same comments as addItem class
        //only update and add functions are different
        super.onStart();
        if(i==0){
        String id = this.getArguments().getString(getResources().getString(R.string.id));
        FireBase db=new FireBase(getActivity());
        Item item=db.getItemById(id,true);
        MaterialButton editbtn = (MaterialButton) (View) getView().findViewById(R.id.editbtn);
        MaterialButton cancelbtn = (MaterialButton) (View) getView().findViewById(R.id.cancel);

        TextView name = (TextView) (View) getView().findViewById(R.id.name2);
        TextView desc = (TextView) (View) getView().findViewById(R.id.description2);
        TextView price = (TextView) (View) getView().findViewById(R.id.price2);
        image = (ImageView) (View)getView().findViewById(R.id.image);
        MainActivity.image=image;
        Spinner spinner = (Spinner) (View)getView().findViewById(R.id.spinner2);
        name.setText(item.getName());
        desc.setText(item.getDesc());
        price.setText(item.getPrice()+"");
        image.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
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
        List<String> categories = new ArrayList<String>();
        categories.add(getResources().getString(R.string.clothes));
        categories.add(getResources().getString(R.string.toys));
        categories.add(getResources().getString(R.string.traveling));
        categories.add(getResources().getString(R.string.other));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(categories.indexOf(item.getCategory()));
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
            });

        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<TextView> al= Arrays.asList(name,desc,price);

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
                bitmap=MainActivity.getResizedBitmap(bitmap,500,500);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                builder.setMessage(getResources().getString(R.string.edit_confirm));
                builder.setTitle(getResources().getString(R.string.edit_warning));
                builder.setCancelable(false);
                builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setPositiveButton(getResources().getString(R.string.update), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            if (db.edititem(item.getId(), name.getText().toString().trim(),
                                    spinner.getSelectedItem().toString(),
                                    price.getText().toString().trim(),
                                    desc.getText().toString().trim(),
                                    byteArray, MainActivity.user.getUsername())) {
                                MainActivity.Success((View) getView().findViewById(R.id.layout),getActivity());
                                parent.onStart();
                                getDialog().dismiss();
                            }
                            else {
                                MainActivity.Fail((View) getView().findViewById(R.id.layout),getActivity());
                            }
                        }
                    });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
            bitmap = item.getImg().copy(item.getImg().getConfig(), true);
            bitmap = MainActivity.getResizedBitmap(bitmap, 2000, 2000);
            image.setImageBitmap(bitmap);
        }
        i++;
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