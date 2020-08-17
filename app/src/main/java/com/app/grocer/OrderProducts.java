package com.app.grocer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class OrderProducts extends AppCompatActivity {

    ArrayList<DataModel> dataModels;
    SharedPreferences lastorderpreferences;
    ShoppingCartAdapter adapter;
    ListView order_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_products);
        order_list = (ListView) findViewById(R.id.order_product_list);
        lastorderpreferences = getSharedPreferences("lastOrder", getApplicationContext().MODE_PRIVATE);
        TextView emptyMessage = (TextView) findViewById(R.id.empty_message);
        order_list.setEmptyView(emptyMessage);
        populateList();
        //Toast.makeText(this,lastorderpreferences.getString("2","null"), Toast.LENGTH_LONG).show();

    }

    //function to populate the shopping cart list
    public void populateList() {
        dataModels = new ArrayList<>();


        Map<String, ?> keys = lastorderpreferences.getAll();

        //for each item in the shopping cart
        for (final Map.Entry<String, ?> entry : keys.entrySet()) {
            //if the quantity of item is more than 0
            if (parseInt(String.valueOf(entry.getValue())) > 0) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("products");
                Query qr = myRef.orderByChild("productId").equalTo(parseInt(entry.getKey()));

                qr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            for (DataSnapshot data : snapshot.getChildren()) {
                                Products prod1 = data.getValue(Products.class);
                                dataModels.add(new DataModel(prod1.productName, prod1.productPrice, prod1.productId, parseInt((String) entry.getValue())));


                            }
                        }

                        adapter = new ShoppingCartAdapter(dataModels, getApplicationContext());
                        order_list.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

        }
    }
}