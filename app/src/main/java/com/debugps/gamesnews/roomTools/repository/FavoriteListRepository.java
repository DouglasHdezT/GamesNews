package com.debugps.gamesnews.roomTools.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.debugps.gamesnews.MainActivity;
import com.debugps.gamesnews.api.controler.GamesNewsApi;
import com.debugps.gamesnews.api.data.FavArrayListDataApi;
import com.debugps.gamesnews.api.deserilizers.FavGSONDeserializer;
import com.debugps.gamesnews.roomTools.DAO.FavoriteListDAO;
import com.debugps.gamesnews.roomTools.POJO.FavoriteList;
import com.debugps.gamesnews.roomTools.database.NewRoomDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavoriteListRepository {

    private FavoriteListDAO favoriteListDAO;

    private LiveData<List<FavoriteList>> favorite_list;
    private ArrayList<String> online_list;

    private CompositeDisposable compositeDisposable =  new CompositeDisposable();
    private GamesNewsApi gamesNewsApi;

    public FavoriteListRepository(Application application){
        favoriteListDAO = NewRoomDatabase.getDatabaseInstance(application).favoriteListDAO();
        favorite_list = favoriteListDAO.getAllFavNews();

        gamesNewsApi = createGamesNewApi();
    }

    public LiveData<List<FavoriteList>> getFavorite_list() {
        return favorite_list;
    }

    public void refreshFavs(){
        compositeDisposable.add(gamesNewsApi.getUserFavs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getFavRefreshObserver()));
    }

    public ArrayList<String> getOnline_list() {
        compositeDisposable.add(gamesNewsApi.getUserFavs().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getFavObserver()));

        return online_list;
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

    private GamesNewsApi createGamesNewApi(){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(FavArrayListDataApi.class, new FavGSONDeserializer())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        Request.Builder builder = originalRequest.newBuilder()
                                .addHeader("Authorization", "Bearer " + MainActivity.token_var);

                        Request newRequest = builder.build();
                        return chain.proceed(newRequest);
                    }
                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GamesNewsApi.ENDPOINT)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(GamesNewsApi.class);
    }

    private DisposableSingleObserver<FavArrayListDataApi> getFavObserver(){
        return new DisposableSingleObserver<FavArrayListDataApi>() {
            @Override
            public void onSuccess(FavArrayListDataApi values) {
                online_list = values.getFav_list();
            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }

    private DisposableSingleObserver<FavArrayListDataApi> getFavRefreshObserver(){
        return new DisposableSingleObserver<FavArrayListDataApi>() {
            @Override
            public void onSuccess(FavArrayListDataApi values) {
                for(String value: values.getFav_list()){
                    //Log.d("hola", value);
                    insertFavNew(value);
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }
}
