package com.app.grocer;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchGlobal {
    public static ArrayList<SearchItem> searchList;
    private Context context;
    private AutoCompleteTextView search_bar;


    public SearchGlobal(){

    }

    public void populateList(final Context context, final AutoCompleteTextView search_bar){
        this.context = context;
        this.search_bar = search_bar;
        searchList = new ArrayList<SearchItem>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("products");
        Query qr = myRef.orderByChild("productName");

        qr.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Products prod1 = data.getValue(Products.class);
                        searchList.add(new SearchItem(prod1.productName,prod1.productId));
                        SearchListAdapter adapter = new SearchListAdapter(context,SearchGlobal.searchList);
                        search_bar.setAdapter(adapter);

                    }
                }

            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
    });
}
}
