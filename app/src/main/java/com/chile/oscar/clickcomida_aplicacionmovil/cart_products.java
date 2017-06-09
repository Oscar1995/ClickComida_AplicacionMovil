package com.chile.oscar.clickcomida_aplicacionmovil;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Carrito;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Codificacion;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Pedidos;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Productos_Carro;

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
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link cart_products.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link cart_products#newInstance} factory method to
 * create an instance of this fragment.
 */
public class cart_products extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    List<Carrito> carritoList = new ArrayList<>();
    List<Pedidos> pedidosList = new ArrayList<>();


    List<Integer> integerListIdStore = new ArrayList<>();
    List<Integer> integerListCantidad= new ArrayList<>();

    List<Integer> integerListStore_id= new ArrayList<>();
    List<String> stringArrayListNombre = new ArrayList<>();
    List<Integer> integerListPrice= new ArrayList<>();
    View v;

    Boolean prodCart = false;

    List<JSONObject> jsonObjectList;

    private OnFragmentInteractionListener mListener;

    public cart_products()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment cart_products.
     */
    // TODO: Rename and change types and number of parameters
    public static cart_products newInstance(String param1, String param2) {
        cart_products fragment = new cart_products();
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
        cargarPreferencias();
        new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/cargarProductosCarro.php", jsonObjectList.toString());
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_cart_products, container, false);


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
    class carroAdapter extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return 0;
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
            convertView = getActivity().getLayoutInflater().inflate(R.layout.custom_cart, null);
            TextView textViewNombre = (TextView) convertView.findViewById(R.id.tvNombreProdCarro);
            TextView textViewPrecio = (TextView) convertView.findViewById(R.id.tvPrecioProdCarro);
            TextView textViewCantidad = (TextView) convertView.findViewById(R.id.tvCantidadProdCarro);
            TextView textViewTotal = (TextView) convertView.findViewById(R.id.tvTotalProdCarro);

            /*for (int i=0; i<posList.size(); i++)
            {
                int posActual = posList.get(i);
                textViewNombre.setText(stringArrayListNombre.get(posActual));
                textViewPrecio.setText(integerListPrice.get(posActual) + "");
            }*/


            ///textViewCantidad.setText(pedidosList.get(position).getCantidadProducto() + "");
           // textViewTotal.setText(pedidosList.get(position).getTotalProducto() + "");


            return convertView;
        }
    }
    public void cargarPreferencias()
    {
        carritoList.clear();
        integerListIdStore.clear();
        integerListCantidad.clear();

        integerListStore_id.clear();
        stringArrayListNombre.clear();
        integerListPrice.clear();

        jsonObjectList = new ArrayList<>();
        integerListIdStore = new ArrayList<>();
        integerListCantidad = new ArrayList<>();

        integerListStore_id = new ArrayList<>();
        stringArrayListNombre = new ArrayList<>();
        integerListPrice = new ArrayList<>();

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("carro", Context.MODE_PRIVATE);
        int canTotal = sharedpreferences.getAll().size();
        if (canTotal != 0)
        {
            prodCart = true;
            Map splitProd = sharedpreferences.getAll();
            Object[] x = splitProd.values().toArray();
            for (int i=0; i<sharedpreferences.getAll().size(); i++)
            {
                String[] vData =  x[i].toString().split("-");

                int idProd = Integer.parseInt(vData[0]);
                int idStore  = Integer.parseInt(vData[1]);
                int idCantidad = Integer.parseInt(vData[2]);

                if (!integerListIdStore.contains(idStore))integerListIdStore.add(idStore);


                try
                {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", idProd);
                    jsonObjectList.add(jsonObject);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            Toast.makeText(getContext(), "" + jsonObjectList.toString(), Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getContext(), "Aun no tienes producto agregado a tu carrito de compras", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), "Intentalo de nuevo.", Toast.LENGTH_SHORT).show();
                        //ex.printStackTrace();
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
                for (int i=0; i<jsonArray.length(); i++)
                {
                    JSONObject object = jsonArray.getJSONObject(i);
                    integerListStore_id.add(object.getInt("id"));
                    stringArrayListNombre.add(object.getString("name"));
                    integerListPrice.add(object.getInt("price"));
                }
                Button buttonAgregarProductos = null, buttonQuitarProductos = null, buttonFinalizarCompra = null;

                if (prodCart)
                {
                    Boolean aBooleanProd = true;
                    final LinearLayout.LayoutParams paramsMATCH_PARENT = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    LinearLayout.LayoutParams paramsButtonHorizontal = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
                    LinearLayout linearLayoutContenedor = (LinearLayout)v.findViewById(R.id.llContenedor);

                    buttonAgregarProductos = new Button(getContext());
                    buttonAgregarProductos.setText(getResources().getString(R.string.Agregar_Productos));
                    buttonAgregarProductos.setBackground(getResources().getDrawable(R.drawable.colorbutton));
                    buttonAgregarProductos.setTextColor(getResources().getColor(R.color.textoBlanco));
                    buttonAgregarProductos.setLayoutParams(params);
                    buttonAgregarProductos.setId(00); //TODO: ID DE LA TIENDA

                    for (int i=0; i<integerListIdStore.size(); i++)
                    {
                        int posActual = integerListIdStore.get(i);
                        LinearLayout linearLayoutProd = new LinearLayout(getContext());
                        TextView textViewNombreTienda = new TextView(getContext());

                        TextView textViewNombreProducto = new TextView(getContext());
                        TextView textViewNombrePrecio = new TextView(getContext());
                        TextView textViewNombreCantidad = new TextView(getContext());
                        TextView textViewNombreTotal = new TextView(getContext());

                        buttonQuitarProductos = new Button(getContext());
                        buttonFinalizarCompra = new Button(getContext());

                        LinearLayout horizontalText = new LinearLayout(getContext());
                        LinearLayout horizontalBoton = new LinearLayout(getContext());


                        linearLayoutProd.setOrientation(LinearLayout.VERTICAL);
                        horizontalText.setGravity(LinearLayout.VERTICAL);
                        horizontalBoton.setOrientation(LinearLayout.HORIZONTAL);
                        horizontalText.setLayoutParams(params);
                        horizontalBoton.setLayoutParams(paramsMATCH_PARENT);

                        linearLayoutProd.setLayoutParams(params);

                        //TODO: ESTO SE UNA VEZ POR TIENDA
                        //Nombre tienda
                        textViewNombreTienda.setId(i);
                        textViewNombreTienda.setText("Tienda " + i);
                        textViewNombreTienda.setLayoutParams(params);
                        textViewNombreTienda.setGravity(Gravity.CENTER);
                        textViewNombreTienda.setTextColor(getResources().getColor(R.color.colorNegro));
                        textViewNombreTienda.setTypeface(Typeface.DEFAULT_BOLD);

                        //Encabezado,
                        textViewNombreProducto.setText("Nombre");
                        textViewNombreProducto.setLayoutParams(paramsButtonHorizontal);
                        textViewNombreProducto.setGravity(Gravity.CENTER);
                        textViewNombreProducto.setTextColor(getResources().getColor(R.color.colorNegro));
                        textViewNombreProducto.setTypeface(Typeface.DEFAULT_BOLD);

                        textViewNombrePrecio.setText("$");
                        textViewNombrePrecio.setLayoutParams(paramsButtonHorizontal);
                        textViewNombrePrecio.setGravity(Gravity.CENTER);
                        textViewNombrePrecio.setTextColor(getResources().getColor(R.color.colorNegro));
                        textViewNombrePrecio.setTypeface(Typeface.DEFAULT_BOLD);

                        textViewNombreCantidad.setText("Cantidad");
                        textViewNombreCantidad.setLayoutParams(paramsButtonHorizontal);
                        textViewNombreCantidad.setGravity(Gravity.CENTER);
                        textViewNombreCantidad.setTextColor(getResources().getColor(R.color.colorNegro));
                        textViewNombreCantidad.setTypeface(Typeface.DEFAULT_BOLD);

                        textViewNombreTotal.setText("Total");
                        textViewNombreTotal.setLayoutParams(paramsButtonHorizontal);
                        textViewNombreTotal.setGravity(Gravity.CENTER);
                        textViewNombreTotal.setTextColor(getResources().getColor(R.color.colorNegro));
                        textViewNombreTotal.setTypeface(Typeface.DEFAULT_BOLD);

                        horizontalText.addView(textViewNombreProducto);
                        horizontalText.addView(textViewNombrePrecio);
                        horizontalText.addView(textViewNombreCantidad);
                        horizontalText.addView(textViewNombreTotal);

                        final List<Integer> posProd = new ArrayList<>();
                        List<String> prodCar = new ArrayList<>();
                        prodCar.clear();

                        for (int k=0; k<integerListStore_id.size(); k++)
                        {
                            if (integerListStore_id.get(k) == posActual)
                            {
                                posProd.add(k);
                            }
                        }
                        for (int j=0; j<posProd.size(); j++)
                        {
                            prodCar.add(stringArrayListNombre.get(posProd.get(j)));;
                        }


                        //TODO: AQUI SE IMPRIMIRA TODOS LOS PRODUCTOS DE LA TIENDA
                        ListView listViewProductos = new ListView(getContext());
                        listViewProductos.setId(i);
                        listViewProductos.setLayoutParams(paramsMATCH_PARENT);
                        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, prodCar);
                        listViewProductos.setAdapter(arrayAdapter);
                        //TODO: TERMINANDO HASTA AQUI

                        buttonFinalizarCompra.setText(getResources().getString(R.string.Finalizar));
                        buttonFinalizarCompra.setBackground(getResources().getDrawable(R.drawable.colorbuttonceleste));
                        buttonFinalizarCompra.setTextColor(getResources().getColor(R.color.textoBlanco));
                        buttonFinalizarCompra.setLayoutParams(paramsButtonHorizontal);
                        buttonFinalizarCompra.setId(i); //TODO: ID DE LA TIENDA

                        buttonQuitarProductos.setText(getResources().getString(R.string.Quitar));
                        buttonQuitarProductos.setBackground(getResources().getDrawable(R.drawable.colorbuttonred));
                        buttonQuitarProductos.setTextColor(getResources().getColor(R.color.textoBlanco));
                        buttonQuitarProductos.setLayoutParams(paramsButtonHorizontal);
                        buttonQuitarProductos.setId(i); //TODO: ID DE LA TIENDA

                        //ESTE AGREGA LOS BOTONES DE FORMA HORIZONTAL EN UN LAYOUT
                        horizontalBoton.addView(buttonFinalizarCompra);
                        horizontalBoton.addView(buttonQuitarProductos);

                        if (aBooleanProd)linearLayoutProd.addView(buttonAgregarProductos); aBooleanProd = false;
                        linearLayoutProd.addView(textViewNombreTienda);
                        linearLayoutProd.addView(horizontalText);
                        linearLayoutProd.addView(listViewProductos);
                        linearLayoutProd.addView(horizontalBoton);
                        //Este siempre debe ir abajo
                        linearLayoutContenedor.addView(linearLayoutProd);


                        buttonQuitarProductos.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                Toast.makeText(getContext(), "Tienda: " +v.getId(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        buttonFinalizarCompra.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(), "Tienda: " +v.getId(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    buttonAgregarProductos.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Toast.makeText(getContext(), "Tienda: " +v.getId(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
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
}
