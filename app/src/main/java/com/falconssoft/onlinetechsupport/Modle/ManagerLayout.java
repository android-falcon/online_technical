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

    public ManagerLayout() {

    }

    public ManagerLayout(String customerName, String companyName, String systemName,
                         String phoneNo, String cheakInTime, String cheakOutTime, String enginerName, String state) {
        this.customerName = customerName;
        this.companyName = companyName;
        this.systemName = systemName;
        this.phoneNo = phoneNo;
        this.cheakInTime = cheakInTime;
        this.cheakOutTime = cheakOutTime;
        EnginerName = enginerName;
        State = state;
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
}
