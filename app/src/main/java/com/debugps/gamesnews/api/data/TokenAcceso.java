package com.debugps.gamesnews.api.data;

import com.google.gson.annotations.SerializedName;

/**
 * Clase que contiene el esquema para trasnformar el JSON resultante de la peticion de login a la API
 */
public class TokenAcceso {

    @SerializedName("token")
    private String token;

    /**
     * Metodo getter del campo token
     * @return Devuelve un String contenedor del Token de acceso de la API para un usuario
     */
    public String getToken() {
        return token;
    }
}
