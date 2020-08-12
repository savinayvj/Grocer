package com.app.grocer;

// data model for the "Select a Language" dropdown
public class LanguageModel {
    String LanguageName;

    public LanguageModel(String lang){
        LanguageName = lang;

    }
    public String getLanguageName(){
        return LanguageName;
    }
}
