package com.chile.oscar.clickcomida_aplicacionmovil;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Coordenadas;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.MapaWorkData;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.MetodosCreados;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.TiendaNotices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
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

/**
 * Created by Oscar on 23-06-2017.
 */

public class tab_map_work extends Fragment implements OnMapReadyCallback
{
    private static String TAG = "Mapa";
    List<String> stringListNameStore = new ArrayList<>();
    GoogleMap mMap;
    ProgressDialog progress;
    String tipoLoad = "";

    private final static int REQUEST_ACCESS_FINE = 1;
    List<MapaWorkData> mapaWorkDataList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.tab_map_work, container, false);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapWork);
        supportMapFragment.getMapAsync(this);


        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
            {
                //Aqui pregunta primero cuando los permidos no estan activados
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE);
            }
        }
        else
        {
            //Päsa aqui cuando los permisos estan activados
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);

            mMap = googleMap;

            if (getMyLocation() != null)
            {
                LatLng myLocation = new LatLng(getMyLocation().getLatitude(), getMyLocation().getLongitude());
                CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(myLocation, 15);
                googleMap.animateCamera(miUbicacion);
            }
            cargar();


            // Add a marker in Sydney and move the camera
            /*LatLng sydney = new LatLng(-34, 151);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
            /*mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    final int posLocal = stringListNameStore.indexOf(marker.getTitle());
                    if (mapaWorkDataList.get(posLocal).getUser_id() != Coordenadas.id)
                    {
                        builder.setTitle("Requisitos de postulación")
                                .setMessage(Html.fromHtml("<b>Vacantes: </b>" + mapaWorkDataList.get(posLocal).getTiendaVacantes() + "<br>") + mapaWorkDataList.get(posLocal).getTiendaRequerimientos())
                                .setPositiveButton("Postular",
                                        new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                                try
                                                {
                                                    progress = new ProgressDialog(getContext());
                                                    progress.setMessage("Postulando...");
                                                    progress.setCanceledOnTouchOutside(false);
                                                    progress.show();

                                                    tipoLoad = "Postular";
                                                    JSONObject jsonObject = new JSONObject();
                                                    jsonObject.put("user_id", Coordenadas.id);
                                                    jsonObject.put("notice_id", mapaWorkDataList.get(posLocal).getNotice_id());
                                                    new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/postularTienda.php", jsonObject.toString());
                                                }
                                                catch (JSONException e)
                                                {
                                                    e.printStackTrace();
                                                }

                                            }
                                        })
                                .setNegativeButton("Cancelar",
                                        new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                                dialog.dismiss();
                                            }
                                        });
                        builder.show();
                    }
                    else
                    {
                        builder.setTitle("Ya estas postulando a este requisito")
                                .setMessage(Html.fromHtml("<b>Vacantes: </b>" + mapaWorkDataList.get(posLocal).getTiendaVacantes() + "<br>") + mapaWorkDataList.get(posLocal).getTiendaRequerimientos())
                                .setPositiveButton("Eliminar",
                                        new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                                try
                                                {
                                                    progress = new ProgressDialog(getContext());
                                                    progress.setMessage("Eliminando postulación...");
                                                    progress.setCanceledOnTouchOutside(false);
                                                    progress.show();

                                                    tipoLoad = "Eliminar";
                                                    JSONObject jsonObject = new JSONObject();
                                                    jsonObject.put("user_id", Coordenadas.id);
                                                    jsonObject.put("notice_id", mapaWorkDataList.get(posLocal).getNotice_id());
                                                    new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/postularTiendaEliminar.php", jsonObject.toString());
                                                }
                                                catch (JSONException e)
                                                {
                                                    e.printStackTrace();
                                                }

                                            }
                                        })
                                .setNegativeButton("Cancelar",
                                        new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                                dialog.dismiss();
                                            }
                                        });
                        builder.show();
                    }
                }
            });*/

        }

    }
    public void cargar()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("algo", null);
            tipoLoad = "Mapa";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/cargarCoordenadasMapaWorks.php", jsonObject.toString());
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults)

    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_ACCESS_FINE:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //Aceptado
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapWork);
                    supportMapFragment.getMapAsync(this);

                }
                else
                {
                    //Negado
                    Toast.makeText(getContext(), "Debes aceptar los permisos para la camara.", Toast.LENGTH_SHORT).show();
                }
            }

        }
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
                if (tipoLoad.equals("Mapa"))
                {
                    try
                    {
                        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_work_map);
                        if (mapaWorkDataList != null)
                        {
                            if (!mapaWorkDataList.isEmpty())
                            {
                                mapaWorkDataList.clear();
                                stringListNameStore.clear();
                            }

                        }
                        JSONArray jsonArray = new JSONArray(s);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            MapaWorkData mapaWorkData = new MapaWorkData();
                            mapaWorkData.setNotice_id(jsonObject.getInt("id"));
                            mapaWorkData.setLatLngCoordenadas(new LatLng(jsonObject.getDouble("latitude"), jsonObject.getDouble("longitude")));
                            mapaWorkData.setFecha(jsonObject.getString("date"));
                            mapaWorkData.setTiendaRequerimientos(jsonObject.getString("requirements"));
                            mapaWorkData.setTiendaVacantes(jsonObject.getInt("vacants"));
                            mapaWorkData.setUser_id(jsonObject.getString("user_id"));

                            stringListNameStore.add(jsonObject.getString("name"));
                            mapaWorkDataList.add(mapaWorkData);

                        }
                        for (int i=0; i<mapaWorkDataList.size(); i++)
                        {
                            mMap.addMarker(new MarkerOptions().position(mapaWorkDataList.get(i).getLatLngCoordenadas()).title(stringListNameStore.get(i)).snippet("Vacantes: " + mapaWorkDataList.get(i).getTiendaVacantes())).setIcon(BitmapDescriptorFactory.fromBitmap(new MetodosCreados().resizeMapIcons(icon, 100, 100)));
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                /*else if (tipoLoad.equals("Postular"))
                {
                    try
                    {
                        JSONObject jsonObject = new JSONObject(s);
                        String res = jsonObject.getString("Insertado");
                        if (res.equals("Si"))
                        {
                            progress.dismiss();
                            Toast.makeText(getActivity(), "Ahora estas postulando a este aviso", Toast.LENGTH_LONG);
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else if (tipoLoad.equals("Eliminar"))
                {
                    try
                    {
                        JSONObject jsonObject = new JSONObject(s);
                        String res = jsonObject.getString("Eliminado");
                        if (res.equals("Si"))
                        {
                            progress.dismiss();
                            Toast.makeText(getActivity(), "Has dejado de postulador al aviso", Toast.LENGTH_LONG);
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }*/
            }
            else
            {
                Toast.makeText(getContext(), "Aun no hay trabajos disponibles.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
