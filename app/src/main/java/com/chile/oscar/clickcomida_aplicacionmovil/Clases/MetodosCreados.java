package com.chile.oscar.clickcomida_aplicacionmovil.Clases;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.chile.oscar.clickcomida_aplicacionmovil.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.NOTIFICATION_SERVICE;

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
    public String formatearFecha2 (String fecha)
    {
        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-M-d");

        Date date = null;
        try
        {
            date = dateParser.parse(fecha);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        dateParser.applyPattern("yyyy-MM-dd");
        String dateformat = dateParser.format(date);
        return dateformat;
    }
    public String formatearFecha3 (String fecha)
    {
        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-M-d");

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
    public void MostrarNotificacion(Context context, Resources resources)
    {


        Intent miNotificacion = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.cl"));
        PendingIntent pending = PendingIntent.getActivity(context, 0, miNotificacion, 0);

        //Constriccion de la notificacion
        NotificationCompat.Builder constructor = new NotificationCompat.Builder(context);
        constructor.setSmallIcon(R.mipmap.ic_launcher);
        constructor.setContentIntent(pending);
        constructor.setAutoCancel(true);
        constructor.setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher));
        constructor.setContentTitle("El repartidor ya esta cerca");
        constructor.setContentText("Ver su posición");
        constructor.setSubText("Toca aqui para mas detalles");

        //Enviar la notificacion
        NotificationManager AdminNotificacion = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        AdminNotificacion.notify(1, constructor.build());

        MediaPlayer player = MediaPlayer.create(context, R.raw.motorep);
        player.start();
    }
    public int CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius=6371;//radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult= Radius*c;
        double km=valueResult/1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec =  Integer.valueOf(newFormat.format(km));
        double meter=valueResult%1000;
        int  meterInDec= Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value",""+valueResult+"   KM  "+kmInDec+" Meter   "+meterInDec);

        return meterInDec;
    }
    private Location getMyLocation(FragmentActivity activity)
    {
        LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (myLocation == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            String provider = lm.getBestProvider(criteria, true);
            myLocation = lm.getLastKnownLocation(provider);
        }
        return myLocation;
    }
    public String LlamarCalendario (Context context)
    {
        final String[] formatDate = {""};
        java.util.Calendar newCalendar = java.util.Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                formatDate[0] = year + "-" + (month + 1) + "-" + dayOfMonth;
            }
        }, newCalendar.get(java.util.Calendar.YEAR), newCalendar.get(java.util.Calendar.MONTH), newCalendar.get(java.util.Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
        return formatDate[0];
    }
}
