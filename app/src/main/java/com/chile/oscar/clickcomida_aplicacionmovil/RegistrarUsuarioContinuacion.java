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
    String correoUsuario = "";
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

        /*txtInformacion.setText("Hola " + getIntent().getStringExtra("nombre_usuario") + ", te pediremos algunos datos antes de empezar a usar la aplicación.");
        String idUsuarioCadena = getIntent().getStringExtra("usuario_id");
        correoUsuario = getIntent().getStringExtra("correo_usuario");

        id_usuario = Integer.parseInt(idUsuarioCadena);*/
        btnRegistroFinal.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnRegistrarUsuarioFinal:
                Intent i = new Intent(this, InicioUsuario.class);
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
                String nickname = jao.getString("nickname");

                if (nickname == "existe")
                {
                    txtNickname.setError("Este nickname ya esta en uso.");
                }
                else
                {
                    Intent i = new Intent(RegistrarUsuarioContinuacion.this, InicioUsuario.class);
                    i.putExtra("correo_usuario", correoUsuario);
                    i.putExtra("nombre_usuario", txtNickname.getText().toString());
                    startActivity(i);
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
