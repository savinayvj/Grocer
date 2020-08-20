package com.app.grocer;

//DataModel for search Items
public class SearchItem {
    private String searchSuggestion;
    private String productId;

    public SearchItem(String suggestion, String pid){
        searchSuggestion = suggestion;
        productId = pid;
    }

    public String getSearchSuggestion(){
        return searchSuggestion;
    }
    public String getProductId(){
        return productId;
    }
}
