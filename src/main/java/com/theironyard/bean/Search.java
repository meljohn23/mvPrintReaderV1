package com.theironyard.bean;

import java.util.ArrayList;

public class Search {

    public Search(){
        //types = new ArrayList<String>();
    }

    public String getKeyword() {
        return keyword==null? "": keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    private String keyword = null;
    private ArrayList<String> types = null;

    public ArrayList<String> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }
}