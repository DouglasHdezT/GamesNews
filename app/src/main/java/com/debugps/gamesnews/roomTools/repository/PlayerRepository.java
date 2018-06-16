package com.debugps.gamesnews.roomTools.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.debugps.gamesnews.MainActivity;
import com.debugps.gamesnews.api.controler.GamesNewsApi;
import com.debugps.gamesnews.api.data.PlayerDataApi;
import com.debugps.gamesnews.roomTools.DAO.PlayerDAO;
import com.debugps.gamesnews.roomTools.POJO.Player;
import com.debugps.gamesnews.roomTools.database.NewRoomDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
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

public class PlayerRepository {

    private PlayerDAO playerDAO;

    private LiveData<List<Player>> playersPerGame;

    private GamesNewsApi gamesNewsApi;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public PlayerRepository(Application application){
        playerDAO = NewRoomDatabase.getDatabaseInstance(application).playerDAO();
        gamesNewsApi = createGamesNewApi();
        refreshPlayers();
    }

    public LiveData<List<Player>> getPlayerPerGame(String game_name) {
        playersPerGame = playerDAO.getPlayerPerGame(game_name);
        return playersPerGame;
    }

    public void refreshPlayers(){
        compositeDisposable.add(gamesNewsApi.getAllPlayers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getPlayersObservers()));
    }

    public void insertPlayer(Player player){
        new InsertAsycTask(playerDAO).execute(player);
    }

    public void deleteAllPlayers(){
        new DeleteAsycTask(playerDAO).execute();
    }

    private static class InsertAsycTask extends AsyncTask<Player, Void, Void>{

        private PlayerDAO playerDAO;

        private InsertAsycTask(PlayerDAO playerDAO) {
            this.playerDAO = playerDAO;
        }

        @Override
        protected Void doInBackground(Player... players) {

            playerDAO.insertPlayer(players[0]);

            return null;
        }
    }

    private static class DeleteAsycTask extends AsyncTask<Void, Void, Void>{

        private PlayerDAO playerDAO;

        private DeleteAsycTask(PlayerDAO playerDAO) {
            this.playerDAO = playerDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            playerDAO.deleteAllPlayers();

            return null;
        }
    }

    private GamesNewsApi createGamesNewApi(){
        Gson gson = new GsonBuilder()
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

    private DisposableSingleObserver<List<PlayerDataApi>> getPlayersObservers(){
        return  new DisposableSingleObserver<List<PlayerDataApi>>() {
            @Override
            public void onSuccess(List<PlayerDataApi> players) {
                deleteAllPlayers();
                for(PlayerDataApi player: players){
                    insertPlayer(new Player(
                            player.get_id(),
                            player.getName(),
                            player.getAvatar(),
                            player.getBiografia(),
                            player.getGame()));
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }
}
