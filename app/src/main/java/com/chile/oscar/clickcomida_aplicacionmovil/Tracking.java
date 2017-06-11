package com.chile.oscar.clickcomida_aplicacionmovil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Coordenadas;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Pedidos_Proceso;

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
 * {@link Tracking.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tracking#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tracking extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView listViewPedidos;
    List<Pedidos_Proceso> pedidos_procesoList;
    ProgressDialog progress;

    private OnFragmentInteractionListener mListener;

    public Tracking() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tracking.
     */
    // TODO: Rename and change types and number of parameters
    public static Tracking newInstance(String param1, String param2) {
        Tracking fragment = new Tracking();
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
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        progress = new ProgressDialog(getContext());
        progress.setMessage("Cargando pedidos...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        View v = inflater.inflate(R.layout.fragment_tracking, container, false);
        listViewPedidos = (ListView) v.findViewById(R.id.lvPedidos);
        pedidos_procesoList = new ArrayList<>();
        try
        {
            JSONObject object = new JSONObject();
            object.put("user_id", Coordenadas.id);
            new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/cargarPedidos_usuario.php", object.toString());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

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
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }
    class PedidosAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return pedidos_procesoList.size();
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
            convertView = getActivity().getLayoutInflater().inflate(R.layout.custom_tracking, null);
            TextView textViewIdPedido = (TextView)convertView.findViewById(R.id.txtIdPedido);
            TextView textViewEstado = (TextView)convertView.findViewById(R.id.txtEstado);
            TextView textViewNombreTienda = (TextView)convertView.findViewById(R.id.txtNombreTienda);
            TextView textViewFecha= (TextView)convertView.findViewById(R.id.txtFecha);

            textViewIdPedido.setText(pedidos_procesoList.get(position).getOrden_id() + "");
            textViewEstado.setText(pedidos_procesoList.get(position).getEstado() + "");
            textViewNombreTienda.setText("Tienda: " + pedidos_procesoList.get(position).getNombreTienda() + "");
            textViewFecha.setText(pedidos_procesoList.get(position).getOrden_fecha() + "");


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
                if (!s.equals("[]"))
                {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Pedidos_Proceso pedidos_proceso = new Pedidos_Proceso();
                        pedidos_proceso.setOrden_id(object.getInt("id"));
                        pedidos_proceso.setOrden_fecha(object.getString("date"));
                        pedidos_proceso.setEstado(object.getString("description"));
                        pedidos_proceso.setNombreTienda(object.getString("name"));
                        pedidos_procesoList.add(pedidos_proceso);
                    }
                    progress.dismiss();
                    listViewPedidos.setAdapter(new PedidosAdapter());
                }
                else
                {
                    progress.dismiss();
                    Toast.makeText(getContext(), "Aun no tienes pedidos", Toast.LENGTH_SHORT).show();
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
