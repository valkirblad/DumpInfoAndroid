package com.example.dumpinfoandroid;

public class Content {

    public String number;
    public String name;
    public String address;
    public CharSequence date;
    public String body;
    public String protocol;
    public String type;
    public String duration;
    public String location;
    public String apps;
    public String IMEI1;
    public String IMEI2;
    public String IMSI;

    //1 elements
    private Content(String apps) {
        this.apps = apps;
    }

    static Content getApps(String apps) {
        return new Content(apps);
    }

    //2 elements
    private Content(String name, String number) {
        this.name = name;
        this.number = number;
    }

    static Content getContact(String name, String number) {
        return new Content(name, number);
    }

    //3 elements
    private Content(String IMEI1, String IMEI2, String IMSI) {
        this.IMEI1 = IMEI1;
        this.IMEI2 = IMEI2;
        this.IMSI = IMSI;
    }

    static Content getId(String IMEI1, String IMEI2, String IMSI) {
        return new Content(IMEI1, IMEI2, IMSI);
    }

    //4 elements
    private Content(String address, CharSequence date, String body, String protocol) {
        this.address = address;
        this.date = date;
        this.body = body;
        this.protocol = protocol;
    }

    static Content getSms(String address, CharSequence date, String body, String protocol) {
        return new Content(address, date, body, protocol);
    }

    //6 elements
    private Content(String name, String number, String type, CharSequence date, String duration, String location) {
        this.name = name;
        this.number = number;
        this.type = type;
        this.date = date;
        this.duration = duration;
        this.location = location;
    }

    static Content getCall(String name, String number, String type, CharSequence date, String duration, String location) {
        return new Content(name, number, type, date, duration, location);
    }
}
