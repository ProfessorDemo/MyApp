package com.example.myapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    List<String> HospitalName;
    List<String> HospitalDescription;
    List<String> HospitalNameAll;
    List<String> HospitalDescriptionAll;


    public RecyclerAdapter(List<String> hospitalName, List<String> hospitalDescription) {
        this.HospitalName = hospitalName;
        this.HospitalDescription = hospitalDescription;
        this.HospitalNameAll = new ArrayList<>(hospitalName);
        this.HospitalDescriptionAll = new ArrayList<>(hospitalDescription);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.hospital_names, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.TVHospitalName.setText(""+ HospitalName.get(position));
        holder.TVHospitalDescription.setText(""+ HospitalDescription.get(position));


    }

    @Override
    public int getItemCount() {
        return HospitalName.size();
    }





   void FR (String NXT) {
       FilterName filterName = new FilterName();
       FilterDesc filterDesc = new FilterDesc();
       filterDesc.getFilter().filter(NXT);
       filterName.getFilter().filter(NXT);
   }


    class ViewHolder extends RecyclerView.ViewHolder{


        TextView TVHospitalName, TVHospitalDescription;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            TVHospitalName = itemView.findViewById(R.id.TVHospitalName);
            TVHospitalDescription = itemView.findViewById(R.id.TVHospitalDescription);
            
        }
    }




    class FilterName implements Filterable{
        @Override
        public Filter getFilter() {
            return filter;
        }

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                List<String> filteredList = new ArrayList<>();

                if (constraint.toString().isEmpty()) {
                    filteredList.addAll(HospitalNameAll);
                } else {
                    int Count = 0;
                    for (String hospital : HospitalNameAll) {
                        if (hospital.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filteredList.add(hospital);
                            Count++;
                        }

                    }
                    if(Count == 0){
                        filteredList.add("Please Enter a Valid Hospital Detail");
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                HospitalName.clear();
                HospitalName.addAll((Collection<? extends String>) results.values);
                notifyDataSetChanged();

            }
        };




    }

    class FilterDesc implements Filterable{

        @Override
        public Filter getFilter() {
            return filter;
        }
         Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                List<String> filteredList = new ArrayList<>();

                if (constraint.toString().isEmpty()) {
                    filteredList.addAll(HospitalDescriptionAll);
                } else {
                    int Count = 0;
                    for (String hospital : HospitalDescriptionAll) {
                        if (hospital.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filteredList.add(hospital);
                            Count++;
                        }
                    }
                    if(Count == 0){
                        filteredList.add("Please Enter a Valid Hospital Detail");
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                HospitalDescription.clear();
                HospitalDescription.addAll((Collection<? extends String>) results.values);
                notifyDataSetChanged();

            }
        };

    }


}

