package com.chile.oscar.clickcomida_aplicacionmovil.Clases;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Login;
import com.chile.oscar.clickcomida_aplicacionmovil.RegistrarUsuario;

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

import javax.xml.transform.Result;

/**
 * Created by Oscar on 27-04-2017.
 */
public class BackGroundWorker extends AsyncTask<String, Void, String>
{
    Context context;
    public BackGroundWorker(Context ctx)
    {
        context = ctx;
    }
    String correo = null;


    @Override
    protected String doInBackground(String... params)
    {
        String direccion = "";
        String tipo = params[0];
        if (tipo.equals("consultar_correo"))
        {
            direccion = "http://clickcomida.esy.es/Controlador/consultar_correo.php";
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
                if (direccion.equals("http://clickcomida.esy.es/Controlador/consultar_correo.php"))
                {
                    post_data= URLEncoder.encode("correo","UTF-8")+"="+URLEncoder.encode(correo,"UTF-8");
                }

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while ((line = bufferedReader.readLine())!=null)
                {
                    result+=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
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
        return null;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s)
    {
        try
        {
            JSONObject jao = new JSONObject(s);
            Toast.makeText(context, jao.getString("email"), Toast.LENGTH_SHORT).show();
            RegistrarUsuario x = new RegistrarUsuario();

        }
        catch (JSONException e)
        {
            Toast.makeText(context, "error json", Toast.LENGTH_SHORT).show();
        }
        super.onPostExecute(s);
    }
}
