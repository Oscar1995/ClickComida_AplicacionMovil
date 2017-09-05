package com.chile.oscar.clickcomida_aplicacionmovil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.NumberPicker;
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
    TextView textViewCuentaProd;

    List<String> idProd;
    List<String> nombreProd, desProd, precioProd;
    List<Bitmap> imagesProd;
    int posProd;
    boolean isProdSelected = false;
    boolean isProdOne = false;

    int posProdOne;

    ProgressDialog progress;


    private OnFragmentInteractionListener mListener;

    public ProductsOtherUser()
    {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Details_products newInstance(Bitmap imagenProd, String nombreTienda, String store_id, String product_id, String nomProd,  String prodDes, String precioProd)
    {
        Details_products fragment = new Details_products();
        Bundle args = new Bundle();
        args.putString("imagen_prod", Codificacion.encodeToBase64(imagenProd, Bitmap.CompressFormat.PNG, 100));
        args.putString("nombre_tienda", nombreTienda);
        args.putString("store_id", store_id);
        args.putString("product_id", product_id);
        args.putString("nombre_prod", nomProd);
        args.putString("des_prod", prodDes);
        args.putString("precio_prod", precioProd);
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

        isProdOne = false;
        View v = inflater.inflate(R.layout.fragment_products_other_user, container, false);
        autoCompleteTextViewProducto = (AutoCompleteTextView)v.findViewById(R.id.actvProductos);
        spinnerFiltro = (Spinner)v.findViewById(R.id.sFiltro);
        gridViewProductos = (GridView)v.findViewById(R.id.gvProductos);
        textViewCuentaProd = (TextView)v.findViewById(R.id.tvCount);

        ImageView imageViewTienda = (ImageView)v.findViewById(R.id.ivTiendaOther);
        TextView textViewTituloTienda = (TextView)v.findViewById(R.id.tvTituloTienda);
        TextView textViewNombreTienda = (TextView)v.findViewById(R.id.tvTienda);
        Button buttonAgregarCarro = (Button)v.findViewById(R.id.btnAgregarCarro);
        Button buttonDetalles = (Button)v.findViewById(R.id.btnDetalles);
        Button buttonBuscarProducto = (Button)v.findViewById(R.id.btnBuscarProd);;

        String[] tOption = {"Nombre", "Menor precio", "Mayor precio"};
        ArrayAdapter arrayAdapterFiltro = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, tOption);
        spinnerFiltro.setAdapter(arrayAdapterFiltro);

        textViewNombreTienda.setText(Html.fromHtml("<b>" + getResources().getString(R.string.Tienda) + ": </b>" + nombreTienda));
        imageViewTienda.setImageDrawable(new MetodosCreados().EncuadrarBitmap(imagenTienda, getResources()));
        textViewTituloTienda.setText(getResources().getString(R.string.titulo_productos_tienda) + " " + nombreTienda);


        buttonBuscarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (nombreProd.contains(autoCompleteTextViewProducto.getText().toString()))
                {
                    posProdOne = nombreProd.indexOf(autoCompleteTextViewProducto.getText().toString());

                    BaseAdapter baseAdapter = new BaseAdapter() {
                        @Override
                        public int getCount()
                        {
                            return 1;
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

                            imageViewProd.setImageDrawable(new MetodosCreados().RedondearBitmap(imagesProd.get(posProdOne), getResources()));
                            textViewNombre.setText(nombreProd.get(posProdOne));
                            textViewPrecio.setText("$" + precioProd.get(posProdOne));

                            return convertView;
                        }
                    };
                    isProdOne = true;
                    gridViewProductos.setAdapter(baseAdapter);
                }
                else
                {
                    isProdOne = false;
                    Toast.makeText(getContext(), "El producto \"" + autoCompleteTextViewProducto.getText().toString() + "\" no existe.", Toast.LENGTH_SHORT).show();
                }
                isProdSelected = false;
            }
        });

        gridViewProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                posProd = position;
                isProdSelected = true;
            }
        });

        buttonAgregarCarro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (isProdSelected)
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    final NumberPicker numberPicker = new NumberPicker(getContext());
                    alert.setMessage("Elige una cantidad...");
                    if (isProdOne)
                    {
                        alert.setTitle(nombreProd.get(posProdOne));
                    }
                    else
                    {
                        alert.setTitle(nombreProd.get(posProd));
                    }

                    numberPicker.setMinValue(1);
                    numberPicker.setMaxValue(20);
                    alert.setView(numberPicker);

                    alert.setPositiveButton("Agregar al carro", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int whichButton)
                        {
                            if (isProdOne)
                            {
                                guardarPreferencias(Integer.parseInt(idProd.get(posProdOne)), numberPicker.getValue(), Integer.parseInt(store_id));
                            }
                            else
                            {
                                guardarPreferencias(Integer.parseInt(idProd.get(posProd)), numberPicker.getValue(), Integer.parseInt(store_id));
                            }
                        }
                    });

                    alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int whichButton)
                        {
                            dialog.dismiss();
                        }
                    });

                    alert.show();
                }
                else
                {
                    Toast.makeText(getContext(), "Debes seleccionar un producto...", Toast.LENGTH_SHORT).show();
                }

            }
        });
        buttonDetalles.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isProdSelected)
                {
                    FragmentTransaction trans = getFragmentManager().beginTransaction();
                    if (isProdOne)
                    {
                        trans.replace(R.id.content_general, newInstance(imagesProd.get(posProdOne), nombreTienda, store_id, idProd.get(posProdOne), nombreProd.get(posProdOne), desProd.get(posProdOne), precioProd.get(posProdOne)));
                    }
                    else
                    {

                        trans.replace(R.id.content_general, newInstance(imagesProd.get(posProd), nombreTienda, store_id, idProd.get(posProd), nombreProd.get(posProd), desProd.get(posProd), precioProd.get(posProd)));

                    }
                    trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    trans.addToBackStack(null);
                    trans.commit();

                }
                else
                {
                    Toast.makeText(getContext(), "Debes seleccionar un producto...", Toast.LENGTH_SHORT).show();
                }

            }
        });
        spinnerFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                progress = new ProgressDialog(getContext());

                if (position == 0)
                {
                    cargarProductos("Nombre");
                    progress.setMessage("Cargando productos...");
                }
                else if (position == 1)
                {
                    cargarProductos("Menor");
                    progress.setMessage("Cargando productos por el menor precio...");
                }
                else if (position == 2)
                {
                    cargarProductos("Mayor");
                    progress.setMessage("Cargando productos por el mayor precio...");
                }
                progress.setCanceledOnTouchOutside(false);
                progress.show();
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
    public void guardarPreferencias(int id_product, int cantidad, int store_id)
    {
        SharedPreferences sharedpreferences =  getActivity().getSharedPreferences("carro", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = null;

        //int cantTotal = sharedpreferences.getAll().size();
        String xProd = (String) sharedpreferences.getAll().get("prod_id_"+id_product+"_storeid_"+store_id);
        if (xProd != null) //Existe el producto, es decir suma la cantidad que existe
        {
            String[] splitProd = xProd.split("-");
            int idProd = Integer.parseInt(splitProd[0]);
            int idCantidad = Integer.parseInt(splitProd[1]);
            if (id_product == idProd)
            {
                editor = sharedpreferences.edit();
                int sumTotal = idCantidad + cantidad;
                String modProd = id_product + "-" + store_id + "-" + sumTotal;
                editor.putString("prod_id_"+id_product, modProd);
                editor.commit();
            }
        }
        else
        {
            editor = sharedpreferences.edit();
            String modProd = id_product + "-" + store_id + "-" + cantidad;
            editor.putString("prod_id_"+id_product, modProd);
            editor.commit();
        }

        Toast.makeText(getContext(), "Has agregado este producto a tu carrito de compras.", Toast.LENGTH_SHORT).show();
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
    public void cargarProductos (String tipo)
    {
        JSONObject object = new JSONObject();
        try
        {
            object.put("store_id", store_id);
            object.put("tipo", tipo);
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

                idProd = new ArrayList<>();
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
                        idProd.add(jsonObject.getString("id"));
                        nombreProd.add(jsonObject.getString("name"));
                        desProd.add(jsonObject.getString("description"));
                        precioProd.add(jsonObject.getString("price"));
                    }
                }
                ArrayAdapter arrayAdapterTexto = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, nombreProd);
                if (cLocal == 1)
                {
                    textViewCuentaProd.setText(Html.fromHtml(getResources().getString(R.string.Esta_Tienda_Cuenta) + " " + cLocal + " producto."));
                }
                else if (cLocal > 1)
                {
                    textViewCuentaProd.setText(Html.fromHtml(getResources().getString(R.string.Esta_Tienda_Cuenta) + " " + cLocal + " productos."));
                }
                else
                {
                    textViewCuentaProd.setText(Html.fromHtml(getResources().getString(R.string.Esta_Tienda_No_Tiene_Productos)));
                }

                autoCompleteTextViewProducto.setAdapter(arrayAdapterTexto);
                progress.dismiss();
                gridViewProductos.setAdapter(new ProductoAdapter());
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
