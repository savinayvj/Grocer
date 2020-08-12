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
    TextView cart_counter;
    private static CustomAdapter adapter;
    shoppingCart cart;
    AutoCompleteTextView search_bar;
    ImageView open_cart;
    ImageView user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        listView=(ListView)findViewById(R.id.list);
        cart_counter = (TextView)findViewById(R.id.cart_counter);
        final SharedPreferences sharedpreferences = getSharedPreferences("Myprefs", getApplicationContext().MODE_PRIVATE);
        cart = new shoppingCart(sharedpreferences,cart_counter);
        search_bar = (AutoCompleteTextView) findViewById(R.id.search_bar);
        open_cart = (ImageView)findViewById(R.id.shoppingCart);
        user = (ImageView) findViewById(R.id.user);
        cart.updateCartUI();
        if(cart.getItemsCount()>0){
            cart_counter.setVisibility(View.VISIBLE);
            cart_counter.setText(Integer.toString(cart.getItemsCount()));
        }

        Bundle extras = getIntent().getExtras();
        final String category = extras.getString("category");
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),userDetails.class);
                //i.putExtra("category","dental care");
                startActivity(i);

            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("products");
        Query qr = myRef.orderByChild("productCategory").equalTo(category);

        dataModels= new ArrayList<>();

        qr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    for(DataSnapshot data : snapshot.getChildren() ){

                        Products prod1 = data.getValue(Products.class);
                        dataModels.add(new DataModel(prod1.productName,prod1.productPrice,prod1.productId,prod1.productQuantity));
                        Log.d("fff",Integer.toString(prod1.productId));


                    }
                    adapter= new CustomAdapter(dataModels,getApplicationContext(),cart_counter);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
        open_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ShoppingCartActivity.class);
                startActivity(i);
            }
        });

        search_bar = (AutoCompleteTextView) findViewById(R.id.search_bar);
        //new SearchGlobal().populateList(this,search_bar);

    }
    @Override
    protected void onResume() {
        super.onResume();
        cart.updateCartUI();
    }
}