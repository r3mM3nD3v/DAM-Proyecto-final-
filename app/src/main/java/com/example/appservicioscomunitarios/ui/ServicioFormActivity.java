package com.example.appservicioscomunitarios.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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

    private static final int PERMISSION_REQUEST_CODE = 100;

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

                            // Pedir permiso persistente para la URI
                            final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                            getContentResolver().takePersistableUriPermission(uri, takeFlags);
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

        imageView.setOnClickListener(v -> pedirPermisoYSeleccionarImagen());

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

    private void pedirPermisoYSeleccionarImagen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, PERMISSION_REQUEST_CODE);
            } else {
                abrirSelectorImagen();
            }
        } else {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                abrirSelectorImagen();
            }
        }
    }

    private void abrirSelectorImagen() {
        selectImageLauncher.launch("image/*");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                abrirSelectorImagen();
            } else {
                Toast.makeText(this, "Permiso denegado para acceder a las imágenes", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
