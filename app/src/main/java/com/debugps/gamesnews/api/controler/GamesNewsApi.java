package com.debugps.gamesnews.api.controler;

import com.debugps.gamesnews.api.data.FavArrayListDataApi;
import com.debugps.gamesnews.api.data.NewDataAPI;
import com.debugps.gamesnews.api.data.PlayerDataApi;
import com.debugps.gamesnews.api.data.TokenAcceso;
import com.debugps.gamesnews.api.data.UserDataApi;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


/**
 * @author DebugPs
 * Interfaz encargada de realizar las peticiones a la API GameNewUca. Sera de caracter general para todas las clases.
 * Se hara uso de el Framework Retrofit para dicho cometido.
 */
public interface GamesNewsApi {

    /**
     * Campo que define de forma estatica la ruta principal en la web de la API.
     */
    String ENDPOINT = "http://gamenewsuca.herokuapp.com";

    /**
     * Metodo utilizado para iniciar sesion en la aplicacion, verifiando el usuario en la base de la api.
     * @param user Nombre de usuario
     * @param password Contraseña de usuario. Esta no debe estar cifrada
     * @return La peticion devuelve un JSON con un token de acceso, este JSON sera procesado en un Single"String" utilizable.
     */
    @POST("/login")
    @FormUrlEncoded
    Single<TokenAcceso> initLogin(@Field("user") String user, @Field("password") String password );

    /**
     * Metodo utilizado para sacar todos las noticias de la API.
     * @return Lista de noticias.
     */
    @GET("/news")
    Single<List<NewDataAPI>> getAllNews();

    /**
     * Obtiene todos las categorias de juegos.
     * @return Lista de Strings con las categorias.
     */
    @GET("/news/type/list")
    Single<List<String>> getListTypeGames();

    /**
     * Obtiene todos los jugadores de la API
     * @return Lista de Jugadores de la API
     */
    @GET("/players")
    Single<List<PlayerDataApi>> getAllPlayers();

    @GET("/users/detail")
    Single<UserDataApi> getUserInfo();

    @GET("/users/detail")
    Single<FavArrayListDataApi> getUserFavs();

    @DELETE("/users/{id_user}/fav")
    void deleteFavFromList(@Path("id_user") String id_user, @Field("new") String id_new);

    @POST("/users/{id_user}/fav")
    void PostFavToList(@Path("id_user") String id_user, @Field("new") String id_new);
}
