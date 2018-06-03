package com.debugps.gamesnews.roomTools.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.debugps.gamesnews.roomTools.POJO.New;
import com.debugps.gamesnews.roomTools.repository.NewRepository;

import java.util.ArrayList;
import java.util.List;

public class NewViewModel extends AndroidViewModel {

    private NewRepository mNewRepository;

    private LiveData<Double> cant_news;
    private LiveData<List<New>> mAllNews;
    private LiveData<List<New>> newsPerGame;

    public NewViewModel(@NonNull Application application) {
        super(application);
        mNewRepository = new NewRepository(application);
        cant_news = mNewRepository.getCant_news();
        mAllNews = mNewRepository.getmAllNews();
    }

    public void insertNew(New new_var){
        mNewRepository.insertNew(new_var);
    }

    public LiveData<List<New>> getAllNews() {
        return mAllNews;
    }

    public LiveData<Double> getCant_news() {
        return cant_news;
    }

    public LiveData<List<New>> getNewsPerGame(String name) {
        newsPerGame = mNewRepository.getNewsPerGame(name);
        return newsPerGame;
    }
}
