package com.chile.oscar.clickcomida_aplicacionmovil;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Codificacion;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import static android.app.Activity.RESULT_OK;


public class fragmentTienda extends Fragment implements View.OnClickListener
{
    static final int REQUEST_IMAGE_CAPTURE = 1;

    String imagenGeneral = "";

    // TODO: Rename and change types of parameters
    private String id_usuario;

    Button botonContinuar, btnMapa;
    CheckBox checkBoxReparto;
    ImageButton tImagen_principal;
    EditText txtNombreTienda, txtCalleTienda, txtNumeroTienda, txtDescripcion;
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
                    if (checkBoxReparto.isChecked())
                    {
                        FragmentTransaction trans = getFragmentManager().beginTransaction();
                        trans.replace(R.id.content_general, newInstance(imagenGeneral, txtNombreTienda.getText().toString(), txtDescripcion.getText().toString(), txtCalleTienda.getText().toString(), txtNumeroTienda.getText().toString(), id_usuario));
                        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        trans.addToBackStack(null);
                        trans.commit();
                    }
                    else
                    {
                        FragmentTransaction trans = getFragmentManager().beginTransaction();
                        trans.replace(R.id.content_general, newInstance(imagenGeneral, txtNombreTienda.getText().toString(), txtDescripcion.getText().toString(), txtCalleTienda.getText().toString(), txtNumeroTienda.getText().toString()), id_usuario);
                        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        trans.addToBackStack(null);
                        trans.commit();
                    }
                }
            }
        });
        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builderMapa = new AlertDialog.Builder(getContext());
                View pMap = getActivity().getLayoutInflater().inflate(R.layout.mapa_fragment_update, null);
                SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapita_lindo);
                builderMapa.setView(pMap);
                AlertDialog mapUpdate = builderMapa.create();
                mapUpdate.show();
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
                dispatchTakePictureIntent();
                break;
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
            imagenGeneral = Codificacion.encodeToBase64(imageBitmap, Bitmap.CompressFormat.PNG, 60);
        }
    }
}