package com.chile.oscar.clickcomida_aplicacionmovil;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Codificacion;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Coordenadas;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;


public class fragmentTienda extends Fragment implements View.OnClickListener
{
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int SELECT_PICTURE = 2;

    String imagenGeneral = "";

    // TODO: Rename and change types of parameters
    private String id_usuario;

    Button botonContinuar, btnMapa;
    CheckBox checkBoxReparto;
    ImageButton tImagen_principal;
    EditText txtNombreTienda, txtCalleTienda, txtNumeroTienda, txtDescripcion;

    int[] images = {R.drawable.ic_camera, R.drawable.ic_take_photo, R.drawable.ic_cancelar};
    String[]des = {"Tomar foto", "Ir a galeria", "Cancelar"};
    private OnFragmentInteractionListener mListener;

    public fragmentTienda()
    {
        // Required empty public constructor

    }

    public static Postulantes_Tienda newInstance(String... params)
    {
        Postulantes_Tienda fragment = new Postulantes_Tienda();
        Bundle args = new Bundle();
        args.putString("IMAGEN_COD", params[0]);
        args.putString("NOMBRE_TIENDA", params[1]);
        args.putString("DES_TIENDA", params[2]);
        args.putString("CALLE_TIENDA", params[3]);
        args.putString("NUMERO_TIENDA", params[4]);
        args.putString("ID_USUARIO", params[5]);
        args.putString("LONGITUD", params[6]);
        args.putString("LATITUD", params[7]);
        fragment.setArguments(args);
        return fragment;
    }
    public static fragmentTiendaDos fragTiendaDos(String id, String... params)
    {
        fragmentTiendaDos fragment = new fragmentTiendaDos();
        Bundle args = new Bundle();
        args.putString("IMAGEN_COD", params[0]);
        args.putString("NOMBRE_TIENDA", params[1]);
        args.putString("DES_TIENDA", params[2]);
        args.putString("CALLE_TIENDA", params[3]);
        args.putString("NUMERO_TIENDA", params[4]);
        args.putString("LONGITUD", params[5]);
        args.putString("LATITUD", params[6]);
        args.putString("ID_USUARIO", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
           id_usuario = getArguments().getString("ID_USUARIO");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_fragment_tienda, container, false);
        botonContinuar = (Button)v.findViewById(R.id.btnContinuarUno);
        btnMapa = (Button)v.findViewById(R.id.btnMostrarMapaTienda);
        checkBoxReparto = (CheckBox)v.findViewById(R.id.cbReparto);
        tImagen_principal = (ImageButton)v.findViewById(R.id.ibImagenPrincipal);
        txtNombreTienda = (EditText)v.findViewById(R.id.etNombreTienda);
        txtCalleTienda = (EditText)v.findViewById(R.id.etCalle);
        txtNumeroTienda = (EditText)v.findViewById(R.id.etNumero);
        txtDescripcion = (EditText)v.findViewById(R.id.etDesTiendaGeneral);
        tImagen_principal.setOnClickListener(this);

        botonContinuar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (imagenGeneral.isEmpty() || txtNombreTienda.getText().toString().isEmpty() || txtNumeroTienda.getText().toString().isEmpty() || txtDescripcion.getText().toString().isEmpty() && txtCalleTienda.getText().toString().isEmpty() && txtNumeroTienda.getText().toString().isEmpty())
                {
                    if (imagenGeneral.isEmpty())Toast.makeText(getContext(), "Debes agregar una imagen para tu tienda.", Toast.LENGTH_SHORT).show();
                    if (txtNombreTienda.getText().toString().isEmpty())txtNombreTienda.setError("Debes completar este campo.");
                    if (txtDescripcion.getText().toString().isEmpty())txtDescripcion.setError("Debes completar este campo.");
                    if (txtCalleTienda.getText().toString().isEmpty())txtCalleTienda.setError("Debes completar este campo.");
                    if (txtNumeroTienda.getText().toString().isEmpty())txtNumeroTienda.setError("Debes completar este campo.");
                }
                else
                {
                    if (Coordenadas.latitud == 0.0 && Coordenadas.longitud == 0.0)
                    {
                        Toast.makeText(getContext(), "Debes indicar tu posicion en el mapa.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if (checkBoxReparto.isChecked())
                        {
                            FragmentTransaction trans = getFragmentManager().beginTransaction();
                            trans.replace(R.id.content_general, newInstance(imagenGeneral, txtNombreTienda.getText().toString(), txtDescripcion.getText().toString(), txtCalleTienda.getText().toString(), txtNumeroTienda.getText().toString(), id_usuario, String.valueOf(Coordenadas.longitud), String.valueOf(Coordenadas.latitud)));
                            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            trans.addToBackStack(null);
                            trans.commit();
                        }
                        else
                        {
                            FragmentTransaction trans = getFragmentManager().beginTransaction();
                            trans.replace(R.id.content_general, fragTiendaDos(id_usuario, imagenGeneral, txtNombreTienda.getText().toString(), txtDescripcion.getText().toString(), txtCalleTienda.getText().toString(), txtNumeroTienda.getText().toString(), String.valueOf(Coordenadas.longitud), String.valueOf(Coordenadas.latitud)));
                            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            trans.addToBackStack(null);
                            trans.commit();
                        }
                    }
                }
            }
        });
        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getContext(), "Indica tu posición en el mapa.", Toast.LENGTH_LONG).show();
                final AlertDialog.Builder builderMapa = new AlertDialog.Builder(getContext());
                View pMap = getActivity().getLayoutInflater().inflate(R.layout.activity_maps_tienda, null);

                final SupportMapFragment map = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.mapFrag);

                Button botonTomarCoor = (Button) pMap.findViewById(R.id.btnFijarMapaTienda);
                builderMapa.setView(pMap);
                final AlertDialog mapUpdate = builderMapa.create();
                mapUpdate.show();


                map.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(final GoogleMap googleMap)
                    {
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        googleMap.setMyLocationEnabled(true);
                        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(LatLng latLng)
                            {
                                googleMap.clear();
                                //LatLng posicionLocal = new LatLng(latLng.latitude, latLng.longitude);
                                googleMap.addMarker(new MarkerOptions().position(latLng).title("Marca"));
                                Coordenadas.latitud = latLng.latitude;
                                Coordenadas.longitud = latLng.longitude;
                            }
                        });
                    }
                });
                botonTomarCoor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        getFragmentManager().beginTransaction().remove(map).commit();
                        mapUpdate.dismiss();
                    }
                });

                /*AlertDialog.Builder builderChange = new AlertDialog.Builder(getContext());
                View p = getActivity().getLayoutInflater().inflate(R.layout.activity_maps_tienda, null);
                builderChange.setView(p);
                AlertDialog dialogChangeDireccionn = builderChange.create();
                dialogChangeDireccionn.show();*/
            }
        });
        return v;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ibImagenPrincipal:
                //dispatchTakePictureIntent();
                AlertDialog.Builder builderChange = new AlertDialog.Builder(getContext());
                View p = getActivity().getLayoutInflater().inflate(R.layout.foto_galeria_cancelar, null);
                builderChange.setView(p);

                ListView listViewPhoto_Gallery = (ListView)p.findViewById(R.id.lvPhoto_Gallery);

                CustomAdapter customAdapter = new CustomAdapter();
                listViewPhoto_Gallery.setAdapter(customAdapter);
                final AlertDialog dialogAlert = builderChange.create();
                dialogAlert.show();

                listViewPhoto_Gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        if (position == 0)
                        {
                            dialogAlert.dismiss();
                            dispatchTakePictureIntent();
                        }
                        else if(position == 1)
                        {
                            dialogAlert.dismiss();
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), SELECT_PICTURE);
                        }
                        else if (position ==2)
                        {
                            dialogAlert.dismiss();
                        }
                    }
                });
                break;
        }
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
            convertView = getActivity().getLayoutInflater().inflate(R.layout.customlayout_photo_galley_cancel, null);
            ImageView imageView = (ImageView)convertView.findViewById(R.id.ivProductoImage);
            TextView textViewNombre = (TextView)convertView.findViewById(R.id.txtOption);

            imageView.setImageResource(images[position]);
            textViewNombre.setText(des[position]);

            return convertView;
        }
    }

    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private void dispatchTakePictureIntent()
    {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
        {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null)
            {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
        else
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA))
            {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
                // Mostrar diálogo explicativo
            }
            else
            {
                // Solicitar permiso
                //ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
            }
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            tImagen_principal.setImageBitmap(imageBitmap);
            imagenGeneral = Codificacion.encodeToBase64(imageBitmap, Bitmap.CompressFormat.PNG, 100);
        }
        else if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK)
        {
            Uri path = data.getData();
            try
            {
                int HeightButton = tImagen_principal.getHeight();
                int WidthButton = tImagen_principal.getWidth();

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), path);
                Bitmap rotado = Codificacion.RotarBitmap(Codificacion.bajarResolucion(bitmap, WidthButton, HeightButton), 90);
                tImagen_principal.setImageBitmap(rotado);
                imagenGeneral = Codificacion.encodeToBase64(rotado, Bitmap.CompressFormat.PNG, 100);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}