package com.chile.oscar.clickcomida_aplicacionmovil;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Coordenadas;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.MapaRepartidorCoordenadas;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.MetodosCreados;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.PedidosRepartidor;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Validadores;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
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
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapaInicioRepartidor.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapaInicioRepartidor#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapaInicioRepartidor extends Fragment implements OnMapReadyCallback
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static int ARG_ID = 0;
    private final int REQUEST_ACCESS_FINE = 0;
    Timer timer = new Timer();

    // TODO: Rename and change types of parameters

    private String mParamId;
    private GoogleMap mMap;
    List<MapaRepartidorCoordenadas> mapaRepartidorCoordenadasList;

    View view;

    private OnFragmentInteractionListener mListener;

    public MapaInicioRepartidor() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapaInicioRepartidor.
     */
    // TODO: Rename and change types and number of parameters
    public static MapaInicioRepartidor newInstance(String param1, String param2) {
        MapaInicioRepartidor fragment = new MapaInicioRepartidor();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            //mParamId = getArguments().getString("ID_USUARIO");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try
        {
            SharedPreferences sharedpreferences = getActivity().getSharedPreferences("datos_del_usuario", Context.MODE_PRIVATE);
            mParamId = sharedpreferences.getString("id_usuario_shared", "");
            view = inflater.inflate(R.layout.fragment_mapa_inicio_repartidor, null, false);
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapaFragmentoRepartidor);

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }
            mapFragment.getMapAsync(this);
        } catch (InflateException ex)
        {

        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap)
    {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
            {

            }
            else
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE);
            }
        }
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
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (mMap != null)
        {
            timer.scheduleAtFixedRate(new TimerTask()
            {
                @Override
                public void run()
                {
                    try
                    {
                        if (getActivity() != null)
                        {
                            Log.w("Timer:", "Iniciado");
                            Location location = getMyLocation();
                            if (location != null)
                            {
                                JSONObject object = new JSONObject();
                                object.put("latitud", location.getLatitude());
                                object.put("longitud", location.getLongitude());
                                object.put("id", mParamId);
                                Log.d("Respuesta", "Enviado");
                                Log.e("Coordenadas", "Latitud: " + location.getLatitude() + " Longitud: " + location.getLongitude());
                                new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/cargarCoordenadasRepartidor.php", object.toString());
                            }
                        }
                        else
                        {
                            Log.e("Timer:", "Detenido");
                            timer.cancel();
                            timer.purge();
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }
            }, 0, 2000);

        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Log.e("onStart", "App abierta");
        getActivity().stopService(new Intent(getActivity(), ServicioRepartidor.class));
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.e("onPause", "Para el servicio y solo envia las coordenadas en pantalla bloqueada...");
        getActivity().stopService(new Intent(getActivity(), ServicioRepartidor.class));
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (ARG_ID == 0)
        {
            if (Inicio_Repartidor.SRepart == 1)
            {
                Log.e("onDestroy", "App cerrada por Pedidos pendientes");
                timer.cancel();
                timer.purge();
                getActivity().stopService(new Intent(getActivity(), ServicioRepartidor.class));
            }
            else
            {
                Log.e("onDestroy", "App cerrada");
                timer.cancel();
                timer.purge();
                getActivity().startService(new Intent(getActivity(), ServicioRepartidor.class));
            }

        }
        else
        {
            timer.cancel();
            timer.purge();
            getActivity().stopService(new Intent(getActivity(), ServicioRepartidor.class));
        }
    }

    private Location getMyLocation()
    {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (myLocation == null)
        {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            String provider = lm.getBestProvider(criteria, true);
            myLocation = lm.getLastKnownLocation(provider);
        }
        return myLocation;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
            if (!s.equals("[]"))
            {
                try
                {
                    mapaRepartidorCoordenadasList = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        MapaRepartidorCoordenadas mapaRepartidorCoordenadas = new MapaRepartidorCoordenadas();
                        mapaRepartidorCoordenadas.setOrden_id(jsonObject.getInt("id"));
                        mapaRepartidorCoordenadas.setcCalle(jsonObject.getString("street"));
                        mapaRepartidorCoordenadas.setcNumero(jsonObject.getString("number"));
                        mapaRepartidorCoordenadas.setdLatitude(jsonObject.getDouble("latitude"));
                        mapaRepartidorCoordenadas.setdLongitud(jsonObject.getDouble("longitude"));
                        mapaRepartidorCoordenadas.setuName(jsonObject.getString("name"));
                        mapaRepartidorCoordenadas.setuApellido(jsonObject.getString("lastname"));
                        mapaRepartidorCoordenadasList.add(mapaRepartidorCoordenadas);
                    }
                    if (mMap != null)
                    {
                        mMap.clear();
                    }
                    for (int i=0; i<mapaRepartidorCoordenadasList.size(); i++)
                    {
                        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.casa_marcador);
                        LatLng latLngLocal = new LatLng(mapaRepartidorCoordenadasList.get(i).getdLatitude(), mapaRepartidorCoordenadasList.get(i).getdLongitud());
                        mMap.addMarker(new MarkerOptions().position(latLngLocal).snippet(mapaRepartidorCoordenadasList.get(i).getcCalle() + " #" + mapaRepartidorCoordenadasList.get(i).getcNumero()).title(mapaRepartidorCoordenadasList.get(i).getuName() + " " + mapaRepartidorCoordenadasList.get(i).getuApellido())).setIcon(BitmapDescriptorFactory.fromBitmap(new MetodosCreados().resizeMapIcons(icon, 100, 100)));
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
