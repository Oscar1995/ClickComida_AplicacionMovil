package com.chile.oscar.clickcomida_aplicacionmovil.Clases;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.chile.oscar.clickcomida_aplicacionmovil.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    public String quitarDosPuntos (String variable)
    {
        String[] x = variable.split(":");
        return x[1].trim();
    }
    public String ComprobarCampos (String v1, String v2)
    {
        if (!v1.isEmpty() && !v2.isEmpty())
        {
            if (v1.trim().equals(quitarDosPuntos(v2.trim())))
            {
                return null;
            }
            else if (v1.trim() != quitarDosPuntos(v2.trim()))
            {
                return v1.trim();
            }
        }
        else
        {
            if (!v1.trim().isEmpty() && v2.trim().isEmpty())
            {
                return v1.trim();
            }
            else if (v1.trim().isEmpty() || v2.trim().isEmpty())
            {
                return null;
            }
        }
        return null;
    }
    public RoundedBitmapDrawable RedondearBitmap (Bitmap bitmapOriginal, Resources resources)
    {
        if (bitmapOriginal.getWidth() > bitmapOriginal.getHeight())
        {
            bitmapOriginal = Bitmap.createBitmap(bitmapOriginal, 0, 0, bitmapOriginal.getHeight(), bitmapOriginal.getHeight());
        }
        else if (bitmapOriginal.getWidth() < bitmapOriginal.getHeight())
        {
            bitmapOriginal = Bitmap.createBitmap(bitmapOriginal, 0, 0, bitmapOriginal.getWidth(), bitmapOriginal.getWidth());
        }

        RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(resources, bitmapOriginal);
        roundedDrawable.setCornerRadius(bitmapOriginal.getWidth());
        return roundedDrawable;
    }
    public String formatearFecha (String fecha)
    {
        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date date = null;
        try
        {
            date = dateParser.parse(fecha);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        dateParser.applyPattern("EEEE, dd 'de' MMMM 'de' yyyy");
        String dateformat = dateParser.format(date);
        return dateformat;
    }
    public Bitmap resizeMapIcons(Bitmap iconName, int width, int height)
    {

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(iconName, width, height, false);
        return resizedBitmap;
    }
}
