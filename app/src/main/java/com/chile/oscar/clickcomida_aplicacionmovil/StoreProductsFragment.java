package com.chile.oscar.clickcomida_aplicacionmovil;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.BusquedaAvanzadaProductos;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StoreProductsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StoreProductsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreProductsFragment extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mTipo, user_id, tipoLoad;

    List<Bitmap> bitmapList = new ArrayList<>();

    List<BusquedaAvanzadaTiendas> busquedaAvanzadaTiendasList = new ArrayList<>();
    List<BusquedaAvanzadaProductos> busquedaAvanzadaProductosList = new ArrayList<>();

    List<String> stringListFechas = new ArrayList<>();
    ListView listViewProducts_Stores;
    ProgressDialog progress;

    private OnFragmentInteractionListener mListener;

    public StoreProductsFragment()
    {
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

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mTipo = getArguments().getString("Tipo");
            user_id = getArguments().getString("user_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        progress = new ProgressDialog(getContext());

        View view =  inflater.inflate(R.layout.fragment_store_products, container, false);
        TextView textViewTitulo = (TextView)view.findViewById(R.id.txtTituloFavoritos);
        listViewProducts_Stores = (ListView)view.findViewById(R.id.lvStores_Products);
        if (mTipo.equals("Tiendas"))textViewTitulo.setText(mTipo + " Favoritas");else if (mTipo.equals("Productos"))textViewTitulo.setText(mTipo + " Favoritos");

        JSONObject object = new JSONObject();
        try
        {
            object.put("user_id", user_id);
            if (mTipo.equals("Tiendas"))
            {
                progress.setMessage("Cargando tiendas favoritas...");
                progress.setCanceledOnTouchOutside(false);
                progress.show();
                new Cargar().execute(getResources().getString(R.string.direccion_web) + "Controlador/cargarFavorito_tienda_usuario.php", object.toString());
            }
            else if (mTipo.equals("Productos"))
            {
                progress.setMessage("Cargando productos favoritos...");
                progress.setCanceledOnTouchOutside(false);
                progress.show();
                new Cargar().execute(getResources().getString(R.string.direccion_web) + "Controlador/cargarFavorito_producto_usuario.php", object.toString());
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        listViewProducts_Stores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (mTipo.equals("Productos"))
                {
                    FragmentTransaction trans = getFragmentManager().beginTransaction();
                    trans.replace(R.id.content_general, newInstance(bitmapList.get(position), busquedaAvanzadaProductosList.get(position).getNameProd(), busquedaAvanzadaProductosList.get(position).getIdStore(), busquedaAvanzadaProductosList.get(position).getIdProd(), busquedaAvanzadaProductosList.get(position).getNameProd(), busquedaAvanzadaProductosList.get(position).getDesProd(), busquedaAvanzadaProductosList.get(position).getpProd()));
                    trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    trans.addToBackStack(null);
                    trans.commit();
                }
                else if (mTipo.equals("Tiendas"))
                {
                    FragmentTransaction trans = getFragmentManager().beginTransaction();
                    trans.replace(R.id.content_general, newInstanceStoreOtherUser(bitmapList.get(position), busquedaAvanzadaTiendasList.get(position).getNameStore(), busquedaAvanzadaTiendasList.get(position).getDescriptionStore(), busquedaAvanzadaTiendasList.get(position).getStreetStore(), busquedaAvanzadaTiendasList.get(position).getNumberStore() + "", busquedaAvanzadaTiendasList.get(position).getStartDay(), busquedaAvanzadaTiendasList.get(position).getEndDay(), busquedaAvanzadaTiendasList.get(position).getOpenHour(), busquedaAvanzadaTiendasList.get(position).getCloseHour(), busquedaAvanzadaTiendasList.get(position).getLunchHour(), busquedaAvanzadaTiendasList.get(position).getLunchAfterHour(), busquedaAvanzadaTiendasList.get(position).getStoreId() + "", busquedaAvanzadaTiendasList.get(position).getLatLngStore().latitude + "", busquedaAvanzadaTiendasList.get(position).getLatLngStore().longitude + "", busquedaAvanzadaTiendasList.get(position).getUserId() + ""));
                    trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    trans.addToBackStack(null);
                    trans.commit();
                }
            }
        });


        return view;
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
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    class AdapterFavorite extends BaseAdapter
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
            try
            {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.customlayout, null);
                ImageView imageView = (ImageView)convertView.findViewById(R.id.ivProductoImage);
                TextView textViewNombre = (TextView)convertView.findViewById(R.id.txtOption);
                TextView textViewDesStore = (TextView)convertView.findViewById(R.id.txtDesStore);

                imageView.setImageDrawable(new MetodosCreados().RedondearBitmap(bitmapList.get(position), getResources()));
                if (mTipo.equals("Productos"))
                {
                    textViewNombre.setText(busquedaAvanzadaProductosList.get(position).getNameProd());
                    textViewDesStore.setText("Agregado el: " + new MetodosCreados().formatearFecha(stringListFechas.get(position)));
                }
                else
                {
                    textViewNombre.setText(busquedaAvanzadaTiendasList.get(position).getNameStore());
                    textViewDesStore.setText("Agregado el: " + new MetodosCreados().formatearFecha(stringListFechas.get(position)));
                }

            }
            catch (Exception ex)
            {
                Toast.makeText(getContext(), "Intentalo otra vez", Toast.LENGTH_SHORT).show();
            }


            return convertView;
        }
    }

    public class Cargar extends AsyncTask<String, Void, String>
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
            try
            {
                if (s.equals("[]"))
                {
                    if (mTipo.equals("Tiendas"))
                    {
                        Toast.makeText(getContext(), "Aun no tienes tiendas agregadas a favoritos", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Aun no tienes productos agregados a favoritos", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                }
                else
                {
                    JSONArray jsonArray = new JSONArray(s);
                    int tomarCuenta = jsonArray.length() / 2; //1: Informacion, 2:fotos
                    int cLocal = 0;

                    if (busquedaAvanzadaTiendasList != null)
                    {
                        if (!busquedaAvanzadaTiendasList.isEmpty())
                        {
                            busquedaAvanzadaTiendasList.clear();
                            stringListFechas.clear();
                            bitmapList.clear();
                        }
                    }
                    if (busquedaAvanzadaProductosList != null)
                    {
                        if (!busquedaAvanzadaProductosList.isEmpty())
                        {
                            busquedaAvanzadaProductosList.clear();
                            stringListFechas.clear();
                            bitmapList.clear();
                        }
                    }
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        if (i >= tomarCuenta)
                        {
                            JSONObject object = jsonArray.getJSONObject(i);
                            bitmapList.add(Codificacion.decodeBase64(object.getString("photo_"+cLocal)));
                            cLocal++;
                        }
                        else
                        {
                            if (mTipo.equals("Productos"))
                            {
                                /*"id" -> Id de la tienda
                                "0" -> Id del producto
                                "2" -> Nombre del producto
                                "name" -> Tienda
                                "description" -> descripcion del producto
                                "price" -> precio del producto*/

                                JSONObject object = jsonArray.getJSONObject(i);
                                BusquedaAvanzadaProductos busquedaAvanzadaProductos = new BusquedaAvanzadaProductos();
                                busquedaAvanzadaProductos.setIdProd(object.getInt("" + 0));
                                busquedaAvanzadaProductos.setIdStore(object.getInt("id"));
                                busquedaAvanzadaProductos.setNameProd(object.getString("" + 2));
                                busquedaAvanzadaProductos.setNameStore(object.getString("name"));
                                busquedaAvanzadaProductos.setDesProd(object.getString("description"));
                                busquedaAvanzadaProductos.setpProd(object.getInt("price"));
                                stringListFechas.add(object.getString("created_at"));
                                busquedaAvanzadaProductosList.add(busquedaAvanzadaProductos);
                            }
                            else if (mTipo.equals("Tiendas"))
                            {
                                JSONObject object = jsonArray.getJSONObject(i);
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
                                busquedaAvanzadaTiendas.setUserId(0);
                                stringListFechas.add(object.getString("created_at"));
                                busquedaAvanzadaTiendasList.add(busquedaAvanzadaTiendas);
                            }
                        }
                    }
                    progress.dismiss();
                    listViewProducts_Stores.setAdapter(new AdapterFavorite());
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
