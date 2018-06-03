package com.debugps.gamesnews.roomTools.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.debugps.gamesnews.interfaces.NetVerified;
import com.debugps.gamesnews.roomTools.DAO.NewDao;
import com.debugps.gamesnews.roomTools.POJO.New;

/**
 * Clase encargada de Instanciar la base de datos para la App
 */
@Database(entities = {New.class}, version = 1)
public abstract class NewRoomDatabase extends RoomDatabase {

    private static NetVerified netVerified;
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
                    netVerified = (NetVerified) context;
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NewRoomDatabase.class, "news_database")
                            .addCallback(sNewRoomDatabaseCallback)
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

    /**
     * Callback que elimina los datos de la base si hay internet. Esto para mantener vivas las noticias Offline.
     */
    private static Callback sNewRoomDatabaseCallback =
            new Callback(){
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    if(netVerified.isNetworkAvailable()){
                        new DeleteAsyncTask(INSTANCE).execute();
                    }
                }
            };

    /**
     * Clase Asicrona para elmiinar los datos de la Base sin para la ejecucion de la UI.
     */
    private static class DeleteAsyncTask extends AsyncTask<Void, Void, Void>{

        private final NewDao mNewDao;

        /**
         * Constructor de la instancia Asincrona
         * @param db Base de datos instanciada
         */
        private DeleteAsyncTask(NewRoomDatabase db) {
            this.mNewDao = db.newDao();
        }

        /**
         * Metodo implementado para realizar la eliminacion de datos en Backend
         * @param voids Nada
         * @return Nada
         */
        @Override
        protected Void doInBackground(Void... voids) {
            mNewDao.deleteAllNews();
            return null;
        }
    }

}
