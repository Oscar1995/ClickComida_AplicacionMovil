package com.chile.oscar.clickcomida_aplicacionmovil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.CarroBorrarLista;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Coordenadas;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Productos_Memory;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Tienda;

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

    String tipoReg = "";
    String numero = null;
    String calle = null;

    List<Tienda> tiendaList = new ArrayList<>();
    List<Productos_Memory> productosMemoryList = new ArrayList<>();
    List<Productos_Memory> productosMemoryListCache;
    List<List<Productos_Memory>> productosMemoryListCacheObject = new ArrayList<>();

    int[] isActivedArray;

    int totalArr = 0;

    List<Integer> integerListIdProducto = new ArrayList<>();
    List<Integer> integerListIdStore = new ArrayList<>();
    List<Integer> integerListCantidad= new ArrayList<>();
    List<Integer> integerListStore_id= new ArrayList<>();
    List<String> stringArrayListNombre = new ArrayList<>();
    List<Integer> integerListPrice= new ArrayList<>();
    List<Integer> integerListCantidadProdOrden = new ArrayList<>();
    List<Integer> integerListTotal = new ArrayList<>();

    int posTienda, idList;
    List<Integer> cantidadPorTienda = new ArrayList<>();

    View v;
    Boolean prodCart = false, isProdSelected = false;
    int posGlobalDelete;
    ProgressDialog progress;

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
        progress = new ProgressDialog(getContext());
        progress.setMessage("Cargando...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        cargarPreferencias();
        // Inflate the layout for this fragment
        /*tiendaList.clear();
        productosMemoryList.clear();
        integerListIdProducto.clear();
        integerListIdStore.clear();
        integerListCantidad.clear();
        integerListStore_id.clear();
        stringArrayListNombre.clear();
        integerListPrice.clear();
        integerListCantidadProdOrden.clear();
        jsonObjectList.clear();*/

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
    public void cargarDireccion()
    {

        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", Coordenadas.id);
            new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/consultarDireccionComprar.php", jsonObject.toString());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public void cargarPreferencias()
    {
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

                Productos_Memory productos_memory = new Productos_Memory();
                productos_memory.setId(idProd);
                productos_memory.setCantidad(idCantidad);
                productosMemoryList.add(productos_memory);

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
            //Toast.makeText(getContext(), "" + jsonObjectList.toString(), Toast.LENGTH_SHORT).show();
            tipoReg = "Carro";
            new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/cargarProductosCarro.php", jsonObjectList.toString());
            cargarDireccion();
        }
        else
        {
            Toast.makeText(getContext(), "Aun no tienes producto agregado a tu carrito de compras", Toast.LENGTH_SHORT).show();
            progress.dismiss();
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
                if (tipoReg.equals("Pedido"))
                {
                    JSONObject object = new JSONObject(s);
                    String res = object.getString("Correcto");

                    SharedPreferences sharedpreferences =  getActivity().getSharedPreferences("carro", Context.MODE_PRIVATE);
                    for (int i=0; i<productosMemoryListCacheObject.get(posTienda).size(); i++)
                    {
                        String formato = "prod_id_"+productosMemoryListCacheObject.get(posTienda).get(i).getId();
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.remove(formato);
                        editor.commit();
                    }
                    tipoReg = "Carro";
                    Toast.makeText(getContext(), "Los productos llegaran pronto a tu domicilio, consulta en la pestaña seguimiento para el donde viene tu pedido.", Toast.LENGTH_LONG).show();
                    Fragment currentFragment = new cart_products();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_general, currentFragment).commit();
                }
                else if (tipoReg.equals("Direccion"))
                {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        calle = object.getString("street");
                        numero = object.getString("number");
                    }
                }
                else if (tipoReg.equals("Carro"))
                {
                    tipoReg = "Direccion";
                    JSONArray jsonArray = new JSONArray(s);
                    int divArray = jsonArray.length() / 3;
                    int cArray = 0;
                    for (int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        if (cArray < divArray)
                        {
                            integerListStore_id.add(object.getInt("id"));
                            stringArrayListNombre.add(object.getString("name"));
                            integerListPrice.add(object.getInt("price"));
                            cArray++;
                        }
                        else
                        {
                            if (cArray >= (divArray * 2))
                            {
                                integerListIdProducto.add(object.getInt("id"));
                            }
                            else
                            {
                                Tienda tienda = new Tienda();
                                tienda.setId(object.getInt("id"));
                                tienda.setNombre(object.getString("name"));
                                if (!tiendaList.contains(tienda))
                                {
                                    tiendaList.add(tienda);
                                }
                                cArray++;
                            }

                        }

                    }
                    Button buttonAgregarProductos = null, buttonQuitarProductos = null, buttonFinalizarCompra = null;

                    if (prodCart)
                    {
                        Boolean aBooleanProd = true;

                        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        LinearLayout.LayoutParams paramsButtonHorizontal = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
                        LinearLayout linearLayoutContenedor = (LinearLayout)v.findViewById(R.id.llContenedor);

                        buttonAgregarProductos = new Button(getContext());
                        buttonAgregarProductos.setText(getResources().getString(R.string.Agregar_Productos));
                        buttonAgregarProductos.setBackground(getResources().getDrawable(R.drawable.colorbutton));
                        buttonAgregarProductos.setTextColor(getResources().getColor(R.color.textoBlanco));
                        buttonAgregarProductos.setLayoutParams(params);
                        buttonAgregarProductos.setId(00); //TODO: ID DE LA TIENDA


                        for (int a=0; a<integerListIdProducto.size(); a++)
                        {
                            for (int j=0; j<productosMemoryList.size(); j++)
                            {
                                if (integerListIdProducto.get(a) == productosMemoryList.get(j).getId())
                                {
                                    integerListCantidadProdOrden.add(productosMemoryList.get(j).getCantidad());
                                }
                            }
                        }

                        isActivedArray = new int[integerListIdStore.size()];
                        for (int i=0; i<integerListIdStore.size(); i++)
                        {
                            isActivedArray[i] = 0;
                            final int posActual = integerListIdStore.get(i);
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

                            final LinearLayout.LayoutParams paramsMATCH_PARENT = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                            linearLayoutProd.setOrientation(LinearLayout.VERTICAL);
                            horizontalText.setGravity(LinearLayout.VERTICAL);
                            horizontalBoton.setOrientation(LinearLayout.HORIZONTAL);
                            horizontalText.setLayoutParams(params);
                            horizontalBoton.setLayoutParams(paramsMATCH_PARENT);
                            linearLayoutProd.setLayoutParams(params);

                            String nombreTienda = null;
                            for (int j=0; j<tiendaList.size(); j++)
                            {
                                if (tiendaList.get(j).getId() == posActual)
                                {
                                    nombreTienda = tiendaList.get(j).getNombre();
                                }
                            }
                            //TODO: ESTO SE UNA VEZ POR TIENDA
                            //Nombre tienda
                            textViewNombreTienda.setId(i);
                            textViewNombreTienda.setText("Tienda " + nombreTienda);
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

                            int acFila = 100;
                            int cFila = 0;
                            final List<Integer> posProd = new ArrayList<>();
                            final List<String> prodCar = new ArrayList<>();

                            for (int k=0; k<integerListStore_id.size(); k++)
                            {
                                if (integerListStore_id.get(k) == posActual)
                                {
                                    posProd.add(k);
                                    cFila++;
                                    if (cFila >= 2)
                                    {
                                        acFila +=100;
                                    }
                                }
                            }
                            for (int j=0; j<posProd.size(); j++)
                            {
                                prodCar.add(stringArrayListNombre.get(j));
                            }

                            //TODO: AQUI SE IMPRIMIRA TODOS LOS PRODUCTOS DE LA TIENDA
                            paramsMATCH_PARENT.height = acFila;
                            final ListView listViewProductos = new ListView(getContext());
                            listViewProductos.setId(i);
                            listViewProductos.setLayoutParams(paramsMATCH_PARENT);
                            productosMemoryListCache = new ArrayList<>();
                            for (int j=0; j<posProd.size(); j++)
                            {
                                Productos_Memory productos_memory = new Productos_Memory();
                                productos_memory.setId(integerListIdProducto.get(posProd.get(j)));
                                productos_memory.setCantidad(integerListCantidadProdOrden.get(posProd.get(j)));
                                productosMemoryListCache.add(productos_memory);
                            }
                            productosMemoryListCacheObject.add(productosMemoryListCache);

                            BaseAdapter baseAdapter = new BaseAdapter()
                            {

                                @Override
                                public int getCount()
                                {
                                    //Guarda la cantidad del producto por orden
                                    return prodCar.size();
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

                                    textViewNombre.setText(stringArrayListNombre.get(posProd.get(position)));
                                    textViewPrecio.setText(integerListPrice.get(posProd.get(position)) + "");

                                    textViewCantidad.setText(integerListCantidadProdOrden.get(posProd.get(position)) + "");

                                    int total = (integerListPrice.get(posProd.get(position)) * integerListCantidadProdOrden.get(posProd.get(position)));
                                    textViewTotal.setText(total + "");

                                    ///textViewCantidad.setText(pedidosList.get(position).getCantidadProducto() + "");
                                    // textViewTotal.setText(pedidosList.get(position).getTotalProducto() + "");
                                    return convertView;
                                }
                            };

                            ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, prodCar);
                            listViewProductos.setAdapter(baseAdapter);
                            listViewProductos.setSelector(getResources().getDrawable(R.drawable.colorbuttonamarillo));
                            //TODO: TERMINANDO HASTA AQUI

                            int totalFinal = 0;
                            for (int j =0; j<posProd.size(); j++)
                            {
                                totalFinal += (integerListPrice.get(posProd.get(j)) * integerListCantidadProdOrden.get(posProd.get(j)));
                            }
                            integerListTotal.add(totalFinal);


                            TextView textViewTotal = new TextView(getContext());
                            textViewTotal.setId(i);
                            textViewTotal.setText("Total: $" + totalFinal);
                            textViewTotal.setLayoutParams(paramsMATCH_PARENT);
                            textViewTotal.setGravity(Gravity.CENTER);
                            textViewTotal.setTextColor(getResources().getColor(R.color.colorNegro));
                            textViewTotal.setTypeface(Typeface.DEFAULT_BOLD);

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
                            linearLayoutProd.addView(textViewTotal);
                            linearLayoutProd.addView(horizontalBoton);
                            //Este siempre debe ir abajo
                            linearLayoutContenedor.addView(linearLayoutProd);

                            buttonQuitarProductos.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    if (isActivedArray[v.getId()] == 1)
                                    {
                                        List<Productos_Memory> productos_memories = (productosMemoryListCacheObject.get(v.getId()));
                                        //int store_id = integerListIdStore.get(v.getId());
                                        int id = productos_memories.get(posGlobalDelete).getId();
                                        //int cantidad = productos_memories.get(posGlobalDelete).getCantidad();

                                        //String formato = id + "-" + store_id + "-" + cantidad; //id_product + "-" + store_id + "-" + sumTotal;
                                        String formato = "prod_id_"+id;
                                        SharedPreferences sharedpreferences =  getActivity().getSharedPreferences("carro", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.remove(formato);
                                        editor.commit();

                                        tipoReg = "Carro";
                                        Toast.makeText(getContext(), "Producto quitado", Toast.LENGTH_SHORT).show();
                                        Fragment currentFragment = new cart_products();
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_general, currentFragment).commit();
                                    }
                                    else
                                    {
                                        Toast.makeText(getContext(), "Debe seleccionar un producto para quitarlo del carro", Toast.LENGTH_SHORT).show();
                                    }
                                    /*if (isProdSelected)
                                    {

                                    }
                                    else
                                    {

                                    }*/
                                }
                            });
                            buttonFinalizarCompra.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(final View v)
                                {
                                    Boolean aBooleanDir = false;
                                    if (numero != null && calle != null) aBooleanDir = true;

                                    final int total = integerListTotal.get(v.getId());
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    if (aBooleanDir)
                                    {
                                        final EditText editTextCommentary = new EditText(getContext());
                                        editTextCommentary.setHint("Escriba su comentario (Opcional)");
                                        builder.setTitle("Confirmación de la compra")
                                                .setMessage(Html.fromHtml("El monto total es de $" + total + " pesos <br>" +
                                                        "<b>Tu direccion es: </b> " + calle +" #" + numero + " </b>" +
                                                        "si esta es tu direccion pincha en <font color=\"blue\">ACEPTAR</font> de lo contrario <font color=\"red\">CANCELAR</font>."))
                                                .setPositiveButton("Aceptar",
                                                        new DialogInterface.OnClickListener()
                                                        {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which)
                                                            {
                                                                JSONObject object = new JSONObject();
                                                                try
                                                                {
                                                                    List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
                                                                    int cantidadTotal = productosMemoryListCacheObject.get(v.getId()).size();
                                                                    object.put("total_price", total);
                                                                    object.put("user_id", Coordenadas.id);
                                                                    object.put("store_id", integerListIdStore.get(v.getId()));
                                                                    object.put("cantidad_total", cantidadTotal);
                                                                    object.put("calle", calle);
                                                                    object.put("numero", numero);
                                                                    if (editTextCommentary.getText().toString().trim().isEmpty())
                                                                    {
                                                                        object.put("commentary", null);
                                                                    }
                                                                    else
                                                                    {
                                                                        object.put("commentary", editTextCommentary.getText().toString().trim());
                                                                    }

                                                                    for (int i=0; i<productosMemoryListCacheObject.get(v.getId()).size(); i++)
                                                                    {
                                                                        JSONObject objectProductos = new JSONObject();
                                                                        objectProductos.put("id", productosMemoryListCacheObject.get(v.getId()).get(i).getId());
                                                                        objectProductos.put("cantidad", productosMemoryListCacheObject.get(v.getId()).get(i).getCantidad());
                                                                        jsonObjects.add(objectProductos);
                                                                    }
                                                                    object.put("lista", jsonObjects.toString());
                                                                    tipoReg = "Pedido";
                                                                    posTienda = v.getId();
                                                                    new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/ordenarProductos.php", object.toString());
                                                                }
                                                                catch (JSONException e)
                                                                {
                                                                    e.printStackTrace();
                                                                }

                                                            }
                                                        })
                                                .setNegativeButton("Cancelar",
                                                        new DialogInterface.OnClickListener()
                                                        {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which)
                                                            {
                                                                dialog.dismiss();
                                                            }
                                                        });
                                        builder.setView(editTextCommentary);
                                        builder.show();
                                    }
                                    else
                                    {
                                        Snackbar.make(getView(), "Aun no tienes una direccion definida, anda a tus datos y selecciona una por defecto", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    }
                                }
                            });

                            listViewProductos.setOnItemClickListener(new AdapterView.OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                                {
                                    isActivedArray[parent.getId()] = 1;
                                    isProdSelected = true;
                                    idList = view.getId();
                                    posGlobalDelete = position;
                                }
                            });
                        }
                        buttonAgregarProductos.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                FragmentTransaction trans = getFragmentManager().beginTransaction();
                                trans.replace(R.id.content_general, new BusquedaAvanzada());
                                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                trans.addToBackStack(null);
                                trans.commit();
                            }
                        });
                    }
                    progress.dismiss();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
