package com.example.sharedcart3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class AllRecipes extends AppCompatActivity {
    Intent intent;
    ListView listView;
    String Uid;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayAdapter<Recipe_list> adapter;
    ProgressBar progressBar;
    ConstraintLayout main;
    ArrayList<Recipe_list> lists;
    AlertDialog.Builder  builder;
    AlertDialog alert;
    ArrayList<String> keys;
    ArrayList<String> Uids;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_recipes);

        intent=getIntent();
        Uid=intent.getStringExtra("Uid");
        main=findViewById(R.id.mainLayout);
        progressBar=findViewById(R.id.progressBar);
        listView=findViewById(R.id.items);
        final Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setTitle("Online Recipes");
        //toolbar configuration
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                if(arg0.getItemId() == R.id.action_logout){
                    FirebaseAuth.getInstance().signOut();
                    intent=new Intent(AllRecipes.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                return false;
            }
        });

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("users");




        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                main.removeView(listView);
                keys=new ArrayList<>();
                lists=new ArrayList<>();
                Uids=new ArrayList<>();
                int i=0;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                    try {
                        for(DataSnapshot listShot: postSnapshot.child("recipes").getChildren()) {
                        Uids.add(i,postSnapshot.getKey());
                            Recipe_list list;
                            list = listShot.getValue(Recipe_list.class);
                            lists.add(list);
                            keys.add(listShot.getKey());
                            i++;
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                        }





                adapter=new ArrayAdapter<>(AllRecipes.this,android.R.layout.simple_list_item_1,lists);
                listView.setAdapter(adapter);
                main.addView(listView);



                ViewGroup.LayoutParams params=listView.getLayoutParams();
                params.height=Functions.getScreenHeight()-toolbar.getHeight();
                listView.setLayoutParams(params);
                progressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                //listSize krmel njeeb 3adadon w mnzeedo attribute bl cart_item


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        intent=new Intent(AllRecipes.this,Recipe1.class);
                        intent.putExtra("Uid",Uids.get(position));
                        intent.putExtra("recipeName",keys.get(position));
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(AllRecipes.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


    }
    @Override
    protected void onStart(){
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()==null)
            finish();
    }
    @Override
    protected void onResume(){
        super.onResume();
        if(FirebaseAuth.getInstance().getCurrentUser()==null)
            finish();
    }



}
