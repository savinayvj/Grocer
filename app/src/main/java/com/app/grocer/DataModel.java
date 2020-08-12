package com.app.grocer;

//Data Model for items List
public class DataModel {

    int item_id;
    String item_name;
    String item_price;
    int item_quantity;


    public DataModel(String name, String price,int id , int quantity) {
        item_name = name;
        item_price = price;
        item_id = id;
        item_quantity = quantity;

    }

    public String getName() {
        return item_name;
    }

    public String getPrice() {
        return item_price;
    }

    public int getId(){return item_id;}

    public int getQuantity(){return item_quantity;}


}
