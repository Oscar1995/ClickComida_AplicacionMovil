package com.chile.oscar.clickcomida_aplicacionmovil;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;

public class fragmentProductosVender extends Fragment
{

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_fragment_productos_vender, container, false);
        EditText txtNombre = (EditText)view.findViewById(R.id.edNombreProducto);
        Button botonAgregar = (Button)view.findViewById(R.id.btnAgregarProducto);
        final ExpandableListView listaProductos = (ExpandableListView)view.findViewById(R.id.elProductos);

        botonAgregar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });

        return view;
    }

}
