package com.chile.oscar.clickcomida_aplicacionmovil;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Codificacion;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Coordenadas;

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
 * {@link showProducts_me.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link showProducts_me#newInstance} factory method to
 * create an instance of this fragment.
 */
public class showProducts_me extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    ListView listViewProductos;
    private OnFragmentInteractionListener mListener;

    String store_id, store_name;

    int[] images = {R.drawable.ic_cancelar};
    String[] des = {"Descripcion"};

    ArrayList<String> descripcion;

    public showProducts_me()
    {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static fragmentProductosVender newInstance(String store_id)
    {
        fragmentProductosVender fragment = new fragmentProductosVender();
        Bundle args = new Bundle();
        args.putString("store_id", store_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            store_id = getArguments().getString("store_id");
            store_name = getArguments().getString("store_name");
        }
    }
    int posCol = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_show_products_me, container, false);
        TextView textView = (TextView)v.findViewById(R.id.txtTitulo_me);
        textView.setText(getResources().getString(R.string.titulo_mis_productos) + " " + store_name);
        descripcion = new ArrayList<String>();
        descripcion.add("Manzana");
        descripcion.add("Pera");
        descripcion.add("Naranja");
        listViewProductos = (ListView)v.findViewById(R.id.lvProductosTienda_me);
        listViewProductos.setAdapter(new CustomAdapter());


        Button buttonAdd = (Button)v.findViewById(R.id.btnAgregarProductos_me);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.content_general, newInstance(store_id));
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
    class CustomAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return 6;
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
            convertView = getActivity().getLayoutInflater().inflate(R.layout.customlayout_product, null);

            ImageView imageViewProd = (ImageView)convertView.findViewById(R.id.ivUnoProducts);
            ImageView imageViewProdDos = (ImageView)convertView.findViewById(R.id.ivDosProducts);
            ImageView imageViewProdTres = (ImageView)convertView.findViewById(R.id.ivTresProducts);

            final TextView textViewUno = (TextView)convertView.findViewById(R.id.txtDesUno);
            final TextView textViewDos = (TextView)convertView.findViewById(R.id.txtDesDos);
            final TextView textViewTres = (TextView)convertView.findViewById(R.id.txtDesTres);

            imageViewProd.setImageResource(images[0]);
            imageViewProdDos.setImageResource(images[0]);
            imageViewProdTres.setImageResource(images[0]);

            textViewUno.setText("Manzana");
            textViewDos.setText("Pera");
            textViewTres.setText("Naranja");


            imageViewProd.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = descripcion.indexOf(textViewUno.getText());
                    Toast.makeText(getContext(), "pos:" + pos, Toast.LENGTH_SHORT).show();
                }
            });
            imageViewProdDos.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = descripcion.indexOf(textViewDos.getText());
                    Toast.makeText(getContext(), "pos:" + pos, Toast.LENGTH_SHORT).show();
                }
            });
            imageViewProdTres.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    int pos = descripcion.indexOf(textViewTres.getText());
                    Toast.makeText(getContext(), "pos:" + pos, Toast.LENGTH_SHORT).show();
                }
            });


            return convertView;
        }
    }
    public class getProducts extends AsyncTask<String, Void, String>
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

        }
    }
}
