package com.chile.oscar.clickcomida_aplicacionmovil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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

import static android.app.Activity.RESULT_OK;


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
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    String store_id, store_name, tipoReg, imagenCod;

    ArrayList<Bitmap> imagesProducts = new ArrayList<>();
    ArrayList<Integer> idProducts = new ArrayList<>();
    ArrayList<String> nameProducts = new ArrayList<>();
    ArrayList<String> desProducts = new ArrayList<>();
    ArrayList<String> priceProducts = new ArrayList<>();
    ImageButton buttonPhotoProduct;
    AlertDialog dialogMod;

    int posProd, positionGlobal;
    GridView gvProductos;
    ProgressDialog progress;
    AutoCompleteTextView autoCompleteTextViewProd;
    boolean pulsado = false;
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
    boolean unProd = false;
    class ImagenDesPrecio extends BaseAdapter
    {

        @Override
        public int getCount()
        {
            if (unProd == false)
            {
                return imagesProducts.size();
            }
            else
            {
                return 1;
            }
        }

        @Override
        public Object getItem(int position)
        {
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
            ImageView imageViewProd = (ImageView)convertView.findViewById(R.id.ivProductoImage);
            TextView textViewNom = (TextView)convertView.findViewById(R.id.txtNombreProd);
            TextView textViewPrecio = (TextView)convertView.findViewById(R.id.txtPrecioProd);

            if (unProd)
            {
                imageViewProd.setImageDrawable(new MetodosCreados().RedondearBitmap(imagesProducts.get(posProd), getResources()));
                textViewNom.setText(nameProducts.get(posProd));
                textViewPrecio.setText("$" + priceProducts.get(posProd));
            }
            else
            {
                imageViewProd.setImageDrawable(new MetodosCreados().RedondearBitmap(imagesProducts.get(position), getResources()));
                textViewNom.setText(nameProducts.get(position));
                textViewPrecio.setText("$" + priceProducts.get(position));
            }

            //ImageView imageView = new ImageView(getContext());
            //imageView.setImageBitmap(imagesProducts.get(position));
            //imageView.setLayoutParams(new GridView.LayoutParams(260, 260));
            return convertView;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mostrar_productos_mios, container, false);

        progress = new ProgressDialog(getContext());
        progress.setMessage("Cargando productos...");
        progress.show();

        Button buttonBuscar = (Button)view.findViewById(R.id.btnBuscarProd);
        Button buttonMod = (Button)view.findViewById(R.id.btnModificarProductos_me);
        Button buttonDel = (Button)view.findViewById(R.id.btnEliminarProductos_me);

        autoCompleteTextViewProd = (AutoCompleteTextView)view.findViewById(R.id.acProductosMios);
        TextView textView = (TextView)view.findViewById(R.id.txtTitulo_me);
        textView.setText(getResources().getString(R.string.titulo_mis_productos) + " " + store_name);

        final JSONObject object = new JSONObject();
        try
        {
            object.put("store_id", store_id);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        gvProductos= (GridView) view.findViewById(R.id.gvProductos);

        gvProductos.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                positionGlobal = position;
                if (unProd)
                {
                    String nombre = nameProducts.get(posProd);
                    pulsado = true;
                }
                else
                {
                    String nombre = nameProducts.get(position);
                    pulsado = true;
                }

            }
        });
        buttonBuscar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (nameProducts.contains(autoCompleteTextViewProd.getText().toString()))
                {
                    nameProducts.add("Mostrar todos");
                    String[] prod = new String[1];
                    posProd = nameProducts.indexOf(autoCompleteTextViewProd.getText().toString());
                    prod[0] = nameProducts.get(nameProducts.indexOf(autoCompleteTextViewProd.getText().toString()));
                    if (prod[0].equals("Mostrar todos"))
                    {
                        unProd = false;
                        nameProducts.remove(nameProducts.indexOf("Mostrar todos"));
                    }
                    else
                    {
                        unProd = true;
                        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, nameProducts);
                        autoCompleteTextViewProd.setAdapter(arrayAdapter);
                        Toast.makeText(getContext(), "Para volver a mostrar todos tus productos escribe, Mostrar todos.", Toast.LENGTH_LONG).show();

                    }
                    pulsado = false;
                    gvProductos.setAdapter(new ImagenDesPrecio());
                    autoCompleteTextViewProd.setText("");
                }
            }
        });
        tipoReg = "Cargar productos";
        new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "/Controlador/cargarProductos.php", object.toString());
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
        buttonMod.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (pulsado)
                {
                    AlertDialog.Builder builderModificar = new AlertDialog.Builder(getContext());
                    View pUpdate = getActivity().getLayoutInflater().inflate(R.layout.modificar_productos_ventana, null);
                    builderModificar.setView(pUpdate);
                    dialogMod = builderModificar.create();
                    dialogMod.show();

                    final EditText textNombreProd = (EditText) pUpdate.findViewById(R.id.etNombreProdMod);
                    final EditText textDesProd = (EditText) pUpdate.findViewById(R.id.etDesProdMod);
                    final EditText textPreProd = (EditText) pUpdate.findViewById(R.id.etPrecioProdMod);

                    Button botonModProd = (Button)pUpdate.findViewById(R.id.btnModProd);
                    buttonPhotoProduct = (ImageButton)pUpdate.findViewById(R.id.ibModProducts);
                    Button botonModCerrar = (Button)pUpdate.findViewById(R.id.btnModCerrar);

                    buttonPhotoProduct.setImageBitmap(imagesProducts.get(positionGlobal));
                    textNombreProd.setText(nameProducts.get(positionGlobal));
                    textDesProd.setText(desProducts.get(positionGlobal));
                    textPreProd.setText(priceProducts.get(positionGlobal));


                    botonModProd.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            try
                            {
                                tipoReg = "Modificar producto";
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("id", idProducts.get(positionGlobal));
                                jsonObject.put("name", textNombreProd.getText().toString().trim());
                                jsonObject.put("description", textDesProd.getText().toString().trim());
                                jsonObject.put("price", textPreProd.getText().toString().trim());
                                jsonObject.put("imagen", imagenCod);
                                jsonObject.put("store_id", store_id);
                                new EjecutarSentencia().execute(getResources().getString(R.string.direccion_web) + "Controlador/modificarProductos_mios.php", jsonObject.toString());
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }

                        }
                    });
                    buttonPhotoProduct.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            dispatchTakePictureIntent();
                        }
                    });
                    botonModCerrar.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {
                            dialogMod.cancel();
                        }
                    });
                }
                else
                {
                    Toast.makeText(getContext(), "Debes seleccionar un producto para modificarlo.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //String[] ejemplo = {"Uno", "Dos"};
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, ejemplo);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
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
    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null)
        {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            imagenCod = Codificacion.encodeToBase64(bitmap, Bitmap.CompressFormat.PNG, 100);
            buttonPhotoProduct.setImageBitmap(bitmap);
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
    public interface OnFragmentInteractionListener
    {
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
                if (tipoReg.equals("Cargar productos"))
                {
                    if (s.equals("[]"))
                    {
                        Toast.makeText(getContext(), "Aun no tienes productos registrados.", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    else
                    {
                        JSONArray jsonArray = new JSONArray(s);
                        int tomarCuenta = jsonArray.length() / 2; //Indica que la otra mitad son las fotos
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
                                priceProducts.add(jsonObject.getString("price"));
                            }
                        }
                        gvProductos.setAdapter(new ImagenDesPrecio());
                        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, nameProducts);
                        autoCompleteTextViewProd.setAdapter(arrayAdapter);
                        progress.dismiss();
                    }
                }
                else if (tipoReg.equals("Modificar producto"))
                {
                    JSONObject object = new JSONObject(s);
                    String res = object.getString("Modificado");
                    if (res.equals("Si"))
                    {
                        Toast.makeText(getContext(), "El producto ha sido modificado", Toast.LENGTH_SHORT).show();
                        dialogMod.dismiss();
                    }
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

}
