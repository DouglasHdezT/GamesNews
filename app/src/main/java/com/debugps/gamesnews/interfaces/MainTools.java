package com.debugps.gamesnews.interfaces;

import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Interfaz para verficiar la existencia de internet.
 */
public interface MainTools {
    boolean isNetworkAvailable();
    void refreshAll();
}
