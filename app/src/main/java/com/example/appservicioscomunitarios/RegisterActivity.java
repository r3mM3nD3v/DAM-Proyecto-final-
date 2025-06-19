package com.example.appservicioscomunitarios;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appservicioscomunitarios.data.AppDatabase;
import com.example.appservicioscomunitarios.data.Usuario;
import com.example.appservicioscomunitarios.data.UsuarioDao;

public class RegisterActivity extends AppCompatActivity {

    EditText etNewUser, etNewPass;
    Button btnSave;
    UsuarioDao usuarioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNewUser = findViewById(R.id.etNewUser);
        etNewPass = findViewById(R.id.etNewPass);
        btnSave = findViewById(R.id.btnSave);

        usuarioDao = AppDatabase.getInstance(this).usuarioDao();

        btnSave.setOnClickListener(v -> {
            String nuevoUsuario = etNewUser.getText().toString().trim();
            String nuevaClave = etNewPass.getText().toString().trim();

            if (nuevoUsuario.isEmpty() || nuevaClave.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verificar si el usuario ya existe
            Usuario usuarioExistente = usuarioDao.findByUsername(nuevoUsuario);
            if (usuarioExistente != null) {
                Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show();
                return;
            }

            Usuario nuevo = new Usuario();
            nuevo.setUsuario(nuevoUsuario);
            nuevo.setClave(nuevaClave);
            usuarioDao.insert(nuevo);

            Toast.makeText(this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
            finish(); // Volver a LoginActivity
        });
    }
}
