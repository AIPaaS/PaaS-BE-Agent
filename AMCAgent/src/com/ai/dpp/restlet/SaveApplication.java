package com.ai.dpp.restlet;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class SaveApplication extends Application{

	@Override
	public Restlet createInboundRoot() {
		
		 Router router = new Router(getContext());
		 
	     //閿熸枻鎷烽敓鏂ゆ嫹婧� 
//	     router.attach("/rewrite" , SaveFileServerceResource.class);  
	     router.attach("/comp" , CompFileServerResource.class);  
	     router.attach("/dist" , DistributeServerResource.class);  
	     
	     return router;
	}
}
