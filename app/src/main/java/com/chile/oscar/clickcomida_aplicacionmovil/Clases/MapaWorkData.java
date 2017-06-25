package com.chile.oscar.clickcomida_aplicacionmovil.Clases;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Oscar on 24-06-2017.
 */

public class MapaWorkData
{
    public int getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(int store_id) {
        this.notice_id = store_id;
    }

    public int getTiendaVacantes() {
        return tiendaVacantes;
    }

    public void setTiendaVacantes(int tiendaVacantes) {
        this.tiendaVacantes = tiendaVacantes;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTiendaRequerimientos() {
        return tiendaRequerimientos;
    }

    public void setTiendaRequerimientos(String tiendaRequerimientos) {
        this.tiendaRequerimientos = tiendaRequerimientos;
    }

    public LatLng getLatLngCoordenadas() {
        return latLngCoordenadas;
    }

    public void setLatLngCoordenadas(LatLng latLngCoordenadas) {
        this.latLngCoordenadas = latLngCoordenadas;
    }

    int notice_id, tiendaVacantes;
    String fecha, tiendaRequerimientos, user_id;
    LatLng latLngCoordenadas;

}
