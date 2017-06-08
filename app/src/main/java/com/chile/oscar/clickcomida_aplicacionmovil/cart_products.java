package com.chile.oscar.clickcomida_aplicacionmovil;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Carrito;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Productos_Carro;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link cart_products.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link cart_products#newInstance} factory method to
 * create an instance of this fragment.
 */
public class cart_products extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    List<Carrito> carritoList = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    public cart_products() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment cart_products.
     */
    // TODO: Rename and change types and number of parameters
    public static cart_products newInstance(String param1, String param2) {
        cart_products fragment = new cart_products();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cart_products, container, false);
        Button buttonAgregarProductos = null, buttonQuitarProductos = null, buttonFinalizarCompra = null;
        if (loadArray("productos", getContext()) != null)
        {
            LinearLayout.LayoutParams paramsMATCH_PARENT = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            LinearLayout linearLayoutContenedor = (LinearLayout)v.findViewById(R.id.llContenedor);

            for (int i=0; i<6; i++)
            {
                LinearLayout linearLayoutProd = new LinearLayout(getContext());
                TextView textViewNombreTienda = new TextView(getContext());

                TextView textViewNombreProducto = new TextView(getContext());
                TextView textViewNombrePrecio = new TextView(getContext());
                TextView textViewNombreCantidad = new TextView(getContext());
                TextView textViewNombreTotal = new TextView(getContext());

                GridView gridView = new GridView(getContext());
                buttonFinalizarCompra = new Button(getContext());
                LinearLayout horizontalText = new LinearLayout(getContext());
                LinearLayout horizontalBoton = new LinearLayout(getContext());
                buttonAgregarProductos = new Button(getContext());
                buttonQuitarProductos = new Button(getContext());

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams paramsButtonHorizontal = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);

                linearLayoutProd.setOrientation(LinearLayout.VERTICAL);
                horizontalText.setGravity(LinearLayout.VERTICAL);
                horizontalBoton.setOrientation(LinearLayout.HORIZONTAL);
                horizontalText.setLayoutParams(params);
                horizontalBoton.setLayoutParams(paramsMATCH_PARENT);

                linearLayoutProd.setLayoutParams(params);

                //TODO: ESTO SE UNA VEZ POR TIENDA
                //Nombre tienda
                textViewNombreTienda.setId(i);
                textViewNombreTienda.setText("Tienda " + i);
                textViewNombreTienda.setLayoutParams(params);
                textViewNombreTienda.setGravity(Gravity.CENTER);
                textViewNombreTienda.setTextColor(getResources().getColor(R.color.colorNegro));
                textViewNombreTienda.setTypeface(Typeface.DEFAULT_BOLD);

                //Encabezado,
                textViewNombreProducto.setText("Nombre");
                textViewNombreProducto.setLayoutParams(paramsButtonHorizontal);
                textViewNombreProducto.setGravity(Gravity.CENTER);
                textViewNombreProducto.setTextColor(getResources().getColor(R.color.colorNegro));
                textViewNombreProducto.setTypeface(Typeface.DEFAULT_BOLD);

                textViewNombrePrecio.setText("$");
                textViewNombrePrecio.setLayoutParams(paramsButtonHorizontal);
                textViewNombrePrecio.setGravity(Gravity.CENTER);
                textViewNombrePrecio.setTextColor(getResources().getColor(R.color.colorNegro));
                textViewNombrePrecio.setTypeface(Typeface.DEFAULT_BOLD);

                textViewNombreCantidad.setText("Cantidad");
                textViewNombreCantidad.setLayoutParams(paramsButtonHorizontal);
                textViewNombreCantidad.setGravity(Gravity.CENTER);
                textViewNombreCantidad.setTextColor(getResources().getColor(R.color.colorNegro));
                textViewNombreCantidad.setTypeface(Typeface.DEFAULT_BOLD);

                textViewNombreTotal.setText("Total");
                textViewNombreTotal.setLayoutParams(paramsButtonHorizontal);
                textViewNombreTotal.setGravity(Gravity.CENTER);
                textViewNombreTotal.setTextColor(getResources().getColor(R.color.colorNegro));
                textViewNombreTotal.setTypeface(Typeface.DEFAULT_BOLD);

                horizontalText.addView(textViewNombreProducto);
                horizontalText.addView(textViewNombrePrecio);
                horizontalText.addView(textViewNombreCantidad);
                horizontalText.addView(textViewNombreTotal);

                //TODO: AQUI SE IMPRIMIRA TODOS LOS PRODUCTOS DE LA TIENDA
                gridView.setId(i);
                gridView.setNumColumns(4);
                gridView.setLayoutParams(paramsMATCH_PARENT);
                gridView.setGravity(Gravity.RIGHT);
                String[] ejemplo = {"Pizza", "2000", "2", "4000"};
                ArrayAdapter arrayAdapterTitulo = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, ejemplo);
                gridView.setAdapter(arrayAdapterTitulo);
                //TODO: TERMINANDO HASTA AQUI

                buttonFinalizarCompra.setText(getResources().getString(R.string.Finalizar));
                buttonFinalizarCompra.setBackground(getResources().getDrawable(R.drawable.colorbuttonceleste));
                buttonFinalizarCompra.setTextColor(getResources().getColor(R.color.textoBlanco));
                buttonFinalizarCompra.setLayoutParams(params);
                buttonFinalizarCompra.setId(i); //TODO: ID DE LA TIENDA

                buttonAgregarProductos.setText(getResources().getString(R.string.Agregar_Productos));
                buttonAgregarProductos.setBackground(getResources().getDrawable(R.drawable.colorbutton));
                buttonAgregarProductos.setTextColor(getResources().getColor(R.color.textoBlanco));
                buttonAgregarProductos.setLayoutParams(paramsButtonHorizontal);
                buttonAgregarProductos.setId(i); //TODO: ID DE LA TIENDA

                buttonQuitarProductos.setText(getResources().getString(R.string.Quitar));
                buttonQuitarProductos.setBackground(getResources().getDrawable(R.drawable.colorbuttonred));
                buttonQuitarProductos.setTextColor(getResources().getColor(R.color.textoBlanco));
                buttonQuitarProductos.setLayoutParams(paramsButtonHorizontal);
                buttonQuitarProductos.setId(i); //TODO: ID DE LA TIENDA

                //ESTE AGREGA LOS BOTONES DE FORMA HORIZONTAL EN UN LAYOUT
                horizontalBoton.addView(buttonAgregarProductos);
                horizontalBoton.addView(buttonQuitarProductos);

                linearLayoutProd.addView(textViewNombreTienda);
                linearLayoutProd.addView(horizontalText);
                linearLayoutProd.addView(gridView);
                linearLayoutProd.addView(buttonFinalizarCompra);
                linearLayoutProd.addView(horizontalBoton);
                //Este siempre debe ir abajo
                linearLayoutContenedor.addView(linearLayoutProd);

                buttonAgregarProductos.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Toast.makeText(getContext(), "Tienda: " +v.getId(), Toast.LENGTH_SHORT).show();
                    }
                });
                buttonQuitarProductos.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Toast.makeText(getContext(), "Tienda: " +v.getId(), Toast.LENGTH_SHORT).show();
                    }
                });
                buttonFinalizarCompra.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "Tienda: " +v.getId(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }


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
    public boolean cargarPreferencias()
    {
        carritoList.clear();
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("carro", Context.MODE_PRIVATE);
        String concatenacion = sharedpreferences.getString("idprod_cantidadprod", "");
        Toast.makeText(getContext(), "" + concatenacion, Toast.LENGTH_SHORT).show();
        if (!concatenacion.isEmpty())
        {
            String[] division = concatenacion.split("-");
            Carrito carrito = new Carrito();
            carrito.setId(Integer.parseInt(division[0])); //Id del producto
            carrito.setCantidad(Integer.parseInt(division[1])); //Cantidad del producto
            carritoList.add(carrito);
        }
        if (carritoList.isEmpty())
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public String[] loadArray(String arrayName, Context mContext)
    {
        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        String array[] = new String[size];
        for(int i=0;i<size;i++)
        {
            array[i] = prefs.getString(arrayName + "_" + i, null);
            Toast.makeText(getContext(), "" + array[i], Toast.LENGTH_SHORT).show();
        }

        return array;
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
}
