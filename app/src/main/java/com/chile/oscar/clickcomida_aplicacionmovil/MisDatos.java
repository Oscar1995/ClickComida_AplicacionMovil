package com.chile.oscar.clickcomida_aplicacionmovil;


import android.Manifest;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Coordenadas;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.MetodosCreados;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MisDatos extends Fragment implements View.OnClickListener
{
    private static final int LOCATION_REQUEST_CODE = 1;
    TextView eNombre, eApellido, eNickname, eCorreo, eTipo, eTel1, eTel2, calle, numCalle, calleDos, numCalleDos, calleTres, numCalleTres;
    String id, tipoReg;
    View view;
    LinearLayout linearLayoutTelefono1, linearLayoutTelefono2, linearLayoutDir1, linearLayoutDir2, linearLayoutDir3;
    ImageView imageViewMisDatos, imageViewUpdateTel1, imageViewUpdateTel2, imageViewUpdateDir1, imageViewUpdateDir2, imageViewUpdateDir3, imageViewDeleteTel1, imageViewDeleteTel2, imageViewDeteleDir1, imageViewDeleteDir2, imageViewDeleteDir3;
    List<String> calleDir, numDir , vTelefonos;
    AlertDialog dialogAlertDatosUsuario, dialogAlertClave, dialogAlertTelefono1, dialogAlertTelefono2, dialogAlertAgregarTelefono, dialogAlertEliminarTelefono, dialogAlertEliminarTelefono2, dialogAlertUpdateDir1, dialogAlertUpdateDir2, dialogAlertUpdateDir3,
            dialogAlertDeleteDir1, dialogAlertDeleteDir2, dialogAlertDeleteDir3, dialogAlertAddDir1, dialogAlertAddDir2, dialogAlertAddDir3;
    EditText editTextClaveActual;
    Button buttonAgregarTelefonoNuevo, buttonAgregarDireccionNueva, buttonUsar1, buttonUsar2, buttonUsar3;
    Boolean mapReady;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.activity_mis_datos, container, false);

        eNombre = (TextView) view.findViewById(R.id.txtNombre_misdatos);
        eApellido = (TextView) view.findViewById(R.id.txtApellido_misdatos);
        eNickname = (TextView) view.findViewById(R.id.txtNickname_misdatos);
        eCorreo = (TextView) view.findViewById(R.id.txtCorreo_misdatos);

        eTipo = (TextView) view.findViewById(R.id.txtTipo_misdatos);
        eTel1 = (TextView) view.findViewById(R.id.txtTeleUno_misdatos);
        eTel2 = (TextView) view.findViewById(R.id.txtTeleDos_misdatos);

        calle = (TextView) view.findViewById(R.id.txtCalle_misdatos);
        numCalle = (TextView) view.findViewById(R.id.txtNumeroCalle_misdatos);
        calleDos = (TextView) view.findViewById(R.id.txtCalle_misdatos_d);
        numCalleDos = (TextView) view.findViewById(R.id.txtNumeros_misdatos_d);
        calleTres = (TextView) view.findViewById(R.id.txtCallemisdatos_t);
        numCalleTres = (TextView) view.findViewById(R.id.txtNumeromisdatos_t);

        buttonAgregarTelefonoNuevo = (Button)view.findViewById(R.id.btnAgregarNuevoTelefono); //Listo
        buttonAgregarDireccionNueva = (Button)view.findViewById(R.id.btnAgregarNuevaDireccion);

        buttonUsar1 = (Button)view.findViewById(R.id.btnUsarUno);
        buttonUsar2 = (Button)view.findViewById(R.id.btnUsarDos);
        buttonUsar3 = (Button)view.findViewById(R.id.btnUsarTres);

        linearLayoutTelefono1 = (LinearLayout)view.findViewById(R.id.llTelefono1);
        linearLayoutTelefono2 = (LinearLayout)view.findViewById(R.id.llTelefono2);
        linearLayoutDir1 = (LinearLayout)view.findViewById(R.id.llDireccion1);
        linearLayoutDir2 = (LinearLayout)view.findViewById(R.id.llDireccion2);
        linearLayoutDir3 = (LinearLayout)view.findViewById(R.id.llDireccion3);

        imageViewMisDatos = (ImageView)view.findViewById(R.id.ivActualizarDatos);
        imageViewUpdateTel1 = (ImageView)view.findViewById(R.id.ivUpdateTel1); //Listo
        imageViewUpdateTel2 = (ImageView)view.findViewById(R.id.ivUpdateTel2); //Listo
        imageViewUpdateDir1 = (ImageView)view.findViewById(R.id.ivUpdateDir1); //Listo
        imageViewUpdateDir2 = (ImageView)view.findViewById(R.id.ivUpdateDir2); //Listo
        imageViewUpdateDir3 = (ImageView)view.findViewById(R.id.ivUpdateDir3); //Listo
        imageViewDeleteTel1 = (ImageView)view.findViewById(R.id.ivDeleteTel1); //Listo
        imageViewDeleteTel2 = (ImageView)view.findViewById(R.id.ivDeleteTel2); //Listo
        imageViewDeteleDir1 = (ImageView)view.findViewById(R.id.ivDeleteDir1); //Listo
        imageViewDeleteDir2 = (ImageView)view.findViewById(R.id.ivDeleteDir2); //Listo
        imageViewDeleteDir3 = (ImageView)view.findViewById(R.id.ivDeleteDir3); //Listo


        buttonAgregarTelefonoNuevo.setOnClickListener(this);
        buttonAgregarDireccionNueva.setOnClickListener(this);

        buttonUsar1.setOnClickListener(this);
        buttonUsar2.setOnClickListener(this);
        buttonUsar3.setOnClickListener(this);

        imageViewMisDatos.setOnClickListener(this);
        imageViewUpdateTel1.setOnClickListener(this);
        imageViewUpdateTel2.setOnClickListener(this);
        imageViewUpdateDir1.setOnClickListener(this);
        imageViewUpdateDir2.setOnClickListener(this);
        imageViewUpdateDir3.setOnClickListener(this);
        imageViewDeleteTel1.setOnClickListener(this);
        imageViewDeleteTel2.setOnClickListener(this);
        imageViewDeteleDir1.setOnClickListener(this);
        imageViewDeleteDir2.setOnClickListener(this);
        imageViewDeleteDir3.setOnClickListener(this);

        Bundle bundle = getArguments();
        id = bundle.getString("IdUser");

        cargar();

        return view;
    }

    public void cargar()
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("id", id);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        tipoReg = "Traer Datos";
        new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/datos_usuario.php", jsonObject.toString());
        cargarDefecto();
    }
    public void cargarDefecto()
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("id", id);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/cargarBotones.php", jsonObject.toString());
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnUsarUno:
                try
                {
                    JSONObject object = new JSONObject();
                    object.put("calle", calleDir.get(0));
                    object.put("numero", numDir.get(0));
                    object.put("user_id", Coordenadas.id);
                    tipoReg = "Definir";
                    new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/direccionDefecto.php", object.toString());
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                break;
            case R.id.btnUsarDos:
                try
                {
                    JSONObject object = new JSONObject();
                    object.put("calle", calleDir.get(1));
                    object.put("numero", numDir.get(1));
                    object.put("user_id", Coordenadas.id);
                    tipoReg = "Definir";
                    new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/direccionDefecto.php", object.toString());
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                break;
            case R.id.btnUsarTres:
                try
                {
                    JSONObject object = new JSONObject();
                    object.put("calle", calleDir.get(2));
                    object.put("numero", numDir.get(2));
                    object.put("user_id", Coordenadas.id);
                    tipoReg = "Definir";
                    new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/direccionDefecto.php", object.toString());
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                break;
            case R.id.btnAgregarNuevaDireccion:
                if (calleDir.size() != 3)
                {
                    AlertDialog.Builder builderAddDir1 = new AlertDialog.Builder(getContext());
                    View pNuevaDir1= getActivity().getLayoutInflater().inflate(R.layout.agregar_direccion_usuario, null);
                    builderAddDir1.setView(pNuevaDir1);
                    dialogAlertAddDir1 = builderAddDir1.create();
                    dialogAlertAddDir1.show();

                    final EditText CalleAdd1 = (EditText)pNuevaDir1.findViewById(R.id.txtCalle_usuario_dir);
                    final EditText CalleNum1 = (EditText)pNuevaDir1.findViewById(R.id.txtNumero_usuario_dir);

                    Button addDirMap1 = (Button)pNuevaDir1.findViewById(R.id.btnAddDireccionCoor);
                    Button addDir1 = (Button)pNuevaDir1.findViewById(R.id.btnModificarDireccion);
                    final Button canDir1 = (Button)pNuevaDir1.findViewById(R.id.btnCancelarDireccion);

                    mapReady = false;

                    addDir1.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            if (!CalleAdd1.getText().toString().isEmpty() && !CalleNum1.getText().toString().isEmpty())
                            {
                                if (mapReady)
                                {
                                    JSONObject object = new JSONObject();
                                    Boolean dirExist = false;
                                    try
                                    {
                                        for (int i=0; i<calleDir.size(); i++)
                                        {
                                            if (calleDir.get(i).toString().equals(CalleAdd1.getText().toString()) && numDir.get(i).toString().equals(CalleNum1.getText().toString()))
                                            {
                                                dirExist = true;
                                            }
                                        }
                                        if (dirExist == false)
                                        {
                                            object.put("Id", id);
                                            object.put("Calle", CalleAdd1.getText().toString().trim());
                                            object.put("Numero", CalleNum1.getText().toString().trim());
                                            object.put("Latitud", Coordenadas.latitud);
                                            object.put("Longitud", Coordenadas.longitud);

                                            tipoReg = "Agregar Direccion 1";
                                            new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/agregar_direccion_usuario.php", object.toString());
                                        }
                                        else
                                        {
                                            Toast.makeText(getContext(), "La calle y el numero de la direccion ya lo tienes registrado.", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                    catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "Debes elegir la posicion en el mapa.", Toast.LENGTH_SHORT).show();
                                }

                            }
                            else
                            {
                                Toast.makeText(getContext(), "Debes completar los campos requeridos", Toast.LENGTH_SHORT).show();
                                if (CalleAdd1.getText().toString().isEmpty())
                                {
                                    CalleAdd1.setError("Debes completar este campo.");
                                }
                                if (CalleNum1.getText().toString().isEmpty())
                                {
                                    CalleNum1.setError("Debes completar este campo.");
                                }
                            }
                        }
                    });
                    addDirMap1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            Toast.makeText(getContext(), "Indica tu posición en el mapa.", Toast.LENGTH_LONG).show();
                            final AlertDialog.Builder builderMapa = new AlertDialog.Builder(getContext());
                            View pMap = getActivity().getLayoutInflater().inflate(R.layout.activity_maps_tienda, null);

                            final SupportMapFragment map = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.mapFrag);

                            Button botonTomarCoor = (Button) pMap.findViewById(R.id.btnFijarMapaTienda);
                            builderMapa.setView(pMap);
                            final AlertDialog mapUpdate = builderMapa.create();
                            mapUpdate.show();


                            map.getMapAsync(new OnMapReadyCallback() {
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
                                    googleMap.setMyLocationEnabled(true);
                                    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                        @Override
                                        public void onMapClick(LatLng latLng)
                                        {
                                            googleMap.clear();
                                            //LatLng posicionLocal = new LatLng(latLng.latitude, latLng.longitude);
                                            googleMap.addMarker(new MarkerOptions().position(latLng).title("Marca"));
                                            Coordenadas.latitud = latLng.latitude;
                                            Coordenadas.longitud = latLng.longitude;
                                            mapReady = true;
                                        }
                                    });
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
                    canDir1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            dialogAlertAddDir1.dismiss();
                        }
                    });
                }
                else
                {
                    Toast.makeText(getContext(), "Solo puede agregar hasta 3 direcciones, puedes modificar o eliminar una direccion que ya tienes registrada.", Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.btnAgregarNuevoTelefono:

                if (vTelefonos.size() != 2)
                {
                    AlertDialog.Builder builderAddTelefono = new AlertDialog.Builder(getContext());
                    View pNuevaTelefono = getActivity().getLayoutInflater().inflate(R.layout.agregar_telefono_usuario, null);
                    builderAddTelefono.setView(pNuevaTelefono);
                    dialogAlertAgregarTelefono = builderAddTelefono.create();
                    dialogAlertAgregarTelefono.show();

                    final EditText editTextTelefonoNuevo = (EditText)pNuevaTelefono.findViewById(R.id.txtTelefono_us);
                    Button buttonAgregar = (Button)pNuevaTelefono.findViewById(R.id.btnAgregarTelefono);
                    Button buttonCerrarNuevo = (Button)pNuevaTelefono.findViewById(R.id.btnCancelarTelefono);

                    buttonAgregar.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            if (editTextTelefonoNuevo.getText().toString().length() >= 6)
                            {
                                JSONObject object = new JSONObject();
                                try
                                {
                                    if (!vTelefonos.contains(editTextTelefonoNuevo.getText().toString()))
                                    {
                                        object.put("Id", id);
                                        object.put("Nuevo", editTextTelefonoNuevo.getText().toString());
                                        tipoReg = "Agregar Telefono";
                                        new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/insertar_datos_usuario.php", object.toString());
                                    }
                                    else
                                    {
                                        Toast.makeText(getContext(), "El telefono " + editTextTelefonoNuevo.getText().toString() + ", ya lo tienes registrado.", Toast.LENGTH_LONG).show();
                                    }

                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                Toast.makeText(getContext(), "El telefono debe tener mayor o igual a 6 digitos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    buttonCerrarNuevo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            dialogAlertAgregarTelefono.dismiss();
                        }
                    });
                }
                else
                {
                    Toast.makeText(getContext(), "Solo puedes agregar hasta dos telefonos, elimina o modifica uno.", Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.ivDeleteTel1:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Eliminar")
                        .setMessage("¿Seguro que deseas eliminar este telefono?")
                        .setPositiveButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        JSONObject object = new JSONObject();
                                        try
                                        {
                                            object.put("Id", id);
                                            object.put("Telefono", new MetodosCreados().quitarDosPuntos(eTel1.getText().toString()));
                                            tipoReg = "Eliminar Telefono 1";
                                            new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/eliminar_datos_usuario.php", object.toString());

                                        }
                                        catch (JSONException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                        .setNegativeButton("Cancelar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        dialog.dismiss();
                                    }
                                });
                builder.show();
                break;
            case R.id.ivDeleteTel2:
                AlertDialog.Builder builderDelTel = new AlertDialog.Builder(getActivity());
                builderDelTel.setTitle("Eliminar")
                        .setMessage("¿Seguro que deseas eliminar este telefono?")
                        .setPositiveButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        JSONObject object = new JSONObject();
                                        try
                                        {
                                            object.put("Id", id);
                                            object.put("Telefono", new MetodosCreados().quitarDosPuntos(eTel2.getText().toString()));
                                            tipoReg = "Eliminar Telefono 2";
                                            new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/eliminar_datos_usuario.php", object.toString());

                                        }
                                        catch (JSONException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                        .setNegativeButton("Cancelar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        dialog.dismiss();
                                    }
                                });
                builderDelTel.show();
                break;

            case R.id.ivActualizarDatos:
                AlertDialog.Builder builderChange = new AlertDialog.Builder(getContext());
                View p = getActivity().getLayoutInflater().inflate(R.layout.misdatos_general, null);
                builderChange.setView(p);
                dialogAlertDatosUsuario = builderChange.create();
                dialogAlertDatosUsuario.show();

                final EditText editTextNombre = (EditText)p.findViewById(R.id.etNombreUsuario);
                final EditText editTextApellido = (EditText)p.findViewById(R.id.etApellidoUsuario);
                Button buttonModificar = (Button)p.findViewById(R.id.btnModificarDatosActual);
                Button buttonCambiarClave = (Button)p.findViewById(R.id.btnCambiarClaveUser);
                Button buttonCancelar= (Button)p.findViewById(R.id.btnCancelarDatos);

                editTextNombre.setText(new MetodosCreados().quitarDosPuntos(eNombre.getText().toString()));
                editTextApellido.setText(new MetodosCreados().quitarDosPuntos(eApellido.getText().toString()));

                buttonModificar.setOnClickListener(new View.OnClickListener() //Listo
                {
                    @Override
                    public void onClick(View v)
                    {
                        Boolean us = false;
                        Boolean apUs = false;
                        if (!editTextNombre.getText().toString().isEmpty() && !editTextApellido.getText().toString().isEmpty())
                        {
                            JSONObject object = new JSONObject();
                            try
                            {
                                object.put("Id", id);
                                if (new MetodosCreados().ComprobarCampos(editTextNombre.getText().toString(), eNombre.getText().toString()) != null)
                                {
                                    object.put("Nombre", editTextNombre.getText().toString().trim());
                                    us = true;
                                }
                                if (new MetodosCreados().ComprobarCampos(editTextApellido.getText().toString(), eApellido.getText().toString()) != null)
                                {
                                    object.put("Apellido", editTextApellido.getText().toString().trim());
                                    apUs = true;
                                }
                                if (us  || apUs )
                                {
                                    tipoReg = "Modificar Nombre";
                                    new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/modificar_datos_actuales_usuario.php", object.toString());
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "No hay nada que Modificar", Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Debes tener un nombre y/o apellido", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                buttonCambiarClave.setOnClickListener(new View.OnClickListener() //Listo
                {
                    @Override
                    public void onClick(View v)
                    {
                        AlertDialog.Builder builderChange = new AlertDialog.Builder(getContext());
                        View pClave = getActivity().getLayoutInflater().inflate(R.layout.modificar_clave_usuario, null);
                        builderChange.setView(pClave);
                        dialogAlertClave = builderChange.create();
                        dialogAlertClave.show();

                        editTextClaveActual = (EditText)pClave.findViewById(R.id.txtClaveActual_us);
                        final EditText editTextClaveNueva = (EditText)pClave.findViewById(R.id.txtClaveNueva_us);
                        final EditText editTextClaveNuevaRep = (EditText)pClave.findViewById(R.id.txtClaveNuevaRep_us);
                        Button buttonModificarClave = (Button)pClave.findViewById(R.id.btnCambiarClave);
                        Button buttonCancelarClave = (Button)pClave.findViewById(R.id.btnCancelarClave);

                        buttonModificarClave.setOnClickListener(new View.OnClickListener() //Listo
                        {
                            @Override
                            public void onClick(View v)
                            {
                                if (!editTextClaveActual.getText().toString().isEmpty() && !editTextClaveNueva.getText().toString().isEmpty() && !editTextClaveNuevaRep.getText().toString().isEmpty())
                                {
                                    JSONObject object = new JSONObject();
                                    try
                                    {
                                        boolean c1 = false, c2 = false, c3 = false;
                                        if (editTextClaveNueva.getText().toString().equals(editTextClaveNuevaRep.getText().toString()))
                                        {
                                            if (editTextClaveActual.getText().toString().length() >= 5)c1 = true; else c1 = false;
                                            if (editTextClaveNueva.getText().toString().length() >= 5)c2 = true; else c2 = false;
                                            if (editTextClaveNuevaRep.getText().toString().length() >= 5)c3 = true; else c3 = false;

                                            if (c1 && c2 && c3)
                                            {
                                                tipoReg = "Clave";
                                                object.put("Id", id);
                                                object.put("ClaveActual", editTextClaveActual.getText().toString());
                                                object.put("ClaveNueva", editTextClaveNueva.getText().toString());
                                                new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/actualizar_datos_usuario.php", object.toString());
                                            }
                                            else
                                            {
                                                if (c1 == false)
                                                {
                                                    editTextClaveActual.setError("La clave debe ser mayor o igual a 5");
                                                }
                                                if (c2 == false)
                                                {
                                                    editTextClaveNueva.setError("La clave debe ser mayor o igual a 5");
                                                }
                                                if (c3 == false)
                                                {
                                                    editTextClaveNuevaRep.setError("La clave debe ser mayor o igual a 5");
                                                }
                                            }
                                        }
                                        else
                                        {
                                            editTextClaveNueva.setError("La clave no combina.");
                                            editTextClaveNuevaRep.setError("La clave no combina.");
                                            Toast.makeText(getContext(), "La clave no combinan", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                    catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "Debes rellenar todos los campos.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        buttonCancelarClave.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                dialogAlertClave.dismiss();
                            }
                        });
                    }
                });
                buttonCancelar.setOnClickListener(new View.OnClickListener() //Listo
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialogAlertDatosUsuario.dismiss();
                    }
                });

                break;

            case R.id.ivUpdateTel1: //Actualizar Telefono 1
                AlertDialog.Builder builderUpdateTel1 = new AlertDialog.Builder(getContext());
                View pTelefono = getActivity().getLayoutInflater().inflate(R.layout.modificar_telefono, null);
                builderUpdateTel1.setView(pTelefono);
                dialogAlertTelefono1 = builderUpdateTel1.create();
                dialogAlertTelefono1.show();

                final EditText editTextTelefono = (EditText)pTelefono.findViewById(R.id.etNuevoTelefonp_us);
                Button buttonModTel = (Button)pTelefono.findViewById(R.id.btnModificarTelefono_us);
                Button buttonCerrar = (Button)pTelefono.findViewById(R.id.btnCerrarTelefono_us);

                buttonModTel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        if (editTextTelefono.getText().toString().length()>=6)
                        {
                            JSONObject object = new JSONObject();
                            try
                            {
                                if (!vTelefonos.contains(editTextTelefono.getText().toString()))
                                {
                                    object.put("Id", id);
                                    object.put("Antiguo", new MetodosCreados().quitarDosPuntos(eTel1.getText().toString()));
                                    object.put("Nuevo", editTextTelefono.getText().toString());
                                    tipoReg = "Modificar Telefono";
                                    new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/actualizar_telefono_usuario.php", object.toString());
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "El telefono " + editTextTelefono.getText().toString() + " ya lo tienes registrado", Toast.LENGTH_SHORT).show();
                                }

                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            editTextTelefono.setError("El telefono deber ser mayor o igual a 6 digitos");
                            Toast.makeText(getContext(), "El telefono deber ser mayor o igual a 6 digitos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                buttonCerrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogAlertTelefono1.dismiss();
                    }
                });
                break;
            case R.id.ivUpdateTel2: //Actualizar Telefono 1
                AlertDialog.Builder builderUpdateTel2 = new AlertDialog.Builder(getContext());
                View pTelefono2 = getActivity().getLayoutInflater().inflate(R.layout.modificar_telefono, null);
                builderUpdateTel2.setView(pTelefono2);
                dialogAlertTelefono2 = builderUpdateTel2.create();
                dialogAlertTelefono2.show();

                final EditText editTextTelefono2 = (EditText)pTelefono2.findViewById(R.id.etNuevoTelefonp_us);
                Button buttonModTel2 = (Button)pTelefono2.findViewById(R.id.btnModificarTelefono_us);
                Button buttonCerrar2 = (Button)pTelefono2.findViewById(R.id.btnCerrarTelefono_us);

                buttonModTel2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        if (editTextTelefono2.getText().toString().length()>=6)
                        {
                            JSONObject object = new JSONObject();
                            try
                            {
                                if (!vTelefonos.contains(editTextTelefono2.getText().toString()))
                                {
                                    object.put("Id", id);
                                    object.put("Antiguo", new MetodosCreados().quitarDosPuntos(eTel2.getText().toString()));
                                    object.put("Nuevo", editTextTelefono2.getText().toString());
                                    tipoReg = "Modificar Telefono 2";
                                    new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/actualizar_telefono_usuario.php", object.toString());
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "El telefono " + editTextTelefono2.getText().toString() + " ya lo tienes registrado", Toast.LENGTH_SHORT).show();
                                }

                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            editTextTelefono2.setError("El telefono deber ser mayor o igual a 6 digitos");
                            Toast.makeText(getContext(), "El telefono deber ser mayor o igual a 6 digitos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                buttonCerrar2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogAlertTelefono2.dismiss();
                    }
                });
                break;

            case R.id.ivUpdateDir1:
                AlertDialog.Builder builderUpdateDir1 = new AlertDialog.Builder(getContext());
                View pDir1 = getActivity().getLayoutInflater().inflate(R.layout.modificar_direccion_usuario, null);
                builderUpdateDir1.setView(pDir1);
                dialogAlertUpdateDir1 = builderUpdateDir1.create();
                dialogAlertUpdateDir1.show();

                final EditText editTextCalle1 = (EditText)pDir1.findViewById(R.id.eNuevaCalle);
                final EditText editTextNumero1 = (EditText)pDir1.findViewById(R.id.eNuevoNumeroCalle);

                Button buttonAbrirMapa1 = (Button)pDir1.findViewById(R.id.btnAbrirMapa);
                Button buttonModificar1 = (Button)pDir1.findViewById(R.id.btnModificarDireccion_us);
                Button buttonCerrar1 = (Button)pDir1.findViewById(R.id.btnCerrarDireccion_us);

                mapReady = false;


                buttonAbrirMapa1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        Toast.makeText(getContext(), "Indica tu posición en el mapa.", Toast.LENGTH_LONG).show();
                        final AlertDialog.Builder builderMapa = new AlertDialog.Builder(getContext());
                        View pMap = getActivity().getLayoutInflater().inflate(R.layout.activity_maps_tienda, null);

                        final SupportMapFragment map = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.mapFrag);

                        Button botonTomarCoor = (Button) pMap.findViewById(R.id.btnFijarMapaTienda);
                        builderMapa.setView(pMap);
                        final AlertDialog mapUpdate = builderMapa.create();
                        mapUpdate.show();


                        map.getMapAsync(new OnMapReadyCallback() {
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
                                googleMap.setMyLocationEnabled(true);
                                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                    @Override
                                    public void onMapClick(LatLng latLng)
                                    {
                                        googleMap.clear();
                                        //LatLng posicionLocal = new LatLng(latLng.latitude, latLng.longitude);
                                        googleMap.addMarker(new MarkerOptions().position(latLng).title("Marca"));
                                        Coordenadas.latitud = latLng.latitude;
                                        Coordenadas.longitud = latLng.longitude;
                                        mapReady = true;
                                    }
                                });
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
                buttonModificar1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        if (!editTextCalle1.getText().toString().isEmpty() && !editTextNumero1.getText().toString().isEmpty())
                        {
                            if (mapReady)
                            {
                                JSONObject object = new JSONObject();
                                try
                                {

                                    object.put("CalleAntigua", new MetodosCreados().quitarDosPuntos(calle.getText().toString()));
                                    object.put("Calle", editTextCalle1.getText().toString().trim());
                                    object.put("NumeroAntiguo", new MetodosCreados().quitarDosPuntos(numCalle.getText().toString()));
                                    object.put("Numero", editTextNumero1.getText().toString().trim());
                                    object.put("Latitud", Coordenadas.latitud);
                                    object.put("Longitud", Coordenadas.longitud);

                                    tipoReg = "Modificar direccion 1";
                                    new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/actualizar_direccion_usuario.php", object.toString());

                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Debes elegir la posicion en el mapa.", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                        {
                            Toast.makeText(getContext(), "Debes completar los campos requeridos.", Toast.LENGTH_SHORT).show();
                            if (editTextCalle1.getText().toString().isEmpty())
                            {
                                editTextCalle1.setError("Debes completar este campo.");
                            }
                            if (editTextNumero1.getText().toString().isEmpty())
                            {
                                editTextNumero1.setError("Debes completar este campo.");
                            }
                        }
                    }
                });
                buttonCerrar1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogAlertUpdateDir1.dismiss();
                    }
                });
                break;
            case R.id.ivUpdateDir2:
                AlertDialog.Builder builderUpdateDir2 = new AlertDialog.Builder(getContext());
                View pDir2 = getActivity().getLayoutInflater().inflate(R.layout.modificar_direccion_usuario, null);
                builderUpdateDir2.setView(pDir2);
                dialogAlertUpdateDir2 = builderUpdateDir2.create();
                dialogAlertUpdateDir2.show();

                final EditText editTextCalle2 = (EditText)pDir2.findViewById(R.id.eNuevaCalle);
                final EditText editTextNumero2 = (EditText)pDir2.findViewById(R.id.eNuevoNumeroCalle);

                Button buttonAbrirMapa2 = (Button)pDir2.findViewById(R.id.btnAbrirMapa);
                Button buttonModificar2 = (Button)pDir2.findViewById(R.id.btnModificarDireccion_us);
                Button buttonCerrarUp2 = (Button)pDir2.findViewById(R.id.btnCerrarDireccion_us);

                mapReady = false;


                buttonAbrirMapa2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        Toast.makeText(getContext(), "Indica tu posición en el mapa.", Toast.LENGTH_LONG).show();
                        final AlertDialog.Builder builderMapa = new AlertDialog.Builder(getContext());
                        View pMap2 = getActivity().getLayoutInflater().inflate(R.layout.activity_maps_tienda, null);

                        final SupportMapFragment map2 = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.mapFrag);

                        Button botonTomarCoor = (Button) pMap2.findViewById(R.id.btnFijarMapaTienda);
                        builderMapa.setView(pMap2);
                        final AlertDialog mapUpdate = builderMapa.create();
                        mapUpdate.show();


                        map2.getMapAsync(new OnMapReadyCallback() {
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
                                googleMap.setMyLocationEnabled(true);
                                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                    @Override
                                    public void onMapClick(LatLng latLng)
                                    {
                                        googleMap.clear();
                                        //LatLng posicionLocal = new LatLng(latLng.latitude, latLng.longitude);
                                        googleMap.addMarker(new MarkerOptions().position(latLng).title("Marca"));
                                        Coordenadas.latitud = latLng.latitude;
                                        Coordenadas.longitud = latLng.longitude;
                                        mapReady = true;
                                    }
                                });
                            }
                        });
                        botonTomarCoor.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                getFragmentManager().beginTransaction().remove(map2).commit();
                                mapUpdate.dismiss();
                            }
                        });
                    }
                });
                buttonModificar2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        if (!editTextCalle2.getText().toString().isEmpty() && !editTextNumero2.getText().toString().isEmpty())
                        {
                            if (mapReady)
                            {
                                JSONObject object = new JSONObject();
                                try
                                {
                                    object.put("CalleAntigua", new MetodosCreados().quitarDosPuntos(calleDos.getText().toString()));
                                    object.put("Calle", editTextCalle2.getText().toString().trim());
                                    object.put("NumeroAntiguo", new MetodosCreados().quitarDosPuntos(numCalleDos.getText().toString()));
                                    object.put("Numero", editTextNumero2.getText().toString().trim());
                                    object.put("Latitud", Coordenadas.latitud);
                                    object.put("Longitud", Coordenadas.longitud);

                                    tipoReg = "Modificar direccion 2";
                                    new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/actualizar_direccion_usuario.php", object.toString());
                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Debes elegir la posicion en el mapa.", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                        {
                            Toast.makeText(getContext(), "Debes completar los campos requeridos.", Toast.LENGTH_SHORT).show();
                            if (editTextCalle2.getText().toString().isEmpty())
                            {
                                editTextCalle2.setError("Debes completar este campo.");
                            }
                            if (editTextNumero2.getText().toString().isEmpty())
                            {
                                editTextNumero2.setError("Debes completar este campo.");
                            }
                        }
                    }
                });
                buttonCerrarUp2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogAlertUpdateDir2.dismiss();
                    }
                });
                break;
            case R.id.ivUpdateDir3:
                AlertDialog.Builder builderUpdateDir3 = new AlertDialog.Builder(getContext());
                View pDir3 = getActivity().getLayoutInflater().inflate(R.layout.modificar_direccion_usuario, null);
                builderUpdateDir3.setView(pDir3);
                dialogAlertUpdateDir3 = builderUpdateDir3.create();
                dialogAlertUpdateDir3.show();

                final EditText editTextCalle3 = (EditText)pDir3.findViewById(R.id.eNuevaCalle);
                final EditText editTextNumero3 = (EditText)pDir3.findViewById(R.id.eNuevoNumeroCalle);

                Button buttonAbrirMapa3 = (Button)pDir3.findViewById(R.id.btnAbrirMapa);
                Button buttonModificar3 = (Button)pDir3.findViewById(R.id.btnModificarDireccion_us);
                Button buttonCerrarUp3 = (Button)pDir3.findViewById(R.id.btnCerrarDireccion_us);

                mapReady = false;


                buttonAbrirMapa3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        Toast.makeText(getContext(), "Indica tu posición en el mapa.", Toast.LENGTH_LONG).show();
                        final AlertDialog.Builder builderMapa = new AlertDialog.Builder(getContext());
                        View pMap2 = getActivity().getLayoutInflater().inflate(R.layout.activity_maps_tienda, null);

                        final SupportMapFragment map2 = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.mapFrag);

                        Button botonTomarCoor = (Button) pMap2.findViewById(R.id.btnFijarMapaTienda);
                        builderMapa.setView(pMap2);
                        final AlertDialog mapUpdate = builderMapa.create();
                        mapUpdate.show();


                        map2.getMapAsync(new OnMapReadyCallback() {
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
                                googleMap.setMyLocationEnabled(true);
                                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                    @Override
                                    public void onMapClick(LatLng latLng)
                                    {
                                        googleMap.clear();
                                        //LatLng posicionLocal = new LatLng(latLng.latitude, latLng.longitude);
                                        googleMap.addMarker(new MarkerOptions().position(latLng).title("Marca"));
                                        Coordenadas.latitud = latLng.latitude;
                                        Coordenadas.longitud = latLng.longitude;
                                        mapReady = true;
                                    }
                                });
                            }
                        });
                        botonTomarCoor.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                getFragmentManager().beginTransaction().remove(map2).commit();
                                mapUpdate.dismiss();
                            }
                        });
                    }
                });
                buttonModificar3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        if (!editTextCalle3.getText().toString().isEmpty() && !editTextNumero3.getText().toString().isEmpty())
                        {
                            if (mapReady)
                            {
                                JSONObject object = new JSONObject();
                                try
                                {
                                    object.put("CalleAntigua", new MetodosCreados().quitarDosPuntos(calleTres.getText().toString()));
                                    object.put("Calle", editTextCalle3.getText().toString().trim());
                                    object.put("NumeroAntiguo", new MetodosCreados().quitarDosPuntos(numCalleTres.getText().toString()));
                                    object.put("Numero", editTextNumero3.getText().toString().trim());
                                    object.put("Latitud", Coordenadas.latitud);
                                    object.put("Longitud", Coordenadas.longitud);

                                    tipoReg = "Modificar direccion 3";
                                    new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/actualizar_direccion_usuario.php", object.toString());
                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Debes elegir la posicion en el mapa.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Debes completar los campos requeridos.", Toast.LENGTH_SHORT).show();
                            if (editTextCalle3.getText().toString().isEmpty())
                            {
                                editTextCalle3.setError("Debes completar este campo.");
                            }
                            if (editTextNumero3.getText().toString().isEmpty())
                            {
                                editTextNumero3.setError("Debes completar este campo.");
                            }
                        }
                    }
                });
                buttonCerrarUp3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogAlertUpdateDir3.dismiss();
                    }
                });
                break;

            case R.id.ivDeleteDir1:
                AlertDialog.Builder builderDeleteDir1 = new AlertDialog.Builder(getContext());
                View pDirDelete1 = getActivity().getLayoutInflater().inflate(R.layout.eliminar_telefono_o_direccion, null);
                builderDeleteDir1.setView(pDirDelete1);
                dialogAlertDeleteDir1 = builderDeleteDir1.create();
                dialogAlertDeleteDir1.show();

                Button buttonEliminarDir1 = (Button) pDirDelete1.findViewById(R.id.btnEliminar_d_f);
                Button buttonCerrarDir1 = (Button) pDirDelete1.findViewById(R.id.btnCancelar_d_f);

                buttonEliminarDir1.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        JSONObject object = new JSONObject();
                        try
                        {
                            object.put("Id", id);
                            object.put("Calle", new MetodosCreados().quitarDosPuntos(calle.getText().toString()));
                            object.put("Numero", new MetodosCreados().quitarDosPuntos(numCalle.getText().toString()));
                            tipoReg = "Eliminar Direccion 1";
                            new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/eliminar_datos_usuario.php", object.toString());

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
                buttonCerrarDir1.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialogAlertDeleteDir1.dismiss();
                    }
                });

                break;
            case R.id.ivDeleteDir2:
                AlertDialog.Builder builderDeleteDir2 = new AlertDialog.Builder(getContext());
                View pDirDelete2 = getActivity().getLayoutInflater().inflate(R.layout.eliminar_telefono_o_direccion, null);
                builderDeleteDir2.setView(pDirDelete2);
                dialogAlertDeleteDir2 = builderDeleteDir2.create();
                dialogAlertDeleteDir2.show();

                Button buttonEliminarDir2 = (Button) pDirDelete2.findViewById(R.id.btnEliminar_d_f);
                Button buttonCerrarDir2 = (Button) pDirDelete2.findViewById(R.id.btnCancelar_d_f);

                buttonEliminarDir2.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        JSONObject object = new JSONObject();
                        try
                        {
                            object.put("Id", id);
                            object.put("Calle", new MetodosCreados().quitarDosPuntos(calleDos.getText().toString()));
                            object.put("Numero", new MetodosCreados().quitarDosPuntos(numCalleDos.getText().toString()));
                            tipoReg = "Eliminar Direccion 2";
                            new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/eliminar_datos_usuario.php", object.toString());

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
                buttonCerrarDir2.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialogAlertDeleteDir2.dismiss();
                    }
                });

                break;

            case R.id.ivDeleteDir3:
                AlertDialog.Builder builderDeleteDir3 = new AlertDialog.Builder(getContext());
                View pDirDelete3 = getActivity().getLayoutInflater().inflate(R.layout.eliminar_telefono_o_direccion, null);
                builderDeleteDir3.setView(pDirDelete3);
                dialogAlertDeleteDir3 = builderDeleteDir3.create();
                dialogAlertDeleteDir3.show();

                Button buttonEliminarDir3 = (Button) pDirDelete3.findViewById(R.id.btnEliminar_d_f);
                Button buttonCerrarDir3 = (Button) pDirDelete3.findViewById(R.id.btnCancelar_d_f);

                buttonEliminarDir3.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        JSONObject object = new JSONObject();
                        try
                        {
                            object.put("Id", id);
                            object.put("Calle", new MetodosCreados().quitarDosPuntos(calleTres.getText().toString()));
                            object.put("Numero", new MetodosCreados().quitarDosPuntos(numCalleTres.getText().toString()));
                            tipoReg = "Eliminar Direccion 3";
                            new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/eliminar_datos_usuario.php", object.toString());

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
                buttonCerrarDir3.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialogAlertDeleteDir3.dismiss();
                    }
                });
                break;
        }
    }


    public class EjecutarConsulta extends AsyncTask<String, Void, String> //Enviar id
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

                if (tipoReg.equals("Traer Datos"))
                {
                    JSONObject jsonResult = new JSONObject(s);
                    if (jsonResult != null)
                    {
                        int lonTel = 0;
                        ProgressDialog progress = new ProgressDialog(getContext());
                        progress.setMessage("Cargando datos...");
                        progress.show();

                        linearLayoutDir1.setVisibility(View.GONE);
                        linearLayoutDir2.setVisibility(View.GONE);
                        linearLayoutDir3.setVisibility(View.GONE);

                        eNombre.setText(getResources().getString(R.string.usuario_nombre) + " " + jsonResult.getString("Nombre"));
                        eApellido.setText(getResources().getString(R.string.usuario_apellido) + " " + jsonResult.getString("Apellido"));
                        eNickname.setText(getResources().getString(R.string.usuario_nickname) + " " + jsonResult.getString("Nickname"));
                        eCorreo.setText(getResources().getString(R.string.correo_usuario) + " " + jsonResult.getString("Email"));
                        eTipo.setText(getResources().getString(R.string.tipo_usuario) + " " + jsonResult.getString("Rol"));

                        if (jsonResult.getString("Telefono").equals("false"))
                        {
                            linearLayoutTelefono1.setVisibility(View.GONE);
                        }
                        else
                        {
                            eTel1.setText(getResources().getString(R.string.numtel_uno) + " " + jsonResult.getString("Telefono"));
                            lonTel++;
                        }
                        if (jsonResult.getString("telefonoDos").equals("false"))
                        {
                            linearLayoutTelefono2.setVisibility(View.GONE);
                        }
                        else
                        {
                            linearLayoutTelefono2.setVisibility(View.VISIBLE);
                            eTel2.setText(getResources().getString(R.string.numtel_dos) + " " + jsonResult.getString("telefonoDos"));
                            lonTel++;

                        }

                        vTelefonos = new ArrayList<>();
                        if (lonTel == 2)
                        {

                            vTelefonos.add(new MetodosCreados().quitarDosPuntos(eTel1.getText().toString()));
                            vTelefonos.add(new MetodosCreados().quitarDosPuntos(eTel2.getText().toString()));
                        }
                        else if (lonTel == 1)
                        {
                            vTelefonos.add(new MetodosCreados().quitarDosPuntos(eTel1.getText().toString()));
                        }

                        int longitud = jsonResult.length();
                        int lonDireccion = longitud - 7;

                        calleDir = new ArrayList<>();
                        numDir = new ArrayList<>();

                        for (int i=0; i<lonDireccion; i++)
                        {
                            if (i == 0)
                            {
                                linearLayoutDir1.setVisibility(View.VISIBLE);
                                JSONObject jsonDireccionUno = new JSONObject(jsonResult.getString("0"));
                                calle.setText(getResources().getString(R.string.calle_usuario) + " " + jsonDireccionUno.getString("street"));
                                numCalle.setText(getResources().getString(R.string.calle_numero_usuario) + " " + jsonDireccionUno.getString("number"));

                                calleDir.add(new MetodosCreados().quitarDosPuntos(calle.getText().toString()));
                                numDir.add(new MetodosCreados().quitarDosPuntos(numCalle.getText().toString()));
                            }
                            if (i == 1)
                            {
                                linearLayoutDir2.setVisibility(View.VISIBLE);
                                JSONObject jsonDireccionDos = new JSONObject(jsonResult.getString("1"));
                                calleDos.setText(getResources().getString(R.string.calle_usuario) + " " + jsonDireccionDos.getString("street"));
                                numCalleDos.setText(getResources().getString(R.string.calle_numero_usuario) + " " + jsonDireccionDos.getString("number"));

                                calleDir.add(new MetodosCreados().quitarDosPuntos(calleDos.getText().toString()));
                                numDir.add(new MetodosCreados().quitarDosPuntos(numCalleDos.getText().toString()));
                            }
                            if (i == 2)
                            {
                                linearLayoutDir3.setVisibility(View.VISIBLE);
                                JSONObject jsonDireccionTres = new JSONObject(jsonResult.getString("2"));
                                calleTres.setText(getResources().getString(R.string.calle_usuario) + " " + jsonDireccionTres.getString("street"));
                                numCalleTres.setText(getResources().getString(R.string.calle_numero_usuario) + " " + jsonDireccionTres.getString("number"));

                                calleDir.add(new MetodosCreados().quitarDosPuntos(calleTres.getText().toString()));
                                numDir.add(new MetodosCreados().quitarDosPuntos(numCalleTres.getText().toString()));
                            }
                        }
                        tipoReg = "Botones";
                        progress.dismiss();

                    }
                    else
                    {
                        Toast.makeText(getContext(), "No trajo datos :(", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (tipoReg.equals("Modificar Nombre"))
                {
                    JSONObject jsonResult = new JSONObject(s);
                    String res = jsonResult.getString("Actualizado");
                    if (res.equals("Si"))
                    {
                        Toast.makeText(getContext(), "Datos modificados", Toast.LENGTH_SHORT).show();
                        cargar();
                        dialogAlertDatosUsuario.dismiss();
                    }
                }
                else if (tipoReg.equals("Clave"))
                {
                    JSONObject jsonResult = new JSONObject(s);
                    String res = jsonResult.getString("Resultado");
                    if (res.equals("Correcto"))
                    {
                        Toast.makeText(getContext(), "Clave actualizada", Toast.LENGTH_SHORT).show();
                        dialogAlertClave.dismiss();
                        dialogAlertDatosUsuario.dismiss();
                    }
                    else if (res.equals("Incorrecto"))
                    {
                        editTextClaveActual.setError("La clave es incorrecta");
                        Toast.makeText(getContext(), "La clave actual es incorrecta", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (tipoReg.equals("Agregar Telefono"))
                {
                    JSONObject jsonResult = new JSONObject(s);
                    String res = jsonResult.getString("Agregado");
                    if (res.equals("Si"))
                    {
                        linearLayoutTelefono1.setVisibility(View.VISIBLE);
                        dialogAlertAgregarTelefono.dismiss();
                        cargar();
                        Toast.makeText(getContext(), "Telefono agregado", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (tipoReg.equals("Modificar Telefono"))
                {
                    JSONObject jsonResult = new JSONObject(s);
                    String res = jsonResult.getString("Resultado");
                    if (res.equals("Si"))
                    {
                        dialogAlertTelefono1.dismiss();
                        cargar();
                        Toast.makeText(getContext(), "Telefono actualizado", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (tipoReg.equals("Modificar Telefono 2"))
                {
                    JSONObject jsonResult = new JSONObject(s);
                    String res = jsonResult.getString("Resultado");
                    if (res.equals("Si"))
                    {
                        dialogAlertTelefono2.dismiss();
                        cargar();
                        Toast.makeText(getContext(), "Telefono actualizado", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (tipoReg.equals("Eliminar Telefono 1"))
                {
                    JSONObject jsonResult = new JSONObject(s);
                    String res = jsonResult.getString("Eliminado");
                    if (res.equals("Si"))
                    {
                        cargar();
                        Toast.makeText(getContext(), "Telefono eliminado", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (tipoReg.equals("Eliminar Telefono 2"))
                {
                    JSONObject jsonResult = new JSONObject(s);
                    String res = jsonResult.getString("Eliminado");
                    if (res.equals("Si"))
                    {
                        cargar();
                        Toast.makeText(getContext(), "Telefono eliminado", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (tipoReg.equals("Modificar direccion 1"))
                {
                    JSONObject jsonResult = new JSONObject(s);
                    String res = jsonResult.getString("Actualizado");
                    if (res.equals("Si"))
                    {
                        dialogAlertUpdateDir1.dismiss();
                        cargar();
                        Toast.makeText(getContext(), "Direccion actualizada", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (tipoReg.equals("Modificar direccion 1"))
                {
                    JSONObject jsonResult = new JSONObject(s);
                    String res = jsonResult.getString("Actualizado");
                    if (res.equals("Si"))
                    {
                        dialogAlertUpdateDir1.dismiss();
                        cargar();
                        Toast.makeText(getContext(), "Direccion actualizada", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (tipoReg.equals("Modificar direccion 2"))
                {
                    JSONObject jsonResult = new JSONObject(s);
                    String res = jsonResult.getString("Actualizado");
                    if (res.equals("Si"))
                    {
                        dialogAlertUpdateDir2.dismiss();
                        cargar();
                        Toast.makeText(getContext(), "Direccion actualizada", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (tipoReg.equals("Modificar direccion 3"))
                {
                    JSONObject jsonResult = new JSONObject(s);
                    String res = jsonResult.getString("Actualizado");
                    if (res.equals("Si"))
                    {
                        dialogAlertUpdateDir3.dismiss();
                        cargar();
                        Toast.makeText(getContext(), "Direccion actualizada", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (tipoReg.equals("Eliminar Direccion 1"))
                {
                    JSONObject jsonResult = new JSONObject(s);
                    String res = jsonResult.getString("Eliminado");
                    if (res.equals("Si"))
                    {
                        dialogAlertDeleteDir1.dismiss();
                        cargar();
                        Toast.makeText(getContext(), "Direccion eliminada", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (tipoReg.equals("Eliminar Direccion 2"))
                {
                    JSONObject jsonResult = new JSONObject(s);
                    String res = jsonResult.getString("Eliminado");
                    if (res.equals("Si"))
                    {
                        dialogAlertDeleteDir2.dismiss();
                        cargar();
                        Toast.makeText(getContext(), "Direccion eliminada", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (tipoReg.equals("Eliminar Direccion 3"))
                {
                    JSONObject jsonResult = new JSONObject(s);
                    String res = jsonResult.getString("Eliminado");
                    if (res.equals("Si"))
                    {
                        dialogAlertDeleteDir3.dismiss();
                        cargar();
                        Toast.makeText(getContext(), "Direccion eliminada", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (tipoReg.equals("Agregar Direccion 1"))
                {
                    JSONObject jsonResult = new JSONObject(s);
                    String res = jsonResult.getString("Direccion");
                    if (res.equals("Si"))
                    {
                        dialogAlertAddDir1.dismiss();
                        cargar();
                        Toast.makeText(getContext(), "Direccion agregada", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Algo paso.", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (tipoReg.equals("Definir"))
                {
                    Boolean enUso = false;
                    JSONArray jsonArray = new JSONArray(s);
                    int posDef = 0;
                    for (int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.getString("default").equals("1"))
                        {
                            posDef = i;
                            enUso = true;
                        }
                    }
                    if (enUso)
                    {
                        if (posDef == 0)
                        {
                            buttonUsar1.setText("En uso");
                            buttonUsar2.setText("Usar");
                            buttonUsar3.setText("Usar");

                            buttonUsar1.setBackground(getResources().getDrawable(R.drawable.colorbuttonred));
                            buttonUsar1.setTextColor(getResources().getColor(R.color.textoBlanco));
                            buttonUsar2.setBackground(getResources().getDrawable(R.drawable.colorbuttonamarillo));
                            buttonUsar2.setTextColor(getResources().getColor(R.color.textoBlanco));
                            buttonUsar3.setBackground(getResources().getDrawable(R.drawable.colorbuttonamarillo));
                            buttonUsar3.setTextColor(getResources().getColor(R.color.textoBlanco));
                        }
                        else if (posDef == 1)
                        {
                            buttonUsar1.setText("Usar");
                            buttonUsar2.setText("En uso");
                            buttonUsar3.setText("Usar");

                            buttonUsar1.setBackground(getResources().getDrawable(R.drawable.colorbuttonamarillo));
                            buttonUsar1.setTextColor(getResources().getColor(R.color.textoBlanco));
                            buttonUsar2.setBackground(getResources().getDrawable(R.drawable.colorbuttonred));
                            buttonUsar2.setTextColor(getResources().getColor(R.color.textoBlanco));
                            buttonUsar3.setBackground(getResources().getDrawable(R.drawable.colorbuttonamarillo));
                            buttonUsar3.setTextColor(getResources().getColor(R.color.textoBlanco));
                        }
                        else if (posDef == 2)
                        {
                            buttonUsar1.setText("Usar");
                            buttonUsar2.setText("Usar");
                            buttonUsar3.setText("En uso");

                            buttonUsar1.setBackground(getResources().getDrawable(R.drawable.colorbuttonamarillo));
                            buttonUsar1.setTextColor(getResources().getColor(R.color.textoBlanco));
                            buttonUsar2.setBackground(getResources().getDrawable(R.drawable.colorbuttonamarillo));
                            buttonUsar2.setTextColor(getResources().getColor(R.color.textoBlanco));
                            buttonUsar3.setBackground(getResources().getDrawable(R.drawable.colorbuttonred));
                            buttonUsar3.setTextColor(getResources().getColor(R.color.textoBlanco));
                        }
                    }

                    cargar();
                }
                else if (tipoReg.equals("Botones"))
                {
                    Boolean enUso = false;
                    JSONArray jsonArray = new JSONArray(s);
                    int posDef = 0;
                    for (int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.getString("default").equals("1"))
                        {
                            posDef = i;
                            enUso = true;
                        }
                    }
                    if (enUso)
                    {
                        if (posDef == 0)
                        {
                            buttonUsar1.setText("En uso");
                            buttonUsar2.setText("Usar");
                            buttonUsar3.setText("Usar");

                            buttonUsar1.setBackground(getResources().getDrawable(R.drawable.colorbuttonred));
                            buttonUsar1.setTextColor(getResources().getColor(R.color.textoBlanco));
                            buttonUsar2.setBackground(getResources().getDrawable(R.drawable.colorbuttonamarillo));
                            buttonUsar2.setTextColor(getResources().getColor(R.color.textoBlanco));
                            buttonUsar3.setBackground(getResources().getDrawable(R.drawable.colorbuttonamarillo));
                            buttonUsar3.setTextColor(getResources().getColor(R.color.textoBlanco));
                        }
                        else if (posDef == 1)
                        {
                            buttonUsar1.setText("Usar");
                            buttonUsar2.setText("En uso");
                            buttonUsar3.setText("Usar");

                            buttonUsar1.setBackground(getResources().getDrawable(R.drawable.colorbuttonamarillo));
                            buttonUsar1.setTextColor(getResources().getColor(R.color.textoBlanco));
                            buttonUsar2.setBackground(getResources().getDrawable(R.drawable.colorbuttonred));
                            buttonUsar2.setTextColor(getResources().getColor(R.color.textoBlanco));
                            buttonUsar3.setBackground(getResources().getDrawable(R.drawable.colorbuttonamarillo));
                            buttonUsar3.setTextColor(getResources().getColor(R.color.textoBlanco));
                        }
                        else if (posDef == 2)
                        {
                            buttonUsar1.setText("Usar");
                            buttonUsar2.setText("Usar");
                            buttonUsar3.setText("En uso");

                            buttonUsar1.setBackground(getResources().getDrawable(R.drawable.colorbuttonamarillo));
                            buttonUsar1.setTextColor(getResources().getColor(R.color.textoBlanco));
                            buttonUsar2.setBackground(getResources().getDrawable(R.drawable.colorbuttonamarillo));
                            buttonUsar2.setTextColor(getResources().getColor(R.color.textoBlanco));
                            buttonUsar3.setBackground(getResources().getDrawable(R.drawable.colorbuttonred));
                            buttonUsar3.setTextColor(getResources().getColor(R.color.textoBlanco));
                        }
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