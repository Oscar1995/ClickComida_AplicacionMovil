package com.chile.oscar.clickcomida_aplicacionmovil;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Coordenadas;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.MetodosCreados;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Pedidos_Proceso;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.plus.model.people.Person;

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
 * {@link Tracking.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tracking#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tracking extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private GoogleApiClient mGoogleApiClient;
    private String mParam1;
    private String mParam2;
    ListView listViewPedidos;
    List<Pedidos_Proceso> pedidos_procesoList;
    ProgressDialog progress;
    String tipoLoad = "";
    View pMap;

    GoogleMap googleMapGlobal;
    LatLng latLngLocal;

    private OnFragmentInteractionListener mListener;

    public Tracking() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tracking.
     */
    // TODO: Rename and change types and number of parameters
    public static Tracking newInstance(String param1, String param2) {
        Tracking fragment = new Tracking();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle)
                    {
                        Toast.makeText(getContext(), "Conectado", Toast.LENGTH_SHORT).show();

                        /*Timer timer = new Timer();
                        timer.scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run()
                            {
                                Log.d("Veces", veces + "");
                                veces++;
                            }
                        }, 0, 5000);*/
                    }

                    @Override
                    public void onConnectionSuspended(int i)
                    {
                        Toast.makeText(getContext(), "Suspendido", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
                    {
                        Toast.makeText(getContext(), "Fallido", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(LocationServices.API)
                .build();


        progress = new ProgressDialog(getContext());
        progress.setMessage("Cargando pedidos...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        View v = inflater.inflate(R.layout.fragment_tracking, container, false);
        listViewPedidos = (ListView) v.findViewById(R.id.lvPedidos);
        pedidos_procesoList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject();
            object.put("user_id", Coordenadas.id);
            tipoLoad = "Pedidos";
            new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/cargarPedidos_usuario.php", object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listViewPedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final AlertDialog.Builder builderMapa = new AlertDialog.Builder(getContext());
                pMap = getActivity().getLayoutInflater().inflate(R.layout.activity_maps_tienda, null);
                final SupportMapFragment map = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.mapFrag);

                Button botonTomarCoor = (Button) pMap.findViewById(R.id.btnFijarMapaTienda);
                builderMapa.setView(pMap);
                final AlertDialog mapUpdate = builderMapa.create();
                mapUpdate.show();
                map.getMapAsync(new OnMapReadyCallback()
                {
                    @Override
                    public void onMapReady(final GoogleMap googleMap)
                    {
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

                        Timer timer = new Timer();
                        timer.scheduleAtFixedRate(new TimerTask()
                        {
                            @Override
                            public void run()
                            {

                                try
                                {
                                    JSONObject object = new JSONObject();
                                    object.put("user_id", Coordenadas.id);
                                    object.put("order_id", pedidos_procesoList.get(position).getOrden_id());
                                    tipoLoad = "Repartidor";
                                    googleMapGlobal = googleMap;
                                    new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/ubicacionRepartidor.php", object.toString());

                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }

                            }
                        }, 0, 60000);
                        googleMap.setMyLocationEnabled(true);
                        googleMap.getUiSettings().setZoomControlsEnabled(true);
                    }
                });
                botonTomarCoor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        getFragmentManager().beginTransaction().remove(map).commit();
                        mapUpdate.dismiss();
                    }
                });

            }
        });

        return v;
    }

    /*@Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }*/

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        Toast.makeText(getContext(), "hola", Toast.LENGTH_SHORT).show();
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }
    class PedidosAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return pedidos_procesoList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            convertView = getActivity().getLayoutInflater().inflate(R.layout.custom_tracking, null);
            TextView textViewIdPedido = (TextView)convertView.findViewById(R.id.txtIdPedido);
            TextView textViewEstado = (TextView)convertView.findViewById(R.id.txtEstado);
            TextView textViewNombreTienda = (TextView)convertView.findViewById(R.id.txtNombreTienda);
            TextView textViewFecha= (TextView)convertView.findViewById(R.id.txtFecha);

            textViewIdPedido.setText(pedidos_procesoList.get(position).getOrden_id() + "");
            textViewEstado.setText(pedidos_procesoList.get(position).getEstado() + "");
            textViewNombreTienda.setText("Tienda: " + pedidos_procesoList.get(position).getNombreTienda() + "");
            textViewFecha.setText(pedidos_procesoList.get(position).getOrden_fecha() + "");


            return convertView;
        }
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
    public interface OnFragmentInteractionListener {
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
            try
            {
                if (tipoLoad.equals("Pedidos"))
                {
                    if (!s.equals("[]"))
                    {
                        JSONArray jsonArray = new JSONArray(s);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject object = jsonArray.getJSONObject(i);
                            Pedidos_Proceso pedidos_proceso = new Pedidos_Proceso();
                            pedidos_proceso.setOrden_id(object.getInt("id"));
                            pedidos_proceso.setOrden_fecha(object.getString("date"));
                            pedidos_proceso.setEstado(object.getString("description"));
                            pedidos_proceso.setNombreTienda(object.getString("name"));
                            pedidos_procesoList.add(pedidos_proceso);
                        }
                        progress.dismiss();
                        listViewPedidos.setAdapter(new PedidosAdapter());
                    }
                    else
                    {
                        progress.dismiss();
                        Toast.makeText(getContext(), "Aun no tienes pedidos", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (tipoLoad.equals("Repartidor"))
                {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    if (!s.equals("[]"))
                    {
                        if (googleMapGlobal != null)
                        {
                            googleMapGlobal.clear();
                        }
                        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_moto_marker);
                        latLngLocal = new LatLng(jsonObject.getDouble("latitude"), jsonObject.getDouble("longitude"));
                        googleMapGlobal.addMarker(new MarkerOptions().position(latLngLocal).title("Repartidor")).setIcon(BitmapDescriptorFactory.fromBitmap(new MetodosCreados().resizeMapIcons(icon, 100, 100)));
                    }
                }


            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
