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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;

public class fragmentTiendaDos extends Fragment
{
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_fragment_tienda_dos, container, false);

        final RadioButton radioContinuado = (RadioButton)view.findViewById(R.id.rbtContinuado);
        final RadioButton radioTomorrow = (RadioButton)view.findViewById(R.id.rbtTomorrow);
        final TextView textViewUno = (TextView)view.findViewById(R.id.tvUno);
        final TextView textViewDos = (TextView)view.findViewById(R.id.tvDos);
        final TextView textViewHoraUno = (TextView)view.findViewById(R.id.tvUnoHora);
        final TextView textViewHoraDos = (TextView)view.findViewById(R.id.tvDosHora);
        final TextView textViewHoraTres = (TextView)view.findViewById(R.id.tvTresHora);
        final TextView textViewHoraCuatro = (TextView)view.findViewById(R.id.tvCuatroHora);
        final RelativeLayout rUno = (RelativeLayout)view.findViewById(R.id.rlUno);
        final RelativeLayout rDos = (RelativeLayout)view.findViewById(R.id.relative_Dos);

        OcultarRelative(rUno, rDos);

        radioContinuado.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (radioTomorrow.isChecked())
                {
                    radioTomorrow.setChecked(false);
                }
                rUno.setVisibility(View.VISIBLE);
                rDos.setVisibility(View.GONE);

                textViewUno.setText(getResources().getString(R.string.horario_continuado));
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
                rUno.setVisibility(View.VISIBLE);
                rDos.setVisibility(View.VISIBLE);

                textViewUno.setText(getResources().getString(R.string.tomorrow));
                textViewDos.setText(getResources().getString(R.string.tarde));
            }
        });
        textViewHoraUno.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                LlamarHora(textViewHoraUno, textViewHoraDos);

            }
        });
        textViewHoraDos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LlamarHora(textViewHoraDos, textViewHoraUno);
            }
        });
        textViewHoraTres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LlamarHora(textViewHoraTres, textViewHoraCuatro);
            }
        });
        textViewHoraCuatro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LlamarHora(textViewHoraCuatro, textViewHoraTres);
            }
        });

        return view;
    }
    private void OcultarRelative(RelativeLayout rUno, RelativeLayout rDos)
    {
        rUno.setVisibility(View.GONE);
        rDos.setVisibility(View.GONE);
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
