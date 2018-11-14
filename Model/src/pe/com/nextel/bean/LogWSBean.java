package pe.com.nextel.bean;

import java.sql.Date;

/**
 * Created by HP on 11/04/2017.
 */
public class LogWSBean {
    private Integer source;
    private Long trxId;
    private String wsName;
    private String wsOperation;
    private String input;
    private String output;
    private Date startDate;
    private Date endDate;
    private Double timeOut;
    private Integer status;
    private String responseCode;
    private String responseMessage;
    private String createdBy;

    public LogWSBean() {}

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Long getTrxId() {
        return trxId;
    }

    public void setTrxId(Long trxId) {
        this.trxId = trxId;
    }

    public String getWsName() {
        return wsName;
    }

    public void setWsName(String wsName) {
        this.wsName = wsName;
    }

    public String getWsOperation() {
        return wsOperation;
    }

    public void setWsOperation(String wsOperation) {
        this.wsOperation = wsOperation;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Double timeOut) {
        this.timeOut = timeOut;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LogWSBean(Integer source,Long orderId, String wsName, String wsOperation, String input, String output, Date startDate, Date endDate, Double timeOut, Integer status, String responseCode, String responseMessage, String createdBy) {
        this.source=source;
        this.trxId = orderId;
        this.wsName = wsName;
        this.wsOperation = wsOperation;
        this.input = input;
        this.output = output;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timeOut = timeOut;
        this.status = status;
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.createdBy = createdBy;
    }
}
