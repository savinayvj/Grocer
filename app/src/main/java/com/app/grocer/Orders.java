package com.app.grocer;

import java.util.ArrayList;

public class Orders {
    public int id;
    public String name;
    public String address;
    public String phone;
    public ArrayList<Products> products;
    public ArrayList<Integer> quantities;

    public Orders(){}

    public Orders(int oid,String oname, String oaddress, String ophone, ArrayList<Products> oproducts,ArrayList<Integer> oquantities){
        id = oid;
        name = oname;
        address = oaddress;
        phone = ophone;
        products = oproducts;
        quantities = oquantities;
    }
}
