package com.debugps.gamesnews.api.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Clase que contiene el esquema para trasnformar el JSON resultante de la peticion de login a la API.
 */
public class TokenAcceso implements Parcelable{

    @SerializedName("token")
    private String token;

    /**
     * Constructor de la Clase TokenAcceso Vacio.
     */
    public TokenAcceso() {
    }

    /**
     * Constructor de la clase TokenAcceso con paramentros.
     * @param token Valos en String del token de autentificacion de la API.
     */
    public TokenAcceso(String token) {
        this.token = token;
    }

    /**
     * Metodo getter del campo token
     * @return Devuelve un String contenedor del Token de acceso de la API para un usuario.
     */
    public String getToken() {
        return token;
    }

    protected TokenAcceso(Parcel in) {
        token = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(token);
    }

    public static final Creator<TokenAcceso> CREATOR = new Creator<TokenAcceso>() {
        @Override
        public TokenAcceso createFromParcel(Parcel in) {
            return new TokenAcceso(in);
        }

        @Override
        public TokenAcceso[] newArray(int size) {
            return new TokenAcceso[size];
        }
    };
}
