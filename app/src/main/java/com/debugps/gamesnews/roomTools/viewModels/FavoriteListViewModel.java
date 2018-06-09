package com.debugps.gamesnews.roomTools.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.debugps.gamesnews.roomTools.POJO.FavoriteList;
import com.debugps.gamesnews.roomTools.repository.FavoriteListRepository;

import java.util.ArrayList;
import java.util.List;

public class FavoriteListViewModel extends AndroidViewModel {

    private FavoriteListRepository repository;
    private LiveData<List<FavoriteList>> favorite_list;

    public FavoriteListViewModel(@NonNull Application application) {
        super(application);
        repository = new FavoriteListRepository(application);
        favorite_list = repository.getFavorite_list();
    }

    public LiveData<List<FavoriteList>> getFavorite_list() {
        return favorite_list;
    }

    public void insertFavNew(String id){
        repository.insertFavNew(id);
    }

    public void deleteFavNew(String id){
        repository.DeleteFavNew(id);
    }

    public void deleteAllFavNews(){
        repository.deleteAllFavNews();
    }

    public ArrayList<String> getOnlineList(){
        return repository.getOnline_list();
    }

    public void refreshFavorites(){
        repository.refreshFavs();
    }

}
