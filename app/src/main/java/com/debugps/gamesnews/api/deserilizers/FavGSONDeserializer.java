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
import java.util.Arrays;

public class FavGSONDeserializer implements JsonDeserializer<FavArrayListDataApi> {
    @Override
    public FavArrayListDataApi deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        final String[] favorites_news_objects = context.deserialize(jsonObject.get("favoriteNews"), String[].class);

        final ArrayList<String> favorites_news_id = new ArrayList<>(Arrays.asList(favorites_news_objects));

        return new FavArrayListDataApi(favorites_news_id);
    }
}
