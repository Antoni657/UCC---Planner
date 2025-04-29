package com.app.uccplanner.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.uccplanner.DeleteDetails;
import com.app.uccplanner.EventsMainPage;
import com.app.uccplanner.R;
import com.app.uccplanner.User;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Admin_adapter extends RecyclerView.Adapter<Admin_adapter.MyViewHolder> {

    Context context;
    ArrayList<User> list;

    public Admin_adapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item,parent,false);
        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = list.get(position);


        holder.title.setText(user.getTitle());
//        holder.place.setText(user.getPlace());
//        holder.count.setText(user.getCount());
        Glide.with(holder.imageView).load(user.getUrl()).into(holder.imageView);

        SimpleDateFormat Month = new SimpleDateFormat("MMM", Locale.getDefault());
        String formatMonth = Month.format(new Date(user.getTimestamp()));
        holder.month.setText(formatMonth);





        SimpleDateFormat Day = new SimpleDateFormat("dd", Locale.getDefault());
        String formatDay = Day.format(new Date(user.getTimestamp()));
        holder.day.setText(formatDay);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DeleteDetails.class);

                intent.putExtra("date",formatDay);
                intent.putExtra("month",formatMonth);
                intent.putExtra("title",list.get(holder.getAbsoluteAdapterPosition()).getTitle());
                intent.putExtra("url",list.get(holder.getAbsoluteAdapterPosition()).getUrl());
                intent.putExtra("Key",list.get(holder.getAbsoluteAdapterPosition()).getKey());
                intent.putExtra("host",user.getHost());
                intent.putExtra("creator",user.getCreator());
                intent.putExtra("desc",user.getDesc());
                intent.putExtra("time",formatTimestamp(user.getTimestamp()));
                intent.putExtra("timestamp",user.getTimestamp());
                intent.putExtra("place",user.getPlace());
                intent.putExtra("count",user.getCount());

                v.getContext().startActivity(intent);

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

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView view;
        TextView day,count,title,place,month;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day);
            month = itemView.findViewById(R.id.month);
            title = itemView.findViewById(R.id.eventTitle);
            place = itemView.findViewById(R.id.location);
            count = itemView.findViewById(R.id.Count);
            imageView = itemView.findViewById(R.id.card_image);
            view = itemView.findViewById(R.id.adminCard);



        }
    }
    public static String formatTimestamp(long timestamp){
        SimpleDateFormat Times = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return Times.format(new Date(timestamp));
    }
}
