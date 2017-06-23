package com.chile.oscar.clickcomida_aplicacionmovil.Clases;

/**
 * Created by Oscar on 22-06-2017.
 */

public class TiendaNotices
{
    String nomTienda, calleTienda, numTienda, fechaPublicacion, noticeRequerimiento;
    int noticeVacant;

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
