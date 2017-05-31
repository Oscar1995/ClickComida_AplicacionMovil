package com.chile.oscar.clickcomida_aplicacionmovil;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Rating;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Codificacion;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.MetodosCreados;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StoreOtherUser.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StoreOtherUser#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreOtherUser extends Fragment implements View.OnClickListener
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String nombre, des, calle, numero, open_hour, close_hour, lunch_hour, lunch_after_hour, start_day, end_day, store_id, latitud, longitud, user_id, tipoReg;
    Bitmap imagenTienda;
    Boolean userCal = false, sw = false;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RatingBar ratingBarUsuario, ratingTienda;
    TextView textViewCal;

    private OnFragmentInteractionListener mListener;

    public StoreOtherUser() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoreOtherUser.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreOtherUser newInstance(String param1, String param2)
    {
        StoreOtherUser fragment = new StoreOtherUser();
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
            store_id = getArguments().getString("tienda_id");
            latitud = getArguments().getString("latitud");
            longitud = getArguments().getString("longitud");
            user_id = getArguments().getString("user_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store_other_user, container, false);

        ImageView imageViewOther = (ImageView)view.findViewById(R.id.ivTiendaOther);
        TextView textViewNombre =(TextView)view.findViewById(R.id.tvNombreOther);
        TextView textViewDireccion =(TextView)view.findViewById(R.id.tvDireccionOther);
        TextView textViewKilometros =(TextView)view.findViewById(R.id.tvKilometrosOther);
        TextView textViewHorario = (TextView)view.findViewById(R.id.tvHorarioOther);
        final EditText editTextComentario  = (EditText)view.findViewById(R.id.etComentarioOther);
        Button buttonComentar = (Button)view.findViewById(R.id.btnComentar);

        textViewCal = (TextView)view.findViewById(R.id.tvInfoCal);

        imageViewOther.setImageBitmap(imagenTienda);
        textViewNombre.setText(nombre);
        textViewDireccion.setText(getResources().getString(R.string.calle_tienda) + ": " + calle + ", " + getResources().getString(R.string.numero_tienda) + ": " + numero);


        String openupdate = new MetodosCreados().HoraNormal(open_hour);
        String closeupdate = new MetodosCreados().HoraNormal(close_hour);

        if (lunch_hour.equals("00:00:00") && lunch_after_hour.equals("00:00:00"))
        {
            textViewHorario.setText("De " + start_day + " a " + end_day + ", horario continuado desde las " + openupdate + " hasta las " + closeupdate);
        }
        else
        {
            String openupdatelunch = new MetodosCreados().HoraNormal(lunch_hour);
            String closeupdatelunch = new MetodosCreados().HoraNormal(lunch_after_hour);
            textViewHorario.setText("De " + start_day + " a " + end_day + ", horario mañana desde las " + openupdate + " hasta las " + closeupdate + ", horario tarde desde las " + openupdatelunch + " hasta las " + closeupdatelunch);
        }

        cargarCalificacion();
        ratingTienda = (RatingBar)view.findViewById(R.id.rbTienda);
        ratingBarUsuario = (RatingBar)view.findViewById(R.id.ratingBarU);
        ratingBarUsuario.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
        {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
            {
                if (sw == false)
                {
                    if (userCal == false)
                    {
                        ratingBarUsuario.setRating(rating);
                        JSONObject jsonObject = new JSONObject();
                        try
                        {
                            jsonObject.put("Valor", rating);
                            jsonObject.put("user_id", user_id);
                            jsonObject.put("store_id", store_id);
                            tipoReg = "Insertar calificacion";
                            userCal = true;
                            new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/calificarTienda.php", jsonObject.toString());
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        //Update
                        ratingBarUsuario.setRating(rating);
                        JSONObject jsonObject = new JSONObject();
                        try
                        {
                            jsonObject.put("Valor", rating);
                            jsonObject.put("user_id", user_id);
                            jsonObject.put("store_id", store_id);
                            tipoReg = "Modificar calificacion";
                            userCal = true;
                            new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/modificarCalificacion.php", jsonObject.toString());
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }

                }
                sw = false;
            }
        });
        buttonComentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (!editTextComentario.getText().toString().isEmpty())
                {
                    new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador");
                }
                else
                {
                    Toast.makeText(getContext(), "El comentario no debe estar en blanco", Toast.LENGTH_SHORT).show();
                }
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

    @Override
    public void onClick(View v) {

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
    public void cargarCalificacion()
    {
        JSONObject object = new JSONObject();
        try
        {
            object.put("user_id", user_id);
            object.put("store_id", store_id);
            tipoReg = "Cargar valor";
            new EjecutarConsulta().execute(getResources().getString(R.string.direccion_web) + "Controlador/cargarCalificacion.php", object.toString());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
    public class EjecutarConsulta extends AsyncTask<String, Void, String> {
        @Override
        public String doInBackground(String... params) {
            HttpURLConnection conn = null;
            try {
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
                switch (responseCode) {
                    case 200:
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                        return response.toString();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (conn != null) {
                    try {
                        conn.disconnect();
                    } catch (Exception ex) {
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

                if (tipoReg.equals("Insertar calificacion"))
                {
                    String res = object.getString("Resultado");
                    if (res.equals("Si"))
                    {
                        String cuenta = object.getString("Cuenta");
                        String suma = object.getString("Suma");
                        ratingTienda.setRating( (Float.parseFloat(suma) / Float.parseFloat(cuenta)) );
                        Toast.makeText(getContext(), "Has calificado esta tienda", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (tipoReg.equals("Cargar valor"))
                {
                    String valor = object.getString("Valor");
                    if (!valor.equals("null"))
                    {
                        String promedio = object.getString("Promedio");
                        userCal = true;
                        sw = true;

                        ratingTienda.setRating(Float.parseFloat(promedio));
                        ratingBarUsuario.setRating(Float.parseFloat(valor));
                        textViewCal.setText("Has calificado esta tienda");
                    }
                    else
                    {
                        sw = false;
                        userCal = false;
                        ratingTienda.setRating(0);
                        textViewCal.setText("Califica esta tienda");
                    }
                }
                else if (tipoReg.equals("Modificar calificacion"))
                {
                    String res = object.getString("Actualizado");
                    if (res.equals("Si"))
                    {
                        String cuenta = object.getString("Cuenta");
                        String suma = object.getString("Suma");

                        ratingTienda.setRating( (Float.parseFloat(suma) / Float.parseFloat(cuenta)) );

                        Toast.makeText(getContext(), "Has modificado la calificacion", Toast.LENGTH_SHORT).show();
                        textViewCal.setText("Has calificado esta tienda");
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
    }
}
