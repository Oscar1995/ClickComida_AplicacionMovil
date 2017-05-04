package com.chile.oscar.clickcomida_aplicacionmovil;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
    TextView eNombre, eApellido, eNickname, eCorreo, eTipo, eTel1, eTel2, calle, numCalle;
    EditText txtVariable ,txtClaveUser;
    FloatingActionButton fbUpdateUser;
    String id;
    AlertDialog dialogCrudUsuario;
    View view, vUpdate;
    AlertDialog dialogUp;

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

        fbUpdateUser.setOnClickListener(this);

        Bundle bundle = getArguments();
        id = bundle.getString("IdUser");

        TraerDatos obj = new TraerDatos();
        obj.execute(id);

        return view;
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
    public void createdd()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View v = getActivity().getLayoutInflater().inflate(R.layout.crud_datos_usuario, null);
        builder.setView(v);
        dialogCrudUsuario = builder.create();
        dialogCrudUsuario.show();

        Button botonAceptar = (Button) v.findViewById(R.id.btnAceptar);
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
                        final AlertDialog dialogChange = builderChange.create();
                        dialogChange.show();


                        Button btnCanTel = (Button)p.findViewById(R.id.btnCancelarTelefono);
                        btnCanTel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                dialogChange.cancel();
                            }
                        });
                    }
                    else if (positionElement == 1) //
                    {
                        //Direccion
                        AlertDialog.Builder builderChange = new AlertDialog.Builder(getContext());
                        View p = getActivity().getLayoutInflater().inflate(R.layout.agregar_direccion_usuario, null);
                        builderChange.setView(p);
                        final AlertDialog dialogChange = builderChange.create();
                        dialogChange.show();
                        Button btnDir = (Button)p.findViewById(R.id.btnCancelarDireccion);
                        btnDir.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                dialogChange.cancel();
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
                        AlertDialog dialogChange = builderChange.create();
                        dialogChange.show();

                        final EditText txtActualClave = (EditText)p.findViewById(R.id.txtClaveActual_us);
                        final EditText txtNuevaClave = (EditText)p.findViewById(R.id.txtClaveNueva_us);
                        final EditText txtNuevaClaveRep = (EditText)p.findViewById(R.id.txtClaveNuevaRep_us);

                        Button botonCambiarClave = (Button)p.findViewById(R.id.btnCambiarClave);

                        botonCambiarClave.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
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
                        AlertDialog dialogChange = builderChange.create();
                        dialogChange.show();

                        String[] telefonos = null;
                        if (!eTel2.getText().toString().equals("false"))
                        {
                            telefonos = new String[]{eTel1.getText().toString(), eTel2.getText().toString()};
                        }

                        ListView listView = (ListView)p.findViewById(R.id.lv_d_f);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, telefonos);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                Toast.makeText(getContext(), "Numero: " + position, Toast.LENGTH_SHORT).show();
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
                        AlertDialog dialogChange = builderChange.create();
                        dialogChange.show();
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
                    String[] dElements = {"Nombre", "Apellido", "Nickname", "Correo electronico", "Clave"};
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

        /*builder.setView(v);
        final AlertDialog dialog = builder.create();
        dialog.show();*/
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
    public void dialogoSingular(AlertDialog dialog, String tipo)
    {
        AlertDialog.Builder builderUpdate = new AlertDialog.Builder(getContext());
        vUpdate = getActivity().getLayoutInflater().inflate(R.layout.modificar_datos_usuario_singular, null);
        builderUpdate.setView(vUpdate);
        dialogUp = builderUpdate.create();
        dialog.cancel();

        if (tipo.equals("Nombre"))
        {
            manipularTexto(vUpdate, getResources().getString(R.string.tu_nombre_nuevo));
            Button btnModUs = (Button)vUpdate.findViewById(R.id.btnModificarUsuario);
            txtVariable = (EditText)vUpdate.findViewById(R.id.etVariable);
            txtClaveUser = (EditText)vUpdate.findViewById(R.id.etClaveVariable);

            btnModUs.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    tipo_registro = "Nombre";
                    modificarUsuario updateUs = new modificarUsuario();
                    updateUs.execute(id, txtVariable.getText().toString(), txtClaveUser.getText().toString());
                }
            });
        }
        else if (tipo.equals("Apellido"))
        {
            manipularTexto(vUpdate, getResources().getString(R.string.tu_apellido_nuevo));
            Button btnModUs = (Button)vUpdate.findViewById(R.id.btnModificarUsuario);
            txtVariable = (EditText)vUpdate.findViewById(R.id.etVariable);
            txtClaveUser = (EditText)vUpdate.findViewById(R.id.etClaveVariable);
            btnModUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    tipo_registro = "Apellido";
                    modificarUsuario updateUs = new modificarUsuario();
                    updateUs.execute(id, txtVariable.getText().toString(), txtClaveUser.getText().toString());
                }
            });

        }
        else if (tipo.equals("Nickname"))
        {
            manipularTexto(vUpdate, getResources().getString(R.string.tu_nickname_nuevo));
            Button btnModUs = (Button)vUpdate.findViewById(R.id.btnModificarUsuario);
            txtVariable = (EditText)vUpdate.findViewById(R.id.etVariable);
            txtClaveUser = (EditText)vUpdate.findViewById(R.id.etClaveVariable);
            btnModUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    tipo_registro = "Nickname";
                    modificarUsuario updateUs = new modificarUsuario();
                    updateUs.execute(id, txtVariable.getText().toString(), txtClaveUser.getText().toString());
                }
            });
        }
        else if (tipo.equals("Correo"))
        {
            manipularTexto(vUpdate, getResources().getString(R.string.tu_correo_nuevo));
            Button btnModUs = (Button)vUpdate.findViewById(R.id.btnModificarUsuario);
            txtVariable = (EditText)vUpdate.findViewById(R.id.etVariable);
            txtClaveUser = (EditText)vUpdate.findViewById(R.id.etClaveVariable);
            btnModUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    tipo_registro = "Correo";
                    modificarUsuario updateUs = new modificarUsuario();
                    updateUs.execute(id, txtVariable.getText().toString(), txtClaveUser.getText().toString());
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
                URL url = new URL("http://clickcomida.esy.es/Controlador/datos_usuario.php");
                try
                {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                    String post_data= URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(params[0],"UTF-8");

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
                    eTel2.setText(getResources().getString(R.string.numtel_dos) + " " + jsonResult.getString("telefonoDos"));
                    calle.setText(getResources().getString(R.string.calle_usuario) + " " + jsonResult.getString("Calle"));
                    numCalle.setText(getResources().getString(R.string.calle_numero_usuario) + " " + jsonResult.getString("Numero_calle"));

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
    public class modificarUsuario extends AsyncTask<String, Void, String>
    {
        @Override
        public String doInBackground(String... params)
        {
            String result = "";
            try
            {
                URL url = new URL("http://clickcomida.esy.es/Controlador/actualizar_datos_usuario.php");
                try
                {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                    String post_data = "";
                    if (tipo_registro.equals("Nombre"))
                    {
                        post_data = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(params[0],"UTF-8") + "&" +
                                URLEncoder.encode("nombre_us","UTF-8")+"="+URLEncoder.encode(params[1],"UTF-8") + "&" +
                                URLEncoder.encode("clave_us","UTF-8")+"="+URLEncoder.encode(params[2],"UTF-8");
                    }
                    else if(tipo_registro.equals("Apellido"))
                    {
                        post_data = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(params[0],"UTF-8") + "&" +
                                URLEncoder.encode("apellido_us","UTF-8")+"="+URLEncoder.encode(params[1],"UTF-8") + "&" +
                                URLEncoder.encode("clave_us","UTF-8")+"="+URLEncoder.encode(params[2],"UTF-8");
                    }
                    else if (tipo_registro.equals("Nickname"))
                    {
                        post_data = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(params[0],"UTF-8") + "&" +
                                URLEncoder.encode("nickname_us","UTF-8")+"="+URLEncoder.encode(params[1],"UTF-8") + "&" +
                                URLEncoder.encode("clave_us","UTF-8")+"="+URLEncoder.encode(params[2],"UTF-8");
                    }
                    else if (tipo_registro.equals("Clave"))
                    {
                        post_data = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(params[0],"UTF-8") + "&" +
                                URLEncoder.encode("clave_nueva_us","UTF-8")+"="+URLEncoder.encode(params[1],"UTF-8") + "&" +
                                URLEncoder.encode("clave_us","UTF-8")+"="+URLEncoder.encode(params[2],"UTF-8");
                    }
                    else if (tipo_registro.equals("Correo"))
                    {
                        post_data = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(params[0],"UTF-8") + "&" +
                                URLEncoder.encode("correo_us","UTF-8")+"="+URLEncoder.encode(params[1],"UTF-8") + "&" +
                                URLEncoder.encode("clave_us","UTF-8")+"="+URLEncoder.encode(params[2],"UTF-8");
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
                        txtClaveUser.setError("La clave es incorrecta.");
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
}