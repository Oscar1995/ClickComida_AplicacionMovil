package com.chile.oscar.clickcomida_aplicacionmovil;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONException;
import org.json.JSONObject;

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

public class MisDatos extends Fragment implements View.OnClickListener
{
    TextView eNombre, eApellido, eNickname, eCorreo, eTipo, eTel1, eTel2, calle, numCalle, calleDos, numCalleDos, calleTres, numCalleTres;
    EditText txtVariable ,txtClaveUser, txtActualClave;
    FloatingActionButton fbUpdateUser;
    String id, telefono, telefonoRestanteUno, telefonoRestanteDos;
    AlertDialog dialogCrudUsuario;
    View view, vUpdate;
    String calleAntigua = "", calleNumAntigua = "", telefono_ant = "";
    String[] direcciones, vTelefonos;
    AlertDialog dialogUp, dialogChangeTelefono, dialogChangeDelete, dialogChangeDireccionn, dialogAgregarDireccion, dialogChangeClave, dialogChangeDireccionUpdate, dialogChangeDireccionTelefono;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.activity_mis_datos, container, false);
        eNombre = (TextView) view.findViewById(R.id.txtNombre_misdatos);
        eApellido = (TextView) view.findViewById(R.id.txtApellido_misdatos);
        eNickname = (TextView) view.findViewById(R.id.txtNickname_misdatos);
        eCorreo = (TextView) view.findViewById(R.id.txtCorreo_misdatos);
        fbUpdateUser = (FloatingActionButton) view.findViewById(R.id.fbModificar);

        eTipo = (TextView) view.findViewById(R.id.txtTipo_misdatos);
        eTel1 = (TextView) view.findViewById(R.id.txtTeleUno_misdatos);
        eTel2 = (TextView) view.findViewById(R.id.txtTeleDos_misdatos);

        calle = (TextView) view.findViewById(R.id.txtCalle_misdatos);
        numCalle = (TextView) view.findViewById(R.id.txtNumeroCalle_misdatos);
        calleDos = (TextView) view.findViewById(R.id.txtCalle_misdatos_d);
        numCalleDos = (TextView) view.findViewById(R.id.txtNumeros_misdatos_d);
        calleTres = (TextView) view.findViewById(R.id.txtCallemisdatos_t);
        numCalleTres = (TextView) view.findViewById(R.id.txtNumeromisdatos_t);


        fbUpdateUser.setOnClickListener(this);

        Bundle bundle = getArguments();
        id = bundle.getString("IdUser");


        TraerDatos obj = new TraerDatos();
        obj.execute(id);

        return view;
    }
    public void cargar()
    {
        TraerDatos obj = new TraerDatos();
        obj.execute(id);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.fbModificar:
                createdd();
                break;
        }
    }
    int positionCrudSelected;
    int positionElement;
    int positionUpdateSelected;
    int positionUpdateSelectedTel;

    boolean swDelete = false, swUpdate = false;
    int positionDelete = 0;

    boolean tel1 = false, tel2 = false;
    public void createdd()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View v = getActivity().getLayoutInflater().inflate(R.layout.crud_datos_usuario, null);
        builder.setView(v);
        dialogCrudUsuario = builder.create();
        dialogCrudUsuario.show();

        final Button botonAceptar = (Button) v.findViewById(R.id.btnAceptar);
        Button botonCancelar = (Button) v.findViewById(R.id.btnCancelar);
        final Button botonCancelarE = (Button) v.findViewById(R.id.btnCancelarE);

        final Spinner sElements = (Spinner)v.findViewById(R.id.ddElements);
        botonAceptar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (positionCrudSelected == 0) //Agregar
                {
                    if (positionElement == 0)
                    {
                        //Telefono
                        AlertDialog.Builder builderChange = new AlertDialog.Builder(getContext());
                        View p = getActivity().getLayoutInflater().inflate(R.layout.agregar_telefono_usuario, null);
                        builderChange.setView(p);
                        dialogChangeTelefono = builderChange.create();
                        dialogChangeTelefono.show();

                        final EditText textoTel = (EditText) p.findViewById(R.id.txtTelefono_us);
                        Button btnAddTel = (Button)p.findViewById(R.id.btnAgregarTelefono);
                        Button btnCanTel = (Button)p.findViewById(R.id.btnCancelarTelefono);
                        btnAddTel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                if (textoTel.getText().toString().isEmpty())
                                {
                                    textoTel.setError("Debes agregar un numero de telefono.");
                                }
                                else
                                {
                                    int longitud = textoTel.length();
                                    if (longitud >= 6)
                                    {
                                        if (tel1 == true && tel2 == true)
                                        {
                                            Toast.makeText(getContext(), "Solo esta permitido agregar dos numeros de telefono.", Toast.LENGTH_SHORT).show();
                                        }
                                        else if (tel1 == true && tel2 == false)
                                        {
                                            tipo_add = "Telefono";
                                            String tel = textoTel.getText().toString();
                                            telefono = tel;
                                            agregarDatos addData = new agregarDatos();
                                            addData.execute(id, tel);
                                        }
                                    }
                                    else
                                    {
                                        textoTel.setError("El numero de telefono debe ser mayor o igual a seis numeros");
                                    }
                                }
                            }
                        });
                        btnCanTel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                dialogChangeTelefono.cancel();
                            }
                        });
                    }
                    else if (positionElement == 1) //
                    {
                        //Direccion
                        AlertDialog.Builder builderChange = new AlertDialog.Builder(getContext());
                        View p = getActivity().getLayoutInflater().inflate(R.layout.agregar_direccion_usuario, null);
                        builderChange.setView(p);
                        dialogAgregarDireccion = builderChange.create();
                        dialogAgregarDireccion.show();
                        final EditText txtCalle = (EditText)p.findViewById(R.id.txtCalle_usuario_dir);
                        final EditText txtNumeroCalle = (EditText)p.findViewById(R.id.txtNumero_usuario_dir);
                        Button btnDir = (Button)p.findViewById(R.id.btnCancelarDireccion);
                        Button botonModificarDi = (Button)p.findViewById(R.id.btnModificarDireccion);
                        botonModificarDi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                int lonDir = direcciones.length;
                                if (lonDir == 3)
                                {
                                    Toast.makeText(getContext(), "Ya tienes tres direcciones, puedes eliminar una direccion o modificar una.", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    if (txtCalle.getText().toString().isEmpty() || txtNumeroCalle.getText().toString().isEmpty())
                                    {
                                        if (txtCalle.getText().toString().isEmpty())
                                        {
                                            txtCalle.setError("Completa este campo.");
                                        }
                                        if(txtNumeroCalle.getText().toString().isEmpty())
                                        {
                                            txtNumeroCalle.setError("Completa este campo.");
                                        }
                                    }
                                    else
                                    {
                                        agregarDireccion addDireccion = new agregarDireccion();
                                        addDireccion.execute(id, txtCalle.getText().toString(), txtNumeroCalle.getText().toString());
                                    }
                                }
                            }
                        });
                        btnDir.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                dialogAgregarDireccion.cancel();
                            }
                        });
                    }
                }
                if (positionCrudSelected == 1) //Modificar
                {
                    if (positionElement == 0)
                    {
                        //Nombre
                        dialogoSingular(dialogCrudUsuario, "Nombre");
                    }
                    else if (positionElement == 1)
                    {
                        //Apellido
                        dialogoSingular(dialogCrudUsuario, "Apellido");
                    }
                    else if (positionElement == 2)
                    {
                        //Nickname
                        dialogoSingular(dialogCrudUsuario, "Nickname");
                    }
                    else if (positionElement == 3)
                    {
                        //Correo
                        dialogoSingular(dialogCrudUsuario, "Correo");
                    }
                    else if (positionElement == 4)
                    {
                        //Clave
                        AlertDialog.Builder builderChange = new AlertDialog.Builder(getContext());
                        View p = getActivity().getLayoutInflater().inflate(R.layout.modificar_clave_usuario, null);
                        builderChange.setView(p);
                        dialogChangeClave = builderChange.create();
                        dialogChangeClave.show();

                        txtActualClave = (EditText)p.findViewById(R.id.txtClaveActual_us);
                        final EditText txtNuevaClave = (EditText)p.findViewById(R.id.txtClaveNueva_us);
                        final EditText txtNuevaClaveRep = (EditText)p.findViewById(R.id.txtClaveNuevaRep_us);

                        Button botonCambiarClave = (Button)p.findViewById(R.id.btnCambiarClave);
                        Button botonCerrarDialogo = (Button)p.findViewById(R.id.btnCancelarClave);

                        botonCambiarClave.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                if (txtActualClave.getText().toString().isEmpty() || txtNuevaClave.getText().toString().isEmpty() || txtNuevaClaveRep.getText().toString().isEmpty())
                                {
                                    if (txtActualClave.getText().toString().isEmpty())
                                    {
                                        txtActualClave.setError("Completa este campo");
                                    }
                                    if (txtNuevaClave.getText().toString().isEmpty())
                                    {
                                        txtNuevaClave.setError("Completa este campo");
                                    }
                                    if (txtNuevaClaveRep.getText().toString().isEmpty())
                                    {
                                        txtNuevaClaveRep.setError("Completa este campo");
                                    }
                                }
                                else
                                {
                                    if (txtNuevaClave.getText().toString().equals(txtNuevaClaveRep.getText().toString()))
                                    {
                                        tipo_registro = "Clave";
                                        modificarUsuario updateUs = new modificarUsuario();
                                        updateUs.execute(id, txtNuevaClave.getText().toString(), txtActualClave.getText().toString());
                                    }
                                    else
                                    {
                                        txtNuevaClaveRep.setError("La clave no coincide con la de arriba.");
                                    }
                                }
                            }
                        });
                        botonCerrarDialogo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                dialogChangeClave.cancel();
                            }
                        });
                    }
                    else if (positionElement == 5)
                    {
                        //Direccion
                        AlertDialog.Builder builderChange = new AlertDialog.Builder(getContext());
                        View p = getActivity().getLayoutInflater().inflate(R.layout.modificar_direccion_usuario, null);
                        builderChange.setView(p);
                        dialogChangeDireccionUpdate = builderChange.create();
                        dialogChangeDireccionUpdate.show();

                        swUpdate = false;
                        final LinearLayout linearUno = (LinearLayout)p.findViewById(R.id.llCalle_numero);
                        final LinearLayout linearDos = (LinearLayout)p.findViewById(R.id.llCalle_numero_textos);
                        final LinearLayout linearTres = (LinearLayout)p.findViewById(R.id.llBotones);
                        final TextView txtInfoCalle = (TextView)p.findViewById(R.id.tvInfoCalle);
                        final TextView txtInfoNumeroCalle = (TextView)p.findViewById(R.id.tvInfoNumeroCalle);
                        final EditText textNuevaCalle = (EditText)p.findViewById(R.id.eNuevaCalle);
                        final EditText textNuevaCalleNumero = (EditText)p.findViewById(R.id.eNuevoNumeroCalle);
                        Button botonMod = (Button)p.findViewById(R.id.btnModificarDireccion_us);
                        Button botonCerrar = (Button)p.findViewById(R.id.btnCerrarDireccion_us);
                        final ListView lvDirecciones = (ListView)p.findViewById(R.id.lvListaDirecciones);

                        linearUno.setVisibility(View.GONE);
                        linearDos.setVisibility(View.GONE);
                        linearTres.setVisibility(View.GONE);

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, direcciones);
                        lvDirecciones.setAdapter(adapter);

                        lvDirecciones.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                positionUpdateSelected = position;
                                swUpdate = true;

                                linearUno.setVisibility(View.VISIBLE);
                                String dirUs = lvDirecciones.getItemAtPosition(position).toString();

                                String[]arr_numero = dirUs.split("Numero:");
                                String numeroCalle = arr_numero[1].trim();

                                String[]arr_calleLeft = dirUs.split("-");
                                String calleLeft = arr_calleLeft[0].trim();
                                String[]arr_calle = calleLeft.split("Calle:");
                                String nomCalle = arr_calle[1].trim();

                                calleAntigua = nomCalle;
                                calleNumAntigua = numeroCalle;

                                txtInfoCalle.setText(getResources().getString(R.string.calle_usuario) + " " + nomCalle);
                                txtInfoNumeroCalle.setText(getResources().getString(R.string.calle_numero_usuario) + " " + numeroCalle);

                                linearDos.setVisibility(View.VISIBLE);
                                linearTres.setVisibility(View.VISIBLE);
                            }
                        });

                        botonMod.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                if (swUpdate)
                                {
                                    if (textNuevaCalle.getText().toString().isEmpty() || textNuevaCalleNumero.getText().toString().isEmpty())
                                    {
                                        if (textNuevaCalle.getText().toString().isEmpty())
                                        {
                                            textNuevaCalle.setError("Debes completar este campo.");
                                        }
                                        if (textNuevaCalleNumero.getText().toString().isEmpty())
                                        {
                                            textNuevaCalleNumero.setError("Debes completar este campo.");
                                        }
                                    }
                                    else
                                    {
                                        tipo_registro = "Direccion";
                                        updateDireccion_or_telefono modificarDir = new updateDireccion_or_telefono();
                                        modificarDir.execute(calleAntigua, calleNumAntigua, textNuevaCalle.getText().toString(), textNuevaCalleNumero.getText().toString());
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "Selecciona una direccion para modificarlo.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        botonCerrar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogChangeDireccionUpdate.cancel();
                            }
                        });


                    }
                    else if (positionElement == 6)
                    {
                        //Telefono
                        AlertDialog.Builder builderChange = new AlertDialog.Builder(getContext());
                        View p = getActivity().getLayoutInflater().inflate(R.layout.modificar_telefono, null);
                        builderChange.setView(p);
                        dialogChangeDireccionTelefono = builderChange.create();
                        dialogChangeDireccionTelefono.show();

                        final ListView listaTelefono = (ListView)p.findViewById(R.id.lv_lista_telefono);
                        final TextView textoNum = (TextView)p.findViewById(R.id.tvInfoTelefono);
                        final EditText txtnuevoTel = (EditText) p.findViewById(R.id.etNuevoTelefonp_us);
                        final LinearLayout linearElementos = (LinearLayout)p.findViewById(R.id.llTextoandButton);
                        Button btnModTel = (Button)p.findViewById(R.id.btnModificarTelefono_us);
                        Button btnCerrarTel = (Button)p.findViewById(R.id.btnCerrarTelefono_us);

                        linearElementos.setVisibility(View.GONE);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, vTelefonos);
                        listaTelefono.setAdapter(adapter);

                        listaTelefono.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                positionUpdateSelectedTel = position;
                                linearElementos.setVisibility(View.VISIBLE);
                                String telUs = listaTelefono.getItemAtPosition(position).toString();

                                String[]arr_numero = telUs.split(":");
                                String telefono_antiguo = arr_numero[1].trim();
                                textoNum.setText(telefono_antiguo);
                                telefono_ant = telefono_antiguo;

                            }
                        });

                        btnModTel.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                if (txtnuevoTel.getText().toString().isEmpty())
                                {
                                    txtnuevoTel.setError("Debes ingresar un telefono.");
                                }
                                else
                                {
                                    if (txtnuevoTel.getText().toString().length() >= 6)
                                    {
                                        tipo_registro = "Telefono";
                                        updateDireccion_or_telefono updateTel = new updateDireccion_or_telefono();
                                        updateTel.execute(id, telefono_ant, txtnuevoTel.getText().toString());
                                    }
                                    else
                                    {
                                        txtnuevoTel.setError("El numero de telefono debe ser mayor igual a 6.");
                                    }

                                }
                            }
                        });
                        btnCerrarTel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogChangeDireccionTelefono.cancel();
                            }
                        });
                    }
                }
                if (positionCrudSelected == 2) //Eliminar
                {
                    if (positionElement == 0)
                    {
                        //Telefono
                        AlertDialog.Builder builderChange = new AlertDialog.Builder(getContext());
                        View p = getActivity().getLayoutInflater().inflate(R.layout.eliminar_telefono_o_direccion, null);
                        TextView tvInfo = (TextView)p.findViewById(R.id.tvInfo_d_f);
                        tvInfo.setText(getResources().getString(R.string.descripcion_telefono_o_direccion));
                        builderChange.setView(p);
                        dialogChangeDelete = builderChange.create();
                        swDelete = false;
                        dialogChangeDelete.show();

                        String[] telefonos = null;
                        if (eTel2.getText().toString().equals("Telefono 2: Vacio"))
                        {
                            telefonos = new String[]{eTel1.getText().toString()};
                        }
                        else
                        {
                            telefonos = new String[]{eTel1.getText().toString(), eTel2.getText().toString()};
                        }
                        Button botonEliminar_t_d = (Button)p.findViewById(R.id.btnEliminar_d_f);
                        Button botonCerrar = (Button)p.findViewById(R.id.btnCancelar_d_f);
                        final ListView listView = (ListView)p.findViewById(R.id.lv_d_f);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, telefonos);
                        listView.setAdapter(adapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                positionDelete = position;
                                swDelete = true;
                            }
                        });
                        botonEliminar_t_d.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                if (swDelete)
                                {
                                    int listaTotal = listView.getAdapter().getCount();
                                    if (listaTotal == 1)
                                    {
                                        Toast.makeText(getContext(), "Debes tener al menos 2 numeros de telefonos para eliminar uno, si este numero ya no lo usas podrias modificarlo.", Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        String telName = listView.getItemAtPosition(positionDelete).toString();
                                        String[] arregloTel = telName.split(":");
                                        String onlyNumber = arregloTel[1].trim();
                                        tipo_delete = "Telefono";
                                        eliminarDatos deleteData = new eliminarDatos();
                                        deleteData.execute(id, onlyNumber);

                                    }
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "Selecciona un telefono e elimina", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        botonCerrar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                dialogChangeDelete.cancel();
                            }
                        });

                    }
                    else if (positionElement == 1)
                    {
                        //Direccion
                        AlertDialog.Builder builderChange = new AlertDialog.Builder(getContext());
                        View p = getActivity().getLayoutInflater().inflate(R.layout.eliminar_telefono_o_direccion, null);
                        TextView tvInfo = (TextView)p.findViewById(R.id.tvInfo_d_f);
                        tvInfo.setText(getResources().getString(R.string.descripcion_direccion));
                        builderChange.setView(p);
                        dialogChangeDireccionn = builderChange.create();
                        dialogChangeDireccionn.show();
                        Button botonDeleteDireccion = (Button)p.findViewById(R.id.btnEliminar_d_f);
                        Button botonCerrarDireccion = (Button)p.findViewById(R.id.btnCancelar_d_f);
                        swDelete = false;

                        final ListView listView = (ListView)p.findViewById(R.id.lv_d_f);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, direcciones);
                        listView.setAdapter(adapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                positionDelete = position;
                                swDelete = true;
                            }
                        });

                        botonDeleteDireccion.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                if (swDelete)
                                {
                                    int listaTotal = listView.getAdapter().getCount();
                                    if (listaTotal == 1)
                                    {
                                        Toast.makeText(getContext(), "Debes tener al menos 2 direcciones para eliminar uno, si esta direccion ya no lo usas podrias modificarlo.", Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        tipo_delete = "Direccion";
                                        String datos = direcciones[positionDelete];

                                        String[]arr_numero = datos.split("Numero:");
                                        String numero = arr_numero[1].trim();

                                        String[]arr_calleLeft = datos.split("-");
                                        String calleLeft = arr_calleLeft[0].trim();
                                        String[]arr_calle = calleLeft.split("Calle:");
                                        String calle = arr_calle[1].trim();


                                        eliminarDatos deleteData = new eliminarDatos();
                                        deleteData.execute(id, calle, numero);
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "Selecciona una direccion e elimina", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        botonCerrarDireccion.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogChangeDireccionn.cancel();
                            }
                        });
                    }
                }
            }
        });
        String[] AddDelete = {"Agregar", "Modificar", "Eliminar"};
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, AddDelete);
        Spinner spinner = (Spinner) v.findViewById(R.id.ddCrud);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int positionCrud, long id)
            {
                positionCrudSelected = positionCrud;
                if (positionCrud == 1)
                {
                    String[] dElements = {"Nombre", "Apellido", "Nickname", "Correo electronico", "Clave", "Direccion", "Telefono"};
                    ArrayAdapter adapElement = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, dElements);
                    sElements.setAdapter(adapElement);
                    sElements.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int positionM, long id)
                        {
                            positionElement = positionM;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else if (positionCrudSelected == 0 || positionCrudSelected == 2)
                {
                    String[] dElements = {"Un telefono", "Una dirección"};
                    ArrayAdapter adapElement = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, dElements);
                    sElements.setAdapter(adapElement);
                    sElements.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int positionE, long id)
                        {
                            positionElement = positionE;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent)
                        {

                        }
                    });
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        botonCancelar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialogCrudUsuario.cancel();
            }
        });
    }
    public void manipularTexto(View p, String toHintP)
    {
        EditText etUno = (EditText)p.findViewById(R.id.etVariable);
        etUno.setHint(toHintP);
    }
    public void dialogoSingular(final AlertDialog dialog, String tipo)
    {
        AlertDialog.Builder builderUpdate = new AlertDialog.Builder(getContext());
        vUpdate = getActivity().getLayoutInflater().inflate(R.layout.modificar_datos_usuario_singular, null);
        builderUpdate.setView(vUpdate);
        dialogUp = builderUpdate.create();

        if (tipo.equals("Nombre"))
        {
            manipularTexto(vUpdate, getResources().getString(R.string.tu_nombre_nuevo));
            Button btnModUs = (Button)vUpdate.findViewById(R.id.btnModificarUsuario);
            Button btnCancel = (Button)vUpdate.findViewById(R.id.btnCancelarE);
            txtVariable = (EditText)vUpdate.findViewById(R.id.etVariable);
            txtClaveUser = (EditText)vUpdate.findViewById(R.id.etClaveVariable);

            btnModUs.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (txtVariable.getText().toString().isEmpty() || txtClaveUser.getText().toString().isEmpty())
                    {
                        if (txtVariable.getText().toString().isEmpty())
                        {
                            txtVariable.setError("Debes completar este campo");
                        }
                        if (txtClaveUser.getText().toString().isEmpty())
                        {
                            txtClaveUser.setError("Debes completar este campo");
                        }
                    }
                    else
                    {
                        tipo_registro = "Nombre";
                        modificarUsuario updateUs = new modificarUsuario();
                        updateUs.execute(id, txtVariable.getText().toString(), txtClaveUser.getText().toString());
                    }
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogUp.cancel();
                }
            });
        }
        else if (tipo.equals("Apellido"))
        {
            manipularTexto(vUpdate, getResources().getString(R.string.tu_apellido_nuevo));
            Button btnModUs = (Button)vUpdate.findViewById(R.id.btnModificarUsuario);
            Button btnCancel = (Button)vUpdate.findViewById(R.id.btnCancelarE);
            txtVariable = (EditText)vUpdate.findViewById(R.id.etVariable);
            txtClaveUser = (EditText)vUpdate.findViewById(R.id.etClaveVariable);
            btnModUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if (txtVariable.getText().toString().isEmpty() || txtClaveUser.getText().toString().isEmpty())
                    {
                        if (txtVariable.getText().toString().isEmpty())
                        {
                            txtVariable.setError("Debes completar este campo");
                        }
                        if (txtClaveUser.getText().toString().isEmpty())
                        {
                            txtClaveUser.setError("Debes completar este campo");
                        }
                    }
                    else
                    {
                        tipo_registro = "Apellido";
                        modificarUsuario updateUs = new modificarUsuario();
                        updateUs.execute(id, txtVariable.getText().toString(), txtClaveUser.getText().toString());
                    }
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogUp.cancel();
                }
            });

        }
        else if (tipo.equals("Nickname"))
        {
            manipularTexto(vUpdate, getResources().getString(R.string.tu_nickname_nuevo));
            Button btnModUs = (Button)vUpdate.findViewById(R.id.btnModificarUsuario);
            Button btnCancel = (Button)vUpdate.findViewById(R.id.btnCancelarE);
            txtVariable = (EditText)vUpdate.findViewById(R.id.etVariable);
            txtClaveUser = (EditText)vUpdate.findViewById(R.id.etClaveVariable);
            btnModUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if (txtVariable.getText().toString().isEmpty() || txtClaveUser.getText().toString().isEmpty())
                    {
                        if (txtVariable.getText().toString().isEmpty())
                        {
                            txtVariable.setError("Debes completar este campo");
                        }
                        if (txtClaveUser.getText().toString().isEmpty())
                        {
                            txtClaveUser.setError("Debes completar este campo");
                        }
                    }
                    else
                    {
                        tipo_registro = "Nickname";
                        modificarUsuario updateUs = new modificarUsuario();
                        updateUs.execute(id, txtVariable.getText().toString(), txtClaveUser.getText().toString());
                    }
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogUp.cancel();
                }
            });
        }
        else if (tipo.equals("Correo"))
        {
            manipularTexto(vUpdate, getResources().getString(R.string.tu_correo_nuevo));
            Button btnModUs = (Button)vUpdate.findViewById(R.id.btnModificarUsuario);
            Button btnCancel = (Button)vUpdate.findViewById(R.id.btnCancelarE);
            txtVariable = (EditText)vUpdate.findViewById(R.id.etVariable);
            txtClaveUser = (EditText)vUpdate.findViewById(R.id.etClaveVariable);
            btnModUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if (txtVariable.getText().toString().isEmpty() || txtClaveUser.getText().toString().isEmpty())
                    {
                        if (txtVariable.getText().toString().isEmpty())
                        {
                            txtVariable.setError("Debes completar este campo");
                        }
                        if (txtClaveUser.getText().toString().isEmpty())
                        {
                            txtClaveUser.setError("Debes completar este campo");
                        }
                    }
                    else
                    {
                        tipo_registro = "Correo";
                        modificarUsuario updateUs = new modificarUsuario();
                        updateUs.execute(id, txtVariable.getText().toString(), txtClaveUser.getText().toString());
                    }
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogUp.cancel();
                }
            });
        }
        dialogUp.show();
    }
    public class TraerDatos extends AsyncTask<String, Void, String>
    {
        @Override
        public String doInBackground(String... params)
        {
            String result = "";
            try
            {
                URL url = new URL(getResources().getString(R.string.direccion_web) + "/Controlador/datos_usuario.php");
                try
                {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                    String json = "";
                    JSONObject object = new JSONObject();
                    try
                    {
                        object.put("id", params[0]);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    json = object.toString();

                    String post_data= URLEncoder.encode("json_data","UTF-8")+"="+URLEncoder.encode(json,"UTF-8");

                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String line="";
                    while ((line = bufferedReader.readLine())!=null)
                    {
                        result+=line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s)
        {
            try
            {
                JSONObject jsonResult = new JSONObject(s);
                if (jsonResult != null)
                {
                    ProgressDialog progress = new ProgressDialog(getContext());
                    progress.setMessage("Cargando datos...");
                    progress.show();

                    eNombre.setText(getResources().getString(R.string.usuario_nombre) + " " + jsonResult.getString("Nombre"));
                    eApellido.setText(getResources().getString(R.string.usuario_apellido) + " " + jsonResult.getString("Apellido"));
                    eNickname.setText(getResources().getString(R.string.usuario_nickname) + " " + jsonResult.getString("Nickname"));
                    eCorreo.setText(getResources().getString(R.string.correo_usuario) + " " + jsonResult.getString("Email"));
                    eTipo.setText(getResources().getString(R.string.tipo_usuario) + " " + jsonResult.getString("Rol"));
                    eTel1.setText(getResources().getString(R.string.numtel_uno) + " " + jsonResult.getString("Telefono"));
                    telefonoRestanteUno = getResources().getString(R.string.numtel_uno) + " " + jsonResult.getString("Telefono");
                    tel1 = true;

                    if (jsonResult.getString("telefonoDos").equals("false"))
                    {
                        eTel2.setText("");
                        tel2 = false;
                        vTelefonos = new String[1];
                        vTelefonos[0] = telefonoRestanteUno;
                    }
                    else
                    {
                        eTel2.setText(getResources().getString(R.string.numtel_dos) + " " + jsonResult.getString("telefonoDos"));
                        tel2 = true;
                        telefonoRestanteDos = getResources().getString(R.string.numtel_dos) + " " + jsonResult.getString("telefonoDos");

                        vTelefonos = new String[2];
                        vTelefonos[0] = telefonoRestanteUno;
                        vTelefonos[1] = telefonoRestanteDos;
                    }

                    int longitud = jsonResult.length();
                    int lonDireccion = longitud - 7;
                    boolean dUno = false, dDos = false, dTres = false;
                    direcciones = new String[lonDireccion];

                    for (int i=0; i<lonDireccion; i++)
                    {
                        if (i == 0)
                        {
                            JSONObject jsonDireccionUno = new JSONObject(jsonResult.getString("0"));
                            calle.setText(getResources().getString(R.string.calle_usuario) + " " + jsonDireccionUno.getString("street"));
                            numCalle.setText(getResources().getString(R.string.calle_numero_usuario) + " " + jsonDireccionUno.getString("number"));
                            direcciones[i] = "Calle: " + jsonDireccionUno.getString("street") + " - " + "Numero: " + jsonDireccionUno.getString("number");
                            dUno = true;
                        }
                        if (i == 1)
                        {
                            JSONObject jsonDireccionDos = new JSONObject(jsonResult.getString("1"));
                            calleDos.setText(getResources().getString(R.string.calle_usuario) + " " + jsonDireccionDos.getString("street"));
                            numCalleDos.setText(getResources().getString(R.string.calle_numero_usuario) + " " + jsonDireccionDos.getString("number"));
                            direcciones[i] = "Calle: " + jsonDireccionDos.getString("street") + " - " + "Numero: " + jsonDireccionDos.getString("number");
                            dDos = true;
                        }
                        if (i == 2)
                        {
                            JSONObject jsonDireccionTres = new JSONObject(jsonResult.getString("2"));
                            calleTres.setText(getResources().getString(R.string.calle_usuario) + " " + jsonDireccionTres.getString("street"));
                            numCalleTres.setText(getResources().getString(R.string.calle_numero_usuario) + " " + jsonDireccionTres.getString("number"));
                            direcciones[i] = "Calle: " + jsonDireccionTres.getString("street") + " - " + "Numero: " + jsonDireccionTres.getString("number");
                            dTres = true;
                        }
                    }
                    if (dDos == false)
                    {
                        calleDos.setText("");
                        numCalleDos.setText("");
                    }
                    if (dTres == false)
                    {
                        calleTres.setText("");
                        numCalleTres.setText("");
                    }
                    progress.cancel();

                }
                else
                {
                    Toast.makeText(getContext(), "No trajo datos :(", Toast.LENGTH_SHORT).show();
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
    String tipo_registro;
    String tipo_delete;
    String tipo_add;
    public class modificarUsuario extends AsyncTask<String, Void, String>
    {
        @Override
        public String doInBackground(String... params)
        {
            String result = "";
            try
            {
                URL url = new URL(getResources().getString(R.string.direccion_web) + "/Controlador/actualizar_datos_usuario.php");
                try
                {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                    String json = "";
                    JSONObject object = new JSONObject();

                    String post_data = "";
                    if (tipo_registro.equals("Nombre"))
                    {
                        try
                        {
                            object.put("id", params[0]);
                            object.put("nombre_us", params[1]);
                            object.put("clave_us", params[2]);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        json = object.toString();
                        post_data = URLEncoder.encode("json_data","UTF-8")+"="+URLEncoder.encode(json, "UTF-8");
                    }
                    else if(tipo_registro.equals("Apellido"))
                    {
                        try
                        {
                            object.put("id", params[0]);
                            object.put("apellido_us", params[1]);
                            object.put("clave_us", params[2]);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        json = object.toString();
                        post_data = URLEncoder.encode("json_data","UTF-8")+"="+URLEncoder.encode(json, "UTF-8");
                    }
                    else if (tipo_registro.equals("Nickname"))
                    {
                        try
                        {
                            object.put("id", params[0]);
                            object.put("nickname_us", params[1]);
                            object.put("clave_us", params[2]);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        json = object.toString();
                        post_data = URLEncoder.encode("json_data","UTF-8")+"="+URLEncoder.encode(json, "UTF-8");
                    }
                    else if (tipo_registro.equals("Clave"))
                    {
                        try
                        {
                            object.put("id", params[0]);
                            object.put("clave_nueva_us", params[1]);
                            object.put("clave_us", params[2]);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        json = object.toString();
                        post_data = URLEncoder.encode("json_data","UTF-8")+"="+URLEncoder.encode(json, "UTF-8");
                    }
                    else if (tipo_registro.equals("Correo"))
                    {
                        try
                        {
                            object.put("id", params[0]);
                            object.put("correo_us", params[1]);
                            object.put("clave_us", params[2]);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        json = object.toString();
                        post_data = URLEncoder.encode("json_data","UTF-8")+"="+URLEncoder.encode(json, "UTF-8");
                    }

                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String line="";
                    while ((line = bufferedReader.readLine())!=null)
                    {
                        result+=line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            return result;
        }
        @Override
        protected void onPostExecute(String s)
        {
            try
            {
                JSONObject jsonResult = new JSONObject(s);
                if (jsonResult != null)
                {
                    if (jsonResult.getString("Clave").equals("Incorrecto"))
                    {
                        if (tipo_registro.equals("Clave"))
                        {
                            txtActualClave.setError("La clave es incorrecta.");
                        }
                        else
                        {
                            txtClaveUser.setError("La clave es incorrecta.");
                        }
                    }
                    else if (jsonResult.getString("Clave").equals("Correcto"))
                    {
                        if (tipo_registro.equals("Nombre"))
                        {
                            eNombre.setText(getResources().getString(R.string.usuario_nombre) + " " + txtVariable.getText().toString());
                            Toast.makeText(getContext(), "Se ha modificado tu nombre por: " + txtVariable.getText().toString(), Toast.LENGTH_SHORT).show();
                            dialogCrudUsuario.cancel();
                            dialogUp.cancel();
                        }
                        else if (tipo_registro.equals("Apellido"))
                        {
                            eApellido.setText(getResources().getString(R.string.usuario_apellido) + " " + txtVariable.getText().toString());
                            Toast.makeText(getContext(), "Se ha modificado tu apellido por: " + txtVariable.getText().toString(), Toast.LENGTH_SHORT).show();
                            dialogCrudUsuario.cancel();
                            dialogUp.cancel();
                        }
                        else if (tipo_registro.equals("Nickname"))
                        {
                            eNickname.setText(getResources().getString(R.string.usuario_nickname) + " " + txtVariable.getText().toString());
                            Toast.makeText(getContext(), "Se ha modificado tu nickname por: " + txtVariable.getText().toString(), Toast.LENGTH_SHORT).show();
                            dialogCrudUsuario.cancel();
                            dialogUp.cancel();
                        }
                        else if (tipo_registro.equals("Clave"))
                        {
                            dialogChangeClave.cancel();
                            dialogCrudUsuario.cancel();
                            Toast.makeText(getContext(), "Tu clave se ha modificado correctamente.", Toast.LENGTH_SHORT).show();
                        }
                        else if (tipo_registro.equals("Correo"))
                        {
                            eCorreo.setText(getResources().getString(R.string.correo_usuario) + " " + txtVariable.getText().toString());
                            Toast.makeText(getContext(), "Se ha modificado tu correo por: " + txtVariable.getText().toString(), Toast.LENGTH_SHORT).show();
                            dialogCrudUsuario.cancel();
                            dialogUp.cancel();
                        }
                    }
                    else if (jsonResult.getString("Clave").equals("Nickname_existe"))
                    {
                        txtVariable.setError("Este nickname ya esta en uso.");
                    }
                    else if (jsonResult.getString("Clave").equals("Correo_existe"))
                    {
                        txtVariable.setError("Este correo ya esta en uso.");
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "No trajo datos :(", Toast.LENGTH_SHORT).show();
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
    public class updateDireccion_or_telefono extends AsyncTask<String, Void, String>
    {
        @Override
        public String doInBackground(String... params)
        {
            String result = "";
            try
            {
                URL url = null;
                if (tipo_registro.equals("Direccion"))
                {
                    url = new URL(getResources().getString(R.string.direccion_web) + "/Controlador/actualizar_direccion_usuario.php");
                }
                else if (tipo_registro.equals("Telefono"))
                {
                    url = new URL(getResources().getString(R.string.direccion_web) + "/Controlador/actualizar_telefono_usuario.php");
                }
                try
                {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                    String json = "";
                    JSONObject object = new JSONObject();
                    String post_data = "";
                    if (tipo_registro.equals("Direccion"))
                    {
                        try
                        {
                            object.put("calle_antigua", params[0]);
                            object.put("calle_num_antigua", params[1]);
                            object.put("calle_nueva", params[2]);
                            object.put("calle_num_nueva", params[3]);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        json = object.toString();
                        post_data = URLEncoder.encode("json_data","UTF-8")+"="+URLEncoder.encode(json,"UTF-8");
                    }
                    else if (tipo_registro.equals("Telefono"))
                    {
                        post_data = URLEncoder.encode("id_us","UTF-8")+"="+URLEncoder.encode(params[0],"UTF-8") + "&" +
                                URLEncoder.encode("num_an","UTF-8")+"="+URLEncoder.encode(params[1],"UTF-8") + "&" +
                                URLEncoder.encode("num_new","UTF-8")+"="+URLEncoder.encode(params[2],"UTF-8");

                        //updateTel.execute(id, telefono_ant, txtnuevoTel.getText().toString());
                    }

                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String line="";
                    while ((line = bufferedReader.readLine())!=null)
                    {
                        result+=line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s)
        {
            try
            {
                JSONObject jsonResult = new JSONObject(s);
                if (jsonResult != null)
                {
                    if (tipo_registro.equals("Direccion"))
                    {
                        if (jsonResult.getString("Actualizado").equals("Si"))
                        {
                            dialogCrudUsuario.cancel();
                            dialogChangeDireccionUpdate.cancel();
                            Toast.makeText(getContext(), "Direccion actualizada.", Toast.LENGTH_LONG).show();

                            cargar();
                        }
                    }
                    else if (tipo_registro.equals("Telefono"))
                    {
                        if (jsonResult.getString("Actualizado").equals("Si"))
                        {
                            dialogCrudUsuario.cancel();
                            dialogChangeDireccionTelefono.cancel();
                            Toast.makeText(getContext(), "Telefono modificado.", Toast.LENGTH_LONG).show();
                            cargar();
                        }
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "No trajo datos :(", Toast.LENGTH_SHORT).show();
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
    public class eliminarDatos extends AsyncTask<String, Void, String>
    {
        @Override
        public String doInBackground(String... params)
        {
            String result = "";
            try
            {
                URL url = new URL(getResources().getString(R.string.direccion_web) + "/Controlador/eliminar_datos_usuario.php");
                try
                {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                    String json = "";
                    String post_data = "";
                    JSONObject object = new JSONObject();
                    if (tipo_delete.equals("Telefono"))
                    {
                        try
                        {
                            object.put("user_id", params[0]);
                            object.put("telefono_us", params[1]);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        json = object.toString();
                        post_data = URLEncoder.encode("json_data","UTF-8")+"="+URLEncoder.encode(json,"UTF-8");
                    }
                    else if (tipo_delete.equals("Direccion"))
                    {
                        try
                        {
                            object.put("user_id", params[0]);
                            object.put("calle_us", params[1]);
                            object.put("callenum_us", params[2]);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        json = object.toString();
                        post_data = URLEncoder.encode("json_data","UTF-8")+"="+URLEncoder.encode(json,"UTF-8");
                    }

                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String line="";
                    while ((line = bufferedReader.readLine())!=null)
                    {
                        result+=line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s)
        {
            try
            {
                JSONObject jsonResult = new JSONObject(s);
                if (jsonResult != null)
                {
                    if (tipo_delete.equals("Telefono"))
                    {
                        if (jsonResult.getString("Eliminado").equals("Si"))
                        {
                            dialogChangeDelete.cancel();
                            dialogCrudUsuario.cancel();
                            cargar();
                            Toast.makeText(getContext(), "Telefono eliminado.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (tipo_delete.equals("Direccion"))
                    {
                        if(jsonResult.getString("Eliminado").equals("Si"))
                        {
                            dialogCrudUsuario.cancel();
                            dialogChangeDireccionn.cancel();
                            cargar();
                            Toast.makeText(getContext(), "Direccion eliminada", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "No trajo datos :(", Toast.LENGTH_SHORT).show();
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
    public class agregarDatos extends AsyncTask<String, Void, String>
    {
        @Override
        public String doInBackground(String... params)
        {
            String result = "";
            try
            {
                URL url = new URL(getResources().getString(R.string.direccion_web) + "/Controlador/insertar_datos_usuario.php");
                try
                {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                    String json = "";
                    JSONObject object = new JSONObject();
                    String post_data = "";
                    if (tipo_add.equals("Telefono"))
                    {
                        try
                        {
                            object.put("user_id", params[0]);
                            object.put("telefono_us", params[1]);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        json = object.toString();
                        post_data = URLEncoder.encode("json_data","UTF-8")+"="+URLEncoder.encode(json,"UTF-8");
                    }
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String line="";
                    while ((line = bufferedReader.readLine())!=null)
                    {
                        result+=line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s)
        {
            try
            {
                JSONObject jsonResult = new JSONObject(s);
                if (jsonResult != null)
                {
                    if (jsonResult.getString("Agregado").equals("Si"))
                    {
                        dialogChangeTelefono.cancel();
                        dialogCrudUsuario.cancel();
                        Toast.makeText(getContext(), "Telefono agregado.", Toast.LENGTH_SHORT).show();
                        //eTel2.setText(getResources().getString(R.string.numtel_dos) + " " + telefono);
                        cargar();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "No trajo datos :(", Toast.LENGTH_SHORT).show();
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
    public class agregarDireccion extends AsyncTask<String, Void, String>
    {
        @Override
        public String doInBackground(String... params)
        {
            String result = "";
            try
            {
                URL url = new URL(getResources().getString(R.string.direccion_web) + "/Controlador/agregar_direccion_usuario.php");
                try
                {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                    String json = "";
                    JSONObject object = new JSONObject();
                    try
                    {
                        object.put("user_id", params[0]);
                        object.put("calle_us", params[1]);
                        object.put("numero_calle_us", params[2]);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    json = object.toString();
                    String post_data = URLEncoder.encode("json_data","UTF-8")+"="+URLEncoder.encode(json, "UTF-8");

                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String line="";
                    while ((line = bufferedReader.readLine())!=null)
                    {
                        result+=line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s)
        {
            try
            {
                JSONObject jsonResult = new JSONObject(s);
                if (jsonResult != null)
                {
                    if (jsonResult.getString("Direccion").equals("Si"))
                    {
                        dialogAgregarDireccion.cancel();
                        dialogCrudUsuario.cancel();
                        cargar();
                        Toast.makeText(getContext(), "Direccion agregada", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "No trajo datos :(", Toast.LENGTH_SHORT).show();
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}