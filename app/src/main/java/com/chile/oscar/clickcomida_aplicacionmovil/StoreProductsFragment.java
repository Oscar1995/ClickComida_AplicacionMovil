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
import android.widget.TextView;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Codificacion;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Favoritos_Tienda;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.MetodosCreados;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


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

    List<String> ids;
    List<String> noms;
    List<String> des;
    List<Bitmap> imagens;
    List<String> fechas;
    List<String> prices;

    List<Favoritos_Tienda> favoritosTiendaList;

    ListView listViewProducts_Stores;
    ProgressDialog progress;

    private OnFragmentInteractionListener mListener;

    public StoreProductsFragment()
    {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Details_products newInstance(Bitmap imagen, String product_id, String nombre_prod, String des_prod, String precio_prod)
    {
        Details_products fragment = new Details_products();
        Bundle args = new Bundle();
        args.putString("imagen_prod", Codificacion.encodeToBase64(imagen, Bitmap.CompressFormat.PNG, 100));
        args.putString("product_id", product_id);
        args.putString("nombre_prod", nombre_prod);
        args.putString("des_prod", des_prod);
        args.putString("precio_prod", precio_prod);
        fragment.setArguments(args);
        return fragment;
    }
    public static StoreOtherUser newStoreOther (Bitmap imagen, String... params)
    {
        StoreOtherUser fragment = new StoreOtherUser();
        Bundle args = new Bundle();
        args.putString("imagen_tienda", Codificacion.encodeToBase64(imagen, Bitmap.CompressFormat.PNG, 100));
        args.putString("nombre_tienda", params[0]);
        args.putString("des_tienda", params[1]);
        args.putString("calle_tienda", params[2]);
        args.putString("numero_tienda", params[3]);
        args.putString("start_day", params[4]);
        args.putString("end_day", params[5]);
        args.putString("open_hour", params[6]);
        args.putString("close_hour", params[7]);
        args.putString("lunch_open_hour", params[8]);
        args.putString("lunch_after_hour", params[9]);
        args.putString("tienda_id", params[10]);
        args.putString("user_id", params[11]);
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
                    trans.replace(R.id.content_general, newInstance(imagens.get(position), ids.get(position), noms.get(position), des.get(position), prices.get(position)));
                    trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    trans.addToBackStack(null);
                    trans.commit();
                }
                else if (mTipo.equals("Tiendas"))
                {
                    FragmentTransaction trans = getFragmentManager().beginTransaction();
                    trans.replace(R.id.content_general, newStoreOther(
                            imagens.get(position),
                            noms.get(position),
                            favoritosTiendaList.get(position).getDescripcion(),
                            favoritosTiendaList.get(position).getCalle(),
                            favoritosTiendaList.get(position).getNumero(),
                            favoritosTiendaList.get(position).getStart_day(),
                            favoritosTiendaList.get(position).getEnd_day(),
                            favoritosTiendaList.get(position).getOpen_hour(),
                            favoritosTiendaList.get(position).getClose_hour(),
                            favoritosTiendaList.get(position).getLunch_open_hour(),
                            favoritosTiendaList.get(position).getLunch_after_hour(),
                            ids.get(position),
                            favoritosTiendaList.get(position).getUser_id()));
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
            return imagens.size();
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

                imageView.setImageDrawable(new MetodosCreados().RedondearBitmap(imagens.get(position), getResources()));
                textViewNombre.setText(noms.get(position));
                textViewDesStore.setText("Agregado el: " + new MetodosCreados().formatearFecha(fechas.get(position)));
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
                    int tomarCuenta = jsonArray.length() / 3; //1: Informacion, 2:fotos, 3:fechas de creacion
                    JSONObject jsonObject = null;

                    if (mTipo.equals("Productos"))
                    {
                        des = new ArrayList<>();
                        prices = new ArrayList<>();
                    }

                    ids = new ArrayList<>();
                    noms= new ArrayList<>();
                    fechas = new ArrayList<>();
                    imagens = new ArrayList<>();
                    favoritosTiendaList = new ArrayList<>();

                    int cLocal = 0;
                    int fLocal = 0;

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        String x = jsonArray.getString(i);
                        if (i >= tomarCuenta)
                        {
                            JSONObject object = new JSONObject(x);

                            if (cLocal <= (tomarCuenta-1))
                            {
                                imagens.add(Codificacion.decodeBase64(object.getString("photo_"+cLocal)));
                                cLocal++;
                            }
                            else
                            {
                                fechas.add(object.getString("fecha_"+fLocal));
                                fLocal++;
                            }
                        }
                        else
                        {

                            JSONArray jsonArray1 = new JSONArray(x);
                            jsonObject = jsonArray1.getJSONObject(0);

                            if (mTipo.equals("Productos"))
                            {
                                des.add(jsonObject.getString("description"));
                                prices.add(jsonObject.getString("price"));
                            }
                            else if (mTipo.equals("Tiendas"))
                            {
                                Favoritos_Tienda favoritos_tienda = new Favoritos_Tienda();
                                favoritos_tienda.setDescripcion(jsonObject.getString("description"));
                                favoritos_tienda.setCalle(jsonObject.getString("street"));
                                favoritos_tienda.setNumero(jsonObject.getString("number"));
                                favoritos_tienda.setOpen_hour(jsonObject.getString("open_hour"));
                                favoritos_tienda.setClose_hour(jsonObject.getString("close_hour"));
                                favoritos_tienda.setLunch_open_hour(jsonObject.getString("lunch_hour"));
                                favoritos_tienda.setLunch_after_hour(jsonObject.getString("lunch_after_hour"));
                                favoritos_tienda.setStart_day(jsonObject.getString("start_day"));
                                favoritos_tienda.setEnd_day(jsonObject.getString("end_day"));
                                favoritos_tienda.setUser_id(jsonObject.getString("user_id"));
                                favoritosTiendaList.add(favoritos_tienda);

                            }
                            ids.add(jsonObject.getString("id"));
                            noms.add(jsonObject.getString("name"));
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
