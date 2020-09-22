package com.example.sharedcart3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class MyRecipes extends AppCompatActivity {
    Intent intent;
    final String CURRENCY=" LBP";
    ListView listView;
    String Uid;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayAdapter<Recipe_list> adapter;
    ProgressBar progressBar;
    ConstraintLayout main;
    ArrayList<Recipe_list> lists;
    ArrayList<String> keys;
    AlertDialog.Builder  builder;
    AlertDialog alert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);
        final Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setTitle("SharedCart");
        MenuItem rename=toolbar.getMenu().findItem(R.id.action_rename);
        rename.setVisible(false);
        toolbar.setSubtitle(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        intent = getIntent();
        Uid=intent.getStringExtra("Uid");



        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("users").child(Uid).child("recipes");


        listView = findViewById(R.id.recipes);
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
                    keys=new ArrayList<>();
                    Recipe_list list;
                    lists=new ArrayList<>();
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        list=postSnapshot.getValue(Recipe_list.class);
                        try {
                            lists.add(list);
                            keys.add(postSnapshot.getKey());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }



                    adapter=new ArrayAdapter<>(MyRecipes.this,android.R.layout.simple_list_item_1,lists);
                    listView.setAdapter(adapter);
                    main.addView(listView);



                    ViewGroup.LayoutParams params=listView.getLayoutParams();
                    params.height=Functions.getScreenHeight()-toolbar.getHeight();
                    listView.setLayoutParams(params);
                    progressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    //listSize krmel njeeb 3adadon w mnzeedo attribute bl cart_item


                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Toast.makeText(MyRecipes.this,error.getMessage(),Toast.LENGTH_LONG).show();
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
                    intent=new Intent(MyRecipes.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                if(arg0.getItemId() == R.id.action_addItem)
                    Functions.addItem(MyRecipes.this,progressBar,myRef);

                return false;
            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent=new Intent(MyRecipes.this,Recipe1.class);
                intent.putExtra("Uid",Uid);
                intent.putExtra("recipeName",keys.get(position));
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                builder = new AlertDialog.Builder(MyRecipes.this);
                builder.setMessage("Delete Recipe?");
                builder.setNegativeButton("No", new  DialogInterface.OnClickListener() {
                    @Override
                    public void  onClick(DialogInterface dialog, int which) { }
                });

                builder.setPositiveButton("Yes", new  DialogInterface.OnClickListener() {



                    @Override

                    public void  onClick(DialogInterface dialog, int which) {

                        progressBar.setVisibility(View.VISIBLE);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(MyRecipes.this,"Deleting Selected Items",Toast.LENGTH_SHORT).show();
                        myRef.child(keys.get(position)).setValue(null);


                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MyRecipes.this,"Recipe Deleted Successfully.",Toast.LENGTH_LONG).show();
                    }

                });

                alert =  builder.create();
                alert.setTitle("Careful"); // dialog  Title
                alert.show();
                return true;
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            finish();
    }


}
