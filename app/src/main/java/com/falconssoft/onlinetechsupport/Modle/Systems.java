package com.falconssoft.onlinetechsupport.Modle;

import org.json.JSONException;
import org.json.JSONObject;

public class Systems {
    private  String systemName;
    private  String systemNo;

    public Systems() {
    }

    public Systems(String systemName, String systemNo) {
        this.systemName = systemName;
        this.systemNo = systemNo;
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

    public JSONObject getData() throws JSONException {

        JSONObject obj = new JSONObject();

        obj.put("SYSTEM_NAME", "'"+systemName+"'");
        obj.put("SYSTEM_NO", "'"+systemNo+"'");


        return  obj;


    }
}
