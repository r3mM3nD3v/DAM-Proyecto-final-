package com.example.appservicioscomunitarios.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "servicios")
public class Servicio {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String titulo;
    private String descripcion;
    private String imagenUri;

    public Servicio() {}

    @Ignore
    public Servicio(String titulo, String descripcion, String imagenUri) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagenUri = imagenUri;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getImagenUri() { return imagenUri; }
    public void setImagenUri(String imagenUri) { this.imagenUri = imagenUri; }
}
