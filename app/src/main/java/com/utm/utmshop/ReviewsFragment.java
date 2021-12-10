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

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReviewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReviewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReviewsFragment newInstance(String param1, String param2) {
        ReviewsFragment fragment = new ReviewsFragment();
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
        return inflater.inflate(R.layout.fragment_reviews, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseDatabase database;
        DatabaseReference ref;

        NavController navController = Navigation.findNavController(view);

        ImageButton btnProfile, btnSend, btnShop, btnBasket, btnLogout;

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("users");

        ProgramActivity root = (ProgramActivity) getActivity();

        ListView listView = view.findViewById(R.id.output_form);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("app", 0);

        Query query = FirebaseDatabase.getInstance().getReference("messages");
        FirebaseListOptions<Message> options = new FirebaseListOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .setLayout(R.layout.item_output)
                .setLifecycleOwner(this)
                .build();
        FirebaseListAdapter adapter = new FirebaseListAdapter<Message>(options) {
            @Override
            protected void populateView(View v, Message model, int position) {
                TextView text = v.findViewById(R.id.user_message);
                TextView user = v.findViewById(R.id.user_name);
                TextView time = v.findViewById(R.id.message_time);
                text.setText(model.getText());
                user.setText(model.getUsername());
            }
        };
        adapter.startListening();
        listView.setAdapter(adapter);

        btnSend = view.findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText inputField = view.findViewById(R.id.input_text_field);
                SharedPreferences pref = getActivity().getSharedPreferences("app", Context.MODE_PRIVATE);
                String username = pref.getString("login", "");
                String message = inputField.getText().toString();
                int num = 0;
                FirebaseDatabase.getInstance().getReference("messages").push().setValue(
                        new Message(username, message, 0));
            }
        });

        btnProfile = view.findViewById(R.id.to_profile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.reviews_to_profile);
            }
        });

        btnShop = view.findViewById(R.id.to_shop);
        btnShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.reviews_to_shop);
            }
        });

        btnBasket = view.findViewById(R.id.to_basket);
        btnBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.reviews_to_basket);
            }
        });

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