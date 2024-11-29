package com.example.miniprojetand;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HotelModifierActivity extends AppCompatActivity {

    private EditText editName, editLocation, editPrice, editImageUrl, editDescription, editLatitude, editLongitude;
    private Button updateButton;
    private DatabaseReference databaseReference;

    private String hotelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_modifier);

        // Récupération des références des vues
        editName = findViewById(R.id.editName);
        editLocation = findViewById(R.id.editLocation);
        editPrice = findViewById(R.id.editPrice);
        editImageUrl = findViewById(R.id.editImageUrl);
        editDescription = findViewById(R.id.editDescription);
        editLatitude = findViewById(R.id.editLatitude);
        editLongitude = findViewById(R.id.editLongitude);
        updateButton = findViewById(R.id.updateButton);


        ImageView backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(v -> {
            finish();
        });

        // Récupération des données de l'hôtel depuis l'Intent
        hotelId = getIntent().getStringExtra("hotelId");
        String hotelName = getIntent().getStringExtra("hotelName");
        String hotelLocation = getIntent().getStringExtra("hotelLocation");
        String hotelPrice = getIntent().getStringExtra("hotelPrice");
        String hotelImageUrl = getIntent().getStringExtra("hotelImage");
        String hotelDescription = getIntent().getStringExtra("hotelDescription");
        double hotelLatitude = getIntent().getDoubleExtra("latitude", 0.0);
        double hotelLongitude = getIntent().getDoubleExtra("longitude", 0.0);

        // Préremplir les champs avec les données actuelles
        editName.setText(hotelName);
        editLocation.setText(hotelLocation);
        editPrice.setText(hotelPrice);
        editImageUrl.setText(hotelImageUrl);
        editDescription.setText(hotelDescription);
        editLatitude.setText(String.valueOf(hotelLatitude));
        editLongitude.setText(String.valueOf(hotelLongitude));

        // Référence à la base de données Firebase
        databaseReference = FirebaseDatabase.getInstance("https://projetandroid-1f00a-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("hotels").child(hotelId);

        // Gestion du clic sur le bouton "Mettre à jour"
        updateButton.setOnClickListener(v -> updateHotel());
    }

    private void updateHotel() {
        // Récupération des nouvelles valeurs des champs
        String newName = editName.getText().toString().trim();
        String newLocation = editLocation.getText().toString().trim();
        String newPrice = editPrice.getText().toString().trim();
        String newImageUrl = editImageUrl.getText().toString().trim();
        String newDescription = editDescription.getText().toString().trim();
        double newLatitude = Double.parseDouble(editLatitude.getText().toString().trim());
        double newLongitude = Double.parseDouble(editLongitude.getText().toString().trim());

        // Validation des champs
        if (TextUtils.isEmpty(newName) || TextUtils.isEmpty(newLocation) ||
                TextUtils.isEmpty(newPrice) || TextUtils.isEmpty(newImageUrl) ||
                TextUtils.isEmpty(newDescription)) {
            Toast.makeText(HotelModifierActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mise à jour de l'hôtel dans Firebase
        Hotel updatedHotel = new Hotel(hotelId, newName, newLocation, newPrice, newImageUrl, newDescription, newLatitude, newLongitude);
        databaseReference.setValue(updatedHotel)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(HotelModifierActivity.this, "Hôtel mis à jour avec succès", Toast.LENGTH_SHORT).show();
                    finish(); // Fermer l'activité après mise à jour
                })
                .addOnFailureListener(e -> Toast.makeText(HotelModifierActivity.this, "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show());
    }
}