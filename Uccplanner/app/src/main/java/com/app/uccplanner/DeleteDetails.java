package com.app.uccplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;

public class DeleteDetails extends AppCompatActivity {

    FloatingActionButton delete, back, update;
    TextView date, month, title, place, count, time, host, creator, desc;
    ImageView imageView;
    String key = "";
    String imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_details);

        date = findViewById(R.id.day);
        month = findViewById(R.id.month);
        title = findViewById(R.id.eventTitle);
        place = findViewById(R.id.location);

        time = findViewById(R.id.time);
        host = findViewById(R.id.host);
        desc = findViewById(R.id.desc);
        creator = findViewById(R.id.creator);
        imageView = findViewById(R.id.card_image);
        delete = findViewById(R.id.fabDelete);
        back = findViewById(R.id.back);
        update = findViewById(R.id.fabEdit);

        Intent intent = getIntent();
        String Day = intent.getExtras().getString("date");
        String Title = intent.getExtras().getString("title");
        String Month = intent.getExtras().getString("month");
        String Place = intent.getExtras().getString("place");
        String Count = intent.getExtras().getString("count");
        String Time = intent.getStringExtra("time");
        String Desc = intent.getStringExtra("desc");
        String Host = intent.getStringExtra("host");
        String Creator = intent.getStringExtra("creator");
        long Timestamp = intent.getLongExtra("timestamp", 0);
//        Date timestampValue = new Date(timestamp);


        key = intent.getExtras().getString("Key");
        imageUrl = intent.getStringExtra("url");
        String Image = intent.getStringExtra("url");

        title.setText(Title);
        date.setText(Day);
        month.setText(Month);
        place.setText(Place);

        creator.setText(Creator);
        time.setText(Time);
        host.setText(Host);
        desc.setText(Desc);
        Glide.with(this).load(Image).into(imageView);


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DeleteDetails.this);
                builder.setMessage("Are you sure you want to delete?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Events");
                                FirebaseStorage storage = FirebaseStorage.getInstance();

                                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        reference.child(key).removeValue();
                                        Toast.makeText(DeleteDetails.this, "Succesfully Deleted!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();


            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DeleteDetails.this);
                builder.setMessage("Are you sure you want to update this?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(getApplicationContext(), Update_Event.class)
                                        .putExtra("Title", title.getText().toString())
                                        .putExtra("Day", date.getText().toString())
                                        .putExtra("Month", month.getText().toString())
                                        .putExtra("Place", place.getText().toString())
                                        .putExtra("Time", time.getText().toString())
                                        .putExtra("Desc", desc.getText().toString())
                                        .putExtra("Host", host.getText().toString())
                                        .putExtra("Creator", creator.getText().toString())
                                        .putExtra("Timestamp", Timestamp)
                                        .putExtra("Image", imageUrl)
                                        .putExtra("Key", key);

                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Admin_recyclerview.class));
                finish();

            }
        });
    }
}