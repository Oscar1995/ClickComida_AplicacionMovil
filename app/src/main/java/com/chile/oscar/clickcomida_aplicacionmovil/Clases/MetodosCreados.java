package com.chile.oscar.clickcomida_aplicacionmovil.Clases;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
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
    public void MostrarMapa (SupportMapFragment map, final Context context)
    {
        map.getMapAsync(new OnMapReadyCallback()
        {
            @Override
            public void onMapReady(final GoogleMap googleMap)
            {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng)
                    {
                        googleMap.clear();
                        //LatLng posicionLocal = new LatLng(latLng.latitude, latLng.longitude);
                        googleMap.addMarker(new MarkerOptions().position(latLng).title("Marca"));
                        Coordenadas.latitud = latLng.latitude;
                        Coordenadas.longitud = latLng.longitude;
                    }
                });
            }
        });
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
}
