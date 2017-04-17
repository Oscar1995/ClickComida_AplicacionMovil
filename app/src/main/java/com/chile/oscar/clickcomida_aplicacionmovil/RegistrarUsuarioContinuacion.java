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

public class RegistrarUsuarioContinuacion extends AppCompatActivity implements View.OnClickListener
{

    WebService contenido = new WebService();

    EditText txtPasaje, txtNumeroPasaje, txtNickname, txtTelefono, txtTelefonoOpcional;
    Button btnRegistroFinal;
    TextView txtInformacion;
    int id_usuario = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario_continuacion);

        txtPasaje = (EditText)findViewById(R.id.etPasajeRegistro);
        txtNumeroPasaje = (EditText)findViewById(R.id.etNumeroRegistro);
        txtNickname = (EditText)findViewById(R.id.etNicknameRegistro);
        txtTelefono = (EditText)findViewById(R.id.etTelefonoRegistro);
        txtTelefonoOpcional = (EditText)findViewById(R.id.etTelefonoOpcionalRegistro);
        txtInformacion = (TextView)findViewById(R.id.tvInfo);
        btnRegistroFinal = (Button)findViewById(R.id.btnRegistrarUsuarioFinal);

        txtInformacion.setText("Hola " + getIntent().getStringExtra("nombre_usuario") + ", te pediremos algunos datos antes de empezar a usar la aplicación.");
        String idUsuarioCadena = getIntent().getStringExtra("usuario_id");

        id_usuario = Integer.parseInt(idUsuarioCadena);
        btnRegistroFinal.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnRegistrarUsuarioFinal:

                String pasajeNombre, uNick, nTelefonoOpcional;
                int numeroPasaje, nTelefono;

                pasajeNombre = txtPasaje.getText().toString().trim();
                uNick = txtNickname.getText().toString().trim();
                numeroPasaje = Integer.parseInt(txtNumeroPasaje.getText().toString().trim());
                nTelefono = Integer.parseInt(txtTelefono.getText().toString().trim());
                nTelefonoOpcional = txtTelefonoOpcional.getText().toString().trim();

                if (nTelefonoOpcional.isEmpty())
                {
                    new EjecutarSentencia().execute("http://192.168.43.71/clickcomida/cargar_usuario_final.php?id="+id_usuario+"&&pasaje="+pasajeNombre+"&&numerocalle="+numeroPasaje+"&&nickname="+uNick+"&&telefono="+nTelefono+"&&telefono_opcional=no");
                }
                else
                {
                    new EjecutarSentencia().execute("http://192.168.43.71/clickcomida/cargar_usuario_final.php?id="+id_usuario+"&&pasaje="+pasajeNombre+"&&numerocalle="+numeroPasaje+"&&nickname="+uNick+"&&telefono="+nTelefono+"&&telefono_opcional="+nTelefonoOpcional);
                }
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
                String nickname = jao.getString("nickname");

                if (nickname == "existe")
                {
                    txtNickname.setError("Este nickname ya esta en uso.");
                }
                else
                {
                    //Toast.makeText(getApplicationContext(), "Insertado.", Toast.LENGTH_SHORT).show();
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
