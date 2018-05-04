package com.elvotra.clean.data.remote.model;

public class UserCompany {
    private String name;

    private String catchPhrase;

    private String bs;

    public UserCompany(String name, String catchPhrase, String bs) {
        this.name = name;
        this.catchPhrase = catchPhrase;
        this.bs = bs;
    }

    public String getName() {
        return name;
    }

    public String getCatchPhrase() {
        return catchPhrase;
    }

    public String getBs() {
        return bs;
    }
}
