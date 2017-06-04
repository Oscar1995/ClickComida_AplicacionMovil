package com.chile.oscar.clickcomida_aplicacionmovil.Clases;

/**
 * Created by Oscar on 04-06-2017.
 */

public class Comentarios
{
    public String getuNickname() {
        return uNickname;
    }

    public void setuNickname(String uNickname) {
        this.uNickname = uNickname;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getuComentario() {
        return uComentario;
    }

    public void setuComentario(String uComentario) {
        this.uComentario = uComentario;
    }

    String uNickname, fechaCreacion, uComentario;
}
