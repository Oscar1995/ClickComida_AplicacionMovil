package com.chile.oscar.clickcomida_aplicacionmovil.Clases;

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
}
