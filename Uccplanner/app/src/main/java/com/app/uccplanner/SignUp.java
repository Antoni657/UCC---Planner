package com.app.uccplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {


     EditText RegPassword, RegEmail, RegFname;
     CheckBox isUser, isAdmin;

     Button register;
     FirebaseAuth mAuth;
     FirebaseFirestore mStore;
    TextView textSignup;
    ProgressBar progressBar;
    Handler h = new Handler();
    boolean valid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setStatusBarColor(Color.GREEN);

        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        RegFname = findViewById(R.id.fName);
        RegEmail = findViewById(R.id.email);
        RegPassword = findViewById(R.id.password);
        register = findViewById(R.id.btn_signup);
        textSignup = findViewById(R.id.login);
        isAdmin = findViewById(R.id.adminCheck);
        isUser = findViewById(R.id.userCheck);

        isUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    isAdmin.setChecked(false);
                }
            }
        });

        isAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    isUser.setChecked(false);
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkField(RegEmail);
                checkField(RegPassword);
                checkField(RegFname);

                if(!(isUser.isChecked() || isAdmin.isChecked())){
                    Toast.makeText(SignUp.this, "Select the account type", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(valid){
                    mAuth.createUserWithEmailAndPassword(RegEmail.getText().toString(),RegPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignUp.this, "Account Created", Toast.LENGTH_SHORT).show();
                            DocumentReference df = mStore.collection("Users").document(user.getUid());
                            Map<String,Object> userInfo = new HashMap<>();
                            userInfo.put("Fullname", RegFname.getText().toString());
                            userInfo.put("Student No", "");
                            userInfo.put("Course", "");
                            userInfo.put("Year & Sec", "");
                            userInfo.put("UserEmail", RegEmail.getText().toString());

                            if(isUser.isChecked()){
                                userInfo.put("isUser", "1");


                            }
                            if(isAdmin.isChecked()){
                                userInfo.put("isAdmin", "1");
                            }

                            df.set(userInfo);
                            if(isUser.isChecked()){
                                startActivity(new Intent(getApplicationContext(), userlist.class));
                                finish();
                            }
                            if(isAdmin.isChecked()){
                                startActivity(new Intent(getApplicationContext(), Admin_recyclerview.class));
                                finish();
                            }
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUp.this, "Failed to Create Account, Gmail is already have an account", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        textSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), login_form.class));
            }
        });

    }
    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("This field is required");
            valid = false;
        }else {
            valid = true;
        }
        return valid;
    }
}