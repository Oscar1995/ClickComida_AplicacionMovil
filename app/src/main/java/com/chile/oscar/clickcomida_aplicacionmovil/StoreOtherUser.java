package com.chile.oscar.clickcomida_aplicacionmovil;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.Rating;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Codificacion;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Comentarios;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Coordenadas;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.MapStatic;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.MetodosCreados;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Notices;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StoreOtherUser.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StoreOtherUser#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreOtherUser extends Fragment implements View.OnClickListener
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String nombre, des, calle, numero, open_hour, close_hour, lunch_hour, lunch_after_hour, start_day, end_day, store_id, latitud, longitud, user_id, tipoReg;
    Bitmap imagenTienda;
    Boolean userCal = false, sw = false;

    int countLike = 0;
    String nameStore = "a tienda";

    List<Comentarios> comentariosList = new ArrayList<>();

    ListView listaComentarios;
    EditText editTextComentario;
    TextView textViewDes;
    GoogleMap googleMapGlobal;
    Boolean mapLoad = false;

    Boolean favorite = false;
    ImageView imageViewFavoritos;
    Button buttonAviso;
    Notices noticesVariable;

    RatingBar ratingBarUsuario, ratingTienda;
    TextView textViewCal, textViewLikeStore;
    ProgressDialog progress;

    private OnFragmentInteractionListener mListener;

    public StoreOtherUser() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoreOtherUser.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreOtherUser newInstance(String param1, String param2)
    {
        StoreOtherUser fragment = new StoreOtherUser();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public static ProductsOtherUser newProd(String store_id, String nombreTienda, Bitmap imagenTienda)
    {
        ProductsOtherUser fragment = new ProductsOtherUser();
        Bundle args = new Bundle();
        args.putString("store_id", store_id);
        args.putString("nombre_tienda", nombreTienda);
        args.putString("imagenTienda", Codificacion.encodeToBase64(imagenTienda, Bitmap.CompressFormat.PNG, 100));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            imagenTienda = Codificacion.decodeBase64(getArguments().getString("imagen_tienda"));
            nombre = getArguments().getString("nombre_tienda");
            des = getArguments().getString("des_tienda");
            calle = getArguments().getString("calle_tienda");
            numero = getArguments().getString("numero_tienda");
            start_day = getArguments().getString("start_day");
            end_day = getArguments().getString("end_day");
            open_hour = getArguments().getString("open_hour");
            close_hour = getArguments().getString("close_hour");
            lunch_hour = getArguments().getString("lunch_open_hour");
            lunch_after_hour = getArguments().getString("lunch_after_hour");
            store_id = getArguments().getString("tienda_id");
            latitud = getArguments().getString("latitud");
            longitud = getArguments().getString("longitud");
            user_id = getArguments().getString("user_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        progress = new ProgressDialog(getContext());
        progress.setMessage("Cargando calificacion...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();


        View view = inflater.inflate(R.layout.fragment_store_other_user, container, false);

        buttonAviso = (Button)view.findViewById(R.id.btnPostularAviso);
        listaComentarios = (ListView)view.findViewById(R.id.lvComentarios);
        ImageView imageViewOther = (ImageView)view.findViewById(R.id.ivTiendaOther);
        TextView textViewNombre =(TextView)view.findViewById(R.id.tvNombreOther);
        final TextView textViewDireccion =(TextView)view.findViewById(R.id.tvDireccionOther);
        TextView textViewKilometros =(TextView)view.findViewById(R.id.tvKilometrosOther);
        TextView textViewHorario = (TextView)view.findViewById(R.id.tvHorarioOther);
        textViewDes = (TextView)view.findViewById(R.id.tvDesStore);
        editTextComentario  = (EditText)view.findViewById(R.id.etComentarioOther);
        imageViewFavoritos = (ImageView)view.findViewById(R.id.ivFavoritos);
        textViewLikeStore = (TextView)view.findViewById(R.id.tvLikeStore);

        Button buttonComentar = (Button)view.findViewById(R.id.btnComentar);
        Button buttonProducto = (Button)view.findViewById(R.id.btnProductOther);
        Button buttonMapa = (Button)view.findViewById(R.id.btnVerMapa);

        textViewCal = (TextView)view.findViewById(R.id.tvInfoCal);

        imageViewOther.setImageDrawable(new MetodosCreados().EncuadrarBitmap(imagenTienda, getResources()));
        textViewNombre.setText(Html.fromHtml("<b>"+getResources().getString(R.string.Tienda) + ": </b>" + nombre));
        textViewDireccion.setText(Html.fromHtml("<b>"+getResources().getString(R.string.Direccion) + ": </b>" + calle + " #" + numero));


        Location location = getMyLocation();
        if (location != null)
        {
            textViewKilometros.setVisibility(View.VISIBLE);
            if (new MetodosCreados().CalculationByDistanceKilometers(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud))) == 0)
            {
                textViewKilometros.setText(Html.fromHtml("<b>"+getResources().getString(R.string.Ubicacion) + ": </b>" + "Esta cerca de ti"));
            }
            else
            {
                textViewKilometros.setText(Html.fromHtml("<b>"+getResources().getString(R.string.Ubicacion) + ": </b>" + new MetodosCreados().CalculationByDistanceKilometers(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud))) + " kilometros de ti"));
            }

        }
        else
        {
            textViewKilometros.setVisibility(View.GONE);
        }


        String openupdate = new MetodosCreados().HoraNormal(open_hour);
        String closeupdate = new MetodosCreados().HoraNormal(close_hour);

        if (lunch_hour.equals("00:00:00") && lunch_after_hour.equals("00:00:00"))
        {
            textViewHorario.setText(Html.fromHtml("<b> <font color=\"blue\">"+start_day + " a " + end_day+ "</font> <br>"+
                    "Continuado: </b>" +  "de " + openupdate + " a " + closeupdate));
        }
        else
        {
            String openupdatelunch = new MetodosCreados().HoraNormal(lunch_hour);
            String closeupdatelunch = new MetodosCreados().HoraNormal(lunch_after_hour);
            textViewHorario.setText(Html.fromHtml("<b> <font color=\"blue\">"+start_day + " a " + end_day+ "</font> <br>"+
                    "Mañana: </b>" +  "de " + openupdate + " a " + closeupdate + "<br>"
                    + "<b>Tarde: </b>de " + openupdatelunch + " a " + closeupdatelunch));
        }

        cargarCalificacion();
        ratingTienda = (RatingBar)view.findViewById(R.id.rbTienda);
        ratingBarUsuario = (RatingBar)view.findViewById(R.id.ratingBarU);
        ratingBarUsuario.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
        {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
            {
                if (sw == false)
                {
                    if (userCal == false)
                    {
                        ratingBarUsuario.setRating(rating);
                        JSONObject jsonObject = new JSONObject();
                        try
                        {
                            jsonObject.put("Valor", rating);
                            jsonObject.put("user_id", Coordenadas.id);
                            jsonObject.put("store_id", store_id);
                            tipoReg = "Insertar calificacion";
                            userCal = true;

                            progress = new ProgressDialog(getContext());
                            progress.setMessage("Calificando tienda...");
                            progress.setCanceledOnTouchOutside(false);
                            progress.show();

                            new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/calificarTienda.php", jsonObject.toString());
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        //Update
                        ratingBarUsuario.setRating(rating);
                        JSONObject jsonObject = new JSONObject();
                        try
                        {
                            jsonObject.put("Valor", rating);
                            jsonObject.put("user_id", Coordenadas.id);
                            jsonObject.put("store_id", store_id);
                            tipoReg = "Modificar calificacion";
                            userCal = true;

                            progress = new ProgressDialog(getContext());
                            progress.setMessage("Actualizando tu calificación...");
                            progress.setCanceledOnTouchOutside(false);
                            progress.show();

                            new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/modificarCalificacion.php", jsonObject.toString());
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }

                }
                sw = false;
            }
        });
        buttonMapa.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (MapStatic.pMap == null)
                {
                    MapStatic.pMap = getActivity().getLayoutInflater().inflate(R.layout.activity_maps_tienda, null);
                }
                if (MapStatic.pMap.getParent() != null)
                {
                    ((ViewGroup)MapStatic.pMap.getParent()).removeView(MapStatic.pMap);
                }
                final AlertDialog.Builder builderMapa = new AlertDialog.Builder(getContext());
                final SupportMapFragment map = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.mapFrag);

                builderMapa.setView(MapStatic.pMap);
                final AlertDialog mapUpdate = builderMapa.create();
                mapUpdate.show();

                map.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(final GoogleMap googleMap)
                    {
                        googleMap.clear();
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
                        googleMap.setMyLocationEnabled(true);
                        googleMap.getUiSettings().setZoomControlsEnabled(true);
                        LatLng latlng = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));
                        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.store_icon);
                        googleMap.addMarker(new MarkerOptions().position(latlng).title(nombre).snippet(calle + " #" + numero)).setIcon(BitmapDescriptorFactory.fromBitmap(new MetodosCreados().resizeMapIcons(icon, 100, 100)));;
                        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(latlng, 15);
                        googleMap.animateCamera(miUbicacion);
                    }
                });
            }
        });
        buttonComentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (!editTextComentario.getText().toString().isEmpty())
                {
                    JSONObject object = new JSONObject();
                    try
                    {
                        object.put("Valor", editTextComentario.getText().toString().trim());
                        object.put("user_id", Coordenadas.id);
                        object.put("store_id", store_id);
                        tipoReg = "Insertar comentario";

                        progress = new ProgressDialog(getContext());
                        progress.setMessage("Espere...");
                        progress.setCanceledOnTouchOutside(false);
                        progress.show();

                        new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/insertarComentarioTienda.php", object.toString());
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "El comentario no debe estar en blanco", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonProducto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.content_general, newProd(store_id, nombre, imagenTienda));
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);
                trans.commit();
            }
        });
        imageViewFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                JSONObject object = new JSONObject();
                try
                {
                    if (favorite == false)
                    {
                        object.put("user_id", Coordenadas.id);
                        object.put("store_id", store_id);
                        tipoReg = "Agregar favoritos";

                        progress = new ProgressDialog(getContext());
                        progress.setMessage("Agregando a favoritos: " + nombre);
                        progress.setCanceledOnTouchOutside(false);
                        progress.show();

                        new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/insertarFavoritos.php", object.toString());
                    }
                    else
                    {
                        object.put("user_id", Coordenadas.id);
                        object.put("store_id", store_id);
                        tipoReg = "Eliminar favoritos";

                        progress = new ProgressDialog(getContext());
                        progress.setMessage("Eliminando de tus favoritos a la tienda " + nombre);
                        progress.setCanceledOnTouchOutside(false);
                        progress.show();

                        new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/eliminarFavorito.php", object.toString());
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });

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
    public void onClick(View v) {

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
    public void cargarCalificacion()
    {
        JSONObject object = new JSONObject();
        try
        {
            object.put("user_id", Coordenadas.id);
            object.put("store_id", store_id);
            tipoReg = "Cargar valor";
            new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/cargarCalificacion.php", object.toString());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
    public void cargarComentarios()
    {
        JSONObject object = new JSONObject();
        try
        {
            object.put("store_id", store_id);
            tipoReg = "Cargar comentarios";
            new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/listaComentarios.php", object.toString());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
    public void cargarFavorito()
    {
        JSONObject object = new JSONObject();
        try
        {
            object.put("user_id", Coordenadas.id);
            object.put("store_id", store_id);
            tipoReg = "Cargar favorito";
            new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/cargarFavorito.php", object.toString());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    class AdapterComentarios extends BaseAdapter
    {

        @Override
        public int getCount() {
            return comentariosList.size();
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
            convertView = getActivity().getLayoutInflater().inflate(R.layout.custom_lista_comentarios, null);
            TextView textViewNickname = (TextView)convertView.findViewById(R.id.tvNickname);
            TextView textViewFecha = (TextView)convertView.findViewById(R.id.tvFechaCreacion);
            TextView textViewComentario = (TextView)convertView.findViewById(R.id.tvComentario);

            textViewNickname.setText(comentariosList.get(position).getuNickname());
            textViewFecha.setText(comentariosList.get(position).getFechaCreacion());
            textViewComentario.setText(comentariosList.get(position).getuComentario());

            return convertView;
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
    public class EjecutarConsulta extends AsyncTask<String, Void, String> {
        @Override
        public String doInBackground(String... params) {
            HttpURLConnection conn = null;
            try {
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
                switch (responseCode) {
                    case 200:
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                        return response.toString();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (conn != null) {
                    try {
                        conn.disconnect();
                    } catch (Exception ex) {
                        Toast.makeText(getContext(), "Intentalo de nuevo.", Toast.LENGTH_SHORT).show();
                        //ex.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            //Cargar valor - cargar favorito - cargar comentarios
            try
            {
                if (tipoReg.equals("Insertar calificacion"))
                {
                    JSONObject object = new JSONObject(s);
                    String res = object.getString("Resultado");
                    if (res.equals("Si"))
                    {
                        String cuenta = object.getString("Cuenta");
                        String suma = object.getString("Suma");
                        ratingTienda.setRating( (Float.parseFloat(suma) / Float.parseFloat(cuenta)) );
                        progress.dismiss();
                        Toast.makeText(getContext(), "Has calificado esta tienda", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (tipoReg.equals("Cargar valor"))
                {
                    JSONObject object = new JSONObject(s);
                    String valor = object.getString("Valor");
                    if (!valor.equals("null")) //Aqui llega cuando el usuario ya califica la tienda
                    {
                        String promedio = object.getString("Promedio");
                        userCal = true;
                        sw = true;

                        ratingTienda.setRating(Float.parseFloat(promedio));
                        ratingBarUsuario.setRating(Float.parseFloat(valor));
                        textViewCal.setText("Has calificado esta tienda");
                    }
                    else //Aqui llega cuando el usuario aun no califica la tienda
                    {
                        if (!object.getString("Promedio").equals("null"))
                        {
                            sw = false;
                            userCal = false;
                            ratingTienda.setRating(Float.parseFloat(object.getString("Promedio")));
                            textViewCal.setText("Califica esta tienda");
                        }
                        else
                        {
                            ratingTienda.setRating(0);
                        }

                    }
                    if (!object.getString("Favoritos").equals("null"))
                    {
                        countLike = object.getInt("Favoritos");
                        if (countLike == 0)
                        {
                            textViewLikeStore.setText(getResources().getString(R.string.none_person));
                        }
                        else if (countLike == 1)
                        {
                            textViewLikeStore.setText(countLike + " " + getResources().getString(R.string.none_person_one) + nameStore);
                        }
                        else
                        {
                            textViewLikeStore.setText(countLike + " " + getResources().getString(R.string.none_person_more) + nameStore);
                        }
                    }
                    else
                    {
                        textViewLikeStore.setText(getResources().getString(R.string.none_person));
                    }
                    textViewDes.setText(Html.fromHtml("<b>" + getResources().getString(R.string.Descripcion) + " </b>" + object.getString("Descripcion")));
                    progress.setMessage("Cargando...");
                    cargarFavorito();
                }
                else if (tipoReg.equals("Modificar calificacion"))
                {
                    JSONObject object = new JSONObject(s);
                    String res = object.getString("Actualizado");
                    if (res.equals("Si"))
                    {
                        String cuenta = object.getString("Cuenta");
                        String suma = object.getString("Suma");

                        ratingTienda.setRating( (Float.parseFloat(suma) / Float.parseFloat(cuenta)) );

                        progress.dismiss();
                        Toast.makeText(getContext(), "Has modificado la calificacion", Toast.LENGTH_SHORT).show();
                        textViewCal.setText("Has calificado esta tienda");
                    }
                }
                else if (tipoReg.equals("Agregar favoritos"))
                {
                    JSONObject object = new JSONObject(s);
                    String res = object.getString("Insertado");
                    if (res.equals("Si"))
                    {
                        favorite = true;
                        imageViewFavoritos.setImageResource(R.drawable.heart_active);
                        progress.dismiss();
                        Toast.makeText(getContext(), "Has agregado la tienda " + nombre +" a tus favoritos.", Toast.LENGTH_SHORT).show();
                        countLike++;
                        if (countLike == 0)
                        {
                            textViewLikeStore.setText(getResources().getString(R.string.none_person));
                        }
                        else if (countLike == 1)
                        {
                            textViewLikeStore.setText(countLike + " " + getResources().getString(R.string.none_person_one) + nameStore);
                        }
                        else
                        {
                            textViewLikeStore.setText(countLike + " " + getResources().getString(R.string.none_person_more) + nameStore);
                        }
                    }
                }
                else if (tipoReg.equals("Eliminar favoritos"))
                {
                    JSONObject object = new JSONObject(s);
                    String res = object.getString("Eliminado");
                    if (res.equals("Si"))
                    {
                        favorite = false;
                        imageViewFavoritos.setImageResource(R.drawable.heart_desactive);
                        progress.dismiss();
                        Toast.makeText(getContext(), "La tienda " + nombre +" ya no pertenece a tus favoritos.", Toast.LENGTH_SHORT).show();
                        countLike--;
                        if (countLike == 0)
                        {
                            textViewLikeStore.setText(getResources().getString(R.string.none_person));
                        }
                        else if (countLike == 1)
                        {
                            textViewLikeStore.setText(countLike + " " + getResources().getString(R.string.none_person_one) + nameStore);
                        }
                        else
                        {
                            textViewLikeStore.setText(countLike + " " + getResources().getString(R.string.none_person_more) + nameStore);
                        }
                    }
                }
                else if (tipoReg.equals("Cargar favorito"))
                {
                    JSONObject object = new JSONObject(s);
                    String res = object.getString("Resultado");
                    if (res.equals("Si"))
                    {
                        imageViewFavoritos.setImageResource(R.drawable.heart_active);
                        favorite = true;
                    }
                    else if (res.equals("No"))
                    {
                        imageViewFavoritos.setImageResource(R.drawable.heart_desactive);
                        favorite = false;
                    }
                    progress.setMessage("Cargando comentarios de la tienda...");
                    cargarComentarios();
                }
                else if (tipoReg.equals("Insertar comentario"))
                {
                    JSONObject object = new JSONObject(s);
                    String res = object.getString("Insertado");
                    if (res.equals("Si"))
                    {
                        progress.dismiss();
                        editTextComentario.setText("");
                        cargarComentarios();
                        Toast.makeText(getContext(), "Has comentado esta tienda.", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (tipoReg.equals("Cargar comentarios"))
                {

                    JSONArray jsonArray = new JSONArray(s);

                    boolean isCommentary = s.contains("commentary");
                    boolean isNotice = s.contains("requirements");
                    int ultimoDigito = jsonArray.length();

                    if (comentariosList != null)
                    {
                        if (!comentariosList.isEmpty())
                        {
                            comentariosList.clear();
                        }
                    }
                    if (jsonArray.length() == 1)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        if (isNotice)
                        {
                            buttonAviso.setVisibility(View.VISIBLE);
                            Notices notices = new Notices();
                            notices.setNoticeId(jsonObject.getInt("id"));
                            notices.setDateNotice(jsonObject.getString("date"));
                            notices.setRequirementsNotice(jsonObject.getString("requirements"));
                            notices.setVacantsNotice(jsonObject.getInt("available"));
                            noticesVariable = notices;
                        }
                        else if (isCommentary)
                        {
                            Comentarios comentarios = new Comentarios();
                            comentarios.setuNickname(jsonObject.getString("nickname"));
                            comentarios.setuComentario(jsonObject.getString("commentary"));
                            comentarios.setFechaCreacion(jsonObject.getString("date"));
                            comentariosList.add(comentarios);
                        }
                    }
                    else
                    {
                        JSONObject jsonObjects = null;
                        if (isNotice)
                        {

                            for (int i=0; i<jsonArray.length(); i++)
                            {
                                jsonObjects = jsonArray.getJSONObject(i);
                                if ((ultimoDigito - 1) == i)
                                {
                                    if (isNotice)
                                    {
                                        buttonAviso.setVisibility(View.VISIBLE);
                                        Notices notices = new Notices();
                                        notices.setNoticeId(jsonObjects.getInt("id"));
                                        notices.setDateNotice(jsonObjects.getString("date"));
                                        notices.setRequirementsNotice(jsonObjects.getString("requirements"));
                                        notices.setVacantsNotice(jsonObjects.getInt("available"));
                                        noticesVariable = notices;
                                    }
                                }
                                else
                                {
                                    if (isCommentary)
                                    {
                                        Comentarios comentarios = new Comentarios();
                                        comentarios.setuNickname(jsonObjects.getString("nickname"));
                                        comentarios.setuComentario(jsonObjects.getString("commentary"));
                                        comentarios.setFechaCreacion(jsonObjects.getString("date"));
                                        comentariosList.add(comentarios);
                                    }
                                }
                            }
                        }
                        else if (isCommentary)
                        {
                            for (int i=0; i<jsonArray.length(); i++)
                            {
                                jsonObjects = jsonArray.getJSONObject(i);
                                Comentarios comentarios = new Comentarios();
                                comentarios.setuNickname(jsonObjects.getString("nickname"));
                                comentarios.setuComentario(jsonObjects.getString("commentary"));
                                comentarios.setFechaCreacion(jsonObjects.getString("date"));
                                comentariosList.add(comentarios);
                            }
                        }

                    }

                    listaComentarios.setAdapter(new AdapterComentarios());
                    progress.dismiss();
                }
            }
            catch (JSONException e)
            {
                progress.dismiss();
                e.printStackTrace();
            }

        }
    }
}
