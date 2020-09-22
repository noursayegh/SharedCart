package com.example.sharedcart3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {
    Intent intent;
    FirebaseAuth mAuth;
    EditText EmailEditText;
    EditText PassEditText;
    ProgressBar progressBar;
    String email;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        EmailEditText=findViewById(R.id.input_email);
        PassEditText=findViewById(R.id.input_password);
        progressBar=findViewById(R.id.progressBar);
        intent=getIntent();
        mAuth=FirebaseAuth.getInstance();
    }



    public void createAccount (View view){
        password = PassEditText.getText().toString().trim();
        email = EmailEditText.getText().toString().trim();

        //1.1Check email and password entry
        if(!Functions.ValidForm(EmailEditText,email,PassEditText,password))
            return;
        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            intent=new Intent(SignUp.this,Lists.class);
                            intent.putExtra("Uid",user.getUid());
                            startActivity(intent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
    public void forgotPass(View view){}

}
