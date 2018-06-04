package com.debugps.gamesnews;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.SearchView;

import com.debugps.gamesnews.adapters.MainAdapter;
import com.debugps.gamesnews.api.controler.GamesNewsApi;
import com.debugps.gamesnews.api.data.TokenAcceso;
import com.debugps.gamesnews.fragment.NewsMainFragment;
import com.debugps.gamesnews.interfaces.NetVerified;
import com.debugps.gamesnews.login.LoginActivity;
import com.debugps.gamesnews.roomTools.POJO.New;
import com.debugps.gamesnews.roomTools.viewModels.NewViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Clase encargada del flujo principal, tanto logico como grafico, de toda la Aplicacion.
 */
public class MainActivity extends AppCompatActivity implements NetVerified, NewsMainFragment.MainTools{

    public static String token_var = "";
    private final static int ID_ITEM_MENU_GAMES = 101010;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private MainAdapter mainAdapter;
    private List<New> newList_main;
    private List<String> games_names;
    private ArrayList<String> styled_names;

    private NewViewModel newViewModel;
    private GamesNewsApi gamesNewsApi;
    private CompositeDisposable games_list_composite_disposable = new CompositeDisposable();

    /**
     * Metodo encargado de la creacion y asignacion de valores a los componentes, logicos y graficos, en la actividad Main.
     * @param savedInstanceState Bundle para la recuperacion de datos a la hora de cambiar de configuraciones.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TokenAcceso token = getIntent().getParcelableExtra(LoginActivity.SHARED_TOKEN_KEY);

        if(token ==  null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }else{
            token_var = token.getToken();
        }

        setResourcesUp();
        setAdaptersUp();

        gamesNewsApi = createGamesNewApi();

        newViewModel = ViewModelProviders.of(this).get(NewViewModel.class);

        games_list_composite_disposable.add(gamesNewsApi.getListTypeGames()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getGamesObserver()));
    }

    /**
     * Metodo implementado para el manejo de la logica en la actividad Main.
     */
    @Override
    protected void onResume() {
        super.onResume();

        NewsMainFragment newsMainFragment = new NewsMainFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_activity_frame_layout, newsMainFragment);
        ft.commit();

        newViewModel.getAllNews().observe(this, new Observer<List<New>>() {
            @Override
            public void onChanged(@Nullable List<New> news) {
                mainAdapter.setNewList(news);
                newList_main = news;
            }
        });

    }

    /**
     * Metodo implementado para controlar el flujo logico de la accion Buscar.
     * @param menu Menu que implementa el metodo.
     * @return Bandera de exito o fracaso.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        toolbar.inflateMenu(R.menu.main_toolbar_menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.main_menu_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    /**
     * Metodo utilizado para instanciar los Adaptadores de los distintos RecyclerViews
     */
    private void setAdaptersUp(){
        mainAdapter = new MainAdapter();
    }

    /**
     * Metodo utilizado para encontrar y asignar valores a los componentes graficos de la Actividad.
     */
    private void setResourcesUp(){
        drawerLayout =  findViewById(R.id.main_drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        navigationView =  findViewById(R.id.main_navigation_view);

        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setTitle(R.string.main_menu_title);
        toolbar.setNavigationOnClickListener(drawerButtonListener());
    }

    /**
     * Metodo que define un listener para la logica de apertura y cierre del drawermenu
     * @return Listener para el drawer dentro del menu
     */
    private View.OnClickListener drawerButtonListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerLayout.isDrawerOpen(navigationView)){
                    drawerLayout.closeDrawers();
                }else {
                    drawerLayout.openDrawer(navigationView);
                }
            }
        };
    }

    /**
     * Metodo encargado de levantar graficamente los items para el navigationView
     */
    private void setDrawerItemsUp(){
        Menu menu = navigationView.getMenu();
        MenuItem MI= menu.findItem(R.id.drawer_menu_games);

        styled_names = getPreferedNames(games_names);

        SubMenu subMenu = MI.getSubMenu();
        subMenu.clear();
        for(int i = 0; i < games_names.size(); i++){
            subMenu.add(R.id.group_games, ID_ITEM_MENU_GAMES+i, i, styled_names.get(i));
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.drawer_menu_news:
                        Log.d("Name", "News");
                        break;
                    case R.id.drawer_menu_fav:
                        Log.d("Name", "Fav");
                        break;
                    case R.id.drawer_menu_settings:
                        Log.d("Name", "Settings");
                        break;
                    default:
                        for(int i=0; i<games_names.size(); i++){
                            if(item.getItemId() == ID_ITEM_MENU_GAMES+i){
                                Log.d("Name", games_names.get(i));
                                Log.d("Name", styled_names.get(i));
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }

    /**
     * Metodo para estilizar las etiquetas del Navigation View; no es dinamico
     * @param names Lista de nombres de la API
     * @return Lista de nombres estilizados.
     */
    private ArrayList<String> getPreferedNames(List<String> names){
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < names.size(); i++){
            switch (names.get(i)){
                case "lol":
                    result.add(i, "League of Legends");
                    break;
                case "overwatch":
                    result.add(i, "Overwatch");
                    break;
                case "csgo":
                    result.add(i, "Counter Strike");
                    break;
                default:
                    result.add(i,names.get(i));
                    break;
            }
        }
        return result;
    }

    /**
     * Metodo implementado de la interfaz para verificar si existe internet
     * @return Boolenao para verificar si existe internet o no.
     */
    @Override
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Metodo utilizado para settear de manera local el adaptador del recycler en el MainFragment
     * @param rv RecyclerView a settear
     */
    @Override
    public void bindMainAdapter(RecyclerView rv) {
        rv.setAdapter(mainAdapter);
    }

    /**
     * Metodo que instancia la API para realizar las peticiones.
     * @return GamesNewsAPI para realizar peticiones
     */
    private GamesNewsApi createGamesNewApi(){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        Log.d("Token ", MainActivity.token_var);
                        Request.Builder builder = originalRequest.newBuilder()
                                .addHeader("Authorization", "Bearer " + MainActivity.token_var);

                        Request newRequest = builder.build();
                        return chain.proceed(newRequest);
                    }
                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GamesNewsApi.ENDPOINT)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(GamesNewsApi.class);
    }

    /**
     * Metodo que devuelve un observer que reacciona ante respuestas de la API  en el ambito de las categorias de los juegos
     * @return Observer a implementar
     */
    private DisposableSingleObserver<List<String>> getGamesObserver(){
        return new DisposableSingleObserver<List<String>>() {
            @Override
            public void onSuccess(List<String> value) {
                games_names = value;
                setDrawerItemsUp();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        };
    }
}