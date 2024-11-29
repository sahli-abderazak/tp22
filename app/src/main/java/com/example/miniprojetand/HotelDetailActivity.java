package com.example.miniprojetand;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HotelDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView hotelNameTextView, hotelLocationTextView, hotelPriceTextView, hotelDescriptionTextView;
    private ImageView hotelImageView;
    private Button reserveButton;

    private GoogleMap myMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);

        // Initialisation des éléments d'interface utilisateur
        hotelNameTextView = findViewById(R.id.hotelNameTextView);
        hotelLocationTextView = findViewById(R.id.hotelLocationTextView);
        hotelPriceTextView = findViewById(R.id.hotelPriceTextView);
        hotelImageView = findViewById(R.id.hotelImageView);
        hotelDescriptionTextView = findViewById(R.id.hotelDescriptionTextView);
        reserveButton = findViewById(R.id.reserveButton);

        ImageView backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(v -> {
            finish();
        });

        // Initialisation de la carte
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Récupération des données transmises via l'intent
        String hotelName = getIntent().getStringExtra("hotelName");
        String hotelLocation = getIntent().getStringExtra("hotelLocation");
        String hotelPrice = getIntent().getStringExtra("hotelPrice");
        String hotelImageUrl = getIntent().getStringExtra("hotelImage");
        String hotelDescription = getIntent().getStringExtra("hotelDescription");
        String hotelCoordinates = getIntent().getStringExtra("hotelCoordinates");

        // Configuration des éléments d'interface utilisateur
        hotelNameTextView.setText(hotelName);
        hotelLocationTextView.setText(hotelLocation);
        hotelPriceTextView.setText(hotelPrice + " TND");
        hotelDescriptionTextView.setText(hotelDescription);

        Glide.with(this).load(hotelImageUrl).into(hotelImageView);

        reserveButton.setOnClickListener(v -> {
            Intent intent = new Intent(HotelDetailActivity.this, ReservationActivity.class);
            intent.putExtra("hotelName", hotelName);
            intent.putExtra("hotelPrice", hotelPrice);
            startActivity(intent);
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        double latitude = getIntent().getDoubleExtra("latitude", 0);
        double longitude = getIntent().getDoubleExtra("longitude", 0);
        String hotelName = getIntent().getStringExtra("hotelName");

        // Vérifier si les coordonnées sont valides
        if (latitude != 0 && longitude != 0 && hotelName != null) {
            LatLng hotelLocation = new LatLng(latitude, longitude);
            myMap.addMarker(new MarkerOptions().position(hotelLocation).title(hotelName));
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hotelLocation, 15));
        }
    }
}