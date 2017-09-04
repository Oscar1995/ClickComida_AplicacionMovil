package com.chile.oscar.clickcomida_aplicacionmovil;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MisDatos extends Fragment
{
    TextView textViewVocal, textViewNombre, textViewApellido, textViewCorreo, textViewCuenta;
    ImageView imageViewModificar;
    Button buttonAddTel, buttonAddDir;
    ListView listViewTel, listViewDir;

    List<String> listTelefono;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_mis_datos, container, false);

        textViewVocal = (TextView)view.findViewById(R.id.tvVocal);
        textViewNombre = (TextView)view.findViewById(R.id.tvNombre);
        textViewApellido = (TextView)view.findViewById(R.id.tvApellido);
        textViewCorreo = (TextView)view.findViewById(R.id.tvCorreo);
        textViewCuenta = (TextView)view.findViewById(R.id.tvCuenta);

        imageViewModificar = (ImageView)view.findViewById(R.id.ivModificar);

        buttonAddTel = (Button)view.findViewById(R.id.btnAgregarTel);
        buttonAddDir = (Button)view.findViewById(R.id.btnAgregarDir);

        listViewTel = (ListView)view.findViewById(R.id.lvTelefonos);
        listViewDir = (ListView)view.findViewById(R.id.lvDirecciones);

        listTelefono = new ArrayList<>();

        Bundle bundle = getArguments();
        int id = Integer.parseInt(bundle.getString("IdUser"));

        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", id);
            new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/datos_usuario.php", jsonObject.toString());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return view;
    }


    private Location getMyLocation()
    {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (myLocation == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            String provider = lm.getBestProvider(criteria, true);
            myLocation = lm.getLastKnownLocation(provider);
        }
        return myLocation;
    }


    public class EjecutarConsulta extends AsyncTask<String, Void, String>
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
                out.write(params[1].toString().getBytes());
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
                JSONObject jsonObject = new JSONObject(s);

                JSONObject jsonObjectP = new JSONObject(jsonObject.getString("p"));
                final JSONArray jsonArrayT = new JSONArray(jsonObject.getString("t"));
                final JSONArray jsonArrayD = new JSONArray(jsonObject.getString("d"));

                final char caracter = jsonObjectP.getString("name").charAt(0);
                textViewVocal.setText(String.valueOf(caracter).toUpperCase());
                textViewNombre.setText(Html.fromHtml("<b>Nombre:</b> " + jsonObjectP.getString("name")));
                textViewApellido.setText(Html.fromHtml("<b>Apellido:</b> " +jsonObjectP.getString("lastname")));
                textViewCorreo.setText(jsonObjectP.getString("email"));
                textViewCuenta.setText(jsonObjectP.getString("type"));

                BaseAdapter baseAdapterT = new BaseAdapter()
                {
                    @Override
                    public int getCount()
                    {
                        return jsonArrayT.length();
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
                    public View getView(int position, View convertView, ViewGroup parent)
                    {
                        convertView = getActivity().getLayoutInflater().inflate(R.layout.custom_lista_telefono, null);
                        TextView textViewAtributo = (TextView)convertView.findViewById(R.id.tvAtributo);
                        TextView textViewTelefono = (TextView)convertView.findViewById(R.id.tvTelefono);
                        Button buttonM = (Button) convertView.findViewById(R.id.btnM);
                        Button buttonD = (Button) convertView.findViewById(R.id.btnD);

                        textViewAtributo.setText("Telefono " + (position + 1 ));

                        try
                        {
                            JSONObject jsonObjectT = jsonArrayT.getJSONObject(position);
                            textViewTelefono.setText(jsonObjectT.getString("number"));
                            buttonM.setId(position);
                            buttonD.setId(position);

                            listTelefono.add(jsonObjectT.getString("number"));

                            if (position >= 1)
                            {
                                buttonAddTel.setEnabled(false);
                            }
                            else if (position == 0)
                            {
                                buttonAddTel.setEnabled(true);
                            }

                            buttonM.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v)
                                {
                                    Toast.makeText(getContext(), "Lo has presionado.", Toast.LENGTH_SHORT).show();
                                    AlertDialog.Builder builderM = new AlertDialog.Builder(getContext());
                                    View p = getActivity().getLayoutInflater().inflate(R.layout.modificar_telefono, null);

                                    EditText editTextTelefono = (EditText)p.findViewById(R.id.etNuevoTelefonp_us);
                                    editTextTelefono.setText(listTelefono.get(v.getId()));

                                    builderM.setView(p);
                                    AlertDialog dialogChangeDireccionn = builderM.create();
                                    dialogChangeDireccionn.show();
                                }
                            });

                            buttonD.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v)
                                {

                                }
                            });
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        return convertView;
                    }
                };
                listViewTel.setAdapter(baseAdapterT);

                BaseAdapter baseAdapterD = new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return jsonArrayD.length();
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
                    public View getView(int position, View convertView, ViewGroup parent)
                    {
                        convertView = getActivity().getLayoutInflater().inflate(R.layout.custom_lista_dir_tel, null);
                        TextView textViewAtributo = (TextView)convertView.findViewById(R.id.tvAtributo);
                        TextView textViewCalle = (TextView)convertView.findViewById(R.id.tvCalle);
                        TextView textViewNumero = (TextView)convertView.findViewById(R.id.tvNumero);
                        Switch aSwitch = (Switch)convertView.findViewById(R.id.swDir);
                        ImageView imageViewM = (ImageView)convertView.findViewById(R.id.ivModify);
                        ImageView imageViewD = (ImageView)convertView.findViewById(R.id.ivDelete);

                        textViewAtributo.setText("Dirección " + (position + 1 ));

                        try
                        {
                            JSONObject jsonObjectD = jsonArrayD.getJSONObject(position);
                            textViewCalle.setText(jsonObjectD.getString("street"));
                            textViewNumero.setText(jsonObjectD.getString("number"));

                            if (jsonObjectD.getString("default").equals("0"))
                            {
                                aSwitch.setChecked(false);
                                aSwitch.setText("Activar");
                            }
                            else
                            {
                                aSwitch.setChecked(true);
                                aSwitch.setText("Desactivar");
                            }

                            aSwitch.setId(position);
                            imageViewM.setId(position);
                            imageViewD.setId(position);

                            if (position >= 2)
                            {
                                buttonAddDir.setEnabled(false);
                            }
                            else if (position == 0 || position == 1)
                            {
                                buttonAddDir.setEnabled(true);
                            }

                            imageViewM.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v)
                                {

                                }
                            });

                            imageViewM.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        return convertView;
                    }
                };
                listViewDir.setAdapter(baseAdapterD);

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}