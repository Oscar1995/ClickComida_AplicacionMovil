package com.chile.oscar.clickcomida_aplicacionmovil;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link showProducts_me.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link showProducts_me#newInstance} factory method to
 * create an instance of this fragment.
 */
public class showProducts_me extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView listViewProductos;
    private OnFragmentInteractionListener mListener;

    int[] images = {R.drawable.ic_cancelar};
    String[] des = {"Descripcion"};

    ArrayList<String> descripcion;

    public showProducts_me()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment showProducts_me.
     */
    // TODO: Rename and change types and number of parameters
    public static showProducts_me newInstance(String param1, String param2) {
        showProducts_me fragment = new showProducts_me();
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
    int posCol = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_show_products_me, container, false);
        descripcion = new ArrayList<String>();

        descripcion.add("Manzana");
        descripcion.add("Pera");
        descripcion.add("Naranja");

        listViewProductos = (ListView)v.findViewById(R.id.lvProductosTienda_me);
        listViewProductos.setAdapter(new CustomAdapter());
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
    class CustomAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return 6;
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
            convertView = getActivity().getLayoutInflater().inflate(R.layout.customlayout_product, null);

            ImageView imageViewProd = (ImageView)convertView.findViewById(R.id.ivUnoProducts);
            ImageView imageViewProdDos = (ImageView)convertView.findViewById(R.id.ivDosProducts);
            ImageView imageViewProdTres = (ImageView)convertView.findViewById(R.id.ivTresProducts);

            final TextView textViewUno = (TextView)convertView.findViewById(R.id.txtDesUno);
            final TextView textViewDos = (TextView)convertView.findViewById(R.id.txtDesDos);
            final TextView textViewTres = (TextView)convertView.findViewById(R.id.txtDesTres);

            imageViewProd.setImageResource(images[0]);
            imageViewProdDos.setImageResource(images[0]);
            imageViewProdTres.setImageResource(images[0]);

            textViewUno.setText("Manzana");
            textViewDos.setText("Pera");
            textViewTres.setText("Naranja");


            imageViewProd.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = descripcion.indexOf(textViewUno.getText());
                    Toast.makeText(getContext(), "pos:" + pos, Toast.LENGTH_SHORT).show();
                }
            });
            imageViewProdDos.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = descripcion.indexOf(textViewDos.getText());
                    Toast.makeText(getContext(), "pos:" + pos, Toast.LENGTH_SHORT).show();
                }
            });
            imageViewProdTres.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    int pos = descripcion.indexOf(textViewTres.getText());
                    Toast.makeText(getContext(), "pos:" + pos, Toast.LENGTH_SHORT).show();
                }
            });


            return convertView;
        }
    }
}
