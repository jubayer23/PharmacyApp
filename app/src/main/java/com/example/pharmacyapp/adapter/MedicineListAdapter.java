package com.example.pharmacyapp.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmacyapp.R;
import com.example.pharmacyapp.model.Event;
import com.example.pharmacyapp.model.Medicine;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class MedicineListAdapter extends RecyclerView.Adapter<MedicineListAdapter.MyViewHolder> implements Filterable {

    private List<Medicine> shopStocks;
    private List<Medicine> originalList;


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_brand_name, tv_strength, tv_dosage, tv_use_for;
        SimpleDraweeView draweeView ;

        CardView item_container;

        MyViewHolder(View view) {
            super(view);

            item_container = view.findViewById(R.id.item_container);

            tv_brand_name = view.findViewById(R.id.tv_brand_name);
            tv_strength = view.findViewById(R.id.tv_strength);
            tv_dosage = view.findViewById(R.id.tv_dosage);
            tv_use_for = view.findViewById(R.id.tv_use_for);
            //draweeView = (SimpleDraweeView) view.findViewById(R.id.my_image_view);


        }




    }


    public MedicineListAdapter(List<Medicine> stockList) {
        this.shopStocks = stockList;
        this.originalList = stockList;


    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medicine_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Medicine rawStock = shopStocks.get(position);

        holder.tv_brand_name.setText(rawStock.getBrandName());
        holder.tv_dosage.setText(rawStock.getDosage());
        holder.tv_strength.setText(rawStock.getStrength());
        holder.tv_use_for.setText(rawStock.getUseFor());



        if (position % 2 == 0)
            holder.item_container.setBackgroundColor(ContextCompat.getColor(holder.item_container.getContext(), R.color.gray_lightest));
        else
            holder.item_container.setBackgroundColor(ContextCompat.getColor(holder.item_container.getContext(), R.color.white));



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
                shopStocks = (List<Medicine>) results.values;
                MedicineListAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Medicine> filteredResults = null;
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

    protected List<Medicine> getFilteredResults(String constraint) {
        List<Medicine> results = new ArrayList<>();

        for (Medicine item : originalList) {

            if (item.getBrandName().toLowerCase().contains(constraint)) {
                results.add(item);
            }

        }
        return results;
    }
}
