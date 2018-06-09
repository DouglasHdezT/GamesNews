package com.debugps.gamesnews.roomTools.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.debugps.gamesnews.roomTools.POJO.User;
import com.debugps.gamesnews.roomTools.repository.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private LiveData<List<User>> user_list;


    public UserViewModel(@NonNull Application application) {
        super(application);

        userRepository = new UserRepository(application);

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
}
