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

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.BackGroundWorker;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Validadores;

import org.json.JSONException;
import org.json.JSONObject;

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
                    WebService wServ= new WebService();
                    wServ.execute("consultar_correo", txtCorreo.getText().toString());
                }

                break;
        }
    }
    public class WebService extends AsyncTask<String, Void, String>
    {
        @Override
        public String doInBackground(String... params)
        {
            String direccion = "";
            String tipo = params[0];
            String correo = "";
            String result = "";
            if (tipo.equals("consultar_correo"))
            {
                direccion = getResources().getString(R.string.direccion_web) + "/Controlador/consultar_correo.php";
                correo = params[1];
            }
            try
            {
                URL url = new URL(direccion);
                try
                {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                    String post_data = null;
                    //post_data= URLEncoder.encode("correo","UTF-8")+"="+URLEncoder.encode(correo,"UTF-8")+"&" +URLEncoder.encode("clave","UTF-8")+"="+URLEncoder.encode(clave,"UTF-8");
                    if (direccion.equals(getResources().getString(R.string.direccion_web) + "/Controlador/consultar_correo.php"))
                    {
                        post_data= URLEncoder.encode("correo","UTF-8")+"="+URLEncoder.encode(correo,"UTF-8");
                    }

                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String line="";
                    while ((line = bufferedReader.readLine())!=null)
                    {
                        result+=line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            return result;
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
