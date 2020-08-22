package com.app.grocer;

import android.view.View;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

public class FirebaseHelper {
    FirebaseDatabase database;

    public FirebaseHelper(){
        database = FirebaseDatabase.getInstance();

    }

    public void getItemsByCategory(String category, final Callback callback){
        DatabaseReference myRef = database.getReference("products");
        Query qr = myRef.orderByChild("productCategory").equalTo(category);
        qr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            callback.onSuccessCallback(snapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }

    public void getItemById(String itemId, final Callback callback){
        DatabaseReference myRef = database.getReference("products");
        Query qr = myRef.orderByChild("productId").equalTo(itemId);
        qr.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onSuccessCallback(snapshot);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void getAllItemNames(final Callback callback){
        DatabaseReference myRef = database.getReference("products");
        Query qr = myRef.orderByChild("productName");
        qr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onSuccessCallback(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void inserOrder(String name, String address, String phone, ArrayList<Products> productList,ArrayList<Integer> quantites){
        DatabaseReference myRef = database.getReference("orders/");
        int id = (int) (Math.random() * ((Integer.MAX_VALUE-1)-0 + 1) + 0);
        myRef.push().setValue(new Orders(id, name, address, phone, productList, quantites));
    }
}
