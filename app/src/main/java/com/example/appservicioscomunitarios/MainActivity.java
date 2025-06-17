package com.example.appservicioscomunitarios;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.appservicioscomunitarios.data.AppDatabase;
import com.example.appservicioscomunitarios.data.Servicio;
import com.example.appservicioscomunitarios.data.ServicioDao;
import com.example.appservicioscomunitarios.ui.ServicioAdapter;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btnAgregar;
    private ServicioAdapter adapter;
    private ServicioDao servicioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerServicios);
        btnAgregar = findViewById(R.id.btnAgregar);

        servicioDao = AppDatabase.getInstance(this).servicioDao();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cargarServicios();

        btnAgregar.setOnClickListener(v -> {
            startActivity(new Intent(this, com.example.appservicioscomunitarios.ui.ServicioFormActivity.class));
        });
    }

    private void cargarServicios() {
        List<Servicio> listaServicios = servicioDao.getAll();
        adapter = new ServicioAdapter(listaServicios, this, new ServicioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Servicio servicio) {
                // Abrir formulario para editar
                Intent intent = new Intent(MainActivity.this, com.example.appservicioscomunitarios.ui.ServicioFormActivity.class);
                intent.putExtra("servicioId", servicio.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Servicio servicio) {
                // Confirmar eliminación
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
        cargarServicios(); // recargar la lista cuando regrese de formulario
    }
}
