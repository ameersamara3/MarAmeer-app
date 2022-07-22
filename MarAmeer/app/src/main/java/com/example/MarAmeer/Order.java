package com.example.MarAmeer;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;

public class Order {
    private String id;
    private LocalDateTime dt;
    private int totalprice;

    public Order(String id, LocalDateTime dt, int totalprice) {
        this.id = id;
        this.dt = dt;
        this.totalprice = totalprice;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dt);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getDt() {
        return dt;
    }

    public void setDt(LocalDateTime dt) {
        this.dt = dt;
    }

    public int getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice = totalprice;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public int compareTo(Order order) {


        return this.getDt().compareTo(order.getDt());


    }

    public static Comparator<Order> OrderComparator
            = new Comparator<Order>() {

        @RequiresApi(api = Build.VERSION_CODES.O)
        public int compare(Order order1, Order order2) {

            //ascending order
            return order2.compareTo(order1);

            //descending order
            //return fruitName2.compareTo(fruitName1);
        }

    };
}
