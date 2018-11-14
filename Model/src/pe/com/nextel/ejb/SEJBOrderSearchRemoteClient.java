package pe.com.nextel.ejb;

import javax.naming.Context;

import javax.rmi.PortableRemoteObject;

import pe.com.nextel.form.OrderSearchForm;
import pe.com.nextel.util.MiUtil;


public class SEJBOrderSearchRemoteClient 
{
	public static void main(String [] args)
	{
		SEJBOrderSearchRemoteClient sEJBOrderSearchRemoteClient = new SEJBOrderSearchRemoteClient();
		try
		{
			Context context = MiUtil.getInitialContext();
			SEJBOrderSearchRemoteHome sEJBOrderSearchRemoteHome = (SEJBOrderSearchRemoteHome)PortableRemoteObject.narrow(context.lookup("SEJBOrderSearch"), SEJBOrderSearchRemoteHome.class);
			SEJBOrderSearchRemote sEJBOrderSearchRemote;

			// Use one of the create() methods below to create a new instance
			sEJBOrderSearchRemote = sEJBOrderSearchRemoteHome.create();

			// Call any of the Remote methods below to access the EJB
			OrderSearchForm objOrderSearchForm = new OrderSearchForm();
			objOrderSearchForm.setIFlag(1);
			System.out.println(sEJBOrderSearchRemote);
			System.out.println(sEJBOrderSearchRemote.getOrderList( objOrderSearchForm ).size());
		}
		catch(Throwable ex)
		{
			ex.printStackTrace();
		}

	}
	/*
	private static Context getInitialContext() throws NamingException
	{
		Hashtable env = new Hashtable();
		// Oracle Application Server 10g connection details
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.evermind.server.rmi.RMIInitialContextFactory");
		env.put(Context.SECURITY_PRINCIPAL, "admin");
		env.put(Context.SECURITY_CREDENTIALS, "null");
		env.put(Context.PROVIDER_URL, "ormi://pelma1w3dap01.nextelperu.net:23791/apporderejb");

		return new InitialContext(env);
	}
	*/
}