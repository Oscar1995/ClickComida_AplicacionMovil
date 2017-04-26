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

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Validadores;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RegistrarUsuario extends AppCompatActivity implements View.OnClickListener
{
    WebService contenido = new WebService();
    Validadores mValidador = new Validadores();
    Button btnAtras, btnContinuar;
    EditText txtCorreo, txtClave, txtClaveR, txtNombre, txtApellido;

    Intent i = null;
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
                String claveUsuario = "";

                boolean isCorrectEmail = false;
                boolean isCorrectClave = false;
                boolean isCorrectClaveR = false;
                boolean isCorrectNombre = false;
                boolean isCorrectApellido = false;

                i = new Intent(RegistrarUsuario.this, RegistrarUsuarioContinuacion.class);

                if (txtCorreo.getText().toString().isEmpty())
                {
                    txtCorreo.setError("Introduce un correo electronico.");
                    isCorrectEmail = false;
                }
                else
                {
                    if (txtCorreo.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))
                    {
                        i.putExtra("correo_usuario", txtCorreo.getText().toString().trim());
                        isCorrectEmail = true;
                    }
                    else
                    {
                        txtCorreo.setError("No tiene formato de correo electronico.");
                        isCorrectEmail = false;
                    }
                }

                if (txtClave.getText().toString().isEmpty())
                {
                    txtClave.setError("Introduce una clave con al menos 6 caracteres.");
                    isCorrectClave = false;
                }
                else
                {
                    if (txtClave.getText().toString().length() >= 6)
                    {
                        claveUsuario = txtClave.getText().toString();
                        isCorrectClave = true;
                    }
                    else
                    {
                        txtClave.setError("La clave debe tener al mennos 6 caracteres.");
                        isCorrectClave = false;
                    }
                }


                if (txtClaveR.getText().toString().isEmpty())
                {
                    txtClaveR.setError("Introduce una clave con al menos 6 caracteres.");
                    isCorrectClaveR = false;
                }
                else
                {
                    if (!txtClaveR.getText().toString().equals(claveUsuario))
                    {
                        txtClaveR.setError("La clave no coincide con la clave de arriba.");
                        isCorrectClaveR = false;
                    }
                    else
                    {
                        i.putExtra("clave_usuario", txtClave.getText().toString());
                        isCorrectClaveR = true;
                    }
                }


                if (txtNombre.getText().toString().isEmpty())
                {
                    txtNombre.setError("Introduce tu nombre sin numeros.");
                    isCorrectNombre = false;
                }
                else
                {
                    if (mValidador.isLetter(txtNombre.getText().toString()) == false)
                    {
                        txtNombre.setError("Solo se permiten letras.");
                        isCorrectNombre = false;
                    }
                    else
                    {
                        i.putExtra("nombre_usuario", txtNombre.getText().toString().trim());
                        isCorrectNombre = true;
                    }
                }
                if (txtApellido.getText().toString().isEmpty())
                {
                    txtApellido.setError("Introduce tu apellido sin numeros.");
                    isCorrectApellido = false;
                }
                else
                {
                    if (mValidador.isLetter(txtApellido.getText().toString()) == false)
                    {
                        txtApellido.setError("Solo se permiten letras.");
                        isCorrectApellido = false;
                    }
                    else
                    {
                        i.putExtra("apellido_usuario", txtApellido.getText().toString().trim());
                        isCorrectApellido = true;
                    }
                }
                if (isCorrectEmail == true && isCorrectClave == true && isCorrectClave == true && isCorrectClaveR == true && isCorrectNombre == true && isCorrectApellido == true)
                {
                    new EjecutarSentencia().execute("http://clickcomida.esy.es/consultar_correo.php?correo="+txtCorreo.getText().toString());
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
                String laConfirmacion = jao.getString("email");
                if (laConfirmacion.equals("yes"))
                {
                    txtCorreo.setError("Este correo ya se encuentra en uso.");
                }
                else
                {
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
