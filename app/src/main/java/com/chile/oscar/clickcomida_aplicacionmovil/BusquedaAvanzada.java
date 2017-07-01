package com.chile.oscar.clickcomida_aplicacionmovil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.BusquedaAvanzadaProductos;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.BusquedaAvanzadaTiendas;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Codificacion;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Coordenadas;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BusquedaAvanzada.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BusquedaAvanzada#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BusquedaAvanzada extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<BusquedaAvanzadaProductos> busquedaAvanzadaProductosList = new ArrayList<>();
    List<BusquedaAvanzadaTiendas> busquedaAvanzadaTiendasList = new ArrayList<>();
    List<Bitmap> bitmapList = new ArrayList<>();

    TextView textViewResultados, txtTitulo;
    EditText editTextCampo;
    ImageView imageViewLupa;
    Spinner spinnerFiltro;
    GridView gridViewProductos;
    int posFiltro;
    RadioButton radioButtonProducto, radioButtonTienda;
    ProgressDialog progress;
    LinearLayout linearLayoutLista, linearLayoutFiltro;
    Boolean aBooleanProdOrStore;

    private OnFragmentInteractionListener mListener;

    public BusquedaAvanzada() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Details_products newInstance(Bitmap bitmapProd, String nomTienda, int store_id, int product_id, String nomProd, String desProd, int precioProd)
    {
        Details_products fragment = new Details_products();
        Bundle args = new Bundle();
        args.putString("imagen_prod", Codificacion.encodeToBase64(bitmapProd, Bitmap.CompressFormat.PNG, 100));
        args.putString("nombre_tienda", nomTienda);
        args.putString("store_id", store_id + "");
        args.putString("product_id", product_id + "");
        args.putString("nombre_prod", nomProd);
        args.putString("des_prod", desProd);
        args.putString("precio_prod", precioProd + "");
        fragment.setArguments(args);
        return fragment;
    }
    public static StoreOtherUser newInstanceStoreOtherUser(Bitmap bitmapStore, String nomTienda, String desTienda, String sCalle, String sNumero, String startDay, String endDay, String openHour, String closeHour, String lunchOpenHour, String lunchAfterHour, String storeId, String latitud, String longitud, String userId)
    {
        StoreOtherUser fragment = new StoreOtherUser();
        Bundle args = new Bundle();
        args.putString("imagen_tienda", Codificacion.encodeToBase64(bitmapStore, Bitmap.CompressFormat.PNG, 100));
        args.putString("nombre_tienda", nomTienda);
        args.putString("des_tienda", desTienda);
        args.putString("calle_tienda", sCalle);
        args.putString("numero_tienda", sNumero);
        args.putString("start_day", startDay);
        args.putString("end_day", endDay);
        args.putString("open_hour", openHour);
        args.putString("close_hour", closeHour);
        args.putString("lunch_open_hour", lunchOpenHour);
        args.putString("lunch_after_hour", lunchAfterHour);
        args.putString("tienda_id", storeId);
        args.putString("latitud", latitud);
        args.putString("longitud", longitud);
        args.putString("user_id", userId);
        fragment.setArguments(args);
        return fragment;
    }
    public static StoreProductsFragment newInstanceStoreMe(Bitmap bitmapStore, String nomTienda, String desTienda, String sCalle, String sNumero, String startDay, String endDay, String openHour, String closeHour, String lunchOpenHour, String lunchAfterHour, String storeId, String latitud, String longitud)
    {
        StoreProductsFragment fragment = new StoreProductsFragment();
        Bundle args = new Bundle();
        args.putString("imagen_tienda", Codificacion.encodeToBase64(bitmapStore, Bitmap.CompressFormat.PNG, 100));
        args.putString("nombre_tienda", nomTienda);
        args.putString("des_tienda", desTienda);
        args.putString("calle_tienda", sCalle);
        args.putString("numero_tienda", sNumero);
        args.putString("start_day", startDay);
        args.putString("end_day", endDay);
        args.putString("open_hour", openHour);
        args.putString("close_hour", closeHour);
        args.putString("lunch_open_hour", lunchOpenHour);
        args.putString("lunch_after_hour", lunchAfterHour);
        args.putString("tienda_id", storeId);
        args.putString("latitud", latitud);
        args.putString("longitud", longitud);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_busqueda_avanzada, container, false);

        final String[] nomFiltros = {"Nombre", "Menor precio", "Mayor precio"};
        editTextCampo = (EditText)v.findViewById(R.id.etProducto);
        imageViewLupa = (ImageView)v.findViewById(R.id.ivLupa);
        spinnerFiltro = (Spinner)v.findViewById(R.id.sFiltro);
        textViewResultados = (TextView)v.findViewById(R.id.tvResultados);
        gridViewProductos = (GridView)v.findViewById(R.id.gvProductosSearch);
        txtTitulo = (TextView)v.findViewById(R.id.txtInfoBuscar);
        radioButtonProducto = (RadioButton)v.findViewById(R.id.rbtProducto);
        radioButtonTienda = (RadioButton)v.findViewById(R.id.rbtTienda);
        linearLayoutLista = (LinearLayout)v.findViewById(R.id.llLista);
        linearLayoutFiltro = (LinearLayout)v.findViewById(R.id.llFiltro);

        linearLayoutLista.setVisibility(View.GONE);
        linearLayoutFiltro.setVisibility(View.GONE);
        textViewResultados.setVisibility(View.GONE);

        radioButtonProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (!bitmapList.isEmpty())bitmapList.clear();
                textViewResultados.setVisibility(View.GONE);
                radioButtonTienda.setChecked(false);
                linearLayoutLista.setVisibility(View.VISIBLE);
                linearLayoutFiltro.setVisibility(View.VISIBLE);
                aBooleanProdOrStore = true;
            }
        });
        radioButtonTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bitmapList.isEmpty())bitmapList.clear();
                textViewResultados.setVisibility(View.GONE);
                radioButtonProducto.setChecked(false);
                linearLayoutLista.setVisibility(View.VISIBLE);
                linearLayoutFiltro.setVisibility(View.GONE);
                aBooleanProdOrStore = false;
            }
        });

        imageViewLupa.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    if (!editTextCampo.getText().toString().isEmpty())
                    {
                        progress = new ProgressDialog(getContext());


                        JSONObject object = new JSONObject();
                        object.put("producto", editTextCampo.getText().toString());
                        object.put("tipo", nomFiltros[posFiltro]);

                        //Cuando aBooleanProdOrStore es true se activa el producto, de lo contrario es la tienda
                        if (aBooleanProdOrStore)
                        {
                            //Producto
                            progress.setMessage("Buscando producto con el termino \"" + editTextCampo.getText().toString() + "\"");
                            progress.setCanceledOnTouchOutside(false);
                            progress.show();
                            new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/consultaAvanzada.php", object.toString());
                        }
                        else
                        {
                            //Tienda
                            progress.setMessage("Buscando tienda con el termino \"" + editTextCampo.getText().toString() + "\"");
                            progress.setCanceledOnTouchOutside(false);
                            progress.show();
                            new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/consultaAvanzadaTienda.php", object.toString());
                        }

                    }
                    else
                    {
                        Toast.makeText(getContext(), "Debes ingresar el nombre de un producto", Toast.LENGTH_SHORT).show();
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });


        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, nomFiltros);
        spinnerFiltro.setAdapter(arrayAdapter);
        spinnerFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                posFiltro = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        gridViewProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (aBooleanProdOrStore)
                {
                    FragmentTransaction trans = getFragmentManager().beginTransaction();
                    trans.replace(R.id.content_general, newInstance(bitmapList.get(position), busquedaAvanzadaProductosList.get(position).getNameProd(), busquedaAvanzadaProductosList.get(position).getIdStore(), busquedaAvanzadaProductosList.get(position).getIdProd(), busquedaAvanzadaProductosList.get(position).getNameProd(), busquedaAvanzadaProductosList.get(position).getDesProd(), busquedaAvanzadaProductosList.get(position).getpProd()));
                    trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    trans.addToBackStack(null);
                    trans.commit();
                }
                else
                {
                    if (busquedaAvanzadaTiendasList.get(position).getUserId() == Integer.parseInt(Coordenadas.id))
                    {
                        FragmentTransaction trans = getFragmentManager().beginTransaction();
                        trans.replace(R.id.content_general, newInstanceStoreMe(bitmapList.get(position), busquedaAvanzadaTiendasList.get(position).getNameStore(), busquedaAvanzadaTiendasList.get(position).getDescriptionStore(), busquedaAvanzadaTiendasList.get(position).getStreetStore(), busquedaAvanzadaTiendasList.get(position).getNumberStore() + "", busquedaAvanzadaTiendasList.get(position).getStartDay(), busquedaAvanzadaTiendasList.get(position).getEndDay(), busquedaAvanzadaTiendasList.get(position).getOpenHour(), busquedaAvanzadaTiendasList.get(position).getCloseHour(), busquedaAvanzadaTiendasList.get(position).getLunchHour(), busquedaAvanzadaTiendasList.get(position).getLunchAfterHour(), busquedaAvanzadaTiendasList.get(position).getStoreId() + "", busquedaAvanzadaTiendasList.get(position).getLatLngStore().latitude + "", busquedaAvanzadaTiendasList.get(position).getLatLngStore().longitude + ""));
                        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        trans.addToBackStack(null);
                        trans.commit();
                    }
                    else
                    {
                        FragmentTransaction trans = getFragmentManager().beginTransaction();
                        trans.replace(R.id.content_general, newInstanceStoreOtherUser(bitmapList.get(position), busquedaAvanzadaTiendasList.get(position).getNameStore(), busquedaAvanzadaTiendasList.get(position).getDescriptionStore(), busquedaAvanzadaTiendasList.get(position).getStreetStore(), busquedaAvanzadaTiendasList.get(position).getNumberStore() + "", busquedaAvanzadaTiendasList.get(position).getStartDay(), busquedaAvanzadaTiendasList.get(position).getEndDay(), busquedaAvanzadaTiendasList.get(position).getOpenHour(), busquedaAvanzadaTiendasList.get(position).getCloseHour(), busquedaAvanzadaTiendasList.get(position).getLunchHour(), busquedaAvanzadaTiendasList.get(position).getLunchAfterHour(), busquedaAvanzadaTiendasList.get(position).getStoreId() + "", busquedaAvanzadaTiendasList.get(position).getLatLngStore().latitude + "", busquedaAvanzadaTiendasList.get(position).getLatLngStore().longitude + "", busquedaAvanzadaTiendasList.get(position).getUserId() + ""));
                        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        trans.addToBackStack(null);
                        trans.commit();
                    }
                }
            }
        });
        return v;
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
    class customAdvanced extends BaseAdapter
    {

        @Override
        public int getCount() {
            return bitmapList.size();
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
            convertView = getActivity().getLayoutInflater().inflate(R.layout.custom_busqueda_avanzada, null);
            ImageView imageViewProducto = (ImageView)convertView.findViewById(R.id.ivProducto);
            TextView textViewProducto = (TextView)convertView.findViewById(R.id.tvNombreProd);
            TextView textViewTienda = (TextView)convertView.findViewById(R.id.tvNombreTienda);
            TextView textViewPrecio = (TextView)convertView.findViewById(R.id.tvPrecio);

            if (aBooleanProdOrStore)
            {
                imageViewProducto.setImageDrawable(new MetodosCreados().RedondearBitmap(bitmapList.get(position), getResources()));
                textViewProducto.setText(Html.fromHtml("<b>Producto: </b>" + busquedaAvanzadaProductosList.get(position).getNameProd().toString()));
                textViewTienda.setText(Html.fromHtml("<b>Tienda: </b>" + busquedaAvanzadaProductosList.get(position).getNameStore().toString()));
                textViewPrecio.setText("$" + busquedaAvanzadaProductosList.get(position).getpProd() + "");
            }
            else
            {
                imageViewProducto.setImageDrawable(new MetodosCreados().RedondearBitmap(bitmapList.get(position), getResources()));
                String openupdate = new MetodosCreados().HoraNormal(busquedaAvanzadaTiendasList.get(position).getOpenHour());
                String closeupdate = new MetodosCreados().HoraNormal(busquedaAvanzadaTiendasList.get(position).getCloseHour());

                if (busquedaAvanzadaTiendasList.get(position).getLunchHour().equals("00:00:00") && busquedaAvanzadaTiendasList.get(position).getLunchAfterHour().equals("00:00:00"))
                {
                    textViewProducto.setText("De " + busquedaAvanzadaTiendasList.get(position).getStartDay() + " a " + busquedaAvanzadaTiendasList.get(position).getEndDay()+ ", horario continuado desde las " + openupdate + " hasta las " + closeupdate);
                }
                else
                {
                    String openupdatelunch = new MetodosCreados().HoraNormal(busquedaAvanzadaTiendasList.get(position).getLunchHour());
                    String closeupdatelunch = new MetodosCreados().HoraNormal(busquedaAvanzadaTiendasList.get(position).getLunchAfterHour());
                    textViewProducto.setText("De " + busquedaAvanzadaTiendasList.get(position).getStartDay() + " a " + busquedaAvanzadaTiendasList.get(position).getEndDay()+ ", horario continuado desde las " + openupdate + " hasta las " + closeupdate + ", horario tarde desde las " + openupdatelunch + " hasta las " + closeupdatelunch);
                }

                textViewTienda.setText(busquedaAvanzadaTiendasList.get(position).getNameStore().toString());
                textViewPrecio.setText("Pasaje: " + busquedaAvanzadaTiendasList.get(position).getStreetStore() + " #" + busquedaAvanzadaTiendasList.get(position).getNumberStore());
            }

            return convertView;
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public class EjecutarSentencia extends AsyncTask<String, Void, String> {
        @Override
        public String doInBackground(String... params) {
            HttpURLConnection conn = null;
            try {
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

                switch (responseCode) {
                    case 200:
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                        return response.toString();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (conn != null) {
                    try {
                        conn.disconnect();
                    } catch (Exception ex) {
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

                try
                {
                    //Cuando aBooleanProdOrStore es true se activa el producto, de lo contrario es la tienda
                    if (aBooleanProdOrStore)
                    {
                        //Producto
                        if (!busquedaAvanzadaProductosList.isEmpty())
                        {
                            busquedaAvanzadaProductosList.clear();
                            bitmapList.clear();
                        }
                        JSONArray jsonArray = new JSONArray(s);
                        int nMitad = jsonArray.length() / 2;
                        int vLocal = 0;
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject object = jsonArray.getJSONObject(i);
                            if (i >= nMitad)
                            {
                                bitmapList.add(Codificacion.decodeBase64(object.getString("photo_" + vLocal)));
                                vLocal++;
                            }
                            else
                            {
                                BusquedaAvanzadaProductos busquedaAvanzadaProductos = new BusquedaAvanzadaProductos();
                                busquedaAvanzadaProductos.setIdProd(object.getInt("id"));
                                busquedaAvanzadaProductos.setIdStore(object.getInt("" + 0));
                                busquedaAvanzadaProductos.setNameProd(object.getString("name"));
                                busquedaAvanzadaProductos.setNameStore(object.getString("" + 1));
                                busquedaAvanzadaProductos.setDesProd(object.getString("description"));
                                busquedaAvanzadaProductos.setpProd(object.getInt("price"));
                                busquedaAvanzadaProductosList.add(busquedaAvanzadaProductos);
                            }
                        }
                        textViewResultados.setVisibility(View.VISIBLE);
                        textViewResultados.setText(Html.fromHtml("<b>Resultados encontrados:</b> " + (vLocal)));
                        gridViewProductos.setAdapter(new customAdvanced());
                        progress.dismiss();
                    }
                    else
                    {
                        if (!busquedaAvanzadaTiendasList.isEmpty())
                        {
                            busquedaAvanzadaTiendasList.clear();
                            bitmapList.clear();
                        }
                        JSONArray jsonArray = new JSONArray(s);
                        int nMitad = jsonArray.length() / 2;
                        int vLocal = 0;
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject object = jsonArray.getJSONObject(i);
                            if (i >= nMitad)
                            {
                                bitmapList.add(Codificacion.decodeBase64(object.getString("photo_" + vLocal)));
                                vLocal++;
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
                                busquedaAvanzadaTiendas.setUserId(object.getInt("user_id"));
                                busquedaAvanzadaTiendasList.add(busquedaAvanzadaTiendas);
                            }
                        }
                        textViewResultados.setVisibility(View.VISIBLE);
                        textViewResultados.setText(Html.fromHtml("<b>Resultados encontrados:</b> " + (vLocal)));
                        gridViewProductos.setAdapter(new customAdvanced());
                        progress.dismiss();
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                progress.dismiss();
                Toast.makeText(getContext(), "No se ha encontrado resultado con el nombre "+ editTextCampo.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
