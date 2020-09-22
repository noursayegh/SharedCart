package com.example.sharedcart3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import com.google.firebase.auth.FirebaseAuth;

public class Lists extends AppCompatActivity {
    Intent intent;
    String Uid;
    CheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);
        intent=getIntent();
        Uid = intent.getStringExtra("Uid");
        checkBox=findViewById(R.id.allRecipes);




        final Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("Shared Cart");
        toolbar.setSubtitle(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        toolbar.inflateMenu(R.menu.main_menu);
        MenuItem add=toolbar.getMenu().findItem(R.id.action_addItem);
        add.setVisible(false);
        MenuItem rename=toolbar.getMenu().findItem(R.id.action_rename);
        rename.setVisible(false);



        //toolbar configuration
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                if(arg0.getItemId() == R.id.action_logout){
                    FirebaseAuth.getInstance().signOut();
                    intent=new Intent(Lists.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                return false;
            }
        });
    }


    public void recipes(View view){
        if(checkBox.isChecked()){
            intent=new Intent(this,AllRecipes.class);
            intent.putExtra("Uid",Uid);
            startActivity(intent);
        }

        else{
            intent=new Intent(this,MyRecipes.class);
            intent.putExtra("Uid",Uid);
            startActivity(intent);
        }
    }

    public void cart(View view ){
        intent=new Intent(this,MyCarts.class);
        intent.putExtra("Uid",Uid);
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            finish();
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("Closing Application")
                .setMessage("Are you sure you want to close this activity?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}
