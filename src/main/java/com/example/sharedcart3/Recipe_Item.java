package com.example.sharedcart3;

public class Recipe_Item {

    private String name;
    private String quantity;

    Recipe_Item(){}
    Recipe_Item(String name,String quantity){
        this.quantity=quantity;
        this.name=name;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }

}
