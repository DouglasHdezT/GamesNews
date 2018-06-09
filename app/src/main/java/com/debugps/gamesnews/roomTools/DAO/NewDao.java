package com.debugps.gamesnews.roomTools.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.debugps.gamesnews.roomTools.POJO.New;

import java.util.List;

/**
 * Interfaz encargada de realizar las consultas a la base de datos.
 */
@Dao
public interface NewDao {

    /**
     * Metodo encargaddo de inssertar en la base una nueva noticia.
     * @param new_var Noticia a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNew(New new_var);

    /**
     *Metodo que elimina todas las noticias de la tabla new_table.
     */
    @Query("DELETE FROM new_table")
    void deleteAllNews();

    /**
     * Metodo para actualizar valores especificos en la tabla.
     * @param new_var Noticia a actualizar.
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateNew(New new_var);

    @Query("SELECT * FROM new_table WHERE _id = :id")
    LiveData<List<New>> getANew(String id);

    @Query("UPDATE new_table SET favorited = 1  where _id = :id")
    void setFavoritedNew(String id);

    @Query("UPDATE new_table SET favorited = 0  where _id = :id")
    void unsetFavoritedNew(String id);

    /**
     * Metodo que recupera todas las noticias de la tabla new_table.
     * @return
     */
    @Query("SELECT * FROM new_table ORDER BY createdDate DESC")
    LiveData<List<New>> getAllNews();

    /**
     * Metodo que recupera las noticias dependiendo del juego al que pertenece.
     * @param game_name Nombre del juego a buscar.
     * @return
     */
    @Query("SELECT * FROM new_table WHERE game = :game_name ORDER BY createdDate DESC")
    LiveData<List<New>> getNewsFromGame(String game_name);

    /**
     * Metodo que recupera la cantidad de noticias que hay en la base
     * @return
     */
    @Query("SELECT count(*) FROM new_table")
    LiveData<Double> getCantNews();

    /**
     * Metodo que devuelve todos los datos favoritos.
     * @return
     */
    @Query("SELECT * FROM new_table WHERE favorited = 1")
    LiveData<List<New>> getFavoritesNews();
}
