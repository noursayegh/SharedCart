package com.example.sharedcart3;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Cart1 extends AppCompatActivity {
    Intent intent;
    final String CURRENCY=" LBP";
    ListView listView;
    String Uid;
    int listsize;
    String cartName="cartTest";
    Cart_List items;
    FirebaseDatabase database;
    DatabaseReference myRef;
    MylistViewAdapter adapter;
    ProgressBar progressBar;
    ConstraintLayout main;
    AlertDialog.Builder  builder;
    AlertDialog alert;
    ArrayList<Cart_Item> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart1);

        final Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main_menu);
        intent = getIntent();
        Uid=intent.getStringExtra("Uid");
        cartName=intent.getStringExtra("cartName");
        toolbar.setTitle(cartName);


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("users").child(Uid).child("carts").child(cartName);


        listView = findViewById(R.id.items);
        main=findViewById(R.id.mainLayout);
        progressBar=findViewById(R.id.progressBar);




        try {
            progressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    main.removeView(listView);
                    items=new Cart_List();
                    list=new ArrayList<>();
                    items=dataSnapshot.getValue(Cart_List.class);
                    try {
                    toolbar.setTitle(items.getName());
                    }catch (Exception e){}

                    listsize=0;

                    int total=0;
                    if(items!=null&&items.getSize()!=0) {
                        list=items.getList();
                        listsize=items.getSize();
                        total = Functions.calculateTotal(items);
                    }
                    try {

                        adapter = new MylistViewAdapter(Cart1.this, R.layout.custom_list_layout,list, CURRENCY);
                    }catch (Exception e){}
                        listView.setAdapter(adapter);
                        main.addView(listView);



                    ViewGroup.LayoutParams params=listView.getLayoutParams();
                    params.height=Functions.getScreenHeight()-toolbar.getHeight();
                    listView.setLayoutParams(params);
                    progressBar.setVisibility(View.GONE);
                    toolbar.setSubtitle("Total Price = "+total+CURRENCY);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    //listSize krmel njeeb 3adadon w mnzeedo attribute bl cart_item


                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Toast.makeText(Cart1.this,error.getMessage(),Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "fail", Toast.LENGTH_LONG).show();
        }


        //toolbar configuration
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                if(arg0.getItemId() == R.id.action_logout){
                    FirebaseAuth.getInstance().signOut();
                    intent=new Intent(Cart1.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                if(arg0.getItemId() == R.id.action_addItem){
                    DatabaseReference myRef = database.getReference().child("users").child(Uid).child("carts").child(cartName).child("list");
                    Functions.addItem(Cart1.this,progressBar,myRef,items);
                }
                if(arg0.getItemId() == R.id.action_rename){
                    Functions.EditItem(Cart1.this,progressBar,myRef);
                }
                return false;
            }
        });


        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DatabaseReference myRef = database.getReference().child("users").child(Uid).child("carts").child(cartName).child("list").child(String.valueOf(position));
                Functions.EditItem(position,Cart1.this,progressBar,myRef,items);
            }
        });

        listView.setMultiChoiceModeListener(new  AbsListView.MultiChoiceModeListener() {
            @Override
            public boolean  onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO  Auto-generated method stub
                return false;
            }
            @Override
            public void  onDestroyActionMode(ActionMode mode) {
                // TODO  Auto-generated method stub
            }
            @Override
            public boolean  onCreateActionMode(ActionMode mode, Menu menu) {
                // TODO  Auto-generated method stub
                mode.getMenuInflater().inflate(R.menu.main_menu_2, menu);
                return true;
            }
            @Override
            public boolean  onActionItemClicked(final ActionMode mode,
                                                MenuItem item) {
                // TODO  Auto-generated method stub
                switch  (item.getItemId()) {

                    case R.id.selectAll:
                        final int checkedCount  = items.getList().size();
                        adapter.removeSelection();
                        for (int i = 0; i <  items.getSize(); i++) {
                                listView.setItemChecked(i, true);
                                adapter.setToggled(i);
                        }
                        mode.setTitle(checkedCount  + "  Selected");
                        return true;

                    case R.id.delete:
                        builder = new AlertDialog.Builder(Cart1.this);
                        builder.setMessage("Delete selected items?");
                        builder.setNegativeButton("No", new  DialogInterface.OnClickListener() {
                            @Override
                            public void  onClick(DialogInterface dialog, int which) { }
                        });

                        builder.setPositiveButton("Yes", new  DialogInterface.OnClickListener() {



                            @Override

                            public void  onClick(DialogInterface dialog, int which) {

                                SparseBooleanArray selected = adapter.getSelectedIds();
                                progressBar.setVisibility(View.VISIBLE);
                                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                Toast.makeText(Cart1.this,"Deleting Selected Items",Toast.LENGTH_SHORT).show();
                                if(adapter.getSelectedCount()==list.size()){
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                    ref.child("users").child(Uid).child("carts").child(cartName).child("list").removeValue();
                                    selected = new SparseBooleanArray();
                                    list = new ArrayList<>();
                                    adapter.notifyDataSetChanged();
                                    finish();
                                }
                                for (int i=(selected.size() - 1); i >= 0; i--) {
                                    Cart_Item  selecteditem = adapter.getItem(selected.keyAt(i));
                                    Functions.deleteItem(list,selecteditem,Uid,cartName);
                                }

                                mode.finish();
                                selected.clear();
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(Cart1.this,"Items Deleted Successfully.",Toast.LENGTH_LONG).show();
                            }

                        });

                        alert =  builder.create();
                        alert.setTitle("Careful"); // dialog  Title
                        alert.show();
                        return true;

                case R.id.compute_price:
                        SparseBooleanArray selected = adapter.getSelectedIds();
                        int price=0;
                        for (int i=(selected.size() - 1); i >= 0; i--) {
                            Cart_Item  selecteditem = adapter.getItem(selected.keyAt(i));
                            price+=selecteditem.getTotalPrice();
                        }
                        builder = new AlertDialog.Builder(Cart1.this);
                        builder.setMessage("Price ="+price+CURRENCY);
                        alert =  builder.create();
                        alert.setTitle("Price"); // dialog  Title
                        alert.show();
                    default:
                        return false;
                }
            }
            @Override

            public void  onItemCheckedStateChanged(ActionMode mode,int position, long id, boolean checked) {


                adapter.toggleSelection(position);
                final int checkedCount  = listView.getCheckedItemCount();
                mode.setTitle(checkedCount  + "  Selected");

            }

        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu_2, menu);
        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            finish();
    }




}
