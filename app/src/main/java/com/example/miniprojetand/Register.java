package com.example.miniprojetand;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText, nameEditText, phoneEditText;
    private Button createAccountButton;
    private TextView loginTextView;  // Ajout de la variable loginTextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialiser FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        createAccountButton = findViewById(R.id.createAccountButton);
        loginTextView = findViewById(R.id.loginTextView);  // Initialisation du TextView

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String phone = phoneEditText.getText().toString();

                if (!email.isEmpty() && !password.isEmpty()) {
                    registerUser(email, password, name, phone);
                } else {
                    Toast.makeText(Register.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Gestionnaire de clic pour redirection vers la page de connexion
        loginTextView.setOnClickListener(v -> {
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
        });
    }

    private void registerUser(String email, String password, String name, String phone) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Ajout d'un utilisateur à la base de données
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                            String userId = user.getUid();
                            User newUser = new User(name, email, phone);

                            databaseReference.child(userId).setValue(newUser).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Toast.makeText(Register.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Register.this, Login.class);
                                    startActivity(intent);
                                    finish();  // Ferme l'écran d'inscription
                                } else {
                                    // Si l'enregistrement dans la base de données échoue
                                    Toast.makeText(Register.this, "Erreur lors de l'enregistrement", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        // Si l'inscription échoue
                        Toast.makeText(Register.this, "Échec de l'inscription : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}