package com.chile.oscar.clickcomida_aplicacionmovil.Clases;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Oscar on 22-06-2017.
 */

public class TiendaNotices
{
    String nomTienda;
    String calleTienda;
    String numTienda;
    String fechaPublicacion;
    String noticeRequerimiento;
    String user_id;
    int noticeVacant;

    public LatLng getLatLngStore() {
        return latLngStore;
    }

    public void setLatLngStore(LatLng latLngStore) {
        this.latLngStore = latLngStore;
    }

    LatLng latLngStore;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public String getNomTienda() {
        return nomTienda;
    }

    public void setNomTienda(String nomTienda) {
        this.nomTienda = nomTienda;
    }

    public String getCalleTienda() {
        return calleTienda;
    }

    public void setCalleTienda(String calleTienda) {
        this.calleTienda = calleTienda;
    }

    public String getNumTienda() {
        return numTienda;
    }

    public void setNumTienda(String numTienda) {
        this.numTienda = numTienda;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getNoticeRequerimiento() {
        return noticeRequerimiento;
    }

    public void setNoticeRequerimiento(String noticeRequerimiento) {
        this.noticeRequerimiento = noticeRequerimiento;
    }

    public int getNoticeVacant() {
        return noticeVacant;
    }

    public void setNoticeVacant(int noticeVacant) {
        this.noticeVacant = noticeVacant;
    }

    public int getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(int noticeId) {
        this.noticeId = noticeId;
    }

    int noticeId;
}
