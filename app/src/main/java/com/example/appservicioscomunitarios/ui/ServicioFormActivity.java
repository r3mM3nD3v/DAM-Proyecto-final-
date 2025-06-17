package com.example.appservicioscomunitarios.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appservicioscomunitarios.R;
import com.example.appservicioscomunitarios.data.AppDatabase;
import com.example.appservicioscomunitarios.data.Servicio;
import com.example.appservicioscomunitarios.data.ServicioDao;

public class ServicioFormActivity extends AppCompatActivity {

    private EditText edtTitulo, edtDescripcion;
    private ImageView imageView;
    private Button btnGuardar, btnRegresar;

    private ServicioDao servicioDao;
    private Servicio servicioActual;

    private Uri imagenSeleccionadaUri;

    // Selector de imagen
    private final ActivityResultLauncher<String> selectImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri != null) {
                            imagenSeleccionadaUri = uri;
                            imageView.setImageURI(uri);
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicio_form);

        edtTitulo = findViewById(R.id.edtTitulo);
        edtDescripcion = findViewById(R.id.edtDescripcion);
        imageView = findViewById(R.id.imageView);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnRegresar = findViewById(R.id.btnRegresar);

        servicioDao = AppDatabase.getInstance(this).servicioDao();

        int servicioId = getIntent().getIntExtra("servicioId", -1);
        if (servicioId != -1) {
            servicioActual = servicioDao.getById(servicioId);
        }

        if (servicioActual != null) {
            edtTitulo.setText(servicioActual.getTitulo());
            edtDescripcion.setText(servicioActual.getDescripcion());
            if (servicioActual.getImagenUri() != null) {
                imagenSeleccionadaUri = Uri.parse(servicioActual.getImagenUri());
                imageView.setImageURI(imagenSeleccionadaUri);
            }
        }

        imageView.setOnClickListener(v -> {
            // Abrir selector de imagen
            selectImageLauncher.launch("image/*");
        });

        btnGuardar.setOnClickListener(v -> {
            String titulo = edtTitulo.getText().toString().trim();
            String descripcion = edtDescripcion.getText().toString().trim();

            if (titulo.isEmpty()) {
                Toast.makeText(this, "Ingrese un título", Toast.LENGTH_SHORT).show();
                return;
            }

            Servicio servicio = (servicioActual == null) ? new Servicio() : servicioActual;

            servicio.setTitulo(titulo);
            servicio.setDescripcion(descripcion);
            servicio.setImagenUri(imagenSeleccionadaUri != null ? imagenSeleccionadaUri.toString() : null);

            if (servicioActual == null) {
                servicioDao.insert(servicio);
                Toast.makeText(this, "Servicio guardado", Toast.LENGTH_SHORT).show();
            } else {
                servicioDao.update(servicio);
                Toast.makeText(this, "Servicio actualizado", Toast.LENGTH_SHORT).show();
            }
            finish();
        });

        btnRegresar.setOnClickListener(v -> finish());
    }
}
