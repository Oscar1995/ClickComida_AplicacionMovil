package com.chile.oscar.clickcomida_aplicacionmovil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Validadores;

public class Inicio_Repartidor extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MapaInicioRepartidor.OnFragmentInteractionListener, PedidoPendientesRepartidor.OnFragmentInteractionListener
{
    String idUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio__repartidor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        Fragment myFrag = new MapaInicioRepartidor();
        Bundle args = new Bundle();
        args.putString("ID_USUARIO", idUsuario);
        myFrag.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.contentRepartidor, myFrag).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inicio__repartidor, menu);
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        boolean fragmentoSeleccionado = false;
        if (id == R.id.nav_start)
        {
            fragment = new MapaInicioRepartidor();
            fragmentoSeleccionado = true;
            getSupportActionBar().setTitle(getResources().getString(R.string.Inicio));
        }
        else if (id == R.id.nav_pedidos)
        {
            fragment = new PedidoPendientesRepartidor();
            Bundle args = new Bundle();
            args.putString("user_id", idUsuario);
            fragment.setArguments(args);
            fragmentoSeleccionado = true;
            getSupportActionBar().setTitle(getResources().getString(R.string.pedidos_péndientes));
        }
        else if (id == R.id.nav_share)
        {

        }
        else if (id == R.id.nav_end)
        {

            SharedPreferences sharedpreferences = getSharedPreferences("datos_del_usuario", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();

            MapaInicioRepartidor.ARG_ID = 1;

            Intent intent = new Intent (Inicio_Repartidor.this, Login.class);
            startActivity(intent);

            this.finish();
        }
        if (fragmentoSeleccionado == true)
        {
            if (new Validadores().isNetDisponible(getApplicationContext()))
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.contentRepartidor, fragment).commit();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Debes estar conectado a una red.", Toast.LENGTH_SHORT).show();
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }
}
