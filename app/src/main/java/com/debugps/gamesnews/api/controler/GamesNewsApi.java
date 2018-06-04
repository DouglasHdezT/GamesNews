package com.debugps.gamesnews.api.controler;

import com.debugps.gamesnews.api.data.NewDataAPI;
import com.debugps.gamesnews.api.data.TokenAcceso;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


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

    @GET("/news")
    Single<List<NewDataAPI>> getAllNews();
}
