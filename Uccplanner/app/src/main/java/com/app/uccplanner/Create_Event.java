package com.app.uccplanner;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.uccplanner.Models.CreateEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Create_Event extends AppCompatActivity {
    TextView day,title,place,month,time,host, desc;
    Button create, dateButton, timeButton, add, back;
    ImageView image;
    int hour, minute;
    String url;
    Uri uri;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;


    Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
//        day = findViewById(R.id.uploadDay);
        title = findViewById(R.id.uploadTitle);
        place = findViewById(R.id.uploadPlace);
//        month = findViewById(R.id.uploadDate);
//        time = findViewById(R.id.uploadTime);
        host = findViewById(R.id.uploadHost);
        desc = findViewById(R.id.uploadDesc);
        create = findViewById(R.id.createEvent);
        image = findViewById(R.id.uploadImg);
        dateButton = findViewById(R.id.DatePickerButton);
        timeButton = findViewById(R.id.timeButton);
        back = findViewById(R.id.back);

        initDatePicker();


        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            image.setImageURI(uri);
                        }else {
                            Toast.makeText(Create_Event.this, "No Image Selected",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Create_Event.this, SignUp.class));
//
//            }
//        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String Day = day.getText().toString();
                String Desc = desc.getText().toString();
                String Host = host.getText().toString();
//                String Month = month.getText().toString();
                String Place = place.getText().toString();

                String Title = title.getText().toString();
                String Date = dateButton.getText().toString();
                String Time = timeButton.getText().toString();
                Drawable drawable = image.getDrawable();


            if (!Desc.isEmpty() && !Host.isEmpty() && !Place.isEmpty() && !Time.isEmpty() && !Title.isEmpty() && !Date.isEmpty() && !Time.isEmpty()){


                Uri imageUri = uri;

                if (imageUri == null || imageUri.toString().isEmpty()) {
                    // Image container is empty
                    // Add your code here to handle the condition
                    recreate();
                    Toast.makeText(Create_Event.this, "You need to add an image!", Toast.LENGTH_SHORT).show();
                } else {
                    // Image container is not empty
                    // Execute the desired action
                    saveData();
                }
                    // Handle the case when the Uri is null




            } else{

//                if (Day.isEmpty()) {
//                    day.setError("");

//                }

                if (Desc.isEmpty()) {
                    desc.setError("Please fill this area");

                }
                if (Host.isEmpty()) {
                    host.setError("Please fill this area");

                }
//                if (Month.isEmpty()) {
//                    month.setError("");

//                }
                if (Place.isEmpty()) {
                    place.setError("Please fill this area");

                }
//                if (Time.isEmpty()) {
//                    time.setError("");
//
//                }
                if (Title.isEmpty()) {
                    title.setError("Please fill this area");

                }
                if(Date.isEmpty()){
                    dateButton.setError("Please fill this area");
                }
                if(Time.isEmpty()){
                    timeButton.setError("Please fill this area");
                }
            }

            }
        });


    }


    private void initDatePicker(){

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day ) {


                String date = makeDateString(day, month, year);
                dateButton.setText(date);

                cal.set(Calendar.YEAR,year);
                cal.set(Calendar.MONTH,month);
                cal.set(Calendar.DAY_OF_MONTH,day);
                cal.set(Calendar.HOUR_OF_DAY,0);
                cal.set(Calendar.MINUTE,0);
                cal.set(Calendar.SECOND,0);
                cal.set(Calendar.MILLISECOND,0);

//                Date dates = cal.getTime();
//                long timeInMillis = dates.getTime();
//                Timestamp timestamp = new Timestamp(timeInMillis);
//                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//                databaseReference.child("dates").push().setValue(timestamp);


            }
        };

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);



        datePickerDialog = new DatePickerDialog(Create_Event.this, dateSetListener, year, month, day);
    }
    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) +" " + day + " " + year;
    }
    private String getMonthFormat(int month){

        if(month == 0)
            return "JAN";
        if(month == 1)
            return "FEB";
        if(month == 2)
            return "MAR";
        if(month == 3)
            return "APR";
        if(month == 4)
            return "MAY";
        if(month == 5)
            return "JUN";
        if(month == 6)
            return "JULY";
        if(month == 7)
            return "AUG";
        if(month == 8)
            return "SEP";
        if(month == 9)
            return "OCT";
        if(month == 10)
            return "NOV";
        if(month == 11)
            return "DEC";

        return "JAN";
    }
    public void openDatePicker(View view){
        datePickerDialog.show();
    }
    public void openTimePicker(View view){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectHour, int selectMinute) {

                hour = selectHour;
                minute = selectMinute;

                timeButton.setText(String.format(Locale.getDefault(),"%02d:%02d", hour,minute));

                cal.set(Calendar.HOUR_OF_DAY,selectHour);
                cal.set(Calendar.MINUTE,selectMinute);

            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(Create_Event.this, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Time");
        timePickerDialog.show();

    }
    public void saveData(){

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Android Images")
                .child(uri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(Create_Event.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();


        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {



                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete()) ;
                    Uri urlImage = uriTask.getResult();
                    url = urlImage.toString();
                    uploadData();
                    dialog.dismiss();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }
    public void uploadData(){
        FirebaseFirestore mStore = FirebaseFirestore.getInstance();
        DocumentReference docRef = mStore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();

                    String Creator = document.getString("Fullname");
                    String Desc = desc.getText().toString();
                    String Place = place.getText().toString();
                    String Host = host.getText().toString();
                    String Title = title.getText().toString();
                    long timestamp = cal.getTimeInMillis();


                    Map<String, Object> dataMap = new HashMap<>();

                    dataMap.put("creator", Creator);
                    dataMap.put("desc", Desc);
                    dataMap.put("host", Host);
                    dataMap.put("place", Place);
                    dataMap.put("timestamp", timestamp);
                    dataMap.put("title", Title);
                    dataMap.put("url", url);

                    FirebaseDatabase.getInstance().getReference("Events").child(String.valueOf(timestamp)).setValue(dataMap)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("TAG", "Data inserted successfully");
                                Toast.makeText(Create_Event.this, "Created", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Admin_recyclerview.class));
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Log.e("TAG", "Error inserting data", e);

                                Toast.makeText(Create_Event.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Admin_recyclerview.class));
                                finish();
                            });
                    return;

            } else {
                Log.d("TAG", "Error getting document: " + task.getException());
            }
        });



    }



}