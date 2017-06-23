package com.chile.oscar.clickcomida_aplicacionmovil;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Coordenadas;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.TiendaNotices;

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
import java.util.List;

/**
 * Created by Oscar on 23-06-2017.
 */

public class tab_list_work extends Fragment
{
    private static String TAG = "Lista";
    ListView listViewTrabajos;
    List<TiendaNotices> tiendaNoticesList = new ArrayList<>();
    String tipoLoad = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.tab_list_work, container, false);
        listViewTrabajos = (ListView)view.findViewById(R.id.lvWork);
        cargar();

        return view;
    }
    public void cargar ()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("null", null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tipoLoad = "Postulacion";
        new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/cargarTiendasNotices.php", jsonObject.toString());
    }
    class WorkAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return tiendaNoticesList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = getActivity().getLayoutInflater().inflate(R.layout.custom_work_list, null);
            TextView textViewTienda = (TextView)convertView.findViewById(R.id.tvTienda);
            TextView textViewCalle = (TextView)convertView.findViewById(R.id.tvCalle);
            Button buttonVer = (Button)convertView.findViewById(R.id.btnRequisitos);
            TextView textViewFecha = (TextView)convertView.findViewById(R.id.tvFechaPublicacion);

            textViewTienda.setText(Html.fromHtml("<b>Tienda: </b>" + tiendaNoticesList.get(position).getNomTienda()));
            textViewCalle.setText(Html.fromHtml("<b>Dirección: </b>" + tiendaNoticesList.get(position).getCalleTienda() + " #" + tiendaNoticesList.get(position).getNumTienda()));
            buttonVer.setId(position);
            textViewFecha.setText("Fecha de publicación: " + tiendaNoticesList.get(position).getFechaPublicacion());

            buttonVer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setTitle("Requisitos de postulación")
                            .setMessage(tiendaNoticesList.get(v.getId()).getNoticeRequerimiento())
                            .setPositiveButton("Postular",
                                    new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {

                                            try
                                            {
                                                tipoLoad = "Postular";
                                                JSONObject jsonObject = new JSONObject();
                                                jsonObject.put("user_id", Coordenadas.id);
                                                jsonObject.put("notice_id", tiendaNoticesList.get(v.getId()).getNoticeId());
                                                new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/postularTienda.php", jsonObject.toString());
                                            }
                                            catch (JSONException e)
                                            {
                                                e.printStackTrace();
                                            }

                                        }
                                    })
                            .setNegativeButton("Cancelar",
                                    new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            dialog.dismiss();
                                        }
                                    });
                    builder.show();
                }
            });

            return convertView;
        }
    }
    public class EjecutarSentencia extends AsyncTask<String, Void, String>
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
            if (!s.equals("[]"))
            {
                if (tipoLoad.equals("Postulacion"))
                {
                    try
                    {
                        if (tiendaNoticesList != null)
                        {
                            if (!tiendaNoticesList.isEmpty())
                            {
                                tiendaNoticesList.clear();
                            }
                        }
                        JSONArray jsonArray = new JSONArray(s);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            TiendaNotices tiendaNotices = new TiendaNotices();
                            tiendaNotices.setNomTienda(jsonObject.getString("name"));
                            tiendaNotices.setCalleTienda(jsonObject.getString("street"));
                            tiendaNotices.setNumTienda(jsonObject.getString("number"));
                            tiendaNotices.setNoticeId(jsonObject.getInt("id"));
                            tiendaNotices.setFechaPublicacion(jsonObject.getString("date"));
                            tiendaNotices.setNoticeRequerimiento(jsonObject.getString("requirements"));
                            tiendaNotices.setNoticeVacant(jsonObject.getInt("vacants"));
                            tiendaNoticesList.add(tiendaNotices);
                        }
                        listViewTrabajos.setAdapter(new WorkAdapter());
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else if (tipoLoad.equals("Postular"))
                {
                    try
                    {
                        JSONObject jsonObject = new JSONObject(s);
                        String res = jsonObject.getString("Insertado");
                        if (res.equals("Si"))
                        {
                            Toast.makeText(getActivity(), "Ahora estas postulando a este aviso", Toast.LENGTH_LONG);
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            else
            {
                Toast.makeText(getContext(), "No hay avisos de trabajos", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
