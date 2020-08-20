package com.app.grocer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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
    String name,address,phone;
    ArrayList<Products> productList = new ArrayList<>();
    ArrayList<Integer> quantites = new ArrayList<>();
    TextView total_tv,delivery_tv,delivery_price;
    boolean detailsSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        detailsSet = checkDetails();

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

        pay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if(detailsSet==false) {
                   AlertDialog.Builder alert = new AlertDialog.Builder(ShoppingCartActivity.this).setMessage(R.string.details_alert).setTitle(R.string.details_alert_detailed).setCancelable(false)
                           .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialogInterface, int i) {
                                   Intent intent = new Intent(getApplicationContext(), userDetails.class);
                                   startActivity(intent);
                                   finish();

                               }
                           });
                   AlertDialog dialog = alert.create();
                   dialog.show();
               }

                if(detailsSet)

            {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("orders/");
                int id = (int) (Math.random() * ((Integer.MAX_VALUE-1)-0 + 1) + 0);
                myRef.push().setValue(new Orders(id, name, address, phone, productList, quantites));
                SharedPreferences lastorderpreferences = getSharedPreferences("lastOrder", getApplicationContext().MODE_PRIVATE);
                SharedPreferences.Editor orderEditor = lastorderpreferences.edit();
                orderEditor.clear();
                copySharedPreferences(sharedpreferences, orderEditor);
                orderEditor.commit();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                Toast.makeText(ShoppingCartActivity.this,R.string.order_placed, Toast.LENGTH_SHORT).show();
                editor.clear();
                editor.commit();
                finish();
            }
            }

        });



    }

    //kill the activity when it in background (so that it restarts everytime it opens and cart remains updated)
    public void onPause() {
        super.onPause();
        finish();
    }

    //function to populate the shopping cart list
    public void populateList(){
        dataModels = new ArrayList<>();
        productList.clear();
        quantites.clear();
        Map<String,?> keys = sharedpreferences.getAll();

        //for each item in the shopping cart
        for(final Map.Entry<String,?> entry : keys.entrySet()){
            //if the quantity of item is more than 0
            if(parseInt(String.valueOf(entry.getValue()))>0) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("products");
                Query qr = myRef.orderByChild("productId").equalTo(entry.getKey());

                qr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            for(DataSnapshot data : snapshot.getChildren()) {
                                Products prod1 = data.getValue(Products.class);
                                productList.add(prod1);
                                quantites.add(parseInt((String) entry.getValue()));
                                //add price * quantity to total
                                total += parseInt(prod1.productPrice) * parseInt((String) entry.getValue());
                                dataModels.add(new DataModel(prod1.productName, prod1.productPrice, prod1.productId, parseInt((String) entry.getValue())));



                            }
                        }

                        adapter = new ShoppingCartAdapter(dataModels,getApplicationContext());
                        shoppingList.setAdapter(adapter);
                        DecimalFormat formatter = (Locale.getDefault().getLanguage().equals("hi")) ? new DecimalFormat("##,##,###") : new DecimalFormat("#,###,###");
                        String total_string = formatter.format(total);
                        total_price.setText(getApplicationContext().getResources().getString(R.string.currency) + " " + total_string);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }




        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public static void copySharedPreferences(SharedPreferences fromPreferences, SharedPreferences.Editor toEditor) {

        for (Map.Entry<String, ?> entry : fromPreferences.getAll().entrySet()) {
            Object value = entry.getValue();
            String key = entry.getKey();
            if (value instanceof String) {
                toEditor.putString(key, ((String) value));
            } else if (value instanceof Set) {
                toEditor.putStringSet(key, (Set<String>) value); // EditorImpl.putStringSet already creates a copy of the set
            } else if (value instanceof Integer) {
                toEditor.putInt(key, (Integer) value);
            } else if (value instanceof Long) {
                toEditor.putLong(key, (Long) value);
            } else if (value instanceof Float) {
                toEditor.putFloat(key, (Float) value);
            } else if (value instanceof Boolean) {
                toEditor.putBoolean(key, (Boolean) value);
            }
        }
    }

    public boolean checkDetails()
    {
        SharedPreferences userdetails = getSharedPreferences("userdetails", getApplicationContext().MODE_PRIVATE);
        name = userdetails.getString("name","");
        address = userdetails.getString("address","");

        phone = userdetails.getString("phone","");
        if(name.equals("") || address.equals("")  || phone.equals("")){
            return false;
        }
        return true;


    }

}