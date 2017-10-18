package com.chile.oscar.clickcomida_aplicacionmovil;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
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

import com.chile.oscar.clickcomida_aplicacionmovil.Actividades.BusquedaAvanzadaActivity;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.LastDate;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Usuarios;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Validadores;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Inicio_Usuario extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, fragmentTienda.OnFragmentInteractionListener, StoreFragment.OnFragmentInteractionListener, StoreFragmentSelected.OnFragmentInteractionListener, MostrarProductosMios.OnFragmentInteractionListener, StoreOtherUser.OnFragmentInteractionListener, Favorites_stores.OnFragmentInteractionListener, StoreProductsFragment.OnFragmentInteractionListener, ProductsOtherUser.OnFragmentInteractionListener, Details_products.OnFragmentInteractionListener, cart_products.OnFragmentInteractionListener, Tracking.OnFragmentInteractionListener, BusquedaAvanzada.OnFragmentInteractionListener
{
    //TextView vCorreo, vNombre;
    String idUsuario;
    Fragment fragment = null;
    ProgressDialog progress;
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
        LastDate.dataInfo = "Maps";
        getSupportFragmentManager().beginTransaction().replace(R.id.content_general, myFrag, "Maps").commit();

        //Inicia la clase de seguimiento en segundo plano preguntando cada 2 minutos si hay algun pedido, si hay... monitoreara....
        //startService(new Intent(Inicio_Usuario.this, ServiceTracking.class));

    }
    Boolean swOff = false;
    @Override
    public void onBackPressed()
    {
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(LastDate.dataInfo);
        if (currentFragment != null && currentFragment.isVisible())
        {
            if (LastDate.dataInfo.equals("Maps"))
            {
                if (swOff == false)
                {
                    swOff = true;
                    Toast.makeText(getApplicationContext(), "Pincha de nuevo para salir de la aplicación", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    System.exit(1);
                }
            }
            else
            {
                swOff = false;
                Fragment fragment = new MapaInicio();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_general, fragment, "Maps").commit();
                getSupportActionBar().setTitle(getResources().getString(R.string.titulo_inicio));
                LastDate.dataInfo = "Maps";
            }
        }
        else
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

        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_usuario);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            //getFragmentManager().beginTransaction().remove(map).commit();
            super.onBackPressed();
        }*/
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



        if (id == R.id.nav_camera)
        {
            fragment = new MapaInicio();
            getSupportActionBar().setTitle(getResources().getString(R.string.titulo_inicio));
            TransactionReplace("Maps", fragment);
        }
        else if (id == R.id.nav_search)
        {
            fragment = new BusquedaAvanzada();
            getSupportActionBar().setTitle(getResources().getString(R.string.busqueda_avanzada));
            TransactionReplace("Busqueda", fragment);
            //startActivity(new Intent(this, BusquedaAvanzadaActivity.class));
        }
        else if (id == R.id.nav_gallery)
        {
            progress = new ProgressDialog(this);
            progress.setMessage("Verificando...");
            progress.setCanceledOnTouchOutside(false);
            progress.show();

            JSONObject jsonObject = new JSONObject();
            try
            {
                jsonObject.put("id", idUsuario);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/contarTiendas.php", jsonObject.toString());
        }
        else if (id == R.id.nav_slideshow)
        {
            fragment = new MisDatos();
            Bundle args = new Bundle();
            args.putString("IdUser", idUsuario);
            fragment.setArguments(args);
            getSupportActionBar().setTitle(getResources().getString(R.string.titulo_mis_datos));
            TransactionReplace("Datos", fragment);
        }
        else if (id == R.id.nav_manage)
        {
            fragment = new StoreFragment();
            Bundle args = new Bundle();
            args.putString("user_id", idUsuario);
            fragment.setArguments(args);
            getSupportActionBar().setTitle(getResources().getString(R.string.titulo_mi_tienda));
            TransactionReplace("Store", fragment);
        }
        else if (id == R.id.nav_favorite)
        {
            fragment = new Favorites_stores();
            Bundle args = new Bundle();
            args.putString("user_id", idUsuario);
            fragment.setArguments(args);;
            getSupportActionBar().setTitle(getResources().getString(R.string.titulo_mi_tienda));
            TransactionReplace("Favoritos", fragment);
        }
        else if (id == R.id.nav_cart)
        {
            fragment = new cart_products();
            Bundle args = new Bundle();
            //args.putString("user_id", idUsuario);
            //fragment.setArguments(args);
            getSupportActionBar().setTitle(getResources().getString(R.string.mi_carro));
            TransactionReplace("Carro", fragment);
        }
        else if (id == R.id.nav_track)
        {
            fragment = new Tracking();
            //Bundle args = new Bundle();
            //args.putString("user_id", idUsuario);
            //fragment.setArguments(args);
            getSupportActionBar().setTitle(getResources().getString(R.string.historial));
            TransactionReplace("Tracking", fragment);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_usuario);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void TransactionReplace (String TAG, Fragment fragment)
    {
        if (new Validadores().isNetDisponible(getApplicationContext()))
        {
            LastDate.dataInfo = TAG;
            getSupportFragmentManager().beginTransaction().replace(R.id.content_general, fragment, TAG).commit();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Debes estar conectado a una red.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }
    public class EjecutarSentencia extends AsyncTask<String, Void, String>
    {
        @Override
        public String doInBackground(String... params)
        {
            HttpURLConnection conn = null;
            try
            {
                StringBuffer response = null;
                URL url = new URL(params[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                OutputStream out = new BufferedOutputStream(conn.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                writer.write(params[1].toString());
                writer.close();
                out.close();
                int responseCode = conn.getResponseCode();
                System.out.println("responseCode" + responseCode);

                switch (responseCode)
                {
                    case 200:
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null)
                        {
                            response.append(inputLine);
                        }
                        in.close();
                        return response.toString();
                }
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
            finally
            {
                if (conn != null)
                {
                    try
                    {
                        conn.disconnect();
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s)
        {
            try
            {
                JSONObject jsonObject = new JSONObject(s);
                int cStore = jsonObject.getInt("count(user_id)");
                progress.dismiss();
                if (cStore >= 3)
                {
                    Toast.makeText(getApplicationContext(), "No puedes crear mas tiendas, el limite maximo es solo hasta 3 tiendas.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    fragment = new fragmentTienda();
                    Bundle args = new Bundle();
                    args.putString("ID_USUARIO", idUsuario);
                    fragment.setArguments(args);
                    getSupportActionBar().setTitle(getResources().getString(R.string.titulo_crear_tienda));
                    TransactionReplace("Tienda", fragment);
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
    }
}
