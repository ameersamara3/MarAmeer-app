package com.example.MarAmeer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class Item {
    private String id;
    private String name;
    private String category;
    private int price;
    private String desc;
    private Bitmap img=null;

    public String getImagename() {
        return imagename;
    }

    private String imagename;

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    private String ownerID;
    public String getId() {
        return id;
    }




    public Item(String id,String name, String category, int price, String desc, byte[] blob,String ownerID,String imagename) {
        this.id=id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.desc = desc;
        if(blob!=null) this.img =  BitmapFactory.decodeByteArray(blob, 0, blob.length);
        this.ownerID=ownerID;
        this.imagename=imagename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }


}
