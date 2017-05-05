package com.chile.oscar.clickcomida_aplicacionmovil;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import static android.app.Activity.RESULT_OK;


public class fragmentTienda extends Fragment implements View.OnClickListener
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static final int REQUEST_IMAGE_CAPTURE = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button botonContinuar, botonUAutomatico;
    ImageButton tImagen_uno, tImagen_dos, tImagen_tres, tImagen_cuatro, tImagen_cinco, tImagen_seis;
    private OnFragmentInteractionListener mListener;

    public fragmentTienda()
    {
        // Required empty public constructor

    }

    public static fragmentTienda newInstance(String param1, String param2)
    {
        fragmentTienda fragment = new fragmentTienda();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment_tienda, container, false);
        botonContinuar = (Button)v.findViewById(R.id.btnContinuarUno);
        botonUAutomatico = (Button)v.findViewById(R.id.btnUbicacionAutomatica);
        tImagen_uno = (ImageButton)v.findViewById(R.id.ibUno);
        tImagen_dos = (ImageButton)v.findViewById(R.id.ibDos);
        tImagen_tres = (ImageButton)v.findViewById(R.id.ibTres);
        tImagen_cuatro = (ImageButton)v.findViewById(R.id.ibCuatro);
        tImagen_cinco = (ImageButton)v.findViewById(R.id.ibCinco);
        tImagen_seis = (ImageButton)v.findViewById(R.id.ibSeis);

        tImagen_uno.setOnClickListener(this);
        tImagen_dos.setOnClickListener(this);
        tImagen_tres.setOnClickListener(this);
        tImagen_cuatro.setOnClickListener(this);
        tImagen_cinco.setOnClickListener(this);
        tImagen_seis.setOnClickListener(this);

        botonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.content_general, new fragmentTiendaDos());
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);
                trans.commit();

            }
        });
        botonUAutomatico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

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

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ibUno:
                posicion = 1;
                dispatchTakePictureIntent();
                break;

            case R.id.ibDos:
                posicion = 2;
                dispatchTakePictureIntent();
                break;

            case R.id.ibTres:
                posicion = 3;
                dispatchTakePictureIntent();
                break;

            case R.id.ibCuatro:
                posicion = 4;
                dispatchTakePictureIntent();
                break;

            case R.id.ibCinco:
                posicion = 5;
                dispatchTakePictureIntent();
                break;

            case R.id.ibSeis:
                posicion = 6;
                dispatchTakePictureIntent();
                break;
        }
    }
    int posicion = 0;
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null)
        {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if (posicion == 1)
            {
                tImagen_uno.setImageBitmap(imageBitmap);
            }
            else if(posicion == 2)
            {
                tImagen_dos.setImageBitmap(imageBitmap);
            }
            else if(posicion == 3)
            {
                tImagen_tres.setImageBitmap(imageBitmap);
            }
            else if(posicion == 4)
            {
                tImagen_cuatro.setImageBitmap(imageBitmap);
            }
            else if(posicion == 5)
            {
                tImagen_cinco.setImageBitmap(imageBitmap);
            }
            else if(posicion == 6)
            {
                tImagen_seis.setImageBitmap(imageBitmap);
            }
        }
    }
}
