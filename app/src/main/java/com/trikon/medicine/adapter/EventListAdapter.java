package com.trikon.medicine.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.trikon.medicine.R;
import com.trikon.medicine.appdata.GlobalAppAccess;
import com.trikon.medicine.model.Event;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.MyViewHolder> implements Filterable {

    private List<Event> shopStocks;
    private List<Event> originalList;


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_item_name, tv_item_details;
        SimpleDraweeView draweeView ;

        CardView item_container;

        MyViewHolder(View view) {
            super(view);

            item_container = view.findViewById(R.id.item_container);

            tv_item_name = view.findViewById(R.id.tv_item_name);
            tv_item_details = view.findViewById(R.id.tv_item_details);
            draweeView = (SimpleDraweeView) view.findViewById(R.id.my_image_view);


        }




    }


    public EventListAdapter(List<Event> stockList) {
        this.shopStocks = stockList;
        this.originalList = stockList;


    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Event rawStock = shopStocks.get(position);

        holder.tv_item_name.setText(rawStock.getEventName());
       // holder.tv_item_price.setText(String.valueOf(rawStock.getPrice()));
        if(rawStock.getImageUrl() != null){
            Uri uri = Uri.parse(GlobalAppAccess.BASE_URL_2 + rawStock.getImageUrl());

            holder.draweeView.setImageURI(uri);
        }

        holder.tv_item_details.setText(rawStock.getDescription());


       /* if (position % 2 == 0)
            holder.item_container.setBackgroundColor(ContextCompat.getColor(holder.item_container.getContext(), R.color.gray_lightest));
        else
            holder.item_container.setBackgroundColor(ContextCompat.getColor(holder.item_container.getContext(), R.color.white));*/

       // holder.bindClick(rawStock, listener);
       // holder.setForSold(rawStock);

    }

    @Override
    public int getItemCount() {
        return shopStocks.size();
    }




    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                shopStocks = (List<Event>) results.values;
                EventListAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Event> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = originalList;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }

    protected List<Event> getFilteredResults(String constraint) {
        List<Event> results = new ArrayList<>();

        for (Event item : originalList) {

            if (item.getEventName().toLowerCase().contains(constraint)) {
                results.add(item);
            }

        }
        return results;
    }
}
