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
    TextView cart_counter;
    Button addToCart;
    shoppingCart cart;
    ImageView user;
    TextView item_desc;
    Button removeToCart;
    ImageView open_cart;
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
        user = (ImageView) findViewById(R.id.user);

        addToCart = (Button) findViewById(R.id.addtocart_button);
        item_desc = (TextView)findViewById(R.id.item_desc_detailed);
        removeToCart = (Button) findViewById(R.id.remove_button);
        open_cart = (ImageView)findViewById(R.id.shoppingCart);
        Bundle extras = getIntent().getExtras();
        final int id = extras.getInt("id");

        //Update shopping cart icon when the Activity starts
        SharedPreferences sharedpreferences = getSharedPreferences("Myprefs", 0);
        cart_counter = (TextView) findViewById(R.id.cart_counter);
        cart = new shoppingCart(sharedpreferences,cart_counter);
        cart.updateCartUI();

        //populate searchbar and activate search
        AutoCompleteTextView search_bar = (AutoCompleteTextView) findViewById(R.id.search_bar);
        search_bar = (AutoCompleteTextView) findViewById(R.id.search_bar);
        new SearchGlobal().populateList(this,search_bar);

        //get the quantity of item (if it is in cart). If quantity > 0, show "remove from cart" button, else No.
        int quantity = Integer.parseInt(sharedpreferences.getString(Integer.toString(id),"0"));
        if(quantity>0){
            removeToCart.setVisibility(View.VISIBLE);
        }

        //Open cart button to start the ShoppingCartActivity
        open_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ShoppingCartActivity.class);
                startActivity(i);
            }
        });

        //user button to start userDetails Activity
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),userDetails.class);
                //i.putExtra("category","dental care");
                startActivity(i);

            }
        });

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
                    //cart.addRemoveItems(dataModel.item_id,viewHolder.item_count.getText().toString());
                    //cart.updateCartUI();
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
                cart.updateCartUI();
            }
        });

        //remove the selected quantity to cart and update cart icon
        removeToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart.addRemoveItems(id,"0");
                cart.updateCartUI();
            }
        });







    }
    //update cart UI everytime the activity comes in foreground.
    @Override
    protected void onResume() {
        super.onResume();
        cart.updateCartUI();
    }
}