package com.debugps.gamesnews.interfaces;

import android.support.v4.widget.SwipeRefreshLayout;

import com.debugps.gamesnews.roomTools.POJO.New;
import com.debugps.gamesnews.roomTools.POJO.Player;

/**
 * Interfaz para verficiar la existencia de internet.
 */
public interface MainTools {
    boolean isNetworkAvailable();
    void refreshAll();
    void showNewDialog(New new_var);
    void showPlayerDialog(Player player);
}
