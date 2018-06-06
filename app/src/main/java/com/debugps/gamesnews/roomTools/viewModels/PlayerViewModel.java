package com.debugps.gamesnews.roomTools.viewModels;

import android.app.Application;
import android.app.ListActivity;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.debugps.gamesnews.roomTools.POJO.Player;
import com.debugps.gamesnews.roomTools.repository.PlayerRepository;

import java.util.List;

public class PlayerViewModel extends AndroidViewModel {

    private LiveData<List<Player>> playersPerGame;
    private PlayerRepository playerRepository;

    public PlayerViewModel(@NonNull Application application) {
        super(application);

        playerRepository = new PlayerRepository(application);
    }

    public LiveData<List<Player>> getPlayersPerGame(String game_name) {
        playersPerGame = playerRepository.getPlayerPerGame(game_name);

        return playersPerGame;
    }

    public void insertPlayer(Player player){
        playerRepository.insertPlayer(player);
    }

    public void deleteAllPlayers(){
        playerRepository.deleteAllPlayers();
    }

    public void refreshPlayers(){
        playerRepository.refreshPlayers();
    }
}
