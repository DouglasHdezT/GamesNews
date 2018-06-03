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

@Database(entities = {New.class}, version = 1)
public abstract class NewRoomDatabase extends RoomDatabase {
    public abstract NewDao  daoController();

    private static NetVerified netVerified;
    private static NewRoomDatabase INSTANCE;

    public static NewRoomDatabase getDatabaseInstance(final Context context){
        if (INSTANCE == null) {
            synchronized (NewRoomDatabase.class) {
                if (INSTANCE == null) {
                    netVerified = (NetVerified) context;
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NewRoomDatabase.class, "news_database")
                            .build();

                }
            }
        }
        return INSTANCE;
    }

    private static Callback sNewRoomDatabaseCallback =
            new Callback(){
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    if(netVerified.isNetworkAvailable()){

                    }
                }
            };

    private static class DeleteAsyncTask extends AsyncTask<Void, Void, Void>{

        private final NewDao mNewDao;

        private DeleteAsyncTask(NewDao mNewDao) {
            this.mNewDao = mNewDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mNewDao.deleteAllNews();
            return null;
        }
    }

}
