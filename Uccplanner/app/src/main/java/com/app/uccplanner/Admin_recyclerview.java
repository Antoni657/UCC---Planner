package com.app.uccplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.uccplanner.Adapters.Admin_adapter;
import com.app.uccplanner.Adapters.MyAdapter;
import com.app.uccplanner.Models.EventModels;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin_recyclerview extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference database;
    Admin_adapter admin_adapter;
    ArrayList<User> list;
    SearchView searchView;
    FloatingActionButton create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_recyclerview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        recyclerView = findViewById(R.id.admin);
        database = FirebaseDatabase.getInstance().getReference("Events");
//        searchView = findViewById(R.id.search);
        create = findViewById(R.id.fabCreate);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AlertDialog.Builder builder = new AlertDialog.Builder(Admin_recyclerview.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        list = new ArrayList<>();
        admin_adapter = new Admin_adapter(this, list);
        recyclerView.setAdapter(admin_adapter);


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    user.setKey(dataSnapshot.getKey());
                    list.add(user);
                }
                admin_adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });




        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Admin_recyclerview.this, Create_Event.class));

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case
                    R.id.profile:

                startActivity(new Intent(getApplicationContext(), user_profile2.class));
                finish();

                break;

            case
                    R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), login_form.class ));
                finish();
                break;

            default:
                break;
        }

        return true;
    }

}