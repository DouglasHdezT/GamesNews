package com.debugps.gamesnews.roomTools.POJO;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * POJO que identifica una noticia en la base de datos generada por ROOM
 */
@Entity(tableName = "new_table")
public class New {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="_id")
    private String _id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "body")
    private String body;

    @ColumnInfo(name = "game")
    private String game;

    @ColumnInfo(name = "coverImage")
    private String coverImage;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "createdDate")
    private String createdDate;

    @ColumnInfo(name = "favorited")
    private int favorited;

    @ColumnInfo(name = "__v")
    private int __v;


    /**
     * Constructor con todos sus parametros
     * @param _id Identificador unico de la noticia.
     * @param title Titulo de la noticia.
     * @param body Cuerpo completo de la noticia.
     * @param game Juego al que pertenece la noticia.
     * @param coverImage URL de la imagen que representa la noticia.
     * @param description Breve descripcion de la noticia
     * @param createdDate Fecha de creacion de la noticia. Formato YYYY-MM-DDThh:mm:ss.ssssZ
     * @param favorited Si la noticia es favorita; 0 false, 1 true.
     * @param __v Nuemero de version
     */
    public New(@NonNull String _id, String title, String body, String game, String coverImage, String description, String createdDate, int favorited, int __v) {
        this._id = _id;
        this.title = title;
        this.body = body;
        this.game = game;
        this.coverImage = coverImage;
        this.description = description;
        this.createdDate = createdDate;
        this.favorited = favorited;
        this.__v = __v;
    }


    @NonNull
    public String get_id() {
        return _id;
    }

    public void set_id(@NonNull String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public int getFavorited() {
        return favorited;
    }

    public void setFavorited(int favorited) {
        this.favorited = favorited;
    }
}
