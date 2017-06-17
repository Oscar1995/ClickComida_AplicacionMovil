package com.chile.oscar.clickcomida_aplicacionmovil;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Coordenadas;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Usuarios;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Validadores;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


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

public class Login extends AppCompatActivity implements View.OnClickListener
{
    String uId, uCorreo, uNombre, uRol;
    Button btnIniciar, btnRegistro, btnOlvidado;
    LoginButton botonFacebook;
    EditText txtCorreo, txtClave;
    CallbackManager callbackManager = CallbackManager.Factory.create();
    Intent i = null;
    ProgressDialog progress;
    Boolean itsEmail = false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        if (cargarPreferencias())
        {
            if (uRol.equals("miembro"))
            {
                i = new Intent(Login.this, Inicio_Usuario.class);
                i.putExtra("id_user_login", uId);
                i.putExtra("correo_usuario", uCorreo);
                i.putExtra("nombre_usuario", uNombre);
                startActivity(i);
                this.finish();
            }
            else if (uRol.equals("repartidor"))
            {
                i = new Intent(Login.this, Inicio_Repartidor.class);
                i.putExtra("id_user_login", uId);
                i.putExtra("correo_usuario", uCorreo);
                i.putExtra("nombre_usuario", uNombre);
                startActivity(i);
                this.finish();
            }
        }

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
                    progress = new ProgressDialog(this);
                    progress.setMessage("Iniciando..");
                    progress.setCanceledOnTouchOutside(false);
                    progress.show();

                    if (txtCorreo.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))
                    {
                        try
                        {
                            String json = "";
                            JSONObject jsonObject = new JSONObject();
                            try
                            {
                                jsonObject.put("correo", txtCorreo.getText().toString());
                                jsonObject.put("nickname", null);
                                jsonObject.put("clave", txtClave.getText().toString());
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                            json = jsonObject.toString();

                            if (new Validadores().isNetDisponible(getApplicationContext()))
                            {
                                itsEmail = true;
                                VerificarCorreoClave x = new VerificarCorreoClave();
                                x.execute(getResources().getString(R.string.direccion_web) + "Controlador/login.php", json);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Debes estar conectado a una red para iniciar sesión.", Toast.LENGTH_SHORT).show();
                                progress.dismiss();
                            }

                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(getApplicationContext(), "El correo y/o contraseña son incorrectos.", Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                        }

                    }
                    else
                    {
                        if (new Validadores().isNetDisponible(getApplicationContext()))
                        {
                            JSONObject jsonObject = new JSONObject();
                            try
                            {
                                jsonObject.put("correo", null);
                                jsonObject.put("nickname", txtCorreo.getText().toString());
                                jsonObject.put("clave", txtClave.getText().toString());
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }

                            itsEmail = false;
                            VerificarCorreoClave x = new VerificarCorreoClave();
                            x.execute(getResources().getString(R.string.direccion_web) + "Controlador/login.php", jsonObject.toString());
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Debes estar conectado a una red para iniciar sesión.", Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                        }
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
                startActivity(new Intent(Login.this, Passwword_forgot.class));
                break;

        }
    }
    public class VerificarCorreoClave extends AsyncTask<String, Void, String>
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
                if (res.equals("Correcto"))
                {
                    guardarPreferencias(jsonResult.getString("Id"), jsonResult.getString("Correo"), jsonResult.getString("Nombre"), jsonResult.getString("Rol"));
                    if (jsonResult.getString("Rol").equals("miembro"))
                    {
                        i = new Intent(Login.this, Inicio_Usuario.class);
                        i.putExtra("id_user_login", jsonResult.getString("Id"));
                        i.putExtra("correo_usuario", jsonResult.getString("Correo"));
                        i.putExtra("nombre_usuario", jsonResult.getString("Nombre"));
                        i.putExtra("rol", jsonResult.getString("Rol"));
                    }
                    else if (jsonResult.getString("Rol").equals("repartidor"))
                    {
                        i = new Intent(Login.this, Inicio_Repartidor.class);
                        i.putExtra("id_user_login", jsonResult.getString("Id"));
                        i.putExtra("correo_usuario", jsonResult.getString("Correo"));
                        i.putExtra("nombre_usuario", jsonResult.getString("Nombre"));
                        i.putExtra("rol", jsonResult.getString("Rol"));
                    }
                    startActivity(i);
                    finish();
                }
                else
                {
                    if (itsEmail)
                    {
                        Toast.makeText(getApplicationContext(), "El correo y/o contraseña son incorrectos.", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "El nickname y/o contraseña son incorrectos.", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), e + "", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void guardarPreferencias(String id, String correo, String nombre, String rol)
    {
        SharedPreferences sharedpreferences = getSharedPreferences("datos_del_usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("id_usuario_shared", id);
        editor.putString("correo_usuario_shared", correo);
        editor.putString("nombre_usuario_shared", nombre);
        editor.putString("rol_usuario_shared", rol);
        editor.commit();
    }
    public boolean cargarPreferencias()
    {
        SharedPreferences sharedpreferences = getSharedPreferences("datos_del_usuario", Context.MODE_PRIVATE);
        uId = sharedpreferences.getString("id_usuario_shared", "");
        uCorreo = sharedpreferences.getString("correo_usuario_shared", "");
        uNombre = sharedpreferences.getString("nombre_usuario_shared", "");
        uRol = sharedpreferences.getString("rol_usuario_shared", "");
        Coordenadas.id = uId;

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
