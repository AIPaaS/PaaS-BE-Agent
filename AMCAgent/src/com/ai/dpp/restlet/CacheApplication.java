package com.ai.dpp.restlet;
/**
 * @author guofei
 */
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class CacheApplication extends Application{

	@Override
	public Restlet createInboundRoot() {
		
		 Router router = new Router(getContext());
		 
	     //����Դ  
	     router.attach("/createRedisconf" , SrcCacheServerResource.class);  
	    // router.attach("/comp" , CompFileServerResource.class);  
	     //router.attach("/dist" , DistributeServerResource.class);  
	     
	     return router;
	}
}
