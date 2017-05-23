package com.chile.oscar.clickcomida_aplicacionmovil;

import android.Manifest;
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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Codificacion;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Coordenadas;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.MetodosCreados;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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

public class MapaInicio extends Fragment implements OnMapReadyCallback
{

    private static final int LOCATION_REQUEST_CODE = 1;

    private GoogleMap mMap;
    Marker prevMarker;
    LatLng location;
    Location mLocation;
    LocationManager locationManager;
    LatLng miPosicion;

    int[] idStore;
    String[] street, number, open_hour, close_hour, lunch_hour, lunch_after_hour, start_day, end_day, user_id;
    ArrayList<String> nameStore, desStore;
    ArrayList<Double> latitude, longitude;
    int pos;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = null;
        SupportMapFragment mapFragment = null;
        try
        {
             view = inflater.inflate(R.layout.activity_mapa_inicio, container, false);
             mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapaFragmento);
        }
        catch (Exception ex)
        {
            ViewGroup viewGroup = null;
            viewGroup.removeView(view);
            getFragmentManager().beginTransaction().remove(mapFragment).commit();
            mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapaFragmento);
        }

        mapFragment.getMapAsync(this);

        JSONObject object = new JSONObject();
        try
        {
            object.put("nada", null);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        new cargarTiendasCoordenadas().execute(getResources().getString(R.string.direccion_web) + "/Controlador/consultarCoor.php", object.toString());
        return view;
    }
    public static StoreFragmentSelected newInstance(String... params)
    {
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
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
        {
            @Override
            public void onInfoWindowClick(Marker marker)
            {
                if (marker.isFlat())
                {
                    pos = nameStore.indexOf(marker.getTitle());
                    JSONObject object = new JSONObject();
                    try
                    {
                        object.put("name_store", nameStore.get(pos));
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }



                    new cargarImagen().execute(getResources().getString(R.string.direccion_web) + "/Controlador/cargar_una_imagen_tienda.php", object.toString());

                    //Toast.makeText(getContext(), "Es tu tienda", Toast.LENGTH_SHORT).show();
                }
                //int pos = nameStore.indexOf(marker.getTitle());
                //Toast.makeText(getContext(), "Posicion de la tienda en el arreglo: " +pos, Toast.LENGTH_SHORT).show();
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                //Double dis = new Coordenadas().CalcularDistancias(mLocation.getLatitude(), mLocation.getLongitude(), marker.getPosition().latitude, marker.getPosition().longitude);
                //Toast.makeText(getContext(), "Distancia:" +dis , Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        try
        {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            mLocation = getMyLocation();
            LatLng miPosicion = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(miPosicion));

            //CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(miPosicion, 16);
            //mMap.animateCamera(miUbicacion);
        }
        catch (Exception x)
        {
            String m = x.getMessage();
        }
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        // Controles UI
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            mMap.setMyLocationEnabled(true);
        }
        else
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION))
            {
                // Mostrar diálogo explicativo
            }
            else
            {
                // Solicitar permiso
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == LOCATION_REQUEST_CODE) {
            // ¿Permisos asignados?
            if (permissions.length > 0 && permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
            }
            else
            {
                Toast.makeText(getContext(), "Error de permisos", Toast.LENGTH_SHORT).show();
            }
        }
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

    /*@Override
    public void onLocationChanged(Location location)
    {
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(miPosicion, 16);
        mMap.animateCamera(miUbicacion);
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

    }*/

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

                    idStore = new int[jsonArray.length()];
                    street = new String[jsonArray.length()];
                    number = new String[jsonArray.length()];
                    open_hour = new String[jsonArray.length()];
                    close_hour = new String[jsonArray.length()];
                    lunch_hour = new String[jsonArray.length()];
                    lunch_after_hour = new String[jsonArray.length()];
                    start_day = new String[jsonArray.length()];
                    end_day = new String[jsonArray.length()];
                    //latitude = new Double[jsonArray.length()];
                    //longitude = new Double[jsonArray.length()];

                    nameStore = new ArrayList<String>();
                    desStore = new ArrayList<String>();
                    latitude = new ArrayList<Double>();
                    longitude = new ArrayList<Double>();

                    user_id = new String[jsonArray.length()];

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        jsonObject = jsonArray.getJSONObject(i);
                        idStore[i] = Integer.parseInt(jsonObject.getString("id"));
                        nameStore.add(jsonObject.getString("name"));
                        desStore.add(jsonObject.getString("description"));
                        street[i] = jsonObject.getString("street");
                        number[i] = jsonObject.getString("number");
                        open_hour[i] = jsonObject.getString("open_hour");
                        close_hour[i] = jsonObject.getString("close_hour");
                        lunch_hour[i] = jsonObject.getString("lunch_hour");
                        lunch_after_hour[i] = jsonObject.getString("lunch_after_hour");
                        start_day[i] = jsonObject.getString("start_day");
                        end_day[i] = jsonObject.getString("end_day");
                        latitude.add(Double.parseDouble(jsonObject.getString("latitude")));
                        longitude.add(Double.parseDouble(jsonObject.getString("longitude")));
                        user_id[i] = jsonObject.getString("user_id");
                    }
                    for (int i=0; i<nameStore.size(); i++)
                    {
                        LatLng coordenadas = new LatLng(latitude.get(i), longitude.get(i));
                        if (user_id[i].equals(Coordenadas.id))
                        {
                            mMap.addMarker(new MarkerOptions().position(coordenadas).title(nameStore.get(i)).flat(true).snippet("Pincha este cuadro para mas información")).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        }
                        else
                        {
                            mMap.addMarker(new MarkerOptions().position(coordenadas).title(nameStore.get(i)).snippet("Pincha este cuadro para mas información"));
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
                JSONObject object = new JSONObject(s);
                FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.content_general, newInstance(object.getString("Imagen"), nameStore.get(pos), desStore.get(pos), street[pos], number[pos], start_day[pos], end_day[pos], open_hour[pos], close_hour[pos], lunch_hour[pos], lunch_after_hour[pos], String.valueOf(idStore[pos])));
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);
                trans.commit();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
