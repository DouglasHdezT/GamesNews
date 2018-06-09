package com.debugps.gamesnews.api.deserilizers;

import com.debugps.gamesnews.api.data.FavArrayListDataApi;
import com.debugps.gamesnews.api.data.NewDataAPI;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FavGSONDeserializer implements JsonDeserializer<FavArrayListDataApi> {
    @Override
    public FavArrayListDataApi deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        final NewDataAPI[] favorites_news_objects = context.deserialize(jsonObject.get("favoriteNews"), NewDataAPI[].class);
        final ArrayList<String> favorites_news_id = new ArrayList<>();

        for (NewDataAPI favorites_news_object : favorites_news_objects) {
            favorites_news_id.add(favorites_news_object.getId());
        }

        return new FavArrayListDataApi(favorites_news_id);
    }
}
