package com.ai.dpp.restlet;
/**
 * @author guofei
 */
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class TestApplication extends Application{

	@Override
	public Restlet createInboundRoot() {
		
		 Router router = new Router(getContext());
		 
	     //°ó¶¨×ÊÔ´  
	     router.attach("/" , TestServerResource.class);  
	     
	     return router;
	}
}
