package com.utm.utmshop;

import android.app.AlertDialog;
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

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BasketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BasketFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BasketFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BasketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BasketFragment newInstance(String param1, String param2) {
        BasketFragment fragment = new BasketFragment();
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
        return inflater.inflate(R.layout.fragment_basket, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);

        ImageButton btnProfile, btnReviews, btnShop, btnBasket, btnLogout;

        User user = new User();
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("app", 0);
        user.setId(sharedPreferences.getString("id", ""));
        String id = user.getId().toString();

        ListView listView = view.findViewById(R.id.list_basket);
        Query query = FirebaseDatabase.getInstance().getReference("users").child(id).child("basket");
        FirebaseListOptions<Basket> options = new FirebaseListOptions.Builder<Basket>()
                .setQuery(query, Basket.class)
                .setLayout(R.layout.item_basket)
                .setLifecycleOwner(this)
                .build();
        FirebaseListAdapter adapter = new FirebaseListAdapter<Basket>(options) {
            @Override
            protected void populateView(View v, Basket model, int position) {
                TextView name = (TextView) v.findViewById(R.id.name_text);
                name.setText(model.getName());
                TextView price = (TextView) v.findViewById(R.id.price_text);
                price.setText(model.getPrice() + " UAN");
                ImageView imageGame = (ImageView) v.findViewById(R.id.image_game);
                Picasso.get().load(model.getImage()).into(imageGame);
                ImageButton btnCancel = (ImageButton) v.findViewById(R.id.btn_cancel);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
                        ref.child(user.getId()).child("basket").child(model.getId()).setValue(null);
                        Snackbar.make(view, name.getText().toString() + " видалено з корзини", BaseTransientBottomBar.LENGTH_LONG).show();
                    }
                });
            }
        };
        adapter.startListening();
        listView.setAdapter(adapter);

        btnProfile = view.findViewById(R.id.to_profile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.basket_to_profile);
            }
        });

        btnReviews = view.findViewById(R.id.to_reviews);
        btnReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.basket_to_reviews);
            }
        });

        btnBasket = view.findViewById(R.id.to_shop);
        btnBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.basket_to_shop);
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