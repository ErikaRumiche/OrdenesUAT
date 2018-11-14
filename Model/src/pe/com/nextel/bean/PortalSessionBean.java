package pe.com.nextel.bean;

import java.io.Serializable;

import java.util.HashMap;

import pe.com.nextel.util.GenericObject;


public class PortalSessionBean extends GenericObject implements Serializable {
  private static final long serialVersionUID = 1L;
    private int    appId;
    private String sessionID;
    private int    personid;
    private String createdby; 
    private int    buildingid;
    private int    appid;
    private int    level;
    private String code; 
    private int userid;
    
    private String departamento;
    private int DeafultRolId;
    private String email;
    
    private int agentId;
    private int extension;
    private int userParentId;
    
    private int incidentGroupId;
    
    private String login; 
    //private String type;
    private String nom_user;
    private String partnerCodBscs;
    private String dn_Num;
    
    //private int busunitId;
    private int regionId;
    private int chnlPartnerId;
    private int providerGrpId;
    //private int territoryId;
    private int customerId;
    
    //private int rolId;
    private int areaId;
    private String areaName;
    private int vicepresidenciaId;
    private String defaultInBox;
    private int cashDeskId;
    private int idApp; 
    private String appName; 
    private String home;
    private HashMap objHshMap = new HashMap();
    
    private String message; 
    private String message_2; 
    
    private String strTienda; 
    private String strRegionTramite; 
    
    //Nuevas variables de sesión:
    //private int providerId;
    private int salesStructId;
    
    
    public PortalSessionBean() {
    }


    public void setTienda(String strTienda) {
           this.strTienda = strTienda;
       }
    public String getTienda() {
           return this.strTienda;
       }
       
    public void setRegionTramite(String strRegionTramite) {
           this.strRegionTramite = strRegionTramite;
       }
    public String getRegionTramite() {
           return this.strRegionTramite;
       } 
      
    public void setPersonid(int personid) {
           this.personid = personid;
       }
    public int getPersonid() {
           return this.personid;
       }
    
    public void setCreatedby(String createdby) {
           this.createdby = createdby;
       }
    public String getCreatedby() {
           return this.createdby;
       }   

    public void setBuildingid(int buildingid) {
           this.buildingid = buildingid;
       }
    public int getBuildingid() {
           return this.buildingid;
       } 
    public void setAppid(int appid) {
           this.appid = appid;
       }
    public int getAppid() {
           return this.appid;
       } 

    public void setLevel(int level) {
           this.level = level;
       }
    public int getLevel() {
           return this.level;
       }        
    
    public void setCode(String code) {
           this.code = code;
       }
    public String getCode() {
           return this.code;
       }  

    public void setUserid(int userid) {
           this.userid = userid;
       }
    public int getUserid() {
           return this.userid;
       }  

    public void setLogin(String login) {
           this.login = login;
       }
    public String getLogin() {
           return this.login;
       } 

    /*
    public void setType(String type) {
           this.type = type;
       }
    public String getType() {
           return this.type;
       } 
    */
       
    public void setNom_user(String nom_user) {
           this.nom_user = nom_user;
       }
       
    public String getNom_user() {
           return this.nom_user;
       } 
       
    /*
    public void setBusunitId(int busunitId) {
           this.busunitId = busunitId;
       }
       
    public int getBusunitId() {
           return this.busunitId;
       } 
    */

    public void setRegionId(int regionId) {
           this.regionId = regionId;
       }
       
    public int getRegionId() {
           return this.regionId;
       } 

    public void setChnlPartnerId(int chnlPartnerId) {
           this.chnlPartnerId = chnlPartnerId;
       }
       
    public int getChnlPartnerId() {
           return this.chnlPartnerId;
       } 

    public void setPartnerCodBscs(String partnerCodBscs) {
           this.partnerCodBscs = partnerCodBscs;
       }
       
