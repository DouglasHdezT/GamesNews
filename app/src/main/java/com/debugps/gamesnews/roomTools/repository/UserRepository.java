package com.debugps.gamesnews.roomTools.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.util.Log;

import com.debugps.gamesnews.MainActivity;
import com.debugps.gamesnews.api.controler.GamesNewsApi;
import com.debugps.gamesnews.api.data.UserDataApi;
import com.debugps.gamesnews.interfaces.MainTools;
import com.debugps.gamesnews.login.LoginActivity;
import com.debugps.gamesnews.roomTools.DAO.UserDao;
import com.debugps.gamesnews.roomTools.POJO.User;
import com.debugps.gamesnews.roomTools.database.NewRoomDatabase;
import com.debugps.gamesnews.roomTools.viewModels.UserViewModel;
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

public class UserRepository {

    private UserDao userDao;

    private LiveData<List<User>> user_list;

    private CompositeDisposable compositeDisposable= new CompositeDisposable();
    private GamesNewsApi gamesNewsApi;

    private MainTools tools;

    public UserRepository(Application application, MainTools tools){
        userDao = NewRoomDatabase.getDatabaseInstance(application).userDao();

        user_list = userDao.getUser();
        this.tools = tools;

        gamesNewsApi = createGamesNewApi();
    }

    public LiveData<List<User>> getUser_list() {
        return user_list;
    }

    public void refreshDataUser(){
        compositeDisposable.add(
                gamesNewsApi.getUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getUserObserver()));
    }

    public void insertUser(User user){
        new InsertAsycTask(userDao).execute(user);
    }

    public void deleeteAllUsers(){
        new DeleteAsycTask(userDao).execute();
    }

    private static class InsertAsycTask extends AsyncTask<User, Void, Void> {

        private UserDao userDao;

        private InsertAsycTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... players) {

            userDao.insertUser(players[0]);

            return null;
        }
    }

    private static class DeleteAsycTask extends AsyncTask<Void, Void, Void> {

        private UserDao userDao;

        private DeleteAsycTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(Void... players) {

            userDao.deleteAllUser();

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

                        Response response = chain.proceed(originalRequest);
                        Log.d("asd",response.body().string());
                        if(response.code() == 401){
                        }
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

    private DisposableSingleObserver<UserDataApi> getUserObserver(){
        return new DisposableSingleObserver<UserDataApi>() {
            @Override
            public void onSuccess(UserDataApi value) {
                insertUser(new User(
                        value.get_id(),
                        value.getUser(),
                        value.getPassword(),
                        value.getAvatar(),
                        value.getCreatedDate()
                ));
            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }
}
