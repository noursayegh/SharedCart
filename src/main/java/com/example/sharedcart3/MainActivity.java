package com.example.sharedcart3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {
    EditText EmailEditText, PassEditText;
    String email, password;
    Button login;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String Uid;
    FirebaseAuth mAuth;
    Intent intent;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar =findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);



        //variables' initiation
        EmailEditText = findViewById(R.id.input_email);
        PassEditText = findViewById(R.id.input_password);
        mAuth = FirebaseAuth.getInstance();
        login = findViewById(R.id.login);
        progressBar = findViewById(R.id.progressBar);


    }

//To check if the user is already logged in
    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            Uid=mAuth.getCurrentUser().getUid();
            intent=new Intent(this,Lists.class);
            intent.putExtra("Uid",Uid);
            startActivity(intent);
            finish();
        }
    }

    public void onlogin(View view) {

        password = PassEditText.getText().toString().trim();
        email = EmailEditText.getText().toString().trim();

        //Check email and password entry
        if(!Functions.ValidForm(EmailEditText,email,PassEditText,password))
            return;
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        // DB Authentication
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        //Launch Main Activity
                        if (task.isSuccessful()) {
                            //if auth complete start new intent
                            FirebaseUser user = mAuth.getCurrentUser();
                            intent=new Intent(MainActivity.this,Lists.class);
                            intent.putExtra("Uid",user.getUid());
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "Email or Password incorrect", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    public void onsignup (View view){
        intent=new Intent(this,SignUp.class);
        startActivity(intent);
        finish();

    }



}



