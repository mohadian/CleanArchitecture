package com.elvotra.clean.data.remote.model;

public class AddressGeo {
    private String lat;

    private String lng;

    public AddressGeo(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }
}
