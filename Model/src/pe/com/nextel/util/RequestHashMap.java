package pe.com.nextel.util;

import java.io.Serializable;

import java.util.HashMap;


public class RequestHashMap extends HashMap implements Serializable {
  
  public String[] getParameterValues(Object key) {
        String[] objectString = null;
        if ( get(key) == null ) 
           return null;
           
        if( get(key) instanceof String[] ){
          objectString = (String[])get(key);
          return objectString;
        }else{
          objectString = new String[1];
          objectString[0] = (String)get(key);
          return objectString;
        }
  }
  
  public String getParameter(Object key) {
        return (String)get(key);
  }
  
}