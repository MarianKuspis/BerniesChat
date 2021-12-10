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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.utilities.Base64;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");

        ImageButton btnProfile, btnReviews, btnShop, btnBasket, btnLogout;
        ImageView img1 = view.findViewById(R.id.game_1);
        ImageView img2 = view.findViewById(R.id.game_2);
        ImageView img3 = view.findViewById(R.id.game_3);
        ImageView img4 = view.findViewById(R.id.game_4);
        ImageView img5 = view.findViewById(R.id.game_5);
        ImageView img6 = view.findViewById(R.id.game_6);

        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/utm-shop-6a568.appspot.com/o/g1.jpg?alt=media&token=2663c49a-9cdc-4a91-810c-f707715f506d").into(img1);
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/utm-shop-6a568.appspot.com/o/g2.jpg?alt=media&token=ae8ff339-3d95-4b52-8592-46fd85ba2a8d").into(img2);
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/utm-shop-6a568.appspot.com/o/g3.jpg?alt=media&token=77957e4c-d7c3-462f-90e4-65e73260ed5e").into(img3);
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/utm-shop-6a568.appspot.com/o/g4.jpg?alt=media&token=8b7edede-7b51-40cb-8971-44fb6b27025e").into(img4);
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/utm-shop-6a568.appspot.com/o/g5.jpg?alt=media&token=48b9761a-d71f-417b-9903-2d95e32edc40").into(img5);
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/utm-shop-6a568.appspot.com/o/g6.jpg?alt=media&token=76c5baf0-095d-437f-834f-1c8192b743f7").into(img6);

        String isLogged;

        User user = new User();

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("app", 0);
        user.setEmail(sharedPreferences.getString("email", ""));
        user.setLogin(sharedPreferences.getString("login", ""));
        user.setPassword(sharedPreferences.getString("password", ""));
        user.setId(sharedPreferences.getString("id", ""));

        DatabaseReference shopRef = database.getInstance().getReference("users").child(user.getId()).child("basket");

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = shopRef.push().getKey();
                shopRef.child(key).setValue(
                        new Basket("World of Warcraft: Shadowlands",
                                "200",
                                "https://firebasestorage.googleapis.com/v0/b/utm-shop-6a568.appspot.com/o/g1.jpg?alt=media&token=2663c49a-9cdc-4a91-810c-f707715f506d",
                                key));
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = shopRef.push().getKey();
                shopRef.child(key).setValue(
                        new Basket("Diablo II", "100"
                        , "https://firebasestorage.googleapis.com/v0/b/utm-shop-6a568.appspot.com/o/g2.jpg?alt=media&token=ae8ff339-3d95-4b52-8592-46fd85ba2a8d",
                                key));
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = shopRef.push().getKey();
                shopRef.child(key).setValue(
                        new Basket("Path of Exile", "0"
                        , "https://firebasestorage.googleapis.com/v0/b/utm-shop-6a568.appspot.com/o/g3.jpg?alt=media&token=77957e4c-d7c3-462f-90e4-65e73260ed5e",
                                key));
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = shopRef.push().getKey();
                shopRef.child(key).setValue(
                        new Basket("World of Warcraft: The Burning Crusade", "200",
                        "https://firebasestorage.googleapis.com/v0/b/utm-shop-6a568.appspot.com/o/g4.jpg?alt=media&token=8b7edede-7b51-40cb-8971-44fb6b27025e",
                                key));
            }
        });
        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = shopRef.push().getKey();
                shopRef.child(key).setValue(
                        new Basket("New World", "500",
                                "https://firebasestorage.googleapis.com/v0/b/utm-shop-6a568.appspot.com/o/g5.jpg?alt=media&token=48b9761a-d71f-417b-9903-2d95e32edc40",
                                key));
            }
        });
        img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = shopRef.push().getKey();
                shopRef.child(key).setValue(
                        new Basket("Guild Wars", "0",
                                "https://firebasestorage.googleapis.com/v0/b/utm-shop-6a568.appspot.com/o/g6.jpg?alt=media&token=76c5baf0-095d-437f-834f-1c8192b743f7",
                                key));
            }
        });

        isLogged = sharedPreferences.getString("isLog", "no");

        if (isLogged.equals("no")) {
            Snackbar.make(view, "Вітаю " + user.getLogin(), BaseTransientBottomBar.LENGTH_LONG).show();
            sharedPreferences.edit().putString("isLog", "yes").apply();
            isLogged = "yes";
        }

        btnProfile= view.findViewById(R.id.to_profile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.shop_to_profile);
            }
        });

        btnReviews = view.findViewById(R.id.to_reviews);
        btnReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.shop_to_reviews);
            }
        });

        btnBasket = view.findViewById(R.id.to_basket);
        btnBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.shop_to_basket);
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
                        sharedPreferences.edit().putString("isLog", "no").apply();
                        Intent ToLogout = new Intent(root, MainActivity.class);
                        startActivity(ToLogout);
                        root.finish();
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