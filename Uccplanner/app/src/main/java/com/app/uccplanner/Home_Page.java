package com.app.uccplanner;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.app.uccplanner.Adapters.EventsRecyclerViewAdapter;
import com.app.uccplanner.Models.EventModels;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Home_Page extends AppCompatActivity {
    ArrayList<EventModels> list ;
    RecyclerView recyclerView;
     DatabaseReference database;

    EventsRecyclerViewAdapter eventsRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        getWindow().setStatusBarColor(Color.GREEN);
        recyclerView = findViewById(R.id.recyclerView);
        database = FirebaseDatabase.getInstance().getReference("EvenModels");
        recyclerView.setHasFixedSize(true);

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        eventsRecyclerViewAdapter = new EventsRecyclerViewAdapter(EventsRepos.getEventsRepo().getEventModelsList());
//        Log.i("data", ""+EventsRepos.getEventsRepo().getEventModelsList().size());
//        recyclerView.setLayoutManager(linearLayoutManager);
//         recyclerView.setAdapter(eventsRecyclerViewAdapter);



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        eventsRecyclerViewAdapter = new EventsRecyclerViewAdapter( this, list);
        recyclerView.setAdapter(eventsRecyclerViewAdapter);

         database.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {

                 for (DataSnapshot dataSnapshot : snapshot.getChildren())
                 {
                    EventModels eventModels = dataSnapshot.getValue(EventModels.class);
                    list.add(eventModels);
                 }
                 eventsRecyclerViewAdapter.notifyDataSetChanged();
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {



             }
         });


    }



}