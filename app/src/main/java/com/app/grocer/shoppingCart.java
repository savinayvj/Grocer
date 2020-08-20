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
    int total=0;
    public shoppingCart(SharedPreferences sharepref){
        sharedpreferences = sharepref;
        myEdit = sharedpreferences.edit();
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

    public void addRemoveItems(String pid, String quantity){

        int quan = Integer.parseInt(quantity);
        String prodid = pid;
        myEdit.putString(prodid,quantity);
        if(quantity=="0"){
            myEdit.remove(prodid);
        }

        myEdit.commit();

        //Update the cart UI in toolbar
        SearchBarFragment.getInstance().updateCartUI();

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








}
