package com.chile.oscar.clickcomida_aplicacionmovil.Clases;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

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

/**
 * Created by Oscar on 27-04-2017.
 */

public class BackGroundWorker extends AsyncTask
{
    Context context;
    public BackGroundWorker(Context ctx)
    {
        context = ctx;
    }

    public String doInBackground(Object[] objects)
    {
        String direccion="http://10.0.2.2:8080/clickcomidaPDO/Controlador/insertar.php";
        try
        {
            String correo_us = (String) objects[0];
            String pass_word = (String) objects[1];
            URL url = new URL(direccion);
            try
            {
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");

                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                String e = "UTF-8";

                String post_data = URLEncoder.encode("correo", e) + "=" + URLEncoder.encode("correo_us", e) + "&"
                        + URLEncoder.encode("clave", e) + "=" + URLEncoder.encode("pass_word", e);

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine())!= null)
                {
                    result+=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (IOException e)
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

    protected void onPreExecute()
    {
        super.onPreExecute();
    }
    protected void onPostExecute(String s)
    {
        Toast.makeText(context, "Resultado: " + s, Toast.LENGTH_SHORT).show();
        super.onPostExecute(s);
    }
}
