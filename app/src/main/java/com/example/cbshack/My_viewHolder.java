package com.example.cbshack;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class My_viewHolder extends RecyclerView.ViewHolder {

    public TextView title;


    public My_viewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.title_med);

    }
}
