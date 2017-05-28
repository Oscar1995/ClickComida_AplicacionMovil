package com.chile.oscar.clickcomida_aplicacionmovil.Clases;

import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.chile.oscar.clickcomida_aplicacionmovil.R;

/**
 * Created by Oscar on 18-05-2017.
 */

public class MetodosCreados
{
    public String HoraNormal(String hora)
    {
        String[] hora_no_normal = hora.split(":");
        String alunch = hora_no_normal[0];
        String blunch = hora_no_normal[1];
        return (alunch + ":" + blunch);
    }
    public void LlamarHora(final TextView texViewHora, final Context context)
    {
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener()
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
                texViewHora.setText(hora_dia);
            }
        }, Calendar.HOUR, Calendar.MINUTE, false);
        timePickerDialog.show();
    }
}
