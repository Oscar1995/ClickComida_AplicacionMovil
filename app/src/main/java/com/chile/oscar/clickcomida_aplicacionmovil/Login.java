package com.chile.oscar.clickcomida_aplicacionmovil;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Key;

public class Login extends AppCompatActivity implements View.OnClickListener
{
    BackGroundWorker webService = new BackGroundWorker(this);

    Button btnIniciar, btnRegistro, btnOlvidado;
    LoginButton botonFacebook;
    EditText txtCorreo, txtClave;
    CallbackManager callbackManager = CallbackManager.Factory.create();
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
                        webService.execute(txtCorreo.getText().toString(), txtClave.getText().toString());
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
}
