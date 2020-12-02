package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class Book1Activity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    Button BtnBook,SelectDateBtn,YesBtn,NoBtn ;
    EditText PatName,PatPhone;
    TextView DateTV;
    android.widget.SearchView SVHospital;
    Spinner SPHospitalNames, SPHospitalTreatments;
    RecyclerView RVHospital;
    String UserID;
    FirebaseAuth mAuth;

    BottomNavigationView bottomNavigationView2;
    DatePickerDialog.OnDateSetListener mDateSetListener;

    RecyclerAdapter recyclerAdapter;
    FirebaseFirestore mStore;
    private final String TAG = "Book1Activity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book1);

        BtnBook = findViewById(R.id.BtnBook);
        SelectDateBtn = findViewById(R.id.SelectDateBtn);
        DateTV = findViewById(R.id.DateTV);
        YesBtn = findViewById(R.id.YesBtn);
        NoBtn = findViewById(R.id.NoBtn);
        PatName = findViewById(R.id.PatName);
        PatPhone = findViewById(R.id.PatPhone);


        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        SPHospitalTreatments = findViewById(R.id.SPHospitalTreatment);
        SPHospitalNames = findViewById(R.id.SPHospitalNames);
        SVHospital = findViewById(R.id.SVHospital);
        RVHospital = findViewById(R.id.RVHospital);

        RVHospital.setVisibility(View.GONE);
        SVHospital.setVisibility(View.GONE);
        PatPhone.setVisibility(View.GONE);
        PatName.setVisibility(View.GONE);


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


        YesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatPhone.setVisibility(View.GONE);
                PatName.setVisibility(View.GONE);
            }
        });

        NoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatPhone.setVisibility(View.VISIBLE);
                PatName.setVisibility(View.VISIBLE);
            }
        });












        SelectDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar myCalender = Calendar.getInstance();

                int year = myCalender.get(Calendar.YEAR);
                int month = myCalender.get(Calendar.MONTH);
                int day = myCalender.get(Calendar.DAY_OF_MONTH);
                //myCalender.after(myCalender.getTime());

                DatePickerDialog dialog = new DatePickerDialog(Book1Activity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar validDate = Calendar.getInstance();
                validDate.set(year, month, dayOfMonth);
                month = month+1;
                String DateSet = dayOfMonth +"/"+month+"/"+year;

                Calendar currentDate = Calendar.getInstance();
                if (validDate.after(currentDate)) {

                    DateTV.setText(DateSet);

                }else{
                    Toast.makeText(Book1Activity.this, "Please Select A Valid date", Toast.LENGTH_SHORT).show();
                }

            }
        };







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
                final String Treatment = (String) SPHospitalTreatments.getSelectedItem();
                String Name ="";
                String Phone ="";
                if (PatName.getVisibility() == View.VISIBLE) {

                    Name = PatName.getText().toString().trim();
                    Phone = PatPhone.getText().toString().trim();

                    if(Name.isEmpty()){
                        PatName.setError("Name is Required");
                    }
                    if(Phone.isEmpty()){
                        PatPhone.setError("Phone is Required");
                    }

                    final String finalName = Name;
                    final String finalPhone = Phone;
                    mStore.collection("Hospitals").whereEqualTo("name",HospitalName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final String ID1 = document.getId();
                                mStore.collection("Hospitals").document(ID1).collection("Treatments").whereEqualTo("Name",Treatment).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            String ID;
                                            ID = document.getId();
                                            UserID = mAuth.getCurrentUser().getUid();
                                            String newID = UUID.randomUUID().toString();
                                            DocumentReference db = mStore.collection("Hospitals").document(ID1).collection("Treatments").document(ID).collection("Slots").document(newID);
                                            Map<String,Object> user = new HashMap<>();
                                            user.put("Name", finalName);
                                            user.put("Phone", finalPhone);
                                            user.put("UserID", UserID);
                                            db.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(Book1Activity.this, "Appointment Forwarded for Booking", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                        }
                                });

                            }
                        }
                    });


                }
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



