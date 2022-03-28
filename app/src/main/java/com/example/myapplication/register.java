package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class register extends AppCompatActivity {
    private EditText editNAme,editEmail,editPassword,editPasswordconf;
    private Button btnRegister,btnLogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editNAme =  findViewById(R.id.name);
        editEmail = findViewById(R.id.email);
        editPassword = findViewById(R.id.password);
        editPasswordconf = findViewById(R.id.password_conf);
        btnRegister = findViewById(R.id.btn_register);
        btnLogin = findViewById(R.id.btn_login);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(register.this);
        progressDialog.setTitle("loading");

        progressDialog.setMessage("silakan tunggu");
        progressDialog.setCancelable(false);

        btnLogin.setOnClickListener(V -> {
            finish();
        });
        btnRegister.setOnClickListener(view -> {
            if(editNAme.getText().length()>0 && editEmail.getText().length()>0 && editPassword.getText().length()>0 && editPasswordconf.getText().length()>0){
                if(editPassword.getText().toString().equals(editPasswordconf.getText().toString())){
                    register(editNAme.getText().toString(),editEmail.getText().toString(),editPassword.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(),"silakan masuk password yang sama!",Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getApplicationContext(),"silakan isi semua data!",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void register(String name,String email,String password){
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() && task.getResult()!=null){
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    if (firebaseUser!=null) {
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name).build();

                        firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                reload();
                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(),"register gagal gagal!",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void  reload(){
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

}