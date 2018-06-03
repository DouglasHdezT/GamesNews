package com.debugps.gamesnews.roomTools.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.os.Bundle;

import com.debugps.gamesnews.roomTools.DAO.NewDao;
import com.debugps.gamesnews.roomTools.POJO.New;
import com.debugps.gamesnews.roomTools.database.NewRoomDatabase;

import java.util.List;

public class NewRepository {

    private NewDao newDao;
    private LiveData<Double> cant_news;
    private LiveData<List<New>> mAllNews;
    private LiveData<List<New>> newsPerGame;

    public NewRepository(Application application){

        NewRoomDatabase db = NewRoomDatabase.getDatabaseInstance(application);
        newDao =  db.daoController();

        cant_news = newDao.getCantNews();
        mAllNews = newDao.getAllNews();
    }

    public LiveData<List<New>> getmAllNews() {
        return mAllNews;
    }

    public LiveData<Double> getCant_news() {
        return cant_news;
    }

    public LiveData<List<New>> getNewsPerGame(String name) {
        newsPerGame = newDao.getNewsFromGame(name);
        return newsPerGame;
    }

    public void insertNew(New new_var){
        new InsertAsyncTask(newDao).execute(new_var);
    }

    private static class InsertAsyncTask extends AsyncTask<New, Void, Void>{

        private NewDao mAsyncNewDao;

        private InsertAsyncTask(NewDao newDao){
            mAsyncNewDao = newDao;
        }

        @Override
        protected Void doInBackground(New... params) {
            mAsyncNewDao.insertNew(params[0]);
            return null;
        }
    }
}
