package com.debugps.gamesnews;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.debugps.gamesnews.adapters.MainAdapter;
import com.debugps.gamesnews.api.controler.GamesNewsApi;
import com.debugps.gamesnews.api.data.TokenAcceso;
import com.debugps.gamesnews.dialogs.NewsDialog;
import com.debugps.gamesnews.dialogs.PlayerDialog;
import com.debugps.gamesnews.fragment.NewsMainFragment;
import com.debugps.gamesnews.fragment.NewsPerGameFragment;
import com.debugps.gamesnews.fragment.RecyclerViewFragment;
import com.debugps.gamesnews.interfaces.MainTools;
import com.debugps.gamesnews.login.LoginActivity;
import com.debugps.gamesnews.roomTools.POJO.Category;
import com.debugps.gamesnews.roomTools.POJO.FavoriteList;
import com.debugps.gamesnews.roomTools.POJO.New;
import com.debugps.gamesnews.roomTools.POJO.Player;
import com.debugps.gamesnews.roomTools.POJO.User;
import com.debugps.gamesnews.roomTools.viewModels.CategoryViewModel;
import com.debugps.gamesnews.roomTools.viewModels.FavoriteListViewModel;
import com.debugps.gamesnews.roomTools.viewModels.NewViewModel;
import com.debugps.gamesnews.roomTools.viewModels.PlayerViewModel;
import com.debugps.gamesnews.roomTools.viewModels.UserViewModel;
import com.debugps.gamesnews.tools.CustomGridLayoutManager;
import com.debugps.gamesnews.tools.RefreshAsyncTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import io.reactivex.Scheduler;
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
public class MainActivity extends AppCompatActivity implements MainTools {

    public static String token_var = "";
    private final static int ID_ITEM_MENU_GAMES = 101010;
    private static final Random rn = new Random();

    public static int REFRESH_DONE_NEWS;
    public static int REFRESH_DONE_CATEGORIES;
    public static int REFRESH_DONE_PLAYERS;
    public static int IN_REFRESH = 0;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    protected NavigationView navigationView;

    private MainAdapter mainAdapter;
    private MainAdapter favoritesAdapter;
    private List<New> newList_main;
    private ArrayList<String> games_names = new ArrayList<>();
    private ArrayList<String> styled_names;

    private User mainUser;
    private GamesNewsApi gamesNewsApi;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    private NewViewModel newViewModel;
    private CategoryViewModel categoryViewModel;
    private PlayerViewModel playerViewModel;
    private FavoriteListViewModel favoriteListViewModel;
    private UserViewModel userViewModel;

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

        Log.d("token", token_var);

        gamesNewsApi = createGamesNewApi();

