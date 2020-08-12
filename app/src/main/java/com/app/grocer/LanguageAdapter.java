package com.app.grocer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.app.grocer.LanguageModel;
import com.app.grocer.R;

import java.util.List;

public class LanguageAdapter extends ArrayAdapter<LanguageModel> {

    private List<LanguageModel> langlist;
    private Context context;

    public LanguageAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<LanguageModel> objects) {
        super(context, resource, textViewResourceId, objects);
        this.langlist = objects;
        this.context = context;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            v = inflater.inflate(R.layout.language_list_layout, null);
        }
        TextView langName = (TextView) v.findViewById(R.id.langName);
        langName.setText(langlist.get(position).getLanguageName());
        String lang = langlist.get(position).getLanguageName();
        String flag = new LanguageHelper().getLanguageFlag(lang);
        Log.d("eee",flag);

        int id = context.getResources().getIdentifier( flag, "drawable", context.getPackageName());
        ImageView langFlag = (ImageView) v.findViewById(R.id.langFlag);
        langFlag.setImageResource(id);


        return v;
    }
    @Override
    public LanguageModel getItem(int position) {
        return langlist.get(position);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            v = inflater.inflate(R.layout.language_list_layout, null);
        }
        TextView langName = (TextView) v.findViewById(R.id.langName);
        langName.setText(langlist.get(position).getLanguageName());
        String lang = langlist.get(position).getLanguageName();
        String flag = new LanguageHelper().getLanguageFlag(lang);
        int id = context.getResources().getIdentifier( flag , "drawable", context.getPackageName());
        ImageView langFlag = (ImageView) v.findViewById(R.id.langFlag);
        langFlag.setImageResource(id);
        return v;
    }

    @Override
    public int getCount() {
        return langlist.size();
    }

    public List<LanguageModel> getItems() {
        return langlist;
    }

}