package com.utm.utmshop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");

        ImageButton btnProfile, btnReviews, btnShop, btnBasket, btnLogout;

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("app", Context.MODE_PRIVATE);

        User user = new User();
        user.setEmail(sharedPreferences.getString("email", ""));
        user.setLogin(sharedPreferences.getString("login", ""));
        user.setPassword(sharedPreferences.getString("password", ""));
        user.setId(sharedPreferences.getString("id", ""));

        EditText emailField = view.findViewById(R.id.email_field1);
        emailField.setText(user.getEmail());

        EditText loginField = view.findViewById(R.id.login_field1);
        loginField.setText(user.getLogin());

        EditText passField = view.findViewById(R.id.pass_field1);
        passField.setText(user.getPassword());

        EditText againField = view.findViewById(R.id.again_field1);
        againField.setText(user.getPassword());

        FirebaseUser account = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth newAccount = FirebaseAuth.getInstance();

        Button btnSave = view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString();
                String log = loginField.getText().toString();
                String pass = passField.getText().toString();
                AuthCredential credential = EmailAuthProvider
                        .getCredential(user.getEmail(), user.getPassword());
            }
        });

        btnProfile = view.findViewById(R.id.to_shop);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.profile_to_shop);
            }
        });

        btnReviews = view.findViewById(R.id.to_reviews);
        btnReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.profile_to_reviews);
            }
        });

        btnBasket = view.findViewById(R.id.to_basket);
        btnBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.profile_to_basket);
            }
        });

        ProgramActivity root = (ProgramActivity) getActivity();

        btnLogout = view.findViewById(R.id.to_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(root);
                dialog.setTitle("Вихід з аккаунту");
                dialog.setMessage("Підтверження виходу з аккаунту");
                dialog.setIcon(R.drawable.ic_logout);
                dialog.setPositiveButton("Так", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent ToLogout = new Intent(root, MainActivity.class);
                        startActivity(ToLogout);
                        getActivity().onBackPressed();
                        getActivity().onBackPressed();
                        root.finish();
                        sharedPreferences.edit().putString("isLog", "no").apply();
                    }
                });

                dialog.setNegativeButton("Ні", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialog.show();
            }
        });

    }
}