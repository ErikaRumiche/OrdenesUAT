package pe.com.nextel.ejb;

import java.rmi.RemoteException;

import java.sql.SQLException;

import java.util.HashMap;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import pe.com.nextel.dao.MassiveOrderDAO;


public class SEJBMassiveOrderBean implements SessionBean 
{  private SessionContext _context;

   MassiveOrderDAO        objMassiveOrderDAO          = null;
  public void ejbCreate()
  {
    objMassiveOrderDAO = new MassiveOrderDAO();
  }



  public void setSessionContext(SessionContext context) throws EJBException {
      _context = context;
  }

  public void ejbRemove() throws EJBException {
  System.out.println("[SEJBMassiveOrderBean][ejbRemove()]");
  }

  public void ejbActivate() throws EJBException {
  System.out.println("[SEJBMassiveOrderBean][ejbRemove()]");
  }

  public void ejbPassivate() throws EJBException {
  System.out.println("[SEJBMassiveOrderBean][ejbRemove()]");
  }
    
 
  public HashMap MassiveOrderDAOgetItemOrder(long npcustomerid, long npsiteid, long npspecification, long npcustomeridAcept) throws RemoteException, Exception, SQLException 
  {
       return objMassiveOrderDAO.getItemOrder(npcustomerid,npsiteid,npspecification,npcustomeridAcept);
  }

  public HashMap getServiceList(int iDivisionId, int iPlanId) throws  Exception, SQLException
  {
    return objMassiveOrderDAO.getServiceList(iDivisionId,iPlanId);
  }

  public HashMap getServiceListBySolution(int iDivisionId) throws  SQLException, Exception, RemoteException
  {
     return objMassiveOrderDAO.getServiceListBySolution(iDivisionId);
  }

  public HashMap getServiceItemListBySolution(int iSolutionId) throws  Exception,  RemoteException, SQLException
  {
    return objMassiveOrderDAO.getServiceItemListBySolution(iSolutionId);
  }

  public HashMap getPlanList() throws  Exception
  {
    return objMassiveOrderDAO.getPlanList();
  }

  public HashMap getCommercialService(long numContract) throws   RemoteException,  SQLException, Exception
  {
    return objMassiveOrderDAO.getCommercialService(numContract);
  }

  public HashMap getValidateByPhone(long npphone, long intSpecification, long nOrderid) throws  SQLException, Exception
  {
    return objMassiveOrderDAO.getValidateByPhone( npphone,intSpecification,nOrderid);
  }

}