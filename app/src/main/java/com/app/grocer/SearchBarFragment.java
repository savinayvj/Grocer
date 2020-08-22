package com.app.grocer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class SearchBarFragment extends Fragment {
    shoppingCart cart;
    TextView cart_counter;
    public static ArrayList<SearchItem> searchList = new ArrayList<>();
    AutoCompleteTextView search_bar;

    private static SearchBarFragment instance;
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        instance = this;
        return inflater.inflate(R.layout.toolbar_layout, parent, false);

    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Search Bar
        search_bar = (AutoCompleteTextView) view.findViewById(R.id.search_bar);
        populateList();


        //open ShoppingCartActivity when the shopping cart icon is clicked
        ImageView open_cart = (ImageView) view.findViewById(R.id.shoppingCart);
        open_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),ShoppingCartActivity.class);
                startActivity(i);
            }
        });

        //user icon to open userDetails Activity
        ImageView user = (ImageView) view.findViewById(R.id.user);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),userDetails.class);
                //i.putExtra("category","dental care");
                startActivity(i);

            }
        });

        //setting up shopping cart counter
        cart_counter = (TextView) view.findViewById(R.id.cart_counter);
        SharedPreferences sharedpreferences = getContext().getSharedPreferences("Myprefs", getContext().MODE_PRIVATE);
        cart = new shoppingCart(sharedpreferences);
        updateCartUI();

    }

    @Override
    public void onResume() {
        super.onResume();
        updateCartUI();

    }

    public static SearchBarFragment getInstance(){
        return instance;
    }

    public void updateCartUI(){
        if(cart.getItemsCount()>0){
            cart_counter.setVisibility(View.VISIBLE);
            cart_counter.setText(Integer.toString(cart.getItemsCount()));
        }else{
            cart_counter.setVisibility(View.INVISIBLE);

        }


    }

    public void populateList(){

        //Connect to Firebase to get Names of all products

        //this block prevents connecting to the database if the list is already been retrieved i.e list is only populated once when the app boots
        if(!searchList.isEmpty()){
            SearchListAdapter adapter = new SearchListAdapter(getContext(),searchList);
            Toast.makeText(getContext(),"Local",Toast.LENGTH_SHORT).show();
            search_bar.setAdapter(adapter);
            return;
        }

        //Retrieving list for the first boot
        FirebaseHelper helper = new FirebaseHelper();
        helper.getAllItemNames(new Callback() {
            @Override
            public void onSuccessCallback(DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot data:snapshot.getChildren()){
                        Products prod1 = data.getValue(Products.class);
                        searchList.add(new SearchItem(prod1.productName,prod1.productId));


                    }
                    SearchListAdapter adapter = new SearchListAdapter(getContext(),searchList);
                    search_bar.setAdapter(adapter);
                    Toast.makeText(getContext(),"Cloud",Toast.LENGTH_SHORT).show();

                }

            }
        });
    }
}
