package com.app.uccplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Admin_Login extends AppCompatActivity {
    EditText username, password;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        username = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.btn_signin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getUser = username.getText().toString();
                String getPassword = password.getText().toString();

                if (TextUtils.isEmpty(getUser)) {
                    username.setError("Email cannot be empty");
                    username.requestFocus();
                } else if (TextUtils.isEmpty(getPassword)) {
                    password.setError("Password cannot be empty");
                    password.requestFocus();
                } else if (getUser.equals("admin") && getPassword.equals("1234")) {
                    Toast.makeText(Admin_Login.this, "Welcome", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Admin_Login.this, Create_Event.class));
                    finish();
                } else {
                    Toast.makeText(Admin_Login.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Admin_Login.this, Admin_Login.class));
                    finish();
                }
            }
            });
        }

}