package com.elvotra.clean.data.remote.model;

public class UserAddress {
        private String street;

        private String suite;

        private String city;

        private String postcode;

        private AddressGeo geo;

        public UserAddress(String street, String suite, String city, String postcode, AddressGeo geo) {
            this.street = street;
            this.suite = suite;
            this.city = city;
            this.postcode = postcode;
            this.geo = geo;
        }

        public String getStreet() {
            return street;
        }

        public String getSuite() {
            return suite;
        }

        public String getCity() {
            return city;
        }

        public String getPostcode() {
            return postcode;
        }

        public AddressGeo getGeo() {
            return geo;
        }
    }

