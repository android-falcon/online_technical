package com.falconssoft.onlinetechsupport.Modle;

import android.util.Log;

import com.falconssoft.onlinetechsupport.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import static com.falconssoft.onlinetechsupport.LoginActivity.LOGIN_ID;
import static com.falconssoft.onlinetechsupport.LoginActivity.LOGIN_NAME;

public class CompaneyInfo {
    private String companyName;
    private String customerName;
    private String systemName;
    private String phoneNo;
    private String cheakInTime;
    private String state_company;
    private String systemId;
    private String EngId;
    private String EngName;
    private String Chechout;
    private String problem;
    private String serial;


    private String engId;

    private String transactionDate;
    private String convertFlag;
    private String callCenterName;



    private String transferFlag;
    private String transferToEngName;

    private String transferToEngId;
    private String transferReason;
    private String transferToSerial;
    private String originalSerial;

    private String holdReason;


    public CompaneyInfo() {
    }

    public String getEngId() {
        return EngId;
    }

    public void setEngId(String EngId) {
        this.EngId = EngId;
    }

    public String getEngName() {
        return EngName;
    }

    public void setEngName(String EngName) {
        this.EngName = EngName;
    }

    public String getChechout() {
        return Chechout;
    }

    public void setChechout(String Chechout) {
        this.Chechout = Chechout;
    }

    public String getproblem() {
        return problem;
    }

    public void setproblem(String problem) {
        this.problem = problem;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public String getState_company() {
        return state_company;
    }

    public void setState_company(String state_company) {
        this.state_company = state_company;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getConvertFlag() {
        return convertFlag;
    }

    public void setConvertFlag(String convertFlag) {
        this.convertFlag = convertFlag;
    }

    public String getCallCenterName() {
        return callCenterName;
    }

    public void setCallCenterName(String callCenterName) {
        this.callCenterName = callCenterName;
    }

    public String getTransferFlag() {
        return transferFlag;
    }

    public void setTransferFlag(String transferFlag) {
        this.transferFlag = transferFlag;
    }

    public String getTransferToEngName() {
        return transferToEngName;
    }

    public void setTransferToEngName(String transferToEngName) {
        this.transferToEngName = transferToEngName;
    }

    public String getTransferToEngId() {
        return transferToEngId;
    }

    public void setTransferToEngId(String transferToEngId) {
        this.transferToEngId = transferToEngId;
    }

    public String getTransferReason() {
        return transferReason;
    }

    public void setTransferReason(String transferReason) {
        this.transferReason = transferReason;
    }

    public String getTransferToSerial() {
        return transferToSerial;
    }

    public void setTransferToSerial(String transferToSerial) {
        this.transferToSerial = transferToSerial;
    }

    public String getOriginalSerial() {
        return originalSerial;
    }

    public void setOriginalSerial(String originalSerial) {
        this.originalSerial = originalSerial;
    }

    public String getHoldReason() {
        return holdReason;
    }

    public void setHoldReason(String holdReason) {
        this.holdReason = holdReason;
    }

    public JSONObject getData() throws JSONException {

        JSONObject obj = new JSONObject();
        obj.put("CUST_NAME", "'" + customerName + "'");
        obj.put("COMPANY_NAME", "'" + companyName + "'");
        obj.put("SYSTEM_NAME", "'" + systemName + "'");
        obj.put("PHONE_NO", "'" + phoneNo + "'");
        obj.put("CHECH_IN_TIME", "'" + cheakInTime + "'");
        obj.put("STATE", "'" + state_company + "'");// state for companey // 1 --> check in  // 2 ---> check out  0----> hold
        obj.put("ENG_NAME", "'" + EngName+ "'");
        obj.put("ENG_ID", "'" + EngId + "'");
        obj.put("SYS_ID", "'" + systemId + "'");
        obj.put("CHECH_OUT_TIME", "'00:00:00'");
        obj.put("PROBLEM", "'problem'");
        final String CallId = LoginActivity.sharedPreferences.getString(LOGIN_ID,"-1");
        final String CallName = LoginActivity.sharedPreferences.getString(LOGIN_NAME, "-1");
        Log.e("call_id2",""+CallId);
        obj.put("CALL_CENTER_ID", "'"+CallId+"'");
        obj.put("HOLD_TIME", "'"+"00:00:00"+"'");
        obj.put("DATE_OF_TRANSACTION", "'00/00/00'");
        obj.put("SERIAL", serial);
        obj.put("CALL_CENTER_NAME", "'" + CallName + "'");
        obj.put("TRANSFER_FLAG", "'0'");
        obj.put("ORGINAL_SERIAL", "'-2'");
        obj.put("HOLD_REASON", holdReason);



        return  obj;

    }

}
