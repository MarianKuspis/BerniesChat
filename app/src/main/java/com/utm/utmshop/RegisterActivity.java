package com.utm.utmshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference ref, userRef;
    FirebaseAuth uAuth;

    Button btnToBack, btnRegister;
    EditText emailField, logField, passField, passField2;

    User user;

    String email, login, password, password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnToBack = (Button) findViewById(R.id.btn_to_back);
        btnRegister = (Button) findViewById(R.id.btn_register);
        emailField = (EditText) findViewById(R.id.email_reg_field);
        logField = (EditText) findViewById(R.id.login_field);
        passField = (EditText) findViewById(R.id.pass_field);
        passField2 = (EditText) findViewById(R.id.again_field);



        btnToBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailField.getText().toString();
                login = logField.getText().toString();
                password = passField.getText().toString();
                password2 = passField2.getText().toString();

                if (email.isEmpty() || login.isEmpty() || password.isEmpty() || password2.isEmpty()) {
                    Snackbar.make(view, "Помилка! Введіть усі дані.", BaseTransientBottomBar.LENGTH_LONG).show();
                } else if (email.length() < 6 || login.length() < 6 || password.length() < 6 || password2.length() < 6) {
                    Snackbar.make(view, "Помилка! В полях може бути не менше 6 символів.", BaseTransientBottomBar.LENGTH_LONG).show();
                } else if (!password.equals(password2)) {
                    Snackbar.make(view, "Помилка! Паролі не співпадають.", BaseTransientBottomBar.LENGTH_LONG).show();
                } else {
                    user = new User();
                    user.setEmail(email);
                    user.setLogin(login);
                    user.setPassword(password);

                    database = FirebaseDatabase.getInstance();
                    ref = database.getReference("users");

                    uAuth = FirebaseAuth.getInstance();

                    uAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>(){
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    user.setId(id);
                                    ref.child(id).setValue(user);
                                    Snackbar.make(view, "Найс", BaseTransientBottomBar.LENGTH_LONG).show();

                                    SharedPreferences sharedPreferences = getSharedPreferences("app", 0);
                                    sharedPreferences.edit().putString("emailPref", user.getEmail()).apply();
                                    sharedPreferences.edit().putString("email", user.getEmail()).apply();
                                    sharedPreferences.edit().putString("login", user.getLogin()).apply();
                                    sharedPreferences.edit().putString("password", user.getPassword()).apply();
                                    sharedPreferences.edit().putString("id", id).apply();

                                    Intent intent;

                                    intent = new Intent(RegisterActivity.this, ProgramActivity.class);

                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(view, "Помилка реєстрації! " + e.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });
    }
}