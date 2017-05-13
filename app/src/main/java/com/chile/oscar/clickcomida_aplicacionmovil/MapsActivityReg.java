package com.chile.oscar.clickcomida_aplicacionmovil;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Coordenadas;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivityReg extends FragmentActivity implements OnMapReadyCallback, LocationListener, View.OnClickListener
{
    private static final int LOCATION_REQUEST_CODE = 1;
    GoogleMap mMap;
    Marker prevMarker;
    LatLng location;
    Location mLocation;
    LocationManager locationManager;
    LatLng miPosicion;
    Button botonTomarCordenadas;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_reg);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        botonTomarCordenadas = (Button)findViewById(R.id.btnFijar);
        botonTomarCordenadas.setOnClickListener(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        // Controles UI
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            mMap.setMyLocationEnabled(true);
        }
        else
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                // Mostrar diálogo explicativo
            }
            else
            {
                // Solicitar permiso
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
        mMap.getUiSettings().setZoomControlsEnabled(true);


        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //mLocation = getMyLocation();
        //LatLng miPosicion = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(miPosicion));
        //Toast.makeText(getApplicationContext(), "Hemos encontrado tu posición, toca la pantalla para que a futuro el repartidor vaya a esa dirección", Toast.LENGTH_LONG).show();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latLng)
            {
                mMap.clear();
                //LatLng posicionLocal = new LatLng(latLng.latitude, latLng.longitude);
                mMap.addMarker(new MarkerOptions().position(latLng).title("Marca"));

                Coordenadas.latitud = latLng.latitude;
                Coordenadas.longitud = latLng.longitude;
            }
        });
        //Toast.makeText(getApplicationContext(), "Mi latitud:" + lat, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == LOCATION_REQUEST_CODE)
        {
            // ¿Permisos asignados?
            if (permissions.length > 0 && permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Error de permisos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Location getMyLocation()
    {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (myLocation == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            String provider = lm.getBestProvider(criteria, true);
            myLocation = lm.getLastKnownLocation(provider);
        }
        return myLocation;
    }


    @Override
    public void onLocationChanged(Location location)
    {
        //CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(miPosicion, 16);
        //mMap.animateCamera(miUbicacion);
        //Toast.makeText(getApplicationContext(), "Se ha movido", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnFijar:
                finish();
                break;
        }
    }
}
