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

import com.app.uccplanner.EventsMainPage;
import com.app.uccplanner.Home_Page;
import com.app.uccplanner.Models.EventModels;
import com.app.uccplanner.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

 public class EventsRecyclerViewAdapter extends RecyclerView.Adapter<EventsRecyclerViewAdapter.ViewHolder> {

         Context context;
     ArrayList<EventModels> list;
     public EventsRecyclerViewAdapter(Context context, ArrayList<EventModels> list) {
         this.context = context;
         this.list = list;
     }



     @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(context).inflate(R.layout.event_card,parent,false);
         return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

         EventModels eventModels = list.get(position);
        holder.date.setText(eventModels.getDay());
        holder.month.setText(eventModels.getMonth());
        holder.title.setText(eventModels.getTitle());
        holder.place.setText(eventModels.getPlace());
        holder.count.setText(eventModels.getCount());
        Glide.with(holder.imageView).load(eventModels.getUrl()).into(holder.imageView);

        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), EventsMainPage.class);
                intent.putExtra("date",eventModels.getDay());
                intent.putExtra("month",eventModels.getMonth());
                intent.putExtra("title",eventModels.getTitle());
                intent.putExtra("place",eventModels.getPlace());
                intent.putExtra("count",eventModels.getCount());

                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        Button btnView;
        TextView date,month,title,place,count;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.day);
            month = itemView.findViewById(R.id.month);
            title = itemView.findViewById(R.id.eventTitle);
            place = itemView.findViewById(R.id.location);
            count = itemView.findViewById(R.id.Count);
            imageView = itemView.findViewById(R.id.card_image);
            btnView = itemView.findViewById(R.id.btnViewEvent);



        }



    }
}
