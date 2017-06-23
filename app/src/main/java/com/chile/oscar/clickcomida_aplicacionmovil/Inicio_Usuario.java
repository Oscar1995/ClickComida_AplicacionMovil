package com.chile.oscar.clickcomida_aplicacionmovil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Usuarios;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Validadores;

public class Inicio_Usuario extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, fragmentTienda.OnFragmentInteractionListener, StoreFragment.OnFragmentInteractionListener, StoreFragmentSelected.OnFragmentInteractionListener, MostrarProductosMios.OnFragmentInteractionListener, StoreOtherUser.OnFragmentInteractionListener, Favorites_stores.OnFragmentInteractionListener, StoreProductsFragment.OnFragmentInteractionListener, ProductsOtherUser.OnFragmentInteractionListener, Details_products.OnFragmentInteractionListener, cart_products.OnFragmentInteractionListener, Tracking.OnFragmentInteractionListener, BusquedaAvanzada.OnFragmentInteractionListener
{
    //TextView vCorreo, vNombre;
    String idUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio__usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_usuario);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View hView = navigationView.getHeaderView(0);
        TextView vCorreo = (TextView)hView.findViewById(R.id.tvCorreoUsuarioMenu);
        TextView vNombre = (TextView)hView.findViewById(R.id.tvNombreUsuarioMenu);

        idUsuario = getIntent().getStringExtra("id_user_login");
        String correoUsuario = getIntent().getStringExtra("correo_usuario");
        String nombreUsuario = getIntent().getStringExtra("nombre_usuario");

        vCorreo.setText(correoUsuario);
        vNombre.setText(nombreUsuario);

        //Este cambia el valor del titulo del navigation_drawer
        getSupportActionBar().setTitle(getResources().getString(R.string.titulo_inicio));

        Fragment myFrag = new MapaInicio();
        Bundle args = new Bundle();
        args.putString("ID_USUARIO", idUsuario);
        myFrag.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_general, myFrag).commit();


    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_usuario);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            //getFragmentManager().beginTransaction().remove(map).commit();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inicio__usuario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        boolean fragmentoSeleccionado = false;

        if (id == R.id.nav_camera)
        {
            fragment = new MapaInicio();
            fragmentoSeleccionado = true;
            getSupportActionBar().setTitle(getResources().getString(R.string.titulo_inicio));
        }
        else if (id == R.id.nav_search)
        {
            fragment = new BusquedaAvanzada();
            fragmentoSeleccionado = true;
            getSupportActionBar().setTitle(getResources().getString(R.string.titulo_inicio));
        }
        else if (id == R.id.nav_gallery)
        {
            fragment = new fragmentTienda();
            Bundle args = new Bundle();
            args.putString("ID_USUARIO", idUsuario);
            fragment.setArguments(args);
            fragmentoSeleccionado = true;
            getSupportActionBar().setTitle(getResources().getString(R.string.titulo_crear_tienda));
        }
        else if (id == R.id.nav_slideshow)
        {
            fragment = new MisDatos();
            Bundle args = new Bundle();
            args.putString("IdUser", idUsuario);
            fragment.setArguments(args);
            fragmentoSeleccionado = true;
            getSupportActionBar().setTitle(getResources().getString(R.string.titulo_mis_datos));
        }
        else if (id == R.id.nav_manage)
        {
            fragment = new StoreFragment();
            Bundle args = new Bundle();
            args.putString("user_id", idUsuario);
            fragment.setArguments(args);
            fragmentoSeleccionado = true;
            getSupportActionBar().setTitle(getResources().getString(R.string.titulo_mi_tienda));
        }
        else if (id == R.id.nav_favorite)
        {
            fragment = new Favorites_stores();
            Bundle args = new Bundle();
            args.putString("user_id", idUsuario);
            fragment.setArguments(args);
            fragmentoSeleccionado = true;
            getSupportActionBar().setTitle(getResources().getString(R.string.titulo_mi_tienda));
        }
        else if (id == R.id.nav_cart)
        {
            fragment = new cart_products();
            Bundle args = new Bundle();
            //args.putString("user_id", idUsuario);
            //fragment.setArguments(args);
            fragmentoSeleccionado = true;
            getSupportActionBar().setTitle(getResources().getString(R.string.mi_carro));
        }
        else if (id == R.id.nav_track)
        {
            fragment = new Tracking();
            //Bundle args = new Bundle();
            //args.putString("user_id", idUsuario);
            //fragment.setArguments(args);
            fragmentoSeleccionado = true;
            getSupportActionBar().setTitle(getResources().getString(R.string.tacking));
        }
        else if (id == R.id.nav_work)
        {
            /*fragment = new WorkFragment();
            fragmentoSeleccionado = true;
            getSupportActionBar().setTitle(getResources().getString(R.string.buscar_trabajo));*/
            Intent intent = new Intent(Inicio_Usuario.this, WorksActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_send)
        {
            SharedPreferences sharedpreferences = getSharedPreferences("datos_del_usuario", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();

            SharedPreferences sharedpreferencesCarro =  getSharedPreferences("carro", Context.MODE_PRIVATE);
            SharedPreferences.Editor editorCarro = sharedpreferencesCarro.edit();
            editorCarro.clear();
            editorCarro.commit();

            Intent intent = new Intent (Inicio_Usuario.this, Login.class);
            startActivity(intent);
            this.finish();
        }
        if (fragmentoSeleccionado == true)
        {
            if (new Validadores().isNetDisponible(getApplicationContext()))
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.content_general, fragment).commit();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Debes estar conectado a una red.", Toast.LENGTH_SHORT).show();
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_usuario);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }
}
