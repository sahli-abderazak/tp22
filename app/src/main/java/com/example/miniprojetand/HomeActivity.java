package com.example.miniprojetand;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView hotelRecyclerView;
    private EditText searchEditText;
    private HotelAdapter hotelAdapter;
    private List<Hotel> hotelList;
    private DatabaseReference databaseReference;
    FirebaseDatabase database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        searchEditText = findViewById(R.id.searchEditText);
        hotelRecyclerView = findViewById(R.id.hotelRecyclerView);

        hotelList = new ArrayList<>();
        hotelAdapter = new HotelAdapter(hotelList);

        hotelRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        hotelRecyclerView.setAdapter(hotelAdapter);

        ImageView logoutIcon = findViewById(R.id.logoutIcon);

        logoutIcon.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, Login.class);
            startActivity(intent);
        });

        database = FirebaseDatabase.getInstance("https://projetandroid-1f00a-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = database.getReference("hotels");

        Log.d("HomeActivity", "Number of hotels retrieved: " + hotelList.size());

        // Listen for data changes
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hotelList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Hotel hotel = snapshot.getValue(Hotel.class);
                    if (hotel != null) {
                        hotelList.add(hotel);
                    } else {
                        Log.e("Firebase", "Hotel data is null for snapshot: " + snapshot.toString());
                    }
                }
                hotelAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hotelAdapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}