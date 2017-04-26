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

        String getCorreo = getIntent().getStringExtra("correo_usuario");
        String getClave = getIntent().getStringExtra("clave_usuario");
        String getNombre = getIntent().getStringExtra("nombre_usuario");
        String getApellido = getIntent().getStringExtra("apellido_usuario");

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

                if (txtPasaje.getText().toString().trim().isEmpty())
                {
                    txtPasaje.setError("Ingrese un pasaje, ejemplo: Chillán 697");
                    txtPasaje.setText("");
                    isCorrectPasaje = false;
                }
                else
                {
                    txtPasaje.toString().trim();
                    isCorrectPasaje = true;
                }

                if (txtNumeroPasaje.getText().toString().trim().isEmpty())
                {
                    txtNumeroPasaje.setError("Ingrese un pasaje, ejemplo: #1921");
                    txtNumeroPasaje.setText("");
                    isCorrectNumero = false;
                }
                else
                {
                    if (mValidar.isLetter(txtNumeroPasaje.getText().toString().trim()) == true)
                    {
                        txtNumeroPasaje.setError("Solo numeros, ejemplo: #1921");
                        txtNumeroPasaje.setText("");
                        isCorrectNumero = false;
                    }
                    else
                    {
                        txtNumeroPasaje.toString().trim();
                        isCorrectNumero = true;
                    }
                }

                if (txtNickname.getText().toString().trim().isEmpty())
                {
                    txtNickname.setError("Ingrese un pasaje, Ejemplo: JuanPerez90");
                    txtNickname.setText("");
                    isCorrectNickname = false;
                }
                else
                {

                    txtNickname.toString().trim();
                    isCorrectNickname = true;
                }

                if (txtTelefono.getText().toString().trim().isEmpty())
                {
                    txtNickname.setError("Ingrese un pasaje, Ejemplo: +569 99999999");
                    txtNickname.setText("");
                    isCorrectTelefono = false;
                }
                else
                {
                    txtTelefono.toString().trim();
                    isCorrectTelefono = true;
                }
                if (txtTelefonoOpcional.getText().toString().isEmpty())
                {
                    txtTelefonoOpcional.toString().trim();
                }
                if (isCorrectPasaje == true && isCorrectNumero == true && isCorrectNickname == true && isCorrectTelefono == true && isCorrectTelefono == true)
                {
                    startActivity(i);
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
