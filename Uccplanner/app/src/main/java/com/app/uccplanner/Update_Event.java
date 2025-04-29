package com.app.uccplanner;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Update_Event extends AppCompatActivity {
    TextView day,title,place,month,time,host, desc;
    Button create, dateButton, timeButton, add;
    ImageView image;
    int hour, minute;
    String url;
    String key, oldUrl;
    Uri uri;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    Calendar cal = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event);
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
        Button back = findViewById(R.id.back);

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
                            Toast.makeText(Update_Event.this, "No Image Selected",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            Glide.with(Update_Event.this).load(bundle.getString("Image")).into(image);
            title.setText(bundle.getString("Title"));
            place.setText(bundle.getString("Place"));
            desc.setText(bundle.getString("Desc"));
            host.setText(bundle.getString("Host"));
            long timestamp = getIntent().getLongExtra("Timestamp", 0);
            key = bundle.getString("key");
            oldUrl = bundle.getString("Image");

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy", Locale.getDefault());
            String dateString = sdf.format(new Date(timestamp));
            dateButton.setText(dateString);

            SimpleDateFormat time = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            String timeString = time.format(new Date(timestamp));
            timeButton.setText(timeString);








        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });



        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String Desc = desc.getText().toString();
                String Host = host.getText().toString();

                String Place = place.getText().toString();

                String Title = title.getText().toString();
                String Date = dateButton.getText().toString();
                String Time = timeButton.getText().toString();
                Drawable drawable = image.getDrawable();
                oldUrl = getIntent().getStringExtra("Image");

                if (!Desc.isEmpty() && !Host.isEmpty() && !Place.isEmpty() && !Time.isEmpty() && !Title.isEmpty() && !Date.isEmpty() && !Time.isEmpty()){


                    Uri imageUri = uri;

                    if (imageUri == null || imageUri.toString().isEmpty()) {
                        // Image container is empty
                        // Add your code here to handle the condition
                        Toast.makeText(Update_Event.this, "You need to add an image again!", Toast.LENGTH_SHORT).show();
                        return;

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
                startActivity(new Intent(getApplicationContext(), Admin_recyclerview.class));
            }

        });


    }


    private void initDatePicker(){
        long timestamp = getIntent().getLongExtra("Timestamp", 0);
        Date newDate = new Date(timestamp);
        cal.setTime(newDate);

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






        datePickerDialog = new DatePickerDialog(Update_Event.this, dateSetListener, year, month, day);


    }
    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) +" " + day + " " + year;
    }
    private String getMonthFormat(int month){

        if(month == 0)
            return "Jan";
        if(month == 1)
            return "Feb";
        if(month == 2)
            return "Mar";
        if(month == 3)
            return "Apr";
        if(month == 4)
            return "May";
        if(month == 5)
            return "Jun";
        if(month == 6)
            return "Jul";
        if(month == 7)
            return "Aug";
        if(month == 8)
            return "Sep";
        if(month == 9)
            return "Oct";
        if(month == 10)
            return "Nov";
        if(month == 11)
            return "Dec";

        return "Jan";
    }
    public void openDatePicker(View view){
        datePickerDialog.show();
    }
    public void openTimePicker(View view){
        long timestamp = getIntent().getLongExtra("Timestamp", 0);
        Date newDate = new Date(timestamp);
        cal.setTime(newDate);
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectHour, int selectMinute) {

                hour = selectHour;
                minute = selectMinute;

                timeButton.setText(String.format(Locale.getDefault(),"%02d:%02d", hour,minute));

                cal.set(Calendar.HOUR_OF_DAY,selectHour);
                cal.set(Calendar.MINUTE,selectMinute);





            }
        };int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(Update_Event.this, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Time");
        timePickerDialog.show();

    }
    public void saveData(){

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Android Images")
                .child(uri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(Update_Event.this);
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
                String Desc = desc.getText().toString().trim();
                String Place = place.getText().toString().trim();
                String Host = host.getText().toString().trim();
                String Title = title.getText().toString().trim();
                long timestamp = cal.getTimeInMillis();


                Map<String, Object> dataMap = new HashMap<>();

                dataMap.put("creator", Creator);
                dataMap.put("desc", Desc);
                dataMap.put("host", Host);
                dataMap.put("place", Place);
                dataMap.put("timestamp", timestamp);
                dataMap.put("title", Title);
                dataMap.put("url", url);
                long timestamp1 = getIntent().getLongExtra("Timestamp", 0);

                FirebaseDatabase.getInstance().getReference("Events").child(String.valueOf(timestamp)).setValue(dataMap)
                        .addOnSuccessListener(aVoid -> {
                            Log.d("TAG", "Data updated successfully");

                            if (timestamp == timestamp1){
                                StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldUrl);
                                reference.delete();
                                Toast.makeText(Update_Event.this, "Updated", Toast.LENGTH_SHORT).show();



                            }

                            else if (task.isSuccessful()){


                                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Events");
                                FirebaseStorage storage = FirebaseStorage.getInstance();

                                StorageReference storageReference = storage.getReferenceFromUrl(oldUrl);
                                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        reference.child(String.valueOf(timestamp1)).removeValue();

                                        Toast.makeText(Update_Event.this, "deleted", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                            finish();

                        })
                        .addOnFailureListener(e -> {
                            Log.e("TAG", "Error updating data", e);

                            Toast.makeText(Update_Event.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
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