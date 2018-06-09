package com.debugps.gamesnews.api.data;

import java.util.ArrayList;

public class FavArrayListDataApi {
    private ArrayList<String> fav_list;

    public FavArrayListDataApi(ArrayList<String> fav_list) {
        this.fav_list = fav_list;
    }

    public ArrayList<String> getFav_list() {
        return fav_list;
    }

    public void setFav_list(ArrayList<String> fav_list) {
        this.fav_list = fav_list;
    }
}
