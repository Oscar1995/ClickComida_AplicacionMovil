package com.chile.oscar.clickcomida_aplicacionmovil.Clases;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Oscar on 29-06-2017.
 */

public class BusquedaAvanzadaTiendas
{
    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getNumberStore() {
        return numberStore;
    }

    public void setNumberStore(int numberStore) {
        this.numberStore = numberStore;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNameStore() {
        return nameStore;
    }

    public void setNameStore(String nameStore) {
        this.nameStore = nameStore;
    }

    public String getDescriptionStore() {
        return descriptionStore;
    }

    public void setDescriptionStore(String descriptionStore) {
        this.descriptionStore = descriptionStore;
    }

    public String getStreetStore() {
        return streetStore;
    }

    public void setStreetStore(String streetStore) {
        this.streetStore = streetStore;
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }

    public String getOpenHour() {
        return openHour;
    }

    public void setOpenHour(String openHour) {
        this.openHour = openHour;
    }

    public String getCloseHour() {
        return closeHour;
    }

    public void setCloseHour(String closeHour) {
        this.closeHour = closeHour;
    }

    public String getLunchHour() {
        return lunchHour;
    }

    public void setLunchHour(String lunchHour) {
        this.lunchHour = lunchHour;
    }

    public String getLunchAfterHour() {
        return lunchAfterHour;
    }

    public void setLunchAfterHour(String lunchAfterHour) {
        this.lunchAfterHour = lunchAfterHour;
    }

    public LatLng getLatLngStore() {
        return latLngStore;
    }

    public void setLatLngStore(LatLng latLngStore) {
        this.latLngStore = latLngStore;
    }

    public float getRatingStore() {
        return ratingStore;
    }

    public void setRatingStore(float ratingStore) {
        this.ratingStore = ratingStore;
    }

    int storeId, numberStore, userId;
    float ratingStore;
    String nameStore;
    String descriptionStore;
    String streetStore;
    String startDay;
    String endDay;
    String openHour;
    String closeHour;
    String lunchHour;
    String lunchAfterHour;

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    String dateCreated;
    LatLng latLngStore;
}
