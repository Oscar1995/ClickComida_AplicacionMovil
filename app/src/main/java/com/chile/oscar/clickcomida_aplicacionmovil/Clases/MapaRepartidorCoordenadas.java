package com.chile.oscar.clickcomida_aplicacionmovil.Clases;

/**
 * Created by Oscar on 17-06-2017.
 */

public class MapaRepartidorCoordenadas
{
    int orden_id;
    String cCalle, cNumero, uName, uApellido;

    public int getOrden_id() {
        return orden_id;
    }

    public void setOrden_id(int orden_id) {
        this.orden_id = orden_id;
    }

    public String getcCalle() {
        return cCalle;
    }

    public void setcCalle(String cCalle) {
        this.cCalle = cCalle;
    }

    public String getcNumero() {
        return cNumero;
    }

    public void setcNumero(String cNumero) {
        this.cNumero = cNumero;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuApellido() {
        return uApellido;
    }

    public void setuApellido(String uApellido) {
        this.uApellido = uApellido;
    }

    public Double getdLatitude() {
        return dLatitude;
    }

    public void setdLatitude(Double dLatitude) {
        this.dLatitude = dLatitude;
    }

    public Double getdLongitud() {
        return dLongitud;
    }

    public void setdLongitud(Double dLongitud) {
        this.dLongitud = dLongitud;
    }

    Double dLatitude, dLongitud;
}
