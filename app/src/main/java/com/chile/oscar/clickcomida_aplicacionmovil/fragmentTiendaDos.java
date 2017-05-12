package com.chile.oscar.clickcomida_aplicacionmovil;

import android.app.TimePickerDialog;
import android.graphics.ImageFormat;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;

public class fragmentTiendaDos extends Fragment
{
    boolean continuado, tomorrow, h1, h2, h3, h4;
    String[] dias;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_fragment_tienda_dos, container, false);

        final RadioButton radioContinuado = (RadioButton)view.findViewById(R.id.rbtContinuado);
        final RadioButton radioTomorrow = (RadioButton)view.findViewById(R.id.rbtTomorrow);
        final TextView textViewUno = (TextView)view.findViewById(R.id.tvUno);

        Spinner sStart = (Spinner)view.findViewById(R.id.s_start);
        Spinner sEnd = (Spinner)view.findViewById(R.id.s_end);

        final TextView textViewDos = (TextView)view.findViewById(R.id.tvDos);
        final TextView textViewHoraUno = (TextView)view.findViewById(R.id.tvUnoHora);
        final TextView textViewHoraDos = (TextView)view.findViewById(R.id.tvDosHora);
        final TextView textViewHoraTres = (TextView)view.findViewById(R.id.tvTresHora);
        final TextView textViewHoraCuatro = (TextView)view.findViewById(R.id.tvCuatroHora);
        final LinearLayout fechaArriba = (LinearLayout)view.findViewById(R.id.llfecha_uno);
        final LinearLayout fechaAbajo = (LinearLayout)view.findViewById(R.id.llfecha_dos);
        Button botonSiguiente = (Button)view.findViewById(R.id.btnContinuarDos);

        fechaArriba.setVisibility(View.GONE);
        fechaAbajo.setVisibility(View.GONE);
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

                fechaAbajo.setVisibility(View.VISIBLE);
                textViewDos.setVisibility(View.VISIBLE);
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
                    trans.replace(R.id.content_general, new fragmentProductosVender());
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
    /*private void CompararHoras(String horaI, String HoraF, TextView txtUno, TextView txtDos)
    {
        String[] hora_minutos = horaI.split(":");
        int hora = Integer.parseInt(hora_minutos[0]);
        int minutos = Integer.parseInt(hora_minutos[1]);

        String[] hora_minutosContraparte = HoraF.split(":");
        int hora_ContraParte = Integer.parseInt(hora_minutosContraparte[0]);
        int minuto_ContraParte = Integer.parseInt(hora_minutosContraparte[1]);

        if (hora == hora_ContraParte)
        {
            if (minutos > minuto_ContraParte)
            {
                txtUno.setText("Debe ser menor de la hora: " + horaI);
            }
            else if (minutos == minuto_ContraParte)
            {
                txtDos.setText("Las horas no deben ser iguales.");
            }
        }
        else
        {
            if (hora > hora_ContraParte)
            {
                txtUno.setText("Este debe ser menor a la hora: " + HoraF);
            }
        }

    }*/
}
