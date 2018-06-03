package com.debugps.gamesnews.roomTools.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.debugps.gamesnews.roomTools.DAO.NewDao;
import com.debugps.gamesnews.roomTools.POJO.New;
import com.debugps.gamesnews.roomTools.database.NewRoomDatabase;

import java.util.List;

/**
 * Clase de tipo repositorio con el fin de intermediar al usuario con el contacto en la base de datos.
 */
public class NewRepository {

    private NewDao newDao;
    private LiveData<Double> cant_news;
    private LiveData<List<New>> mAllNews;
    private LiveData<List<New>> newsPerGame;

    /**
     * Constructor que intancia la base de datos y los campos LiveData.
     * @param application Contexto de la App.
     */
    public NewRepository(Application application){

        NewRoomDatabase db = NewRoomDatabase.getDatabaseInstance(application);
        newDao =  db.newDao();

        cant_news = newDao.getCantNews();
        mAllNews = newDao.getAllNews();
    }

    /**
     * Wrapper entre el DAO y el usuario.
     * @return Lista de todas las news.
     */
    public LiveData<List<New>> getmAllNews() {
        return mAllNews;
    }

    /**
     * Wrapper entre el Dao y el usuario
     * @return Cantidad de todas las News
     */
    public LiveData<Double> getCant_news() {
        return cant_news;
    }

    /**
     * Wrapper entre el Dao y el usuario, para una lsita especifica.
     * @param name Nombre del juego especifico.
     * @return Lista de noticias por juego.
     */
    public LiveData<List<New>> getNewsPerGame(String name) {
        newsPerGame = newDao.getNewsFromGame(name);
        return newsPerGame;
    }

    /**
     * Wrapper entre el Dao y el Usuario, para ingresar una NEW.
     * @param new_var New a ingresar
     */
    public void insertNew(New new_var){
        new InsertAsyncTask(newDao).execute(new_var);
    }

    /**
     * Wrapper entre el Dao y el Usuario, para actualizar una NEW.
     * @param _var New a actualizar
     */
    public void updateNew(New _var){
        new UpdateAsyncTask(newDao).execute(_var);
    }

    /**
     * Wrapper entre Dao y Usuario, para eliminar todas las NEW de la base de datos
     */
    public void deleteAllNews(){
        new DeleteAsyncTask(newDao).execute();
    }

    /**
     * Clase Asincrona para ingresar News en la base.
     */
    private static class InsertAsyncTask extends AsyncTask<New, Void, Void>{

        private NewDao mAsyncNewDao;

        /**
         * Constructor de la clase asincrona.
         * @param newDao DAO de control de QUERYS.
         */
        private InsertAsyncTask(NewDao newDao){
            mAsyncNewDao = newDao;
        }

        /**
         * Metodo para realizar los ingresos de noticias en Backend.
         * @param params Arrego de noticias; siempre viene solo 1.
         * @return Nada.
         */
        @Override
        protected Void doInBackground(New... params) {
            mAsyncNewDao.insertNew(params[0]);
            return null;
        }
    }

    /**
     * Clase Asincrona para actualizar News en la base.
     */
    private static class UpdateAsyncTask extends AsyncTask<New, Void, Void>{

        private NewDao mAsyncNewDao;

        /**
         * Constructor de la clase asincrona.
         * @param newDao DAO de control de QUERYS.
         */
        private UpdateAsyncTask(NewDao newDao){
            mAsyncNewDao = newDao;
        }

        /**
         * Metodo para realizar las actualizaciones de noticias en Backend.
         * @param params Arrego de noticias; siempre viene solo 1.
         * @return Nada.
         */
        @Override
        protected Void doInBackground(New... params) {
            mAsyncNewDao.updateNew(params[0]);
            return null;
        }
    }

    /**
     * Clase Asicrona para elmiinar los datos de la Base sin para la ejecucion de la UI.
     */
    private static class DeleteAsyncTask extends AsyncTask<Void, Void, Void>{

        private final NewDao mNewDao;

        /**
         * Constructor de la instancia Asincrona
         * @param mNewDao Dao de control
         */
        private DeleteAsyncTask(NewDao mNewDao) {
            this.mNewDao = mNewDao;
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
