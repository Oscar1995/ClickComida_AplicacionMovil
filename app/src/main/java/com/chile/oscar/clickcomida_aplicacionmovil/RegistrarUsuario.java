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
    Button btnAtras, btnContinuar;
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
        btnContinuar = (Button)findViewById(R.id.btnContinuar);
        btnAtras = (Button)findViewById(R.id.btnback);

        btnContinuar.setOnClickListener(this);
        btnAtras.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnback:
                this.finish();
                break;

            case R.id.btnContinuar:
                Intent i = new Intent(this, RegistrarUsuarioContinuacion.class);
                startActivity(i);
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
