package com.chile.oscar.clickcomida_aplicacionmovil;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Codificacion;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.MetodosCreados;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StoreFragmentSelected.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StoreFragmentSelected#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreFragmentSelected extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    String nombre, des, calle, numero, open_hour, close_hour, lunch_hour, lunch_after_hour, start_day, end_day;;
    Bitmap imagenTienda;

    private OnFragmentInteractionListener mListener;

    public StoreFragmentSelected() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoreFragmentSelected.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreFragmentSelected newInstance(String param1, String param2) {
        StoreFragmentSelected fragment = new StoreFragmentSelected();
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
            imagenTienda = Codificacion.decodeBase64(getArguments().getString("imagen_tienda"));
            nombre = getArguments().getString("nombre_tienda");
            des = getArguments().getString("des_tienda");
            calle = getArguments().getString("calle_tienda");
            numero = getArguments().getString("numero_tienda");
            start_day = getArguments().getString("start_day");
            end_day = getArguments().getString("end_day");
            open_hour = getArguments().getString("open_hour");
            close_hour = getArguments().getString("close_hour");
            lunch_hour = getArguments().getString("lunch_open_hour");
            lunch_after_hour = getArguments().getString("lunch_after_hour");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store_fragment_selected, container, false);

        Button buttonMostrarProductos = (Button)view.findViewById(R.id.btnMostrarProductosTienda);
        TextView textViewNombrePrincipal = (TextView)view.findViewById(R.id.tvTituloTienda);
        TextView textViewNombre = (TextView)view.findViewById(R.id.tvNameTiendaSelected);
        TextView textViewDes = (TextView)view.findViewById(R.id.tvDesSelected);
        TextView textViewCalle= (TextView)view.findViewById(R.id.tvCalleSelected);
        TextView textViewNumero = (TextView)view.findViewById(R.id.tvNumeroSelected);
        TextView textViewHorario = (TextView)view.findViewById(R.id.tvHorarioSelected);
        ImageView imageView = (ImageView)view.findViewById(R.id.ivTiendaSelected);

        textViewNombrePrincipal.setText(getResources().getString(R.string.titulo_tienda_seleccionada) + " " + nombre);
        textViewNombre.setText(getResources().getString(R.string.nombre_tienda) + ": " + nombre);
        textViewDes.setText(getResources().getString(R.string.titulo_descripcion) + ": " + des);
        textViewCalle.setText(getResources().getString(R.string.calle_tienda) + ": " + calle);
        textViewNumero.setText(getResources().getString(R.string.titulo_calle_numero_usuario) + ": " + numero);

        String openupdate = new MetodosCreados().HoraNormal(open_hour);
        String closeupdate = new MetodosCreados().HoraNormal(close_hour);

        if (lunch_hour == "null" && lunch_after_hour == "null")
        {
            textViewHorario.setText("De " + start_day + " a " + end_day + ", horario continuado desde las " + openupdate + " hasta las " + closeupdate);
        }
        else
        {
            String openupdatelunch = new MetodosCreados().HoraNormal(lunch_hour);
            String closeupdatelunch = new MetodosCreados().HoraNormal(lunch_after_hour);
            textViewHorario.setText("De " + start_day + " a " + end_day + ", horario mañana desde las " + openupdate + " hasta las " + closeupdate + ", horario tarde desde las " + openupdatelunch + " hasta las " + closeupdatelunch);
        }
        imageView.setImageBitmap(imagenTienda);

        buttonMostrarProductos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.content_general, new showProducts_me());
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);
                trans.commit();
            }
        });
        return view;
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
