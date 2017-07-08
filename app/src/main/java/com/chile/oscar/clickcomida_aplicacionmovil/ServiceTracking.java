package com.chile.oscar.clickcomida_aplicacionmovil;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Coordenadas;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.OrdenSegundoPlano;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Oscar on 08-07-2017.
 */

public class ServiceTracking extends Service
{
    //60000 = 1 minutos, 5000 = 5 segundos
    int secondTimer = 60000;
    List<OrdenSegundoPlano> ordenSegundoPlanoList = new ArrayList<>();
    Context context = this;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    // Se ejecuta cuando el servicio está creado en memoria. Si el servicio ya está activo, entonces se evita de nuevo su llamada.
    @Override
    public void onCreate()
    {
        Log.e("Servicio", "Creado");
    }

    //Método que ejecuta las instrucciones del servicio. Se llama solo si el servicio se inició con startService().
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.w("Servicio", "Iniciado");
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                try
                {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", Coordenadas.id);
                    new EjecutarSentenciaService().execute(getResources().getString(R.string.direccion_web) + "Controlador/ordenSegundoPlano.php", jsonObject.toString());
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, 0, secondTimer);

        return START_STICKY;
    }

    //Se llama cuando el servicio está siendo destruido. Importantísimo que dentro de este método detengas los hilos iniciados.
    @Override
    public void onDestroy()
    {
        Log.e("Servicio", "Detenido");
    }
    public class EjecutarSentenciaService extends AsyncTask<String, Void, String>
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
            if (s.equals("[]"))
            {
                secondTimer = 60000;
            }
            else
            {
                /*if (ordenSegundoPlanoList != null)
                {
                    if (!ordenSegundoPlanoList.isEmpty())
                    {
                        ordenSegundoPlanoList.clear();
                    }
                }*/
                //secondTimer = 10000;
                try
                {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        OrdenSegundoPlano ordenSegundoPlano = new OrdenSegundoPlano();
                        ordenSegundoPlano.setOrdenId(jsonObject.getInt("id"));
                        if (!ordenSegundoPlanoList.contains(ordenSegundoPlano)) //Si el objeto no existe dentro de la lista, lo agrega SOLO UNA VEZ
                        {
                            ordenSegundoPlano.setOrdenState(jsonObject.getString("description"));
                            ordenSegundoPlano.setNameStore(jsonObject.getString("name"));
                            ordenSegundoPlano.setNotify(false);
                            ordenSegundoPlanoList.add(ordenSegundoPlano);
                        }
                    }
                    for (int i=0; i<ordenSegundoPlanoList.size(); i++)
                    {
                        if (ordenSegundoPlanoList.get(i).isNotify() == false)
                        {
                            if (ordenSegundoPlanoList.get(i).getOrdenState().equals("Repartiendo"))
                            {
                                NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                                        .setSmallIcon(android.R.drawable.stat_sys_download_done)
                                        .setContentTitle("El repartidor ha comenzado a repartir desde la tienda: " + ordenSegundoPlanoList.get(i).getNameStore())
                                        .setContentText("Repartiendo...");

                                Intent notificationIntent = new Intent(ServiceTracking.this, Tracking.class);
                                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
                                builder.setContentIntent(pendingIntent);

                                startForeground(1, builder.build());

                                OrdenSegundoPlano ordenSegundoPlano = new OrdenSegundoPlano();
                                ordenSegundoPlano.setNotify(true);
                                ordenSegundoPlanoList.set(i, ordenSegundoPlano);
                            }
                        }
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        }
    }
}
