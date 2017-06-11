package com.chile.oscar.clickcomida_aplicacionmovil.Clases;

/**
 * Created by Oscar on 10-06-2017.
 */

public class Pedidos_Proceso
{
    int orden_id;

    public int getOrden_id() {
        return orden_id;
    }

    public void setOrden_id(int orden_id) {
        this.orden_id = orden_id;
    }

    public String getOrden_fecha() {
        return orden_fecha;
    }

    public void setOrden_fecha(String orden_fecha) {
        this.orden_fecha = orden_fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombreTienda() {
        return nombreTienda;
    }

    public void setNombreTienda(String nombreTienda) {
        this.nombreTienda = nombreTienda;
    }

    String orden_fecha, estado, nombreTienda;
}
