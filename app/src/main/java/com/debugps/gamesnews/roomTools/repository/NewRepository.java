package com.debugps.gamesnews.roomTools.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.debugps.gamesnews.MainActivity;
import com.debugps.gamesnews.api.controler.GamesNewsApi;
import com.debugps.gamesnews.api.data.FavArrayListDataApi;
import com.debugps.gamesnews.api.data.NewDataAPI;
import com.debugps.gamesnews.api.deserilizers.FavGSONDeserializer;
import com.debugps.gamesnews.interfaces.MainTools;
import com.debugps.gamesnews.roomTools.DAO.NewDao;
import com.debugps.gamesnews.roomTools.POJO.New;
import com.debugps.gamesnews.roomTools.database.NewRoomDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Clase de tipo repositorio con el fin de intermediar al usuario con el contacto en la base de datos.
 */
public class NewRepository {

    private NewDao newDao;
    private LiveData<Double> cant_news;
    private LiveData<List<New>> mAllNews;
    private LiveData<List<New>> favNews;
    private LiveData<List<New>> newsPerGame;

    private GamesNewsApi gamesNewsApi;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    /**
     * Constructor que intancia la base de datos y los campos LiveData.
     * @param application Contexto de la App.
     */
    public NewRepository(Application application){

        NewRoomDatabase db = NewRoomDatabase.getDatabaseInstance(application);
        newDao =  db.newDao();

        cant_news = newDao.getCantNews();
        mAllNews = newDao.getAllNews();
        favNews = newDao.getFavoritesNews();

        gamesNewsApi = createGamesNewApi();
    }

    /**
     * Wrapper entre el DAO y el usuario.
     * @return Lista de todas las news.
     */
    public LiveData<List<New>> getmAllNews() {
        return mAllNews;
    }

    public void refreshNews(){
        compositeDisposable.add(gamesNewsApi.getAllNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getNewsObserver()));
    }

    public void getUserFavList(){
        compositeDisposable.add(gamesNewsApi.getUserFavs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getFavListObserver()));
    }

    /**
     * Wrapper entre el Dao y el usuario
     * @return Cantidad de todas las News
     */
    public LiveData<Double> getCant_news() {
        return cant_news;
    }

    /**
     * Wrapper entre el Dao y el usuario, para una lista especifica.
     * @param name Nombre del juego especifico.
     * @return Lista de noticias por juego.
     */
    public LiveData<List<New>> getNewsPerGame(String name) {
        newsPerGame = newDao.getNewsFromGame(name);
        return newsPerGame;
    }

    public LiveData<List<New>> getFavNews() {
        return favNews;
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

        private InsertAsyncTask(NewDao newDao){
            mAsyncNewDao = newDao;
        }

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

        private UpdateAsyncTask(NewDao newDao){
            mAsyncNewDao = newDao;
        }

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

        private DeleteAsyncTask(NewDao mNewDao) {
            this.mNewDao = mNewDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mNewDao.deleteAllNews();
            return null;
        }
    }

    /**
     * Metodo que instancia la API para realizar las peticiones.
     * @return GamesNewsAPI para realizar peticiones
     */
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

    /**
     * Metodo que devuelve un observer que reacciona ante respuestas de la API  en el ambito de listas de News
     * @return Observer a implementar
     */
    private DisposableSingleObserver<List<NewDataAPI>> getNewsObserver(){
        return new DisposableSingleObserver<List<NewDataAPI>>() {
            @Override
            public void onSuccess(List<NewDataAPI> news_list) {
                for(NewDataAPI new_var: news_list){
                    insertNew(new New(
                            new_var.getId(),
                            new_var.getTitle(),
                            new_var.getBody(),
                            new_var.getGame(),
                            new_var.getCoverImage(),
                            new_var.getDescription(),
                            new_var.getCreatedDate(),
                            0,
                            MainActivity.getColorId(),
                            new_var.get__v()));
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        };
    }

    private DisposableSingleObserver<FavArrayListDataApi> getFavListObserver(){
        return  new DisposableSingleObserver<FavArrayListDataApi>() {
            @Override
            public void onSuccess(FavArrayListDataApi Datalist) {
                ArrayList<String> list = Datalist.getFav_list();
                for(String value: list){
                    Log.d("PUTO EL QUE LO LEA", value);
                }

            }

            @Override
            public void onError(Throwable e) {
                Log.d("PUTO EL QUE LO LEA", "cague");
            }
        };
    }
}
