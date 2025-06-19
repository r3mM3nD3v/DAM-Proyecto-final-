package com.example.appservicioscomunitarios;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.CompoundButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appservicioscomunitarios.data.AppDatabase;
import com.example.appservicioscomunitarios.data.Servicio;
import com.example.appservicioscomunitarios.data.ServicioDao;
import com.example.appservicioscomunitarios.ui.ServicioAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btnAgregar, btnCerrarSesion;
    private Switch switchTema;
    private ServicioAdapter adapter;
    private ServicioDao servicioDao;
    private SharedPreferences preferencias;
    private SharedPreferences sharedPreferences; // Para sesión usuario

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Aplicar tema antes de mostrar UI
        preferencias = getSharedPreferences("tema", MODE_PRIVATE);
        boolean modoOscuro = preferencias.getBoolean("modo_oscuro", false);
        if (modoOscuro) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerServicios);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        switchTema = findViewById(R.id.switch_tema);

        sharedPreferences = getSharedPreferences(LoginActivity.PREF_NAME, MODE_PRIVATE);

        switchTema.setChecked(modoOscuro);
        switchTema.setOnCheckedChangeListener(this::onSwitchChanged);

        servicioDao = AppDatabase.getInstance(this).servicioDao();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cargarServicios();

        btnAgregar.setOnClickListener(v -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Agregar nuevo servicio")
                    .setMessage("¿Deseas agregar un nuevo servicio?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        startActivity(new Intent(MainActivity.this, com.example.appservicioscomunitarios.ui.ServicioFormActivity.class));
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        btnCerrarSesion.setOnClickListener(v -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Cerrar sesión")
                    .setMessage("¿Seguro que deseas cerrar sesión?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void onSwitchChanged(CompoundButton buttonView, boolean isChecked) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Cambiar tema")
                .setMessage("¿Deseas cambiar al modo " + (isChecked ? "oscuro" : "claro") + "?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    guardarPreferencia(isChecked);
                    if (isChecked) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
                    recreate();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    switchTema.setOnCheckedChangeListener(null);
                    switchTema.setChecked(!isChecked);
                    switchTema.setOnCheckedChangeListener(this::onSwitchChanged);
                })
                .show();
    }

    private void guardarPreferencia(boolean modoOscuro) {
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putBoolean("modo_oscuro", modoOscuro);
        editor.apply();
    }

    private void cargarServicios() {
        List<Servicio> listaServicios = servicioDao.getAll();
        adapter = new ServicioAdapter(listaServicios, this, new ServicioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Servicio servicio) {
                Intent intent = new Intent(MainActivity.this, com.example.appservicioscomunitarios.ui.ServicioFormActivity.class);
                intent.putExtra("servicioId", servicio.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Servicio servicio) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Eliminar Servicio")
                        .setMessage("¿Seguro que deseas eliminar este servicio?")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            servicioDao.delete(servicio);
                            Toast.makeText(MainActivity.this, "Servicio eliminado", Toast.LENGTH_SHORT).show();
                            cargarServicios();
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarServicios();
    }
}
