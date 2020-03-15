package com.falconssoft.onlinetechsupport.Modle;

public class EngineerInfo {
    private  String name;
    private  String id;
    private  String password;
    private  String eng_type;

    public EngineerInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEng_type() {
        return eng_type;
    }

    public void setEng_type(String eng_type) {
        this.eng_type = eng_type;
    }
}
