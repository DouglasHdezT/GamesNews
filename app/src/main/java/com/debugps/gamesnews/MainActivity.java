package com.debugps.gamesnews;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.debugps.gamesnews.api.data.TokenAcceso;
import com.debugps.gamesnews.login.LoginActivity;

/**
 * Clase encargada del flujo principal, tanto logico como grafico, de toda la Aplicacion.
 */
public class MainActivity extends AppCompatActivity {

    private TokenAcceso token;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    /**
     * Metodo encargado de la creacion y asignacion de valores a los componentes, logicos y graficos, en la actividad Main.
     * @param savedInstanceState Bundle para la recuperacion de datos a la hora de cambiar de configuraciones.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        token = getIntent().getParcelableExtra(LoginActivity.SHARED_TOKEN_KEY);

        if(token ==  null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        setResourcesUp();
    }

    /**
     * Metodo implementado para el maejo de la logica en la actividad Main.
     */
    @Override
    protected void onResume() {
        super.onResume();

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


}
