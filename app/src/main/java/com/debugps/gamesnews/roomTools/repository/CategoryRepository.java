package com.debugps.gamesnews.roomTools.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.util.Log;

import com.debugps.gamesnews.MainActivity;
import com.debugps.gamesnews.api.controler.GamesNewsApi;
import com.debugps.gamesnews.roomTools.DAO.CategoryDAO;
import com.debugps.gamesnews.roomTools.POJO.Category;
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

public class CategoryRepository {

    private CategoryDAO categoryDAO;
    private LiveData<List<Category>> categoryList;
    private LiveData<Double> cantCategories;

    private GamesNewsApi gamesNewsApi;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public CategoryRepository(Application application){
        NewRoomDatabase db = NewRoomDatabase.getDatabaseInstance(application);
        categoryDAO = db.categoryDAO();

        categoryList = categoryDAO.getAllCategories();
        cantCategories = categoryDAO.getCantCategories();

        gamesNewsApi = createGamesNewApi();
    }

    public LiveData<List<Category>> getCategoryList() {
        return categoryList;
    }

    public void refreshCategories(){
        compositeDisposable.add(gamesNewsApi.getListTypeGames()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getCategoriesObserver()));

    }

    public LiveData<Double> getCantCategories() {
        return cantCategories;
    }

    public void insertCategory(Category category){
        new InsertCategoryAsyncTask(categoryDAO).execute(category);
    }

    public void deleteAllCategories(){
        new DeleteCategoriesAsyncTask(categoryDAO).execute();
    }

    private static class InsertCategoryAsyncTask extends AsyncTask<Category, Void, Void>{

        private CategoryDAO asyncCategoryDAO;

        private InsertCategoryAsyncTask(CategoryDAO asyncCategoryDAO) {
            this.asyncCategoryDAO = asyncCategoryDAO;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            asyncCategoryDAO.insertCategory(categories[0]);
            return null;
        }
    }

    private static class DeleteCategoriesAsyncTask extends AsyncTask<Void, Void, Void>{

        private CategoryDAO asyncCategoryDAO;

        private DeleteCategoriesAsyncTask(CategoryDAO asyncCategoryDAO) {
            this.asyncCategoryDAO = asyncCategoryDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            asyncCategoryDAO.deleteAllCategories();
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

    private DisposableSingleObserver<List<String>> getCategoriesObserver(){
        return new DisposableSingleObserver<List<String>>() {
            @Override
            public void onSuccess(List<String> strings) {
                deleteAllCategories();
                for(String value: strings){
                    insertCategory(new Category(value));
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }
}
