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
                Intent abrirLogin = new Intent(RegistrarUsuario.this, Login.class);
                startActivity(abrirLogin);
                break;

            case R.id.btnRegistrarUsuario:
                String cUno = txtClave.getText().toString().trim();
                String cDos = txtClaveR.getText().toString().trim();
                if (cUno.equals(cDos))
                {
                    new CargarDatos().execute("http://192.168.43.71/clickcomida/cargar_usuario.php?nombre="+txtNombre.getText().toString().trim()+"&&apellido="+txtApellido.getText().toString().trim()+"&&correo="+txtCorreo.getText().toString().trim()+"&&clave="+txtClave.getText().toString().trim());
                }
                else
                {
                    txtClave.setText("");
                    txtClaveR.setText("");
                    Toast.makeText(getApplicationContext(), "Tu clave no coincide, repite tu clave nuevamente.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private class CargarDatos extends AsyncTask<String, Void, String>
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
                if (laConfirmacion.equals("yes"))
                {
                    Toast.makeText(getApplicationContext(), "El correo ya existe.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Bienvenido " + txtNombre.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
