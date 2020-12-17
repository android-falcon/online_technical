package com.falconssoft.onlinetechsupport.Modle;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustomerOnline {
//    {"CUSTOMER_INFO":[{"CUST_NAME":"U?U?U???","COMPANY_NAME":"time canter "
//            ,"SYSTEM_NAME":"Time Attendance System","PHONE_NO":"0796507712"
//            ,"CHECH_IN_TIME":"13:44:15","STATE":"1","ENG_ID":"19","SYS_ID":"2"
//            ,"CHECH_OUT_TIME":"00:00","PROBLEM":"\"bgg\"","HOLD_TIME":"14:17:22"
//            ,"DATE_OF_TRANSACTION":"09\/12\/2020","CALL_CENTER_ID":"20"
//            ,"SERIAL":"1570","ENG_NAME":"ahmad","CONVERT_STATE":"0","CALL_CENTER_NAME":"rema"
//            ,"TRANSFER_FLAG":"2","TRANSFER_TO_ENG_ID":null,"TRANSFER_TO_ENG_NAME":null
//            ,"TRANSFER_TO_SERIAL":null,"TRANSFER_RESON":null,"ORGINAL_SERIAL":"1569"
//            ,"HOLD_REASON":null,"CANCLE_REASON":null,"ROW_STATUS":"0","TEC_TYPE":"4"
//            ,"COMPANY_ID":"5880","ROW_UPDATE_ID":"-1","CHECK_OUT_LATITUDE":"31.9694705"
//            ,"CHECK_OUT_LONGITUDE":"35.9140855","VR_PIC":"null","LATITUDE":"31.9694705","LONGITUDE":"35.9140855"}]}

    @SerializedName("CUSTOMER_INFO")
    private List<CustomerOnline> onlineList ;

    private int isSavPic ;

    @SerializedName("CUST_NAME")
    private String customerName ;

    @SerializedName("COMPANY_NAME")
    private String companyName ;

    @SerializedName("COMPANY_ID")
    private String companyId ;

    @SerializedName("SYSTEM_NAME")
    private String systemName;

    @SerializedName("PHONE_NO")
    private String phoneNo;

    @SerializedName("CHECH_IN_TIME")
    private String cheakInTime;

    @SerializedName("STATE")
    private int customerState;// , -1 =>  log out ,0 => log in and check out , 1=> check in ,// break => 2

    @SerializedName("ENG_NAME")
    private String engineerName;

    @SerializedName("ENG_ID")
    private String engineerID;

    @SerializedName("SYS_ID")
    private String systemId;

    @SerializedName("CHECH_OUT_TIME")
    private String cheakOutTime;

    @SerializedName("PROBLEM")
    private String problem;

    @SerializedName("CALL_CENTER_ID")
    private String CallCenterId;

    @SerializedName("SERIAL")
    private String serial;

    @SerializedName("VR_PIC")
    private String visitReportImage;

    @SerializedName("LONGITUDE")
    private double longitudeIn;

    @SerializedName("LATITUDE")
    private double latitudeIn;

    @SerializedName("CHECK_OUT_LONGITUDE")
    private double longitudeOut;

    @SerializedName("CHECK_OUT_LATITUDE")
    private double latitudeOut;

//    @SerializedName("CHECK_OUT_LATITUDE")
    private int update;

    @SerializedName("DATE_OF_TRANSACTION")
    private String date;

    public CustomerOnline() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIsSavPic() {
        return isSavPic;
    }

    public void setIsSavPic(int isSavPic) {
        this.isSavPic = isSavPic;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public int getUpdate() {
        return update;
    }

    public void setUpdate(int update) {
        this.update = update;
    }

    public double getLongitudeOut() {
        return longitudeOut;
    }

    public void setLongitudeOut(double longitudeOut) {
        this.longitudeOut = longitudeOut;
    }

    public double getLatitudeOut() {
        return latitudeOut;
    }

    public void setLatitudeOut(double latitudeOut) {
        this.latitudeOut = latitudeOut;
    }

    public double getLongitudeIn() {
        return longitudeIn;
    }

    public void setLongitudeIn(double longitudeIn) {
        this.longitudeIn = longitudeIn;
    }

    public double getLatitudeIn() {
        return latitudeIn;
    }

    public void setLatitudeIn(double latitudeIn) {
        this.latitudeIn = latitudeIn;
    }

    public List<CustomerOnline> getOnlineList() {
        return onlineList;
    }

    public String getVisitReportImage() {
        return visitReportImage;
    }

    public void setVisitReportImage(String visitReportImage) {
        this.visitReportImage = visitReportImage;
    }

    public void setOnlineList(List<CustomerOnline> onlineList) {
        this.onlineList = onlineList;
    }

    public String getCallCenterId() {
        return CallCenterId;
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
