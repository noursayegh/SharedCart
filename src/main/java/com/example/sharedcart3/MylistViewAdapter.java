package com.example.sharedcart3;
import java.util.ArrayList;
import  java.util.List;



import  android.content.Context;

import  android.util.SparseBooleanArray;

import  android.view.LayoutInflater;

import  android.view.View;

import  android.view.ViewGroup;

import  android.widget.ArrayAdapter;

import  android.widget.ImageView;

import  android.widget.TextView;



public class  MylistViewAdapter extends ArrayAdapter<Cart_Item> {

    Context myContext;

    LayoutInflater inflater;

    ArrayList<Cart_Item> items;

    SparseBooleanArray mSelectedItemsIds;
    String currency;



    // Constructor for get Context and  list

    public  MylistViewAdapter(Context context, int resourceId,  ArrayList<Cart_Item> items,String currency) {

        super(context,  resourceId, items);

        mSelectedItemsIds = new  SparseBooleanArray();

        myContext = context;

        this.items = items;
        this.currency=currency;
        inflater =  LayoutInflater.from(context);

    }



    // Container Class for item

    private class ViewHolder {

        TextView name;
        TextView category;
        TextView price;
       // ImageView myImg;

    }



    public View getView(int position,  View view, ViewGroup parent) {

        final ViewHolder  holder;

        if (view == null) {

            holder = new ViewHolder();

            view = inflater.inflate(R.layout.custom_list_layout, null);

            // Locate the TextViews in  listview_item.xml

            holder.name =   view.findViewById(R.id.textview);
            holder.category =   view.findViewById(R.id.textView2);
            holder.price =  view.findViewById(R.id.textView3);



            // Locate the ImageView in  listview_item.xml

           // holder.myImg = (ImageView)  view.findViewById(R.id.myImage);

            view.setTag(holder);

        } else {

            holder = (ViewHolder)  view.getTag();

        }

        // Capture position and set to the  TextViews
        Cart_Item item=items.get(position);
        holder.name.setText(position+". "+item.getName());
        if(item.getCategory()!=null)
            holder.category.setText(item.getCategory());
        else
            holder.category.setText("N/A");
        if(item.getPrice()!=0)
            holder.price.setText("Price = "+item.getQuantity()+" x "+item.getPrice()+" = "+item.getTotalPrice()+currency);
        else
            holder.price.setText("N/A");


        return view;

    }



   // @Override

    public void remove(String  object) {
        items.remove(object);
        notifyDataSetChanged();

    }



    // get List after update or delete

    public  ArrayList<Cart_Item> getMyList() {
        return items;
    }



    public void  toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }
    public void setToggled(int position){
        if(!mSelectedItemsIds.get(position))
            mSelectedItemsIds.put(position,true);
    }



    // Remove selection after unchecked

    public void  removeSelection() {

        mSelectedItemsIds = new  SparseBooleanArray();

        notifyDataSetChanged();

    }

    // Item checked on selection

    public void selectView(int position, boolean value) {

        if (value)

            mSelectedItemsIds.put(position, true);

        else

            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();

    }

    // Get number of selected item
    public int  getSelectedCount() {

        return mSelectedItemsIds.size();

    }

    public  SparseBooleanArray getSelectedIds() {

        return mSelectedItemsIds;

    }

}