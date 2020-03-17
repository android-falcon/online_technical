package com.falconssoft.onlinetechsupport.Modle;

public class ManagerLayout {
    private String customerName ;
    private String companyName ;
    private String systemName;
    private String phoneNo;
    private String cheakInTime;
    private String cheakOutTime;
    private String EnginerName;
    private String State;
    private String systemId;
    private String Proplem;
    private String LOding;
    private String currentTime;


    public ManagerLayout() {

    }

    public ManagerLayout(String customerName, String companyName, String systemName, String phoneNo, String cheakInTime,
                         String cheakOutTime, String enginerName, String state, String systemId, String proplem) {
        this.customerName = customerName;
        this.companyName = companyName;
        this.systemName = systemName;
        this.phoneNo = phoneNo;
        this.cheakInTime = cheakInTime;
        this.cheakOutTime = cheakOutTime;
        this. EnginerName = enginerName;
        this.State = state;
        this.systemId = systemId;
        this.Proplem = proplem;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setCheakInTime(String cheakInTime) {
        this.cheakInTime = cheakInTime;
    }

    public void setCheakOutTime(String cheakOutTime) {
        this.cheakOutTime = cheakOutTime;
    }

    public void setEnginerName(String enginerName) {
        EnginerName = enginerName;
    }

    public void setState(String state) {
        State = state;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public void setProplem(String proplem) {
        Proplem = proplem;
    }

    public void setLOding(String LOding) {
        this.LOding = LOding;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getSystemName() {
        return systemName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getCheakInTime() {
        return cheakInTime;
    }

    public String getCheakOutTime() {
        return cheakOutTime;
    }

    public String getEnginerName() {
        return EnginerName;
    }

    public String getState() {
        return State;
    }

    public String getSystemId() {
        return systemId;
    }

    public String getProplem() {
        return Proplem;
    }

    public String getLOding() {
        return LOding;
    }

    public String getCurrentTime() {
        return currentTime;
    }
}
