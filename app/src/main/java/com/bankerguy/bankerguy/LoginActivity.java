package com.bankerguy.bankerguy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity implements Button.OnClickListener {

    private Button loginButton;
    private EditText userEmail;
    private EditText userPassword;
    private TextView loginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.LoginButton);
        userEmail = (EditText) findViewById(R.id.UserEmail);
        userPassword = (EditText) findViewById(R.id.UserPassword);
        loginTextView = (TextView) findViewById(R.id.LoginTextView);

        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(loginButton)){
            loginTextView.setText("yeeee");
        }
    }
}
