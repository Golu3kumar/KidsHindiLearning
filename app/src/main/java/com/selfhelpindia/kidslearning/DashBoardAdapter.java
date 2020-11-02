package com.selfhelpindia.kidslearning;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DashBoardAdapter extends RecyclerView.Adapter<DashBoardAdapter.ViewHolder>  {
    private List<DashboardList> myListList;
    private Context ct;
    public DashBoardAdapter(List<DashboardList> myListList, Context ct) {
        this.myListList = myListList;
        this.ct = ct;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.images, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DashboardList myList = myListList.get(position);
        holder.imageView.setImageDrawable(ct.getResources().getDrawable(myList.getImage()));
        holder.textView.setText(myList.getName());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ct,SlideImages.class);
                intent.putExtra("dash_name",myList.getName());
                ct.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myListList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.myimage);
            textView = itemView.findViewById(R.id.name);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

}
