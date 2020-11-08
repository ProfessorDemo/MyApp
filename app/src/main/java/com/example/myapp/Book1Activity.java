package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView.OnQueryTextListener;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.SearchView;
import android.widget.Spinner;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class Book1Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    SearchView SVHospital;
    Spinner SPHospitalNames, SPHospitalTreatments;
    RecyclerView RVHospital;
    RecyclerAdapter recyclerAdapter;
    FirebaseFirestore mStore;
    private final String TAG = "Book1Activity";


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book1);


        mStore = FirebaseFirestore.getInstance();

        SPHospitalTreatments = findViewById(R.id.SPHospitalTreatment);
        SPHospitalNames = findViewById(R.id.SPHospitalNames);
        SVHospital = findViewById(R.id.SVHospital);
        RVHospital = findViewById(R.id.RVHospital);
        SPHospitalTreatments.setVisibility(View.GONE);


        SVHospital.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });




        /*
        mStore.collection("Hospitals").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                    Log.i(TAG, myListOfDocuments.toString());
                }
            }
        });

         */


        mStore.collection("Hospitals").orderBy("Name").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int x = 0;
                    for (QueryDocumentSnapshot document : task.getResult()){
                        x++;
                    }

                    String[] HospitalIDList = new String[x];
                    String[] HospitalNameX = new String[x];
                    String[] HospitalDescriptionX = new String[x];
                    int i = 0;

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        HospitalIDList[i] = document.getId();
                        HospitalNameX[i] = document.getString("Name");
                        HospitalDescriptionX[i] = document.getString("About Us");


                        i++;
                    }

                    ArrayAdapter adapter1 = new ArrayAdapter(Book1Activity.this, android.R.layout.simple_spinner_item,HospitalNameX);
                    adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    SPHospitalNames.setAdapter(adapter1);

                    SPHospitalNames.setOnItemSelectedListener(Book1Activity.this);

                    recyclerAdapter = new RecyclerAdapter(HospitalNameX,HospitalDescriptionX);
                    RVHospital.setAdapter(recyclerAdapter);
                    DividerItemDecoration Did = new DividerItemDecoration(Book1Activity.this, DividerItemDecoration.VERTICAL);
                    RVHospital.addItemDecoration(Did);
                    RVHospital.setLayoutManager(new LinearLayoutManager(Book1Activity.this));


                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }

            }
        });























    }

    @Override
    public void onItemSelected(AdapterView<?> SPHospitalNames, View view, int position, long id) {
        String Info = SPHospitalNames.getItemAtPosition(position).toString();

        mStore.collection("Hospitals").whereEqualTo("Name",Info).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()){
                    String ID;
                    ID = document.getId();
                    mStore.collection("Hospitals").document(ID).collection("Treatments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            int x = 0;
                            for (QueryDocumentSnapshot document : task.getResult()){
                                x ++;
                            }
                            String[] HospitalTreatment = new String[x];
                            int i=0;
                            for(QueryDocumentSnapshot document :task.getResult()){
                                HospitalTreatment[i] = document.getId();
                                i++;
                            }
                            SPHospitalTreatments.setVisibility(View.VISIBLE);

                            ArrayAdapter adapter = new ArrayAdapter(Book1Activity.this, android.R.layout.simple_spinner_item,HospitalTreatment);
                            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            SPHospitalTreatments.setAdapter(adapter);
                        }
                    });
                }
            }
        });




    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        SPHospitalTreatments.setVisibility(View.GONE);

    }


}