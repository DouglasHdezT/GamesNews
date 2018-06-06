package com.debugps.gamesnews.tools;

import android.os.AsyncTask;

import com.debugps.gamesnews.MainActivity;
import com.debugps.gamesnews.interfaces.MainTools;

public class RefreshAsyncTask extends AsyncTask<Void,Void,Integer> {

    @Override
    protected Integer doInBackground(Void... voids) {

        while(MainActivity.REFRESH_DONE_PLAYERS != 1 &&
                MainActivity.REFRESH_DONE_CATEGORIES != 1 &&
                MainActivity.REFRESH_DONE_NEWS != 1){

            if(MainActivity.REFRESH_DONE_PLAYERS == -1 ||
                    MainActivity.REFRESH_DONE_CATEGORIES == -1 ||
                    MainActivity.REFRESH_DONE_NEWS == -1){
                MainActivity.IN_REFRESH = 0;
                return -1;
            }
        }
        MainActivity.IN_REFRESH = 0;
        return 1;
    }
}
