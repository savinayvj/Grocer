package com.app.grocer;

import java.util.ArrayList;
import java.util.HashMap;

public class LanguageHelper {

    //language helper class to get details about supported languages

    private String[] languages = {"English","हिंदी","português"};
    private String[] languageFlags = {"english_flag","india_flag","brazil_flag"};
    private String[] languageCodes = {"en","hi","pt"};
    public LanguageHelper(){

    }

    public int getLanguageIndex(String lang){
        for(int i=0;i<languages.length;i++){
            if(languages[i].equals(lang)){
                return i;
            }
        }
        return 0;
    }


    public String getLanguageFlag(String lang){

        return languageFlags[getLanguageIndex(lang)];
    }

    public String getLanguageCode(String lang){

        return languageCodes[getLanguageIndex(lang)];
    }

    public int getLanguageCount(){
        return languages.length;
    }

    public ArrayList<LanguageModel> getLanguagesList(){
        ArrayList<LanguageModel> langlist = new ArrayList<>();
        for(int i=0;i<languages.length;i++){
            langlist.add(new LanguageModel(languages[i]));
        }
        return langlist;


    }

}
