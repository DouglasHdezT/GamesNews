package com.debugps.gamesnews.roomTools.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.debugps.gamesnews.interfaces.MainTools;
import com.debugps.gamesnews.roomTools.POJO.User;
import com.debugps.gamesnews.roomTools.repository.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private LiveData<List<User>> user_list;


    public UserViewModel(@NonNull Application application, MainTools tools) {
        super(application);

        userRepository = new UserRepository(application, tools);

        user_list = userRepository.getUser_list();
    }

    public LiveData<List<User>> getUser_list() {
        return user_list;
    }

    public void insertUser(User user){
        userRepository.insertUser(user);
    }

    public void deleteAll(){
        userRepository.deleeteAllUsers();
    }

    public void refreshUsers(){
        userRepository.refreshDataUser();
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory{

        @NonNull
        private Application application;

        private MainTools tools;

        public Factory(@NonNull Application application, MainTools tools){
            this.application= application;
            this.tools = tools;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new UserViewModel(application, tools);
        }
    }
}
