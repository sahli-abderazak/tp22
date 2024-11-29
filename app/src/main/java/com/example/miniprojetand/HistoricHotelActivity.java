package com.example.miniprojetand;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoricHotelActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReservationAdapter adapter;
    private List<Reservation> reservationList;
    private DatabaseReference reservationRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic_hotel);

        Log.d("HistoricHotelActivity", "Activity Started");

        // Initialiser la RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialiser la liste des réservations
        reservationList = new ArrayList<>();

        // Initialiser l'adaptateur
        adapter = new ReservationAdapter(reservationList);
        recyclerView.setAdapter(adapter);

        // Référence Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reservationRef = database.getReference("reservations");

        Log.d("Firebase", "Firebase reference initialized for 'reservations'");

        // Récupérer les réservations depuis Firebase
        reservationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Firebase", "Data snapshot received: " + dataSnapshot.getChildrenCount() + " reservations found");

                reservationList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Reservation reservation = snapshot.getValue(Reservation.class);
                    if (reservation != null) {
                        reservationList.add(reservation);
                        Log.d("Firebase", "Reservation added: " + reservation.toString());
                    } else {
                        Log.w("Firebase", "Reservation data is null for snapshot: " + snapshot.toString());
                    }
                }
                adapter.notifyDataSetChanged();
                Log.d("HistoricHotelActivity", "Reservation list size after update: " + reservationList.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Error loading reservations: " + databaseError.getMessage());
                Toast.makeText(HistoricHotelActivity.this, "Failed to load reservations.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}