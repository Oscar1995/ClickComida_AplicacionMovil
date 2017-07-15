package com.chile.oscar.clickcomida_aplicacionmovil;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.BusquedaAvanzadaTiendas;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Codificacion;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.MetodosCreados;
import com.google.android.gms.maps.model.LatLng;

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


public class StoreFragment extends Fragment implements StoreFragmentSelected.OnFragmentInteractionListener
{
    private OnFragmentInteractionListener mListener;
    private String id_usuario;

    List<BusquedaAvanzadaTiendas> busquedaAvanzadaTiendasList = new ArrayList<>();
    List<Bitmap> images = new ArrayList<>();

    ListView listViewStores;
    ProgressDialog progress;

    public StoreFragment()
    {

    }

    public static StoreFragmentSelected newInstance(String... params)
    {
        StoreFragmentSelected fragment = new StoreFragmentSelected();
        Bundle args = new Bundle();
        args.putString("imagen_tienda", params[0]);
        args.putString("nombre_tienda", params[1]);
        args.putString("des_tienda", params[2]);
        args.putString("calle_tienda", params[3]);
        args.putString("numero_tienda", params[4]);
        args.putString("start_day", params[5]);
        args.putString("end_day", params[6]);
        args.putString("open_hour", params[7]);
        args.putString("close_hour", params[8]);
        args.putString("lunch_open_hour", params[9]);
        args.putString("lunch_after_hour", params[10]);
        args.putString("tienda_id", params[11]);
        args.putString("latitud", params[12]);
        args.putString("longitud", params[13]);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            id_usuario = getArguments().getString("user_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        progress = new ProgressDialog(getContext());
        progress.setMessage("Cargando tiendas...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        View view = inflater.inflate(R.layout.fragment_store, container, false);

        JSONObject object = new JSONObject();
        try
        {
            object.put("id_user", id_usuario);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        String json = object.toString();
        new cargarTiendas().execute(getResources().getString(R.string.direccion_web) + "Controlador/cargarTienda.php", json);

        listViewStores = (ListView)view.findViewById(R.id.lvTiendas);
        listViewStores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.content_general, newInstance(Codificacion.encodeToBase64(images.get(position), Bitmap.CompressFormat.PNG, 60),
                        busquedaAvanzadaTiendasList.get(position).getNameStore(),
                        busquedaAvanzadaTiendasList.get(position).getDescriptionStore(),
                        busquedaAvanzadaTiendasList.get(position).getStreetStore(),
                        busquedaAvanzadaTiendasList.get(position).getNumberStore() + "",
                        busquedaAvanzadaTiendasList.get(position).getStartDay(),
                        busquedaAvanzadaTiendasList.get(position).getEndDay(),
                        busquedaAvanzadaTiendasList.get(position).getOpenHour(),
                        busquedaAvanzadaTiendasList.get(position).getCloseHour(),
                        busquedaAvanzadaTiendasList.get(position).getLunchHour(),
                        busquedaAvanzadaTiendasList.get(position).getLunchAfterHour(),
                        busquedaAvanzadaTiendasList.get(position).getStoreId() + "",
                        busquedaAvanzadaTiendasList.get(position).getLatLngStore().latitude + "",
                        busquedaAvanzadaTiendasList.get(position).getLatLngStore().longitude + ""));
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);
                trans.commit();
            }
        });
        return view;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    class CustomAdapter extends BaseAdapter
    {

        @Override
        public int getCount()
        {
            return images.size(); //Indico las veces que debe recorrer
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
            try
            {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.customlayout, null);
                ImageView imageView = (ImageView)convertView.findViewById(R.id.ivProductoImage);
                TextView textViewNombre = (TextView)convertView.findViewById(R.id.txtOption);
                TextView textViewDesStore = (TextView)convertView.findViewById(R.id.txtDesStore);
                RatingBar ratingBarStore = (RatingBar)convertView.findViewById(R.id.rbStore);
                TextView textViewFechaCreacion = (TextView)convertView.findViewById(R.id.tvCreated);

                imageView.setImageDrawable(new MetodosCreados().RedondearBitmap(images.get(position), getResources()));
                textViewNombre.setText(Html.fromHtml("<b>" + getResources().getString(R.string.Tienda) + ":</b> " + busquedaAvanzadaTiendasList.get(position).getNameStore()));

                String openupdate = new MetodosCreados().HoraNormal(busquedaAvanzadaTiendasList.get(position).getOpenHour());
                String closeupdate = new MetodosCreados().HoraNormal(busquedaAvanzadaTiendasList.get(position).getCloseHour());
                if (busquedaAvanzadaTiendasList.get(position).getLunchHour().equals("00:00:00") && busquedaAvanzadaTiendasList.get(position).getLunchAfterHour().equals("00:00:00"))
                {
                    textViewDesStore.setText(Html.fromHtml("<b> <font color=\"blue\">"+busquedaAvanzadaTiendasList.get(position).getStartDay() + " a " + busquedaAvanzadaTiendasList.get(position).getEndDay()+ "</font> <br>"+
                            "Continuado: </b>" +  "de " + openupdate + " a " + closeupdate));
                }
                else
                {
                    String openupdatelunch = new MetodosCreados().HoraNormal(busquedaAvanzadaTiendasList.get(position).getLunchHour());
                    String closeupdatelunch = new MetodosCreados().HoraNormal(busquedaAvanzadaTiendasList.get(position).getLunchAfterHour());
                    textViewDesStore.setText(Html.fromHtml("<b> <font color=\"blue\">"+busquedaAvanzadaTiendasList.get(position).getStartDay() + " a " + busquedaAvanzadaTiendasList.get(position).getEndDay()+ "</font> <br>"+
                            "Mañana: </b>" +  "de " + openupdate + " a " + closeupdate + "<br>"
                            + "<b>Tarde: </b>de " + openupdatelunch + " a " + closeupdatelunch));
                }

                ratingBarStore.setRating(busquedaAvanzadaTiendasList.get(position).getRatingStore());
                textViewFechaCreacion.setText(Html.fromHtml("<b>" + getResources().getString(R.string.Fecha_Creacion) + ": </b>" + new MetodosCreados().formatearFechaSimple(busquedaAvanzadaTiendasList.get(position).getDateCreated())));
            }
            catch (Exception ex)
            {
                Toast.makeText(getContext(), "Intentalo otra vez", Toast.LENGTH_SHORT).show();
            }
            return convertView;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public class cargarTiendas extends AsyncTask<String, Void, String>
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
                        Toast.makeText(getContext(), "Intentalo de nuevo.", Toast.LENGTH_SHORT).show();
                        //ex.printStackTrace();
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
                if (s.equals("[]"))
                {
                    Toast.makeText(getContext(), "Aun no tienes tiendas creadas.", Toast.LENGTH_LONG).show();
                    progress.dismiss();
                }
                else
                {
                    JSONArray jsonArray = new JSONArray(s);
                    int tomarCuenta = jsonArray.length() / 2; //Indica que la otra mitad son las fotos

                    if (!busquedaAvanzadaTiendasList.isEmpty())
                    {
                        busquedaAvanzadaTiendasList.clear();
                        images.clear();
                    }
                    int cLocal = 0;
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        if (i >= tomarCuenta)
                        {
                            images.add(Codificacion.decodeBase64(object.getString("photo_" + cLocal)));
                            cLocal++;
                        }
                        else
                        {
                            BusquedaAvanzadaTiendas busquedaAvanzadaTiendas = new BusquedaAvanzadaTiendas();
                            busquedaAvanzadaTiendas.setStoreId(object.getInt("id"));
                            busquedaAvanzadaTiendas.setNameStore(object.getString("name"));
                            busquedaAvanzadaTiendas.setDescriptionStore(object.getString("description"));
                            busquedaAvanzadaTiendas.setStreetStore(object.getString("street"));
                            busquedaAvanzadaTiendas.setNumberStore(object.getInt("number"));
                            busquedaAvanzadaTiendas.setStartDay(object.getString("start_day"));
                            busquedaAvanzadaTiendas.setEndDay(object.getString("end_day"));
                            busquedaAvanzadaTiendas.setOpenHour(object.getString("open_hour"));
                            busquedaAvanzadaTiendas.setCloseHour(object.getString("close_hour"));
                            busquedaAvanzadaTiendas.setLunchHour(object.getString("lunch_hour"));
                            busquedaAvanzadaTiendas.setLunchAfterHour(object.getString("lunch_after_hour"));
                            busquedaAvanzadaTiendas.setLatLngStore(new LatLng(object.getDouble("latitude"), object.getDouble("longitude")));
                            busquedaAvanzadaTiendas.setUserId(Integer.parseInt(id_usuario));
                            busquedaAvanzadaTiendas.setDateCreated(object.getString("created_at"));
                            if (object.getString("calification_average").equals("null"))
                            {
                                busquedaAvanzadaTiendas.setRatingStore(0.0f);
                            }
                            else
                            {
                                busquedaAvanzadaTiendas.setRatingStore(Float.parseFloat(object.getString("calification_average")));
                            }
                            busquedaAvanzadaTiendasList.add(busquedaAvanzadaTiendas);
                        }
                    }
                    CustomAdapter customAdapter = new CustomAdapter();
                    listViewStores.setAdapter(customAdapter);
                    progress.dismiss();
                }
            }
            catch (JSONException e)
            {
                Toast.makeText(getContext(), "Intentalo de nuevo.", Toast.LENGTH_SHORT).show();
               // e.printStackTrace();
            }
        }
    }
}
