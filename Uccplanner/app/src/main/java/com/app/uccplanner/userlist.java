    
package com.app.uccplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.app.uccplanner.Adapters.MyAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class userlist extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    MyAdapter myAdapter;
    ArrayList<User> list;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);
//        getWindow().setStatusBarColor(Color.GRAY);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);





        recyclerView = findViewById(R.id.userlist);
        database = FirebaseDatabase.getInstance().getReference("Events");
//        searchView = findViewById(R.id.search);
//        searchView.clearFocus();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AlertDialog.Builder builder = new AlertDialog.Builder(userlist.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        list = new ArrayList<>();
        myAdapter = new MyAdapter(this, list);
        recyclerView.setAdapter(myAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);

                    list.add(user);


                }
                myAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                searchList(newText);
//                return true;
//            }
//        });
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

                                    startActivity(new Intent(getApplicationContext(), user_profile.class));
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
//                        public void searchList(String text){
//        ArrayList<User> searchList = new ArrayList<>();
//        for(User user: list){
//            if (user.getTitle().toLowerCase().contains(text.toLowerCase())){
//                searchList.add(user);
//            }
//        }
//        myAdapter.searchDataList(searchList);
//                        }


}