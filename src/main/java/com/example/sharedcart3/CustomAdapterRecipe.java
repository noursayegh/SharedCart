package com.example.sharedcart3;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomAdapterRecipe extends ArrayAdapter<Recipe_Item> {

    Context myContext;

    LayoutInflater inflater;

    ArrayList<Recipe_Item> items;

    SparseBooleanArray mSelectedItemsIds;
    String currency;



    // Constructor for get Context and  list

    public CustomAdapterRecipe(Context context, int resourceId, ArrayList<Recipe_Item> items) {

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
        Recipe_Item item=items.get(position);
        holder.name.setText(position+". "+item.getName()+" x "+item.getQuantity());
        holder.category.setText(" ");
        holder.price.setText(" ");


        return view;

    }



   // @Override

    public void remove(String  object) {
        items.remove(object);
        notifyDataSetChanged();

    }



    // get List after update or delete

    public  ArrayList<Recipe_Item> getMyList() {
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

            setToggled(position);

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