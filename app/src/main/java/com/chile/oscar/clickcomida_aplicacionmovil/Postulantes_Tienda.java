package com.chile.oscar.clickcomida_aplicacionmovil;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class Postulantes_Tienda extends Fragment
{
    String imagen_cod, nombre_tienda, des_tienda, calle_tienda, numero_tienda, id_usuario;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.activity_postulantes__tienda, container, false);
        Button buttonContinuar = (Button)v.findViewById(R.id.btnContinuarPostulacion);
        final EditText editTextDescripcion = (EditText)v.findViewById(R.id.etDescripcionPostulantes);
        final NumberPicker numberPickerNumeroPostulantes = (NumberPicker)v.findViewById(R.id.npNumeroPostulantes);
        numberPickerNumeroPostulantes.setMaxValue(10);
        numberPickerNumeroPostulantes.setMinValue(1);
        numberPickerNumeroPostulantes.setWrapSelectorWheel(false);

        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (editTextDescripcion.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(), "Debes escribir una breve descripción acerca de los requisitos." + " " + nombre_tienda, Toast.LENGTH_SHORT).show();
                    editTextDescripcion.setError("Debes completar este campo.");
                }
                else
                {
                    int numPos = numberPickerNumeroPostulantes.getValue();
                    FragmentTransaction trans = getFragmentManager().beginTransaction();
                    trans.replace(R.id.content_general, newInstance(imagen_cod, nombre_tienda, des_tienda, calle_tienda, numero_tienda, editTextDescripcion.getText().toString(), String.valueOf(numPos), id_usuario));
                    trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    trans.addToBackStack(null);
                    trans.commit();
                }
            }
        });
        return v;
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            imagen_cod = getArguments().getString("IMAGEN_COD");
            nombre_tienda = getArguments().getString("NOMBRE_TIENDA");
            des_tienda = getArguments().getString("DES_TIENDA");
            calle_tienda = getArguments().getString("CALLE_TIENDA");
            numero_tienda = getArguments().getString("NUMERO_TIENDA");
            id_usuario = getArguments().getString("ID_USUARIO");
        }
    }
    public static fragmentTiendaDos newInstance(String... params)
    {
        fragmentTiendaDos fragment = new fragmentTiendaDos();
        Bundle args = new Bundle();
        args.putString("IMAGEN_COD", params[0]);
        args.putString("NOMBRE_TIENDA", params[1]);
        args.putString("DES_TIENDA", params[2]);
        args.putString("CALLE_TIENDA", params[3]);
        args.putString("NUMERO_TIENDA", params[4]);
        args.putString("DES_POSTULANTES", params[5]);
        args.putString("NUM_POSTULANTES", params[6]);
        args.putString("ID_USUARIO", params[7]);
        fragment.setArguments(args);
        return fragment;
    }
}
