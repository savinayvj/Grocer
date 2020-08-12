package com.app.grocer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.util.DisplayMetrics;
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

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    CarouselView carouselView;
    ImageView catBread;
    ImageView catDentalCare;
    ImageView user;
    TextView cart_counter;
    shoppingCart cart;
    AutoCompleteTextView search_bar;
    ImageView open_cart;
    int[] sampleImages = {R.drawable.banner1, R.drawable.banner2};

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setLocale();
        setContentView(R.layout.activity_main);

        catBread = (ImageView) findViewById(R.id.cat_bread);
        catDentalCare = (ImageView)findViewById(R.id.cat_dental_care);
        user = (ImageView) findViewById(R.id.user);
        carouselView = findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);
        cart_counter = (TextView) findViewById(R.id.cart_counter);
        open_cart = (ImageView)findViewById(R.id.shoppingCart);
        SharedPreferences sharedpreferences = getSharedPreferences("Myprefs", getApplicationContext().MODE_PRIVATE);
        cart = new shoppingCart(sharedpreferences,cart_counter);
        cart.updateCartUI();




        open_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ShoppingCartActivity.class);
                startActivity(i);
            }
        });

        catBread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),items.class);
                i.putExtra("category","bread");
                startActivity(i);
            }
        });

        catDentalCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),items.class);
                i.putExtra("category","dental care");
                startActivity(i);
            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),userDetails.class);
                //i.putExtra("category","dental care");
                startActivity(i);

            }
        });
        search_bar = (AutoCompleteTextView) findViewById(R.id.search_bar);
        new SearchGlobal().populateList(this,search_bar);



    }







    @Override
    protected void onResume() {
        super.onResume();
        cart.updateCartUI();
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };



    public void setLocale(){
        SharedPreferences sharedpreferences = getSharedPreferences("userdetails", getApplicationContext().MODE_PRIVATE);
        String currentLang = sharedpreferences.getString("lang","English");
        String langcode= new LanguageHelper().getLanguageCode(currentLang);
        Resources resources = getResources();
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        Configuration configuration = getApplicationContext().getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(new Locale(langcode.toLowerCase()));
        }
        getApplicationContext().getResources().updateConfiguration(configuration, displayMetrics);
        configuration.locale = new Locale(langcode.toLowerCase());
        resources.updateConfiguration(configuration, displayMetrics);
    }
}


