package com.ai.dpp.restlet;
/**
 * @author guofei
 */
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class CmdApplication extends Application
{

	@Override
	public Restlet createInboundRoot() 
	{
		
		 Router router = new Router(getContext());  
	     //绑定资源
	     router.attach("/" , CmdServerResource.class);  

	     return router;
	}

	
}
