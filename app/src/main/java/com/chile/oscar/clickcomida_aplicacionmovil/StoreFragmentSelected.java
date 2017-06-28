package com.chile.oscar.clickcomida_aplicacionmovil;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Codificacion;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Coordenadas;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.MetodosCreados;
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

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StoreFragmentSelected.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StoreFragmentSelected#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreFragmentSelected extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    String nombre, des, calle, numero, open_hour, close_hour, lunch_hour, lunch_after_hour, start_day, end_day, store_id, latitud, longitud;
    Bitmap imagenTienda;
    String tipo;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int SELECT_PICTURE = 2;
    int[] images = {R.drawable.ic_camera, R.drawable.ic_take_photo, R.drawable.ic_cancelar};
    String[] desc = {"Tomar foto", "Ir a galeria", "Cancelar"};
    ImageView imageViewTienda;
    GoogleMap googleMapGlobal;
    View vMod;
    AlertDialog mapUpdate;


    private OnFragmentInteractionListener mListener;

    public StoreFragmentSelected() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MostrarProductosMios newInstance(String store_id, String store_name) {
        MostrarProductosMios fragment = new MostrarProductosMios();
        Bundle args = new Bundle();
        args.putString("store_id", store_id);
        args.putString("store_name", store_name);
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store_fragment_selected, container, false);

        Button buttonMostrarProductos = (Button) view.findViewById(R.id.btnMostrarProductosTienda);
        Button buttonModificarFoto = (Button) view.findViewById(R.id.btnModificarFoto);
        Button buttonModificarDatos = (Button) view.findViewById(R.id.btnModificarDatosTienda);
        TextView textViewNombrePrincipal = (TextView) view.findViewById(R.id.tvTituloTienda);
        TextView textViewNombre = (TextView) view.findViewById(R.id.tvNameTiendaSelected);
        TextView textViewDes = (TextView) view.findViewById(R.id.tvDesSelected);
        final TextView textViewCalle = (TextView) view.findViewById(R.id.tvCalleSelected);
        TextView textViewNumero = (TextView) view.findViewById(R.id.tvNumeroSelected);
        TextView textViewHorario = (TextView) view.findViewById(R.id.tvHorarioSelected);
        imageViewTienda = (ImageView) view.findViewById(R.id.ivTiendaSelected);

        textViewNombrePrincipal.setText(getResources().getString(R.string.titulo_tienda_seleccionada) + " " + nombre);
        textViewNombre.setText(getResources().getString(R.string.nombre_tienda) + ": " + nombre);
        textViewDes.setText(getResources().getString(R.string.titulo_descripcion) + ": " + des);
        textViewCalle.setText(getResources().getString(R.string.calle_tienda) + ": " + calle);
        textViewNumero.setText(getResources().getString(R.string.titulo_calle_numero_usuario) + ": " + numero);

        String openupdate = new MetodosCreados().HoraNormal(open_hour);
        String closeupdate = new MetodosCreados().HoraNormal(close_hour);

        if (lunch_hour.equals("00:00:00") && lunch_after_hour.equals("00:00:00"))
        {
            textViewHorario.setText("De " + start_day + " a " + end_day + ", horario continuado desde las " + openupdate + " hasta las " + closeupdate);
        }
        else
            {
            String openupdatelunch = new MetodosCreados().HoraNormal(lunch_hour);
            String closeupdatelunch = new MetodosCreados().HoraNormal(lunch_after_hour);
            textViewHorario.setText("De " + start_day + " a " + end_day + ", horario mañana desde las " + openupdate + " hasta las " + closeupdate + ", horario tarde desde las " + openupdatelunch + " hasta las " + closeupdatelunch);
        }
        imageViewTienda.setImageBitmap(imagenTienda);

        buttonMostrarProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.content_general, newInstance(store_id, nombre));
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);
                trans.commit();
            }
        });
        buttonModificarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderChange = new AlertDialog.Builder(getContext());
                View p = getActivity().getLayoutInflater().inflate(R.layout.foto_galeria_cancelar, null);
                builderChange.setView(p);

                ListView listViewPhoto_Gallery = (ListView) p.findViewById(R.id.lvPhoto_Gallery);


                listViewPhoto_Gallery.setAdapter(new CustomAdapter());
                final AlertDialog dialogAlert = builderChange.create();
                dialogAlert.show();

                listViewPhoto_Gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            dialogAlert.dismiss();
                            dispatchTakePictureIntent();
                        } else if (position == 1) {
                            dialogAlert.dismiss();
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), SELECT_PICTURE);
                        } else if (position == 2) {
                            dialogAlert.dismiss();
                        }
                    }
                });
            }
        });
        buttonModificarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                final AlertDialog.Builder builderMod = new AlertDialog.Builder(getContext());
                if (googleMapGlobal != null)
                {
                    googleMapGlobal.clear();
                }
                if (vMod == null)
                {
                    vMod = getActivity().getLayoutInflater().inflate(R.layout.modificar_datos_tienda, null);
                }
                if (vMod.getParent() != null)
                {
                    ((ViewGroup)vMod.getParent()).removeView(vMod);
                }
                final EditText editTextNombreTienda = (EditText) vMod.findViewById(R.id.etTiendaMod);
                final EditText editTextDesTienda = (EditText) vMod.findViewById(R.id.etDesMod);
                final EditText editTextCalle = (EditText) vMod.findViewById(R.id.etCalleMod);
                final EditText editTextNum = (EditText) vMod.findViewById(R.id.etNumMod);
                LinearLayout linearLayoutUno = (LinearLayout) vMod.findViewById(R.id.llHorarioUno);
                LinearLayout linearLayoutDos = (LinearLayout) vMod.findViewById(R.id.llHorarioDos);
                TextView textViewTituloUno = (TextView) vMod.findViewById(R.id.tvTituloUno);
                TextView textViewTituloDos = (TextView) vMod.findViewById(R.id.tvTituloDos);
                final TextView textViewHora1 = (TextView) vMod.findViewById(R.id.tvHora1);
                final TextView textViewHora2 = (TextView) vMod.findViewById(R.id.tvHora2);
                final TextView textViewHora3 = (TextView) vMod.findViewById(R.id.tvHora3);
                final TextView textViewHora4 = (TextView) vMod.findViewById(R.id.tvHora4);
                //MapView mapViewTienda = (MapView)vMod.findViewById(R.id.mpTienda);
                Button buttonModTienda = (Button) vMod.findViewById(R.id.btnModTienda);



                SupportMapFragment map = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.mpTienda);

                linearLayoutUno.setVisibility(View.GONE);
                linearLayoutDos.setVisibility(View.GONE);

                editTextNombreTienda.setText(nombre);
                editTextDesTienda.setText(des);
                editTextCalle.setText(calle);
                editTextNum.setText(numero);

                if (lunch_hour == "null" && lunch_after_hour == "null")
                {
                    linearLayoutUno.setVisibility(View.VISIBLE);
                    textViewTituloUno.setText(getResources().getString(R.string.horario_continuado));
                    textViewHora1.setText(new MetodosCreados().HoraNormal(open_hour));
                    textViewHora2.setText(new MetodosCreados().HoraNormal(close_hour));
                } else {
                    linearLayoutUno.setVisibility(View.VISIBLE);
                    linearLayoutDos.setVisibility(View.VISIBLE);
                    textViewTituloUno.setText(getResources().getString(R.string.horario_m));
                    textViewTituloDos.setText(getResources().getString(R.string.horario_t));
                    textViewHora1.setText(new MetodosCreados().HoraNormal(open_hour));
                    textViewHora2.setText(new MetodosCreados().HoraNormal(close_hour));
                    textViewHora3.setText(new MetodosCreados().HoraNormal(lunch_hour));
                    textViewHora4.setText(new MetodosCreados().HoraNormal(lunch_after_hour));
                }
                map.getMapAsync(new OnMapReadyCallback()
                {
                    @Override
                    public void onMapReady(final GoogleMap googleMap)
                    {
                        googleMapGlobal = googleMap;
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
                        googleMap.getUiSettings().setMapToolbarEnabled(false);
                        Location location = getMyLocation();
                        if (location != null)
                        {
                            LatLng latLngLocal = new LatLng(location.getLatitude(), location.getLongitude());
                            CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(latLngLocal, 15);
                            googleMap.animateCamera(miUbicacion);
                        }

                        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                            @Override
                            public void onCameraMove()
                            {
                                if (googleMap != null)
                                {
                                    googleMap.clear();
                                }
                                LatLng latlngLocal = googleMap.getProjection().getVisibleRegion().latLngBounds.getCenter();
                                googleMap.addMarker(new MarkerOptions().position(latlngLocal).title(nombre));

                                Coordenadas.latitud = googleMap.getCameraPosition().target.latitude;
                                Coordenadas.longitud = googleMap.getCameraPosition().target.longitude;
                            }
                        });

                        LatLng latLng = new LatLng(Float.parseFloat(latitud), Float.parseFloat(longitud));
                        googleMap.addMarker(new MarkerOptions().position(latLng).title(nombre));

                        Coordenadas.latitud = Double.parseDouble(latitud);
                        Coordenadas.longitud = Double.parseDouble(longitud);

                        CameraUpdate LocationStore = CameraUpdateFactory.newLatLngZoom(latLng, 5);
                        googleMap.animateCamera(LocationStore);

                    }
                });
                textViewHora1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        new MetodosCreados().LlamarHora(textViewHora1, getContext());
                    }
                });
                textViewHora2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        new MetodosCreados().LlamarHora(textViewHora2, getContext());
                    }
                });
                textViewHora3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        new MetodosCreados().LlamarHora(textViewHora3, getContext());
                    }
                });
                textViewHora4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        new MetodosCreados().LlamarHora(textViewHora4, getContext());
                    }
                });
                buttonModTienda.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        tipo = "Datos";
                        JSONObject object = new JSONObject();
                        try
                        {
                            object.put("Id", store_id);
                            object.put("Nombre", editTextNombreTienda.getText().toString());
                            object.put("Descripcion", editTextDesTienda.getText().toString());
                            object.put("Calle", editTextCalle.getText().toString());
                            object.put("Numero", editTextNum.getText().toString());
                            object.put("open_hour", textViewHora1.getText().toString());
                            object.put("close_hour", textViewHora2.getText().toString());
                            object.put("lunch_hour", textViewHora3.getText().toString());
                            object.put("lunch_after_hour", textViewHora4.getText().toString());
                            object.put("latitud", Coordenadas.latitud);
                            object.put("longitud", Coordenadas.longitud);

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        new ModificarDatos().execute(getResources().getString(R.string.direccion_web) + "Controlador/modificarDatosTienda.php", object.toString());
                    }
                });


                builderMod.setView(vMod);
                mapUpdate = builderMod.create();
                mapUpdate.show();
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    private void dispatchTakePictureIntent()
    {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
        {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null)
            {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
        else
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA))
            {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
                // Mostrar diálogo explicativo
            }
            else
            {
                // Solicitar permiso
                //ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
            }
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageViewTienda.setImageBitmap(imageBitmap);
            String imagenGeneral = Codificacion.encodeToBase64(imageBitmap, Bitmap.CompressFormat.PNG, 100);
            JSONObject object = new JSONObject();
            try
            {
                object.put("Nombre", nombre);
                object.put("Imagen", imagenGeneral);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            new ModificarDatos().execute(getResources().getString(R.string.direccion_web) + "Controlador/modificarImagenTienda.php", object.toString());
        }
        else if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK)
        {
            Uri path = data.getData();
            try
            {
                //int HeightButton = imageViewTienda.getHeight();
                //int WidthButton = imageViewTienda.getWidth();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), path);
                //Bitmap rotado = Codificacion.RotarBitmap(Codificacion.bajarResolucion(bitmap, WidthButton, HeightButton), 90);
                imageViewTienda.setImageBitmap(Codificacion.bajarResolucion(bitmap, 500, 500));
                String imagenCod = Codificacion.encodeToBase64(Codificacion.bajarResolucion(bitmap, 500, 500), Bitmap.CompressFormat.PNG, 100);
                JSONObject object = new JSONObject();
                try
                {
                    object.put("Nombre", nombre);
                    object.put("Imagen", imagenCod);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                new ModificarDatos().execute(getResources().getString(R.string.direccion_web) + "Controlador/modificarImagenTienda.php", object.toString());
                //imagenGeneral = Codificacion.encodeToBase64(rotado, Bitmap.CompressFormat.PNG, 100);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    class CustomAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return images.length; //Indico las veces que debe recorrer
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
            convertView = getActivity().getLayoutInflater().inflate(R.layout.customlayout_photo_galley_cancel, null);
            ImageView imageView = (ImageView)convertView.findViewById(R.id.ivProductoImage);
            TextView textViewNombre = (TextView)convertView.findViewById(R.id.txtOption);

            imageView.setImageResource(images[position]);
            textViewNombre.setText(desc[position]);

            return convertView;
        }
    }
    public class ModificarDatos extends AsyncTask<String, Void, String>
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
            if (tipo == "Datos")
            {
                try
                {
                    JSONObject object = new JSONObject(s);
                    String res = object.getString("Resultado");
                    if (res.equals("Si"))
                    {
                        Toast.makeText(getContext(), "Los datos han sido modificados.", Toast.LENGTH_SHORT).show();
                        mapUpdate.dismiss();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(getContext(), "Imagen modificada.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
