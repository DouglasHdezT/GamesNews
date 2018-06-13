package com.debugps.gamesnews.login;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.debugps.gamesnews.MainActivity;
import com.debugps.gamesnews.R;
import com.debugps.gamesnews.api.controler.GamesNewsApi;
import com.debugps.gamesnews.api.data.TokenAcceso;
import com.debugps.gamesnews.roomTools.viewModels.UserViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Clase encargada de iniciar sesion en la aplicacion; esto se lograra conectando con la API, traduciendo los JSON,
 * administrando a traves de TOKENS, guardando preferencias en el SHARED_PREFERENCES, y administrando la UI del login.
 * Esta clase por defecto esta establecida como Launcher de la aplicacion.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Campo utilizado para el control y acceso a las Shared preferences y poder consultar si existe algun token.
     * Tambien sera la llave utilizada para comunicar la actividad login con el Main Activity.
     */
    public static String SHARED_TOKEN_KEY = "TOKEN_KEY_SHARED";

    public static String Token_var = "";
    private TokenAcceso token;

    private EditText username;
    private EditText password;
    private Button loginButton;
    private LinearLayout mainView;

    private GamesNewsApi gamesNewsApi;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private UserViewModel userViewModel;

    /**
     * Metodo sobreescrito encargado del flujo principal de la actividad "Login"
     * @param savedInstanceState Bundle para la recuperacion de los datos al momento de cambiar configuraciones.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Token_var = getApplicationContext().getSharedPreferences("Shared",Context.MODE_PRIVATE).getString(SHARED_TOKEN_KEY, "");

        if(!Token_var.equals("")){
            token = new TokenAcceso(Token_var);
            startMainActivity(token);
        }

        setContentView(R.layout.activity_login);

        setResourcesUp();

        gamesNewsApi = createGamesNewsApi();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username_txt = username.getText().toString();
                String password_txt = password.getText().toString();

                if(username_txt.equals("") || password_txt.equals("")){
                    Snackbar.make(mainView, R.string.error_empty_field, Snackbar.LENGTH_SHORT).show();
                }else{
                    compositeDisposable.add(gamesNewsApi.initLogin(username_txt,password_txt)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(getTokenObserver()));
                }
            }
        });

    }

    /**
     * Metodo encargado de encontrar y asignar valores a los componentes de la UI
     */
    private void setResourcesUp(){
        username = findViewById(R.id.edit_text_username_login);
        password = findViewById(R.id.edit_text_password_login);
        loginButton = findViewById(R.id.button_signin_login);
        mainView = findViewById(R.id.main_view_login);
    }

    /**
     *Metodo encargado de crear el cliente que conecte la API para la autentificacion en el login.
     * @return Devuevle la instancia de un GamesNewsApi.
     */
    private GamesNewsApi createGamesNewsApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GamesNewsApi.ENDPOINT)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(GamesNewsApi.class);
    }

    /**
     * Crea un Observer para los objetos Single"Token" obtenidos por la API y realiza las acciones necesarias cuando se ha tenido una respuesta exitosa.
     * @return Devuelve un observador encargado de realizar los cambios necesarios en el flujo del login.
     */
    private DisposableSingleObserver<TokenAcceso> getTokenObserver(){
        return new DisposableSingleObserver<TokenAcceso>() {
            @Override
            public void onSuccess(TokenAcceso value) {
                token = value;

                if(token.getToken() != null){
                    SharedPreferences sharedPreferences = LoginActivity.this.getApplicationContext().getSharedPreferences("Shared",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(SHARED_TOKEN_KEY, token.getToken());
                    editor.apply();
                    Token_var = token.getToken();
                    userViewModel = ViewModelProviders.of(LoginActivity.this).get(UserViewModel.class);
                    userViewModel.refreshUsers();
                    startMainActivity(token);
                    //Log.i("Token", token.getToken());
                }else{
                    Snackbar.make(mainView, R.string.error_invalid_credentias, Snackbar.LENGTH_SHORT).show();
                    username.setText("");
                    password.setText("");
                }


            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Snackbar.make(mainView, R.string.error_login_connect, Snackbar.LENGTH_SHORT).show();
            }
        };
    }

    /**
     * Metodo encargado de iniciar Main Activity, la cual es encargada de tolo el funcionamiento de la App.
     * @param tokenAcceso Objeto encargado de contener el String correspondiente al Token de acceso de la API.
     */
    private void startMainActivity(TokenAcceso tokenAcceso){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra(SHARED_TOKEN_KEY, tokenAcceso);
        startActivity(intent);
    }
}
