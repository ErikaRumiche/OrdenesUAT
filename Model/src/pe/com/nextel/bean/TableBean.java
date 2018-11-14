package pe.com.nextel.bean;

import java.io.Serializable;

public class TableBean implements Serializable{
  private static final long serialVersionUID = 1L;
    private String npTable;
    private String npValue;
    private String npValueDesc;  
    
    
    public TableBean() {
    }
    
    public void setNpTable(String npTable) {
           this.npTable = npTable;
       }
    public String getNpTable() {
           return this.npTable;
       }  

    public void setNpValue(String npValue) {
           this.npValue = npValue;
       }
    public String getNpValue() {
           return this.npValue;
       }  
    public void setNpValueDesc(String npValueDesc) {
           this.npValueDesc = npValueDesc;
       }
    public String getNpValueDesc() {
           return this.npValueDesc;
       }  
    
}
