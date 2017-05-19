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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StoreFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreFragment extends Fragment implements StoreFragmentSelected.OnFragmentInteractionListener
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String id_usuario;


    Bitmap[] images;
    String[] id, name, des, street, numberStreet, open_hour, close_hour, lunch_hour, lunch_after_hour, start_day, end_day;
    ListView listViewStores;
    ProgressDialog progress;


    private OnFragmentInteractionListener mListener;

    public StoreFragment()
    {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters

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
        progress.setMessage("Cargando datos...");
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
                trans.replace(R.id.content_general, newInstance(Codificacion.encodeToBase64(images[position], Bitmap.CompressFormat.PNG, 60), name[position], des[position], street[position]
                , numberStreet[position], start_day[position], end_day[position], open_hour[position], close_hour[position], lunch_hour[position], lunch_after_hour[position]));
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
        public int getCount() {
            return images.length; //Indico las veces que debe recorrer
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
            ImageView imageView = (ImageView)convertView.findViewById(R.id.ivUnoProducts);
            TextView textViewNombre = (TextView)convertView.findViewById(R.id.txtOption);
            TextView textViewDesStore = (TextView)convertView.findViewById(R.id.txtDesStore);

            imageView.setImageBitmap(images[position]);
            textViewNombre.setText(name[position]);
            textViewDesStore.setText(des[position]);

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
                    Toast.makeText(getContext(), "Aun no tienes tiendas creadas.", Toast.LENGTH_LONG).show();
                    progress.dismiss();
                }
                else
                {
                    JSONArray jsonArray = new JSONArray(s);
                    int tomarCuenta = jsonArray.length() / 2; //Indica que la otra mitad son las fotos
                    id = new String[tomarCuenta];
                    name = new String[tomarCuenta];
                    des = new String[tomarCuenta];
                    street = new String[tomarCuenta];
                    numberStreet = new String[tomarCuenta];
                    open_hour = new String[tomarCuenta];
                    close_hour = new String[tomarCuenta];
                    lunch_hour = new String[tomarCuenta];
                    lunch_after_hour = new String[tomarCuenta];
                    start_day = new String[tomarCuenta];
                    end_day = new String[tomarCuenta];
                    images = new Bitmap[tomarCuenta];

                    JSONObject jsonObject = null;
                    int cLocal = 0;
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        jsonObject = jsonArray.getJSONObject(i);
                        if (i >= tomarCuenta)
                        {
                            Bitmap bitmap = Codificacion.decodeBase64(jsonObject.getString("photo_"+cLocal));
                            images[cLocal] = bitmap;
                            cLocal++;
                        }
                        else
                        {
                            id[i] = jsonObject.getString("id");
                            name[i] = jsonObject.getString("name");
                            des[i] = jsonObject.getString("description");
                            street[i] = jsonObject.getString("street");
                            numberStreet[i] = jsonObject.getString("number");
                            open_hour[i] = jsonObject.getString("open_hour");
                            close_hour[i] = jsonObject.getString("close_hour");
                            lunch_hour[i] = jsonObject.getString("lunch_hour");
                            lunch_after_hour[i] = jsonObject.getString("lunch_after_hour");
                            start_day[i] = jsonObject.getString("start_day");
                            end_day[i] = jsonObject.getString("end_day");
                        }
                    }
                    CustomAdapter customAdapter = new CustomAdapter();
                    listViewStores.setAdapter(customAdapter);
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
