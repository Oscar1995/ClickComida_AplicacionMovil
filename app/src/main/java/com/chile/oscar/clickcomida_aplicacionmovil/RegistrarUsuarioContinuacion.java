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

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Validadores;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RegistrarUsuarioContinuacion extends AppCompatActivity implements View.OnClickListener
{

    WebService contenido = new WebService();
    Validadores mValidar = new Validadores();
    EditText txtPasaje, txtNumeroPasaje, txtNickname, txtTelefono, txtTelefonoOpcional;
    Button btnRegistroFinal;
    TextView txtInformacion;

    String getCorreo;
    String getClave;
    String getNombre;
    String getApellido;
    String correoUsuario;
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

        getCorreo = getIntent().getStringExtra("correo_usuario");
        getClave = getIntent().getStringExtra("clave_usuario");
        getNombre = getIntent().getStringExtra("nombre_usuario");
        getApellido = getIntent().getStringExtra("apellido_usuario");

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

                boolean isCorrectPasaje = false;
                boolean isCorrectNumero = false;
                boolean isCorrectNickname = false;
                boolean isCorrectTelefono = false;

                Intent i = new Intent(this, Inicio_Usuario.class);

                if (txtPasaje.getText().toString().isEmpty())
                {
                    txtPasaje.setError("Ingrese un pasaje, ejemplo: Chillán 697");
                    isCorrectPasaje = false;
                }
                else
                {
                    isCorrectPasaje = true;
                }

                if (txtNumeroPasaje.getText().toString().isEmpty())
                {
                    txtNumeroPasaje.setError("Ingrese un pasaje, ejemplo: #1921");
                    isCorrectNumero = false;
                }
                else
                {
                    if (mValidar.isLetter(txtNumeroPasaje.getText().toString().trim()) == true)
                    {
                        txtNumeroPasaje.setError("Solo numeros, ejemplo: #1921");
                        isCorrectNumero = false;
                    }
                    else
                    {
                        isCorrectNumero = true;
                    }
                }

                if (txtNickname.getText().toString().isEmpty())
                {
                    txtNickname.setError("Ingrese un Nickname, Ejemplo: JuanPerez90");
                    isCorrectNickname = false;
                }
                else
                {
                    isCorrectNickname = true;
                }

                if (txtTelefono.getText().toString().trim().isEmpty())
                {
                    txtTelefono.setError("Ingrese un pasaje, Ejemplo: +569 99999999");
                    isCorrectTelefono = false;
                }
                else
                {
                    isCorrectTelefono = true;
                }
                if (txtTelefonoOpcional.getText().toString().isEmpty())
                {
                    txtTelefonoOpcional.toString().trim();
                }
                if (isCorrectPasaje == true && isCorrectNumero == true && isCorrectNickname == true && isCorrectTelefono == true && isCorrectTelefono == true)
                {
                    new EjecutarSentencia().execute("http://clickcomida.esy.es/cargar_usuario.php?nombre="+getNombre+"&&apellido="+getApellido+"&&nickname="+txtNickname.getText().toString()+"&&correo="+getCorreo+"&&clave="+getClave);
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
            try
            {
                JSONObject jao = new JSONObject(result);
                String nickname = jao.getString("nickname_usuario");

                if (nickname.equals("yes"))
                {
                    txtNickname.setError("Este nickname ya esta en uso.");
                }
                else if (nickname.equals("no"))
                {
                    Intent i = new Intent(RegistrarUsuarioContinuacion.this, Inicio_Usuario.class);
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
