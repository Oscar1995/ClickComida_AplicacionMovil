package com.chile.oscar.clickcomida_aplicacionmovil;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.BackGroundWorker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.Key;
import java.util.ArrayList;

public class Login extends AppCompatActivity implements View.OnClickListener
{
    String uId, uCorreo, uNombre;
    Button btnIniciar, btnRegistro, btnOlvidado;
    LoginButton botonFacebook;
    EditText txtCorreo, txtClave;
    CallbackManager callbackManager = CallbackManager.Factory.create();
    Intent i = null;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnIniciar = (Button)findViewById(R.id.btnIniciarSesion);
        btnRegistro = (Button)findViewById(R.id.btnRegistrate);
        botonFacebook = (LoginButton)findViewById(R.id.btnFacebook);
        btnOlvidado = (Button)findViewById(R.id.btnClaveOlvidada);
        txtCorreo = (EditText)findViewById(R.id.etCorreoLogin);
        txtClave = (EditText)findViewById(R.id.etClaveLogin);

        btnIniciar.setOnClickListener(this);
        btnRegistro.setOnClickListener(this);
        btnOlvidado.setOnClickListener(this);

        if (cargarPreferencias())
        {
            i = new Intent(Login.this, Inicio_Usuario.class);
            i.putExtra("id_user_login", uId);
            i.putExtra("correo_usuario", uCorreo);
            i.putExtra("nombre_usuario", uNombre);
            startActivity(i);
            this.finish();
        }
        botonFacebook.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                try
                {
                    botonFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
                    {
                        @Override
                        public void onSuccess(LoginResult loginResult)
                        {
                            Toast.makeText(getApplicationContext(), loginResult.toString(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancel()
                        {
                            // App code
                        }

                        @Override
                        public void onError(FacebookException exception)
                        {
                            // App code
                        }
                    });
                }
                catch (Exception x)
                {
                    Toast.makeText(getApplicationContext(), x.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnIniciarSesion:

                int nCaracterCorreo = txtCorreo.getText().toString().length();
                int nCaracterClave = txtClave.getText().toString().length();

                if (nCaracterCorreo > 0 && nCaracterClave > 0)
                {
                    if (txtCorreo.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))
                    {
                        try
                        {
                            progress = new ProgressDialog(this);
                            progress.setMessage("Iniciando..");
                            progress.show();

                            VerificarCorreoClave x = new VerificarCorreoClave();
                            x.execute(txtCorreo.getText().toString(), txtClave.getText().toString());
                        }
                        catch (Exception ex)
                        {
                            progress.cancel();
                            Toast.makeText(getApplicationContext(), "El usuario y/o contraseña son incorrectos.", Toast.LENGTH_SHORT).show();
                        }

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
                        if (!txtCorreo.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))
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
    public class VerificarCorreoClave extends AsyncTask<String, Void, String>
    {
        @Override
        public String doInBackground(String... params)
        {
            String result = "";
            try
            {
                URL url = new URL(getResources().getString(R.string.direccion_web) + "/Controlador/login.php");
                try
                {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                    String post_data= URLEncoder.encode("correo","UTF-8")+"="+URLEncoder.encode(params[0],"UTF-8")+"&" +URLEncoder.encode("clave","UTF-8")+"="+URLEncoder.encode(params[1],"UTF-8");

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
                if (res.equals("Correcto"))
                {
                    guardarPreferencias(jsonResult.getString("Id"), txtCorreo.getText().toString(), jsonResult.getString("Nombre"));
                    i = new Intent(Login.this, Inicio_Usuario.class);
                    i.putExtra("id_user_login", jsonResult.getString("Id"));
                    i.putExtra("correo_usuario", txtCorreo.getText().toString());
                    i.putExtra("nombre_usuario", jsonResult.getString("Nombre"));
                    startActivity(i);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Correo y/o clave son incorrectos.", Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), e + "", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void guardarPreferencias(String id, String correo, String nombre)
    {
        SharedPreferences sharedpreferences = getSharedPreferences("datos_del_usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("id_usuario_shared", id);
        editor.putString("correo_usuario_shared", correo);
        editor.putString("nombre_usuario_shared", nombre);
        editor.commit();
    }
    public boolean cargarPreferencias()
    {
        SharedPreferences sharedpreferences = getSharedPreferences("datos_del_usuario", Context.MODE_PRIVATE);
        uId = sharedpreferences.getString("id_usuario_shared", "");
        uCorreo = sharedpreferences.getString("correo_usuario_shared", "");
        uNombre = sharedpreferences.getString("nombre_usuario_shared", "");

        if(uId.isEmpty() && uCorreo.isEmpty() && uNombre.isEmpty())
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}
