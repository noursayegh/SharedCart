package com.example.sharedcart3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditDetails extends AppCompatActivity {
    EditText DetailsEditText;
    Intent intent;
    String recipeName;
    String Uid;
    String details;
    FirebaseDatabase database;
    DatabaseReference myRef;
    LinearLayout listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);

        intent=getIntent();
        recipeName=intent.getStringExtra("recipeName");
        Uid=intent.getStringExtra("Uid");
        details=intent.getStringExtra("details");
        DetailsEditText=findViewById(R.id.details);
        listView=findViewById(R.id.listView);

        final androidx.appcompat.widget.Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setTitle("Details");

        ViewGroup.LayoutParams params=listView.getLayoutParams();
        params.height=Functions.getScreenHeight()-toolbar.getHeight()-Functions.getScreenHeight()/2;
        listView.setLayoutParams(params);



        if(details!=null)
            DetailsEditText.setText(details);

    }
    public void saveChanges (View view){
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference().child("users").child(Uid).child("recipes").child(recipeName).child("details");
        String newDetails="";
        try {

            newDetails=DetailsEditText.getText().toString().trim();
        }catch (Exception e){}
        if(!newDetails.isEmpty())
            myRef.setValue(newDetails);
    }
}
