package com.chile.oscar.clickcomida_aplicacionmovil.Clases;

/**
 * Created by Oscar on 25-04-2017.
 */

public class Validadores
{
    public boolean isLetter (String cadena)
    {
        boolean swL = false;
        boolean swN = false;
        boolean swFinal = false;

        int alcance = cadena.length();

        for (int i=0; i<alcance; i++)
        {
            char mC = cadena.charAt(i);
            if (mC == '0' || mC == '1' || mC == '2' || mC == '3' || mC == '4' || mC == '5' || mC == '6' || mC == '7' || mC == '8' || mC == '9')
            {
                swN = true;
            }
            else
            {
                swL = true;
            }
        }
        if (swN == false && swL == true)
        {
            swFinal = true;
        }
        else
        {
            swFinal = false;
        }
        return swFinal;
    }
}
