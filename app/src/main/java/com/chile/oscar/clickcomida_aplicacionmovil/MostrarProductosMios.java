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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MostrarProductosMios.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MostrarProductosMios#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MostrarProductosMios extends Fragment
{
    String store_id, store_name;
    ArrayList<Bitmap> imagesProducts = new ArrayList<>();
    ArrayList<Integer> idProducts = new ArrayList<>();
    ArrayList<String> nameProducts = new ArrayList<>();
    ArrayList<String> desProducts = new ArrayList<>();
    int[] priceProducts;
    int[] arrCont = new int[2];
    ListView lvProd;
    ProgressDialog progress;
    private OnFragmentInteractionListener mListener;

    public MostrarProductosMios()
    {
        // Required empty public constructor
    }


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mostrar_productos_mios, container, false);

        progress = new ProgressDialog(getContext());
        progress.setMessage("Cargando productos...");
        progress.show();

        TextView textView = (TextView)view.findViewById(R.id.txtTitulo_me);
        textView.setText(getResources().getString(R.string.titulo_mis_productos) + " " + store_name);

        JSONObject object = new JSONObject();
        try
        {
            object.put("store_id", store_id);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        lvProd = (ListView)view.findViewById(R.id.listaNuevaProd);
        new getProducts().execute(getResources().getString(R.string.direccion_web) + "/Controlador/cargarProductos.php", object.toString());
        Button buttonAdd = (Button)view.findViewById(R.id.btnAgregarProductos_me);
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
        //String[] ejemplo = {"Uno", "Dos"};
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, ejemplo);
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
    int a = 0;
    int con = 0;

    class CustomAdapterProducts extends BaseAdapter
    {

        @Override
        public int getCount() {
            return imagesProducts.size();
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
            convertView = getActivity().getLayoutInflater().inflate(R.layout.customlayout, null);

            /*ImageView imageViewProd = (ImageView)convertView.findViewById(R.id.ivUnoProducts);
            ImageView imageViewProdDos = (ImageView)convertView.findViewById(R.id.ivDosProducts);
            ImageView imageViewProdTres = (ImageView)convertView.findViewById(R.id.ivTresProducts);*

            /*final TextView textViewUno = (TextView)convertView.findViewById(R.id.txtDesUno);
            final TextView textViewDos = (TextView)convertView.findViewById(R.id.txtDesDos);
            final TextView textViewTres = (TextView)convertView.findViewById(R.id.txtDesTres);*/

            try
            {
                /*imageViewProdTres.setImageBitmap(imagesProducts.get(position + a));
                textViewTres.setText(nameProducts.get(position + a));

                imageViewProdDos.setImageBitmap(imagesProducts.get(position + a));
                textViewDos.setText(nameProducts.get(position + a));

                imageViewProd.setImageBitmap(imagesProducts.get(position + a));
                textViewUno.setText(nameProducts.get(position + a));*/

                ImageView imageView = (ImageView)convertView.findViewById(R.id.ivUnoProducts);
                TextView textViewNombre = (TextView)convertView.findViewById(R.id.txtOption);
                TextView textViewDesStore = (TextView)convertView.findViewById(R.id.txtDesStore);

                imageView.setImageBitmap(imagesProducts.get(position));
                textViewNombre.setText(nameProducts.get(position));
                textViewDesStore.setText(priceProducts[position]);
            }
            catch (Exception ex)
            {

            }
            return convertView;
        }
    }
    int m3 = 3;
    int col = 1;
    int totalProd;
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
            try
            {
                if (s.equals("[]"))
                {
                    Toast.makeText(getContext(), "Aun no tienes productos registrados.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    JSONArray jsonArray = new JSONArray(s);
                    int tomarCuenta = jsonArray.length() / 2; //Indica que la otra mitad son las fotos
                    priceProducts = new int[tomarCuenta];
                    JSONObject jsonObject = null;
                    int cLocal = 0;
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        jsonObject = jsonArray.getJSONObject(i);
                        if (i >= tomarCuenta)
                        {
                            Bitmap bitmap = Codificacion.decodeBase64(jsonObject.getString("photo_"+cLocal));
                            imagesProducts.add(bitmap);
                            cLocal++;
                        }
                        else
                        {
                            idProducts.add(Integer.parseInt(jsonObject.getString("id")));
                            nameProducts.add(jsonObject.getString("name"));
                            desProducts.add(jsonObject.getString("description"));
                            priceProducts[i] = Integer.parseInt(jsonObject.getString("price"));
                        }
                    }
                    for (int i =0; i<nameProducts.size() +1; i++)
                    {
                        if (m3 == i)
                        {
                            m3+=3;
                            col +=1;
                        }
                    }

                    totalProd = nameProducts.size();
                    lvProd.setAdapter(new CustomAdapterProducts());
                    progress.dismiss();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

}
