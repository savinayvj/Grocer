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
    TextView cart_counter;
    ImageView open_cart;
    Button pay_button;
    TextView emptyMessage;
    TextView total_tv,delivery_tv,delivery_price;
    ImageView user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        shoppingList = (ListView) findViewById(R.id.shoppingCartList);
        total_price = (TextView) findViewById(R.id.total_price);
        sharedpreferences = getSharedPreferences("Myprefs", getApplicationContext().MODE_PRIVATE);
        cart_counter = (TextView) findViewById(R.id.cart_counter);
        total_tv = (TextView) findViewById(R.id.total);
        delivery_tv = (TextView) findViewById(R.id.delivery);
        delivery_price = (TextView) findViewById(R.id.delivery_price);
        pay_button = (Button) findViewById(R.id.pay_button);
        emptyMessage = (TextView)findViewById(R.id.empty_message);
        shoppingList.setEmptyView(findViewById(R.id.empty_message));
        user = (ImageView) findViewById(R.id.user);


        populateList();
        SharedPreferences sharedpreferences = getSharedPreferences("Myprefs", 0);
        shoppingCart cart = new shoppingCart(sharedpreferences,cart_counter);
        if(cart.getItemsCount()>0){
            total_price.setVisibility(View.VISIBLE);
            total_tv.setVisibility(View.VISIBLE);
            delivery_tv.setVisibility(View.VISIBLE);
            delivery_price.setVisibility(View.VISIBLE);
            pay_button.setVisibility(View.VISIBLE);


        }
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),userDetails.class);
                //i.putExtra("category","dental care");
                startActivity(i);

            }
        });
        cart.updateCartUI();
        open_cart = (ImageView)findViewById(R.id.shoppingCart);


        open_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ShoppingCartActivity.class);
                startActivity(i);
            }
        });
    }

    public void onPause() {

        super.onPause();
        finish();
    }



    public void populateList(){
        dataModels = new ArrayList<>();
        Map<String,?> keys = sharedpreferences.getAll();

        for(final Map.Entry<String,?> entry : keys.entrySet()){
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
                                Log.d("eee", prod1.productName);
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