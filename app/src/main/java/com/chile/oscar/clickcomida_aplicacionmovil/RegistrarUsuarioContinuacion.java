package com.chile.oscar.clickcomida_aplicacionmovil;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.CorrectionInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Coordenadas;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Validadores;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
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

public class RegistrarUsuarioContinuacion extends AppCompatActivity implements View.OnClickListener
{
    Validadores mValidar = new Validadores();
    EditText txtPasaje, txtNumeroPasaje, txtNickname, txtTelefono, txtTelefonoOpcional;
    Button btnRegistroFinal, botonVolver, btnMapa;
    TextView txtInformacion;

    String getCorreo;
    String getClave;
    String getNombre;
    String getApellido;
    Intent i = null;
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

        txtInformacion = (TextView)findViewById(R.id.tvInfo_us);
        botonVolver = (Button)findViewById(R.id.btnVolver_reg_uno);
        btnMapa = (Button)findViewById(R.id.btnMap);
        btnRegistroFinal = (Button)findViewById(R.id.btnRegistrar_usuario);

        getCorreo = getIntent().getStringExtra("correo_usuario");
        getClave = getIntent().getStringExtra("clave_usuario");
        getNombre = getIntent().getStringExtra("nombre_usuario");
        getApellido = getIntent().getStringExtra("apellido_usuario");
        txtInformacion.setText("Hola " + getIntent().getStringExtra("nombre_usuario") + ", te pediremos algunos datos antes de empezar a usar la aplicación.");

        Coordenadas.latitud = 0.0;
        Coordenadas.longitud = 0.0;

        botonVolver.setOnClickListener(this);
        btnRegistroFinal.setOnClickListener(this);
        btnMapa.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnRegistrar_usuario:

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

                if (isCorrectPasaje == true && isCorrectNumero == true && isCorrectNickname == true && isCorrectTelefono == true && isCorrectTelefono == true)
                {
                    if (Coordenadas.longitud == 0.0 && Coordenadas.latitud == 0.0)
                    {
                        Toast.makeText(getApplicationContext(), "Debes marcar tu posicion en el mapa en el boton \"INDICAR MI POSICION EN EL MAPA\"", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        String json = "";
                        JSONObject jsonObject = new JSONObject();
                        try
                        {
                            jsonObject.put("nickname", txtNickname.getText().toString());
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        json = jsonObject.toString();
                        new ConsultarNick().execute(getResources().getString(R.string.direccion_web) + "Controlador/consultar_nickname.php", json);
                    }

                }
                break;

            case R.id.btnVolver_reg_uno:
                Intent iBack = new Intent(RegistrarUsuarioContinuacion.this, RegistrarUsuario.class);
                startActivity(iBack);
                this.finish();
                break;

            case R.id.btnMap:

                /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(R.layout.activity_maps_reg);
                AlertDialog dialogCrudUsuario = builder.create();
                dialogCrudUsuario.show();*/
                Intent intent = new Intent(RegistrarUsuarioContinuacion.this, MapsActivityReg.class);
                startActivity(intent);

                break;
        }
    }

    public class ConsultarNick extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params)
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
                JSONObject bringNick = new JSONObject(s);
                if (bringNick.getString("Resultado").equals("0"))
                {
                    if(txtTelefonoOpcional.getText().toString().isEmpty())
                    {
                        String json = "";
                        JSONObject jsonObject = new JSONObject();
                        try
                        {
                            jsonObject.put("nombre", getNombre);
                            jsonObject.put("apellido", getApellido);
                            jsonObject.put("nickname", txtNickname.getText().toString());
                            jsonObject.put("correo", getCorreo);
                            jsonObject.put("clave", getClave);
                            jsonObject.put("calle", txtPasaje.getText().toString());
                            jsonObject.put("numerocalle", txtNumeroPasaje.getText().toString());
                            jsonObject.put("numero_telefono", txtTelefono.getText().toString());
                            jsonObject.put("numero_telefono_opcional", "0");
                            jsonObject.put("latitud_us", Coordenadas.latitud);
                            jsonObject.put("longitud_us", Coordenadas.longitud);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        json = jsonObject.toString();
                        new RegistrarUsuarioApp().execute(getResources().getString(R.string.direccion_web) + "Controlador/insertar_usuario.php", json);
                    }
                    else
                    {
                        String json = "";
                        JSONObject jsonObject = new JSONObject();
                        try
                        {
                            jsonObject.put("nombre", getNombre);
                            jsonObject.put("apellido", getApellido);
                            jsonObject.put("nickname", txtNickname.getText().toString());
                            jsonObject.put("correo", getCorreo);
                            jsonObject.put("clave", getClave);
                            jsonObject.put("calle", txtPasaje.getText().toString());
                            jsonObject.put("numerocalle", txtNumeroPasaje.getText().toString());
                            jsonObject.put("numero_telefono", txtTelefono.getText().toString());
                            jsonObject.put("numero_telefono_opcional", txtTelefonoOpcional.getText().toString());
                            jsonObject.put("latitud_us", Coordenadas.latitud);
                            jsonObject.put("longitud_us", Coordenadas.longitud);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        json = jsonObject.toString();
                        new RegistrarUsuarioApp().execute(getResources().getString(R.string.direccion_web) + "Controlador/insertar_usuario.php", json);
                    }
                }
                else
                {
                    txtNickname.setError("Este nickname ya esta en uso");
                    Toast.makeText(getApplicationContext(), "Este nickname ya esta en uso", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
    public class RegistrarUsuarioApp extends AsyncTask<String, Void, String>
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
                    Coordenadas.latitud = 0.0;
                    Coordenadas.longitud = 0.0;
                    Toast.makeText(getApplicationContext(), "Te has registrado con exito, inicia sesion con las credenciales que te has registrado.", Toast.LENGTH_LONG).show();
                    i = new Intent(RegistrarUsuarioContinuacion.this, Login.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No insertado", Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