    public String getPartnerCodBscs() {
           return this.partnerCodBscs;
       } 

    public void setProviderGrpId(int providerGrpId) {
           this.providerGrpId = providerGrpId;
       }
       
    public int getProviderGrpId() {
           return this.providerGrpId;
       } 

    /*
    public void setTerritoryId(int territoryId) {
           this.territoryId = territoryId;
       }
       
    public int getTerritoryId() {
           return this.territoryId;
       } 
    */

    public void setDn_Num(String dn_Num) {
           this.dn_Num = dn_Num;
       }
       
    public String getDn_Num() {
           return this.dn_Num;
       } 

    public void setCustomerId(int customerId) {
           this.customerId = customerId;
       }
       
    public int getCustomerId() {
           return this.customerId;
       } 

    /*
    public void setRolId(int rolId) {
           this.rolId = rolId;
       }
    public int getRolId() {
           return this.rolId;
       }
    */

    public void setAreaId(int areaId) {
           this.areaId = areaId;
       }
    public int getAreaId() {
           return this.areaId;
       }
       
    public void setAreaName(String areaName) {
           this.areaName = areaName;
       }
    public String getAreaName() {
           return this.areaName;
       }

    public void setVicepresidenciaId(int vicepresidenciaId) {
           this.vicepresidenciaId = vicepresidenciaId;
       }
    public int getVicepresidenciaId() {
           return this.vicepresidenciaId;
       }

    public void setDefaultInBox(String defaultInBox) {
           this.defaultInBox = defaultInBox;
       }
    public String getDefaultInBox() {
           return this.defaultInBox;
       }

    public void setCashDeskId(int cashDeskId) {
           this.cashDeskId = cashDeskId;
       }
    public int getCashDeskId() {
           return this.cashDeskId;
       }
       
    public void setIdApp(int idApp) {
           this.idApp = idApp;
       }
    public int getIdApp() {
           return this.idApp;
       }

    public void setAppName(String appName) {
           this.appName = appName;
       }
    public String getAppName() {
           return this.appName;
       }
       
    public void setHome(String home) {
           this.home = home;
       }
    public String getHome() {
           return this.home;
       }

    public void setMessage(String message) {
           this.message = message;
       }
    public String getMessage() {
           return this.message;
       }


    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public int getAppId() {
        return appId;
    }

    public void setObjHshMap(HashMap objHshMap){
        this.objHshMap = objHshMap;
    }

    public HashMap getObjHshMap(){
        return objHshMap;
    }


  public void setDepartamento(String departamento)
  {
    this.departamento = departamento;
  }


  public String getDepartamento()
  {
    return departamento;
  }


  public void setDeafultRolId(int DeafultRolId)
  {
    this.DeafultRolId = DeafultRolId;
  }


  public int getDeafultRolId()
  {
    return DeafultRolId;
  }


  public void setEmail(String email)
  {
    this.email = email;
  }


  public String getEmail()
  {
    return email;
  }


  public void setAgentId(int agentId)
  {
    this.agentId = agentId;
  }


  public int getAgentId()
  {
    return agentId;
  }


  public void setExtension(int extension)
  {
    this.extension = extension;
  }


  public int getExtension()
  {
    return extension;
  }


  public void setUserParentId(int userParentId)
  {
    this.userParentId = userParentId;
  }


  public int getUserParentId()
  {
    return userParentId;
  }


  public void setIncidentGroupId(int incidentGroupId)
  {
    this.incidentGroupId = incidentGroupId;
  }


  public int getIncidentGroupId()
  {
    return incidentGroupId;
  }


  /*public void setProviderId(int providerId)
  {
    this.providerId = providerId;
  }


  public int getProviderId()
  {
    return providerId;
  }*/


  public void setSalesStructId(int salesStructId)
  {
    this.salesStructId = salesStructId;
  }


  public int getSalesStructId()
  {
    return salesStructId;
  }
}
