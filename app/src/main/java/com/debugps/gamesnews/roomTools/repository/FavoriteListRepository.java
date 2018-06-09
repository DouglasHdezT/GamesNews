package com.debugps.gamesnews.roomTools.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.debugps.gamesnews.roomTools.DAO.FavoriteListDAO;
import com.debugps.gamesnews.roomTools.POJO.FavoriteList;
import com.debugps.gamesnews.roomTools.database.NewRoomDatabase;

import java.util.List;

public class FavoriteListRepository {

    private FavoriteListDAO favoriteListDAO;

    private LiveData<List<FavoriteList>> favorite_list;

    public FavoriteListRepository(Application application){
        favoriteListDAO = NewRoomDatabase.getDatabaseInstance(application).favoriteListDAO();
        favorite_list = favoriteListDAO.getAllFavNews();
    }

    public LiveData<List<FavoriteList>> getFavorite_list() {
        return favorite_list;
    }

    public void insertFavNew(String id){
        new InsertAsynTask(favoriteListDAO).execute(new FavoriteList(id));

    }

    public void DeleteFavNew(String id){
        new DeleteAsynTask(favoriteListDAO).execute(id);

    }

    public void deleteAllFavNews(){
        new DeleteAllAsynTask(favoriteListDAO).execute();
    }

    private static class InsertAsynTask extends AsyncTask<FavoriteList, Void, Void>{

        private FavoriteListDAO dao;

        public InsertAsynTask(FavoriteListDAO dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(FavoriteList... favoriteLists) {
            dao.insertFavNew(favoriteLists[0]);
            return null;
        }
    }

    private static class DeleteAllAsynTask extends AsyncTask<Void, Void, Void>{

        private FavoriteListDAO dao;

        public DeleteAllAsynTask(FavoriteListDAO dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAllFavs();
            return null;
        }
    }

    private static class DeleteAsynTask extends AsyncTask<String, Void, Void>{

        private FavoriteListDAO dao;

        public DeleteAsynTask(FavoriteListDAO dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(String... id) {
            dao.deleteFavNew(id[0]);
            return null;
        }
    }
}
