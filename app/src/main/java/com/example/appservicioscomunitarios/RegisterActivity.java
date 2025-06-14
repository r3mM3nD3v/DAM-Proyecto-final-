package com.example.appservicioscomunitarios;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText etNewUser, etNewPass;
    Button btnSave;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNewUser = findViewById(R.id.etNewUser);
        etNewPass = findViewById(R.id.etNewPass);
        btnSave = findViewById(R.id.btnSave);

        sharedPreferences = getSharedPreferences(LoginActivity.PREF_NAME, MODE_PRIVATE);

        btnSave.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(LoginActivity.KEY_USER, etNewUser.getText().toString());
            editor.putString(LoginActivity.KEY_PASS, etNewPass.getText().toString());
            editor.apply();
            Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show();
            finish(); // volver a Login
        });
    }
}
