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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    CarouselView carouselView;
    ImageView catBread;
    ImageView catDentalCare;
    int[] sampleImages = {R.drawable.banner1, R.drawable.banner2};

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //set Locale
        setLocale();
        setContentView(R.layout.activity_main);

        //sliding caraousel on the main screen
        carouselView = findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);

        //open the items page with category bread to list items in that category
        catBread = (ImageView) findViewById(R.id.cat_bread);
        catBread.setOnClickListener(this);

        //open the items page with category dental care to list items in that category
        catDentalCare = (ImageView)findViewById(R.id.cat_dental_care);
        catDentalCare.setOnClickListener(this);


    }
    
    //carousel switcher
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

    //get the last saved locale and set for current instance
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

    //onCLickListener for all categories
    @Override
    public void onClick(View view) {
        Intent i = new Intent(getApplicationContext(),items.class);
        switch (view.getId()){
            case R.id.cat_bread:
                i.putExtra("category","bread");
                break;
            case R.id.cat_dental_care:
                i.putExtra("category","dental care");
                break;
        }
        startActivity(i);
    }
}


