package com.chile.oscar.clickcomida_aplicacionmovil.Clases;

/**
 * Created by Oscar on 16-06-2017.
 */

public class PedidosRepartidor
{
    String pCalle;

    public String getpCalle() {
        return pCalle;
    }

    public void setpCalle(String pCalle) {
        this.pCalle = pCalle;
    }

    public String getpNumero() {
        return pNumero;
    }

    public void setpNumero(String pNumero) {
        this.pNumero = pNumero;
    }

    public String getuNombre() {
        return uNombre;
    }

    public void setuNombre(String uNombre) {
        this.uNombre = uNombre;
    }

    public String getuApellido() {
        return uApellido;
    }

    public void setuApellido(String uApellido) {
        this.uApellido = uApellido;
    }

    public String getEstado_Descripcion() {
        return estado_Descripcion;
    }

    public void setEstado_Descripcion(String estado_Descripcion) {
        this.estado_Descripcion = estado_Descripcion;
    }

    public String getFecha_Pedido() {
        return fecha_Pedido;
    }

    public void setFecha_Pedido(String fecha_Pedido) {
        this.fecha_Pedido = fecha_Pedido;
    }

    String pNumero;
    String uNombre;
    String uApellido;
    String estado_Descripcion;
    String fecha_Pedido;
}
