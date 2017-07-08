package com.chile.oscar.clickcomida_aplicacionmovil;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.ImageFormat;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Coordenadas;
import com.chile.oscar.clickcomida_aplicacionmovil.Clases.Validadores;

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
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;

public class fragmentTiendaDos extends Fragment
{
    boolean continuado, tomorrow, h1, h2, h3, h4, isContinuado;
    String[] dias;
    String imagen_cod, nombre_tienda, des_tienda, calle_tienda, numero_tienda, desPos, numPos, id_usuario, latitud, longitud;
    String diaInicio, diaFin, Hora1, Hora2, Hora3, Hora4;
    ProgressDialog progress;

    int posInicio = 0;
    int posFin = 0;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_fragment_tienda_dos, container, false);

        final RadioButton radioContinuado = (RadioButton)view.findViewById(R.id.rbtContinuado);
        final RadioButton radioTomorrow = (RadioButton)view.findViewById(R.id.rbtTomorrow);

        final Spinner sStart = (Spinner)view.findViewById(R.id.s_start);
        final Spinner sEnd = (Spinner)view.findViewById(R.id.s_end);

        final TextView textViewUno = (TextView)view.findViewById(R.id.tvUno);
        final TextView textViewDos = (TextView)view.findViewById(R.id.tvDos);
        final TextView textViewHoraUno = (TextView)view.findViewById(R.id.tvUnoHora);
        final TextView textViewHoraDos = (TextView)view.findViewById(R.id.tvDosHora);
        final TextView textViewHoraTres = (TextView)view.findViewById(R.id.tvTresHora);
        final TextView textViewHoraCuatro = (TextView)view.findViewById(R.id.tvCuatroHora);
        final LinearLayout fechaArriba = (LinearLayout)view.findViewById(R.id.llfecha_uno);
        final LinearLayout fechaAbajo = (LinearLayout)view.findViewById(R.id.llfecha_dos);
        final LinearLayout linearDias = (LinearLayout)view.findViewById(R.id.llDias);
        Button botonSiguiente = (Button)view.findViewById(R.id.btnContinuarDos);

        fechaArriba.setVisibility(View.GONE);
        fechaAbajo.setVisibility(View.GONE);
        linearDias.setVisibility(View.GONE);
        textViewUno.setVisibility(View.GONE);
        textViewDos.setVisibility(View.GONE);

        continuado = false;
        tomorrow = false;
        h1 = false;
        h2 = false;
        h3 = false;
        h4 = false;

        dias = new String[]{"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, dias);
        sStart.setAdapter(adapter);
        sEnd.setAdapter(adapter);


        sStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                posInicio = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                posFin = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        radioContinuado.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (radioTomorrow.isChecked())
                {
                    radioTomorrow.setChecked(false);
                }
                textViewUno.setText(getResources().getString(R.string.horario_continuado));

                textViewUno.setVisibility(View.VISIBLE);
                fechaArriba.setVisibility(View.VISIBLE);
                linearDias.setVisibility(View.VISIBLE);

                fechaAbajo.setVisibility(View.GONE);
                textViewDos.setVisibility(View.GONE);
                continuado = true;
                tomorrow = false;
                h3 = false;
                h4 = false;
            }
        });
        radioTomorrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (radioContinuado.isChecked())
                {
                    radioContinuado.setChecked(false);
                }
                textViewUno.setText(getResources().getString(R.string.tomorrow));
                textViewDos.setText(getResources().getString(R.string.tarde));

                textViewUno.setVisibility(View.VISIBLE);
                fechaArriba.setVisibility(View.VISIBLE);
                fechaAbajo.setVisibility(View.VISIBLE);
                textViewDos.setVisibility(View.VISIBLE);
                linearDias.setVisibility(View.VISIBLE);

                tomorrow = true;
                continuado = false;
            }
        });
        textViewHoraUno.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                LlamarHora(textViewHoraUno, textViewHoraDos);
                h1 = true;
            }
        });
        textViewHoraDos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LlamarHora(textViewHoraDos, textViewHoraUno);
                h2 = true;
            }
        });
        textViewHoraTres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LlamarHora(textViewHoraTres, textViewHoraCuatro);
                h3 = true;
            }
        });
        textViewHoraCuatro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LlamarHora(textViewHoraCuatro, textViewHoraTres);
                h4 = true;
            }
        });
        botonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (continuado && h1 && h2 || tomorrow && h1 && h2 && h3 & h4)
                {
                    FragmentTransaction trans = getFragmentManager().beginTransaction();
                    if (continuado && h1 && h2)
                    {
                        if (posInicio <= posFin)
                        {
                            if (new Validadores().CompararHoras(false, getContext(), textViewHoraUno.getText().toString(), textViewHoraDos.getText().toString()))
                            {
                                if (new Validadores().isNetDisponible(getContext()))
                                {
                                    isContinuado = true;
                                    diaInicio = sStart.getItemAtPosition(posInicio).toString();
                                    diaFin = sEnd.getItemAtPosition(posInicio).toString();
                                    Hora1 = textViewHoraUno.getText().toString() + ":00";
                                    Hora2 = textViewHoraDos.getText().toString() + ":00";
                                    RegistrarTienda(true, nombre_tienda);
                                    //new consultarTienda().execute(getResources().getString(R.string.direccion_web)+ "Controlador/insertar_tienda.php", newInstance(imagen_cod, nombre_tienda, des_tienda, calle_tienda, numero_tienda, desPos, numPos, textViewHoraUno.getText().toString() + ":00", textViewHoraDos.getText().toString() + ":00", "00:00:00", "00:00:00", sStart.getItemAtPosition(posInicio).toString(), sEnd.getItemAtPosition(posFin).toString(), id_usuario, latitud, longitud));
                                }
                                else
                                {
                                    Toast.makeText(getContext(), getResources().getString(R.string.debes_estar_conectado), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        else
                        {
                            Toast.makeText(getContext(), "El dia de inicio debe ser menor o igual al dia de termino.", Toast.LENGTH_SHORT).show();
                        }
                        //trans.replace(R.id.content_general, newInstance(true, imagen_cod, nombre_tienda, des_tienda, calle_tienda, numero_tienda, desPos, numPos, textViewHoraUno.getText().toString(), textViewHoraDos.getText().toString(), sStart.getItemAtPosition(posInicio).toString(), sEnd.getItemAtPosition(posFin).toString()));

                    }
                    else if (tomorrow && h1 && h2 && h3 & h4)
                    {
                        if (posInicio <= posFin)
                        {
                            if (new Validadores().CompararHoras(true, getContext(), textViewHoraUno.getText().toString(), textViewHoraDos.getText().toString(), textViewHoraTres.getText().toString(), textViewHoraCuatro.getText().toString()))
                            {
                                if (new Validadores().isNetDisponible(getContext()))
                                {
                                    isContinuado = false;
                                    diaInicio = sStart.getItemAtPosition(posInicio).toString();
                                    diaFin = sEnd.getItemAtPosition(posInicio).toString();
                                    Hora1 = textViewHoraUno.getText().toString() + ":00";
                                    Hora2 = textViewHoraDos.getText().toString() + ":00";
                                    Hora3 = textViewHoraTres.getText().toString() + ":00";
                                    Hora4 = textViewHoraCuatro.getText().toString() + ":00";
                                    RegistrarTienda(false, nombre_tienda);
                                    //new consultarTienda().execute(getResources().getString(R.string.direccion_web)+ "Controlador/insertar_tienda.php", newInstance(imagen_cod, nombre_tienda, des_tienda, calle_tienda, numero_tienda, desPos, numPos, textViewHoraUno.getText().toString() + ":00", textViewHoraDos.getText().toString() + ":00", textViewHoraTres.getText().toString() + ":00", textViewHoraCuatro.getText().toString() + ":00", sStart.getItemAtPosition(posInicio).toString(), sEnd.getItemAtPosition(posFin).toString(), id_usuario, latitud, longitud));
                                }
                                else
                                {
                                    Toast.makeText(getContext(), getResources().getString(R.string.debes_estar_conectado), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        else
                        {
                            Toast.makeText(getContext(), "El dia de inicio debe ser menor o igual al dia de termino.", Toast.LENGTH_SHORT).show();
                        }

                        //trans.replace(R.id.content_general, newInstance(false, imagen_cod, nombre_tienda, des_tienda, calle_tienda, numero_tienda, desPos, numPos, textViewHoraUno.getText().toString(), textViewHoraDos.getText().toString(), textViewHoraTres.getText().toString(), textViewHoraCuatro.getText().toString(), sStart.getItemAtPosition(posInicio).toString(), sEnd.getItemAtPosition(posFin).toString()));
                        //new consultarTienda().execute()

                    }
                    trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    trans.addToBackStack(null);
                    trans.commit();

                }
                else
                {
                    Toast.makeText(getContext(), "Debes elegir un horario de atencion y especificar la hora que trabajara tu tienda.", Toast.LENGTH_LONG).show();
                }

            }
        });

        return view;
    }
    public void RegistrarTienda(boolean aBooleanContinuado, String nombreTienda)
    {
        if (aBooleanContinuado)
        {
            new consultarTienda().execute(getResources().getString(R.string.direccion_web)+ "Controlador/insertar_tienda.php", newInstance(imagen_cod, nombreTienda, des_tienda, calle_tienda, numero_tienda, desPos, numPos, Hora1, Hora2, "00:00:00", "00:00:00", diaInicio, diaFin, id_usuario, latitud, longitud));
        }
        else
        {
            new consultarTienda().execute(getResources().getString(R.string.direccion_web)+ "Controlador/insertar_tienda.php", newInstance(imagen_cod, nombreTienda, des_tienda, calle_tienda, numero_tienda, desPos, numPos, Hora1, Hora2, Hora3, Hora4, diaInicio, diaFin, id_usuario, latitud, longitud));
        }

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
            desPos = getArguments().getString("DES_POSTULANTES");
            numPos = getArguments().getString("NUM_POSTULANTES");
            latitud = getArguments().getString("LATITUD");
            longitud = getArguments().getString("LONGITUD");
            id_usuario = getArguments().getString("ID_USUARIO");
        }
    }
    public String newInstance(String... params)
    {
        JSONObject object = new JSONObject();
        try
        {
            object.put("IMAGEN_COD", params[0]);
            object.put("NOMBRE_TIENDA", params[1]);
            object.put("DES_TIENDA", params[2]);
            object.put("CALLE_TIENDA", params[3]);
            object.put("NUMERO_TIENDA", params[4]);
            object.put("DES_POSTULANTES", params[5]);
            object.put("NUM_POSTULANTES", params[6]);
            object.put("HORA_1", params[7]);
            object.put("HORA_2", params[8]);
            object.put("HORA_3", params[9]);
            object.put("HORA_4", params[10]);
            object.put("DIA_INICIO", params[11]);
            object.put("DIA_FIN", params[12]);
            object.put("ID_USUARIO", params[13]);
            object.put("LATITUD", params[14]);
            object.put("LONGITUD", params[15]);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return object.toString();
    }

    private void LlamarHora(final TextView texViewHora, final TextView textDos)
    {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener()
        {
            public void onTimeSet(TimePicker view, int horadeldia, int minutos)
            {
                String horaEditado = "";
                String minutosEditado = "";
                if (minutos >=0 && minutos <= 9)
                {
                    minutosEditado = "0" + minutos;
                }
                else
                {
                    minutosEditado = minutos + "";
                }

                if (horadeldia >=0 && horadeldia <= 9)
                {
                    horaEditado = "0" + horadeldia;
                }
                else
                {
                    horaEditado = horadeldia + "";
                }

                String hora_dia = horaEditado + ":" + minutosEditado;
                String segundaHora = textDos.getText().toString();
                if (!segundaHora.equals(getResources().getString(R.string.horario_hasta)))
                {
                    segundaHora = textDos.getText().toString();
                }
                texViewHora.setText(hora_dia);
            }
        }, Calendar.HOUR, Calendar.MINUTE, false);
        timePickerDialog.show();
    }

    public class consultarTienda extends AsyncTask<String, Void, String>
    {
        @Override
        public String doInBackground(String... params)
        {
            HttpURLConnection conn = null;
            try
            {
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
                switch (responseCode)
                {
                    case 200:
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null)
                        {
                            response.append(inputLine);
                        }
                        in.close();
                        return response.toString();
                }
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
            finally
            {
                if (conn != null)
                {
                    try
                    {
                        conn.disconnect();
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
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
                JSONObject jsonResult = null;
                boolean sw = false;
                int result = s.indexOf("{\"Resultado\":\"Si\"}"); //Busca una palabra o simbolo dentro una cadena, retorna negativo si no lo encuentra, de lo contrario positivo si encuentra algo.
                if (result != -1)
                {
                    sw = true;
                    jsonResult = new JSONObject("{\"Resultado\":\"Si\"}");
                }
                else
                {
                    sw = false;
                    jsonResult = new JSONObject(s);
                }

                if (jsonResult != null)
                {
                    if (sw == true)
                    {
                        if (progress != null)
                        {
                            progress.dismiss();
                        }
                        Toast.makeText(getContext(), "Tu tienda ha sido creada, entra la pestaña llamada mis tiendas.", Toast.LENGTH_LONG).show();
                        FragmentTransaction trans = getFragmentManager().beginTransaction();
                        trans.replace(R.id.content_general, new MapaInicio());
                        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        trans.addToBackStack(null);
                        trans.commit();
                    }
                    else if (jsonResult.getString("Resultado").equals("1"))
                    {
                        Toast.makeText(getContext(), "El nombre de la tienda ya existe.", Toast.LENGTH_LONG).show();
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        final EditText edittext = new EditText(getContext());
                        alert.setMessage("La tienda \"" + nombre_tienda + "\" ya existe");
                        alert.setTitle("Escribe otro nombre...");

                        alert.setView(edittext);

                        alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                String editStore = edittext.getText().toString().trim();
                                if (editStore.isEmpty())
                                {
                                    edittext.setError("Debes colocar un nombre");
                                }
                                else
                                {
                                    if (editStore.length() >= 4)
                                    {
                                        RegistrarTienda(isContinuado, editStore);

                                        progress = new ProgressDialog(getContext());
                                        progress.setMessage("Registrando tienda...");
                                        progress.setCanceledOnTouchOutside(false);
                                        progress.show();
                                    }
                                    else
                                    {
                                        edittext.setError("El nombre de la tienda debe ser mayor o igual a cuatro caracteres");
                                    }
                                }
                            }
                        });

                        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                dialog.dismiss();
                            }
                        });

                        alert.show();
                    }
                    else if (jsonResult.getString("Resultado").equals("Error"))
                    {
                        Toast.makeText(getContext(), "Hay un error.", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "No trajo datos :(", Toast.LENGTH_SHORT).show();
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
