package com.app.grocer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class ShoppingCartActivity extends AppCompatActivity {
    ListView shoppingList;
    ShoppingCartAdapter adapter;
    int total = 0;
    TextView total_price;
    SharedPreferences sharedpreferences;
    ArrayList<DataModel> dataModels;
    Button pay_button;
    TextView emptyMessage;
    TextView total_tv,delivery_tv,delivery_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        //set empty message if the shopping cart is empty
        shoppingList = (ListView) findViewById(R.id.shoppingCartList);
        emptyMessage = (TextView)findViewById(R.id.empty_message);
        shoppingList.setEmptyView(findViewById(R.id.empty_message));

        //shopping cart UI update on Activity onset
        sharedpreferences = getSharedPreferences("Myprefs", getApplicationContext().MODE_PRIVATE);
        shoppingCart cart = new shoppingCart(sharedpreferences);

        //populate shopping list
        populateList();

        //if no Item in cart remove Pay button and all charges
        total_tv = (TextView) findViewById(R.id.total);
        total_price = (TextView) findViewById(R.id.total_price);
        delivery_tv = (TextView) findViewById(R.id.delivery);
        delivery_price = (TextView) findViewById(R.id.delivery_price);
        pay_button = (Button) findViewById(R.id.pay_button);
        if(cart.getItemsCount()>0){
            total_price.setVisibility(View.VISIBLE);
            total_tv.setVisibility(View.VISIBLE);
            delivery_tv.setVisibility(View.VISIBLE);
            delivery_price.setVisibility(View.VISIBLE);
            pay_button.setVisibility(View.VISIBLE);
        }


    }

    //kill the activity when it in background (so that it restarts everytime it opens and cart remains updated)
    public void onPause() {
        super.onPause();
        finish();
    }

    //function to populate the shopping cart list
    public void populateList(){
        dataModels = new ArrayList<>();
        Map<String,?> keys = sharedpreferences.getAll();

        //for each item in the shopping cart
        for(final Map.Entry<String,?> entry : keys.entrySet()){
            //if the quantity of item is more than 0
            if(parseInt(String.valueOf(entry.getValue()))>0) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("products");
                Query qr = myRef.orderByChild("productId").equalTo(parseInt(entry.getKey()));

                qr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            for(DataSnapshot data : snapshot.getChildren()) {
                                Products prod1 = data.getValue(Products.class);
                                //add price * quantity to total
                                total += parseInt(prod1.productPrice) * parseInt((String) entry.getValue());
                                dataModels.add(new DataModel(prod1.productName, prod1.productPrice, prod1.productId, parseInt((String) entry.getValue())));



                            }
                        }

                        adapter = new ShoppingCartAdapter(dataModels,getApplicationContext());
                        shoppingList.setAdapter(adapter);
                        total_price.setText(Integer.toString(total));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }




        }

    }
}