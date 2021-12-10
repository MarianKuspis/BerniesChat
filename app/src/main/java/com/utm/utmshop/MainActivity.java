package com.utm.utmshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.google.firebase.database.annotations.Nullable;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase data;
    private DatabaseReference ref;
    private FirebaseAuth auth;
    private Intent intent;

    private User user;

    private boolean visionPass = false;
    private String isLogged;

    private Button btnToReg, btnLog, btnVision;
    private EditText logField, passField;
    private TextView forgotPass;

    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences loginPref = getSharedPreferences("app", Context.MODE_PRIVATE);
        isLogged = loginPref.getString("isLog", "no");

        if (isLogged.equals("yes")) {
            intent = new Intent(MainActivity.this, ProgramActivity.class);
            startActivity(intent);
        }

        btnToReg = (Button) findViewById(R.id.btn_to_reg);
        btnToReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLog = (Button) findViewById(R.id.btn_login);
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logField = (EditText) findViewById(R.id.log_field);
                passField = (EditText) findViewById(R.id.pass_field);

                email = logField.getText().toString();
                password = passField.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Snackbar.make(view, "Помилка! Заповніть всі поля.", BaseTransientBottomBar.LENGTH_LONG).show();
                } else if (email.length() < 6 || password.length() < 6) {
                    Snackbar.make(view, "Введіть коректні дані.", BaseTransientBottomBar.LENGTH_LONG).show();
                } else {
                    data = FirebaseDatabase.getInstance();
                    ref = data.getReference("users");
                    auth = FirebaseAuth.getInstance();

                    auth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Query myQuery = ref.orderByChild("email").equalTo(email);
                                    myQuery.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                            User user;
                                            Intent intent;
                                            user = snapshot.getValue(User.class);

                                            SharedPreferences sharedPreferences = getSharedPreferences("app", 0);
                                            sharedPreferences.edit().putString("email", user.getEmail()).apply();
                                            sharedPreferences.edit().putString("login", user.getLogin()).apply();
                                            sharedPreferences.edit().putString("password", user.getPassword()).apply();

                                            intent = new Intent(MainActivity.this, ProgramActivity.class);
                                            startActivity(intent);
                                        }

                                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
                                        public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
                                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
                                        public void onCancelled(@NonNull DatabaseError error) { }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(view, "Помилка!" + e.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        forgotPass = (TextView) findViewById(R.id.forgot_pass);
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Функція це не працює.", BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });
    }
}