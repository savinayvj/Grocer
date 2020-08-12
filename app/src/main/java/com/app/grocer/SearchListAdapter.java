package com.app.grocer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
//Custom Adapter for search list
public class SearchListAdapter extends ArrayAdapter<SearchItem> {
    private List<SearchItem> searchListFull;
    public SearchListAdapter(@NonNull Context context, @NonNull List<SearchItem> searchItems) {
        super(context, 0, searchItems);
        searchListFull = new ArrayList<>(searchItems);
    }
    @NonNull
    @Override
    public Filter getFilter() {
        return searchFilter;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.search_suggestion_rows, parent, false
            );
        }
        TextView textViewName = convertView.findViewById(R.id.search_text);
        final SearchItem searchItem = getItem(position);
        if (searchItem != null) {
            textViewName.setText(searchItem.getSearchSuggestion());

        }

        textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(parent.getContext(),ItemDetail.class);
                i.putExtra("id",searchItem.getProductId());
                parent.getContext().startActivity(i);

            }
        });




        return convertView;
    }
    private Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<SearchItem> suggestions = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(searchListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (SearchItem item : searchListFull) {
                    if (item.getSearchSuggestion().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }
            results.values = suggestions;
            results.count = suggestions.size();
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((SearchItem) resultValue).getSearchSuggestion();
        }
    };
}
