package com.chile.oscar.clickcomida_aplicacionmovil.Clases;

/**
 * Created by Oscar on 02-06-2017.
 */

public class Mapa
{
    int id;
    String nombre;
    String Descripcion;
    String Calle;
    String Numero;
    String Open_Hour;
    String Close_Hour;
    String Lunch_Hour;
    String Lunch_After_Hour;
    String Star_Day;
    String End_Day;
    Double Latitude;
    Double Longitude;
    int user_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getCalle() {
        return Calle;
    }

    public void setCalle(String calle) {
        Calle = calle;
    }

    public String getNumero() {
        return Numero;
    }

    public void setNumero(String numero) {
        Numero = numero;
    }

    public String getOpen_Hour() {
        return Open_Hour;
    }

    public void setOpen_Hour(String open_Hour) {
        Open_Hour = open_Hour;
    }

    public String getClose_Hour() {
        return Close_Hour;
    }

    public void setClose_Hour(String close_Hour) {
        Close_Hour = close_Hour;
    }

    public String getLunch_Hour() {
        return Lunch_Hour;
    }

    public void setLunch_Hour(String lunch_Hour) {
        Lunch_Hour = lunch_Hour;
    }

    public String getLunch_After_Hour() {
        return Lunch_After_Hour;
    }

    public void setLunch_After_Hour(String lunch_After_Hour) {
        Lunch_After_Hour = lunch_After_Hour;
    }

    public String getStar_Day() {
        return Star_Day;
    }

    public void setStar_Day(String star_Day) {
        Star_Day = star_Day;
    }

    public String getEnd_Day() {
        return End_Day;
    }

    public void setEnd_Day(String end_Day) {
        End_Day = end_Day;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

}
