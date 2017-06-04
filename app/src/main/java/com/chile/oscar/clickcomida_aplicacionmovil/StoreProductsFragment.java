package com.chile.oscar.clickcomida_aplicacionmovil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Codificacion;
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
    List<String> ids;
    List<String> noms;
    List<Bitmap> imagens;
    ListView listViewProducts_Stores;
    ProgressDialog progress;

    private OnFragmentInteractionListener mListener;

    public StoreProductsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoreProductsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreProductsFragment newInstance(String param1, String param2) {
        StoreProductsFragment fragment = new StoreProductsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
                textViewDesStore.setText(ids.get(position));
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
                    int tomarCuenta = jsonArray.length() / 2;
                    JSONObject jsonObject = null;

                    ids = new ArrayList<>();
                    noms= new ArrayList<>();
                    imagens = new ArrayList<>();

                    int cLocal = 0;
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        String x = jsonArray.getString(i);
                        if (i >= tomarCuenta)
                        {
                            JSONObject object = new JSONObject(x);

                            imagens.add(Codificacion.decodeBase64(object.getString("photo_"+cLocal)));
                            cLocal++;
                        }
                        else
                        {

                            JSONArray jsonArray1 = new JSONArray(x);
                            jsonObject = jsonArray1.getJSONObject(0);
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
