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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
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



public class ProductsOtherUser extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters
    private String store_id, nombreTienda;
    private Bitmap imagenTienda;

    AutoCompleteTextView autoCompleteTextViewProducto;
    Spinner spinnerFiltro;
    GridView gridViewProductos;

    List<String> nombreProd, desProd, precioProd;
    List<Bitmap> imagesProd;
    int posProd;

    ProgressDialog progressDialog;


    private OnFragmentInteractionListener mListener;

    public ProductsOtherUser()
    {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Details_products newInstance(String prodDes)
    {
        Details_products fragment = new Details_products();
        Bundle args = new Bundle();
        args.putString("des_prod", prodDes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            store_id = getArguments().getString("store_id");
            nombreTienda = getArguments().getString("nombre_tienda");
            imagenTienda = Codificacion.decodeBase64(getArguments().getString("imagenTienda"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_products_other_user, container, false);
        autoCompleteTextViewProducto = (AutoCompleteTextView)v.findViewById(R.id.actvProductos);
        spinnerFiltro = (Spinner)v.findViewById(R.id.sFiltro);
        gridViewProductos = (GridView)v.findViewById(R.id.gvProductos);

        ImageView imageViewTienda = (ImageView)v.findViewById(R.id.ivTiendaOther);
        TextView textViewTituloTienda = (TextView)v.findViewById(R.id.tvTituloTienda);
        Button buttonAgregarCarro = (Button)v.findViewById(R.id.btnAgregarCarro);
        Button buttonDetalles = (Button)v.findViewById(R.id.btnDetalles);

        String[] tOption = {"Menor precio", "Mayor precio"};
        ArrayAdapter arrayAdapterFiltro = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, tOption);
        spinnerFiltro.setAdapter(arrayAdapterFiltro);

        imageViewTienda.setImageDrawable(new MetodosCreados().RedondearBitmap(imagenTienda, getResources()));
        textViewTituloTienda.setText(getResources().getString(R.string.titulo_productos_tienda) + " " + nombreTienda);
        cargarProductos();

        gridViewProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                posProd = position;
            }
        });

        buttonDetalles.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.content_general, newInstance(desProd.get(posProd)));
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);
                trans.commit();
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
    class ProductoAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return nombreProd.size();
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
            convertView = getActivity().getLayoutInflater().inflate(R.layout.custom_products_other, null);
            ImageView imageViewProd = (ImageView)convertView.findViewById(R.id.ivProductOther);
            TextView textViewNombre = (TextView)convertView.findViewById(R.id.txtNombreProd);
            TextView textViewPrecio = (TextView)convertView.findViewById(R.id.etPrecioProd);

            imageViewProd.setImageDrawable(new MetodosCreados().RedondearBitmap(imagesProd.get(position), getResources()));
            textViewNombre.setText(nombreProd.get(position));
            textViewPrecio.setText("$" + precioProd.get(position));

            return convertView;
        }
    }
    public void cargarProductos ()
    {
        JSONObject object = new JSONObject();
        try
        {
            object.put("store_id", store_id);
            new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/cargarProductos_other.php", object.toString());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
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
            try
            {
                JSONArray jsonArray = new JSONArray(s);

                int tomarCuenta = jsonArray.length() / 2; //Indica que la otra mitad son las fotos
                JSONObject jsonObject = null;

                int cLocal = 0;

                nombreProd = new ArrayList<>();
                desProd = new ArrayList<>();
                precioProd = new ArrayList<>();
                imagesProd = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    jsonObject = jsonArray.getJSONObject(i);
                    if (i >= tomarCuenta)
                    {
                        imagesProd.add(Codificacion.decodeBase64(jsonObject.getString("photo_"+cLocal)));
                        cLocal++;
                    }
                    else
                    {
                        nombreProd.add(jsonObject.getString("name"));
                        desProd.add(jsonObject.getString("description"));
                        precioProd.add(jsonObject.getString("price"));
                    }
                }
                ArrayAdapter arrayAdapterTexto = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, nombreProd);
                autoCompleteTextViewProducto.setAdapter(arrayAdapterTexto);

                gridViewProductos.setAdapter(new ProductoAdapter());
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
