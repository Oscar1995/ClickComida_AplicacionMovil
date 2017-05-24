package com.chile.oscar.clickcomida_aplicacionmovil.Clases;

import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by Oscar on 25-04-2017.
 */

public class Validadores
{
    public boolean isLetter (String cadena)
    {
        boolean swL = false;
        boolean swN = false;
        boolean swFinal = false;

        int alcance = cadena.length();

        for (int i=0; i<alcance; i++)
        {
            char mC = cadena.charAt(i);
            if (mC == '0' || mC == '1' || mC == '2' || mC == '3' || mC == '4' || mC == '5' || mC == '6' || mC == '7' || mC == '8' || mC == '9')
            {
                swN = true;
            }
            else
            {
                swL = true;
            }
        }
        if (swN == false && swL == true)
        {
            swFinal = true;
        }
        else
        {
            swFinal = false;
        }
        return swFinal;
    }
    public boolean isNetDisponible(Context context)
    {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();
        return (actNetInfo != null && actNetInfo.isConnected());
    }

    public boolean CompararHoras(boolean tarde, Context context, String... horas)
    {
        boolean h1 = false, h2 = false;
        String[] hora_minutos = horas[0].split(":");
        int hora = Integer.parseInt(hora_minutos[0]);
        int minutos = Integer.parseInt(hora_minutos[1]);

        String[] hora_minutosContraparte = horas[1].split(":");
        int hora_ContraParte = Integer.parseInt(hora_minutosContraparte[0]);
        int minuto_ContraParte = Integer.parseInt(hora_minutosContraparte[1]);

        if (hora < hora_ContraParte)
        {
            h1 = true;
        }
        else if (hora == hora_ContraParte)
        {
            if (minutos == minuto_ContraParte)
            {
                if (tarde)
                {
                    Toast.makeText(context, "Mañana: Las horas y minutos no deben ser iguales.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(context, "Continuado: Las horas y minutos no deben ser iguales.", Toast.LENGTH_LONG).show();
                }
                h1 = false;
            }
            else if (minutos > minuto_ContraParte)
            {
                if (tarde)
                {
                    Toast.makeText(context, "Mañana: La hora de inicio debe ser menor a la hora de termino..", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(context, "Continuado: La hora de inicio debe ser menor a la hora de termino..", Toast.LENGTH_LONG).show();
                }
                h1 = false;
            }
            else if (minutos < minuto_ContraParte)
            {
                h1 = true;
            }
        }
        else if (hora > hora_ContraParte)
        {
            if (tarde)
            {
                Toast.makeText(context, "Mañana: La hora de inicio no debe ser mayor a la hora de termino.", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(context, "Continuado: La hora de inicio no debe ser mayor a la hora de termino.", Toast.LENGTH_LONG).show();
            }
            h1 = false;
        }
        if (tarde)
        {
            String[] hora_minutos_tarde = horas[2].split(":");
            int hora_tarde = Integer.parseInt(hora_minutos_tarde[0]);
            int minutos_tarde = Integer.parseInt(hora_minutos_tarde[1]);

            String[] hora_minutosContraparte_tarde = horas[3].split(":");
            int hora_ContraParte_tarde = Integer.parseInt(hora_minutosContraparte_tarde[0]);
            int minuto_ContraParte_tarde = Integer.parseInt(hora_minutosContraparte_tarde[1]);

            //Validacion para hora 1 de tarde
            if (hora_ContraParte < hora_tarde)
            {
                h2 = true;
            }
            else if (hora_ContraParte == hora_tarde)
            {
                if (minuto_ContraParte == minutos_tarde)
                {
                    Toast.makeText(context, "Tarde: Las horas y minutos no deben ser iguales.", Toast.LENGTH_LONG).show();
                    h2 = false;
                }
                else if (minuto_ContraParte > minutos_tarde)
                {
                    Toast.makeText(context, "Tarde: La hora de inicio debe ser menor a la hora de termino..", Toast.LENGTH_LONG).show();
                    h2 = false;
                }
                else if (minuto_ContraParte < minutos_tarde)
                {
                    h2 = true;
                }
            }
            else if (hora_ContraParte > hora_tarde)
            {
                Toast.makeText(context, "Tarde: La hora de inicio no debe ser mayor a la hora de termino.", Toast.LENGTH_LONG).show();
                h2 = false;
            }

            //Validacion para hora 2 de tarde
            if (hora_ContraParte < hora_ContraParte_tarde)
            {
                h2 = true;
            }
            else if (hora_ContraParte == hora_ContraParte_tarde)
            {
                if (minuto_ContraParte == minuto_ContraParte_tarde)
                {
                    Toast.makeText(context, "Tarde: Las horas y minutos no deben ser iguales.", Toast.LENGTH_LONG).show();
                    h2 = false;
                }
                else if (minuto_ContraParte > minuto_ContraParte_tarde)
                {
                    Toast.makeText(context, "Tarde: La hora de inicio debe ser menor a la hora de termino..", Toast.LENGTH_LONG).show();
                    h2 = false;
                }
                else if (minuto_ContraParte < minuto_ContraParte_tarde)
                {
                    h2 = true;
                }
            }
            else if (hora_ContraParte > hora_ContraParte_tarde)
            {
                Toast.makeText(context, "Tarde: La hora de inicio no debe ser mayor a la hora de termino.", Toast.LENGTH_LONG).show();
                h2 = false;
            }
        }
        if (tarde)
        {
            if (h1 && h2)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            if (h1)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }
}
