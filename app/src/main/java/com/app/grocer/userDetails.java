package com.app.grocer;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class userDetails extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        final Spinner language_selector = (Spinner) findViewById(R.id.language_selector);
        ArrayList<LanguageModel> langlist = new ArrayList<>();
        langlist = new LanguageHelper().getLanguagesList();
        Button save_button = (Button) findViewById(R.id.save_button);
        LanguageAdapter adapter = new LanguageAdapter(getApplicationContext(),R.layout.language_list_layout,R.id.language_selector,langlist);
        /* set the spinners adapter to the previously created one. */
        language_selector.setAdapter(adapter);
        final SharedPreferences sharedpreferences = getSharedPreferences("userdetails", getApplicationContext().MODE_PRIVATE);
        final SharedPreferences.Editor myEdit = sharedpreferences.edit();
        String currLang = sharedpreferences.getString("lang","English");
        language_selector.setSelection(new LanguageHelper().getLanguageIndex(currLang));

       save_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               LanguageModel lang1 = (LanguageModel) language_selector.getSelectedItem();
               myEdit.putString("lang",lang1.getLanguageName());
               myEdit.commit();
               Toast.makeText(userDetails.this,R.string.language_changed,Toast.LENGTH_LONG).show();

           }
       });
    }


}