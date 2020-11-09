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
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    String[] HospitalName;
    String[] HospitalDescription;
    String[] HospitalNameAll;


    public RecyclerAdapter(String[] hospitalName, String[] hospitalDescription) {
        HospitalName = hospitalName;
        HospitalDescription = hospitalDescription;
        HospitalNameAll = hospitalName;
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

        holder.TVHospitalName.setText(""+ HospitalName[position]);
        holder.TVHospitalDescription.setText(""+ HospitalDescription[position]);


    }

    @Override
    public int getItemCount() {
        return HospitalName.length;
    }


    class ViewHolder extends RecyclerView.ViewHolder{


        TextView TVHospitalName, TVHospitalDescription;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            TVHospitalName = itemView.findViewById(R.id.TVHospitalName);
            TVHospitalDescription = itemView.findViewById(R.id.TVHospitalDescription);
            
        }
    }

}
