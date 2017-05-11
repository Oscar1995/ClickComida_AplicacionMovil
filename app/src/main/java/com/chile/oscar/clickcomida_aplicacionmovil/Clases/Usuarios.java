package com.chile.oscar.clickcomida_aplicacionmovil.Clases;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Inicio_Usuario;
import com.chile.oscar.clickcomida_aplicacionmovil.Login;
import com.chile.oscar.clickcomida_aplicacionmovil.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Oscar on 11-05-2017.
 */
public class Usuarios extends AsyncTask<String, Void, String>
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
            JSONObject jsonResult = new JSONObject(s);
            String res = jsonResult.getString("Resultado");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
