package com.chile.oscar.clickcomida_aplicacionmovil.Clases;

/**
 * Created by Oscar on 06-06-2017.
 */

public class Favoritos_Tienda
{
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getStart_day() {
        return start_day;
    }

    public void setStart_day(String start_day) {
        this.start_day = start_day;
    }

    public String getEnd_day() {
        return end_day;
    }

    public void setEnd_day(String end_day) {
        this.end_day = end_day;
    }

    public String getOpen_hour() {
        return open_hour;
    }

    public void setOpen_hour(String open_hour) {
        this.open_hour = open_hour;
    }

    public String getClose_hour() {
        return close_hour;
    }

    public void setClose_hour(String close_hour) {
        this.close_hour = close_hour;
    }

    public String getLunch_open_hour() {
        return lunch_open_hour;
    }

    public void setLunch_open_hour(String lunch_open_hour) {
        this.lunch_open_hour = lunch_open_hour;
    }

    public String getLunch_after_hour() {
        return lunch_after_hour;
    }

    public void setLunch_after_hour(String lunch_after_hour) {
        this.lunch_after_hour = lunch_after_hour;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    String descripcion, calle, numero, start_day, end_day, open_hour, close_hour, lunch_open_hour, lunch_after_hour, user_id;
}
