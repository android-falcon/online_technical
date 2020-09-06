package com.falconssoft.onlinetechsupport.Modle;

import android.util.Log;

import com.falconssoft.onlinetechsupport.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import static com.falconssoft.onlinetechsupport.LoginActivity.LOGIN_ID;

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
        Log.e("call_id2",""+CallId);
        obj.put("CALL_CENTER_ID", "'"+CallId+"'");
        obj.put("HOLD_TIME", "'"+"00:00:00"+"'");
        obj.put("DATE_OF_TRANSACTION", "'00/00/00'");
        obj.put("SERIAL", serial);

        return  obj;

    }

}
