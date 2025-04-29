package com.app.uccplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class user_profile extends AppCompatActivity {
    Button update, back;
    EditText name, num, course, year;
    TextView user;
    FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    boolean valid = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile2);

        getWindow().setStatusBarColor(Color.GREEN);


        user = findViewById(R.id.UsernameEmail);
        name = findViewById(R.id.uploadName);
        course = findViewById(R.id.uploadCourse);
        num = findViewById(R.id.uploadNo);
        year = findViewById(R.id.uploadYear);
        update = findViewById(R.id.updateUser);
        back = findViewById(R.id.back);



//        Intent intent = getIntent();
//        String Email = intent.getStringExtra("email");

//        user.setText(Email);
//
//
//        // Initialize Firebase
//
//         FirebaseDatabase.getInstance().getReference("Users").child(sanitizedEmail);



        DocumentReference docRef = mStore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());



        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Get the string value from the field
                    String Fname = document.getString("Fullname");
                    String Email = document.getString("UserEmail");
                    String Year = document.getString("Year & Sec");
                    String Course = document.getString("Course");
                    String Num = document.getString("Student No");

                    user.setText(Email);
                    name.setText(Fname);
                    course.setText(Course);
                    num.setText(Num);
                    year.setText(Year);

                    checkField(course);
                    checkField(num);
                    checkField(year);
                    return;
                } else {
                    Log.d("TAG", "Document does not exist");
                }
            } else {
                Log.d("TAG", "Error getting document: " + task.getException());
            }
        });



        // Get references to views


        // Set OnClickListener for save button
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Course = course.getText().toString();
                String Num = num.getText().toString();
                String Year = year.getText().toString();


                Map<String, Object> updates = new HashMap<>();
                updates.put("Course", Course);
                updates.put("Student No", Num);
                updates.put("Year & Sec", Year);



                docRef.update(updates);

                Toast.makeText(user_profile.this, "User successfully updated", Toast.LENGTH_SHORT).show();


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), userlist.class));
                finish();
            }
        });

        // Retrieve and display user profile data

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




