package pe.com.nextel.bean;

import java.io.Serializable;


public class CategoryBean implements Serializable{
  private static final long serialVersionUID = 1L;
    private String swType;
   
    public void setSwType(String swType) {
           this.swType = swType;
    }
    
    public String getSwType() {
           return this.swType;
    }  
  
}
