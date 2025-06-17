package com.example.appservicioscomunitarios.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ServicioDao {
    @Query("SELECT * FROM servicios")
    List<Servicio> getAll();

    @Query("SELECT * FROM servicios WHERE id = :id LIMIT 1")
    Servicio getById(int id);

    @Insert
    void insert(Servicio servicio);

    @Update
    void update(Servicio servicio);

    @Delete
    void delete(Servicio servicio);
}