package com.example.MarAmeer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
/*
this is the firebase class that is responsible of everything related to firebase
every function does like it's name suggests
some of the function has a while loop, this has been done to
 avoid problems related to oncomplete / onfailure functions
*/
public class FireBase {
    private FirebaseAuth mAuth;
    private Activity activity;
    private FirebaseFirestore db;
    private User user;
    private ArrayList<Item> itemsList;
    private byte[] img;
    public FireBase(Activity activity) {
        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
        this.activity = activity;
    }
    public static boolean isNetworkAvailable(Context con) {
        ConnectivityManager cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            NetworkCapabilities cap = cm.getNetworkCapabilities(cm.getActiveNetwork());
            if (cap == null) return false;
            return cap.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = cm.getAllNetworks();
            for (Network n: networks) {
                NetworkInfo nInfo = cm.getNetworkInfo(n);
                if (nInfo != null && nInfo.isConnected()) return true;
            }
        } else {
            NetworkInfo[] networks = cm.getAllNetworkInfo();
            for (NetworkInfo nInfo: networks) {
                if (nInfo != null && nInfo.isConnected()) return true;
            }
        }

        return false;
    }
    public void register(String email, String password, String first, String last,View v) {
        if(!isNetworkAvailable(activity)){
            MainActivity.Alert(activity,activity.getResources().getString(R.string.error) ,activity.getResources().getString(R.string.no_net));
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            addUser(email, first, last);
                            // Sign in success, update UI with the signed-in user's information
                            MainActivity.Success(v,activity);
                        } else {
                            // If sign in fails, display a message to the user.
                            MainActivity.Custom(v,activity,task.getException().getMessage());

                        }
                    }
                });

    }

    public void SignIn(String email, String password) {
        if(!isNetworkAvailable(activity)){
            MainActivity.Alert(activity,activity.getResources().getString(R.string.error) ,activity.getResources().getString(R.string.no_net));
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            setMainUser(email);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(activity, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    public void addUser(String email, String first, String last) {
        if(!isNetworkAvailable(activity)){
            MainActivity.Alert(activity,activity.getResources().getString(R.string.error) ,activity.getResources().getString(R.string.no_net));

            return;
        }
        Map<String, Object> user = new HashMap<>();
        user.put("First Name", first);
        user.put("Last Name", last);
        db.collection("Users").document(email).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        setMainUser(email);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

    }

    public boolean addItem(String name, String category, String price, String desc, byte[] img, String ownerID) {
        if(!isNetworkAvailable(activity)){
            MainActivity.Alert(activity,activity.getResources().getString(R.string.error) ,activity.getResources().getString(R.string.no_net));

            return false;
        }
        Map<String, Object> item = new HashMap<>();
        item.put("name", name);
        item.put("category", category);
        item.put("price", price);
        item.put("desc", desc);
        item.put("imageName", uploadImage(img));
        item.put("owner", ownerID);
        item.put("Status", "UNSOLD");
        Task task=db.collection("Items").add(item);
        int c=0;
        while(!task.isSuccessful() && !task.isComplete() && !task.isCanceled()){
            try {
                Thread.sleep(100);
                c++;
                if(c>100){
                    MainActivity.Alert(activity,activity.getResources().getString(R.string.error),activity.getResources().getString(R.string.server_error));
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return task.isSuccessful();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean addOrder(String city, String mobile, String address, String zip, String comment, ArrayList<Item> al, String email, int price) {
        if(!isNetworkAvailable(activity)){
            MainActivity.Alert(activity,activity.getResources().getString(R.string.error) ,activity.getResources().getString(R.string.no_net));
            MainActivity.dialog.dismiss();
            return false;
        }
        Map<String, Object> order = new HashMap<>();
        order.put("city", city);
        order.put("mobile", mobile);
        order.put("address", address);
        order.put("zip", zip);
        order.put("comment", comment);
        order.put("orderedBy", email);
        order.put("price", price);
        order.put("date", LocalDateTime.now());

        Task task=db.collection("Orders").add(order)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        for(Item t:al){
                            addItemToOrder(t.getId(),documentReference.getId());
                            deleteCartItem(t.getId(), MainActivity.user.getUsername());
                            MarkItemAsSold(t.getId());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
        int c=0;
        while (!task.isSuccessful() && !task.isComplete() && !task.isCanceled()) {
            try {
                Thread.sleep(10);
                c++;
                if(c>500){
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        return task.isSuccessful();
    }

    private boolean addItemToOrder(String itemid,String orderid) {
        if(!isNetworkAvailable(activity)){
            MainActivity.Alert(activity,activity.getResources().getString(R.string.error) ,activity.getResources().getString(R.string.no_net));

            return false;
        }
        Map<String, Object> item = new HashMap<>();
        item.put("itemID", itemid);
        Task task=db.collection("Orders").document(orderid).collection("items").document(itemid).set(item);
        int c=0;
        while(!task.isSuccessful() && !task.isComplete() && !task.isCanceled()){
            try {
                Thread.sleep(100);
                c++;
                if(c>100){
                    MainActivity.Alert(activity,activity.getResources().getString(R.string.error),activity.getResources().getString(R.string.server_error));
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return task.isSuccessful();
    }

    public boolean addItemToCart(String user, String itemID) {
        if(!isNetworkAvailable(activity)){
            MainActivity.Alert(activity,activity.getResources().getString(R.string.error) ,activity.getResources().getString(R.string.no_net));

            return false;
        }
        Map<String, Object> cart = new HashMap<>();
        cart.put("itemID", itemID);
        Task task=db.collection("Users").document(user).collection("cart").document(itemID).set(cart);
        int c=0;
        while(!task.isSuccessful() && !task.isComplete() && !task.isCanceled()){
            try {
                Thread.sleep(100);
                c++;
                if(c>100){
                    MainActivity.Alert(activity,activity.getResources().getString(R.string.error),activity.getResources().getString(R.string.server_error));
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return task.isSuccessful();

    }
    public boolean deleteCartItem(String itemID, String user) {
        if(!isNetworkAvailable(activity)){
            MainActivity.Alert(activity,activity.getResources().getString(R.string.error) ,activity.getResources().getString(R.string.no_net));

            return false;
        }
        Log.w("TAG", "itemID "+itemID);
        Task task=db.collection("Users").document(user).collection("cart").document(itemID).delete();
        int c=0;
        while(!task.isSuccessful() && !task.isComplete() && !task.isCanceled()){
            try {
                Thread.sleep(100);
                c++;
                if(c>100){
                    MainActivity.Alert(activity,activity.getResources().getString(R.string.error),activity.getResources().getString(R.string.server_error));
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return task.isSuccessful();

    }
    public boolean deleteImage(String id){
        if(!isNetworkAvailable(activity)){
            MainActivity.Alert(activity,activity.getResources().getString(R.string.error) ,activity.getResources().getString(R.string.no_net));

            return false;
        }
        String imagename=getItemById(id,false).getImagename();
        Log.d("imagename", "imagename"+ imagename);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child(imagename);
        Task task=imageRef.delete();
        int c=0;
        while (!task.isSuccessful() && !task.isComplete() && !task.isCanceled()) {
            try {
                Thread.sleep(10);
                c++;
                if(c>500){
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return task.isSuccessful();
    }
    public boolean deleteItem(String id) {
        if(!isNetworkAvailable(activity)){
            MainActivity.Alert(activity,activity.getResources().getString(R.string.error) ,activity.getResources().getString(R.string.no_net));

            return false;
        }
        deleteImage(id);
        Task task = db.collection("Items").document(id).delete();
        int c=0;
        while(!task.isSuccessful() && !task.isComplete() && !task.isCanceled()){
            try {
                Thread.sleep(100);
                c++;
                if(c>100){
                    MainActivity.Alert(activity,activity.getResources().getString(R.string.error),activity.getResources().getString(R.string.server_error));
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return task.isSuccessful() && deleteItemFromCarts(id);
    }
    public String uploadImage(byte[] img) {
        if(!isNetworkAvailable(activity)){
            MainActivity.Alert(activity,activity.getResources().getString(R.string.error) ,activity.getResources().getString(R.string.no_net));

            return null;
        }
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final String imageName = generateRandomPassword(15) + ".jpg";
        StorageReference imageRef = storageRef.child(imageName);
        UploadTask uploadTask = imageRef.putBytes(img);
        int c=0;
        while(!uploadTask.isSuccessful() && !uploadTask.isComplete() && !uploadTask.isCanceled()){
            try {
                Thread.sleep(10);
                c++;
                if(c>500){
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return imageName;
    }

    public static String generateRandomPassword(int len) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }

    public void setMainUser(String email) {
        if(!isNetworkAvailable(activity)){
            MainActivity.Alert(activity,activity.getResources().getString(R.string.error) ,activity.getResources().getString(R.string.no_net));
            return;
        }

        DocumentReference docRef = db.collection("Users").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> mp = document.getData();
                        MainActivity.user = new User(email, (String) mp.get("First Name"), (String) mp.get("Last Name"));
                        Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
                        activity.startActivity(intent);
                    }
                }
            }
        });

    }
        public void getMyItems (String email,ManageItems manageItems,View v){
            if(!isNetworkAvailable(activity)){
                MainActivity.dialog.dismiss();
                MainActivity.Alert(activity,activity.getResources().getString(R.string.error) ,activity.getResources().getString(R.string.no_net));
                return;
            }
            Task<QuerySnapshot> task = db.collection("Items").whereEqualTo("owner", email).whereEqualTo("Status", "UNSOLD").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            ArrayList<Item> itemsList = new ArrayList<Item>();
                            if(task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String, Object> mp = document.getData();
                                    byte[] image = DownloadImg((String) mp.get("imageName"));
                                    Item item = new Item(document.getId(),(String) mp.get("name"), (String) mp.get("category"), Integer.parseInt(String.valueOf(mp.get("price"))), (String) mp.get("desc"),
                                            image, (String) mp.get("owner"),(String) mp.get("imageName"));
                                    itemsList.add(item);
                                }
                                MainActivity.dialog.dismiss();
                                manageItems.setupAdapter(itemsList);
                            }
                            else MainActivity.Fail(v,manageItems.getActivity());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    MainActivity.Fail(v,manageItems.getActivity());                }
            });
        }
    public void getMySoldItems (String email,SoldPage manageItems,View v){
        if(!isNetworkAvailable(activity)){
            MainActivity.dialog.dismiss();
            MainActivity.Alert(activity,activity.getResources().getString(R.string.error) ,activity.getResources().getString(R.string.no_net));
            return;
        }
        Task<QuerySnapshot> task = db.collection("Items").whereEqualTo("owner", email).whereEqualTo("Status", "SOLD").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Item> itemsList = new ArrayList<Item>();
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> mp = document.getData();
                                byte[] image = DownloadImg((String) mp.get("imageName"));
                                Item item = new Item(document.getId(),(String) mp.get("name"), (String) mp.get("category"), Integer.parseInt(String.valueOf(mp.get("price"))), (String) mp.get("desc"),
                                        image, (String) mp.get("owner"),(String) mp.get("imageName"));
                                itemsList.add(item);
                            }
                            MainActivity.dialog.dismiss();
                            manageItems.setupAdapter(itemsList);
                        }
                        else MainActivity.Fail(v,manageItems.getActivity());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        MainActivity.Fail(v,manageItems.getActivity());                }
                });
    }


    public void getItems (String category,CategoryItems categoryItems){
        if(!isNetworkAvailable(activity)){
            MainActivity.dialog.dismiss();
            MainActivity.Alert(activity,activity.getResources().getString(R.string.error) ,activity.getResources().getString(R.string.no_net));

            return;
        }
        Task<QuerySnapshot> task = db.collection("Items").whereEqualTo("category", category).whereEqualTo("Status", "UNSOLD").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        itemsList = new ArrayList<Item>();
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> mp = document.getData();
                                byte[] image = DownloadImg((String) mp.get("imageName"));
                                Item item = new Item(document.getId(),(String) mp.get("name"), (String) mp.get("category"), Integer.parseInt(String.valueOf(mp.get("price"))), (String) mp.get("desc"),
                                        image, (String) mp.get("owner"),(String) mp.get("imageName"));
                                itemsList.add(item);
                            }
                            MainActivity.dialog.dismiss();
                            categoryItems.setupAdapter(itemsList);
                        } else MainActivity.Fail((View)categoryItems.getView().findViewById(R.id.layout),categoryItems.getActivity());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        MainActivity.Fail((View)categoryItems.getView().findViewById(R.id.layout),categoryItems.getActivity());
                    }
                });

    }
        private byte[] DownloadImg (String imageName) {
            if(!isNetworkAvailable(activity)){
                MainActivity.Alert(activity,activity.getResources().getString(R.string.error) ,activity.getResources().getString(R.string.no_net));

                return null;
            }
            img = null;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference islandRef = storageRef.child(imageName);
        final long ONE_MEGABYTE = 1024*1024;
        Task task=islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                img = bytes;

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
            int c=0;
            while(!task.isSuccessful() && !task.isComplete() && !task.isCanceled()){
                try {
                    Thread.sleep(100);
                    c++;
                    if(c>100){
                        MainActivity.Alert(activity,activity.getResources().getString(R.string.error),activity.getResources().getString(R.string.server_error));
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        img=(byte[]) task.getResult();
        return img;
        }

    public Item getItemById(String id, boolean withImage) {
        if(!isNetworkAvailable(activity)){
            MainActivity.dialog.dismiss();
            MainActivity.Alert(activity,activity.getResources().getString(R.string.error) ,activity.getResources().getString(R.string.no_net));

            return null;
        }
        Task<DocumentSnapshot> task = db.collection("Items").document(id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                 }
        }});
        int c=0;
        while(!task.isSuccessful() && !task.isComplete() && !task.isCanceled()){
            try {
                Thread.sleep(100);
                c++;
                if(c>100){
                    MainActivity.Alert(activity,activity.getResources().getString(R.string.error),activity.getResources().getString(R.string.server_error));
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(task.isSuccessful()){
            if(withImage) {
                Map<String, Object> mp = task.getResult().getData();
                byte[] image = DownloadImg((String) mp.get("imageName"));
                Item item = new Item(task.getResult().getId(), (String) mp.get("name"), (String) mp.get("category"), Integer.parseInt(String.valueOf(mp.get("price"))), (String) mp.get("desc"),
                        image, (String) mp.get("owner"), (String) mp.get("imageName"));
                return item;
            }
            else{
                Map<String, Object> mp = task.getResult().getData();
                Item item = new Item(task.getResult().getId(), (String) mp.get("name"), (String) mp.get("category"), Integer.parseInt(String.valueOf(mp.get("price"))), (String) mp.get("desc"),
                        null, (String) mp.get("owner"), (String) mp.get("imageName"));
                return item;
            }
        }
        return null;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean MarkItemAsSold(String id){
        if(!isNetworkAvailable(activity)){
            MainActivity.Alert(activity,activity.getResources().getString(R.string.error) ,activity.getResources().getString(R.string.no_net));

            return false;
        }
        DocumentReference ref = db.collection("Items").document(id);
        Map<String,Object> updates = new HashMap<>();
        updates.put("Status","SOLD");
        updates.put("SoldDate", LocalDateTime.now());
        Task task=ref.update(updates);
        int c=0;
        while(!task.isSuccessful() && !task.isComplete() && !task.isCanceled()){
            try {
                Thread.sleep(100);
                c++;
                if(c>100){
                    MainActivity.Alert(activity,activity.getResources().getString(R.string.error),activity.getResources().getString(R.string.server_error));
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return task.isSuccessful() && deleteItemFromCarts(id);
    }
    public boolean edititem(String id, String name, String category, String price, String desc, byte[] img, String ownerID){
        if(!isNetworkAvailable(activity)){
            MainActivity.Alert(activity,activity.getResources().getString(R.string.error) ,activity.getResources().getString(R.string.no_net));

            return false;
        }
        DocumentReference ref = db.collection("Items").document(id);
        Log.d("TAG", "deleted image "+  deleteImage(id));
        Map<String,Object> updates = new HashMap<>();
        updates.put("name", name);
        updates.put("category", category);
        updates.put("price", price);
        updates.put("desc", desc);
        updates.put("imageName", uploadImage(img));
        updates.put("owner", ownerID);
        Log.d("TAG", "image uploaded ");
        Task task=ref.update(updates);
        int c=0;
        while(!task.isSuccessful() && !task.isComplete() && !task.isCanceled()){
            try {
                Thread.sleep(100);
                c++;
                if(c>100){
                    MainActivity.Alert(activity,activity.getResources().getString(R.string.error),activity.getResources().getString(R.string.server_error));
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return task.isSuccessful();
    }

    public boolean checkIfMyItem(String username, String id) {
        return getItemById(id,false).getOwnerID().equals(username);
    }

    public void getMyCart(String username,Cart cart) {
        if (!isNetworkAvailable(activity)) {
            MainActivity.dialog.dismiss();
            MainActivity.Alert(activity,activity.getResources().getString(R.string.error) ,activity.getResources().getString(R.string.no_net));

            return ;
        }

        Task<QuerySnapshot> task = db.collection("Users").document(username).collection("cart").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        itemsList = new ArrayList<Item>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                itemsList.add(getItemById(document.getId(), true));
                            }
                            cart.setupAdapter(itemsList);
                            MainActivity.dialog.dismiss();
                        } else  MainActivity.Fail((View)cart.getView().findViewById(R.id.layout),cart.getActivity());

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        MainActivity.Fail((View)cart.getView().findViewById(R.id.layout),cart.getActivity());
                    }
                });
    }
    public void getMyCart2(String username,paymentPage p) {
        if (!isNetworkAvailable(activity)) {
            MainActivity.Alert(activity,activity.getResources().getString(R.string.error) ,activity.getResources().getString(R.string.no_net));

            return ;
        }

        Task<QuerySnapshot> task = db.collection("Users").document(username).collection("cart").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        itemsList = new ArrayList<Item>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                itemsList.add(getItemById(document.getId(), false));
                            }
                            p.handleOrder(itemsList);
                            MainActivity.dialog.dismiss();
                        } else  MainActivity.Fail((View)p.getView().findViewById(R.id.layout),p.getActivity());

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        MainActivity.Fail((View)p.getView().findViewById(R.id.layout),p.getActivity());
                    }
                });
    }

    public boolean updateBank(String email,String accountOwner, String accountNumber, String branchNumber, String bankNumber) {
        if(!isNetworkAvailable(activity)){
            MainActivity.Alert(activity,activity.getResources().getString(R.string.error) ,activity.getResources().getString(R.string.no_net));

            return false;
        }
        DocumentReference ref = db.collection("Users").document(email);
        Map<String,Object> updates = new HashMap<>();
        updates.put("accountOwner", accountOwner);
        updates.put("accountNumber", accountNumber);
        updates.put("branchNumber", branchNumber);
        updates.put("bankNumber", bankNumber);
        Task task=ref.update(updates);
        int c=0;
        while(!task.isSuccessful() && !task.isComplete() && !task.isCanceled()){
            try {
                Thread.sleep(100);
                c++;
                if(c>100){
                    MainActivity.Alert(activity,activity.getResources().getString(R.string.error),activity.getResources().getString(R.string.server_error));
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return task.isSuccessful();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Order> getOrders(String username) {
        if(!isNetworkAvailable(activity)){
            MainActivity.Alert(activity,activity.getResources().getString(R.string.error) ,activity.getResources().getString(R.string.no_net));
            return null;
        }
        ArrayList<Order> ordersList = new ArrayList<Order>();
            Task<QuerySnapshot> task = db.collection("Orders").whereEqualTo("orderedBy", username).get();
        int c=0;
        while(!task.isSuccessful() && !task.isComplete() && !task.isCanceled()){
            try {
                Thread.sleep(100);
                c++;
                if(c>100){
                    MainActivity.Alert(activity,activity.getResources().getString(R.string.error),activity.getResources().getString(R.string.server_error));
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> mp = document.getData();
                    Map<String, Long> date = (Map) mp.get("date");
                    LocalDateTime dateTime=LocalDateTime.of(date.get("year").intValue(),date.get("monthValue").intValue(),date.get("dayOfMonth").intValue(),date.get("hour").intValue(),date.get("minute").intValue());//gets specific LocalDateTime
                    Order order = new Order(document.getId(),dateTime,  Integer.parseInt(String.valueOf(mp.get("price"))));
                    ordersList.add(order);
                }
            }
            ordersList.sort(Order.OrderComparator);//i couldn't orderby date as a firebase query
            return ordersList;
        }

    public void getOrderItems(String id,OrderItems oi) {
        if(!isNetworkAvailable(activity)){
            MainActivity.dialog.dismiss();
            MainActivity.Alert(activity,activity.getResources().getString(R.string.error) ,activity.getResources().getString(R.string.no_net));
            return ;
        }
        itemsList = new ArrayList<Item>();
        Task<QuerySnapshot> task = db.collection("Orders").document(id).collection("items").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    itemsList.add(getItemById(document.getId(), true));
                                }
                            MainActivity.dialog.dismiss();
                            oi.setupAdapter(itemsList);
                            }
                        else MainActivity.Fail((View)oi.getView().findViewById(R.id.layout),oi.getActivity());
                    }
                });

    }

    public boolean addRating(String itemID, String ownerID,float rating) {
        if(!isNetworkAvailable(activity)){
            MainActivity.Alert(activity,activity.getResources().getString(R.string.error) ,activity.getResources().getString(R.string.no_net));
            return false;
        }
        Map<String, Object> rate = new HashMap<>();
        rate.put("Rating", rating);
        Task task=db.collection("Users").document(ownerID).collection("ratings").document(itemID).set(rate);
        int c=0;
        while(!task.isSuccessful() && !task.isComplete() && !task.isCanceled()){
            try {
                Thread.sleep(100);
                c++;
                if(c>100){
                    MainActivity.Alert(activity,activity.getResources().getString(R.string.error),activity.getResources().getString(R.string.server_error));
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return task.isSuccessful();
    }
    public boolean deleteRating(String itemID, String ownerID) {
        if(!isNetworkAvailable(activity)){
            MainActivity.Alert(activity,activity.getResources().getString(R.string.error) ,activity.getResources().getString(R.string.no_net));

            return false;
        }
        Task task=db.collection("Users").document(ownerID).collection("ratings").document(itemID).delete();
        int c=0;
        while(!task.isSuccessful() && !task.isComplete() && !task.isCanceled()){
            try {
                Thread.sleep(100);
                c++;
                if(c>100){
                    MainActivity.Alert(activity,activity.getResources().getString(R.string.error),activity.getResources().getString(R.string.server_error));
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return task.isSuccessful();
    }
    public float getAverageRating(String ownerID) {
        if(!isNetworkAvailable(activity)){
            MainActivity.Alert(activity,activity.getResources().getString(R.string.error) ,activity.getResources().getString(R.string.no_net));

            return -1;
        }
        Task<QuerySnapshot> task = db.collection("Users").document(ownerID).collection("ratings").get();
        int c=0;
        while(!task.isSuccessful() && !task.isComplete() && !task.isCanceled()){
            try {
                Thread.sleep(100);
                c++;
                if(c>100){
                    MainActivity.Alert(activity,activity.getResources().getString(R.string.error),activity.getResources().getString(R.string.server_error));
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int i=0;
        float sum=0;
        if(task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
                i++;
                Number n=(Number)document.getData().get("Rating");
                sum+=n.floatValue();
            }
        }
        if(i==0){
            return -1;
        }
        return sum/i;
    }

    public float alreadyRated(String id, String ownerID) {
        if(!isNetworkAvailable(activity)){
            MainActivity.Alert(activity,activity.getResources().getString(R.string.error) ,activity.getResources().getString(R.string.no_net));

        }
        itemsList = new ArrayList<Item>();
        Task<DocumentSnapshot> task = db.collection("Users").document(ownerID).collection("ratings").document(id).get();
        int c=0;
        while(!task.isSuccessful() && !task.isComplete() && !task.isCanceled()){
            try {
                Thread.sleep(100);
                c++;
                if(c>100){
                    MainActivity.Alert(activity,activity.getResources().getString(R.string.error),activity.getResources().getString(R.string.server_error));
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        float res=-1;
        if(task.isSuccessful()) {
            DocumentSnapshot document = task.getResult();
            if(document.exists()){
                Number n=(Number)document.get("Rating");
                res=n.floatValue();
            }
        }
        return res;
    }
    public boolean deleteItemFromCarts(String itemID) {
        if(!isNetworkAvailable(activity)){
            MainActivity.Alert(activity,activity.getResources().getString(R.string.error) ,activity.getResources().getString(R.string.no_net));

            return false;
        }
        Task<QuerySnapshot> task = db.collection("Users").get();
        int c=0;
        while(!task.isSuccessful() && !task.isComplete() && !task.isCanceled()){
            try {
                Thread.sleep(100);
                c++;
                if(c>100){
                    MainActivity.Alert(activity,activity.getResources().getString(R.string.error),activity.getResources().getString(R.string.server_error));
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        boolean flag=true;
        if(task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
                if(!deleteCartItem(itemID,document.getId())){
                    flag=false;
                }
            }
        }
        return flag;
    }
}
