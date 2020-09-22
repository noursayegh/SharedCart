package com.example.sharedcart3;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

//hyda bas krmel ma n3eed l check martein bl login wl signup
class Functions {
    static boolean ValidForm(EditText EmailEditText,String email,EditText PassEditText,String password) {
        if (email.isEmpty()) {
            EmailEditText.setError("Please Enter Email Address");
            EmailEditText.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            EmailEditText.setError("Please Enter A Valid Email");
            EmailEditText.requestFocus();
            return false;
        }
        if (password.length()<6) {
            PassEditText.setError("Please Fill Field Properly");
            PassEditText.requestFocus();
            return false;
        }

        return true;
    }
    static boolean ValidItem(EditText NameEditText,String name,EditText QuantityEditText,int quantity,EditText PriceEditText,int price) {
        if (name.isEmpty()) {
            NameEditText.setError("Please Enter Item Name");
            NameEditText.requestFocus();
            return false;
        }
        if (price!=0&&quantity<1){
            QuantityEditText.setError("Please Enter A Valid Amount");
            QuantityEditText.requestFocus();
            return false;
        }
        if (price<0){
            PriceEditText.setError("Please Enter A Valid Amount");
            PriceEditText.requestFocus();
            return false;
        }
        return true;
    }
    static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
    static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }



    static int calculateTotal(Cart_List items){
        int total=0;
        for(Cart_Item item :items.getList())
            total+=item.getTotalPrice();
        return total;
    }


    static void addItem(final Context context, final ProgressBar progressBar, final DatabaseReference myRef){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View mview = inflater.inflate(R.layout.new_cart_popup,null);
        builder.setView(mview);
        final androidx.appcompat.app.AlertDialog alertDialog= builder.create();
        final EditText NameEditText=mview.findViewById(R.id.name);
        final Button addCart=mview.findViewById(R.id.createCart);
        final Button close=mview.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alertDialog.isShowing())
                    alertDialog.hide();
            }
        });
        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String name = NameEditText.getText().toString().trim();
                if(name.isEmpty()){
                    NameEditText.requestFocus();
                    NameEditText.setError("Please Enter a Valid Name");
                    return;
                }
                Cart_List item = new Cart_List(name);
                myRef.push().setValue(item);

                try {
                    Thread.sleep(400);
                } catch (Exception e) {
                }
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Item added", Toast.LENGTH_SHORT).show();
                if(alertDialog.isShowing())
                    alertDialog.hide();
            }
        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

    }
    static void EditItem(final Context context, final ProgressBar progressBar, final DatabaseReference myRef){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View mview = inflater.inflate(R.layout.new_cart_popup,null);
        builder.setView(mview);
        final androidx.appcompat.app.AlertDialog alertDialog= builder.create();
        final EditText NameEditText=mview.findViewById(R.id.name);
        final Button addCart=mview.findViewById(R.id.createCart);
        addCart.setText("Rename");
        final Button close=mview.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alertDialog.isShowing())
                    alertDialog.hide();
            }
        });
        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String name = NameEditText.getText().toString().trim();
                if(name.isEmpty()){
                    NameEditText.requestFocus();
                    NameEditText.setError("Please Enter a Valid Name");
                    return;
                }
                myRef.child("name").setValue(name);

                try {
                    Thread.sleep(400);
                } catch (Exception e) {
                }
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Renamed List Successfully", Toast.LENGTH_SHORT).show();
                alertDialog.hide();
            }
        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

    }


    public static void addItem(final Context context, final ProgressBar progressBar, final DatabaseReference myRef, Cart_List items) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View mview = inflater.inflate(R.layout.cart_popup,null);
        builder.setView(mview);
        final int index=getListSize(items);
        final androidx.appcompat.app.AlertDialog alertDialog= builder.create();
        final EditText NameEditText=mview.findViewById(R.id.name);
        final EditText CatEditText=mview.findViewById(R.id.category);
        final EditText PriceEditText=mview.findViewById(R.id.price);
        final EditText QuantityEditText=mview.findViewById(R.id.quantity);
        final Button addItem1=mview.findViewById(R.id.addItem);
        final Button close=mview.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alertDialog.isShowing())
                    alertDialog.hide();
            }
        });
        addItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                int price=0;
                int quantity=0;
                try {
                    price=Integer.parseInt(PriceEditText.getText().toString());
                }catch (Exception e){}
                try {
                    quantity=Integer.parseInt(QuantityEditText.getText().toString());
                }catch (Exception e){}
                String name = NameEditText.getText().toString().trim();
                String category = CatEditText.getText().toString().trim();

                if (!Functions.ValidItem(NameEditText, name,QuantityEditText,quantity,PriceEditText,price)) return;

                Cart_Item item = new Cart_Item(name, category, price,quantity);
                myRef.child(String.valueOf(index)).setValue(item);

                try {
                    Thread.sleep(400);
                } catch (Exception e) {
                }
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Item added", Toast.LENGTH_SHORT).show();
                alertDialog.hide();
            }
        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }


    public static void EditItem(final int position, final Context context, final ProgressBar progressBar, final DatabaseReference myRef, Cart_List items) {
        final Cart_Item item=items.getList().get(position);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View mview = inflater.inflate(R.layout.cart_popup,null);
        builder.setView(mview);
        final androidx.appcompat.app.AlertDialog alertDialog= builder.create();
        final EditText NameEditText=mview.findViewById(R.id.name);
        final EditText CatEditText=mview.findViewById(R.id.category);
        final EditText PriceEditText=mview.findViewById(R.id.price);
        final EditText QuantityEditText=mview.findViewById(R.id.quantity);
        final Button editItem=mview.findViewById(R.id.addItem);
        editItem.setText("Save");
        final Button close=mview.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alertDialog.isShowing())
                    alertDialog.hide();
            }
        });
        editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                int price = item.getPrice();
                int quantity = item.getQuantity();
                String name = NameEditText.getText().toString().trim();
                String category = CatEditText.getText().toString().trim();
                try {
                    if(Integer.parseInt(PriceEditText.getText().toString())>0)
                        price=Integer.parseInt(PriceEditText.getText().toString());
                }catch (Exception e){}
                try {
                    if(Integer.parseInt(QuantityEditText.getText().toString())>0)
                        quantity=Integer.parseInt(QuantityEditText.getText().toString());
                }catch (Exception e){}
                if(name.isEmpty())
                    name=item.getName();
                if(category.isEmpty())
                    category=item.getCategory();



                Cart_Item newItem = new Cart_Item(name, category, price,quantity);
                if(newItem!=item)
                    myRef.setValue(newItem);


                try {
                    Thread.sleep(700);
                } catch (Exception e) {
                }
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Item updated", Toast.LENGTH_SHORT).show();
                if(alertDialog.isShowing())
                    alertDialog.hide();
            }
        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
    public static void EditItem(final int position, final Context context, final ProgressBar progressBar, final DatabaseReference myRef, Recipe_list items) {
        final Recipe_Item item = items.getList().get(position);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mview = inflater.inflate(R.layout.recipe_popup, null);
        builder.setView(mview);
        final androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        final EditText NameEditText = mview.findViewById(R.id.name);
        final EditText UnitEditText= mview.findViewById(R.id.unit);
        final EditText QuantityEditText = mview.findViewById(R.id.quantity);
        final Button editItem = mview.findViewById(R.id.addItem);
        editItem.setText("Save");
        final Button close = mview.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog.isShowing())
                    alertDialog.hide();
            }
        });
        editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String name = NameEditText.getText().toString().trim();
                String quantity = QuantityEditText.getText().toString().trim();
                String unit = UnitEditText.getText().toString().trim();
                quantity+= " "+unit;

                if (name.isEmpty())
                    name = item.getName();
                if (quantity.isEmpty())
                    quantity = item.getQuantity();


                Recipe_Item newItem = new Recipe_Item(name, quantity);
                if (newItem != item)
                    myRef.setValue(newItem);


                try {
                    Thread.sleep(700);
                } catch (Exception e) {
                }
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Item updated", Toast.LENGTH_SHORT).show();
                if(alertDialog.isShowing())
                    alertDialog.hide();
            }
        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
    public static void AddItem(final Context context, final ProgressBar progressBar, final DatabaseReference myRef, Recipe_list items) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mview = inflater.inflate(R.layout.recipe_popup, null);
        builder.setView(mview);

        final int index=getListSize(items);
        final androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        final EditText NameEditText = mview.findViewById(R.id.name);
        final EditText UnitEditText= mview.findViewById(R.id.unit);
        final EditText QuantityEditText = mview.findViewById(R.id.quantity);
        final Button editItem = mview.findViewById(R.id.addItem);
        final Button close = mview.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog.isShowing())
                    alertDialog.hide();
            }
        });
        editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=NameEditText.getText().toString().trim();
                String quantity=QuantityEditText.getText().toString().trim();
                String unit=UnitEditText.getText().toString().trim();
                if(name.isEmpty()){
                    NameEditText.setError("Enter a valid name");
                    NameEditText.requestFocus();
                    return;
                }
                if(quantity.isEmpty())
                    quantity="1";
                if(unit.isEmpty())
                    unit="unit";

                try {
                    Recipe_Item item=new Recipe_Item(name,quantity+" "+unit+"(s)");
                    myRef.child(String.valueOf(index)).setValue(item);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(context,"Database error!",Toast.LENGTH_LONG).show();
                }
                try {
                    Thread.sleep(400);
                }catch (Exception e){}
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context,"Item added",Toast.LENGTH_SHORT).show();
                alertDialog.hide();

            }

        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
    static void deleteItem(ArrayList<Recipe_Item> list,Recipe_Item position, String Uid, String listName){
        list.remove(position);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users").child(Uid).child("recipes").child(listName).child("list").setValue(null);
        ref.child("users").child(Uid).child("recipes").child(listName).child("list").setValue(list);
    }
    static void deleteItem(ArrayList<Cart_Item> list,Cart_Item position, String Uid, String listName){
        list.remove(position);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users").child(Uid).child("carts").child(listName).child("list").setValue(list);
    }
    static int getListSize(Cart_List items){
        return items.getSize();
    }
    static int getListSize(Recipe_list items){
        return items.getSize();
    }

}
