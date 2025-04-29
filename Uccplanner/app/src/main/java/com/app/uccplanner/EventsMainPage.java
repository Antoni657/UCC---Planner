package com.app.uccplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventsMainPage extends AppCompatActivity {
    TextView date,month,title,place,count, desc, host, time, creator;



    ImageView imageView, back;
    MaterialButton join, cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.GREEN);
        setContentView(R.layout.activity_events_main_page);






        date = findViewById(R.id.day);
        month = findViewById(R.id.month);
        title = findViewById(R.id.eventTitle);
        place = findViewById(R.id.location);

        time = findViewById(R.id.time);
        host = findViewById(R.id.host);
        creator = findViewById(R.id.creator);
        desc = findViewById(R.id.desc);
        imageView = findViewById(R.id.card_image);
        back = findViewById(R.id.btnBack);
        join = findViewById(R.id.btnJoinEvent);
        cancel = findViewById(R.id.btnCancelEvent);

        Intent intent = getIntent();
        String Day = intent.getExtras().getString("date");
        String Title = intent.getExtras().getString("title");
        String Month = intent.getExtras().getString("month");
        String Place = intent.getExtras().getString("place");
        String Count = intent.getExtras().getString("count");
        String Image = intent.getStringExtra("url");
        String Time = intent.getStringExtra("time");
        String Desc = intent.getStringExtra("desc");
        String Host = intent.getStringExtra("host");
        String Users = intent.getStringExtra("users");
        String Creator = intent.getStringExtra("creator");






        title.setText(Title);
        date.setText(Day);
        month.setText(Month);
        place.setText(Place);

        creator.setText(Creator);
        time.setText(Time);
        host.setText(Host);
        desc.setText(Desc);
        Glide.with(this).load(Image).into(imageView);



        long eventTimeInMillis = intent.getExtras().getLong("timestamp");
        long advanceTime = eventTimeInMillis - (30 * 60 * 1000);
        AlarmManager alarmManager = (AlarmManager) EventsMainPage.this.getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(EventsMainPage.this  , NotificationReceiver.class);
        notificationIntent.putExtra("title", Title);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(EventsMainPage.this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(EventsMainPage.this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);



        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                alarmManager.setExact(AlarmManager.RTC_WAKEUP, eventTimeInMillis, pendingIntent1);

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, advanceTime, pendingIntent2);

                Toast.makeText(EventsMainPage.this, "You successfully joined the event on   " + Month + " " + Day + ", at " + Time, Toast.LENGTH_LONG).show();


                FirebaseFirestore mStore = FirebaseFirestore.getInstance();
                DocumentReference docRef = mStore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());


                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String Fname = documentSnapshot.getString("Fullname");
                            String Course = documentSnapshot.getString("Course");
                            String Year_sec = documentSnapshot.getString("Year & Sec");
                            String StudNo = documentSnapshot.getString("Student No");
                            String Gmail = documentSnapshot.getString("UserEmail");

                            joinEvent(Fname, Course, Year_sec, StudNo, Gmail);
                        }
                    }
                });

                finish();
            }
        });

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String Fname = sharedPreferences.getString("fName", "");



        DatabaseReference participantsRef = FirebaseDatabase.getInstance().getReference()
                .child("Participants")
                .child(Title);

        participantsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        HashMap<String, Object> participantsMap = (HashMap<String, Object>) dataSnapshot.getValue();

                        boolean isUserJoined = false;

                            for (Map.Entry<String, Object> entry : participantsMap.entrySet()) {
                                HashMap<String, Object> userData = (HashMap<String, Object>) entry.getValue();
                                String Fullname = (String) userData.get("Fullname");
                                if (Fullname.equals(Fname)) {
                                    // The user's email exists in the participants list
                                    isUserJoined = true;
                                    break;

                                }
                            }


                            if (isUserJoined) {

                                join.setVisibility(View.GONE);
                                cancel.setVisibility(View.VISIBLE);


                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("Participants").child(Title);
                                        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    HashMap<String, Object> participantsMap = (HashMap<String, Object>) dataSnapshot.getValue();



                                                    for (Map.Entry<String, Object> entry : participantsMap.entrySet()) {
                                                        HashMap<String, Object> userData = (HashMap<String, Object>) entry.getValue();
                                                        String key = entry.getKey();
                                                        String Fullname = (String) userData.get("Fullname");
                                                        if (Fullname.equals(Fname)) {
                                                            // The user's email exists in the participants list
                                                            DatabaseReference participantRef = participantsRef.child(key);
                                                            participantRef.removeValue();

                                                            break;

                                                        }
                                                    }


                                                    finish(); // Finish the current activity


                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                // Handle the error scenario here
                                            }
                                        });


                                        alarmManager.cancel(pendingIntent1);
                                        alarmManager.cancel(pendingIntent2);

                                    }
                                });


                            }else {

                                join.setVisibility(View.VISIBLE);
                                cancel.setVisibility(View.GONE);
                            }

                        }
                    }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });






        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                startActivity(new Intent(EventsMainPage.this, userlist.class));
                finish();

            }
        });

    }
    private void joinEvent(String Fname, String Course, String Year, String StudNo, String gmail){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference pRef = db.getReference("Participants");



        HashMap<String, Object> participantMap = new HashMap<>();
        participantMap.put("Fullname", Fname);
        participantMap.put("Course", Course);
        participantMap.put("Student No", StudNo);
        participantMap.put("Year & Sec", Year);
        participantMap.put("UserEmail", gmail);


        Intent intent = getIntent();
        String Title = intent.getExtras().getString("title");
        pRef.child(Title).child(Fname).setValue(participantMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EventsMainPage.this, "Participant added succesfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EventsMainPage.this, "Can't add participants", Toast.LENGTH_SHORT).show();
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

                Toast.makeText(this,"userProfile",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(EventsMainPage.this, user_profile.class));
            finish();
                break;

            case
                    R.id.logout:
                    startActivity(new Intent(EventsMainPage.this, login_form.class ));
                    finish();
                break;

            default:

                break;
        }

        return true;
    }


}