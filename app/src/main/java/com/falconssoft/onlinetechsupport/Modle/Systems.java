package com.falconssoft.onlinetechsupport.Modle;

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
}
