package com.ai.dpp.restlet;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class FileApplication extends Application{

	@Override
	public Restlet createInboundRoot() {
		
		 Router router = new Router(getContext());
		 
	     //����Դ  
	     router.attach("/transfer" , SrcFileServerResource.class);  
	    // router.attach("/comp" , CompFileServerResource.class);  
	     //router.attach("/dist" , DistributeServerResource.class);  
	     
	     return router;
	}
}
