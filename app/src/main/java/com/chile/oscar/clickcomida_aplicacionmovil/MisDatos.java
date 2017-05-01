package com.chile.oscar.clickcomida_aplicacionmovil;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;

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

public class MisDatos extends Fragment
{
    TextView eNombre, eApellido, eNickname, eCorreo, eTipo, eTel1, eTel2, calle, numCalle;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_mis_datos, container, false);
        eNombre = (TextView) view.findViewById(R.id.txtNombre_misdatos);
        eApellido = (TextView) view.findViewById(R.id.txtApellido_misdatos);
        eNickname = (TextView) view.findViewById(R.id.txtNickname_misdatos);
        eCorreo = (TextView) view.findViewById(R.id.txtCorreo_misdatos);

        eTipo = (TextView) view.findViewById(R.id.txtTipo_misdatos);
        eTel1 = (TextView) view.findViewById(R.id.txtTeleUno_misdatos);
        eTel2 = (TextView) view.findViewById(R.id.txtTeleDos_misdatos);
        calle = (TextView) view.findViewById(R.id.txtCalle_misdatos);
        numCalle = (TextView) view.findViewById(R.id.txtNumeroCalle_misdatos);

        Bundle bundle = getArguments();
        String id = bundle.getString("IdUser");

        TraerDatos obj = new TraerDatos();
        obj.execute(id);

        return view;
    }
    public class TraerDatos extends AsyncTask<String, Void, String>
    {
        @Override
        public String doInBackground(String... params)
        {
            String result = "";
            try
            {
                URL url = new URL("http://clickcomida.esy.es/Controlador/datos_usuario.php");
                try
                {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                    String post_data= URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(params[0],"UTF-8");

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
                if (jsonResult != null)
                {
                    eNombre.setText(getResources().getString(R.string.usuario_nombre) + " " + jsonResult.getString("Nombre"));
                    eApellido.setText(getResources().getString(R.string.usuario_apellido) + " " + jsonResult.getString("Apellido"));
                    eNickname.setText(getResources().getString(R.string.usuario_nickname) + " " + jsonResult.getString("Nickname"));
                    eCorreo.setText(getResources().getString(R.string.correo_usuario) + " " + jsonResult.getString("Email"));
                    eTipo.setText(getResources().getString(R.string.tipo_usuario) + " " + jsonResult.getString("Rol"));
                    eTel1.setText(getResources().getString(R.string.numtel_uno) + " " + jsonResult.getString("Telefono"));
                    calle.setText(getResources().getString(R.string.calle_usuario) + " " + jsonResult.getString("Calle"));
                    numCalle.setText(getResources().getString(R.string.calle_numero_usuario) + " " + jsonResult.getString("Numero_calle"));

                }
                else
                {
                    Toast.makeText(getContext(), "No trajo datos :(", Toast.LENGTH_SHORT).show();
                }
                
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}