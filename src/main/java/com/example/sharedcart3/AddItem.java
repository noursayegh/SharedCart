package com.example.sharedcart3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static java.lang.Thread.sleep;

public class AddItem extends AppCompatActivity {
    Intent intent;
    EditText NameEditText;
    EditText QuanEditText;
    EditText UnitEditText;
    Button addItem;
    FirebaseDatabase database;
    String Uid;
    String name;
    String quantity;
    String unit;
    int listSize;
    ProgressBar progressBar;
    int price;
    String recipeName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Toolbar myToolbar =findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        UnitEditText=findViewById(R.id.unit);
        NameEditText=findViewById(R.id.name);
        QuanEditText=findViewById(R.id.quantity);
        addItem=findViewById(R.id.addItem);


        progressBar=findViewById(R.id.progressBar);
        intent=getIntent();
        Uid=intent.getStringExtra("Uid");
        listSize=intent.getIntExtra("listSize",0);
        recipeName=intent.getStringExtra("recipeName");
    }
    public void addItem(View view){
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        name=NameEditText.getText().toString().trim();
        quantity=QuanEditText.getText().toString().trim();
        unit=UnitEditText.getText().toString().trim();
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
            database=FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference().child("users").child(Uid).child("recipes").child(recipeName).child("list").child(String.valueOf(listSize));
            Recipe_Item item=new Recipe_Item(name,quantity+" "+unit+"(s)");

            myRef.setValue(item);
            listSize++;
        }catch (Exception e){
            Toast.makeText(this,"test",Toast.LENGTH_LONG).show();
        }
        try {
        Thread.sleep(700);
        }catch (Exception e){}
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this,"Item added",Toast.LENGTH_SHORT).show();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }
}
