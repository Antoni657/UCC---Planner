package com.app.uccplanner.Adapters;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.uccplanner.EventsMainPage;
import com.app.uccplanner.R;
import com.app.uccplanner.User;
import com.bumptech.glide.Glide;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<User> list;

    public MyAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);


        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {






        User user = list.get(position);


        holder.title.setText(user.getTitle());
        holder.place.setText(user.getPlace());
        holder.count.setText(user.getCount());
        Glide.with(holder.imageView).load(user.getUrl()).into(holder.imageView);

        SimpleDateFormat Month = new SimpleDateFormat("MMM", Locale.getDefault());
        String formatMonth = Month.format(new Date(user.getTimestamp()));
        holder.month.setText(formatMonth);





        SimpleDateFormat Day = new SimpleDateFormat("dd", Locale.getDefault());
        String formatDay = Day.format(new Date(user.getTimestamp()));
        holder.day.setText(formatDay);

//        SimpleDateFormat Year = new SimpleDateFormat("yyyy",Locale.getDefault());
//        String formatYear = Year.format(new Date(user.getTimestamp()));






        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                Intent intent = new Intent(view.getContext(), EventsMainPage.class);

                intent.putExtra("date",formatDay);
                intent.putExtra("month",formatMonth);
                intent.putExtra("title",user.getTitle());
                intent.putExtra("place",user.getPlace());
                intent.putExtra("count",user.getCount());
                intent.putExtra("url",user.getUrl());
                intent.putExtra("time",formatTimestamp(user.getTimestamp()));
                intent.putExtra("host",user.getHost());
                intent.putExtra("creator",user.getCreator());
                intent.putExtra("desc",user.getDesc());
                intent.putExtra("timestamp",user.getTimestamp());

                view.getContext().startActivity(intent);
//                ((Activity)context).finish();

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void searchDataList (ArrayList<User> searchList){
        list = searchList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        Button btnView;
        TextView day,count,title,place,month,time;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day);
            month = itemView.findViewById(R.id.month);
            time = itemView.findViewById(R.id.time);
            title = itemView.findViewById(R.id.eventTitle);
            place = itemView.findViewById(R.id.location);
            count = itemView.findViewById(R.id.Count);
            imageView = itemView.findViewById(R.id.card_image);
            btnView = itemView.findViewById(R.id.btnViewEvent);
        }
    }
    public static String formatTimestamp(long timestamp){
        SimpleDateFormat Times = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return Times.format(new Date(timestamp));
    }


}
