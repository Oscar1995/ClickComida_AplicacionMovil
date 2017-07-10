package com.chile.oscar.clickcomida_aplicacionmovil.Actividades;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chile.oscar.clickcomida_aplicacionmovil.BusquedaAvanzada;
import com.chile.oscar.clickcomida_aplicacionmovil.Details_products;
import com.chile.oscar.clickcomida_aplicacionmovil.R;

public class BusquedaAvanzadaActivity extends AppCompatActivity implements BusquedaAvanzada.OnFragmentInteractionListener, Details_products.OnFragmentInteractionListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Fragment fragment = new BusquedaAvanzada();
        getSupportActionBar().setTitle(getResources().getString(R.string.titulo_inicio));
        getSupportFragmentManager().beginTransaction().replace(R.id.flAdvanced, fragment).commit();
        setContentView(R.layout.activity_busqueda_avanzada);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return super.onSupportNavigateUp();
    }
}
