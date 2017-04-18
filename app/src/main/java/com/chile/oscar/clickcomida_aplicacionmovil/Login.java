package com.chile.oscar.clickcomida_aplicacionmovil;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Login extends AppCompatActivity implements View.OnClickListener
{

    WebService contenido = new WebService();
    Button btnIniciar, btnRegistro, btnOlvidado;
    public EditText txtCorreo, txtClave;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnIniciar = (Button)findViewById(R.id.btnIniciarSesion);
        btnRegistro = (Button)findViewById(R.id.btnRegistrate);
        btnOlvidado = (Button)findViewById(R.id.btnClaveOlvidada);
        txtCorreo = (EditText)findViewById(R.id.etCorreoLogin);
        txtClave = (EditText)findViewById(R.id.etClaveLogin);

        btnIniciar.setOnClickListener(this);
        btnRegistro.setOnClickListener(this);
        btnOlvidado.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnIniciarSesion:
                boolean correoCorrecto = false;
                String correoElectronico = txtCorreo.getText().toString().trim();
                String claveUsuario = txtClave.getText().toString().trim();

                int nCaracterCorreo = correoElectronico.length();
                int nCaracterClave = claveUsuario.length();

                if (nCaracterCorreo > 0 && nCaracterClave > 0)
                {
                    if (correoElectronico.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))
                    {
                        correoCorrecto = true;
                    }
                    if (correoCorrecto == true)
                    {
                        String ip = getResources().getString(R.string.direccion_ip);
                        new ConsultarDatos().execute("http://"+ip+"/clickcomida/consultar_usuario_login.php?correo="+correoElectronico+ "&&clave="+claveUsuario);
                    }
                    else
                    {
                        txtCorreo.setError("Formato invalido.");
                    }
                }
                else
                {
                    if (nCaracterCorreo == 0 && nCaracterClave == 0)
                    {
                        txtCorreo.setError("Rellena este campo.");
                        txtClave.setError("Rellena este campo.");
                    }
                    else if (nCaracterCorreo > 0 && nCaracterClave == 0)
                    {
                        if (!correoElectronico.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))
                        {
                            txtCorreo.setError("Formato Invalido.");
                        }
                        txtClave.setError("Rellena este campo.");
                    }
                    else if (nCaracterCorreo == 0 && nCaracterClave > 0)
                    {
                        txtCorreo.setError("Rellena este campo.");
                    }
                }


                break;

            case R.id.btnRegistrate:
                Intent abrirRegistro = new Intent(Login.this, RegistrarUsuario.class);
                startActivity(abrirRegistro);
                break;

            case R.id.btnClaveOlvidada:
                break;
        }
    }
    private class ConsultarDatos extends AsyncTask<String, Void, String>
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
                String afirmacion = jao.getString("datos");
                if (afirmacion.equals("verdad"))
                {
                    String nombre_usuario = jao.getString("nombre");
                    Intent abrirInicio = new Intent(Login.this, InicioUsuario.class);
                    abrirInicio.putExtra("correo_usuario", txtCorreo.getText().toString());
                    abrirInicio.putExtra("nombre_usuario", nombre_usuario);
                    startActivity(abrirInicio);

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "El correo o clave son incorrectos.", Toast.LENGTH_SHORT).show();
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
    }
}
