package com.example.miniprojetand;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivityAdmin extends AppCompatActivity {
    private RecyclerView hotelRecyclerView;
    private EditText searchEditText;
    private HotelAdapterAdmin hotelAdapterAdmin;
    private List<Hotel> hotelList;
    private DatabaseReference databaseReference;
    FirebaseDatabase database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        searchEditText = findViewById(R.id.searchEditText);
        hotelRecyclerView = findViewById(R.id.hotelRecyclerView);

        hotelList = new ArrayList<>();
        hotelAdapterAdmin = new HotelAdapterAdmin(hotelList);

        hotelRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        hotelRecyclerView.setAdapter(hotelAdapterAdmin);

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
                hotelAdapterAdmin.notifyDataSetChanged();
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
                hotelAdapterAdmin.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        Button fabAddHotel = findViewById(R.id.fabAddHotel);
        fabAddHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancer AddHotelActivity lorsque l'on clique sur "+"
                Intent intent = new Intent(HomeActivityAdmin.this, AddHotelActivity.class);
                startActivity(intent);
            }
        });

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Get the hotel that was swiped
                int position = viewHolder.getAdapterPosition();
                Hotel hotel = hotelList.get(position);

                // Remove the hotel from Firebase
                databaseReference.child(hotel.getId()).removeValue()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Remove the hotel from the list
                                hotelList.remove(position);
                                hotelAdapterAdmin.notifyItemRemoved(position);
                                Log.d("HomeActivity", "Hotel deleted successfully");
                            } else {
                                Log.e("HomeActivity", "Failed to delete hotel");
                            }
                        });
            }
        };

        // Attach the ItemTouchHelper to the RecyclerView
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(hotelRecyclerView);
    }
}