package com.app.grocer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import static android.R.layout.simple_list_item_1;

public class userDetails extends AppCompatActivity {

    shoppingCart cart;
    TextView orderList;
    EditText name,address,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        name = (EditText) findViewById(R.id.username);
        address = (EditText) findViewById(R.id.address);
        phone = (EditText) findViewById(R.id.phone);

        String[] languages = getResources().getStringArray(R.array.languages);

        //set the language selector
        final Spinner language_selector = (Spinner) findViewById(R.id.language_selector);
        ArrayList<LanguageModel> langlist = new ArrayList<>();

        for(int i=0;i<languages.length;i++){
            langlist.add(new LanguageModel(languages[i]));
        }

        LanguageAdapter adapter = new LanguageAdapter(getApplicationContext(),R.layout.language_list_layout,R.id.language_selector,langlist);
        language_selector.setAdapter(adapter);

        //save the language in preferences
        Button save_button = (Button) findViewById(R.id.save_button);
        final SharedPreferences sharedpreferences = getSharedPreferences("userdetails", getApplicationContext().MODE_PRIVATE);
        final SharedPreferences.Editor myEdit = sharedpreferences.edit();
        name.setText(sharedpreferences.getString("name",""));
        phone.setText(sharedpreferences.getString("phone",""));
        address.setText(sharedpreferences.getString("address",""));
        final String currLang = sharedpreferences.getString("lang","en");
        language_selector.setSelection(Arrays.asList(languages).indexOf(currLang));
        save_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               LanguageModel lang1 = (LanguageModel) language_selector.getSelectedItem();
               myEdit.putString("lang",lang1.getLanguageName());
               myEdit.putString("name", name.getText().toString());
               myEdit.putString("address",address.getText().toString());
               myEdit.putString("phone",phone.getText().toString());
               myEdit.commit();

               if(!getResources().getConfiguration().locale.getLanguage().equals(lang1.getLanguageName())) {
                   Toast.makeText(userDetails.this, R.string.language_changed, Toast.LENGTH_LONG).show();
               }
               Toast.makeText(userDetails.this,R.string.details_saved, Toast.LENGTH_SHORT).show();
               finish();
           }
       });
        orderList = (TextView) findViewById(R.id.order_number);
        orderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),OrderProducts.class);
                startActivity(i);
            }
        });


    }

}


