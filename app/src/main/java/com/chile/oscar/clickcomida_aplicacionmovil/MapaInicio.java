package com.chile.oscar.clickcomida_aplicacionmovil;

import android.Manifest;
import android.app.Activity;

import java.lang.Object;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentContainer;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Codificacion;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Coordenadas;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Mapa;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.MetodosCreados;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Validadores;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
import java.util.ArrayList;
import java.util.List;

public class MapaInicio extends Fragment implements OnMapReadyCallback, LocationSource.OnLocationChangedListener {

    private final int REQUEST_ACCESS_FINE = 0;

    private GoogleMap mMap;
    Marker prevMarker;
    LatLng location;
    Location mLocation;
    LocationManager locationManager;
    LatLng miPosicion;

    String tipoConsulta;
    ProgressDialog progress;

    List<Mapa> getDataMaps;
    List<String> nombreTienda;
    SupportMapFragment mapFragment;

    GoogleMap googlemapsGlobal;
    View view;
    int pos = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            view = inflater.inflate(R.layout.activity_mapa_inicio, null, false);
            mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapaFragmento);
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    //Aqui pregunta primero cuando los permidos no estan activados
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE);
                }
            } else {
                //Päsa aqui cuando los permisos estan activados
                cargarCoordenadas();
                mapFragment.getMapAsync(this);
            }
        } catch (InflateException ex) {

        }

        return view;
    }

    public void cargarCoordenadas() {
        if (new Validadores().isNetDisponible(getContext())) {
            JSONObject object = new JSONObject();
            try {
                object.put("nada", null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new cargarTiendasCoordenadas().execute(getResources().getString(R.string.direccion_web) + "/Controlador/consultarCoor.php", object.toString());
        } else {
            Toast.makeText(getContext(), "Debes estar conectado a una red para ver las tiendas.", Toast.LENGTH_SHORT).show();
        }
    }

    public static StoreFragmentSelected newInstance(String... params) {
        StoreFragmentSelected fragment = new StoreFragmentSelected();
        Bundle args = new Bundle();
        args.putString("imagen_tienda", params[0]);
        args.putString("nombre_tienda", params[1]);
        args.putString("des_tienda", params[2]);
        args.putString("calle_tienda", params[3]);
        args.putString("numero_tienda", params[4]);
        args.putString("start_day", params[5]);
        args.putString("end_day", params[6]);
        args.putString("open_hour", params[7]);
        args.putString("close_hour", params[8]);
        args.putString("lunch_open_hour", params[9]);
        args.putString("lunch_after_hour", params[10]);
        args.putString("tienda_id", params[11]);
        args.putString("latitud", params[12]);
        args.putString("longitud", params[13]);
        fragment.setArguments(args);
        return fragment;
    }

    public static StoreOtherUser otherStore(String... params) {
        StoreOtherUser fragment = new StoreOtherUser();
        Bundle args = new Bundle();
        args.putString("imagen_tienda", params[0]);
        args.putString("nombre_tienda", params[1]);
        args.putString("des_tienda", params[2]);
        args.putString("calle_tienda", params[3]);
        args.putString("numero_tienda", params[4]);
        args.putString("start_day", params[5]);
        args.putString("end_day", params[6]);
        args.putString("open_hour", params[7]);
        args.putString("close_hour", params[8]);
        args.putString("lunch_open_hour", params[9]);
        args.putString("lunch_after_hour", params[10]);
        args.putString("tienda_id", params[11]);
        args.putString("latitud", params[12]);
        args.putString("longitud", params[13]);
        args.putString("user_id", params[14]);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googlemapsGlobal = googleMap;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googlemapsGlobal.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        mMap = googleMap;

        if (getMyLocation() != null)
        {

            Location location = getMyLocation();
            LatLng latLngLocal = new LatLng(location.getLatitude(), location.getLongitude());
            float zoomLevel = 14.0f; //This goes up to 21
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngLocal, zoomLevel));
        }
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
        {
            @Override
            public void onInfoWindowClick(Marker marker)
            {
                if (marker.isFlat())
                {
                    pos = nombreTienda.indexOf(marker.getTitle());

                    JSONObject object = new JSONObject();
                    try
                    {
                        object.put("name_store", getDataMaps.get(pos).getNombre());
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    tipoConsulta = "Mi Tienda";
                    
                    progress = new ProgressDialog(getContext());
                    progress.setMessage("Cargando información acerca de tu tienda "+ "\"" + marker.getTitle() + "\"");
                    progress.setCanceledOnTouchOutside(false);
                    progress.show();

                    new cargarImagen().execute(getResources().getString(R.string.direccion_web) + "/Controlador/cargar_una_imagen_tienda.php", object.toString());

                    //Toast.makeText(getContext(), "Es tu tienda", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    pos = nombreTienda.indexOf(marker.getTitle());
                    JSONObject object = new JSONObject();
                    try
                    {
                        object.put("name_store", getDataMaps.get(pos).getNombre());
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    tipoConsulta = "Otra Tienda";

                    progress = new ProgressDialog(getContext());
                    progress.setMessage("Cargando información acerca de la tienda "+ "\"" + marker.getTitle() + "\"");
                    progress.setCanceledOnTouchOutside(false);
                    progress.show();

                    new cargarImagen().execute(getResources().getString(R.string.direccion_web) + "/Controlador/cargar_una_imagen_tienda.php", object.toString());

                }
                //int pos = nameStore.indexOf(marker.getTitle());
                //Toast.makeText(getContext(), "Posicion de la tienda en el arreglo: " +pos, Toast.LENGTH_SHORT).show();
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //Double dis = new Coordenadas().CalcularDistancias(mLocation.getLatitude(), mLocation.getLongitude(), marker.getPosition().latitude, marker.getPosition().longitude);
                //Toast.makeText(getContext(), "Distancia:" +dis , Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            mLocation = getMyLocation();
            LatLng miPosicion = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(miPosicion));

            //CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(miPosicion, 16);
            //mMap.animateCamera(miUbicacion);
        } catch (Exception x) {
            String m = x.getMessage();
        }
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        // Controles UI

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ACCESS_FINE:
                {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mapFragment.getMapAsync(this);
                    //googlemapsGlobal.getUiSettings().setZoomControlsEnabled(true);
                    //googlemapsGlobal.setMyLocationEnabled(true);
                    cargarCoordenadas();
                    //Toast.makeText(getContext(), "Aceptado", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(getContext(), "Debes otorgar permisos de ubicacion.", Toast.LENGTH_SHORT).show();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            Coordenadas.id = getArguments().getString("ID_USUARIO");
        }
    }
    private Location getMyLocation()
    {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        Toast.makeText(MapaInicio.this.getContext(), "Se ha movido", Toast.LENGTH_SHORT).show();
    }

    public class cargarTiendasCoordenadas extends AsyncTask<String, Void, String>
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

                if (s != null)
                {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject = null;
                    getDataMaps = new ArrayList<>();
                    nombreTienda = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        Mapa mapaClass = new Mapa();
                        jsonObject = jsonArray.getJSONObject(i);

                        mapaClass.setId(Integer.parseInt(jsonObject.getString("id")));
                        mapaClass.setNombre(jsonObject.getString("name"));
                        mapaClass.setDescripcion(jsonObject.getString("description"));
                        mapaClass.setCalle(jsonObject.getString("street"));
                        mapaClass.setNumero(jsonObject.getString("number"));
                        mapaClass.setOpen_Hour(jsonObject.getString("open_hour"));
                        mapaClass.setClose_Hour(jsonObject.getString("close_hour"));
                        mapaClass.setLunch_Hour(jsonObject.getString("lunch_hour"));
                        mapaClass.setLunch_After_Hour(jsonObject.getString("lunch_after_hour"));
                        mapaClass.setStar_Day(jsonObject.getString("start_day"));
                        mapaClass.setEnd_Day(jsonObject.getString("end_day"));
                        mapaClass.setLatitude(Double.parseDouble(jsonObject.getString("latitude")));
                        mapaClass.setLongitude(Double.parseDouble(jsonObject.getString("longitude")));
                        mapaClass.setUser_id(Integer.parseInt(jsonObject.getString("user_id")));
                        nombreTienda.add(mapaClass.getNombre());
                        getDataMaps.add(mapaClass);

                    }
                    for (int i=0; i<getDataMaps.size(); i++)
                    {
                        LatLng coordenadas = new LatLng(getDataMaps.get(i).getLatitude(), getDataMaps.get(i).getLongitude());
                        if (getDataMaps.get(i).getUser_id() == Integer.parseInt(Coordenadas.id))
                        {
                            mMap.addMarker(new MarkerOptions().position(coordenadas).title(getDataMaps.get(i).getNombre()).flat(true).snippet("Pincha este cuadro para mas información")).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        }
                        else
                        {

                            mMap.addMarker(new MarkerOptions().position(coordenadas).title(getDataMaps.get(i).getNombre()).snippet("Pincha este cuadro para mas información"));
                        }

                    }
                }
            }
            catch (JSONException e)
            {
                String x = e.getMessage();
            }
        }
    }
    public class cargarImagen extends AsyncTask<String, Void, String>
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
                out.write(params[1].toString().getBytes());
                /*BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                writer.write(params[1].toString());
                writer.close();*/
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
                JSONObject object = new JSONObject(s);

                if (tipoConsulta.equals("Mi Tienda"))
                {
                    if (object != null)
                    {
                        progress.dismiss();
                        FragmentTransaction trans = getFragmentManager().beginTransaction();
                        trans.replace(R.id.content_general, newInstance(object.getString("Imagen"), getDataMaps.get(pos).getNombre(), getDataMaps.get(pos).getDescripcion(), getDataMaps.get(pos).getCalle(), getDataMaps.get(pos).getNumero(), getDataMaps.get(pos).getStar_Day(), getDataMaps.get(pos).getEnd_Day(), getDataMaps.get(pos).getOpen_Hour(), getDataMaps.get(pos).getClose_Hour(), getDataMaps.get(pos).getLunch_Hour(), getDataMaps.get(pos).getLunch_After_Hour(), String.valueOf(getDataMaps.get(pos).getId()), getDataMaps.get(pos).getLatitude() + "", getDataMaps.get(pos).getLongitude() + ""));
                        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        trans.addToBackStack(null);
                        trans.commit();

                    }
                }
                else if (tipoConsulta.equals("Otra Tienda"))
                {
                    progress.dismiss();
                    FragmentTransaction trans = getFragmentManager().beginTransaction();
                    trans.replace(R.id.content_general, otherStore(object.getString("Imagen"), getDataMaps.get(pos).getNombre(), getDataMaps.get(pos).getDescripcion(), getDataMaps.get(pos).getCalle(), getDataMaps.get(pos).getNumero(), getDataMaps.get(pos).getStar_Day(), getDataMaps.get(pos).getEnd_Day(), getDataMaps.get(pos).getOpen_Hour(), getDataMaps.get(pos).getClose_Hour(), getDataMaps.get(pos).getLunch_Hour(), getDataMaps.get(pos).getLunch_After_Hour(), String.valueOf(getDataMaps.get(pos).getId()),getDataMaps.get(pos).getLatitude() + "", getDataMaps.get(pos).getLongitude() + "", Coordenadas.id));
                    trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    trans.addToBackStack(null);
                    trans.commit();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
