package com.chile.oscar.clickcomida_aplicacionmovil;


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
    FloatingActionButton fbUpdateUser;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_mis_datos, container, false);
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
        String id = bundle.getString("IdUser");

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
                    eNombre.setText(getResources().getString(R.string.usuario_nombre) + " " + jsonResult.getString("Nombre"));
                    eApellido.setText(getResources().getString(R.string.usuario_apellido) + " " + jsonResult.getString("Apellido"));
                    eNickname.setText(getResources().getString(R.string.usuario_nickname) + " " + jsonResult.getString("Nickname"));
                    eCorreo.setText(getResources().getString(R.string.correo_usuario) + " " + jsonResult.getString("Email"));
                    eTipo.setText(getResources().getString(R.string.tipo_usuario) + " " + jsonResult.getString("Rol"));
                    eTel1.setText(getResources().getString(R.string.numtel_uno) + " " + jsonResult.getString("Telefono"));
                    eTel2.setText(getResources().getString(R.string.numtel_dos) + " " + jsonResult.getString("telefonoDos"));
                    calle.setText(getResources().getString(R.string.calle_usuario) + " " + jsonResult.getString("Calle"));
                    numCalle.setText(getResources().getString(R.string.calle_numero_usuario) + " " + jsonResult.getString("Numero_calle"));

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
    int positionCrudSelected;
    int positionElement;
    public void createdd()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View v = getActivity().getLayoutInflater().inflate(R.layout.crud_datos_usuario, null);
        builder.setView(v);
        final AlertDialog dialog = builder.create();
        dialog.show();

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
                    }
                    else if (positionElement == 1) //
                    {
                        //Direccion
                    }
                }
                if (positionCrudSelected == 1) //Modificar
                {
                    if (positionElement == 0)
                    {
                        //Nombre
                        dialogoSingular(dialog, "Nombre");

                    }
                    else if (positionElement == 1)
                    {
                        //Apellido
                        dialogoSingular(dialog, "Apellido");
                    }
                    else if (positionElement == 2)
                    {
                        //Nickname
                        dialogoSingular(dialog, "Nickname");
                    }
                    else if (positionElement == 3)
                    {
                        //Correo
                        dialogoSingular(dialog, "Correo");
                    }
                    else if (positionElement == 4)
                    {
                        //Clave
                    }
                }
                if (positionCrudSelected == 2) //Eliminar
                {
                    if (positionElement == 0)
                    {
                        //Telefono
                    }
                    else if (positionElement == 1)
                    {
                        //Direccion
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
                dialog.cancel();
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
        View p = getActivity().getLayoutInflater().inflate(R.layout.modificar_datos_usuario_singular, null);
        builderUpdate.setView(p);
        AlertDialog dialogUp = builderUpdate.create();
        dialog.cancel();
        if (tipo.equals("Nombre"))
        {
            manipularTexto(p, getResources().getString(R.string.tu_nombre_nuevo));
        }
        else if (tipo.equals("Apellido"))
        {
            manipularTexto(p, getResources().getString(R.string.tu_apellido_nuevo));
        }
        else if (tipo.equals("Nickname"))
        {
            manipularTexto(p, getResources().getString(R.string.tu_nickname_nuevo));
        }
        else if (tipo.equals("Correo"))
        {
            manipularTexto(p, getResources().getString(R.string.tu_correo_nuevo));
        }
        dialogUp.show();
    }
}