package com.app.grocer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.rtp.AudioCodec;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

public class ItemDetail extends AppCompatActivity {

    TextView item_name_detailed;
    ImageView item_image_detailed;
    TextView item_price_detailed;
    ImageView item_add,item_remove;
    TextView item_count;
    Button addToCart;
    shoppingCart cart;
    TextView item_desc;
    Button removeToCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        item_name_detailed = (TextView)findViewById(R.id.item_name_detailed);
        item_image_detailed = (ImageView) findViewById(R.id.item_image_detailed);
        item_price_detailed = (TextView)findViewById(R.id.item_price_detailed);
        item_add = (ImageView) findViewById(R.id.item_add);
        item_remove = (ImageView) findViewById(R.id.item_delete);
        item_count = (TextView) findViewById(R.id.item_count);
        addToCart = (Button) findViewById(R.id.addtocart_button);
        item_desc = (TextView)findViewById(R.id.item_desc_detailed);
        removeToCart = (Button) findViewById(R.id.remove_button);
        Bundle extras = getIntent().getExtras();
        final int id = extras.getInt("id");

        //get the quantity of item (if it is in cart). If quantity > 0, show "remove from cart" button, else No.
        SharedPreferences sharedpreferences = getSharedPreferences("Myprefs", 0);
        int quantity = Integer.parseInt(sharedpreferences.getString(Integer.toString(id),"0"));
        if(quantity>0){
            removeToCart.setVisibility(View.VISIBLE);
        }

        //initialize shopping cart
        cart = new shoppingCart(sharedpreferences);

        //connect to Firebase Database to to get details about this item.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("products");
        Query qr = myRef.orderByChild("productId").equalTo(id);
        qr.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot data : snapshot.getChildren() ){

                    Products prod1 = data.getValue(Products.class);
                    item_name_detailed.setText(prod1.productName);
                    item_price_detailed.setText("Rs." + prod1.productPrice);
                    item_desc.setText(prod1.productDescription);
                    //if no stocks, hide the add to cart options
                    if(prod1.productQuantity<=0){
                        addToCart.setVisibility(View.INVISIBLE);
                        item_add.setVisibility(View.INVISIBLE);
                        item_remove.setVisibility(View.INVISIBLE);
                        item_count.setVisibility(View.INVISIBLE);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // add quantity button (+)
        item_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int count = Integer.valueOf(item_count.getText().toString()) +1;
                item_count.setText(Integer.toString(count));

            }



        });

        // remove quantity button (-)
        item_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.valueOf(item_count.getText().toString()) -1;
                if(count>=0) {
                    item_count.setText(Integer.toString(count));
                }
            }
        });


        //Get Image for the product from Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("productImages/" + id + ".jpg");
        GlideApp.with(item_image_detailed.getContext())
                .load(storageRef)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(item_image_detailed);

        //add the selected quantity to cart and update cart icon
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart.addRemoveItems(id,item_count.getText().toString());
            }
        });

        //remove the selected quantity to cart and update cart icon
        removeToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart.addRemoveItems(id,"0");
            }
        });


    }

}