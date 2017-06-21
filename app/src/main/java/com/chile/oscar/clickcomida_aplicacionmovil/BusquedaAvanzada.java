package com.chile.oscar.clickcomida_aplicacionmovil;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.BusquedaAvanzadaProductos;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Codificacion;

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
    List<BusquedaAvanzadaProductos> busquedaAvanzadaProductosList;
    List<Bitmap> bitmapList;

    TextView textViewResultados;
    EditText editTextCampo;
    ImageView imageViewLupa;
    Spinner spinnerFiltro;
    GridView gridViewProductos;
    int posFiltro;

    private OnFragmentInteractionListener mListener;

    public BusquedaAvanzada() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BusquedaAvanzada.
     */
    // TODO: Rename and change types and number of parameters
    public static BusquedaAvanzada newInstance(String param1, String param2) {
        BusquedaAvanzada fragment = new BusquedaAvanzada();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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

        final String[] nomFiltros = {"Nombre", "Menor Precio", "Mayor Precio"};
        editTextCampo = (EditText)v.findViewById(R.id.etProducto);
        imageViewLupa = (ImageView)v.findViewById(R.id.ivLupa);
        spinnerFiltro = (Spinner)v.findViewById(R.id.sFiltro);
        textViewResultados = (TextView)v.findViewById(R.id.tvResultados);
        gridViewProductos = (GridView)v.findViewById(R.id.gvProductosSearch);

        imageViewLupa.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    if (!editTextCampo.getText().toString().isEmpty())
                    {
                        JSONObject object = new JSONObject();
                        object.put("producto", editTextCampo.getText().toString());
                        object.put("tipo", nomFiltros[posFiltro]);
                        new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/consultaAvanzada.php", object.toString());
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
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
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
                busquedaAvanzadaProductosList = new ArrayList<>();
                bitmapList = new ArrayList<>();
                try
                {
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
                            busquedaAvanzadaProductos.setIdStore(object.getInt("" + i));
                            busquedaAvanzadaProductos.setNameProd(object.getString("name"));
                            busquedaAvanzadaProductos.setNameStore(object.getString("" + i));
                            busquedaAvanzadaProductos.setDesProd(object.getString("description"));
                            busquedaAvanzadaProductos.setpProd(object.getInt("price"));
                            busquedaAvanzadaProductosList.add(busquedaAvanzadaProductos);
                        }

                        /*[{"id":"18","0":"21","name":"Compu","1":"Luz","2":"18","3":"Compu","description":"lala","4":"lala","price":"250","5":"250"}]

                        id_product = "id"
                        id_store = "0"
                        nameProd = "name"
                        nameStore = "1"
                        desProd = "description"
                        precioProd = "price"*/
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(getContext(), "No se ha encontrado resultado con el nombre "+ editTextCampo.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
