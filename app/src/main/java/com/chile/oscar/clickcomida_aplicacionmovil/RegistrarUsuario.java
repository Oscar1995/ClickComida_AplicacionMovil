package com.chile.oscar.clickcomida_aplicacionmovil;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RegistrarUsuario extends AppCompatActivity implements View.OnClickListener
{
    WebService contenido = new WebService();
    Button btnAtras, btnRegistro;
    EditText txtCorreo, txtClave, txtClaveR, txtNombre, txtApellido;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);

        txtCorreo = (EditText)findViewById(R.id.etCorreo);
        txtClave = (EditText)findViewById(R.id.etClave);
        txtClaveR = (EditText)findViewById(R.id.etClaveRep);
        txtNombre = (EditText)findViewById(R.id.etNombre);
        txtApellido = (EditText)findViewById(R.id.etApellido);
        btnRegistro = (Button)findViewById(R.id.btnRegistrarUsuario);
        btnAtras = (Button)findViewById(R.id.btnback);

        btnRegistro.setOnClickListener(this);
        btnAtras.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnback:
                break;

            case R.id.btnRegistrarUsuario:
                /*String cUno = txtClave.getText().toString().trim();
                String cDos = txtClaveR.getText().toString().trim();

                boolean datosCorrectos = false;
                int nCorreo = txtCorreo.getText().length();
                int nClave = txtClave.getText().length();
                int nClaveRep = txtClaveR.getText().length();
                int nNombre = txtNombre.getText().length();
                int nApellido = txtApellido.getText().length();

                if (nCorreo == 0 || nCorreo != 0)
                {
                    if (nCorreo == 0)
                    {
                        txtCorreo.setError("Complete este campo.");
                        datosCorrectos = false;
                    }
                    else
                    {
                        if (!txtCorreo.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))
                        {
                            txtCorreo.setError("Formato Invalido.");
                            datosCorrectos = false;
                        }
                        else
                        {
                            datosCorrectos = true;
                        }
                    }

                }
                if (nClave == 0 || nClave != 0)
                {
                    if (nClave == 0)
                    {
                        txtClave.setError("Complete este campo.");
                        datosCorrectos = false;
                    }
                    else
                    {
                        if (nClave <= 4)
                        {
                            txtClave.setError("La clave debe tener al menos 5 caracteres.");
                            datosCorrectos = false;
                        }
                        else
                        {
                            datosCorrectos = true;
                        }
                    }
                }
                if (nClaveRep == 0 || nClaveRep != 0)
                {
                    if (nClaveRep == 0)
                    {
                        txtClaveR.setError("Complete este campo.");
                        datosCorrectos = false;
                    }
                    else
                    {
                        if (!txtClave.getText().toString().equals(txtClaveR.getText().toString()))
                        {
                            txtClaveR.setError("No coincide con el campo clave.");
                            datosCorrectos = false;
                        }
                        else
                        {
                            datosCorrectos = true;
                        }
                    }
                }
                if (nNombre == 0 || nNombre != 0)
                {
                    if (nNombre == 0)
                    {
                        txtNombre.setError("Complete este campo.");
                        datosCorrectos = false;
                    }
                    else
                    {
                        datosCorrectos = true;
                    }
                }
                if (nApellido == 0 || nApellido !=0)
                {
                    if (nApellido == 0)
                    {
                        txtApellido.setError("Complete este campo.");
                        datosCorrectos = false;
                    }
                    else
                    {
                        datosCorrectos = true;
                    }

                }
                if (datosCorrectos == true)
                {
                    String uCorreo, uClave, uNombre, uApellido;
                    uCorreo = txtCorreo.getText().toString().trim();
                    uClave = txtClave.getText().toString().trim();
                    uNombre = txtNombre.getText().toString().trim();
                    uApellido = txtApellido.getText().toString().trim();


                    String ip = getResources().getString(R.string.direccion_ip);
                    new EjecutarSentencia().execute("http://"+ip+"/clickcomida/cargar_usuario.php?nombre="+uNombre+"&&apellido="+uApellido+"&&correo="+uCorreo+"&&clave="+uClave);
                }*/
                break;

        }
    }
    private class EjecutarSentencia extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {
            // params comes from the execute() call: params[0] is the url.
            try
            {
                return contenido.downloadUrl(urls[0]);
            }
            catch (IOException e)
            {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            JSONObject jao = null;
            try
            {
                jao = new JSONObject(result);
                String laConfirmacion = jao.getString("email");
                String idUsuario = jao.getString("id_usuario");
                if (laConfirmacion.equals("yes"))
                {
                    Toast.makeText(getApplicationContext(), "El correo ya existe.", Toast.LENGTH_SHORT).show();
                    txtCorreo.setError("Este correo ya se encuentra en uso.");
                }
                else
                {
                    Intent abrirRegistroContinuacion = new Intent(RegistrarUsuario.this, RegistrarUsuarioContinuacion.class);
                    abrirRegistroContinuacion.putExtra("nombre_usuario", txtNombre.getText().toString());
                    abrirRegistroContinuacion.putExtra("usuario_id", idUsuario);
                    abrirRegistroContinuacion.putExtra("correo_usuario", txtCorreo.getText().toString());
                    startActivity(abrirRegistroContinuacion);
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
