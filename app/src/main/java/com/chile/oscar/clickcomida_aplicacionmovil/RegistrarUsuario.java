package com.chile.oscar.clickcomida_aplicacionmovil;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Validadores;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
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

public class RegistrarUsuario extends AppCompatActivity  implements View.OnClickListener
{
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
        btnContinuar = (Button)findViewById(R.id.btnContinuar_r);
        btnAtras = (Button)findViewById(R.id.btnRegresar_us);

        btnContinuar.setOnClickListener(this);
        btnAtras.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnRegresar_us:
                this.finish();
                break;

            case R.id.btnContinuar_r:
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
                    String json = "";
                    JSONObject jsonObject = new JSONObject();
                    try
                    {
                        jsonObject.put("correo", txtCorreo.getText().toString());
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    json = jsonObject.toString();
                    new WebService().execute(getResources().getString(R.string.direccion_web) + "Controlador/consultar_correo.php", json);
                }

                break;
        }
    }
    public class WebService extends AsyncTask<String, Void, String>
    {
        @Override
        public String doInBackground(String... params)
        {
            HttpURLConnection conn = null;
            try
            {
                StringBuffer response = null;
                URL url = new URL(params[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                OutputStream out = new BufferedOutputStream(conn.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                writer.write(params[1].toString());
                writer.close();
                out.close();
                int responseCode = conn.getResponseCode();
                System.out.println("responseCode" + responseCode);
                switch (responseCode)
                {
                    case 200:
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null)
                        {
                            response.append(inputLine);
                        }
                        in.close();
                        return response.toString();
                }
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
            finally
            {
                if (conn != null)
                {
                    try
                    {
                        conn.disconnect();
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            try
            {
                JSONObject jsonResult = new JSONObject(s);
                String res = jsonResult.getString("Resultado");
                if (!res.equals("0"))
                {
                    txtCorreo.setError("Este correo ya se encuentra en uso.");
                    Toast.makeText(getApplicationContext(), "Este correo ya esta en uso", Toast.LENGTH_LONG).show();
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
