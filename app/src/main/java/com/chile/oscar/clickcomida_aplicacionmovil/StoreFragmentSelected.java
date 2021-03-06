package com.chile.oscar.clickcomida_aplicacionmovil;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
    ListView listViewComentarios;
    Bitmap imagenTienda;
    String tipo, subTipo, requerimientoGlobal;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int SELECT_PICTURE = 2;


    private final static int REQUEST_ACCESS_CAMERA = 123;
    private final static int WRITE_EXTERNAL_STORAGE = 643;


    int[] images = {R.drawable.ic_camera, R.drawable.ic_take_photo, R.drawable.ic_cancelar};
    String[] desc = {"Tomar foto", "Ir a galeria", "Cancelar"};
    ImageView imageViewTienda;
    GoogleMap googleMapGlobal;
    Button buttonPublicarAviso;
    View vMod;
    Notices noticesObject;
    AlertDialog mapUpdate;
    ProgressDialog progress;
    String dInicio = "";
    String dFin = "";
    String textEnabled = "";
    int nAvailable;
    Notices noticesLocal = new Notices();
    String[] dayWeek = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};
    List<Comentarios> comentariosList = new ArrayList<>();

    TextView textViewNombrePrincipal;
    TextView textViewNombre;
    TextView textViewDireccion;
    TextView textViewDescripcion;
    TextView textViewHorario;

    TextView textViewHora1;
    TextView textViewHora2;
    TextView textViewHora3;
    TextView textViewHora4;

    EditText editTextNombreTienda;
    EditText editTextDesTienda;
    EditText editTextCalle;
    EditText editTextNum;


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
        final View view = inflater.inflate(R.layout.fragment_store_fragment_selected, container, false);
        CargarComentarios();
        Button buttonMostrarProductos = (Button) view.findViewById(R.id.btnMostrarProductosTienda);
        Button buttonModificarFoto = (Button) view.findViewById(R.id.btnModificarFoto);
        Button buttonModificarDatos = (Button) view.findViewById(R.id.btnModificarDatosTienda);
        buttonPublicarAviso = (Button) view.findViewById(R.id.btnPublicarAviso);
        textViewNombrePrincipal = (TextView) view.findViewById(R.id.tvTituloTienda);
        textViewNombre = (TextView) view.findViewById(R.id.tvNameTiendaSelected);
        textViewDireccion = (TextView)view.findViewById(R.id.tvDireccion);
        textViewDescripcion = (TextView)view.findViewById(R.id.tvDesSelected);
        textViewHorario = (TextView) view.findViewById(R.id.tvHorarioSelected);
        imageViewTienda = (ImageView) view.findViewById(R.id.ivTiendaSelected);
        listViewComentarios = (ListView)view.findViewById(R.id.lvComentarios);

        textViewNombrePrincipal.setText(getResources().getString(R.string.titulo_tienda_seleccionada) + " " + nombre);
        textViewNombre.setText(Html.fromHtml("<b>"+getResources().getString(R.string.Tienda) + ": </b>" + nombre));
        textViewDescripcion.setText(Html.fromHtml("<b>"+getResources().getString(R.string.titulo_descripcion)+ ": </b>" + des));
        textViewDireccion.setText(Html.fromHtml("<b>"+getResources().getString(R.string.Direccion) + ": </b>" + calle + " #" + numero));
        Coordenadas.latitud = Double.parseDouble(latitud);
        Coordenadas.longitud = Double.parseDouble(longitud);

        final String openupdate = new MetodosCreados().HoraNormal(open_hour);
        final String closeupdate = new MetodosCreados().HoraNormal(close_hour);

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
            //textViewHorario.setText("De " + start_day + " a " + end_day + ", horario mañana desde las " + openupdate + " hasta las " + closeupdate + ", horario tarde desde las " + openupdatelunch + " hasta las " + closeupdatelunch);
        }
        imageViewTienda.setImageDrawable(new MetodosCreados().EncuadrarBitmap(imagenTienda, getResources()));

        buttonPublicarAviso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                tipo = "Notices";
                if (buttonPublicarAviso.getText().toString().equals(getResources().getString(R.string.Publicar_Aviso)))
                {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                    final EditText editTextCommentary = new EditText(getContext());
                    final NumberPicker numberPicker = new NumberPicker(getContext());
                    numberPicker.setMinValue(1);
                    numberPicker.setMaxValue(10);

                    LinearLayout layout = new LinearLayout(getContext());
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.addView(editTextCommentary);
                    layout.addView(numberPicker);

                    editTextCommentary.setHint("...");
                    builder.setTitle("Escriba su aviso...")
                            .setPositiveButton("Publicar aviso",
                                    new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            JSONObject object = new JSONObject();
                                            try
                                            {
                                                progress = new ProgressDialog(getContext());
                                                progress.setMessage("Agregando aviso...");
                                                progress.setCanceledOnTouchOutside(false);
                                                progress.show();

                                                object.put("requirements", editTextCommentary.getText().toString());
                                                object.put("vacants", numberPicker.getValue());
                                                object.put("store_id", store_id);
                                                object.put("type", "add");
                                                subTipo = "add";

                                                //noticesLocal.setNoticeId(noticesObject.getNoticeId());
                                                noticesLocal.setDateNotice("00-00-0000");
                                                noticesLocal.setRequirementsNotice(editTextCommentary.getText().toString().trim());
                                                noticesLocal.setVacantsNotice(numberPicker.getValue());

                                                new ModificarDatos().execute(getResources().getString(R.string.direccion_web) + "Controlador/InUpDelNotices.php", object.toString());
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
                    builder.setView(layout);
                    builder.show();
                }
                else
                {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                    final EditText editTextCommentary = new EditText(getContext());
                    final NumberPicker numberPicker = new NumberPicker(getContext());
                    numberPicker.setMinValue(1);
                    numberPicker.setMaxValue(10);

                    editTextCommentary.setText(noticesObject.getRequirementsNotice());

                    LinearLayout layout = new LinearLayout(getContext());
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.addView(editTextCommentary);
                    layout.addView(numberPicker);

                    editTextCommentary.setHint("...");
                    builder.setTitle("Modifique su aviso...")
                            .setPositiveButton("Modificar aviso",
                                    new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            JSONObject object = new JSONObject();
                                            try
                                            {
                                                progress = new ProgressDialog(getContext());
                                                progress.setMessage("Modificando aviso...");
                                                progress.setCanceledOnTouchOutside(false);
                                                progress.show();

                                                object.put("id", noticesObject.getNoticeId());
                                                object.put("requirements", editTextCommentary.getText().toString().trim());
                                                object.put("vacants", numberPicker.getValue());
                                                object.put("store_id", store_id);
                                                object.put("type", "update");
                                                subTipo = "update";


                                                Notices notices = new Notices();
                                                notices.setNoticeId(noticesObject.getNoticeId());
                                                notices.setDateNotice(noticesObject.getDateNotice());
                                                notices.setRequirementsNotice(editTextCommentary.getText().toString().trim());
                                                notices.setVacantsNotice(numberPicker.getValue());
                                                noticesObject = notices;

                                                new ModificarDatos().execute(getResources().getString(R.string.direccion_web) + "Controlador/InUpDelNotices.php", object.toString());
                                            }
                                            catch (JSONException e)
                                            {
                                                e.printStackTrace();
                                            }

                                        }
                                    })
                            .setNeutralButton(textEnabled, new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {

                                    try
                                    {
                                        if (textEnabled.equals("Habilitar aviso"))
                                        {
                                            progress = new ProgressDialog(getContext());
                                            progress.setMessage("Habilitando aviso...");
                                            progress.setCanceledOnTouchOutside(false);
                                            progress.show();

                                            JSONObject object = new JSONObject();
                                            object.put("id", noticesObject.getNoticeId());
                                            object.put("type", "updateEnabled");

                                            tipo = "Notices";
                                            subTipo = "deleteEnabled";

                                            textEnabled = "Deshabilitar aviso";
                                            new ModificarDatos().execute(getResources().getString(R.string.direccion_web) + "Controlador/InUpDelNotices.php", object.toString());
                                        }
                                        else
                                        {
                                            progress = new ProgressDialog(getContext());
                                            progress.setMessage("Deshabilitando aviso...");
                                            progress.setCanceledOnTouchOutside(false);
                                            progress.show();

                                            JSONObject object = new JSONObject();
                                            object.put("id", noticesObject.getNoticeId());
                                            object.put("type", "updateDisabled");

                                            tipo = "Notices";
                                            subTipo = "deleteDisabled";

                                            textEnabled = "Habilitar aviso";
                                            new ModificarDatos().execute(getResources().getString(R.string.direccion_web) + "Controlador/InUpDelNotices.php", object.toString());
                                        }

                                    } catch (JSONException e)
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

                    builder.setView(layout);
                    builder.show();
                }
            }
        });

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
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final CharSequence[] items = new CharSequence[3];
                items[0] = "Camara";
                items[1] = "Galeria";
                items[2] = "Cancelar";
                builder.setTitle("Elije una opción")
                        .setItems(items, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if (items[which].equals("Camara"))
                                {
                                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                                        if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))
                                        {
                                            //Aqui pregunta primero cuando los permidos no estan activados
                                            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_ACCESS_CAMERA);
                                        }
                                    }
                                    else
                                    {
                                        //Päsa aqui cuando los permisos estan activados
                                        tipo = "Imagen";
                                        dispatchTakePictureIntent();
                                    }
                                }
                                else if (items[which].equals("Galeria"))
                                {
                                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                        if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))
                                        {
                                            //Aqui pregunta primero cuando los permidos no estan activados
                                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
                                        }
                                    }
                                    else
                                    {
                                        //Päsa aqui cuando los permisos estan activados
                                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        intent.setType("image/*");
                                        startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), SELECT_PICTURE);
                                        tipo = "Imagen";
                                    }
                                }
                                else if (items[which].equals("Cancelar"))
                                {
                                    dialog.dismiss();
                                }
                            }
                        });
                builder.show();
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
                editTextNombreTienda = (EditText) vMod.findViewById(R.id.etTiendaMod);
                editTextDesTienda = (EditText) vMod.findViewById(R.id.etDesMod);
                editTextCalle = (EditText) vMod.findViewById(R.id.etCalleMod);
                editTextNum = (EditText) vMod.findViewById(R.id.etNumMod);

                LinearLayout linearLayoutUno = (LinearLayout) vMod.findViewById(R.id.llHorarioUno);
                LinearLayout linearLayoutDos = (LinearLayout) vMod.findViewById(R.id.llHorarioDos);
                TextView textViewTituloUno = (TextView) vMod.findViewById(R.id.tvTituloUno);
                TextView textViewTituloDos = (TextView) vMod.findViewById(R.id.tvTituloDos);

                textViewHora1 = (TextView) vMod.findViewById(R.id.tvHora1);
                textViewHora2 = (TextView) vMod.findViewById(R.id.tvHora2);
                textViewHora3 = (TextView) vMod.findViewById(R.id.tvHora3);
                textViewHora4 = (TextView) vMod.findViewById(R.id.tvHora4);

                Button buttonMapa = (Button)vMod.findViewById(R.id.btnModCoor);

                Spinner spinnerInicio = (Spinner)vMod.findViewById(R.id.spnInicio);
                Spinner spinnerFin = (Spinner)vMod.findViewById(R.id.spnFin);

                ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, dayWeek);
                spinnerInicio.setAdapter(arrayAdapter);
                spinnerFin.setAdapter(arrayAdapter);

                //MapView mapViewTienda = (MapView)vMod.findViewById(R.id.mpTienda);
                Button buttonModTienda = (Button) vMod.findViewById(R.id.btnModTienda);



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

                spinnerInicio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        dInicio = dayWeek[position];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {

                    }
                });
                spinnerFin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        dFin = dayWeek[position];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {

                    }
                });
                buttonMapa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        Toast.makeText(getContext(), "Indica tu posición en el mapa.", Toast.LENGTH_LONG).show();
                        if (googleMapGlobal != null)
                        {
                            googleMapGlobal.clear();
                        }
                        if (MapStatic.pMapUpdate == null)
                        {
                            MapStatic.pMapUpdate = getActivity().getLayoutInflater().inflate(R.layout.activity_maps_tienda, null);
                        }
                        if (MapStatic.pMapUpdate.getParent() != null)
                        {
                            ((ViewGroup)MapStatic.pMapUpdate.getParent()).removeView(MapStatic.pMapUpdate);
                        }
                        final AlertDialog.Builder builderMapa = new AlertDialog.Builder(getContext());
                        final SupportMapFragment map = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.mapFrag);

                        Button botonTomarCoor = (Button) MapStatic.pMapUpdate.findViewById(R.id.btnFijarMapaTienda);
                        botonTomarCoor.setVisibility(View.VISIBLE);
                        builderMapa.setView(MapStatic.pMapUpdate);
                        final AlertDialog mapUpdate = builderMapa.create();
                        mapUpdate.show();

                        map.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(final GoogleMap googleMap)
                            {
                                googleMapGlobal = googleMap;
                                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                    return;
                                }
                                googleMap.setMyLocationEnabled(true);
                                googleMap.getUiSettings().setZoomControlsEnabled(true);
                                LatLng latlng = googleMap.getProjection().getVisibleRegion().latLngBounds.getCenter();
                                googleMap.addMarker(new MarkerOptions().position(latlng).title("Marca"));
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
                                        LatLng latlng = googleMap.getProjection().getVisibleRegion().latLngBounds.getCenter();
                                        googleMap.addMarker(new MarkerOptions().position(latlng).title("Marca"));

                                        Coordenadas.latitud = googleMap.getCameraPosition().target.latitude;
                                        Coordenadas.longitud = googleMap.getCameraPosition().target.longitude;
                                    }
                                });
                            }
                        });
                        botonTomarCoor.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                //getFragmentManager().beginTransaction().remove(map).commit();
                                mapUpdate.dismiss();
                            }
                        });
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
                            object.put("Nombre", editTextNombreTienda.getText().toString().trim());
                            object.put("Descripcion", editTextDesTienda.getText().toString().trim());
                            object.put("Calle", editTextCalle.getText().toString().trim());
                            object.put("Numero", editTextNum.getText().toString().trim());
                            object.put("open_hour", textViewHora1.getText().toString());
                            object.put("close_hour", textViewHora2.getText().toString());
                            object.put("lunch_hour", textViewHora3.getText().toString());
                            object.put("lunch_after_hour", textViewHora4.getText().toString());
                            object.put("dayI", dInicio);
                            object.put("dayE", dFin);
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
    public void CargarComentarios()
    {
        try
        {
            progress = new ProgressDialog(getContext());
            progress.setMessage("Cargando Comentarios...");
            progress.setCanceledOnTouchOutside(false);
            progress.show();

            tipo = "Cargar comentarios";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("store_id", store_id);
            new ModificarDatos().execute(getResources().getString(R.string.direccion_web) + "Controlador/listaComentarios.php", jsonObject.toString());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
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
            imageViewTienda.setImageDrawable(new MetodosCreados().EncuadrarBitmap(imageBitmap, getResources()));
            String imagenGeneral = Codificacion.encodeToBase64(imageBitmap, Bitmap.CompressFormat.PNG, 100);
            JSONObject object = new JSONObject();
            try
            {
                object.put("Nombre", nombre);
                object.put("Imagen", imagenGeneral);
                tipo = "Imagen";
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            progress = new ProgressDialog(getContext());
            progress.setMessage("Modificando imagen...");
            progress.setCanceledOnTouchOutside(false);
            progress.show();
            new ModificarDatos().execute(getResources().getString(R.string.direccion_web) + "Controlador/modificarImagenTienda.php", object.toString());
        }
        else if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK)
        {
            Uri path = data.getData();
            try
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), path);
                imageViewTienda.setImageBitmap(Codificacion.bajarResolucion(bitmap, 500, 500));
                String imagenCod = Codificacion.encodeToBase64(Codificacion.bajarResolucion(bitmap, 500, 500), Bitmap.CompressFormat.PNG, 100);
                JSONObject object = new JSONObject();
                try
                {
                    object.put("Nombre", nombre);
                    object.put("Imagen", imagenCod);
                    tipo = "Imagen";
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                progress = new ProgressDialog(getContext());
                progress.setMessage("Modificando imagen...");
                progress.setCanceledOnTouchOutside(false);
                progress.show();
                new ModificarDatos().execute(getResources().getString(R.string.direccion_web) + "Controlador/modificarImagenTienda.php", object.toString());
                //imagenGeneral = Codificacion.encodeToBase64(rotado, Bitmap.CompressFormat.PNG, 100);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_ACCESS_CAMERA:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //Aceptado
                    tipo = "Imagen";
                    dispatchTakePictureIntent();

                }
                else
                {
                    //Negado
                    Toast.makeText(getContext(), "Debes aceptar los permisos para la camara.", Toast.LENGTH_SHORT).show();
                }
            }
            case WRITE_EXTERNAL_STORAGE:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //Aceptado
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), SELECT_PICTURE);
                    tipo = "Imagen";

                }
                else
                {
                    //Negado
                    Toast.makeText(getContext(), "Debes aceptar los permisos para la camara.", Toast.LENGTH_SHORT).show();
                }
            }
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

                        String openupdate = new MetodosCreados().HoraNormal(textViewHora1.getText().toString());
                        String closeupdate = new MetodosCreados().HoraNormal(textViewHora2.getText().toString());
                        textViewNombre.setText(Html.fromHtml("<b>"+getResources().getString(R.string.Tienda) + ": </b>" + editTextNombreTienda.getText().toString().trim()));
                        textViewDireccion.setText(Html.fromHtml("<b>"+getResources().getString(R.string.Direccion) + ": </b>" + editTextCalle.getText().toString().trim() + " #" + editTextNum.getText().toString().trim()));
                        textViewDescripcion.setText(Html.fromHtml("<b>"+getResources().getString(R.string.titulo_descripcion)+ ": </b>" + editTextDesTienda.getText().toString().trim()));
                        if (textViewHora3.getText().equals("00:00:00") && textViewHora4.getText().equals("00:00:00"))
                        {
                            textViewHorario.setText(Html.fromHtml("<b> <font color=\"blue\">"+dInicio + " a " + dFin+ "</font> <br>"+
                                    "Continuado: </b>" +  "de " + openupdate + " a " + closeupdate));
                        }
                        else
                        {
                            String openupdatelunch = new MetodosCreados().HoraNormal(textViewHora3.getText().toString());
                            String closeupdatelunch = new MetodosCreados().HoraNormal(textViewHora4.getText().toString());
                            textViewHorario.setText(Html.fromHtml("<b> <font color=\"blue\">"+dInicio + " a " + dFin+ "</font> <br>"+
                                    "Mañana: </b>" +  "de " + openupdate + " a " + closeupdate + "<br>"
                                    + "<b>Tarde: </b>de " + openupdatelunch + " a " + closeupdatelunch));
                        }
                    }
                    else if (res.equals("Existente"))
                    {
                        Toast.makeText(getContext(), "El nombre de la tienda ya esta en uso.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            else if (tipo.equals("Cargar comentarios"))
            {
                if (!s.equals("[]"))
                {
                    try
                    {
                         //1: Comentarios, 2: Diponibilidad de la postulacion
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
                                Notices notices = new Notices();
                                notices.setNoticeId(jsonObject.getInt("id"));
                                notices.setDateNotice(jsonObject.getString("date"));
                                notices.setRequirementsNotice(jsonObject.getString("requirements"));
                                notices.setVacantsNotice(jsonObject.getInt("available"));
                                noticesObject = notices;
                                buttonPublicarAviso.setText(getResources().getString(R.string.Modificar_Aviso));
                                if (jsonObject.getInt("available") == 1)
                                {
                                    textEnabled = "Deshabilitar aviso";
                                }
                                else
                                {
                                    textEnabled = "Habilitar aviso";
                                }
                            }
                            else if (isCommentary)
                            {
                                Comentarios comentarios = new Comentarios();
                                comentarios.setuNickname(jsonObject.getString("nickname"));
                                comentarios.setuComentario(jsonObject.getString("commentary"));
                                comentarios.setFechaCreacion(jsonObject.getString("date"));
                                comentariosList.add(comentarios);
                                buttonPublicarAviso.setText(getResources().getString(R.string.Publicar_Aviso));
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
                                            Notices notices = new Notices();
                                            notices.setNoticeId(jsonObjects.getInt("id"));
                                            notices.setDateNotice(jsonObjects.getString("date"));
                                            notices.setRequirementsNotice(jsonObjects.getString("requirements"));
                                            notices.setVacantsNotice(jsonObjects.getInt("available"));
                                            noticesObject = notices;
                                            buttonPublicarAviso.setText(getResources().getString(R.string.Modificar_Aviso));
                                            if (jsonObjects.getInt("available") == 1)
                                            {
                                                textEnabled = "Deshabilitar aviso";
                                            }
                                            else
                                            {
                                                textEnabled = "Habilitar aviso";
                                            }
                                        }
                                        else
                                        {
                                            buttonPublicarAviso.setText(getResources().getString(R.string.Publicar_Aviso));
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

                        progress.dismiss();
                        BaseAdapter baseAdapter = new BaseAdapter()
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
                        };
                        listViewComentarios.setAdapter(baseAdapter);

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }
                else
                {
                    progress.dismiss();
                }

            }
            else if (tipo.equals("Imagen"))
            {
                Toast.makeText(getContext(), "Imagen modificada.", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
            else if (tipo.equals("Notices"))
            {
                if (subTipo.equals("add"))
                {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        noticesLocal.setNoticeId(jsonObject.getInt("Id"));
                        noticesObject = noticesLocal;
                        Toast.makeText(getContext(), "Aviso publicado.", Toast.LENGTH_SHORT).show();
                        buttonPublicarAviso.setText(getResources().getString(R.string.Modificar_Aviso));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else if (subTipo.equals("update"))
                {
                    try
                    {
                        JSONObject jsonObject = new JSONObject(s);
                        nAvailable = jsonObject.getInt("Disponible");
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    Toast.makeText(getContext(), "Aviso actualizado.", Toast.LENGTH_SHORT).show();
                    buttonPublicarAviso.setText(getResources().getString(R.string.Modificar_Aviso));
                }
                else if (subTipo.equals("deleteDisabled"))
                {
                    try
                    {
                        JSONObject jsonObject = new JSONObject(s);
                        nAvailable = jsonObject.getInt("Disponible");
                        Toast.makeText(getContext(), "Aviso deshabilitado.", Toast.LENGTH_SHORT).show();
                        textEnabled = "Habilitar aviso";
                        buttonPublicarAviso.setText(getResources().getString(R.string.Publicar_Aviso));
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }
                else if (subTipo.equals("deleteEnabled"))
                {
                    try
                    {
                        JSONObject jsonObject = new JSONObject(s);
                        nAvailable = jsonObject.getInt("Disponible");
                        Toast.makeText(getContext(), "Aviso Habilitado.", Toast.LENGTH_SHORT).show();
                        textEnabled = "Deshabilitar aviso";
                        buttonPublicarAviso.setText(getResources().getString(R.string.Publicar_Aviso));
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                progress.dismiss();
            }
        }
    }
}
