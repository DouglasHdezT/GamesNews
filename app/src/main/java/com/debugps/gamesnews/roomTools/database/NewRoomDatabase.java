package com.debugps.gamesnews.roomTools.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.debugps.gamesnews.roomTools.DAO.CategoryDAO;
import com.debugps.gamesnews.roomTools.DAO.FavoriteListDAO;
import com.debugps.gamesnews.roomTools.DAO.NewDao;
import com.debugps.gamesnews.roomTools.DAO.PlayerDAO;
import com.debugps.gamesnews.roomTools.POJO.Category;
import com.debugps.gamesnews.roomTools.POJO.FavoriteList;
import com.debugps.gamesnews.roomTools.POJO.New;
import com.debugps.gamesnews.roomTools.POJO.Player;

/**
 * Clase encargada de Instanciar la base de datos para la App
 */
@Database(entities = {New.class, Category.class, Player.class, FavoriteList.class}, version = 5)
public abstract class NewRoomDatabase extends RoomDatabase {

    private static NewRoomDatabase INSTANCE;

    /**
     * Metodo estatico que cumple con los patrones de diseño Singleton; esto significa tener una unica instancia de BD.
     * @param context Contexto con el que se va a instanciar la BD.
     * @return Devuelve la instancia de la Base de datos.
     */
    public static NewRoomDatabase getDatabaseInstance(final Context context){
        if (INSTANCE == null) {
            synchronized (NewRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NewRoomDatabase.class, "news_database")
                            .fallbackToDestructiveMigration()
                            .build();

                }
            }
        }
        return INSTANCE;
    }

    /**
     * Metodo abstracto destinado a recuperar el DAO.
     * @return Devuelve un NewDao para el control de QUERYS.
     */
    public abstract NewDao newDao();

    public abstract CategoryDAO categoryDAO();

    public abstract PlayerDAO playerDAO();

    public abstract FavoriteListDAO favoriteListDAO();

}
