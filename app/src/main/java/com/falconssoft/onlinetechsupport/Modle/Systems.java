package com.falconssoft.onlinetechsupport.Modle;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Systems {

    @SerializedName("SYSTEMS")
    private List<Systems> systemsList;

    @SerializedName("SYSTEM_NAME")
    private String systemName;

    @SerializedName("SYSTEM_NO")
    private String systemNo;

    private double systemHour;
    private double systemCount;


    public Systems() {
    }

    public Systems(String systemName, String systemNo) {
        this.systemName = systemName;
        this.systemNo = systemNo;
    }

    public List<Systems> getSystemsList() {
        return systemsList;
    }

    public void setSystemsList(List<Systems> systemsList) {
        this.systemsList = systemsList;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemNo() {
        return systemNo;
    }

    public void setSystemNo(String systemNo) {
        this.systemNo = systemNo;
    }

    public double getSystemHour() {
        return systemHour;
    }

    public void setSystemHour(double systemHour) {
        this.systemHour = systemHour;
    }

    public double getSystemCount() {
        return systemCount;
    }

    public void setSystemCount(double systemCount) {
        this.systemCount = systemCount;
    }

    public JSONObject getData() throws JSONException {

        JSONObject obj = new JSONObject();

        obj.put("SYSTEM_NAME", "'" + systemName + "'");
        obj.put("SYSTEM_NO", "'" + systemNo + "'");


        return obj;


    }
}
