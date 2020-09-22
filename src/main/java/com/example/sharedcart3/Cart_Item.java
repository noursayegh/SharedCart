package com.example.sharedcart3;


public class Cart_Item {
    private String itemName;
    private String itemCategory;
    private int price;
    private int quantity;

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getPrice(){ return price;}
    public int getTotalPrice(){ return price*quantity;}
    public void setPrice(int price){ this.price = price; }

    public String getName() {
        return itemName;
    }
    public void setName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategory() {
        return itemCategory;
    }
    public void setCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }


    Cart_Item(String itemName,String itemCategory,int price,int quantity){
        this.itemCategory=itemCategory;
        this.itemName=itemName;
        this.quantity=quantity;
        this.price=price;
    }
    Cart_Item(){}


    @Override
    public String toString(){
        return this.itemName+"        "+this.itemCategory +"        "+this.price+"        "+this.quantity;
    }

}
