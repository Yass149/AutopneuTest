package com.example.autopneutest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {
    private EditText first_name;
    private EditText last_name;
    private EditText password;
    private EditText username;
    private EditText adresse_mail;
    private EditText adresse;
    private EditText phone_number;
    private Button register;
    private Button d_ja_inscrit;
    private Button se_connecter;
    private FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        adresse_mail = findViewById(R.id.adresse_email);
        password = findViewById(R.id.password);
        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        username = findViewById(R.id.username);
        adresse = findViewById(R.id.adresse);
        phone_number = findViewById(R.id.phone_number);
        register = findViewById(R.id.s_inscrire);
        d_ja_inscrit = findViewById(R.id.d_ja_inscrit);
        se_connecter = findViewById(R.id.se_connecter);

        auth = FirebaseAuth.getInstance();
        //buttons mechanism
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_adresse_mail = adresse_mail.getText().toString();
                String txt_password = password.getText().toString();
                String txt_first_name = first_name.getText().toString();
                String txt_last_name = last_name.getText().toString();
                String txt_username = username.getText().toString();
                String txt_adresse = adresse.getText().toString();
                String txt_phone_number = phone_number.getText().toString();

                //if empty mechanism
                if (TextUtils.isEmpty(txt_adresse_mail) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_first_name)
                        || TextUtils.isEmpty(txt_last_name) || TextUtils.isEmpty(txt_phone_number) || TextUtils.isEmpty(txt_username)
                        || TextUtils.isEmpty(txt_adresse)){
                    Toast.makeText(RegistrationActivity.this, "certains champs sont vides!", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6){
                    Toast.makeText(RegistrationActivity.this, "Le mot de passe est trop court!", Toast.LENGTH_SHORT).show();
                } else if (txt_phone_number.length() < 10){
                    Toast.makeText(RegistrationActivity.this, "Le numéro de téléphone doit être composé de 10 chiffres !", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(txt_adresse_mail, txt_password, txt_first_name, txt_last_name, txt_adresse,txt_phone_number, txt_username);
                }
            }
        });
    }

    private void registerUser(String adresse_mail, String password, String first_name,
                              String last_name, String adresse, String phone_number, String username) {
        auth.createUserWithEmailAndPassword(adresse_mail , password).addOnCompleteListener(RegistrationActivity.this , new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegistrationActivity.this, "\n" +
                            "Vous êtes inscrit avec succès!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(RegistrationActivity.this, "\n" +
                            "échec de l'enregistrement!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void login(View view) {
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
    }

    public void mainActivity(View view) {
        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
    }
}