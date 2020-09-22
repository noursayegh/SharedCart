package com.example.sharedcart3;

import java.util.ArrayList;

public class Cart_List {
    private String name;
    private ArrayList<Cart_Item> list;

    Cart_List(){
        this.list=new ArrayList<>();
    }
    Cart_List(String name){
        this.name=name;
        this.list=new ArrayList<>();
    }
    Cart_List(String name,ArrayList<Cart_Item> items){
        this.name=name;
        this.list=items;
    }
    public int getSize(){
        try {
            return list.size();
        }catch (Exception e){}
        return 0;
    }
    public String getCategory(){return null;}
    public int getPrice(){return calculatePrice();}
    public void addItem(Cart_Item item){list.add(item);}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public ArrayList<Cart_Item> getList() { return list; }
    public void setList(ArrayList<Cart_Item> list) { this.list = list; }

    public String toString(){
        return name;
    }
    private int calculatePrice(){
        int total=0;
        for (Cart_Item item: list)
            total+=item.getTotalPrice();
        return total;
    }
}
