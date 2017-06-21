package com.chile.oscar.clickcomida_aplicacionmovil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.PedidosRepartidor;

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
 * {@link PedidoPendientesRepartidor.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PedidoPendientesRepartidor#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PedidoPendientesRepartidor extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "user_id";

    // TODO: Rename and change types of parameters
    private String mParamId;
    ProgressDialog progress;
    String tipoReg = "";
    ListView listViewPedidos;
    List<PedidosRepartidor> pedidosRepartidorList;

    private OnFragmentInteractionListener mListener;

    public PedidoPendientesRepartidor() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PedidoPendientesRepartidor.
     */
    // TODO: Rename and change types and number of parameters
    public static PedidoPendientesRepartidor newInstance(String param1, String param2) {
        PedidoPendientesRepartidor fragment = new PedidoPendientesRepartidor();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParamId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pedido_pendientes_repartidor, container, false);
        Button buttonActualizar = (Button)v.findViewById(R.id.btnActualizar);
        buttonActualizar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                cargarPedidos();
            }
        });
        listViewPedidos = (ListView)v.findViewById(R.id.lvPedidosRepartidor);
        cargarPedidos();
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public void cargarPedidos ()
    {
        try
        {
            progress = new ProgressDialog(getContext());
            progress.setMessage("Cargando pedidos pendientes...");
            progress.setCanceledOnTouchOutside(false);
            progress.show();

            tipoReg = "Pedidos";
            JSONObject object = new JSONObject();
            object.put("id", mParamId);
            new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/cargarPedidosFijosRepartidor.php", object.toString());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
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
    class CustomPedidos extends BaseAdapter
    {

        @Override
        public int getCount() {
            return pedidosRepartidorList.size();
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
            convertView = getActivity().getLayoutInflater().inflate(R.layout.custom_lista_repartidor, null);
            TextView textViewCalle = (TextView)convertView.findViewById(R.id.tvCalle);
            TextView textViewUsuario = (TextView)convertView.findViewById(R.id.tvUsuario);
            TextView textViewFecha = (TextView)convertView.findViewById(R.id.tvFecha);

            textViewCalle.setText(Html.fromHtml("<b>Calle:</b> " + pedidosRepartidorList.get(position).getpCalle().toString() + " #" + pedidosRepartidorList.get(position).getpNumero().toString()));
            textViewUsuario.setText(Html.fromHtml("<b>Nombre cliente:</b> " + pedidosRepartidorList.get(position).getuNombre().toString() + " " + pedidosRepartidorList.get(position).getuApellido().toString()));
            textViewFecha.setText(pedidosRepartidorList.get(position).getFecha_Pedido().toString());

            Button buttonComenzar = (Button)convertView.findViewById(R.id.btnComenzar);
            Button buttonEntregar = (Button)convertView.findViewById(R.id.btnEntregar);

            buttonComenzar.setId(position);
            buttonEntregar.setId(position);
            buttonComenzar.setText("Comenzar");
            buttonEntregar.setText("Entregar");

            if (pedidosRepartidorList.get(position).getEstado_Descripcion().equals("En Preparación"))
            {
                buttonComenzar.setEnabled(true);
                buttonEntregar.setTextColor(Color.LTGRAY);
                buttonEntregar.setEnabled(false);
            }
            else if (pedidosRepartidorList.get(position).getEstado_Descripcion().equals("En Reparto"))
            {
                buttonComenzar.setEnabled(false);
                buttonEntregar.setTextColor(Color.RED);
                buttonEntregar.setEnabled(true);
            }

            buttonComenzar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setTitle("Comenzar")
                            .setMessage("¿Comenzar a repartir al cliente " + pedidosRepartidorList.get(v.getId()).getuNombre().toString() + " " + pedidosRepartidorList.get(v.getId()).getuApellido().toString())
                            .setPositiveButton("Aceptar",
                                    new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {

                                            try
                                            {
                                                JSONObject jsonObject = new JSONObject();
                                                jsonObject.put("estado", "2");
                                                jsonObject.put("order_id", pedidosRepartidorList.get(v.getId()).getIdOrden());
                                                tipoReg = "Comenzar";
                                                new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/modificarEstadoPedido.php", jsonObject.toString());
                                            }
                                            catch (JSONException e)
                                            {
                                                e.printStackTrace();
                                            }

                                        }
                                    })
                            .setNegativeButton("Mas tarde",
                                    new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                    builder.show();

                }

            });
            buttonEntregar.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(final View v)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setTitle("Entregar")
                            .setMessage("¿Entregar el pedido al cliente " + pedidosRepartidorList.get(v.getId()).getuNombre().toString() + " " + pedidosRepartidorList.get(v.getId()).getuApellido().toString())
                            .setPositiveButton("Si",
                                    new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            try
                                            {
                                                JSONObject jsonObject = new JSONObject();
                                                jsonObject.put("estado", "3");
                                                jsonObject.put("order_id", pedidosRepartidorList.get(v.getId()).getIdOrden());
                                                tipoReg = "Terminar";
                                                new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/modificarEstadoPedido.php", jsonObject.toString());
                                            }
                                            catch (JSONException e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }
                                    })
                            .setNegativeButton("No",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            dialog.dismiss();
                                        }
                                    });
                    builder.show();
                }
            });
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
            if (tipoReg.equals("Pedidos"))
            {
                if (!s.equals("[]"))
                {
                    pedidosRepartidorList = new ArrayList<>();
                    try
                    {
                        JSONArray jsonArray = new JSONArray(s);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            PedidosRepartidor pedidosRepartidor = new PedidosRepartidor();
                            pedidosRepartidor.setIdOrden(jsonObject.getInt("id"));
                            pedidosRepartidor.setpCalle(jsonObject.getString("street"));
                            pedidosRepartidor.setpNumero(jsonObject.getString("number"));
                            pedidosRepartidor.setuNombre(jsonObject.getString("name"));
                            pedidosRepartidor.setuApellido(jsonObject.getString("lastname"));
                            pedidosRepartidor.setEstado_Descripcion(jsonObject.getString("description"));
                            pedidosRepartidor.setFecha_Pedido(jsonObject.getString("date"));
                            pedidosRepartidorList.add(pedidosRepartidor);
                        }
                        listViewPedidos.setAdapter(new CustomPedidos());
                        progress.dismiss();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    if (pedidosRepartidorList != null)
                    {
                        if (!pedidosRepartidorList.isEmpty())
                        {
                            pedidosRepartidorList.clear();
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, pedidosRepartidorList);
                        listViewPedidos.setAdapter(arrayAdapter);
                    }

                    Toast.makeText(getContext(), "Aun no se te han asignado pedidos por repartir", Toast.LENGTH_LONG).show();
                    progress.dismiss();
                }
            }
            else if (tipoReg.equals("Comenzar"))
            {
                pedidosRepartidorList.clear();
                Toast.makeText(getContext(), "Has comenzado a repartir...", Toast.LENGTH_SHORT).show();
                cargarPedidos();
            }
            else if (tipoReg.equals("Terminar"))
            {
                pedidosRepartidorList.clear();
                Toast.makeText(getContext(), "Has terminado el pedido.", Toast.LENGTH_SHORT).show();
                cargarPedidos();
            }
        }
    }
}
