package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

public class Book2Activity extends AppCompatActivity {

    TextView TVHospitalName2, TVHospitalTreatment2;
    FirebaseFirestore mStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book2);

        mStore = FirebaseFirestore.getInstance();

        TVHospitalName2 = findViewById(R.id.TVHospitalName2);
        TVHospitalTreatment2 = findViewById(R.id.TVHospitalTreatment2);



        Bundle bundle = getIntent().getExtras();
        String HospitalName = bundle.getString("HospitalName");
        String HospitalTreatment = bundle.getString("HospitalTreatment");

        mStore.collection("Hospitals").whereEqualTo("name",HospitalName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String HospitalID = "";
                for (QueryDocumentSnapshot document : task.getResult())
                    HospitalID = document.getId();

            }
        });


        TVHospitalName2.setText(HospitalName);
        TVHospitalTreatment2.setText(HospitalTreatment);



    }
}