package com.chile.oscar.clickcomida_aplicacionmovil;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Codificacion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import static android.app.Activity.RESULT_OK;

public class fragmentProductosVender extends Fragment implements View.OnClickListener, View.OnFocusChangeListener
{
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private final static int REQUEST_ACCESS_CAMERA = 123;

    Button btnAgregar, btnModificar, btnEliminar, btnContinuar;
    ListView listaProductos;
    String store_id;

    List<String> nombreProd = new ArrayList<>();
    List<String> desProd = new ArrayList<>();
    List<String> precioProd = new ArrayList<>();
    List<String> photosProd = new ArrayList<>();

    Bitmap imageBitmap;
    String tipo = "";
    ImageButton buttonPhotoProduct;
    List<JSONObject> jsonObjects = new ArrayList<>();
    ProgressDialog progress;

    EditText txtProducto;

    ImageButton botonImagen;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View p = inflater.inflate(R.layout.activity_fragment_productos_vender, container, false);

        btnAgregar = (Button)p.findViewById(R.id.btnAgregarProd);
        btnModificar = (Button)p.findViewById(R.id.btnModProd);
        btnEliminar = (Button)p.findViewById(R.id.btnEliminarProd);
        btnContinuar = (Button)p.findViewById(R.id.btnContinuar_prod);
        listaProductos = (ListView)p.findViewById(R.id.lvProd);

        btnAgregar.setOnClickListener(this);
        btnModificar.setOnClickListener(this);
        btnEliminar.setOnClickListener(this);
        btnContinuar.setOnClickListener(this);

        listaProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                posicionProducto = position;
                productoEncontrado = true;
            }
        });
        return p;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            store_id = getArguments().getString("store_id");
        }
    }
    boolean prodDb = false;
    int posicionProducto;
    boolean productoEncontrado = false, fotoTomada = false;
    String[] mostrarProd;
    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            //Agregar productos
            case R.id.btnAgregarProd:

                AlertDialog.Builder builderAgregar = new AlertDialog.Builder(getContext());
                View p = getActivity().getLayoutInflater().inflate(R.layout.agregar_productos_ventana, null);
                builderAgregar.setView(p);
                final AlertDialog dialogAdd = builderAgregar.create();
                dialogAdd.show();

                final EditText txtDescripcion, txtPrecio;
                txtProducto = (EditText)p.findViewById(R.id.etNombreProducto);
                txtDescripcion = (EditText)p.findViewById(R.id.etDescripcionProd);
                txtPrecio = (EditText)p.findViewById(R.id.etPrecioProd);

                Button botonAgre = (Button)p.findViewById(R.id.botonAdd);
                Button botonCerr = (Button)p.findViewById(R.id.botonClose);
                botonImagen = (ImageButton)p.findViewById(R.id.ibProduct);
                txtProducto.setOnFocusChangeListener(new View.OnFocusChangeListener()
                {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus)
                    {
                        if (hasFocus == false)
                        {
                            if (!txtProducto.getText().toString().isEmpty())
                            {
                                progress = new ProgressDialog(getContext());
                                progress.setMessage("Comprobando nombre..");
                                progress.setCanceledOnTouchOutside(false);
                                progress.show();

                                JSONObject object = new JSONObject();
                                try
                                {
                                    object.put("Nombre", txtProducto.getText().toString());
                                    object.put("Id", store_id);
                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }
                                new ConsultarProducto().execute(getResources().getString(R.string.direccion_web) + "Controlador/consultarProducto.php", object.toString());
                            }

                        }
                    }
                });

                botonAgre.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (validarCampos(txtProducto, txtDescripcion, txtPrecio))
                        {
                            if (prodDb == false)
                            {
                                if (nombreProd.contains(txtProducto.getText().toString()))
                                {
                                    Toast.makeText(getContext(), "El nombre ya existe, elige otro.", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    if (fotoTomada)
                                    {
                                        nombreProd.add(txtProducto.getText().toString().trim());
                                        desProd.add(txtDescripcion.getText().toString().trim());
                                        precioProd.add(txtPrecio.getText().toString().trim());
                                        photosProd.add(Codificacion.encodeToBase64(imageBitmap, Bitmap.CompressFormat.JPEG, 100));
                                        mostrarProd = new String[nombreProd.size()];

                                        for (int i = 0; i < nombreProd.size(); i++)
                                        {
                                            mostrarProd[i] = "Nombre: " + nombreProd.get(i) + ", Precio: " + precioProd.get(i);
                                        }
                                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, mostrarProd);
                                        listaProductos.setAdapter(arrayAdapter);

                                        txtProducto.setText("");
                                        txtDescripcion.setText("");
                                        txtPrecio.setText("");
                                        botonImagen.setImageResource(R.drawable.ic_menu_camera);

                                        Toast.makeText(getContext(), "Producto agregado.", Toast.LENGTH_SHORT).show();
                                        fotoTomada = false;
                                    }
                                    else
                                    {
                                        Toast.makeText(getContext(), "Debes tomar una foto para el producto.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            else
                            {
                                Toast.makeText(getContext(), "El producto " + txtProducto.getText().toString() + ", ya se encuentra en la base de datos.", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                });
                botonCerr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogAdd.cancel();
                    }
                });

                botonImagen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))
                            {
                                //Aqui pregunta primero cuando los permidos no estan activados
                                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_ACCESS_CAMERA);
                            }
                        }
                        else
                        {
                            //Päsa aqui cuando los permisos estan activados
                            tipo = "Agregar";
                            dispatchTakePictureIntent();
                        }
                    }
                });
                break;

            //Modificar productos
            case R.id.btnModProd:
                if (productoEncontrado)
                {
                    AlertDialog.Builder builderModificar = new AlertDialog.Builder(getContext());
                    View pUpdate = getActivity().getLayoutInflater().inflate(R.layout.modificar_productos_ventana, null);
                    builderModificar.setView(pUpdate);
                    final AlertDialog dialogMod = builderModificar.create();
                    dialogMod.show();

                    final EditText textNombreProd = (EditText) pUpdate.findViewById(R.id.etNombreProdMod);
                    final EditText textDesProd = (EditText) pUpdate.findViewById(R.id.etDesProdMod);
                    final EditText textPreProd = (EditText) pUpdate.findViewById(R.id.etPrecioProdMod);
                    Button botonModProd = (Button) pUpdate.findViewById(R.id.btnModProd);
                    Button botonModCerrar = (Button) pUpdate.findViewById(R.id.btnModCerrar);
                    buttonPhotoProduct = (ImageButton)pUpdate.findViewById(R.id.ibModProducts);

                    textNombreProd.setText(nombreProd.get(posicionProducto));
                    textDesProd.setText(desProd.get(posicionProducto));
                    textPreProd.setText(precioProd.get(posicionProducto));
                    buttonPhotoProduct.setImageBitmap(Codificacion.decodeBase64(photosProd.get(posicionProducto)));


                    botonModProd.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            if (validarCampos(textNombreProd, textDesProd, textPreProd))
                            {
                                int posicionActual = nombreProd.indexOf(textNombreProd.getText().toString());
                                if (posicionActual == posicionProducto || posicionActual == -1)
                                {
                                    nombreProd.set(posicionProducto, textNombreProd.getText().toString().trim());
                                    desProd.set(posicionProducto, textDesProd.getText().toString().trim());
                                    precioProd.set(posicionProducto, textPreProd.getText().toString().trim());
                                    photosProd.set(posicionProducto, Codificacion.encodeToBase64(imageBitmap, Bitmap.CompressFormat.JPEG, 100));
                                    buttonPhotoProduct.setImageResource(R.drawable.ic_menu_camera);
                                    cargarProductos();
                                    dialogMod.cancel();
                                    Toast.makeText(getContext(), "Producto modificado", Toast.LENGTH_SHORT).show();
                                    productoEncontrado = false;
                                }
                                else
                                {
                                    String other_item = nombreProd.get(posicionActual);
                                    Toast.makeText(getContext(), "El nombre " + other_item + " ya existe.", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    });
                    buttonPhotoProduct.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                                if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))
                                {
                                    //Aqui pregunta primero cuando los permidos no estan activados
                                    requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_ACCESS_CAMERA);
                                }
                            }
                            else
                            {
                                //Päsa aqui cuando los permisos estan activados
                                tipo = "Modificar";
                                dispatchTakePictureIntent();
                            }
                        }
                    });
                    botonModCerrar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogMod.cancel();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Para modificar un producto debes seleccionar un producto en la lista", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.btnEliminarProd:

                if (productoEncontrado)
                {
                    nombreProd.remove(posicionProducto);
                    desProd.remove(posicionProducto);
                    precioProd.remove(posicionProducto);
                    cargarProductos();
                    Toast.makeText(getContext(), "Producto eliminado", Toast.LENGTH_SHORT).show();
                    productoEncontrado = false;
                }
                else
                {
                    Toast.makeText(getContext(), "Para eliminar un producto debes seleccionar un producto en la lista", Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.btnContinuar_prod:
                if (nombreProd.isEmpty())
                {
                    Toast.makeText(getContext(), "Debes agregar al menos un producto.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    for (int i=0; i<nombreProd.size(); i++)
                    {
                        try
                        {
                            JSONObject object = new JSONObject();
                            object.put("Nombre", nombreProd.get(i));
                            object.put("Descripcion", desProd.get(i));
                            object.put("Precio", precioProd.get(i));
                            object.put("Fotos", photosProd.get(i));
                            object.put("store_id", store_id);
                            jsonObjects.add(object);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    String x = jsonObjects.toString();
                    new agregarProductos().execute(getResources().getString(R.string.direccion_web) + "Controlador/insertar_productos.php" ,jsonObjects.toString());
                }
                break;

            case R.id.etDescripcionProd:
                mostrarDialogoDescripcion();
                break;
        }
    }
    public boolean validarCampos (EditText t1, EditText t2, EditText t3)
    {
        if (t1.getText().toString().isEmpty() || t2.getText().toString().isEmpty() || t3.getText().toString().isEmpty())
        {
            if (t1.getText().toString().isEmpty())
            {
                t1.setError("Completa este campo.");
            }
            if (t2.getText().toString().isEmpty())
            {
                t2.setError("Completa este campo.");
            }
            if (t3.getText().toString().isEmpty())
            {
                t3.setError("Completa este campo.");
            }
            return false;
        }
        else
        {
            return true;
        }
    }
    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        switch (v.getId())
        {
            case R.id.etDescripcionProd:
                if (hasFocus)
                {
                    mostrarDialogoDescripcion();
                }
                break;
        }
    }
    public void mostrarDialogoDescripcion()
    {
        AlertDialog.Builder builderChange = new AlertDialog.Builder(getContext());
        View p = getActivity().getLayoutInflater().inflate(R.layout.ventana_descripcion, null);
        builderChange.setView(p);
        final AlertDialog dialogChangeDireccionTelefono = builderChange.create();
        dialogChangeDireccionTelefono.show();

        Button botonAddDes = (Button)p.findViewById(R.id.btnAddDes);
        Button botonDeleteDes = (Button)p.findViewById(R.id.btnBorrarDes);
        Button botonCloseDes = (Button)p.findViewById(R.id.btnCerrarDes);
        final EditText textoDialogo = (EditText)p.findViewById(R.id.etTextoDes);

        botonAddDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialogChangeDireccionTelefono.cancel();
            }
        });
        botonDeleteDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                textoDialogo.setText("");
            }
        });
        botonCloseDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChangeDireccionTelefono.cancel();
            }
        });
    }
    public void cargarProductos()
    {
        mostrarProd = new String[nombreProd.size()];
        for (int i = 0; i < nombreProd.size(); i++) {
            mostrarProd[i] = "Nombre: " + nombreProd.get(i) + ", Precio: " + precioProd.get(i);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, mostrarProd);
        listaProductos.setAdapter(arrayAdapter);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_ACCESS_CAMERA:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //Aceptado
                    if (tipo.equals("Modificar"))
                    {
                        tipo = "Modificar";
                    }
                    else
                    {
                        tipo = "Agregar";
                    }
                    dispatchTakePictureIntent();
                }
                else
                {
                    //Negado
                    Toast.makeText(getContext(), "Debes aceptar los permisos para la camara.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            if (tipo == "Agregar")
            {
                android.view.ViewGroup.LayoutParams params = botonImagen.getLayoutParams();
                params.height = imageBitmap.getHeight();
                params.width = imageBitmap.getHeight();
                botonImagen.setLayoutParams(params);
                botonImagen.setImageBitmap(imageBitmap);
            }

            if (tipo == "Modificar")
            {

                android.view.ViewGroup.LayoutParams params = buttonPhotoProduct.getLayoutParams();
                params.height = imageBitmap.getHeight();
                params.width = imageBitmap.getHeight();
                buttonPhotoProduct.setLayoutParams(params);
                buttonPhotoProduct.setImageBitmap(imageBitmap);
            }
            fotoTomada = true;
        }
    }

    public class agregarProductos extends AsyncTask<String, Void, String>
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
                JSONObject object = new JSONObject(s);
                String resultado = object.getString("Resultado");
                if (resultado.equals("Si"))
                {
                    Toast.makeText(getContext(), "Productos agregados con exito.", Toast.LENGTH_SHORT).show();
                    getFragmentManager().popBackStack();
                }
                else
                {
                    Toast.makeText(getContext(), "Algo paso.", Toast.LENGTH_SHORT).show();
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
    public class ConsultarProducto extends AsyncTask<String, Void, String>
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
                JSONObject object = new JSONObject(s);
                String res = object.getString("Resultado");
                if (res.equals("Si"))
                {
                    txtProducto.setError("Este producto ya lo tienes registrado en tu tienda.");
                    Toast.makeText(getContext(), txtProducto.getText().toString() + " ya existe en la base de datos.", Toast.LENGTH_SHORT).show();
                    prodDb = true;
                }
                else if (res.equals("No"))
                {
                    prodDb = false;
                }
                progress.dismiss();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
    }
}
