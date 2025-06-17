package com.example.appcartochka;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


class  MyAdapter extends  RecyclerView.Adapter<MyViewHolder> {
    private  final  RecyclerViewInterface recyclerViewInterface;
    Context context;
    List<item> items;

    public MyAdapter(Context context, List<item> items, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.items = items;
        this.recyclerViewInterface = recyclerViewInterface;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view =  LayoutInflater.from(context).inflate(R.layout.item_view,parent,false);

       return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nameView.setText(items.get(position).getName());
        holder.dateView.setText(items.get(position).getDate());
        holder.haltView.setText(items.get(position).getAddress());


        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewInterface.onDeleteClick(items.get(holder.getAdapterPosition()).getId());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(recyclerViewInterface != null){
                    int pos = holder.getAdapterPosition();


                    if(pos != RecyclerView.NO_POSITION){
                        recyclerViewInterface.onItemClick(pos);
                    }
                }
            }
        });
    }


    @SuppressLint("NotifyDataSetChanged")
    public void refreshData(List<item> Items){
        items = Items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(items==null) return 0;
        return items.size();
    }
}


public class MyViewHolder  extends RecyclerView.ViewHolder {
    TextView nameView;
    TextView haltView;
    TextView dateView;

    CardView deleteButton;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        nameView = itemView.findViewById(R.id.nameView);

        haltView = itemView.findViewById(R.id.haltView);

        dateView = itemView.findViewById(R.id.dateView);

        deleteButton = itemView.findViewById(R.id.deleteIcon);

    }
}
