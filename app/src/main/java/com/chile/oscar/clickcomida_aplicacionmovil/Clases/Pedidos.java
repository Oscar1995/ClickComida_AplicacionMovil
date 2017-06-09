package com.chile.oscar.clickcomida_aplicacionmovil.Clases;

import java.util.List;

/**
 * Created by Oscar on 08-06-2017.
 */

public class Pedidos
{
    int idStore;
    List<String> nombreProducto;

    public int getIdStore() {
        return idStore;
    }

    public void setIdStore(int idStore) {
        this.idStore = idStore;
    }

    public List<String> getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(List<String> nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public List<Integer> getPrecioProd() {
        return precioProd;
    }

    public void setPrecioProd(List<Integer> precioProd) {
        this.precioProd = precioProd;
    }

    List<Integer> precioProd;
}
