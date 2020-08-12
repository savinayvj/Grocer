package com.app.grocer;

//DataModel for search Items
public class SearchItem {
    private String searchSuggestion;
    private int productId;

    public SearchItem(String suggestion, int pid){
        searchSuggestion = suggestion;
        productId = pid;
    }

    public String getSearchSuggestion(){
        return searchSuggestion;
    }
    public int getProductId(){
        return productId;
    }
}
