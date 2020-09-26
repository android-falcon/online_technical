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
    private String holdTime;
    private String callCenterId;
    private String serial;
    private String engId;

    private String transactionDate;
    private String convertFlag;
    private String callCenterName;

    private boolean showDetails;

    private String transferFlag;
    private String transferToEngName;

    private String transferToEngId;
    private String transferReason;
    private String transferToSerial;
    private String originalSerial;

    private String holdReason;

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

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public boolean isShowDetails() {
        return showDetails;
    }

    public void setShowDetails(boolean showDetails) {
        this.showDetails = showDetails;
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

    public String getHoldTime() {
        return holdTime;
    }

    public void setHoldTime(String holdTime) {
        this.holdTime = holdTime;
    }

    public String getCallCenterId() {
        return callCenterId;
    }

    public void setCallCenterId(String callCenterId) {
        this.callCenterId = callCenterId;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getEngId() {
        return engId;
    }

    public void setEngId(String engId) {
        this.engId = engId;
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
}
