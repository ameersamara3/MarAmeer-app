package com.example.MarAmeer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.InputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity{
public  static ArrayList<Item> itemsToBuy;
public static User user=null;
public static ImageView image;
public static ProgressDialog dialog;
public static androidx.fragment.app.FragmentManager fm=null;
private TextView title;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fm=getSupportFragmentManager();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title=(TextView)findViewById(R.id.actionText);
        setActionTitle(getResources().getString(R.string.hi));
        ImageView backButton = (ImageView)this.findViewById(R.id.backicon);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        BottomNavigationView menu=(BottomNavigationView)findViewById(R.id.bottomNavigationView);
        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            //setting up menu
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getApplicationContext(), SignInUpActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.home:
                        FragmentManager fm = getFragmentManager();
                        HomePage fragment= new HomePage();
                        FragmentTransaction t= fm.beginTransaction();
                        t.replace(R.id.root_layout2, fragment);
                        t.addToBackStack(null);
                        t.commit();

                        break;
                    case R.id.cart:
                            FragmentManager fm2 = getFragmentManager();
                            Cart fragment2 = new Cart();
                            FragmentTransaction t2 = fm2.beginTransaction();
                            t2.replace(R.id.root_layout2, fragment2);
                            t2.addToBackStack(null);
                            t2.commit();
                        break;
                }
                return true;
            }
        });
        menu.setBackgroundColor(getResources().getColor(R.color.pinkNavigation));
        menu.setSelectedItemId(R.id.home);
        FragmentManager fm2 = getFragmentManager();
        HomePage fragment2= new HomePage();
        FragmentTransaction t2= fm2.beginTransaction();
        t2.replace(R.id.root_layout2, fragment2);
        t2.commit();


    }

    public void setActionTitle(String s) {
        title.setText(s);
    }

    @Override
    //for setting up
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        try {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {

                    Uri resultUri = result.getUri();
                    //.with(this).load(resultUri).into(image);

                        InputStream stream = getContentResolver().openInputStream(resultUri);

                        AddItem.bitmap = BitmapFactory.decodeStream(stream);
                        EditItem.bitmap=AddItem.bitmap;
                        Bitmap bitmap=AddItem.bitmap.copy(AddItem.bitmap.getConfig(), true);
                        //bitmap= Bitmap.createBitmap(bitmap, 0, 0, image.getWidth(), image.getHeight());
                        bitmap = getResizedBitmap(bitmap, 500, 500);
                        image.setImageBitmap(bitmap);
                    }
                 else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    //resize map
    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
    //make a normal alert with ok button
    public static void Alert(Context context, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(title);

        builder.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }
    public static void Success(View v, Activity activity) {
        Snackbar bar=Snackbar.make(v, activity.getResources().getString(R.string.complete_suc), Snackbar.LENGTH_LONG)
                .setAction(activity.getResources().getString(R.string.close), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(activity.getResources().getColor(android.R.color.white ));
        bar.setTextColor(activity.getResources().getColor(android.R.color.holo_green_light));
        bar.show();
    }
    public static void Fail(View v, Activity activity) {
        Snackbar bar=Snackbar.make(v, activity.getResources().getString(R.string.something_wrong), Snackbar.LENGTH_LONG)
                .setAction(activity.getResources().getString(R.string.close), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(activity.getResources().getColor(android.R.color.white ));
        bar.setTextColor(activity.getResources().getColor(android.R.color.holo_red_light));
        bar.show();
    }
    public static void Custom(View v, Activity activity,String message) {
        Snackbar bar=Snackbar.make(v, message, Snackbar.LENGTH_LONG)
                .setAction(activity.getResources().getString(R.string.close), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(activity.getResources().getColor(android.R.color.white ));
        bar.setTextColor(activity.getResources().getColor(android.R.color.holo_red_light));
        bar.show();
    }


}