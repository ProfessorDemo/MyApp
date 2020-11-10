package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Book2Activity extends AppCompatActivity {

    TextView TVHospitalName2, TVHospitalTreatment2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book2);

        TVHospitalName2 = findViewById(R.id.TVHospitalName2);
        TVHospitalTreatment2 = findViewById(R.id.TVHospitalTreatment2);


        Bundle bundle = getIntent().getExtras();
        String HospitalName = bundle.getString("HospitalName");
        String HospitalTreatment = bundle.getString("HospitalTreatment");

        TVHospitalName2.setText(HospitalName);
        TVHospitalTreatment2.setText(HospitalTreatment);



    }
}