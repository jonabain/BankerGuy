package com.bankerguy.bankerguy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

public class LoginActivity extends Activity implements Button.OnClickListener {

    private Button loginButton;
    private Button createButton;
    private EditText userEmail;
    private EditText userPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        loginButton = findViewById(R.id.LoginButton);
        createButton = findViewById(R.id.CreateButton);
        userEmail = findViewById(R.id.UserEmail);
        userPassword = findViewById(R.id.UserPassword);

        loginButton.setOnClickListener(this);
        createButton.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            goToHome();
        }
    }

    @Override
    public void onClick(View view) {
        if(view == loginButton){
            login(userEmail.getText().toString(), userPassword.getText().toString());
        } else if (view == createButton){
            create(userEmail.getText().toString(), userPassword.getText().toString());
        }
    }

    private void login(final String email, final String password){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(LoginActivity.this, "Please use a valid email address.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.isEmpty()){
            Toast.makeText(LoginActivity.this, "Please enter a password.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            goToHome();
                            //goToCourse();
                        } else {
                            Toast.makeText(LoginActivity.this, "Login unsuccessful.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void create(final String email, final String password){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(LoginActivity.this, "Please use a valid email address.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.isEmpty()){
            Toast.makeText(LoginActivity.this, "Please enter a password.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            login(email, password);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Account creation failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void goToHome(){
        userEmail.setText("");
        userPassword.setText("");
        Intent home = new Intent(this, HomeActivity.class);
        this.startActivity(home);
    }

    private void goToCourse(){
        userEmail.setText("");
        userPassword.setText("");
        Intent course = new Intent(this, CourseActivity.class);
        this.startActivity(course);
    }

}
