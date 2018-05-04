package com.elvotra.clean.data.remote.model;

public class UserData {
    private int id;

    private String name;

    private String username;

    private String email;

    private UserAddress address;

    private String phone;

    private String website;

    private UserCompany company;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public UserAddress getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }

    public UserCompany getCompany() {
        return company;
    }
}