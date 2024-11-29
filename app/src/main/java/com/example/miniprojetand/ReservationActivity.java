package com.example.miniprojetand;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReservationActivity extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, phoneEditText, emailEditText, checkInDateEditText, checkOutDateEditText;
    private Spinner adultsCountSpinner, childrenCountSpinner, roomsCountSpinner;
    private MaterialButton bookingButton;
    private DatabaseReference reservationRef;
    private DatabaseReference hotelsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        // Initialize UI elements
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        checkInDateEditText = findViewById(R.id.checkInDateEditText);
        checkOutDateEditText = findViewById(R.id.checkOutDateEditText);
        adultsCountSpinner = findViewById(R.id.adultsCountSpinner);
        childrenCountSpinner = findViewById(R.id.childrenCountSpinner);
        roomsCountSpinner = findViewById(R.id.roomsCountSpinner);
        bookingButton = findViewById(R.id.bookingButton);

        // Initialize Firebase Database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://projetandroid-1f00a-default-rtdb.europe-west1.firebasedatabase.app");
        reservationRef = database.getReference("reservations");
        hotelsRef = database.getReference("hotels");

        // Get hotel details passed from HotelDetailActivity
        String hotelName = getIntent().getStringExtra("hotelName");
        String hotelPrice = getIntent().getStringExtra("hotelPrice");

        // Initialize Spinners
        ArrayAdapter<Integer> adultsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getRange(1, 10));
        adultsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adultsCountSpinner.setAdapter(adultsAdapter);

        ArrayAdapter<Integer> childrenAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getRange(0, 10));
        childrenAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        childrenCountSpinner.setAdapter(childrenAdapter);

        ArrayAdapter<Integer> roomsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getRange(1, 5));
        roomsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomsCountSpinner.setAdapter(roomsAdapter);

        // Set DatePicker for check-in and check-out dates
        setDatePicker(checkInDateEditText);
        setDatePicker(checkOutDateEditText);

        // Set the booking button action
        bookingButton.setOnClickListener(v -> {
            String firstName = firstNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String checkInDate = checkInDateEditText.getText().toString().trim();
            String checkOutDate = checkOutDateEditText.getText().toString().trim();
            String adultsCount = adultsCountSpinner.getSelectedItem().toString();
            String childrenCount = childrenCountSpinner.getSelectedItem().toString();
            String roomsCount = roomsCountSpinner.getSelectedItem().toString();

            // Validate the form
            if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || email.isEmpty() ||
                    checkInDate.isEmpty() || checkOutDate.isEmpty()) {
                Toast.makeText(ReservationActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            } else {

                if (!isValidDateRange(checkInDate, checkOutDate)) {
                    Toast.makeText(ReservationActivity.this, "Il faut la date de Check-in se fait avant la date de Check-out date", Toast.LENGTH_SHORT).show();
                } else {

                    // Create a Reservation object
                    Reservation reservation = new Reservation(hotelName, hotelPrice, firstName, lastName, phone, email, checkInDate, checkOutDate,
                            adultsCount, childrenCount, roomsCount);
                    Log.e("Reservation", reservation.toString());

                    // Save to Firebase under the "reservations" node
                    String reservationId = reservationRef.push().getKey();
                    Log.e("ReservationID", reservationId);// Generate unique ID for reservation
                    if (reservationId != null) {
                        reservationRef.child(reservationId).setValue(reservation)
                                .addOnSuccessListener(aVoid -> {
                                    Log.e("Firebase", "Reservation saved successfully");
                                    Toast.makeText(ReservationActivity.this, "Reservation validée", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ReservationActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Firebase", "Reservation failed: " + e.getMessage());
                                    Toast.makeText(ReservationActivity.this, "Reservation Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Log.e("Firebaseee", "Reservation ID generation failed");
                    }
                }
            }
        });
    }

    private void setDatePicker(final EditText dateEditText) {
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        dateEditText.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(ReservationActivity.this, (view, selectedYear, selectedMonth, selectedDay) -> {
                // Format the date and set it on the EditText
                String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                dateEditText.setText(date);
            }, year, month, day);
            datePickerDialog.show();
        });
    }
    private boolean isValidDateRange(String checkInDate, String checkOutDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date checkIn = sdf.parse(checkInDate);
            Date checkOut = sdf.parse(checkOutDate);

            // Return true if check-in date is before check-out date
            return checkIn != null && checkOut != null && checkIn.before(checkOut);
        } catch (Exception e) {
            Log.e("DateError", "Invalid date format", e);
            return false;
        }
    }
    private Integer[] getRange(int start, int end) {
        Integer[] range = new Integer[end - start + 1];
        for (int i = 0; i <= end - start; i++) {
            range[i] = start + i;
        }
        return range;
    }
}