        newViewModel = ViewModelProviders.of(this).get(NewViewModel.class);
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel.class);
        favoriteListViewModel = ViewModelProviders.of(this).get(FavoriteListViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        if(isNetworkAvailable()) {
            newViewModel.refreshNews();
            userViewModel.refreshUsers();
            categoryViewModel.refreshCategories();
            playerViewModel.refreshPlayers();
            //favoriteListViewModel.refreshFavorites();
            Log.d("NETWORK", "Con inter");
        }else{
            Toast.makeText(this,"Sin inter", Toast.LENGTH_SHORT).show();
        }



        toolbar.setTitle(R.string.main_menu_title);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_activity_frame_layout, RecyclerViewFragment.newInstance(mainAdapter, 1));
        ft.commit();
    }

    /**
     * Metodo implementado para el manejo de la logica en la actividad Main.
     */
    @Override
    protected void onResume() {
        super.onResume();

        userViewModel.getUser_list().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                if (users != null && users.size()>0) {
                    mainUser = users.get(0);
                }
            }
        });

        newViewModel.getAllNews().observe(this, new Observer<List<New>>() {
            @Override
            public void onChanged(@Nullable List<New> news) {
                mainAdapter.setNewList(news);
                newList_main = news;
            }
        });

        favoriteListViewModel.getFavorite_list().observe(this, new Observer<List<FavoriteList>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteList> favoriteLists) {
                if (favoriteLists != null) {
                    for(int i=0; i < favoriteLists.size(); i++){
                        Log.d("for", favoriteLists.get(i).getId());
                        newViewModel.setFav(favoriteLists.get(i).getId());
                    }
                }
            }
        });

        newViewModel.getFavNews().observe(this, new Observer<List<New>>() {
            @Override
            public void onChanged(@Nullable List<New> news) {
                favoritesAdapter.setNewList(news);
            }
        });

        categoryViewModel.getCategoriesList().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                //Log.d("String ", categories.size()+"");
                if (categories != null && categories.size() != 0) {
                    games_names.clear();
                    for (int i = 0; i < categories.size(); i++) {
                        games_names.add(categories.get(i).getCategory());
                    }
                    setDrawerItemsUp();
                }
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
        mainAdapter = new MainAdapter((MainTools) this);
        favoritesAdapter = new MainAdapter((MainTools) this);
    }

    /**
     * Metodo utilizado para encontrar y asignar valores a los componentes graficos de la Actividad.
     */
    private void setResourcesUp(){
        drawerLayout =  findViewById(R.id.main_drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        navigationView =  findViewById(R.id.main_navigation_view);

        toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle(R.string.main_menu_title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
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
        subMenu.setGroupCheckable(R.id.group_games, true, true);
        for(int i = 0; i < games_names.size(); i++) {
            subMenu.add(R.id.group_games, ID_ITEM_MENU_GAMES + i, i, styled_names.get(i))
                    .setCheckable(true).setIcon(R.drawable.ic_game);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.isChecked()){
                    return true;
                }
                drawerLayout.closeDrawers();
                switch (item.getItemId()){
                    case R.id.drawer_menu_news:
                        setUpMainFragment(RecyclerViewFragment.newInstance(mainAdapter, 1));
                        toolbar.setTitle(R.string.main_menu_title);
                        //Log.d("Name", "News");
                        break;
                    case R.id.drawer_menu_fav:
                        setUpMainFragment(RecyclerViewFragment.newInstance(favoritesAdapter, 2));
                        toolbar.setTitle(R.string.main_menu_fav);
                        //Log.d("Name", "Fav");
                        break;
                    case R.id.drawer_menu_settings:
                        toolbar.setTitle(R.string.main_menu_settings);
                        //Log.d("Name", "Settings");
                        break;
                    case R.id.drawer_menu_exit:
                        logoutUser();
                        break;
                    default:
                        for(int i=0; i<games_names.size(); i++){
                            if(item.getItemId() == ID_ITEM_MENU_GAMES+i){
                                toolbar.setTitle(styled_names.get(i));
                                setUpMainFragment(NewsPerGameFragment.newInstance(games_names.get(i)));
                                //Log.d("Name", games_names.get(i));
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
    private ArrayList<String> getPreferedNames(ArrayList<String> names){
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
     * Metodo que reemplaza el fragmento pricipal dentro del main activity
     * @param fragment Fragmento a reemplazar.
     */
    private void setUpMainFragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_activity_frame_layout, fragment);
        ft.commit();
    }

    private GamesNewsApi createGamesNewApi(){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        Request.Builder builder = originalRequest.newBuilder()
                                .addHeader("Authorization", "Bearer " + LoginActivity.Token_var);

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
     * Metodo implementado de la interfaz para verificar si existe internet
     * @return Boolenao para verificar si existe internet o no.
     */
    @Override
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) return false;
        NetworkInfo.State network = info.getState();
        return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
    }

    /**
     * Metodo utilizado para actulizar las listas a traves de SwipeRefreshLayout
     * -1 Algun Error al actualizar.
     * 0 No ha actualizado o no esta refrescando
     * 1 Exito
     */
    @Override
    public void refreshAll() {
        playerViewModel.deleteAllPlayers();
        newViewModel.deleteAllNews();
        categoryViewModel.deleteAllCategories();
        favoriteListViewModel.deleteAllFavNews();

        playerViewModel.refreshPlayers();
        newViewModel.refreshNews();
        categoryViewModel.refreshCategories();
        //favoriteListViewModel.refreshFavorites();
    }

    /**
     * Metodo para mostrar el dialogo ce las noticias;
     * @param new_var Noticia a mostrar
     */
    @Override
    public void showNewDialog(New new_var) {
        NewsDialog newsDialog = NewsDialog.newInstace(new_var);
        newsDialog.show(MainActivity.this.getSupportFragmentManager(),"Dialogo");
    }

    /**
     * Metodo para mostrar el dialogo de los jugadores.
     * @param player Player a mostrar
     */
    @Override
    public void showPlayerDialog(Player player) {
       PlayerDialog dialog= PlayerDialog.newInstance(player);
       dialog.show(MainActivity.this.getSupportFragmentManager(), "Dialogo");
    }

    /**
     * Metodo para settear una noticia como favorita.
     * @param id
     */
    @Override
    public void setFavorited(String id) {
        compositeDisposable.add(gamesNewsApi.PostFavToList(mainUser.getId(), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getInsDelObserver()));
        newViewModel.setFav(id);
        favoriteListViewModel.insertFavNew(id);
    }

    /**
     * Metodo para desettear una noticia como favorita
     * @param id
     */
    @Override
    public void unsetFavorited(String id) {
        compositeDisposable.add(gamesNewsApi.deleteFavFromList(mainUser.getId(), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getInsDelObserver()));
        favoriteListViewModel.deleteFavNew(id);
        newViewModel.unsetFav(id);
    }

    /**
     * Metodo para deslogear al usuario
     */
    @Override
    public void logoutUser(){
        playerViewModel.deleteAllPlayers();
        newViewModel.deleteAllNews();
        categoryViewModel.deleteAllCategories();
        favoriteListViewModel.deleteAllFavNews();
        userViewModel.deleteAll();
        MainActivity.this.getApplicationContext()
                .getSharedPreferences("Shared",Context.MODE_PRIVATE)
                .edit().clear().commit();
        MainActivity.this.startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    @Override
    public void resetToken() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.token_rip_message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logoutUser();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void prueba() {
        Log.d("TOOLS", "Si funciona");
    }

    /**
     * Generador de colores al azar entre la paleta 700 Material Design
     * @return Id del color generado
     */
    public static int getColorId(){
        //Random rn2 = new Random();
        int rnNumber = Math.abs((rn.nextInt() % 17)) + 1;
        int idColor=R.color.MaterialDeepPurple900;

        switch (rnNumber){
            case 1:
                idColor = R.color.MaterialBlue900;
                break;
            case 2:
                idColor = R.color.MaterialDeepPurple900;
                break;
            case 3:
                idColor = R.color.MaterialPurple900;
                break;
            case 4:
                idColor = R.color.MaterialBlue900;
                break;
            case 5:
                idColor = R.color.MaterialCyan900;
                break;
            case 6:
                idColor = R.color.MaterialIndigo900;
                break;
            case 7:
                idColor = R.color.MaterialLime900;
                break;
            case 8:
                idColor = R.color.MaterialBrown900;
                break;
            case 9:
                idColor = R.color.MaterialLightBlue900;
                break;
            case 10:
                idColor = R.color.MaterialPink900;
                break;
            case 11:
                idColor = R.color.MaterialTeal900;
                break;
            case 12:
                idColor = R.color.MaterialLightGreen900;
                break;
            case 13:
                idColor = R.color.MaterialLightBlue900;
                break;
            case 14:
                idColor = R.color.MaterialDeepPurple900;
                break;
            case 15:
                idColor = R.color.MaterialCyan900;
                break;
            case 16:
                idColor = R.color.MaterialGrey900;
                break;
            case 17:
                idColor = R.color.MaterialGreen900;
                break;
        }

        return idColor;
    }

    private DisposableSingleObserver<Void> getInsDelObserver(){
        return new DisposableSingleObserver<Void>() {
            @Override
            public void onSuccess(Void value) {
                Log.d("MSM", "Succed");
            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }
}