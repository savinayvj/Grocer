package com.app.grocer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class items extends AppCompatActivity {
    ArrayList<DataModel> dataModels;
    ListView listView;
    private static CustomAdapter adapter;
    shoppingCart cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        listView=(ListView)findViewById(R.id.list);

        //updating shopping cart counter/UI
        final SharedPreferences sharedpreferences = getSharedPreferences("Myprefs", getApplicationContext().MODE_PRIVATE);
        cart = new shoppingCart(sharedpreferences);

        //getting the category selected for the items
        Bundle extras = getIntent().getExtras();
        final String category = extras.getString("category");

        //querying Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("products");
        Query qr = myRef.orderByChild("productCategory").equalTo(category);

        dataModels= new ArrayList<>();

        qr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    for(DataSnapshot data : snapshot.getChildren() ){
                        //retrieve all products satisfying the category and add to the list
                        Products prod1 = data.getValue(Products.class);
                        dataModels.add(new DataModel(prod1.productName,prod1.productPrice,prod1.productId,prod1.productQuantity));
                        Log.d("fff",Integer.toString(prod1.productId));


                    }
                    //insert datalist in the ListView
                    adapter= new CustomAdapter(dataModels,getApplicationContext());
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

    }

}