package com.debugps.gamesnews.roomTools.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.debugps.gamesnews.roomTools.POJO.New;
import com.debugps.gamesnews.roomTools.repository.NewRepository;

import java.util.List;

/**
 * Clase que sirve para guardar informacion en cambios de configuracion; ademas es la capa de contacto con el usuario.
 */
public class NewViewModel extends AndroidViewModel {

    private NewRepository mNewRepository;

    private LiveData<Double> cant_news;
    private LiveData<List<New>> mAllNews;
    private LiveData<List<New>> newsPerGame;

    /**
     * Constructor del Modelo que instacia un Repositorio y parametros de uso comun.
     * @param application Contexto de la Aplicacion.
     */
    public NewViewModel(@NonNull Application application) {
        super(application);
        mNewRepository = new NewRepository(application);
        cant_news = mNewRepository.getCant_news();
        mAllNews = mNewRepository.getmAllNews();
    }

    /**
     * Metodo para insertar una nueva noticia.
     * @param new_var Noticia nueva.
     */
    public void insertNew(New new_var){
        mNewRepository.insertNew(new_var);
    }

    /**
     * Metodo para actualizar una noticia.
     * @param _var Noticia a actualizar.
     */
    public void updateNew(New _var){
        mNewRepository.updateNew(_var);
    }

    /**
     * Metodo para borrar todas las noticias de la base de datos.
     */
    public void deleteAllNews(){
        mNewRepository.deleteAllNews();
    }

    /**
     * Metodo para obtener todas la noticias.
     * @return LiveData con las noticias.
     */
    public LiveData<List<New>> getAllNews() {
        return mAllNews;
    }

    /**
     * Metodo para obtener el total de noticias.
     * @return Double con el numero de noticias.
     */
    public LiveData<Double> getCant_news() {
        return cant_news;
    }

    /**
     * Metodo que para obtener una lista de noticias por juego.
     * @param name Nombre del juego.
     * @return LiveData con las noticias por juego.
     */
    public LiveData<List<New>> getNewsPerGame(String name) {
        newsPerGame = mNewRepository.getNewsPerGame(name);
        return newsPerGame;
    }
}
