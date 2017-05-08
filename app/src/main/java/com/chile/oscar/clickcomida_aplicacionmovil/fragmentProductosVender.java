package com.chile.oscar.clickcomida_aplicacionmovil;

import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class fragmentProductosVender extends Fragment implements View.OnClickListener, View.OnFocusChangeListener
{
    Button btnAgregar, btnModificar, btnEliminar, btnContinuar;
    EditText txtProducto, txtDescripcion, txtPrecio;
    ListView listaProductos;

    List<String> nombreProd = new ArrayList<>();
    List<String> desProd = new ArrayList<>();
    List<String> precioProd = new ArrayList<>();
    List<String> listaMostrar = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View p = inflater.inflate(R.layout.activity_fragment_productos_vender, container, false);

        btnAgregar = (Button)p.findViewById(R.id.btnAgregarProd);
        btnModificar = (Button)p.findViewById(R.id.btnModProd);
        btnEliminar = (Button)p.findViewById(R.id.btnEliminarProd);
        btnContinuar = (Button)p.findViewById(R.id.btnContinuar_prod);

        txtProducto = (EditText)p.findViewById(R.id.etNombreProducto);
        txtDescripcion = (EditText)p.findViewById(R.id.etDescripcionProd);
        txtPrecio = (EditText)p.findViewById(R.id.etPrecioProd);

        listaProductos = (ListView)p.findViewById(R.id.lvProd);

        txtDescripcion.setOnFocusChangeListener(this);
        txtDescripcion.setOnClickListener(this);
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

    int posicionProducto;
    boolean productoEncontrado = false;
    String[] mostrarProd;
    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.btnAgregarProd:
                if (validarCampos(txtProducto, txtDescripcion, txtPrecio)) {
                    if (nombreProd.contains(txtProducto.getText().toString())) {
                        Toast.makeText(getContext(), "El nombre ya existe, elige otro.", Toast.LENGTH_SHORT).show();
                    } else {
                        nombreProd.add(txtProducto.getText().toString());
                        desProd.add(txtDescripcion.getText().toString());
                        precioProd.add(txtPrecio.getText().toString());
                        mostrarProd = new String[nombreProd.size()];

                        for (int i = 0; i < nombreProd.size(); i++) {
                            mostrarProd[i] = "Nombre: " + nombreProd.get(i) + ", Precio: " + precioProd.get(i);
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, mostrarProd);
                        listaProductos.setAdapter(arrayAdapter);
                    }
                }
                break;

            //Modificar productos
            case R.id.btnModProd:
                if (productoEncontrado) {
                    final String dato = listaProductos.getItemAtPosition(posicionProducto).toString();

                    AlertDialog.Builder builderModificar = new AlertDialog.Builder(getContext());
                    View p = getActivity().getLayoutInflater().inflate(R.layout.modificar_productos_ventana, null);
                    builderModificar.setView(p);
                    final AlertDialog dialogMod = builderModificar.create();
                    dialogMod.show();

                    final EditText textNombreProd = (EditText) p.findViewById(R.id.etNombreProdMod);
                    final EditText textDesProd = (EditText) p.findViewById(R.id.etDesProdMod);
                    final EditText textPreProd = (EditText) p.findViewById(R.id.etPrecioProdMod);
                    Button botonModProd = (Button) p.findViewById(R.id.btnModProd);
                    Button botonModCerrar = (Button) p.findViewById(R.id.btnModCerrar);

                    textNombreProd.setText(nombreProd.get(posicionProducto));
                    textDesProd.setText(desProd.get(posicionProducto));
                    textPreProd.setText(precioProd.get(posicionProducto));

                    botonModProd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (validarCampos(textNombreProd, textDesProd, textPreProd)) {
                                if (nombreProd.contains(textNombreProd.getText().toString())) {
                                    Toast.makeText(getContext(), "El producto ya exite con el nombre " + textNombreProd.getText().toString() + ". Elige otro nombre.", Toast.LENGTH_LONG).show();
                                } else {
                                    nombreProd.set(posicionProducto, textNombreProd.getText().toString());
                                    desProd.set(posicionProducto, textDesProd.getText().toString());
                                    precioProd.set(posicionProducto, textPreProd.getText().toString());

                                    mostrarProd = new String[nombreProd.size()];
                                    for (int i = 0; i < nombreProd.size(); i++) {
                                        mostrarProd[i] = "Nombre: " + nombreProd.get(i) + ", Precio: " + precioProd.get(i);
                                    }
                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, mostrarProd);
                                    listaProductos.setAdapter(arrayAdapter);
                                    dialogMod.cancel();
                                    Toast.makeText(getContext(), "Producto modificado", Toast.LENGTH_SHORT).show();
                                }
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

                    mostrarProd = new String[nombreProd.size()];
                    for (int i = 0; i < nombreProd.size(); i++) {
                        mostrarProd[i] = "Nombre: " + nombreProd.get(i) + ", Precio: " + precioProd.get(i);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, mostrarProd);
                    listaProductos.setAdapter(arrayAdapter);
                    Toast.makeText(getContext(), "Producto eliminado", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(), "Para eliminar un producto debes seleccionar un producto en la lista", Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.btnContinuar_prod:

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
        textoDialogo.setText(txtDescripcion.getText().toString());

        botonAddDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                txtDescripcion.setText(textoDialogo.getText().toString());
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
}
