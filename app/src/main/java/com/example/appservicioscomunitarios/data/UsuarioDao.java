package com.example.appservicioscomunitarios.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UsuarioDao {

    @Insert
    void insert(Usuario usuario);

    @Query("SELECT * FROM usuarios WHERE usuario = :usuario AND clave = :clave LIMIT 1")
    Usuario login(String usuario, String clave);

    @Query("SELECT COUNT(*) FROM usuarios WHERE usuario = :usuario")
    int existeUsuario(String usuario);

    // Método para buscar un usuario por nombre de usuario
    @Query("SELECT * FROM usuarios WHERE usuario = :usuario LIMIT 1")
    Usuario findByUsername(String usuario);
}
