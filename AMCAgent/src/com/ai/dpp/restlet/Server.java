package com.ai.dpp.restlet;
/**
 * @author guofei
 */
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.restlet.Component;
import org.restlet.data.Protocol;

public class Server {

	private static final Log log = 
			LogFactory.getLog(Server.class);

	
	public static void main(String[] args) {
		try {
			
			Component comp = new Component();
			
			Properties prop = new Properties();
			
			FileInputStream inStream = null;
				
			inStream = new FileInputStream(new File(System.getenv("AIOSS_HOME") + "/conf/config.properties"));
			
			prop.load(inStream);
			
			int port = Integer.parseInt(prop.get("port").toString());
			
			comp.getServers().add(Protocol.HTTP, port);
	
			CmdApplication cmdApp = new CmdApplication();
			
			FileApplication fileApp = new FileApplication();
	
			TestApplication testApp = new TestApplication();
			
			comp.getDefaultHost().attach("/cmd",cmdApp);
			comp.getDefaultHost().attach("/file",fileApp);
			comp.getDefaultHost().attach("/test",testApp);
			
			comp.start();
			
			log.info("restlet service is already started...");
			
			
		} catch (Exception e) {
			
			throw new RuntimeException(e);
		}

	}

}
