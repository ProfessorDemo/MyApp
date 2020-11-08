package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {


    public static final String TAG = "TAG";
    EditText Name, Email, Password, Phone;
    TextView TVLogin;
    Button Register;
    ProgressBar PgBar;
    String UserID;
    FirebaseAuth mAuth;
    FirebaseFirestore mStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        Name = findViewById(R.id.PersonName);
        Email = findViewById(R.id.EmailAddress2);
        Password = findViewById(R.id.Password2);
        Phone = findViewById(R.id.Phone);
        TVLogin = findViewById(R.id.TvLogin);
        Register = findViewById(R.id.Register);
        PgBar = findViewById(R.id.progressBar);


        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
            finish();
        }


        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailId = Email.getText().toString().trim();
                String pwd = Password.getText().toString().trim();
                final String name = Name.getText().toString();
                final String phone = Phone.getText().toString();


                if(TextUtils.isEmpty(emailId)){
                    Email.setError("Email is Required");
                    return;
                }

                if(TextUtils.isEmpty(pwd)){
                    Password.setError("Password is Required");
                    return;
                }

                if(TextUtils.isEmpty(name)){
                    Name.setError("Name is Required");
                    return;
                }

                if(TextUtils.isEmpty(phone)){
                    Phone.setError("Phone is Required");
                    return;
                }

                if(pwd.length() < 6){
                    Password.setError("Password must be at least 6 characters long");
                    return;
                }

                if(phone.length() != 10){
                    Phone.setError("Enter a Valid Phone Number");
                    return;
                }


                PgBar.setVisibility(View.VISIBLE);


                mAuth.createUserWithEmailAndPassword(emailId,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this,"User Created!",Toast.LENGTH_SHORT).show();
                            UserID = mAuth.getCurrentUser().getUid();
                            DocumentReference db = mStore.collection("Users").document(UserID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("Name ",name);
                            user.put("Email ",emailId);
                            user.put("Phone ",phone);
                            user.put("URI ","https://firebasestorage.googleapis.com/v0/b/dummydata-1601d.appspot.com/o/Images%2FProfileImages%2Fa444e3c23abb2cec25adface54e61317.png?alt=media&token=d32da7a9-10f6-4f35-91a7-acaf895fc4c2");
                            db.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG,"On Success: User Profile is created for "+ UserID);
                                }
                            });
                            startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
                        }else{
                            Toast.makeText(RegisterActivity.this,"Error !"+ task.getException(),Toast.LENGTH_SHORT).show();
                            PgBar.setVisibility(View.GONE);
                        }

                    }
                });


                TVLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                    }
                });
            }
        });
    }
}