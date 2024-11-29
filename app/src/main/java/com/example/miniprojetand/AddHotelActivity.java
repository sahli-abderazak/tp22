package com.example.miniprojetand;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddHotelActivity extends AppCompatActivity {

    private EditText editName, editLocation, editPrice, editImageUrl, editDescription;
    private Button btnAddHotel;
    private DatabaseReference databaseReference;

    private EditText editLatitude, editLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hotel);

        // Initialisation des vues
        editName = findViewById(R.id.editName);
        editLocation = findViewById(R.id.editLocation);
        editPrice = findViewById(R.id.editPrice);
        editImageUrl = findViewById(R.id.editImageUrl);
        editDescription = findViewById(R.id.editDescription);
        btnAddHotel = findViewById(R.id.btnAddHotel);
        editLatitude = findViewById(R.id.editLatitude);
        editLongitude = findViewById(R.id.editLongitude);
        ImageView backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(v -> {
            finish();
        });

        // Référence Firebase
        databaseReference = FirebaseDatabase.getInstance("https://projetandroid-1f00a-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("hotels");

        // Gestion du clic sur le bouton "Ajouter"
        btnAddHotel.setOnClickListener(v -> addHotelToFirebase());
    }

    private void addHotelToFirebase() {
        // Récupération des valeurs du formulaire
        String name = editName.getText().toString().trim();
        String location = editLocation.getText().toString().trim();
        String price = editPrice.getText().toString().trim();
        String imageUrl = editImageUrl.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String latitudeStr = editLatitude.getText().toString().trim();
        String longitudeStr = editLongitude.getText().toString().trim();

        // Validation des champs
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(location) || TextUtils.isEmpty(price) ||
                TextUtils.isEmpty(imageUrl) || TextUtils.isEmpty(description) ||
                TextUtils.isEmpty(latitudeStr) || TextUtils.isEmpty(longitudeStr)) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Conversion de la latitude et de la longitude en double
        double latitude, longitude;
        try {
            latitude = Double.parseDouble(latitudeStr);
            longitude = Double.parseDouble(longitudeStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Latitude et longitude doivent être des nombres", Toast.LENGTH_SHORT).show();
            return;
        }

        // Création d'un ID unique pour l'hôtel
        String hotelId = databaseReference.push().getKey();

        // Création de l'objet hôtel
        Hotel hotel = new Hotel(hotelId, name, location, price, imageUrl, description, latitude, longitude);

        // Ajout à Firebase
        databaseReference.child(hotelId).setValue(hotel)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddHotelActivity.this, "Hôtel ajouté avec succès", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(AddHotelActivity.this, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show());
    }
}