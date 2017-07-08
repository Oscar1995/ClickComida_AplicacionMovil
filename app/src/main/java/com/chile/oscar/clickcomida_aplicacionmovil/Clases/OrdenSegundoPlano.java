package com.chile.oscar.clickcomida_aplicacionmovil.Clases;

/**
 * Created by Oscar on 08-07-2017.
 */

public class OrdenSegundoPlano
{
    public int getOrdenId() {
        return ordenId;
    }

    public void setOrdenId(int ordenId) {
        this.ordenId = ordenId;
    }

    public String getOrdenState() {
        return ordenState;
    }

    public void setOrdenState(String ordenState) {
        this.ordenState = ordenState;
    }

    int ordenId;
    String ordenState;

    public String getNameStore() {
        return nameStore;
    }

    public void setNameStore(String nameStore) {
        this.nameStore = nameStore;
    }

    String nameStore;

    public boolean isNotify() {
        return isNotify;
    }

    public void setNotify(boolean notify) {
        isNotify = notify;
    }

    boolean isNotify;
}
