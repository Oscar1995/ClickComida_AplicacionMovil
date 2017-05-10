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
    Button btnRegistroFinal, botonVolver;
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
        btnRegistroFinal = (Button)findViewById(R.id.btnRegistrar_usuario);

        getCorreo = getIntent().getStringExtra("correo_usuario");
        getClave = getIntent().getStringExtra("clave_usuario");
        getNombre = getIntent().getStringExtra("nombre_usuario");
        getApellido = getIntent().getStringExtra("apellido_usuario");

        txtInformacion.setText("Hola " + getIntent().getStringExtra("nombre_usuario") + ", te pediremos algunos datos antes de empezar a usar la aplicación.");

        botonVolver.setOnClickListener(this);
        btnRegistroFinal.setOnClickListener(this);
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
                    ConsultarNick x = new ConsultarNick();
                    x.execute(txtNickname.getText().toString());
                }
                break;

            case R.id.btnVolver_reg_uno:
                Intent iBack = new Intent(RegistrarUsuarioContinuacion.this, RegistrarUsuario.class);
                startActivity(iBack);
                this.finish();
                break;
        }
    }
    public class ConsultarNick extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try
            {
                URL url = new URL(getResources().getString(R.string.direccion_web) + "/Controlador/consultar_nickname.php");
                try
                {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                    String json = "";
                    JSONObject object = new JSONObject();
                    try
                    {
                        object.put("nickname", params[0]);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    json = object.toString();

                    String post_data= URLEncoder.encode("data_json","UTF-8")+"="+URLEncoder.encode(json,"UTF-8");
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
                JSONObject bringNick = new JSONObject(s);
                if (bringNick.getString("Resultado").equals("0"))
                {
                    RegistrarUsuarioApp regUs = new RegistrarUsuarioApp();
                    if(txtTelefonoOpcional.getText().toString().isEmpty())
                    {
                        regUs.execute(getNombre, getApellido, txtNickname.getText().toString(), getCorreo, getClave, txtPasaje.getText().toString(), txtNumeroPasaje.getText().toString(), txtTelefono.getText().toString(), "0");
                    }
                    else
                    {
                        regUs.execute(getNombre, getApellido, txtNickname.getText().toString(), getCorreo, getClave, txtPasaje.getText().toString(), txtNumeroPasaje.getText().toString(), txtTelefono.getText().toString(), txtTelefonoOpcional.getText().toString());
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
            String result = "";
            try
            {
                URL url = new URL(getResources().getString(R.string.direccion_web) + "/Controlador/insertar_usuario.php");
                try
                {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                    String post_data = null;
                    post_data= URLEncoder.encode("nombre","UTF-8")+"="+URLEncoder.encode(params[0],"UTF-8")+"&"+
                            URLEncoder.encode("apellido","UTF-8")+"="+URLEncoder.encode(params[1],"UTF-8")+"&"+
                            URLEncoder.encode("nickname","UTF-8")+"="+URLEncoder.encode(params[2],"UTF-8")+"&"+
                            URLEncoder.encode("correo","UTF-8")+"="+URLEncoder.encode(params[3],"UTF-8")+"&"+
                            URLEncoder.encode("clave","UTF-8")+"="+URLEncoder.encode(params[4],"UTF-8")+"&"+
                            URLEncoder.encode("calle","UTF-8")+"="+URLEncoder.encode(params[5],"UTF-8")+"&"+
                            URLEncoder.encode("numerocalle","UTF-8")+"="+URLEncoder.encode(params[6],"UTF-8")+"&"+
                            URLEncoder.encode("numero_telefono","UTF-8")+"="+URLEncoder.encode(params[7],"UTF-8")+"&"+
                            URLEncoder.encode("numero_telefono_opcional","UTF-8")+"="+URLEncoder.encode(params[8],"UTF-8");
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
                    Toast.makeText(getApplicationContext(), "Te has registrado con exito, inicia sesion con las credenciales que te has registrado.", Toast.LENGTH_SHORT).show();
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
