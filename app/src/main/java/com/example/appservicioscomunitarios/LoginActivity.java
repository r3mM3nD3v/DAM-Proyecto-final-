package com.example.appservicioscomunitarios;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appservicioscomunitarios.data.AppDatabase;
import com.example.appservicioscomunitarios.data.Usuario;
import com.example.appservicioscomunitarios.data.UsuarioDao;

public class LoginActivity extends AppCompatActivity {

    public static final String PREF_NAME = "sesion_usuario";
    public static final String KEY_USER = "usuario_activo";

    EditText etUser, etPass;
    Button btnLogin, btnRegister;
    ProgressBar progreso;

    UsuarioDao usuarioDao;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        progreso = findViewById(R.id.progreso);

        usuarioDao = AppDatabase.getInstance(this).usuarioDao();
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Si ya hay usuario logueado, ir directo a MainActivity
        if (sharedPreferences.contains(KEY_USER)) {
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }

        btnLogin.setOnClickListener(v -> {
            String usuarioInput = etUser.getText().toString().trim();
            String claveInput = etPass.getText().toString().trim();

            if (usuarioInput.isEmpty() || claveInput.isEmpty()) {
                Toast.makeText(this, "Ingrese usuario y contraseña", Toast.LENGTH_SHORT).show();
                return;
            }

            mostrarCargando(true);

            new Thread(() -> {
                try {
                    Usuario usuario = usuarioDao.login(usuarioInput, claveInput);
                    runOnUiThread(() -> {
                        mostrarCargando(false);
                        if (usuario != null) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(KEY_USER, usuario.getUsuario());
                            editor.apply();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        mostrarCargando(false);
                        Toast.makeText(LoginActivity.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
                    });
                }
            }).start();
        });

        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    private void mostrarCargando(boolean cargando) {
        progreso.setVisibility(cargando ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!cargando);
        btnRegister.setEnabled(!cargando);
        etUser.setEnabled(!cargando);
        etPass.setEnabled(!cargando);
    }
}
