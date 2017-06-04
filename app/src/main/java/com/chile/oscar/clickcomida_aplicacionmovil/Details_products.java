package com.chile.oscar.clickcomida_aplicacionmovil;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Codificacion;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Coordenadas;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Details_products.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Details_products#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Details_products extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Boolean prodFav = false;
    RatingBar ratingBarGlobal, ratingBarUser;
    Bitmap imagenProducto;
    ImageView imageViewFavProd;
    String store_id, product_id, nombreProd, desProd, precioProd, tipoReg;

    private OnFragmentInteractionListener mListener;

    public Details_products() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Details_products.
     */
    // TODO: Rename and change types and number of parameters
    public static Details_products newInstance(String param1, String param2) {
        Details_products fragment = new Details_products();
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
            imagenProducto = Codificacion.decodeBase64(getArguments().getString("imagen_prod"));
            store_id = getArguments().getString("store_id");
            product_id = getArguments().getString("product_id");
            nombreProd = getArguments().getString("nombre_prod");
            desProd = getArguments().getString("des_prod");
            precioProd = getArguments().getString("precio_prod");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_details_products, container, false);

        ImageView imageViewProd = (ImageView)v.findViewById(R.id.ivProductoOther);
        imageViewFavProd  = (ImageView)v.findViewById(R.id.ivFavoritoProd);
        ratingBarGlobal = (RatingBar)v.findViewById(R.id.rbProdGlobal);
        ratingBarUser = (RatingBar)v.findViewById(R.id.rbProdUsuario);
        TextView textViewNombre = (TextView)v.findViewById(R.id.tvNombreProdOther);
        TextView textViewDescripcion = (TextView)v.findViewById(R.id.tvDesProductoOther);
        TextView textViewPrecio = (TextView)v.findViewById(R.id.tvPrecioProductoOther);
        Button buttonCarro = (Button)v.findViewById(R.id.btnAgregarProdCarro);

        imageViewProd.setImageDrawable(new MetodosCreados().RedondearBitmap(imagenProducto, getResources()));
        textViewNombre.setText(nombreProd);
        textViewDescripcion.setText(desProd);
        textViewPrecio.setText("$" + precioProd);

        cargarFavorito();

        imageViewFavProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                JSONObject object = new JSONObject();
                try
                {
                    object.put("user_id", Coordenadas.id);
                    object.put("product_id", product_id);

                    if (prodFav)
                    {
                        tipoReg = "Eliminar favoritos";
                        new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/eliminarFavoritoProducto.php", object.toString());
                    }
                    else
                    {
                        tipoReg = "Insertar favoritos";
                        new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/insertarFavoritosProducto.php", object.toString());
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
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
    public void onAttach(Context context)
    {
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

    public void cargarFavorito ()
    {
        try
        {
            JSONObject object = new JSONObject();
            object.put("user_id", Coordenadas.id);
            object.put("product_id", product_id);
            tipoReg = "Cargar favorito";
            new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/cargarFavoritoProducto.php", object.toString());
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
                JSONObject object = new JSONObject(s);
                if (tipoReg.equals("Insertar favoritos"))
                {
                    String res = object.getString("Insertado");
                    if (res.equals("Si"))
                    {
                        prodFav = true;
                        imageViewFavProd.setImageResource(R.drawable.heart_active);
                        Toast.makeText(getContext(), "Este producto ha sido agregado a tus favoritos", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (tipoReg.equals("Eliminar favoritos"))
                {
                    String res = object.getString("Eliminado");
                    if (res.equals("Si"))
                    {
                        prodFav = false;
                        imageViewFavProd.setImageResource(R.drawable.heart_desactive);
                        Toast.makeText(getContext(), "El producto ya no pertenece a tus favoritos", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (tipoReg.equals("Cargar favorito"))
                {
                    String res = object.getString("Resultado");
                    if (res.equals("Si"))
                    {
                        prodFav = true;
                        imageViewFavProd.setImageResource(R.drawable.heart_active);
                    }
                    else
                    {
                        prodFav = false;
                        imageViewFavProd.setImageResource(R.drawable.heart_desactive);
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
