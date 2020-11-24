package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class Book1Activity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    Button BtnBook;
    android.widget.SearchView SVHospital;
    Spinner SPHospitalNames, SPHospitalTreatments;
    RecyclerView RVHospital;

    BottomNavigationView bottomNavigationView2;

    RecyclerAdapter recyclerAdapter;
    FirebaseFirestore mStore;
    private final String TAG = "Book1Activity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book1);

        BtnBook = findViewById(R.id.BtnBook);

        mStore = FirebaseFirestore.getInstance();

        SPHospitalTreatments = findViewById(R.id.SPHospitalTreatment);
        SPHospitalNames = findViewById(R.id.SPHospitalNames);
        SVHospital = findViewById(R.id.SVHospital);
        RVHospital = findViewById(R.id.RVHospital);
        SPHospitalTreatments.setVisibility(View.GONE);
        BtnBook.setVisibility(View.GONE);

        bottomNavigationView2 = findViewById(R.id.BottomNav2);

        bottomNavigationView2.setSelectedItemId(R.id.NavBook);
        bottomNavigationView2.setOnNavigationItemSelectedListener(this);





        SVHospital.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerAdapter.FR(newText);
                return false;
            }
        });





        final ProgressDialog pd = new ProgressDialog(Book1Activity.this);
        pd.setMessage("Loading...");
        pd.show();

        mStore.collection("Hospitals").orderBy("name").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    int x = 0;
                    for (QueryDocumentSnapshot ignored : task.getResult()) {
                        x++;
                    }

                    List<String> HospitalName = new ArrayList<>();
                    List<String> HospitalDesc = new ArrayList<>();

                    String[] HospitalIDList = new String[x+1];
                    String[] HospitalNameX = new String[x+1];
                    int i = 1;
                    HospitalNameX[0]= "Hospital";
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        HospitalIDList[i] = document.getId();
                        HospitalNameX[i] = document.getString("name");
                        HospitalName.add(document.getString("name"));
                        HospitalDesc.add(document.getString("aboutUs"));

                        i++;
                    }

                    ArrayAdapter adapter1 = new ArrayAdapter(Book1Activity.this, android.R.layout.simple_spinner_item, HospitalNameX);
                    adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    SPHospitalNames.setAdapter(adapter1);
                    SPHospitalNames.setOnItemSelectedListener(new SPHospitalNames());



                    recyclerAdapter = new RecyclerAdapter(HospitalName, HospitalDesc);
                    RVHospital.setAdapter(recyclerAdapter);
                    DividerItemDecoration Did = new DividerItemDecoration(Book1Activity.this, DividerItemDecoration.VERTICAL);
                    RVHospital.addItemDecoration(Did);
                    RVHospital.setLayoutManager(new LinearLayoutManager(Book1Activity.this));

                    pd.dismiss();

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }

            }
        });





        BtnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String HospitalName = (String) SPHospitalNames.getSelectedItem();
                String Treatment = (String) SPHospitalTreatments.getSelectedItem();
                Intent intent = new Intent(Book1Activity.this, Book2Activity.class);
                intent.putExtra("HospitalName", HospitalName);
                intent.putExtra("HospitalTreatment", Treatment);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.NavHome:
                startActivity(new Intent(Book1Activity.this,HomeActivity.class));
                break;

            case R.id.NavProfile:
                startActivity(new Intent(Book1Activity.this,ProfileActivity.class));
                break;

            case R.id.NavBook:
                break;

            case R.id.NavSetting:
                break;
        }
        return true;
    }



    class SPHospitalNames implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String Name = parent.getItemAtPosition(position).toString();
            if(Name !="Hospital"){
                //method.HospitalGetData(Info);
            mStore.collection("Hospitals").whereEqualTo("name", Name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String ID;
                        ID = document.getId();
                        mStore.collection("Hospitals").document(ID).collection("Treatments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                int x = 0;
                                for (QueryDocumentSnapshot ignored : task.getResult()) {
                                    x++;
                                }
                                String[] HospitalTreatment = new String[x+1];
                                String[] HospitalID = new String[x+1];
                                String[] HospitalTreatPrice = new String[x+1];
                                int i = 1;
                                HospitalTreatment[0]="Treatment";
                                HospitalID[0]="ID";
                                HospitalTreatPrice[0] = "--";
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    HospitalID[i] = document.getId();
                                    HospitalTreatment[i] =  document.getString("Name");
                                    HospitalTreatPrice[i] =  document.getString("Price");
                                    i++;
                                }


                                SPHospitalTreatments.setVisibility(View.VISIBLE);
                                ArrayAdapter adapter = new ArrayAdapter(Book1Activity.this, android.R.layout.simple_spinner_item, HospitalTreatment);
                                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                                SPHospitalTreatments.setAdapter(adapter);
                                SPHospitalTreatments.setOnItemSelectedListener(new SPHospitalTreat());
                            }

                        });
                    }
                }
            });
        }
        }


        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            SPHospitalTreatments.setVisibility(View.GONE);

        }


    }


    class SPHospitalTreat extends SPHospitalNames implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String Treatment = parent.getItemAtPosition(position).toString();
            if(Treatment!="Treatment") {
                BtnBook.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }

    }
}



