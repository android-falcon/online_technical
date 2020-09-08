package com.falconssoft.onlinetechsupport.Modle;

public class CustomerOnline {

    private String customerName ;
    private String companyName ;
    private String systemName;
    private String phoneNo;
    private String cheakInTime;
    private int customerState;// , -1 =>  log out ,0 => log in and check out , 1=> check in ,// break => 2
    private String engineerName;
    private String engineerID;
    private String systemId;
    private String cheakOutTime;
    private String problem;
    private String CallCenterId;

    private String serial;


    public CustomerOnline() {
    }

    public String getCallId() {
        return CallCenterId;
    }

    public void setCallCenterId(String callCenterId) {
        this.CallCenterId = callCenterId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getCheakInTime() {
        return cheakInTime;
    }

    public void setCheakInTime(String cheakInTime) {
        this.cheakInTime = cheakInTime;
    }

    public int getCustomerState() {
        return customerState;
    }

    public void setCustomerState(int customerState) {
        this.customerState = customerState;
    }

    public String getEngineerName() {
        return engineerName;
    }

    public void setEngineerName(String engineerName) {
        this.engineerName = engineerName;
    }

    public String getEngineerID() {
        return engineerID;
    }

    public void setEngineerID(String engineerID) {
        this.engineerID = engineerID;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getCheakOutTime() {
        return cheakOutTime;
    }

    public void setCheakOutTime(String cheakOutTime) {
        this.cheakOutTime = cheakOutTime;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }
}
