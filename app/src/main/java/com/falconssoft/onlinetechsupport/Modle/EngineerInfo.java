package com.falconssoft.onlinetechsupport.Modle;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EngineerInfo {
    private String name;
    private String id;
    private String password;
    private int eng_type;
    private int state;

    private double noOfCountCall;
    private double percCall;


    public EngineerInfo() {
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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

    public int getEng_type() {
        return eng_type;
    }

    public void setEng_type(int eng_type) {
        this.eng_type = eng_type;
    }

    public double getNoOfCountCall() {
        return noOfCountCall;
    }

    public void setNoOfCountCall(double noOfCountCall) {
        this.noOfCountCall = noOfCountCall;
    }

    public double getPercCall() {
        return percCall;
    }

    public void setPercCall(double percCall) {
        this.percCall = percCall;
    }

    public JSONObject getData() throws JSONException {

        JSONObject obj = new JSONObject();

            obj.put("ENG_NAME", "'"+name+"'");
            obj.put("ENG_ID", "'"+id+"'");
            obj.put("PASSWORD", "'"+password+"'");
            obj.put("ENG_TYPE", "'"+eng_type+"'");
            obj.put("STATE", "'"+state+"'");

        return  obj;


    }
}
