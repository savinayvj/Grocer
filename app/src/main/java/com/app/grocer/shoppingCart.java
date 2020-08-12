package com.app.grocer;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Map;

//Shopping cart library
public class shoppingCart {
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor myEdit;
    TextView cart_counter;
    int total=0;
    public shoppingCart(SharedPreferences sharepref, TextView tv){
        sharedpreferences = sharepref;
        myEdit = sharedpreferences.edit();
        cart_counter = tv;
    }

    public boolean isItemInCart(int pid){
        String prodid = Integer.toString(pid);
        if(sharedpreferences.contains(prodid)){
            return true;
        }
        else {
            return false;
        }
    }

    public void addRemoveItems(int pid, String quantity){

        int quan = Integer.parseInt(quantity);
        String prodid = Integer.toString(pid);
        myEdit.putString(prodid,quantity);
        if(quantity=="0"){
            myEdit.remove(prodid);
        }

        myEdit.commit();


    }


    public String getQuantity(int pid){
        String prodid = Integer.toString(pid);
        return sharedpreferences.getString(prodid,"");
    }

    public int getItemsCount(){
        int count = 0;
        for (Map.Entry<String, ?> entry: sharedpreferences.getAll().entrySet()) {
            if (Integer.parseInt(String.valueOf(entry.getValue()))>0) {
                count++;
            }

        }
        return count;
    }


    public void updateCartUI(){
        Log.d("ddd",Integer.toString(getItemsCount()));
        if(getItemsCount()>0){
            cart_counter.setVisibility(View.VISIBLE);
            cart_counter.setText(Integer.toString(getItemsCount()));
        }else{
            cart_counter.setVisibility(View.INVISIBLE);

        }


    }





}
