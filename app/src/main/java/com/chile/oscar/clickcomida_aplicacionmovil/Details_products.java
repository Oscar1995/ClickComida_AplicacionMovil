package com.chile.oscar.clickcomida_aplicacionmovil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Carrito;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Codificacion;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Comentarios;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Coordenadas;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.MetodosCreados;
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
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;


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

    Boolean prodFav = false, prodCal = false, sw = true, loadCommentary;
    RatingBar ratingBarGlobal, ratingBarUser;
    Bitmap imagenProducto;
    ImageView imageViewFavProd;
    String store_id, product_id, nombreProd, nombreTienda, desProd, precioProd, tipoReg;
    Float calLocal;
    ListView listViewComentariosProd;
    EditText editTextComentario;
    List<Comentarios> comentariosProd;
    TextView textViewLikeProd;
    String nameProd = "e producto";
    int likeGlobal = 0;

    ProgressDialog progress;

    private OnFragmentInteractionListener mListener;

    public Details_products()
    {
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
            nombreTienda = getArguments().getString("nombre_tienda");
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
        TextView textViewTituloTienda = (TextView)v.findViewById(R.id.txtTituloTienda);
        textViewLikeProd = (TextView)v.findViewById(R.id.tvLikeProducts);
        editTextComentario = (EditText)v.findViewById(R.id.etComentarioOtherProd);
        final NumberPicker numberPickerCantidad = (NumberPicker)v.findViewById(R.id.numberPickerCantidad);
        numberPickerCantidad.setMinValue(1);
        numberPickerCantidad.setMaxValue(20);

        listViewComentariosProd  = (ListView)v.findViewById(R.id.lvComentariosProd);

        Button buttonCarro = (Button)v.findViewById(R.id.btnAgregarProdCarro);
        Button buttonAgregarComentario = (Button)v.findViewById(R.id.btnComentarProducto);

        imageViewProd.setImageDrawable(new MetodosCreados().RedondearBitmap(imagenProducto, getResources()));
        textViewNombre.setText(nombreProd);
        textViewDescripcion.setText(desProd);
        textViewTituloTienda.setText("Producto de la tienda " + nombreTienda);
        textViewPrecio.setText("$" + precioProd);

        cargarFavorito();

        buttonCarro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                guardarPreferencias(Integer.parseInt(product_id), numberPickerCantidad.getValue(), Integer.parseInt(store_id));
            }
        });
        buttonAgregarComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try
                {
                    if (!editTextComentario.getText().toString().isEmpty())
                    {
                        progress = new ProgressDialog(getContext());
                        progress.setMessage("Agregando comentario...");
                        progress.setCanceledOnTouchOutside(false);
                        progress.show();

                        JSONObject object = new JSONObject();
                        object.put("Comentario", editTextComentario.getText().toString());
                        object.put("user_id", Coordenadas.id);
                        object.put("product_id", product_id);

                        tipoReg = "Agregar comentario";
                        loadCommentary = true;
                        new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/insertarComentarioProducto.php", object.toString());
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Debes escribir para comentar", Toast.LENGTH_SHORT).show();
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });
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
                        progress = new ProgressDialog(getContext());
                        progress.setMessage("Eliminado " + nombreProd + " de tus favoritos");
                        progress.setCanceledOnTouchOutside(false);
                        progress.show();

                        tipoReg = "Eliminar favoritos";
                        new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/eliminarFavoritoProducto.php", object.toString());
                    }
                    else
                    {
                        progress = new ProgressDialog(getContext());
                        progress.setMessage("Agregando  " + nombreProd + " a tus favoritos");
                        progress.setCanceledOnTouchOutside(false);
                        progress.show();

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
        ratingBarUser.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
            {
                JSONObject object = new JSONObject();
                try
                {
                    if (sw == false)
                    {
                        if (prodCal == false) //Insertar
                        {
                            progress = new ProgressDialog(getContext());
                            progress.setMessage("Calificando " + nombreProd + "...");
                            progress.setCanceledOnTouchOutside(false);
                            progress.show();

                            object.put("Valor", rating);
                            object.put("user_id", Coordenadas.id);
                            object.put("product_id", product_id);

                            tipoReg = "Insertar calificacion";
                            new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/insertarCalificacion_producto.php", object.toString());
                        }
                        else //Actualizar
                        {
                            progress = new ProgressDialog(getContext());
                            progress.setMessage("Modificando calificacion...");
                            progress.setCanceledOnTouchOutside(false);
                            progress.show();

                            object.put("Valor", rating);
                            object.put("user_id", Coordenadas.id);
                            object.put("product_id", product_id);

                            calLocal = rating;

                            tipoReg = "Modificar calificacion";
                            new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/modificarCalificacionProducto.php", object.toString());
                        }
                    }
                    sw = false;

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

    private class AdapterCommentProducts extends BaseAdapter
    {

        @Override
        public int getCount() {
            return comentariosProd.size();
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
            convertView = getActivity().getLayoutInflater().inflate(R.layout.custom_lista_comentarios, null);
            TextView textViewNickname = (TextView)convertView.findViewById(R.id.tvNickname);
            TextView textViewFecha = (TextView)convertView.findViewById(R.id.tvFechaCreacion);
            TextView textViewComentario = (TextView)convertView.findViewById(R.id.tvComentario);

            textViewNickname.setText(comentariosProd.get(position).getuNickname());
            textViewFecha.setText(comentariosProd.get(position).getFechaCreacion());
            textViewComentario.setText(comentariosProd.get(position).getuComentario());

            return convertView;
        }
    }

    public void cargarFavorito ()
    {
        try
        {
            progress = new ProgressDialog(getContext());
            progress.setMessage("Cargando...");
            progress.setCanceledOnTouchOutside(false);
            progress.show();

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
    public void cargarCalificacion()
    {
        try
        {
            progress = new ProgressDialog(getContext());
            progress.setMessage("Cargando calificación...");
            progress.setCanceledOnTouchOutside(false);
            progress.show();

            JSONObject object = new JSONObject();
            object.put("user_id", Coordenadas.id);
            object.put("product_id", product_id);
            tipoReg = "Cargar calificacion";
            new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/cargarCalificacionProducto.php", object.toString());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
    public void cargarComentarios ()
    {
        try
        {
            progress = new ProgressDialog(getContext());
            progress.setMessage("Cargando comentarios...");
            progress.setCanceledOnTouchOutside(false);
            progress.show();

            JSONObject object = new JSONObject();
            object.put("product_id", product_id);
            tipoReg = "Cargar comentarios";
            new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/listaComentariosProducto.php", object.toString());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
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
            //Cargar favoritos - cargar calificacion - cargar comentarios -
            try
            {
                if (tipoReg.equals("Insertar favoritos"))
                {
                    JSONObject object = new JSONObject(s);
                    String res = object.getString("Insertado");
                    if (res.equals("Si"))
                    {
                        prodFav = true;
                        imageViewFavProd.setImageResource(R.drawable.heart_active);
                        progress.dismiss();
                        Toast.makeText(getContext(), "Este producto ha sido agregado a tus favoritos", Toast.LENGTH_SHORT).show();

                        likeGlobal = likeGlobal + 1;
                        if (likeGlobal == 0)
                        {
                            textViewLikeProd.setText(getResources().getString(R.string.none_person));
                        }
                        else if (likeGlobal == 1)
                        {
                            textViewLikeProd.setText(likeGlobal + " " + getResources().getString(R.string.none_person_one) + nameProd);
                        }
                        else
                        {
                            textViewLikeProd.setText(likeGlobal + " " + getResources().getString(R.string.none_person_more) + nameProd);
                        }

                    }
                }
                else if (tipoReg.equals("Eliminar favoritos"))
                {
                    JSONObject object = new JSONObject(s);
                    String res = object.getString("Eliminado");
                    if (res.equals("Si"))
                    {
                        prodFav = false;
                        imageViewFavProd.setImageResource(R.drawable.heart_desactive);
                        progress.dismiss();
                        Toast.makeText(getContext(), "El producto ya no pertenece a tus favoritos", Toast.LENGTH_SHORT).show();
                        likeGlobal = likeGlobal - 1;
                        if (likeGlobal == 0)
                        {
                            textViewLikeProd.setText(getResources().getString(R.string.none_person));
                        }
                        else if (likeGlobal == 1)
                        {
                            textViewLikeProd.setText(likeGlobal + " " + getResources().getString(R.string.none_person_one) + nameProd);
                        }
                        else
                        {
                            textViewLikeProd.setText(likeGlobal + " " + getResources().getString(R.string.none_person_more) + nameProd);
                        }
                    }
                }
                else if (tipoReg.equals("Cargar favorito"))
                {
                    JSONObject object = new JSONObject(s);
                    String res = object.getString("Resultado");
                    if (res.equals("Si"))
                    {
                        prodFav = true;
                        imageViewFavProd.setImageResource(R.drawable.heart_active);
                        if (object.getInt("Cuenta") == 0)
                        {
                            textViewLikeProd.setText(getResources().getString(R.string.none_person));
                        }
                        else if (object.getInt("Cuenta") == 1)
                        {
                            textViewLikeProd.setText(object.getInt("Cuenta") + " " + getResources().getString(R.string.none_person_one) + nameProd);
                        }
                        else
                        {
                            textViewLikeProd.setText(object.getInt("Cuenta") + " " + getResources().getString(R.string.none_person_more) + nameProd);
                        }
                        likeGlobal = object.getInt("Cuenta");

                    }
                    else
                    {
                        prodFav = false;
                        imageViewFavProd.setImageResource(R.drawable.heart_desactive);
                    }
                    progress.dismiss();
                    cargarCalificacion();
                }
                else if (tipoReg.equals("Insertar calificacion"))
                {
                    JSONObject object = new JSONObject(s);
                    String res = object.getString("Resultado");
                    if (res.equals("Si"))
                    {
                        float suma = Float.parseFloat(object.getString("Suma"));
                        int cuenta = Integer.parseInt(object.getString("Cuenta"));
                        float promedio = suma /cuenta;
                        ratingBarGlobal.setRating(promedio);
                        progress.dismiss();
                        prodCal = true;
                        Toast.makeText(getContext(), "Has calificado este producto", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (tipoReg.equals("Cargar calificacion"))
                {
                    JSONObject object = new JSONObject(s);
                    String res = object.getString("Valor");
                    if (!res.equals("null"))
                    {

                        prodCal = true;
                        sw = true;
                        ratingBarUser.setRating(Float.parseFloat(object.getString("Valor")));
                        ratingBarGlobal.setRating(Float.parseFloat(object.getString("Promedio")));
                    }
                    else
                    {
                        if (!object.getString("Promedio").equals("null"))
                        {
                            ratingBarGlobal.setRating(Float.parseFloat(object.getString("Promedio")));
                        }
                        sw = false;
                        prodCal = false;
                    }
                    progress.dismiss();
                    cargarComentarios();

                }
                else if (tipoReg.equals("Modificar calificacion"))
                {
                    JSONObject object = new JSONObject(s);
                    String res = object.getString("Actualizado");
                    if (res.equals("Si"))
                    {
                        float suma = Float.parseFloat(object.getString("Suma"));
                        int cuenta = Integer.parseInt(object.getString("Cuenta"));
                        float promedio = suma /cuenta;

                        ratingBarUser.setRating(calLocal);
                        ratingBarGlobal.setRating(promedio);

                        prodCal = true;
                        Toast.makeText(getContext(), "Has actualizado tu calificacion al producto " + nombreProd, Toast.LENGTH_SHORT).show();
                    }
                    progress.dismiss();
                }
                else if (tipoReg.equals("Agregar comentario"))
                {
                    JSONObject object = new JSONObject(s);
                    String res = object.getString("Insertado");
                    if (res.equals("Si"))
                    {
                        Toast.makeText(getContext(), "Comentario agregado", Toast.LENGTH_SHORT).show();
                        editTextComentario.setText("");
                        if (loadCommentary) progress.dismiss(); loadCommentary = false;
                        cargarComentarios();
                    }
                }
                else if (tipoReg.equals("Cargar comentarios"))
                {
                    if (!s.equals("[]"))
                    {
                        JSONArray jsonArray = new JSONArray(s);
                        comentariosProd = new ArrayList<>();
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject object = jsonArray.getJSONObject(i);

                            Comentarios comentarios = new Comentarios();
                            comentarios.setuNickname(object.getString("nickname"));
                            comentarios.setFechaCreacion(object.getString("date"));
                            comentarios.setuComentario(object.getString("commentary"));

                            comentariosProd.add(comentarios);
                        }
                        listViewComentariosProd.setAdapter(new AdapterCommentProducts());
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
}
