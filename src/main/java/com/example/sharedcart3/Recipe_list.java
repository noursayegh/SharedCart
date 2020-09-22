package com.example.sharedcart3;

import java.util.ArrayList;

public class Recipe_list {

    private String name;
    private ArrayList<Recipe_Item> list;
    private String details;

    public int getSize(){
        try {
            return list.size();
        }catch (Exception e){}
        return 0;
    }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public ArrayList<Recipe_Item> getList() { return list; }
    public void setList(ArrayList<Recipe_Item> list) { this.list = list; }

    public String toString (){return name;}
}
