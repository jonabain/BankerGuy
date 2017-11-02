package com.bankerguy.bankerguy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity implements Button.OnClickListener {

    private Button loginButton;
    private EditText userEmail;
    private EditText userPassword;
    private TextView loginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.LoginButton);
        userEmail = findViewById(R.id.UserEmail);
        userPassword = findViewById(R.id.UserPassword);
        loginTextView = findViewById(R.id.LoginTextView);

        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {


    }

}
